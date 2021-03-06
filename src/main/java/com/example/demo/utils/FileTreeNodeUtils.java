package com.example.demo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.demo.domain.File;
import com.example.demo.domain.FileTreeNode;
import com.example.demo.enums.FileType;

/**
 * 
 * @ClassName FileTreeNodeUtils
 * @Description 操作文件多叉树的工具类
 * @author fuling
 * @date 2020年9月15日 下午8:28:32
 */
public class FileTreeNodeUtils {
	
	/**
	 * 
	 * @Title createFileTree
	 * @Description 创建文件树
	 * @param files 文件实体类的列表
	 * @return 创建好的文件树
	 * @throws RuntimeException
	 */
	public static Map<Integer, FileTreeNode> createFileTree(List<File> files) {
		
		if(null == files || 0 == files.size())throw new RuntimeException("文件列表不能为空！");
		
		Map<Integer, FileTreeNode> fileTreeNodes = new HashMap<>();
		
		//将传进来的每一个file包装成FileTreeNode，再放入fileTreeNodes中保存，键保存的是该file的id
		files.stream().forEach(f->{
			FileTreeNode node = new FileTreeNode(f.getParentId(), f);
			fileTreeNodes.put(f.getId(), node);
		});
		
		
		//遍历fileTreeNodes，建立每一个fileTreeNode的关系
		fileTreeNodes.entrySet().stream().forEach(f->{
			FileTreeNode node = f.getValue();
			
			//如果当前节点不是根目录节点，就将当前节点挂载到其父节点上
			if(node.getParent() != -1) {
				FileTreeNode parentNode = fileTreeNodes.get(node.getParent());
				parentNode.getChildren().put(f.getKey(), node);
			}
		});
		
		return fileTreeNodes;
	}
	
	
	/**
	 * 
	 * @Title findNodeById
	 * @Description 按节点id查找节点
	 * @param fileTreeNodes 文件树节点Map
	 * @param id 节点id
	 * @return 查找到返回节点，找不到返回null
	 * @throws RuntimeException
	 */
	public static FileTreeNode findNodeById(Map<Integer, FileTreeNode> fileTreeNodes, Integer id) {
		if(null == id)throw new RuntimeException("文件节点id不能为空！");
		return fileTreeNodes.get(id);
	}
	
	
	/**
	 * 
	 * @Title getObjectKeysByParentId
	 * @Description 获取指定文件/文件夹及其下所有文件的obs对象名
	 * @param fileTreeNodes 文件树节点Map
	 * @param id 指定的文件/文件夹id
	 * @return 返回指定文件/文件夹及其下所有文件的obs对象名，若没有，则List<String>长度为0
	 * @throws RuntimeException
	 */
	public static List<String> getObjectKeysByParentId(Map<Integer, FileTreeNode> fileTreeNodes, Integer id){
		FileTreeNode node = findNodeById(fileTreeNodes, id);
		if(null == node)throw new RuntimeException("没有id为" + id + "的文件/文件夹");
		List<String> ObjectKeys = new LinkedList<String>();
		
		//递归将node节点及其下所有节点的ObjectKey存放到ObjectKeys中
		setObjectKey(ObjectKeys, node);
		return ObjectKeys;
	}
	
	
	/**
	 * 
	 * @Title setObjectKey
	 * @Description 递归遍历对应文件/文件夹及其下的所有文件的obs对象名，存放到ObjectKeys中
	 * @param ObjectKeys 存放对应文件/文件夹及其下所有obs对象的列表
	 * @param node 当前节点
	 * @return 没有返回值
	 */
	private static void setObjectKey(List<String> ObjectKeys, FileTreeNode node) {
		//如果当前节点是文件，就将其对应的objectKey存放到ObjectKeys
		if(node.getFile().getType() == FileType.USER_FILE.value()) {
			ObjectKeys.add(node.getFile().getObjectName());
		}else {
			//如果当前节点是文件夹，就遍历它的孩子节点
			node.getChildren().entrySet().stream().forEach(f->{
				FileTreeNode n = f.getValue();
				setObjectKey(ObjectKeys, n);
			});
		}
		
	}
	
	
	
	
	
	/**
	 * 
	 * @Title getIdsByParentId
	 * @Description 获取指定文件/文件夹及其下所有文件和文件夹的id
	 * @param fileTreeNodes 文件树节点Map
	 * @param id 指定的文件/文件夹id
	 * @return 返回该文件/文件夹及其下所有文件和文件夹的id
	 * @throws RuntimeException
	 */
	public static List<Integer> getIdsByParentId(Map<Integer, FileTreeNode> fileTreeNodes, Integer id){
		FileTreeNode node = findNodeById(fileTreeNodes, id);
		System.out.println("node:" + node.getFile().getId());
		if(null == node)throw new RuntimeException("没有id为" + id + "的文件/文件夹");
		List<Integer> ids = new LinkedList<>();
		//递归将node及其下的所有节点对应的id存放到ids中
		setId(ids, node);
		return ids;
	}
	
	
	
	
	/**
	 * 
	 * @Title setId
	 * @Description 递归遍历对应文件夹下的所有文件和文件夹的id，存放到ids中
	 * @param ids 存放对应文件夹下所有文件或文件夹id的列表
	 * @param node 当前节点
	 * @return 没有返回值
	 */
	private static void setId(List<Integer> ids, FileTreeNode node) {
		//将当前节点id存放到ids中
		ids.add(node.getFile().getId());
		
		//遍历当前节点的孩子节点
		node.getChildren().entrySet().stream().forEach(f->{
			FileTreeNode n = f.getValue();
			setId(ids, n);
		});
	}

	/**
	 * 
	 * @Title getLevelCountById
	 * @Description 获取文件/文件夹所处的层级
	 * @param fileTreeNodes 文件树节点Map
	 * @param fileId 文件/文件夹id
	 * @return 文件/文件夹所处的层级(最低层级为0)，若fileId指向的是文件夹，则该层级包含文件夹本身
	 */
	public static Integer getLevelCountById(Map<Integer, FileTreeNode> fileTreeNodes, Integer fileId) {
		//初始化层数为-1
		Integer count = -1;
		
		FileTreeNode temp = fileTreeNodes.get(fileId);
		
		//如果fileId对应的是文件夹，则层数要算上该文件夹本身
		if(temp.getFile().getType() == FileType.USER_DIR.value())count++;
		
		//从该节点往上遍历，增加层数，直到根节点
		while(temp.getParent() != -1) {
			count++;
			temp = fileTreeNodes.get(temp.getParent());			
		}
		
		return count;
	}
	
	
	
	/**
	 * 
	 * @Title getPathById
	 * @Description 根据fileId返回其全路径
	 * @param fileTreeNodes 文件树节点Map
	 * @param fileId 文件id
	 * @return 返回指定文件全路径
	 */
	public static String getPathById(Map<Integer, FileTreeNode> fileTreeNodes, Integer fileId) {
		
		//初始化路径为"/"	
		StringBuffer path = new StringBuffer("/");
		
		FileTreeNode temp = fileTreeNodes.get(fileId);
		
		//从该节点往上遍历，将每个途经节点的名称拼接到路径参数path上，直到根节点
		while(temp.getParent() != -1) {
			path.insert(0, temp.getFile().getName());
			path.insert(0, "/");
			temp = fileTreeNodes.get(temp.getParent());		
		}
			
		return path.toString();
		
	}
	
	
	/**
	 * 
	 * @Title getFilesByNameAndParentId
	 * @Description 获取parentId文件夹下的所有名称符合name模糊匹配的文件或文件夹
	 * @param fileTreeNodes 文件树节点Map
	 * @param parentId 文件夹id
	 * @param name 文件/文件夹名称
	 * @return 返回fileId文件夹下的所有名称符合name模糊匹配的文件或文件夹
	 * @throws RuntimeException
	 */
	public static List<Map<String, Object>> getFilesByNameAndParentId(Map<Integer, FileTreeNode> fileTreeNodes, Integer parentId, String name){
		
		if(null == name || "".equals(name))throw new RuntimeException("名称关键字不能为空！");
		if(null == fileTreeNodes || fileTreeNodes.size() == 0 || null == parentId)throw new RuntimeException("参数不能为空！");
		
		FileTreeNode node = fileTreeNodes.get(parentId);
		if(null == node)throw new RuntimeException("当前文件夹不存在！");
		
		List<Map<String, Object>> files = new ArrayList<>();
		
		//遍历当前节点的孩子节点，将符合条件的节点信息存放到files中，其中键存放的是对应file的相对路径
		node.getChildren().entrySet().stream().forEach(f->{
			setFilesByEqNameAndParentId(files, f.getValue(), name, "");
		});
		
		return files;
		
	}
	
	
	/**
	 * 
	 * @Title setFilesByEqNameAndParentId
	 * @Description 描述这个方法的作用
	 * @param files 存储所有符合条件的file以及其对应的相对路径
	 * @param node 当前节点
	 * @param name 用于模糊匹配的名称
	 * @param path 当前路径
	 * @return 没有返回值
	 */
	private static void setFilesByEqNameAndParentId(List<Map<String, Object>> files, FileTreeNode node, String name, String path) {
		
		//将当前节点的名称作为途经路径拼接到path中
		String fileName = node.getFile().getName();
		path += ("/" + fileName);
		
		//如果当前节点符合name的模糊匹配，就将该节点连同其对应的相对路径存入files中
		if(fileName.contains(name)) {
			String cPath = path.replaceFirst("/", "");
			HashMap<String, Object> map = new HashMap<>();
			map.put("path", cPath);
			map.put("file", node.getFile());
			files.add(map);
		}
		
		//遍历当前节点的孩子节点
		String currentPath = path;
		node.getChildren().entrySet().stream().forEach(f->{
			FileTreeNode node2 = f.getValue();
			setFilesByEqNameAndParentId(files, node2, name, currentPath);
		});
	}
	
	
	
	/**
	 * 
	 * @Title getRootGroupDirIdByFileId
	 * @Description 获取指定fileId所处的根目录下的群组文件夹id
	 * @param fileId 文件/文件夹id
	 * @param fileTreeNodes 文件树节点Map
	 * @return 返回指定fileId所处的根目录下的群组文件夹id
	 */
	public static Integer getRootGroupDirIdByFileId(Map<Integer, FileTreeNode> fileTreeNodes, Integer fileId) {
		
		if(0 == fileId)return null;
		
		FileTreeNode temp = fileTreeNodes.get(fileId);
		
		if(null == temp)return null;
		
		while(temp.getParent() != 0) {
			temp = fileTreeNodes.get(temp.getParent());		
		}
		
		return temp.getFile().getId();
	}
}
