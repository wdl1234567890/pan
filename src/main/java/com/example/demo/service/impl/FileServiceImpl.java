package com.example.demo.service.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Authority;
import com.example.demo.domain.File;
import com.example.demo.domain.FileExample;
import com.example.demo.domain.FileExample.Criteria;
import com.example.demo.domain.FileTreeNode;
import com.example.demo.domain.User;
import com.example.demo.enums.AuthorityType;
import com.example.demo.enums.FileType;
import com.example.demo.enums.StatusCode;
import com.example.demo.enums.UserLevel;
import com.example.demo.exception.PanException;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.FileService;
import com.example.demo.service.ObsService;
import com.example.demo.utils.FileTreeNodeUtils;
import com.example.demo.utils.ToolUtils;
import com.example.demo.vo.GroupDirOrFileInfos;
import com.obs.services.model.PostSignatureResponse;



@Service
public class FileServiceImpl implements FileService{
	
	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private ObsService obsService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Value("${obs.config.maxLevel}")
	private Integer maxLevel;
	
	@Value("${obs.config.accessKey}")
	String ak;
	
	@Value("${obs.config.shareUrl}")
	String shareUrl;
	
	
	/**
	 * 
	 * @Title checkArgs
	 * @Description 检查参数是否为空，检查文件/文件夹是否存在，检查文件/文件夹是否是属于指定用户的
	 * @param args 需要检测值是否为空的参数列表
	 * @param fileId 文件/文件夹id，如果此项参数为空，就不对文件的存在性做检测
	 * @param userId 用户id， 如果此项参数为空，就不做文件/文件夹与用户是否匹配的检测
	 * @return 如果fileId不为空，并且检测都通过，则返回对应fileId的file;若fileId和userId都为空，则返回null，检测不通过则抛出异常
	 * @throws PanException
	 */
	private File checkArgs(List<Object> args, Integer fileId, Integer userId) {
		
		//参数值判空
		boolean anyMatch = args.stream().anyMatch(arg->{
			return null == arg;
		});
		if(anyMatch)throw new PanException(StatusCode.PARAM_IS_EMPTY.code(), StatusCode.PARAM_IS_EMPTY.message());
		
		//文件存在性检测以及文件用户是否匹配的检测
		if(null != fileId) {
			File file = fileMapper.selectByPrimaryKey(fileId);
			if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
			if(0 != fileId && null != userId) {
				if(!file.getCreatorId().equals(userId))throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
			}
			return file;
		}
		return null;
		
	}
	
	
	
	/**
	 * 
	 * @Title isRepeat
	 * @Description 检查文件/文件夹是否已经存在
	 * @param file 需要判断是否重复的文件参数
	 * @return 重复返回重复的文件,不重复返回null
	 */
	private File isRepeat(File file) {
		
		FileExample fileExample = new FileExample();
		Criteria criteria = fileExample.createCriteria();
		
		List<File> files = null;
		
		//获取当前文件/文件夹的类型
		Integer type = file.getType();
		
		//如果是个人云盘下的内容，则在创建者的某个父文件夹下查找是否有相同名称的file；如果是群组云盘，则在某个父文件夹下查找是否有相同名称的file
		if(type == FileType.USER_DIR.value() || type == FileType.USER_FILE.value()) {
			criteria.andParentIdEqualTo(file.getParentId()).
			andNameEqualTo(file.getName())
			.andCreatorIdEqualTo(file.getCreatorId())
			.andTypeIn(Arrays.asList(FileType.USER_DIR.value(), FileType.USER_FILE.value()));
			files = fileMapper.selectByExample(fileExample);
	
		}else {
			criteria.andParentIdEqualTo(file.getParentId()).
			andNameEqualTo(file.getName())
			.andTypeIn(Arrays.asList(FileType.GROUP_DIR.value(), FileType.GROUP_FILE.value()));
			files = fileMapper.selectByExample(fileExample);
			
		}
		
		if(null == files || files.size() == 0)return null;
		return files.get(0);
		
	}
	
	
	
	/**
	 * 
	 * @Title isOverMaxLevel
	 * @Description 当前文件夹所处的层数是否大于等于最大可创建层数
	 * @param fileId 文件夹id
	 * @param maxLevel 最大的层数
	 * @param userId 用户id
	 * @return 大于等于最大可创建层数则返回true，否则返回false
	 * @throws PanException
	 */
	private boolean isOverMaxLevel(Integer fileId, Integer maxLevel, Integer userId) {

		File file = fileMapper.selectByPrimaryKey(fileId);
		
		//获取文件夹的类型
		Integer type = file.getType();
		
		if(null == file || (type != FileType.USER_DIR.value() && type != FileType.GROUP_DIR.value()))throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		
		
		List<File> files = null;
		
		//如果是个人云盘，则获取个人的所有文件和文件夹；如果是群组云盘，则获取所有的群文件和文件夹
		if(type == FileType.USER_DIR.value() || type == FileType.USER_FILE.value()) {
			files = fileMapper.getByCreatorId(userId);
		}else if(type == FileType.GROUP_DIR.value() || type == FileType.GROUP_FILE.value()){
			files = fileMapper.getGroupFileAndDir();
		}
		
		if(null == files || files.size() == 0)return false;
		
		Map<Integer, FileTreeNode> fileTree= FileTreeNodeUtils.createFileTree(files);
		
		//获取当前文件夹所处的层数
		Integer currentLevel = FileTreeNodeUtils.getLevelCountById(fileTree, fileId);
		
		if(currentLevel >= maxLevel)return true;
		return false;	
	}
	
	/**
	 * 
	 * @Title getFileByObjectName
	 * @Description 描述这个方法的作用
	 * @param @param objectName
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	public File getFileByObjectName(String objectName) {
		
		FileExample example = new FileExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andObjectNameEqualTo(objectName);
		List<File> list = fileMapper.selectByExample(example);
		if(null == list || list.size() == 0)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		return list.get(0);
	}
	
	
	/**
	 * 
	 * @Title addDir
	 * @Description 描述这个方法的作用
	 * @param @param file
	 * @param @param userId
	 * @param @param type
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	private boolean addDir(File file, Integer userId, Integer type) {
		//为文件夹添加属性值
		file.setCreateDay(new Date());
		file.setUpdateDay(new Date());
		file.setCreatorId(userId);
		file.setType(type);
				
		//检测文件夹是否已经存在
		if(isRepeat(file) != null)throw new PanException(StatusCode.FILE_IS_EXISTED.code(), StatusCode.FILE_IS_EXISTED.message());
				
		//判断是否已经到达新建文件夹的最大层数
		if(isOverMaxLevel(file.getParentId(), maxLevel, userId))throw new PanException(StatusCode.IS_OVER_DIR_MAX_LEVEL.code(), StatusCode.IS_OVER_DIR_MAX_LEVEL.message());
				
		int result = fileMapper.insert(file);
		if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		return true;
	}
	
	/**
	 * 
	 * @Title addFile
	 * @Description 描述这个方法的作用
	 * @param @param file
	 * @param @param userId
	 * @param @param type
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	private boolean addFile(File file, Integer userId, Integer type) {
		file.setCreateDay(new Date());
		file.setUpdateDay(new Date());
		file.setCreatorId(userId);
		file.setType(type);
		File file2 = isRepeat(file);
		if(file2 != null) {
			obsService.createObsClicent();
			obsService.deleteObsject(file2.getObjectName());
			obsService.closeObsClient();
			file2.setObjectName(file.getObjectName());
			file2.setUpdateDay(new Date());
			file2.setCreatorId(userId);
			fileMapper.updateByPrimaryKey(file2);
			file.setId(file2.getId());
			return true;
		}
		
		int result = fileMapper.insert(file);
		if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		return true;
	}
	
	
	/**
	 * 
	 * @Title downloadFile
	 * @Description 描述这个方法的作用
	 * @param @param file
	 * @param @param request
	 * @param @param response 参数说明
	 * @return 返回说明
	 * @throws
	 */
	private void downloadFile(File file, HttpServletRequest request, HttpServletResponse response) {
		obsService.createObsClicent();
		InputStream inputStream = obsService.getObsObject(file.getObjectName());
		BufferedOutputStream outputStream = null;
		String fileName = file.getName();
		try {
			outputStream = new BufferedOutputStream(response.getOutputStream());
			
			// 为防止 文件名出现乱码
            final String userAgent = request.getHeader("USER-AGENT");
            // IE浏览器
            if (userAgent.contains("MSIE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                // google,火狐浏览器
                if (userAgent.contains("Mozilla")) {
                    fileName = new String(fileName.getBytes(), "ISO8859-1");
                } else {
                    // 其他浏览器
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                }
            }
			
			//response.setContentType("application/x-download");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			throw new PanException(StatusCode.DEFAULT_ERROR.code(), e.getMessage());
		}finally{
			try {
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				throw new PanException(StatusCode.DEFAULT_ERROR.code(), e.getMessage());
			}
			obsService.closeObsClient();
		}
	}
	
	
	private boolean renameDirOrFile(File file, String name) {
		file.setName(name);
		//检测新名称是否重名
		if(isRepeat(file) != null)throw new PanException(StatusCode.FILE_IS_EXISTED.code(), StatusCode.FILE_IS_EXISTED.message());
		int result = fileMapper.updateByPrimaryKey(file);
		if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		return true;
	}
	
	
	/**
	 * 
	 * @Title getShareUrl
	 * @Description 描述这个方法的作用
	 * @param @param file
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	private String getShareUrl(File file) {
		return shareUrl + file.getObjectName();
	}
	
	
	/**
	 * 
	 * @Title checkAuth
	 * @Description 描述这个方法的作用
	 * @param @param file
	 * @param @param user
	 * @param @param auth
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	private Authority checkAuth(File file, User user, AuthorityType auth) {
		Authority currentAuthority = authorityService.getCurrentAuthority(file.getId(), user.getDepartment());
		Integer authByType = currentAuthority.getAuthByType(auth);
		if(authByType == AuthorityType.UNAUTHORIZED.value())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		return currentAuthority;
	}
	
	
	/**
	 * 
	 * @Title removeFileOrDir
	 * @Description 删除单个文件或文件夹
	 * @param fileId
	 * @param userId
	 * @return 删除成功则返回true，否则抛异常
	 * @throws PanException
	 */
	@Transactional
	private boolean removeFileOrDir(Integer fileId, Integer userId) {
		File file = checkArgs(Arrays.asList(fileId), fileId, userId);
		if(file == null)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
	
		//删除文件
		if(file.getType() == FileType.USER_FILE.value() || file.getType() == FileType.GROUP_FILE.value()) {
			
			//数据库中删除
			int result = fileMapper.deleteByPrimaryKey(fileId);
			if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
			
			//obs删除对应对象
			boolean deleteResult = obsService.deleteObsject(file.getObjectName());
			if(!deleteResult)return false;
			
			
		}else if(file.getType() == FileType.USER_DIR.value() || file.getType() == FileType.GROUP_DIR.value()) {
			//删除文件夹
			
			List<File> files = null;
			if(file.getType() == FileType.USER_DIR.value())files = fileMapper.getByCreatorId(userId);
			else files = fileMapper.getGroupFileAndDir();
				
			Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
			
			//获取fileId对应的文件夹以及其下所有文件和文件夹的id
			List<Integer> ids = FileTreeNodeUtils.getIdsByParentId(fileTree, fileId);
			//获取fileId对应的文件夹以及其下所有文件的objectKey
			List<String> objectKeys = FileTreeNodeUtils.getObjectKeysByParentId(fileTree, fileId);
			
			//批量删除数据库中对应id的file
			boolean result = fileMapper.batchDeleteById(ids);
			if(!result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
			
			//删除obs中objectKeys对应的对象
			objectKeys.stream().forEach(objectKey->{
				obsService.deleteObsject(objectKey);		
			});
		}
		
		return true;
		
	}
	
	
	@Override
	public Map<String, Object> getUploadParam(){
		obsService.createObsClicent();
		PostSignatureResponse postSignature = obsService.getPostSignature();
		obsService.closeObsClient();
		Map<String, Object> data = new HashMap<>();
		data.put("ak", ak);
		data.put("policy", postSignature.getPolicy());
		data.put("signature", postSignature.getSignature());
		data.put("objectKey",UUID.randomUUID().toString().replaceAll("-", ""));
		return data;
	}

	@Override
	public boolean createDir(File file, Integer userId) {
		File parentFile = checkArgs(Arrays.asList(file, userId), file.getParentId(), userId);
		if(null != parentFile && parentFile.getType() != FileType.USER_DIR.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		return addDir(file, userId, FileType.USER_DIR.value());
	}

	@Override
	public boolean createFile(File file, Integer userId) {
		
		File parentFile = checkArgs(Arrays.asList(file, userId), file.getParentId(), userId);
		if(null != parentFile && parentFile.getType() != FileType.USER_DIR.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());

		return addFile(file, userId, FileType.USER_FILE.value());
	}

	@Override
	public void downloadFile(Integer fileId, Integer userId, HttpServletRequest request, HttpServletResponse response) {

		File file = checkArgs(Arrays.asList(fileId, userId), fileId, userId);
		if(fileId == 0 || file.getType() != FileType.USER_FILE.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		downloadFile(file, request,response);
		
	}

	@Override
	@Transactional
	public boolean batchRemoveFileAndDir(List<Integer> ids, Integer userId) {
		checkArgs(Arrays.asList(ids, userId), null, null);
		
		obsService.createObsClicent();
		ids.stream().forEach(id->{
			removeFileOrDir(id, userId);
		});
		obsService.closeObsClient();
		return true;
	}

	@Override
	public boolean renameFileOrDir(File file, Integer userId) {
		File file1 = checkArgs(Arrays.asList(file, file.getName(), userId), file.getId(), userId);

		return renameDirOrFile(file1, file.getName());
	}

	@Override
	public String getShareUrl(Integer fileId, Integer userId) {
		File file = checkArgs(Arrays.asList(fileId, userId), fileId, userId);
		if(file.getType() != FileType.USER_FILE.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		return getShareUrl(file);
	}

	@Override
	public Map<String, Object> getDirAndFileListByParentId(Integer parentId, Integer userId) {
		File file = checkArgs(Arrays.asList(parentId, userId), parentId, userId);
		if(file.getType() != FileType.USER_DIR.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		List<File> files = fileMapper.selectByParentId(parentId, userId);
		
		String path = getPathById(parentId, userId);
		Map<String, Object> map = new HashMap<>();
		map.put("path", path);
		map.put("files", files);
		
		return map;
	}

	@Override
	public List<Map<String, Object>> getDirAndFileListByName(String name, Integer parentId, Integer userId) {
		File file = checkArgs(Arrays.asList(name,parentId, userId), parentId, userId);
		if(file.getType() != FileType.USER_DIR.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		List<File> files = fileMapper.getByCreatorId(userId);

		Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
		return FileTreeNodeUtils.getFilesByNameAndParentId(fileTree, parentId, name);
	}

	@Override
	public String getPathById(Integer fileId, Integer userId) {
		
		List<File> files = null;
		
		if(null != userId)files = fileMapper.getByCreatorId(userId);//个人云盘
		else files = fileMapper.getGroupFileAndDir();//群组云盘
		
		Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
		String path = FileTreeNodeUtils.getPathById(fileTree, fileId);
		return path;
	}

	@Override
	public Integer getRootGroupDirIdByCurrentId(Integer fileId) {
		List<File> groupFileAndDir = fileMapper.getGroupFileAndDir();
		
		Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(groupFileAndDir);
		
		Integer rootGroupDirId = FileTreeNodeUtils.getRootGroupDirIdByFileId(fileTree, fileId);
		return rootGroupDirId;
	}

	@Override
	public GroupDirOrFileInfos getGroupDirAndFileListByParentId(Integer parentId, User user) {
		File file = checkArgs(Arrays.asList(parentId, user), parentId, null);
		if(file.getType() != FileType.GROUP_DIR.value() && parentId != 0)throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		GroupDirOrFileInfos groupDirOrFileInfos = new GroupDirOrFileInfos();
		
		//存储对应用户对某一文件/文件夹所具有的权限->该文件/文件夹的Map组成的集合
		List<Map<String, Object>> infoList = new LinkedList<>();
		
		//是否拥有创建文件的权限
		Integer createDir = AuthorityType.UNAUTHORIZED.value();
		
		//是否拥有上传权限
		Integer upload = AuthorityType.UNAUTHORIZED.value();
		
		//是否拥有删除权限
		Integer delete = AuthorityType.UNAUTHORIZED.value();
		
		
		//如果parentId是根目录
		if(0 == parentId) {
			//所有人都对群组根目录有可读权限，所以可以直接获取群组根目录下的内容
			List<File> files = fileMapper.getGroupFileAndDirByParentId(parentId);
			//获取当前用户所拥有的所有权限
			Map<Integer, Authority> authoritys = authorityService.getAuthorityByDepartmentId(user.getDepartment());
			
			//将文件或文件夹与权限一一对应存储
			infoList = files.stream().map(f -> {
				//获取该用户对于当前文件所拥有的权限
				Authority authority = authoritys.get(f.getId());
				if(null == authority)authority = new Authority(user.getDepartment(), f.getId());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("file", f);
				map.put("authority", authority);
				return map;
			}).collect(Collectors.toList());
			
			//如果是文件管理员，则拥有对根目录下一级内容的创建文件夹，上传文件以及删除的权限，其他人则均没有
			if(user.getLevel() == UserLevel.FILE_ADMIN.value()) {
				createDir = AuthorityType.AUTHORIZE.value();
				upload = AuthorityType.AUTHORIZE.value();
				delete = AuthorityType.AUTHORIZE.value();
			}
			
			
		}else {
			
			
			Authority currentAuthority = checkAuth(file, user, AuthorityType.READ);
			
			//获取该文件夹下的内容
			List<File> files = fileMapper.getGroupFileAndDirByParentId(parentId);
			
			//将文件或文件夹与权限一一对应存储
			infoList = files.stream().map(f -> {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("file", f);
				map.put("authority", currentAuthority);
				return map;
			}).collect(Collectors.toList());
			
			//设置对应的权限
			createDir = currentAuthority.getCreateDir();
			upload = currentAuthority.getUpload();
			delete = currentAuthority.getDelete();
			
		}
		
		//填充返回内容
		groupDirOrFileInfos.setCurrentPath(getPathById(parentId, null));
		groupDirOrFileInfos.setCreateDir(createDir);
		groupDirOrFileInfos.setUpload(upload);
		groupDirOrFileInfos.setDelete(delete);
		groupDirOrFileInfos.setFileAndAuthorityInfos(infoList);
		
		
		return groupDirOrFileInfos;
	}



	@Override
	public boolean createGroupDir(File file, User user) {
		File parentDir = checkArgs(Arrays.asList(file, user), file.getParentId(), null);
		
		//如果是群组根目录,且当前用户是文件管理员，则可以直接在群组根目录下创建文件夹，否则，抛异常
		//如果不是群组根目录，则执行else分支
		if(parentDir.getId() == 0 && user.getLevel() != UserLevel.FILE_ADMIN.value()) {
			throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		}else if(parentDir.getId() != 0){
			if(null == parentDir || parentDir.getType() != FileType.GROUP_DIR.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
			
			//检查当前用户是否有在该文件夹下创建文件夹的权限
			checkAuth(parentDir, user, AuthorityType.CREATE_DIR);
		}
		
		return addDir(file, user.getId(), FileType.GROUP_DIR.value());
	}



	@Override
	public boolean createGroupFile(File file, User user) {
		File parentDir = checkArgs(Arrays.asList(file, user), file.getParentId(), null);
		//TODO 对parentDir的检查
		//如果是群组根目录,且当前用户是文件管理员，则可以直接在群组根目录下上传文件，否则，抛异常
		//如果不是群组根目录，则执行else分支
		if(parentDir.getId() == 0 && user.getLevel() != UserLevel.FILE_ADMIN.value()) {
			throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		}else if(parentDir.getId() != 0){
			if(null == parentDir || parentDir.getType() != FileType.GROUP_DIR.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
			
			//检查当前用户是否有在该文件夹下创建文件夹的权限
			checkAuth(parentDir, user, AuthorityType.UPLOAD);
		}
		
		return addFile(file, user.getId(), FileType.GROUP_FILE.value());
	}



	@Override
	public void downloadGroupFile(Integer fileId, User user, HttpServletRequest request,
			HttpServletResponse response) {
		
		File file = checkArgs(Arrays.asList(fileId, user), fileId, null);
		if(null == file || fileId == 0 || file.getType() != FileType.GROUP_FILE.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		//群组云盘根目录下的文件只有文件管理员和被授权的用户可以下载，其他群组文件下载需要验证权限
		if(0 != file.getParentId() || (file.getParentId() == 0 && user.getLevel() != UserLevel.FILE_ADMIN.value())) {
			checkAuth(file, user, AuthorityType.DOWNLOAD);
		}
		
		downloadFile(file, request, response);
		
	}



	@Override
	public String getGroupFileShareUrl(Integer fileId, User user) {
		File file = checkArgs(Arrays.asList(fileId, user), fileId, null);
		if(fileId == 0 || file.getType() != FileType.GROUP_FILE.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		//群组云盘根目录下的文件只有文件管理员和被授权的用户可以分享，其他群组文件分享需要验证权限
		if(0 != file.getParentId() || (file.getParentId() == 0 && user.getLevel() != UserLevel.FILE_ADMIN.value())) {
			checkAuth(file, user, AuthorityType.SHARE);
		}
		
		return getShareUrl(file);
	}



	@Override
	public boolean renameGroupFileOrDir(File file, User user) {
		File file1 = checkArgs(Arrays.asList(file, user), file.getId(), null);
		if(file1.getType() != FileType.GROUP_DIR.value() && file1.getType() != FileType.GROUP_FILE.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		//群组云盘根目录下的一级文件或者文件夹只有文件管理员以及被授权的用户能重命名
		if(file1.getParentId() != 0 || (file1.getParentId() == 0 && user.getLevel() != UserLevel.FILE_ADMIN.value())){
			checkAuth(file1, user, AuthorityType.RENAME);
		}
		
		return renameDirOrFile(file1, file.getName());
	}



	@Override
	public Map<String, Object> getGroupUploadParam(Integer parentId, User user) {
		
		File parentDir = checkArgs(Arrays.asList(parentId, user), parentId, null);
		//TODO 对parentDir的检查
		//如果是群组根目录,且当前用户是文件管理员，则可以直接在群组根目录下上传文件，否则，抛异常
		//如果不是群组根目录，则执行else分支
		if(parentDir.getId() == 0 && user.getLevel() != UserLevel.FILE_ADMIN.value()) {
			throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		}else if(parentDir.getId() != 0){
			if(null == parentDir || parentDir.getType() != FileType.GROUP_DIR.value())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
			
			//检查当前用户是否有在该文件夹下创建文件夹的权限
			checkAuth(parentDir, user, AuthorityType.UPLOAD);
		}
		
		
		return getUploadParam();
	}



	@Override
	public List<Map<String, Object>> getGroupDirAndFileListByName(String name, Integer parentId, User user) {
		File file = checkArgs(Arrays.asList(name, parentId, user), parentId, null);
		if(file.getType() != FileType.GROUP_DIR.value() && parentId != 0)throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		List<Map<String, Object>> filesInfos = null;
		
		//如果当前文件夹是群组云盘根文件夹，则查找范围是根文件夹下的一级内容以及当前用户所拥有读权限的所有内容
		//否则，查找范围是当前文件夹下的所有内容
		if(parentId == 0) {
			
			List<File> files = fileMapper.getGroupFileAndDir();
			Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
			filesInfos = FileTreeNodeUtils.getFilesByNameAndParentId(fileTree, parentId, name);
			
			//筛选出有读权限的内容，并把相应的权限绑定给每个file
			filesInfos = filesInfos.stream().filter(f -> {
				File file1 = (File) f.get("file");
				Authority currentAuthority = authorityService.getCurrentAuthority(file1.getId(), user.getDepartment());
				if(0 != file1.getParentId() && currentAuthority.getRead() != AuthorityType.AUTHORIZE.value()) {
					return false;
				}
				
				f.put("authority", currentAuthority);
				return true;
				
			}).collect(Collectors.toList());
			
		}else {
			Authority authority = checkAuth(file, user, AuthorityType.READ);

			//查找当前文件夹下所有符合条件的file信息
			List<File> files = fileMapper.getGroupFileAndDir();
			Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(files);
			filesInfos = FileTreeNodeUtils.getFilesByNameAndParentId(fileTree, parentId, name);
		
			//给每个file信息绑定相应的权限
			filesInfos.stream().forEach(f -> {
				f.put("authority", authority);
			});
		}
		
		return filesInfos;
	}



	@Override
	public void getShareContent(String key, HttpServletRequest request,
			HttpServletResponse response) {
		
		File file = getFileByObjectName(key);
		
		if(file.getType() != FileType.GROUP_FILE.value() && file.getType() !=  FileType.USER_FILE.value())throw new PanException(StatusCode.KEY_INVALID.code(), StatusCode.KEY_INVALID.message());
		
		//获取文件内容
		downloadFile(file, request,response);
		
	}



	@Override
	@Transactional
	public boolean batchRemoveGroupFileAndDir(List<Integer> ids, Integer parentId, User user) {
		
		File file = checkArgs(Arrays.asList(ids, parentId, user), parentId, null);
		if(file.getType() != FileType.GROUP_DIR.value() && parentId != 0)throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		//如果当前文件夹是群组云盘根目录，则只有文件管理员能够删除文件和文件夹；其他群组文件和文件夹则需要授权才能删除
		if(parentId == 0 && user.getLevel() != UserLevel.FILE_ADMIN.value()) {
			throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		}else if(parentId != 0) {
			checkAuth(file, user, AuthorityType.DELETE);
		}
		
		obsService.createObsClicent();
		ids.stream().forEach(id->{
			removeFileOrDir(id, null);
		});
		obsService.closeObsClient();
		
		return true;
	}
	
	
	

}
