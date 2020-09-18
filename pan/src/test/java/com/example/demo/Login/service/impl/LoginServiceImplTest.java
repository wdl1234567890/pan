/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
package com.example.demo.Login.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Login.service.LoginService;
import com.example.demo.domain.User;
import com.example.demo.domain.UserLog;
import com.example.demo.service.UserService;

/**
 * @author 白开水
 *
 */
@SpringBootTest
class LoginServiceImplTest {
	@Autowired
	private LoginService loginService;
	/**
	 * Test method for {@link com.example.demo.Login.service.impl.LoginServiceImpl#loginUser(com.example.demo.domain.User)}.
	 * @throws Exception 
	 */
	@Test
	void testLoginUser() throws Exception {
		User user = new User();
		UserLog log = new UserLog();
		user.setId(114514);
		user.setPwd("1919");
		log = loginService.loginUser(user);
		System.out.println(log.getUser());
	}

	/**
	 * Test method for {@link com.example.demo.Login.service.impl.LoginServiceImpl#logOffUser(java.lang.String)}.
	 */
	@Test
	void testLogOffUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.example.demo.Login.service.impl.LoginServiceImpl#pwdToMail(java.lang.String, java.lang.Integer)}.
	 */
	@Test
	void testPwdToMail() {
		fail("Not yet implemented");
	}

}
