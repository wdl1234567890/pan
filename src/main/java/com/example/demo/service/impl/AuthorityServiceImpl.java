package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Authority;
import com.example.demo.domain.File;
import com.example.demo.domain.FileTreeNode;
import com.example.demo.enums.FileType;
import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;
import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.AuthorityService;
import com.example.demo.utils.FileTreeNodeUtils;
import com.example.demo.utils.ToolUtils;

@Service
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
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if(file.getType() != FileType.GROUP_DIR.value() || 0 != file.getParentId())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());

		
		//过滤具有重复部门id的条目，若重复了，则以第一次出现的条目为准
		authoritys = ToolUtils.distinctAuthority(authoritys);
		
		//为每个权限设置fileId
		final int ff = fileId;
		authoritys.stream().forEach(auth->{
			auth.setFlieId(ff);
		});
		
		boolean result = authorityMapper.batchInsert(authoritys);
		
		if(!result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	@Override
	public boolean deleteAuthority(Integer authorityId, Integer userId) {
		
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		
		Authority authority = authorityMapper.selectByPrimaryKey(authorityId);
		if(null == authority)throw new PanException(StatusCode.AUTHORITY_IS_NOT_EXISTED.code(), StatusCode.AUTHORITY_IS_NOT_EXISTED.message());
		
		int result = authorityMapper.deleteByPrimaryKey(authorityId);
		if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	@Override
	@Transactional
	public boolean batchDeleteAuthority(List<Integer> authorityIds, Integer userId) {
		
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		
		boolean result = authorityMapper.batchDelete(authorityIds);
		if(!result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	@Override
	public boolean updateAuthority(Authority authority, Integer userId) {
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		
		Authority authority2 = authorityMapper.selectByPrimaryKey(authority.getId());
		if(null == authority2)throw new PanException(StatusCode.AUTHORITY_IS_NOT_EXISTED.code(), StatusCode.AUTHORITY_IS_NOT_EXISTED.message());
		
		int result = authorityMapper.updateByExampleSelective(authority, null);
		if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	@Override
	public List<Authority> getRootGroupDirAuthorityByFileId(Integer fileId, Integer userId) {
		//TODO 参数检验
		//TODO userId是否有权限执行此操作
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if(file.getType() != FileType.GROUP_DIR.value() || 0 != file.getParentId())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		
		return authorityMapper.getByFileId(fileId);
	}

	@Override
	public Authority getAuthorityByFileIdAndDepartmentId(Integer fileId, Integer departmentId) {
		//TODO 参数检验
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if(file.getType() != FileType.GROUP_DIR.value() || 0 != file.getParentId())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		
		return authorityMapper.getByFileIdAndDepartmentId(fileId, departmentId);
		
	}
	
	
	
	@Override
	public Authority getCurrentAuthority(Integer parentId, Integer departmentId) {
		File file = fileMapper.selectByPrimaryKey(parentId);
		//TODO 验证部门是否存在
		
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if(file.getType() != FileType.GROUP_DIR.value() || file.getId() == 0)throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
	
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
