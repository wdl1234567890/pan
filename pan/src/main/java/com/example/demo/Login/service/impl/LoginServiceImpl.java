/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/
package com.example.demo.Login.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Login.service.LoginService;
import com.example.demo.domain.User;
import com.example.demo.domain.UserExample;
import com.example.demo.domain.UserLog;
import com.example.demo.mapper.UserMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 白开水
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisPool jedisPool;
	/**
	 * 
	 * @Title: loginUser
	 * @Description: 用来验证输入的用户名与密码是否正确
	 * @param user 带有用户输入的用户名与密码的对象
	 * @date 2020-09-16 08:44:33
	 */
	@Override
	public UserLog loginUser(User user) throws Exception {
		//保存登陆信息
		UserLog message = new UserLog();
		// 获取同用户名的对象
		User check = userMapper.selectByPrimaryKey(user.getId());
		// 如果为空则不存在该用户，返回空
		if (check == null) {
			message.setMessage("不存在该用户");
			return message;
		} else if (check.getPwd().equals(user.getPwd())) {
			message.setMessage("登陆成功");
			message.setUser(check);
			return message;
		} else {
			// 密码不匹配说明密码错误，返回null
			message.setMessage("密码错误");
			return message;
		}
	}

	/**
	 * @Title: logOffUser
	 * @Description: 传入token，连接redis服务器，删除token
	 * @param token 传入redis服务器中的token键
	 * @date 2020-09-17 09:43:02
	 */
	@Override
	public void logOffUser(String token) throws Exception {
		Jedis jedis = jedisPool.getResource();
		String oldToken=jedis.get(token);
		jedis.del(oldToken);
		jedis.del(token);
		jedis.close();
	}

	/**
	 * @Title: pwdToMail
	 * @Description: 将找回的用户密码发送至用户邮箱
	 * @param mail 用户邮箱
	 * @param id   用户id
	 * @date 2020-09-17 09:44:40
	 */
	@Override
	public void pwdToMail(String mail, Integer id) throws Exception {
		// TODO Auto-generated method stub

	}

}
