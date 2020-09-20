package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Authority;


public interface AuthorityService {
	
	/**
	 * 
	 * @Title batchAddAuthority
	 * @Description 批量增加fileId对应文件夹的权限
	 * @param authoritys 待添加的权限列表
	 * @param fileId 需要设置权限的文件夹id
	 * @param userId 执行该操作的用户id
	 * @return 批量增加成功返回true, 失败抛异常
	 * @throws RuntimeException
	 */
	boolean batchAddAuthority(List<Authority> authoritys, Integer fileId, Integer userId);

	
	
	/**
	 * 
	 * @Title deleteAuthority
	 * @Description 删除单个权限
	 * @param authorityId 要删除的权限的id
	 * @param userId 执行该操作的用户id
	 * @return 删除成功返回true, 失败抛异常
	 * @throws RuntimeException
	 */
	boolean deleteAuthority(Integer authorityId, Integer userId);
	
	
	
	/**
	 * 
	 * @Title batchDeleteAuthority
	 * @Description 批量删除权限
	 * @param authorityId 要删除的权限的id
	 * @param userId 执行该操作的用户id
	 * @return 删除成功返回true,否则抛异常
	 * @throws RuntimeException
	 */
	boolean batchDeleteAuthority(List<Integer> authorityId, Integer userId);
	
	
	
	/**
	 * 
	 * @Title updateAuthority
	 * @Description 批量删除权限
	 * @param authoritys 待删除的权限列表
	 * @param userId 执行此操作的用户id
	 * @return 批量删除成功返回true, 失败抛异常
	 * @throws RuntimeException
	 */
	boolean updateAuthority(Authority authority, Integer userId);
	
	
	
	/**
	 * 
	 * @Title getRootGroupDirAuthorityByFileId
	 * @Description 获取指定根目录下群组文件夹的权限列表
	 * @param fileId 根目录下群组文件夹的id
	 * @param userId 用户id
	 * @return 返回指定根目录下群组文件夹的权限列表，还没设置权限则返回null，其他异常情况抛出异常
	 * @throws RuntimeException
	 */
	List<Authority> getRootGroupDirAuthorityByFileId(Integer fileId, Integer userId);
	
	
	
	/**
	 * 
	 * @Title getAuthorityByFileIdAndDepartmentId
	 * @Description 根据文件夹id和部门id获取对应的权限信息
	 * @param fileId 文件夹id
	 * @param departmentId 部门id
	 * @return 返回符合条件的权限信息
	 * @throws RuntimeException
	 */
	Authority getAuthorityByFileIdAndDepartmentId(Integer fileId, Integer departmentId);

	
	
	
	
	/**
	 * 
	 * @Title getCurrentAuthority
	 * @Description 获取指定文件夹下当前用户所拥有的权限
	 * @param parentId 当前所处文件夹的id
	 * @param departmentId 用户所属部门的id
	 * @return 返回指定文件夹下当前用户所拥有的权限
	 * @throws RuntimeException
	 */
	Authority getCurrentAuthority(Integer parentId, Integer departmentId);

	
	
	

}
