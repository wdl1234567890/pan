package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Authority;
import com.example.demo.domain.File;
import com.example.demo.domain.User;
import com.example.demo.enums.FileType;
import com.example.demo.enums.StatusCode;
import com.example.demo.enums.UserLevel;
import com.example.demo.exception.PanException;
import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.FileService;
import com.example.demo.utils.ToolUtils;

@Service
public class AuthorityServiceImpl implements AuthorityService{
	
	@Autowired
	AuthorityMapper authorityMapper;
	
	@Autowired
	FileMapper fileMapper;
	
	@Autowired
	FileService fileService;
	

	
	@Transactional
	private boolean batchAddAuthority(List<Authority> authoritys) {
		//TODO 参数检验
		
		//if(user.getLevel() != UserLevel.FILE_ADMIN.value())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		
		//检测文件夹是否是根目录下的群组文件夹
		//File file = fileMapper.selectByPrimaryKey(fileId);
		//if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		//if(file.getType() != FileType.GROUP_DIR.value() || 0 != file.getParentId())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());

		
		//过滤具有重复部门id的条目，若重复了，则以第一次出现的条目为准
		//authoritys = ToolUtils.distinctAuthority(authoritys);
		
		//为每个权限设置fileId
//		final int ff = fileId;
//		authoritys.stream().forEach(auth->{
//			auth.setFlieId(ff);
//		});
		
		if(0 == authoritys.size())return true;
		
		boolean result = authorityMapper.batchInsert(authoritys);
		
		if(!result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	@Override
	public boolean deleteAuthority(Integer authorityId, User user) {
		
		//TODO 参数检验
		if(user.getLevel() != UserLevel.FILE_ADMIN.value())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		
		Authority authority = authorityMapper.selectByPrimaryKey(authorityId);
		if(null == authority)throw new PanException(StatusCode.AUTHORITY_IS_NOT_EXISTED.code(), StatusCode.AUTHORITY_IS_NOT_EXISTED.message());
		
		int result = authorityMapper.deleteByPrimaryKey(authorityId);
		if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	
	@Transactional
	private boolean batchDeleteAuthority(List<Authority> authoritys) {
		
		//TODO 参数检验
		//if(user.getLevel() != UserLevel.FILE_ADMIN.value())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		if(0 == authoritys.size())return true;
			
		List<Integer> authorityIds = authoritys.stream().map(auth -> {
			return auth.getId();
		}).collect(Collectors.toList());
		
		boolean result = authorityMapper.batchDelete(authorityIds);
		if(!result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	
	private boolean updateAuthority(Authority authority) {
		//TODO 参数检验
		//if(user.getLevel() != UserLevel.FILE_ADMIN.value())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		
		//Authority authority2 = authorityMapper.selectByPrimaryKey(authority.getId());
		//if(null == authority2)throw new PanException(StatusCode.AUTHORITY_IS_NOT_EXISTED.code(), StatusCode.AUTHORITY_IS_NOT_EXISTED.message());
		
		if(null == authority)return true;
		
		int result = authorityMapper.updateByPrimaryKeySelective(authority);
		if(1 != result)throw new PanException(StatusCode.DATABASE_ERROR.code(), StatusCode.DATABASE_ERROR.message());
		
		return true;
	}

	@Override
	public List<Authority> getRootGroupDirAuthorityByFileId(Integer fileId, User user) {
		//TODO 参数检验

		if(user.getLevel() != UserLevel.FILE_ADMIN.value())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if(file.getType() != FileType.GROUP_DIR.value() || 0 != file.getParentId())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		return authorityMapper.getByFileId(fileId);
	}

	@Override
	public Authority getAuthorityByFileIdAndDepartmentId(Integer fileId, Integer departmentId) {
		//TODO 参数检验
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if((file.getType() != FileType.GROUP_DIR.value() && file.getType() != FileType.GROUP_FILE.value()) || 0 != file.getParentId())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
		
		Authority authority = authorityMapper.getByFileIdAndDepartmentId(fileId, departmentId);
		
		//若还没有在该文件夹上对当前部门设置权限，则默认所有操作均为无权限
		if(null == authority)authority = new Authority(departmentId, fileId);
				
		return authority;
	}
	
	
	
	@Override
	public Authority getCurrentAuthority(Integer parentId, Integer departmentId) {
	
		File file = fileMapper.selectByPrimaryKey(parentId);
		//TODO 验证部门是否存在
		
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if((file.getType() != FileType.GROUP_DIR.value() && file.getType() != FileType.GROUP_FILE.value()) || file.getId() == 0)throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());
	
		//找到当前所处文件夹的所属根文件夹id
		Integer rootGroupDirId = fileService.getRootGroupDirIdByCurrentId(parentId);
		
		//获取部门对于该根文件夹所拥有的权限
		return getAuthorityByFileIdAndDepartmentId(rootGroupDirId, departmentId);
		
	}

	@Override
	public Map<Integer, Authority> getRootGroupDirAndFileAuthorityByDepartmentId(Integer departmentId) {
		//TODO 参数验证
		return authorityMapper.getAuthorityByDepartmentId(departmentId);
	}

	@Override
	public Map<Integer, Authority> getAuthorityByDepartmentId(Integer departmentId) {
		//TODO 参数验证
		return authorityMapper.getAuthorityByDepartmentId(departmentId);
	}

	@Override
	@Transactional
	public boolean saveAuthorityList(List<Authority> authoritys, Integer fileId, User user) {
		
		if(null == authoritys || authoritys.size() == 0 || null == fileId)throw new PanException(StatusCode.PARAM_VALIDATE_FAILED.code(), StatusCode.PARAM_VALIDATE_FAILED.message());
		
		//检查当前用户的身份，只有文件管理员能够对权限进行更新操作
		if(user.getLevel() != UserLevel.FILE_ADMIN.value())throw new PanException(StatusCode.NOT_ACCESS.code(), StatusCode.NOT_ACCESS.message());
		
		//检测文件夹是否是根目录下的群组文件夹
		File file = fileMapper.selectByPrimaryKey(fileId);
		if(null == file)throw new PanException(StatusCode.FILE_IS_NOT_EXISTED.code(), StatusCode.FILE_IS_NOT_EXISTED.message());
		if(file.getType() != FileType.GROUP_DIR.value() || 0 != file.getParentId())throw new PanException(StatusCode.OPERATION_NOT_ALLOWED.code(), StatusCode.OPERATION_NOT_ALLOWED.message());

				
		//过滤具有重复部门id的条目，若重复了，则以第一次出现的条目为准
		authoritys = ToolUtils.distinctAuthority(authoritys);
				
		//为每个权限设置fileId
		authoritys.stream().forEach(auth->{
			auth.setFlieId(fileId);
		});
		
		//需要被添加的权限
		List<Authority> addAuthoritys = null;
		
		//需要被更新的权限
		List<Authority> updateAuthoritys = null;
				
		//需要被删除的权限
		List<Authority> removeAuthoritys = null;
		
		
		addAuthoritys = authoritys.stream().filter(auth -> {
			return auth.getId() == null;
		}).collect(Collectors.toList());
		
		updateAuthoritys = authoritys.stream().filter(auth -> {
			return auth.getId() != null && auth.getIsDelete() == 0;
		}).collect(Collectors.toList());
		
		
		removeAuthoritys = authoritys.stream().filter(auth -> {
			return auth.getId() != null && auth.getIsDelete() != 0;
		}).collect(Collectors.toList());
		
		
		batchAddAuthority(addAuthoritys);
		batchDeleteAuthority(removeAuthoritys);
		
		updateAuthoritys.stream().forEach(auth -> {
			updateAuthority(auth);
		});
		
		return true;
	}
}
