package com.example.demo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.domain.File;
import com.example.demo.domain.User;
import com.example.demo.vo.GroupDirOrFileInfos;

public interface FileService {
	
	/**
	 * 
	 * @Title getUploadParam
	 * @Description 描述这个方法的作用
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	public Map<String, Object> getUploadParam();

	
	/**
	 * 
	 * @Title createDir
	 * @Description 创建文件夹
	 * @param file 文件夹参数
	 * @param userId 用户id
	 * @return 创建成功返回true，否则抛异常
	 * @throws RuntimeException
	 */
	boolean createDir(File file, Integer userId);
	
	
	
	/**
	 * 
	 * @Title createFile
	 * @Description 创建文件
	 * @param file 文件参数
	 * @param userId 用户id
	 * @return 创建成功返回true，否则返回抛异常
	 * @throws RuntimeException
	 */
	boolean createFile(File file, Integer userId);
	
	
	
	/**
	 * 
	 * @Title getDownloadUrl
	 * @Description 下载文件
	 * @param fileId 文件id
	 * @param userId 用户id
	 * @param request 请求
	 * @param response 响应
	 * @throws RuntimeException
	 */
	void downloadFile(Integer fileId, Integer userId, HttpServletRequest request, HttpServletResponse response);
	
	
	
//	/**
//	 * 
//	 * @Title: removeFile
//	 * @Description: 删除文件
//	 * @param: fileId 文件id
//	 * @return: boolean 删除成功返回true,否则返回false
//	 * @throws
//	 */
//	//boolean removeFile(Integer fileId);
	
	
	
//	/**
//	 * 
//	 * @Title: removeFileOrDir
//	 * @Description: 删除文件夹
//	 * @param: fileId 文件夹id
//	 * @return: boolean 删除成功返回true,否则返回false
//	 * @throws
//	 */
//	boolean removeFileOrDir(Integer fileId);
	
	
	/**
	 * 
	 * @Title batchRemoveFileAndDir
	 * @Description 批量删除文件和文件夹
	 * @param ids 文件和文件夹的id列表
	 * @param userId 用户id
	 * @return 全部删除成功返回true,否则返回抛异常
	 * @throws RuntimeException
	 */
	boolean batchRemoveFileAndDir(List<Integer> ids, Integer userId);
	
	
	
	/**
	 * 
	 * @Title renameFileOrDir
	 * @Description 重命名文件或文件夹
	 * @param file 文件或文件夹参数
	 * @param userId 用户id
	 * @return 重命名成功返回true，否则返回抛异常
	 * @throws RuntimeException
	 */
	boolean renameFileOrDir(File file,Integer userId);
	
	
	/**
	 * 
	 * @Title getShareContent
	 * @Description 描述这个方法的作用
	 * @param @param key
	 * @param @param request
	 * @param @param response 参数说明
	 * @return 返回说明
	 * @throws
	 */
	void getShareContent(String key, HttpServletRequest request, HttpServletResponse response);
	
	
	
	
	/**
	 * 
	 * @Title getShareUrl
	 * @Description 获取文件外部分享的链接
	 * @param fileId 文件id
	 * @param userId 用户id
	 * @return 分享链接
	 * @throws RuntimeException
	 */
	String getShareUrl(Integer fileId, Integer userId);
	
	
	/**
	 * 
	 * @Title getDirAndFileListByParentId
	 * @Description 根据父id获取该父id下所有的文件和文件夹
	 * @param parentId 父id
	 * @param userId 用户id
	 * @return
	 * @throws RuntimeException
	 */
	Map<String, Object> getDirAndFileListByParentId(Integer parentId, Integer userId);
	
	
	/**
	 * 
	 * @Title getDirAndFileListByName
	 * @Description 根据名称在当前文件夹下查询文件/文件夹
	 * @param name 文件/文件夹名称
	 * @param userId 用户id
	 * @return 符合条件的文件/文件夹
	 * @throws RuntimeException
	 */
	 List<Map<String, Object>>  getDirAndFileListByName(String name, Integer parentId, Integer userId);

	
	
	
	/**
	 * 
	 * @Title getPathById
	 * @Description 获取当前文件/文件夹的路径信息
	 * @param fileId 当前文件/文件夹id
	 * @param userId 用户id, 如果为null，则表示查找的范围是群组云盘，否则是个人云盘
	 * @return 路径信息
	 * @throws RuntimeException
	 */
	String getPathById(Integer fileId, Integer userId);
	
	
	/**
	 * 
	 * @Title getRootGroupDirIdByCurrentId
	 * @Description 找到当前所处文件夹的所属根文件夹id
	 * @param fileId 当前所处文件夹的id
	 * @return 所属根文件夹id
	 */
	public Integer getRootGroupDirIdByCurrentId(Integer fileId);
	
	
	/**
	 * 
	 * @Title getGroupDirAndFileListByParentId
	 * @Description 根据父群组文件夹id获取其包含的群组文件和文件夹列表以及对应当前用户所具有的的权限信息
	 * @param parentId 父文件夹id
	 * @param user 当前用户
	 * @return
	 * @throws RuntimeException
	 */
	GroupDirOrFileInfos getGroupDirAndFileListByParentId(Integer parentId, User user);
	
	
	
	/**
	 * 
	 * @Title createGroupDir
	 * @Description 创建群组文件夹
	 * @param file 将创建的文件夹
	 * @param user 当前用户
	 * @return 添加成功返回true
	 * @throws PanException
	 */
	boolean createGroupDir(File file, User user);
	
	
	/**
	 * 
	 * @Title createGroupFile
	 * @Description 创建群组文件
	 * @param file 将要创建的群组文件
	 * @param user 当前用户
	 * @return 创建成功返回true
	 * @throws PanException
	 */
	boolean createGroupFile(File file, User user);
	
	
	
	/**
	 * 
	 * @Title downloadGroupFile
	 * @Description 描述这个方法的作用
	 * @param @param fileId
	 * @param @param user
	 * @param @param request
	 * @param @param response 参数说明
	 * @return 返回说明
	 * @throws
	 */
	void downloadGroupFile(Integer fileId, User user, HttpServletRequest request, HttpServletResponse response);
	
	
	/**
	 * 
	 * @Title getGroupFileShareUrl
	 * @Description 描述这个方法的作用
	 * @param @param fileId
	 * @param @param user
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	String getGroupFileShareUrl(Integer fileId, User user);

	
	/**
	 * 
	 * @Title renameGroupFileOrDir
	 * @Description 描述这个方法的作用
	 * @param @param file
	 * @param @param user
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	boolean renameGroupFileOrDir(File file, User user);
	
	
	/**
	 * 
	 * @Title getGroupUploadParam
	 * @Description 描述这个方法的作用
	 * @param @param parentId
	 * @param @param user
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	public Map<String, Object> getGroupUploadParam(Integer parentId, User user);
	
	
	/**
	 * 
	 * @Title getGroupDirAndFileListByName
	 * @Description 描述这个方法的作用
	 * @param @param name
	 * @param @param parentId
	 * @param @param user
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	public List<Map<String, Object>> getGroupDirAndFileListByName(String name, Integer parentId, User user);
	
	
	
	
	/**
	 * 
	 * @Title batchRemoveGroupFileAndDir
	 * @Description 描述这个方法的作用
	 * @param @param ids
	 * @param @param parentId
	 * @param @param user
	 * @param @return 参数说明
	 * @return 返回说明
	 * @throws
	 */
	public boolean batchRemoveGroupFileAndDir(List<Integer> ids, Integer parentId, User user);

	

}
