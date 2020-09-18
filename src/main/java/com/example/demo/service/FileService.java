package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.File;
import com.obs.services.model.PostSignatureResponse;

public interface FileService {
	
	
	
	
	
	/**
	 * 
	 * @Title getPostSignature
	 * @Description 获取obs签名信息
	 * @return 包装了Signature和Policy的类
	 */
	public PostSignatureResponse getPostSignature();
	

	/**
	 * 
	 * @Title createDir
	 * @Description 创建文件夹
	 * @param file 文件夹参数
	 * @param userId 用户id
	 * @return 创建成功返回true，否则返回false
	 * @throws RuntimeException
	 */
	boolean createDir(File file, Integer userId);
	
	
	
	/**
	 * 
	 * @Title createFile
	 * @Description 创建文件
	 * @param file 文件参数
	 * @param userId 用户id
	 * @return 创建成功返回true，否则返回false
	 * @throws RuntimeException
	 */
	boolean createFile(File file, Integer userId);
	
	
	
	/**
	 * 
	 * @Title getDownloadUrl
	 * @Description 获取下载文件的链接
	 * @param 文件id
	 * @param 用户id
	 * @return 文件的下载链接
	 * @throws RuntimeException
	 */
	String getDownloadUrl(Integer fileId, Integer userId);
	
	
	
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
	 * @return 全部删除成功返回true,否则返回false
	 * @throws RuntimeException
	 */
	boolean batchRemoveFileAndDir(List<Integer> ids, Integer userId);
	
	
	
	/**
	 * 
	 * @Title renameFileOrDir
	 * @Description 重命名文件或文件夹
	 * @param file 文件或文件夹参数
	 * @param newName 新的名称
	 * @param userId 用户id
	 * @return 重命名成功返回true，否则返回false
	 * @throws RuntimeException
	 */
	boolean renameFileOrDir(File file, String newName, Integer userId);
	
	
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
	 * @Title getFileListByParentId
	 * @Description 根据父id获取该父id下所有的文件和文件夹
	 * @param parentId 父id
	 * @param userId 用户id
	 * @return 父id下所有的文件和文件夹构成的列表
	 * @throws RuntimeException
	 */
	List<File> getFileListByParentId(Integer parentId, Integer userId);
	
	
	/**
	 * 
	 * @Title getFileListByName
	 * @Description 根据名称在当前文件夹下查询文件/文件夹
	 * @param name 文件/文件夹名称
	 * @param userId 用户id
	 * @return 符合条件的文件/文件夹，键存储相对路径,值存储对应文件
	 * @throws RuntimeException
	 */
	 Map<String, File> getFileListByName(String name, Integer parentId, Integer userId);

	
	
	
	/**
	 * 
	 * @Title getPathById
	 * @Description 获取当前文件/文件夹的路径信息
	 * @param fileId 当前文件/文件夹id
	 * @param userId 用户id
	 * @return 路径信息
	 * @throws RuntimeException
	 */
	String getPathById(Integer fileId, Integer userId);
	
}
