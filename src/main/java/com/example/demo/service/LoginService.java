/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
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
}
