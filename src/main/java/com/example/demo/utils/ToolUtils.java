package com.example.demo.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.domain.Authority;

/**
 * 
 * @ClassName ToolUtils
 * @Description 工具类
 * @author fuling
 * @date 2020年9月19日 上午10:31:30
 */
public class ToolUtils {
	
	/**
	 * 
	 * @Title distinctAuthority
	 * @Description 过滤具有重复部门Id的条目，若重复了，则以第一次出现的条目为准
	 * @param authoritys 需要过滤的权限集合
	 * @return 过滤完毕的权限集合
	 */
	public static List<Authority> distinctAuthority(List<Authority> authoritys){
		if(null == authoritys || authoritys.size() == 0)return authoritys;
		Set<Integer> deparmentIds = new HashSet<>();
		return authoritys.stream().filter(auth->{
			return deparmentIds.add(auth.getDepartmentId());
			
		}).collect(Collectors.toList());
	}
}
