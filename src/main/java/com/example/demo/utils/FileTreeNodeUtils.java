package com.example.demo.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.demo.domain.File;
import com.example.demo.domain.FileTreeNode;
import com.example.demo.enums.FileType;

/**
 * 
 * @ClassName: FileTreeNodeUtils
 * @Description: 操作文件多叉树的工具类
 * @author: fuling
 * @date: 2020年9月15日 下午8:28:32
 */
public class FileTreeNodeUtils {
	
	/**
	 * 
	 * @Title: createFileTree
	 * @Description: 创建文件树
	 * @param: files 文件实体类的列表
	 * @return: Map<Integer,FileTreeNode> 创建好的文件树
	 * @throws RuntimeException
	 */
	public static Map<Integer, FileTreeNode> createFileTree(List<File> files) {
		
		if(null == files || 0 == files.size())throw new RuntimeException("文件列表不能为空！");
		
		Map<Integer, FileTreeNode> fileTreeNodes = new HashMap<>();
		
		FileTreeNode root = new FileTreeNode(-1, null);
		
		files.stream().forEach(f->{
			FileTreeNode node = new FileTreeNode(f.getParentId(), f);
			fileTreeNodes.put(f.getId(), node);
		});
		
		fileTreeNodes.entrySet().stream().forEach(f->{
			FileTreeNode node = f.getValue();
			if(node.getParent() == 0) {
				root.getChildren().put(f.getKey(), node);
			}else {
				FileTreeNode parentNode = fileTreeNodes.get(node.getParent());
				parentNode.getChildren().put(f.getKey(), node);
			}
		});
		
		fileTreeNodes.put(0, root);
		
		return fileTreeNodes;
	}
	
	
	/**
	 * 
	 * @Title: findNodeById
	 * @Description: 按节点id查找节点
	 * @param: id 节点id
	 * @return: FileTreeNode 查找到返回节点，找不到返回null
	 * @throws RuntimeException
	 */
	public static FileTreeNode findNodeById(Map<Integer, FileTreeNode> fileTreeNodes, Integer id) {
		if(null == id)throw new RuntimeException("文件节点id不能为空！");
		return fileTreeNodes.get(id);
	}
	
	
	/**
	 * 
	 * @Title: getObjectKeysByParentId
	 * @Description: 获取指定文件/文件夹及其下所有文件的obs对象名
	 * @param: id 指定的文件/文件夹id
	 * @return: List<String> 返回指定文件/文件夹及其下所有文件的obs对象名，若没有，则List<String>长度为0
	 * @throws RuntimeException
	 */
	public static List<String> getObjectKeysByParentId(Map<Integer, FileTreeNode> fileTreeNodes, Integer id){
		FileTreeNode node = findNodeById(fileTreeNodes, id);
		if(null == node)throw new RuntimeException("没有id为" + id + "的文件/文件夹");
		List<String> ObjectKeys = new LinkedList<String>();
		setObjectKey(ObjectKeys, node);
		return ObjectKeys;
	}
	
	
	/**
	 * 
	 * @Title: setObjectKey
	 * @Description: 递归遍历对应文件/文件夹及其下的所有文件的obs对象名，存放到ObjectKeys中
	 * @param: ObjectKeys 存放对应文件/文件夹及其下所有obs对象的列表
	 * @param: node 当前节点
	 * @return: void
	 */
	private static void setObjectKey(List<String> ObjectKeys, FileTreeNode node) {
		if(node.getFile().getType() == FileType.USER_FILE.value()) {
			ObjectKeys.add(node.getFile().getObjectName());
		}else {
			node.getChildren().entrySet().stream().forEach(f->{
				FileTreeNode n = f.getValue();
				setObjectKey(ObjectKeys, n);
			});
		}
		
	}
	
	
	
	
	
	/**
	 * 
	 * @Title: getIdsByParentId
	 * @Description: 获取指定文件/文件夹及其下所有文件和文件夹的id
	 * @param: id 指定的文件/文件夹id
	 * @return: List<Integer> 返回该文件/文件夹及其下所有文件和文件夹的id
	 * @throws RuntimeException
	 */
	public static List<Integer> getIdsByParentId(Map<Integer, FileTreeNode> fileTreeNodes, Integer id){
		FileTreeNode node = findNodeById(fileTreeNodes, id);
		System.out.println("node:" + node.getFile().getId());
		if(null == node)throw new RuntimeException("没有id为" + id + "的文件/文件夹");
		List<Integer> ids = new LinkedList<>();
		setId(ids, node);
		return ids;
	}
	
	
	
	
	/**
	 * 
	 * @Title: setId
	 * @Description: 递归遍历对应文件夹下的所有文件和文件夹的id，存放到ids中
	 * @param: ids 存放对应文件夹下所有文件或文件夹id的列表
	 * @param: node 当前节点
	 * @return: void
	 */
	private static void setId(List<Integer> ids, FileTreeNode node) {
		ids.add(node.getFile().getId());
		node.getChildren().entrySet().stream().forEach(f->{
			FileTreeNode n = f.getValue();
			setId(ids, n);
		});
	}

	/**
	 * 
	 * @Title: getLevelCountById
	 * @Description: 获取文件/文件夹所处的层级
	 * @param: fileTreeNodes 文件树节点
	 * @param: fileId 文件/文件夹id
	 * @return: Integer 文件/文件夹所处的层级(最低层级为1)，若fileId指向的是文件夹，则该层级包含文件夹本身
	 * @throws
	 */
	public static Integer getLevelCountById(Map<Integer, FileTreeNode> fileTreeNodes, Integer fileId) {
		//TODO 参数检测
		Integer count = 0;
		
		FileTreeNode temp = fileTreeNodes.get(fileId);
		
		
		if(temp.getFile().getType() == FileType.USER_DIR.value())count++;
		
		
		while(temp.getParent() != -1) {
			count++;
			temp = fileTreeNodes.get(temp.getParent());			
		}
		
		return count;
	}
	
	/**
	 * 
	 * @Title: getPathById
	 * @Description: 描述这个方法的作用
	 * @param: @param fileTreeNodes
	 * @param: @param fileId
	 * @param: @return 参数说明
	 * @return: String 返回类型
	 * @throws
	 */
	public static String getPathById(Map<Integer, FileTreeNode> fileTreeNodes, Integer fileId) {
		//TODO 参数检测
				
		StringBuffer path = new StringBuffer("/");
		
		FileTreeNode temp = fileTreeNodes.get(fileId);
				
		while(temp.getParent() != -1) {
			path.insert(0, temp.getFile().getName());
			path.insert(0, "/");
			temp = fileTreeNodes.get(temp.getParent());		
		}
			
		return path.toString();
		
	}
	
	
	/**
	 * 
	 * @Title: getFilesByNameAndParentId
	 * @Description: 获取parentId文件夹下的所有名称符合name模糊匹配的文件或文件夹
	 * @param: fileTreeNodes 文件树节点
	 * @param: parentId 文件夹id
	 * @param: name 文件/文件夹名称
	 * @return: Map<String,File> 返回fileId文件夹下的所有名称符合name模糊匹配的文件或文件夹,键存放相对路径，值存放对应文件/文件夹
	 * @throws
	 */
	public static Map<String, File> getFilesByNameAndParentId(Map<Integer, FileTreeNode> fileTreeNodes, Integer parentId, String name){
		//TODO 参数检测
		if(null == name || "".equals(name))throw new RuntimeException("名称关键字不能为空！");
		if(null == fileTreeNodes || fileTreeNodes.size() == 0 || null == parentId)throw new RuntimeException("参数不能为空！");
		
		FileTreeNode node = fileTreeNodes.get(parentId);
		if(null == node)throw new RuntimeException("当前文件夹不存在！");
		
		Map<String, File> files = new HashMap<>();
		node.getChildren().entrySet().stream().forEach(f->{
			setFilesByEqNameAndParentId(files, f.getValue(), name, "");
		});
		
		return files;
		
	}
	
	
	private static void setFilesByEqNameAndParentId(Map<String, File> files, FileTreeNode node, String name, String path) {
		String fileName = node.getFile().getName();
		path += ("/" + fileName);
		if(fileName.contains(name)) {
			String cPath = path.replaceFirst("/", "");
			files.put(cPath, node.getFile());
		}
		String currentPath = path;
		node.getChildren().entrySet().stream().forEach(f->{
			FileTreeNode node2 = f.getValue();
			setFilesByEqNameAndParentId(files, node2, name, currentPath);
		});
	}
}
