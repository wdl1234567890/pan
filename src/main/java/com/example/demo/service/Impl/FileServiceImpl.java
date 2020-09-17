package com.example.demo.service.Impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.File;
import com.example.demo.domain.FileExample;
import com.example.demo.domain.FileExample.Criteria;
import com.example.demo.domain.FileTreeNode;
import com.example.demo.enums.FileType;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.FileService;
import com.example.demo.utils.FileTreeNodeUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.PostSignatureRequest;
import com.obs.services.model.PostSignatureResponse;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;

public class FileServiceImpl implements FileService{

	@Autowired
	private ObsClient obsClient;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Value("${obs.config.maxLevel}")
	private Integer maxLevel;
	
	@Value("${obs.config.expires}")
	private Long expires;
	
	@Value("${obs.config.bucketName}")
	private String bucketName;
	
	
	private File checkArgs(List<Object> args, Integer fileId, Integer userId) {
		boolean anyMatch = args.stream().anyMatch(arg->{
			return null == arg;
		});
		if(anyMatch)throw new RuntimeException("参数为空！");
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new RuntimeException("文件/文件夹不存在，删除失败！");
		if(file.getCreatorId() != userId)throw new RuntimeException("当前用户无权限操作");
		return file;
	}
	
	private boolean isRepeat(File file) {
		FileExample fileExample = new FileExample();
		Criteria criteria = fileExample.createCriteria();
		criteria.andParentIdEqualTo(file.getParentId()).andNameEqualTo(file.getName()).andCreatorIdEqualTo(file.getCreatorId());
		List<File> files = fileMapper.selectByExample(fileExample);
		if(null == files || files.size() == 0)return false;
		return true;
	}
	
	private boolean isOverMaxLevel(Integer fileId, Integer maxLevel, Integer userId) {
		List<File> files = fileMapper.getByCreatorId(userId);
		if(null == files || files.size() == 0)throw new RuntimeException("当前用户还没有任何文件/文件夹！");
		Map<Integer, FileTreeNode> fileTree= FileTreeNodeUtils.createFileTree(files);
		Integer currentLevel = FileTreeNodeUtils.getLevelCountById(fileTree, fileId);
		if(currentLevel >= maxLevel)return true;
		return false;
		
		
	}
	
	
	@Transactional
	private boolean removeFileOrDir(Integer fileId, Integer userId) {
		File file = checkArgs(Arrays.asList(fileId, userId), fileId, userId);
		
		if(file.getType() == FileType.USER_FILE.value()) {
			
			int result = fileMapper.deleteByPrimaryKey(fileId);
			if(1 != result)throw new RuntimeException("数据路出错，删除失败！");
			
			DeleteObjectResult deleteObject = obsClient.deleteObject(bucketName, file.getObjectName());
			//TODO 对deleteObject进行判断
			
			
		}else if(file.getType() == FileType.USER_DIR.value()) {
			List<File> files = fileMapper.getByCreatorId(userId);
			if(null == files || files.size() == 0)throw new RuntimeException("当前用户还没有任何文件/文件夹！");
			Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
			List<Integer> ids = FileTreeNodeUtils.getIdsByParentId(fileTree, fileId);
			List<String> objectKeys = FileTreeNodeUtils.getObjectKeysByParentId(fileTree, fileId);
			boolean result = fileMapper.batchDeleteById(ids);
			if(!result)throw new RuntimeException("数据库出错，删除失败！");
			objectKeys.stream().forEach(objectKey->{
				DeleteObjectResult deleteObjectResult = obsClient.deleteObject(bucketName, objectKey);
				//TODO obs出错处理
			});
		}
		
		return true;
		
	}
	
	
	@Override
	public PostSignatureResponse getPostSignature() {
		PostSignatureRequest request = new PostSignatureRequest();
		request.setExpires(expires);
		return obsClient.createPostSignature(request);
		
	}

	@Override
	public boolean createDir(File file, Integer userId) {
		checkArgs(Arrays.asList(file, userId), file.getParentId(), userId);

		file.setCreateDay(new Date());
		file.setUpdateDay(new Date());
		file.setCreatorId(userId);
		file.setType(FileType.USER_DIR.value());
		
		if(isRepeat(file))throw new RuntimeException("文件夹已存在！");
		if(isOverMaxLevel(file.getParentId(), maxLevel, userId))throw new RuntimeException("已经超过最大层数，不能再新建文件夹！");
		
		int result = fileMapper.insert(file);
		if(1 != result)throw new RuntimeException("数据库出错，创建文件夹失败！");
		
		return true;
	}

	@Override
	public boolean createFile(File file, Integer userId) {
		
		checkArgs(Arrays.asList(file, userId), file.getParentId(), userId);
		
		file.setCreateDay(new Date());
		file.setUpdateDay(new Date());
		file.setCreatorId(userId);
		file.setType(FileType.USER_FILE.value());
		
		if(isRepeat(file))throw new RuntimeException("文件已存在！");
		
		int result = fileMapper.insert(file);
		if(1 != result)throw new RuntimeException("数据库出错，创建文件失败！");
		
		return true;
	}

	@Override
	public String getDownloadUrl(Integer fileId, Integer userId) {
		
		File file = checkArgs(Arrays.asList(fileId, userId), fileId, userId);
		if(file.getType() != FileType.USER_FILE.value())throw new RuntimeException("暂时不支持下载或分享文件夹！");
		
		TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expires);
		request.setBucketName("bucketname");
		request.setObjectKey("objectname");

		TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
		
		return response.getSignedUrl();
	}

	@Override
	@Transactional
	public boolean batchRemoveFileAndDir(List<Integer> ids, Integer userId) {
		
		checkArgs(Arrays.asList(ids, userId), null, null);
		
		ids.stream().forEach(id->{
			removeFileOrDir(id, userId);
		});
		
		return true;
	}

	@Override
	public boolean renameFileOrDir(File file, String newName, Integer userId) {
		
		File file1 = checkArgs(Arrays.asList(file, newName, userId), file.getId(), userId);
		file1.setName(newName);
		int result = fileMapper.updateByPrimaryKey(file1);
		if(1 != result)throw new RuntimeException("数据库出错，重命名失败！");
		
		return true;
	}

	@Override
	public String getShareUrl(Integer fileId, Integer userId) {
		
		return getDownloadUrl(fileId, userId);
	}

	@Override
	public List<File> getFileListByParentId(Integer parentId, Integer userId) {
		File file = checkArgs(Arrays.asList(parentId, userId), parentId, userId);
		if(file.getType() != FileType.USER_DIR.value())throw new RuntimeException("文件无法执行此操作！");
		
		List<File> files = fileMapper.selectByParentId(parentId);
		if(null == files || files.size() == 0) throw new RuntimeException("该文件夹为空！");
		
		return files;
	}

	@Override
	public Map<String, File> getFileListByName(String name, Integer parentId, Integer userId) {
		File file = checkArgs(Arrays.asList(name,parentId, userId), parentId, userId);
		if(file.getType() != FileType.USER_DIR.value())throw new RuntimeException("文件无法执行此操作！");
		
		List<File> files = fileMapper.getByCreatorId(userId);
		if(null == files || files.size() == 0)throw new RuntimeException("当前用户还没有任何文件/文件夹！");
		Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
		return FileTreeNodeUtils.getFilesByNameAndParentId(fileTree, parentId, name);
	}

	@Override
	public String getPathById(Integer fileId, Integer userId) {
		checkArgs(Arrays.asList(fileId, userId), fileId, userId);
		
		List<File> files = fileMapper.getByCreatorId(userId);
		if(null == files || files.size() == 0)throw new RuntimeException("当前用户还没有任何文件/文件夹！");
		Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
		String path = FileTreeNodeUtils.getPathById(fileTree, fileId);
		return path;
	}

}
