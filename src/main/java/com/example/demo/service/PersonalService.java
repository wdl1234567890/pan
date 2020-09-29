package com.example.demo.service;

import com.example.demo.domain.User;

public interface PersonalService {
	User phoneChange(String phone,Integer id)throws Exception;
	String pwdChange(String oldPwd,String newPwd,Integer id)throws Exception;
	void logOffUser(String token)throws Exception;
}
