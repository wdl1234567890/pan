/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.LoginService;
import com.example.demo.service.PersonalService;
import com.example.demo.domain.User;
import com.example.demo.exception.PanException;
import com.example.demo.utils.JsonData;
import com.example.demo.utils.LoginData;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 白开水
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/pri/Personal")
public class PersonalController {
	@Autowired
	private PersonalService personalService;
	// 注入连接池对象
	@Autowired
	private JedisPool jedisPool;
	//利用loginOffUser方法注销修改密码后的用户
	@Autowired
	private LoginService loginService;
	
	
	/**
	
	* @Title: 修改手机
	* @Description: 用以修改用户手机
	* @param phone 要修改的手机号
	* @param id 用户id  
	* @return JsonData
	 */
	@RequestMapping("phoneC") 
	public JsonData phoneChange(String phone, Integer id)   {
		User user;
		try {
			user = personalService.phoneChange(phone, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonData.buildCustom(-3, "", "手机修改异常");
		}
		return JsonData.buildSuccess(user);
	}
	/**
	 * 
	
	* @Title: 密码修改
	
	* @Description: (这里用一句话描述这个方法的作用)  
	  
	* @return   JsonData
	* @param oldPwd 旧密码
	* @param newPWd 新密码
	* @param id 用户id
	* @param token 获取用户token，对redis服务器进行操作
	 * @throws Exception 
	 */
	@RequestMapping("pwdC")
	public JsonData pwdChange(String oldPwd, String newPwd, Integer id,@RequestHeader("token") String token) throws Exception {
		String message;
		//直接调用修改密码方法
		try {
			message = personalService.pwdChange(oldPwd, newPwd, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonData.buildCustom(-3, "", "密码修改异常");
		}
		//如果返回信息不为空说明修改失败，返回错误信息
		if(message!=null) {
			return JsonData.buildError(message);
		}else {
			//若返回信息为空则修改密码成功
			//需要修改redis服务器token信息使用户退出登陆
			personalService.logOffUser(token);
			return JsonData.buildSuccess("密码修改成功！请重新登陆!");
		}
		
	}
	
	/**
	 * 
	* @Description: 注销用户
	* @param  token 登陆状态标识  
	* @return  JsonData
	 */
	@RequestMapping("loginOff") 
	public JsonData loginOff(@RequestHeader("token") String token) {
		try {
			//直接调用注销方法，删除redis使用户退出登陆
			personalService.logOffUser(token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonData.buildCustom(-3, "", "登出异常");
		}
		return JsonData.buildSuccess("登出成功");
	}

}
