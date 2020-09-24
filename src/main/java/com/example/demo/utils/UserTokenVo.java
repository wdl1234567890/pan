package com.example.demo.utils;

import com.example.demo.domain.User;

public class UserTokenVo {
	private User user;
	private Integer validTime;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getValidTime() {
		return validTime;
	}
	public void setValidTime(Integer validTime) {
		this.validTime = validTime;
	}
	public UserTokenVo(User user, Integer validTime) {
		super();
		this.user = user;
		this.validTime = validTime;
	}
	public UserTokenVo() {
		super();
	}
	@Override
	public String toString() {
		return "UserTokenVo [user=" + user + ", validTime=" + validTime + "]";
	}
	
}
