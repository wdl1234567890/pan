package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.Authority;
import com.example.demo.domain.User;


public interface AuthorityService {
	
	/**
	 * 
	 * @Title saveAuthorityList
	 * @Description 保存权限列表
	 * @param authoritys 待保存的权限列表
	 * @param fileId 需要设置权限的文件夹id
	 * @param user 执行该操作的用户
	 * @return 保存成功返回true, 失败抛异常
	 * @throws PanException
	 */
	boolean saveAuthorityList(List<Authority> authoritys, Integer fileId, User user);

	
	
	/**
	 * 
	 * @Title deleteAuthority
	 * @Description 删除单个权限
	 * @param authorityId 要删除的权限的id
	 * @param user 执行该操作的用户
	 * @return 删除成功返回true, 失败抛异常
	 * @throws PanException
	 */
	boolean deleteAuthority(Integer authorityId, User user);
	
	
	
//	/**
//	 * 
//	 * @Title batchDeleteAuthority
//	 * @Description 批量删除权限
//	 * @param authorityId 要删除的权限的id
//	 * @param user 执行该操作的用户
//	 * @return 删除成功返回true,否则抛异常
//	 * @throws PanException
//	 */
//	 boolean batchDeleteAuthority(List<Integer> authorityId, User user);
	
	
	
//	/**
//	 * 
//	 * @Title updateAuthority
//	 * @Description 批量删除权限
//	 * @param authoritys 待删除的权限列表
//	 * @param user 执行此操作的用户
//	 * @return 批量删除成功返回true, 失败抛异常
//	 * @throws PanException
//	 */
//	boolean updateAuthority(Authority authority, User user);
	
	
	
	/**
	 * 
	 * @Title getRootGroupDirAuthorityByFileId
	 * @Description 获取指定根目录下群组文件夹的权限列表
	 * @param fileId 根目录下群组文件夹的id
	 * @param user 用户
	 * @return 返回指定根目录下群组文件夹的权限列表，还没设置权限则返回null，其他异常情况抛出异常
	 * @throws PanException
	 */
	List<Authority> getRootGroupDirAuthorityByFileId(Integer fileId, User user);
	
	
	
	/**
	 * 
	 * @Title getAuthorityByFileIdAndDepartmentId
	 * @Description 根据根目录下一级文件夹id和部门id获取对应的权限信息
	 * @param fileId 文件夹id
	 * @param departmentId 部门id
	 * @return 返回符合条件的权限信息
	 * @throws PanException
	 */
	Authority getAuthorityByFileIdAndDepartmentId(Integer fileId, Integer departmentId);

	
	
	
	
	/**
	 * 
	 * @Title getCurrentAuthority
	 * @Description 获取指定文件夹下当前用户所拥有的权限
	 * @param parentId 当前所处文件夹的id
	 * @param departmentId 用户所属部门的id
	 * @return 返回指定文件夹下当前用户所拥有的权限
	 * @throws PanException
	 */
	Authority getCurrentAuthority(Integer parentId, Integer departmentId);

	
	
	/**
	 * 
	 * @Title getRootGroupDirAndFileAuthorityByDepartmentId
	 * @Description 获取群组根目录下一级文件和文件夹于当前用户所拥有的权限
	 * @param departmentId 当前用户所属的部门id
	 * @return 返回群组根目录下一级文件和文件夹于当前用户所拥有的权限，键为fileId,值为权限
	 * @throws PanException
	 */
	Map<Integer, Authority> getRootGroupDirAndFileAuthorityByDepartmentId(Integer departmentId);
	
	
	/**
	 * 
	 * @Title getAuthorityByDepartmentId
	 * @Description 根据部门id获取其所拥有的权限
	 * @param departmentId 部门id
	 * @return 返回存储文件/文件夹->该部门拥有的权限的map
	 * @throws PanException
	 */
	Map<Integer, Authority> getAuthorityByDepartmentId(Integer departmentId);
}
