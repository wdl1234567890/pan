package com.example.demo.service.Impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Authority;
import com.example.demo.domain.File;
import com.example.demo.domain.FileTreeNode;
import com.example.demo.enums.FileType;
import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.AuthorityService;
import com.example.demo.utils.FileTreeNodeUtils;
import com.example.demo.utils.ToolUtils;

public class AuthorityServiceImpl implements AuthorityService{
	
	@Autowired
	AuthorityMapper authorityMapper;
	
	@Autowired
	FileMapper fileMapper;
	

	@Override
	@Transactional
	public boolean batchAddAuthority(List<Authority> authoritys, Integer fileId, Integer userId) {
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		
		//检测文件夹是否是根目录下的群组文件夹
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new RuntimeException("文件夹不存在！");
		if(file.getType() != FileType.GROUP_DIR.value())throw new RuntimeException("文件夹不属于群组，无法设置权限！");
		if(1 != file.getParentId())throw new RuntimeException("该文件夹不能设置权限！");
		
		//过滤具有重复部门id的条目，若重复了，则以第一次出现的条目为准
		authoritys = ToolUtils.distinctAuthority(authoritys);
		
		//为每个权限设置fileId
		final int ff = fileId;
		authoritys.stream().forEach(auth->{
			auth.setFlieId(ff);
		});
		
		boolean result = authorityMapper.batchInsert(authoritys);
		
		if(!result)throw new RuntimeException("数据库出错，批量添加权限失败！");
		
		return true;
	}

	@Override
	public boolean deleteAuthority(Integer authorityId, Integer userId) {
		
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		
		Authority authority = authorityMapper.selectByPrimaryKey(authorityId);
		if(null == authority)throw new RuntimeException("该权限不存在！");
		
		int result = authorityMapper.deleteByPrimaryKey(authorityId);
		if(1 != result)throw new RuntimeException("数据库出错，删除权限失败！");
		
		return true;
	}

	@Override
	@Transactional
	public boolean batchDeleteAuthority(List<Integer> authorityIds, Integer userId) {
		
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		
		boolean result = authorityMapper.batchDelete(authorityIds);
		if(!result)throw new RuntimeException("批量删除权限失败！");
		
		return true;
	}

	@Override
	public boolean updateAuthority(Authority authority, Integer userId) {
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		
		Authority authority2 = authorityMapper.selectByPrimaryKey(authority.getId());
		if(null == authority2)throw new RuntimeException("权限不存在，修改失败！");
		
		int result = authorityMapper.updateByExampleSelective(authority, null);
		if(1 != result)throw new RuntimeException("数据库出错，修改权限失败！");
		
		return true;
	}

	@Override
	public List<Authority> getRootGroupDirAuthorityByFileId(Integer fileId, Integer userId) {
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new RuntimeException("文件夹不存在");
		if(file.getType() != FileType.GROUP_DIR.value())throw new RuntimeException("不是群组根目录下的一级文件夹，无法执行该操作");
		if(0 != file.getParentId())throw new RuntimeException("不是群组根目录下的一级文件夹，无法执行该操作");
		
		return authorityMapper.getByFileId(fileId);
	}

	@Override
	public Authority getAuthorityByFileIdAndDepartmentId(Integer fileId, Integer departmentId) {
		//TODO 参数检验
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new RuntimeException("文件夹不存在");
		if(file.getType() != FileType.GROUP_DIR.value())throw new RuntimeException("不是群组根目录下的一级文件夹，无法执行该操作");
		if(0 != file.getParentId())throw new RuntimeException("不是群组根目录下的一级文件夹，无法执行该操作");
		
		return authorityMapper.getByFileIdAndDepartmentId(fileId, departmentId);
		
	}
	
	
	
	@Override
	public Authority getCurrentAuthority(Integer parentId, Integer departmentId) {
		File file = fileMapper.selectByPrimaryKey(parentId);
		//TODO 验证部门是否存在
		if(null != file && file.getType() != FileType.GROUP_DIR.value())throw new RuntimeException("该文件/文件夹不能执行获取权限的操作！");
		if(file.getId() == 0)throw new RuntimeException("群组根目录不能执行该方法！");
		//找到当前所处文件夹的所属根文件夹id
		List<File> groupFileAndDir = fileMapper.getGroupFileAndDir();
		Map<Integer, FileTreeNode> fileTree = FileTreeNodeUtils.createFileTree(groupFileAndDir);
		Integer rootGroupDirId = FileTreeNodeUtils.getRootGroupDirIdByFileId(fileTree, parentId);
		
		//获取部门对于该根文件夹所拥有的权限
		Authority authority = getAuthorityByFileIdAndDepartmentId(rootGroupDirId, departmentId);
		
		//若还没有在该文件夹上对当前部门设置权限，则默认所有操作均为无权限
		if(null == authority)authority = new Authority(departmentId, rootGroupDirId);
		
		return authority;
	}
}
