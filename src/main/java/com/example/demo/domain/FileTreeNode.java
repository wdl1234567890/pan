package com.example.demo.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: FileTreeNode
 * @Description: 将从数据库读取出来的File类封装成多叉树节点
 * @author: fuling
 * @date: 2020年9月15日 下午8:25:47
 */
public class FileTreeNode {
	private File file;
	private Integer parentId;
	private Map<Integer, FileTreeNode> children = new HashMap<>();
	
	
	
	public FileTreeNode(Integer parentId, File file) {
	
		this.parentId = parentId;
		this.file = file;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Integer getParent() {
		return parentId;
	}
	public void setParent(Integer parentId) {
		this.parentId = parentId;
	}
	public Map<Integer,FileTreeNode> getChildren() {
		return children;
	}
	public void setChildren(Map<Integer,FileTreeNode> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "FileTreeNode [file=" + file + "]";
	}

}
