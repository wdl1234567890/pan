package com.example.demo.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Authority;
import com.example.demo.domain.User;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.LoginService;
import com.example.demo.vo.AuthorityListSearch;
import com.example.demo.vo.JsonData;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@Validated
@RequestMapping("/api/v1/pri/authority")
public class AuthorityController {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	LoginService loginService;
	
	
	@GetMapping("/list/{fileId}")
	public JsonData getAuthorityListByFileId(@PathVariable("fileId") @NotNull(message = "fileId不能为空")Integer fileId, @RequestHeader HttpHeaders headers){
		User user = loginService.getUser(headers.get("token").get(0));
		
//		User user1 = new User();
//		user1.setDepartment(1);
//		user1.setId(1);
//		user1.setLevel(1);
		
		List<Authority> list = authorityService.getRootGroupDirAuthorityByFileId(fileId, user);
		
		return JsonData.buildSuccess(list);
	}
	
	
	@PostMapping("/list/save")
	public JsonData saveAuthorityList(@RequestBody @Validated AuthorityListSearch authorityListSearch, @RequestHeader HttpHeaders headers) {
		User user = loginService.getUser(headers.get("token").get(0));
		
//		User user1 = new User();
//		user1.setDepartment(1);
//		user1.setId(1);
//		user1.setLevel(1);
		
		authorityService.saveAuthorityList(authorityListSearch.getAuthoritys(), authorityListSearch.getFileId(), user);
		
		return JsonData.buildSuccess(null);
	}
	
}
