/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/
package com.example.demo.Login.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Login.service.LoginService;
import com.example.demo.domain.User;
import com.example.demo.domain.UserLog;
import com.example.demo.util.JsonData;
import com.example.demo.util.UserTokenVo;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 白开水
 *
 */
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

	@RequestMapping("login") 
	public JsonData LoginUser(Integer id,String pwd,Integer savetime) {
		//设置基本参数
		UserLog message=new UserLog();
		User user = new User();
		user.setId(id);
		user.setPwd(pwd);
		if(savetime==null)savetime=defaultTokenValidTime;
		//生成token
		String token=UUID.randomUUID().toString().replace("-", "").toUpperCase();
		//检测登陆是否成功
		try {
			message = loginService.loginUser(user);
			if(message.getUser()==null) {
				return JsonData.buildError(message.getMessage());
			}
			//将用户信息转换成json字符串 
			ObjectMapper mapper = new ObjectMapper(); 
			UserTokenVo tokenVo = new UserTokenVo(message.getUser(),savetime);
			String jsonRst = mapper.writeValueAsString(tokenVo);
			System.out.println(message.getUser().getId());
			//获取redis连接池
			Jedis jedis = jedisPool.getResource();
			//设置token对象，用以检测登陆状态
			jedis.set(token,jsonRst);
			jedis.expire(token, savetime);
			//删除原有的token对象
			if(jedis.exists(user.getId().toString())) {
				 //如果已经有人登陆，删除正在登陆的token
				 //使其它人无法继续访问私有页面
				 String oldToken=jedis.get(user.getId().toString());
				 jedis.del(oldToken);
				 //设置新的可登陆token
				 jedis.set(user.getId().toString(),token);
				 jedis.expire(user.getId().toString(), savetime); 
				 //关闭jedis连接
				 jedis.close();
			}else {
				//不存在已登录的用户，直接设置用户名对象
				jedis.set(user.getId().toString(),token);
				jedis.expire(user.getId().toString(), savetime);
				//关闭
				jedis.close();
			}
			//redis服务器设置完毕后，将token返回至前端
			return JsonData.buildSuccess(token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonData.buildError("异常");
		}
	}

}