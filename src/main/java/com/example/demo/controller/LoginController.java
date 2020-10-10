package com.example.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.LoginService;
import com.example.demo.domain.User;
import com.example.demo.domain.UserLog;
import com.example.demo.enums.StatusCode;
import com.example.demo.vo.JsonData;
import com.example.demo.vo.LoginData;
import com.example.demo.vo.UserTokenVo;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 白开水
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1/pub/login")
public class LoginController {
	@Autowired
	private LoginService loginService;
	// 注入连接池对象
	@Autowired
	private JedisPool jedisPool;
	// 自动注入默认有效时间
	@Value("${server.default_token_savatime}")
	private Integer defaultTokenValidTime;
	
	/**
	 * 
	* @Title: 登陆
	* @Description: 用户登陆并修改redis服务器信息 
	* @param id 用户账号
	* @param pwd 用户密码 
	* @return LoginData 
	 */
	@RequestMapping("login") 
	public LoginData LoginUser(String mail,String pwd,Integer savetime) {
		
		//设置基本参数
		UserLog message=new UserLog();
		User user = new User();
		
		user.setMail(mail);
		user.setPwd(pwd);
		if(savetime==null)savetime=defaultTokenValidTime;
		//生成token
		String token=UUID.randomUUID().toString().replace("-", "").toUpperCase();
		//检测登陆是否成功
		try {
			message = loginService.loginUser(user);
			if(message.getUser()==null) {
				return LoginData.buildError(message.getMessage());
			}
			//将用户信息转换成json字符串 
			ObjectMapper mapper = new ObjectMapper(); 
			UserTokenVo tokenVo = new UserTokenVo(message.getUser(),savetime);
			String jsonRst = mapper.writeValueAsString(tokenVo);
			
			//获取redis连接池
			Jedis jedis = jedisPool.getResource();
			//设置token对象，用以检测登陆状态
			jedis.set(token,jsonRst);
			jedis.expire(token, savetime);
			//删除原有的token对象
			if(jedis.exists(user.getMail())) {
				 //如果已经有人登陆，删除正在登陆的token
				 //使其它人无法继续访问私有页面
				 String oldToken=jedis.get(user.getMail());
				 jedis.del(oldToken);
				 //设置新的可登陆token
				 jedis.set(user.getMail(),token);
				 jedis.expire(user.getMail(), savetime); 
				 //关闭jedis连接
				 jedis.close();
			}else {
				//不存在已登录的用户，直接设置用户名对象
				jedis.set(user.getMail(),token);
				jedis.expire(user.getMail(), savetime);
				//关闭
				jedis.close();
			}
			//redis服务器设置完毕后，将token返回至前端
			return LoginData.buildSuccess(token,message.getUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LoginData.buildCustom(-3, "", "登陆异常");
		}
	}
	/**
	 * 
	* @Title: 找回密码
	* @Description: 将密码发送到用户的邮箱中
	* @param  mail 用户邮箱
	* @param  id 用户账号  
	* @return  JsonData
	 */
	@RequestMapping("mail") 
	public JsonData findPwd(String mail) {
		String message;
		try {
			message = loginService.pwdToMail(mail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonData.buildError(StatusCode.DEFAULT_ERROR.code(), "密码找回异常");
		}
		if(message==null) {
			return JsonData.buildSuccess("密码找回成功，请查看你的邮箱");
		}else {
			return JsonData.buildError(StatusCode.DEFAULT_ERROR.code(), message);
		}
	}
	
	public static boolean isNumeric(String str){
		   for (int i = str.length();--i>=0;){  
		       if (!Character.isDigit(str.charAt(i))){
		           return false;
		       }
		   }
		   return true;

	}

}