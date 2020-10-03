package com.example.demo.vo;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName GroupDirOrFileInfos
 * @Description 请求获取某群组文件夹下的一级内容时返回的实体
 * @author fuling
 * @date 2020年9月29日 下午10:47:39
 */
public class GroupDirOrFileInfos {
	
	private String currentPath; //当前所在路径
	
	private Integer createDir; //创建文件夹的权限
	
	private Integer upload; //上传文件的权限
	
	private Integer delete; //删除文件/文件夹的权限
	
	//存储对应用户对某一文件/文件夹所具有的权限->该文件/文件夹的Map组成的集合
	private List<Map<String, Object>> fileAndAuthorityInfos;

	
	
	public GroupDirOrFileInfos() {
		
	}

	

	public GroupDirOrFileInfos(String currentPath, Integer createDir, Integer upload, Integer delete,
			List<Map<String, Object>> fileAndAuthorityInfos) {
		super();
		this.currentPath = currentPath;
		this.createDir = createDir;
		this.upload = upload;
		this.delete = delete;
		this.fileAndAuthorityInfos = fileAndAuthorityInfos;
	}



	public String getCurrentPath() {
		return currentPath;
	}
	
	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}
	
	public Integer getCreateDir() {
		return createDir;
	}



	public void setCreateDir(Integer createDir) {
		this.createDir = createDir;
	}



	public Integer getUpload() {
		return upload;
	}



	public void setUpload(Integer upload) {
		this.upload = upload;
	}



	public Integer getDelete() {
		return delete;
	}



	public void setDelete(Integer delete) {
		this.delete = delete;
	}



	public List<Map<String, Object>> getFileAndAuthorityInfos() {
		return fileAndAuthorityInfos;
	}



	public void setFileAndAuthorityInfos(List<Map<String, Object>> fileAndAuthorityInfos) {
		this.fileAndAuthorityInfos = fileAndAuthorityInfos;
	} 
	
	

	
}
