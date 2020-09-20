package com.example.demo.service.Impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Authority;
import com.example.demo.domain.File;
import com.example.demo.domain.FileExample;
import com.example.demo.domain.FileExample.Criteria;
import com.example.demo.domain.FileTreeNode;
import com.example.demo.enums.FileType;
import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.FileService;
import com.example.demo.utils.FileTreeNodeUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.PostSignatureRequest;
import com.obs.services.model.PostSignatureResponse;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;

@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private ObsClient obsClient;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Value("${obs.config.maxLevel}")
	private Integer maxLevel;
	
	@Value("${obs.config.expires}")
	private Long expires;
	
	@Value("${obs.config.bucketName}")
	private String bucketName;
	
	
	
	/**
	 * 
	 * @Title checkArgs
	 * @Description 检查参数是否为空，检查文件/文件夹是否存在，检查文件/文件夹是否是属于指定用户的
	 * @param args 需要检测值是否为空的参数列表
	 * @param fileId 文件/文件夹id，如果此项参数为空，就不对文件的存在性做检测
	 * @param userId 用户id， 如果此项参数为空，就不做文件/文件夹与用户是否匹配的检测
	 * @return 如果fileId不为空，并且检测都通过，则返回对应fileId的file;若fileId和userId都为空，则返回null，检测不通过则抛出异常
	 * @throws RuntimeException
	 */
	private File checkArgs(List<Object> args, Integer fileId, Integer userId) {
		
		//参数值判空
		boolean anyMatch = args.stream().anyMatch(arg->{
			return null == arg;
		});
		if(anyMatch)throw new RuntimeException("参数为空！");
		
		//文件存在性检测以及文件用户是否匹配的检测
		if(null != fileId) {
			File file = fileMapper.selectByPrimaryKey(fileId);
			if(null == file)throw new RuntimeException("文件/文件夹不存在！");
			if(0 != fileId && null != userId) {
				if(file.getCreatorId() != userId)throw new RuntimeException("当前用户无权限操作");
			}
			return file;
		}
		return null;
		
	}
	
	
	
	/**
	 * 
	 * @Title isRepeat
	 * @Description 描述这个方法的作用
	 * @param file 需要判断是否重复的文件参数
	 * @return 重复返回true,不重复返回false
	 */
	private boolean isRepeat(File file) {
		
		FileExample fileExample = new FileExample();
		Criteria criteria = fileExample.createCriteria();
		
		//在创建者的某个父文件夹下查找是否有相同名称的file
		criteria.andParentIdEqualTo(file.getParentId()).andNameEqualTo(file.getName()).andCreatorIdEqualTo(file.getCreatorId());
		List<File> files = fileMapper.selectByExample(fileExample);
		if(null == files || files.size() == 0)return false;
		return true;
	}
	
	
	
	/**
	 * 
	 * @Title isOverMaxLevel
	 * @Description 当前文件夹所处的层数是否大于等于最大可创建层数
	 * @param fileId 文件夹id
	 * @param maxLevel 最大的层数
	 * @param userId 用户id
	 * @return 大于等于最大可创建层数则返回true，否则返回false
	 * @throws RuntimeException
	 */
	private boolean isOverMaxLevel(Integer fileId, Integer maxLevel, Integer userId) {
		List<File> files = fileMapper.getByCreatorId(userId);
		if(null == files || files.size() == 0)throw new RuntimeException("当前用户还没有任何文件/文件夹！");
		
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file || file.getType() != FileType.USER_DIR.value())throw new RuntimeException("当前文件夹不存在！");
		
		Map<Integer, FileTreeNode> fileTree= FileTreeNodeUtils.createFileTree(files);
		
		//获取当前文件夹所处的层数
		Integer currentLevel = FileTreeNodeUtils.getLevelCountById(fileTree, fileId);
		
		if(currentLevel >= maxLevel)return true;
		return false;	
	}
	
	
	/**
	 * 
	 * @Title removeFileOrDir
	 * @Description 删除单个文件或文件夹
	 * @param fileId
	 * @param userId
	 * @return 删除成功则返回true，否则抛异常
	 * @throws RuntimeException
	 */
	@Transactional
	private boolean removeFileOrDir(Integer fileId, Integer userId) {
		File file = checkArgs(Arrays.asList(fileId, userId), fileId, userId);
	
		//删除文件
		if(file.getType() == FileType.USER_FILE.value()) {
			
			//数据库中删除
			int result = fileMapper.deleteByPrimaryKey(fileId);
			if(1 != result)throw new RuntimeException("数据路出错，删除失败！");
			
			//obs删除对应对象
			DeleteObjectResult deleteObject = obsClient.deleteObject(bucketName, file.getObjectName());
			//TODO 对deleteObject进行判断
			
			
		}else if(file.getType() == FileType.USER_DIR.value()) {
			//删除文件夹
			
			List<File> files = fileMapper.getByCreatorId(userId);
			if(null == files || files.size() == 0)throw new RuntimeException("当前用户还没有任何文件/文件夹！");
			
			
			Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
			
			//获取fileId对应的文件夹以及其下所有文件和文件夹的id
			List<Integer> ids = FileTreeNodeUtils.getIdsByParentId(fileTree, fileId);
			//获取fileId对应的文件夹以及其下所有文件的objectKey
			List<String> objectKeys = FileTreeNodeUtils.getObjectKeysByParentId(fileTree, fileId);
			
			//批量删除数据库中对应id的file
			boolean result = fileMapper.batchDeleteById(ids);
			if(!result)throw new RuntimeException("数据库出错，删除失败！");
			
			//删除obs中objectKeys对应的对象
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
		File parentFile = checkArgs(Arrays.asList(file, userId), file.getParentId(), userId);
		if(null != parentFile && parentFile.getType() != FileType.USER_DIR.value())throw new RuntimeException("文件下不能执行创建文件夹操作！");
		
		//为文件夹添加属性值
		file.setCreateDay(new Date());
		file.setUpdateDay(new Date());
		file.setCreatorId(userId);
		file.setType(FileType.USER_DIR.value());
		
		//检测文件夹是否已经存在
		if(isRepeat(file))throw new RuntimeException("文件夹已存在！");
		
		//判断是否已经到达新建文件夹的最大层数
		if(isOverMaxLevel(file.getParentId(), maxLevel, userId))throw new RuntimeException("已经超过最大层数，不能再新建文件夹！");
		
		int result = fileMapper.insert(file);
		if(1 != result)throw new RuntimeException("数据库出错，创建文件夹失败！");
		
		return true;
	}

	@Override
	public boolean createFile(File file, Integer userId) {
		
		File parentFile = checkArgs(Arrays.asList(file, userId), file.getParentId(), userId);
		if(null != parentFile && parentFile.getType() != FileType.USER_DIR.value())throw new RuntimeException("文件下不能执行上传文件操作！");
		
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
		request.setBucketName(bucketName);
		request.setObjectKey(file.getObjectName());

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
	public List<File> getDirAndFileListByParentId(Integer parentId, Integer userId) {
		File file = checkArgs(Arrays.asList(parentId, userId), parentId, userId);
		if(file.getType() != FileType.USER_DIR.value())throw new RuntimeException("文件无法执行此操作！");
		
		List<File> files = fileMapper.selectByParentId(parentId, userId);
		if(null == files || files.size() == 0) throw new RuntimeException("该文件夹为空！");
		
		return files;
	}

	@Override
	public Map<String, File> getDirAndFileListByName(String name, Integer parentId, Integer userId) {
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



	@Override
	public Map<File, Authority> getGroupDirAndFileListByParentId(Integer parentId, Integer userId) {
		File file = checkArgs(Arrays.asList(parentId, userId), parentId, userId);
		if(null == file || file.getType() != FileType.GROUP_DIR.value())throw new RuntimeException("该文件/文件夹不能能执行此操作1！");
		
		//TODO 检查userId是否有权限
		
		//如果parentId是根目录
		if(0 == parentId) {
			
		}else {
			
		}
		return null;
	}

	

}
