package com.example.demo;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.Authority;
import com.example.demo.domain.User;
import com.example.demo.service.AuthorityService;

@SpringBootTest
class AuthorityServiceTest {
	
	@Autowired
	private AuthorityService authorityService;

	@Test
	void testSaveAuthorityList() {
		User user = new User();
		user.setId(1);
		user.setDepartment(1);
		user.setLevel(1);
		List<Authority> authoritys = authorityService.getRootGroupDirAuthorityByFileId(130, user);
		//authoritys.add(new Authority(3, null));
//		authoritys.get(1).setUpload(1);
//		authoritys.get(2).setIsDelete(1);
		Authority aa = new Authority(3, null);
		aa.setDownload(0);
		authoritys.add(aa);
		authorityService.saveAuthorityList(authoritys, 130, user);
	}

	@Test
	void testDeleteAuthority() {
		
	}

	@Test
	void testBatchDeleteAuthority() {
		
	}

	@Test
	void testUpdateAuthority() {
		
	}

	@Test
	void testGetRootGroupDirAuthorityByFileId() {
		User user = new User();
		user.setId(1);
		user.setDepartment(1);
		user.setLevel(1);
		List<Authority> authorityByFileId = authorityService.getRootGroupDirAuthorityByFileId(130, user);
		authorityByFileId.forEach(f->{
			System.out.println(f);
		});
		
	}

	@Test
	void testGetAuthorityByFileIdAndDepartmentId() {
		
	}

	@Test
	void testGetCurrentAuthority() {
		
	}

	@Test
	void testGetRootGroupDirAndFileAuthorityByDepartmentId() {
		
	}

	@Test
	void testGetAuthorityByDepartmentId() {
		
	}

}
