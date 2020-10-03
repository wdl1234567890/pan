package com.example.demo.vo;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.example.demo.domain.Authority;


public class AuthorityListSearch {
	
	@NotNull(message = "authoritys不能为空")
	private List<Authority> authoritys = new LinkedList<>();
	
	@NotNull(message = "fileId不能为空")
	@Min(message = "fileId必须是大于等于1的整数", value = 1)
	private Integer fileId;
	
	public AuthorityListSearch(){}

	public List<Authority> getAuthoritys() {
		return authoritys;
	}

	public void setAuthoritys(List<Authority> authoritys) {
		this.authoritys = authoritys;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	
	
	
	
}
