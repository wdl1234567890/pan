package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.UserLog;

/**
 * @author 白开水
 *
 */
public interface LoginService {
	UserLog loginUser(User user)throws Exception;
	String pwdToMail(String mail,Integer id)throws Exception;
	User getUser(String token);
}
