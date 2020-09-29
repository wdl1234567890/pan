/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.service.PersonalService;
import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 白开水
 *
 */
@Service
public class PersonalServiceImpl implements PersonalService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisPool jedisPool;
	/**  
	 * @Title: phoneChange修改手机
	 * @Description: 输入新手机号码进行修改
	 * @param phone 新手机号码
	 * @param id 用户id
	 * @date 2020-09-23 11:25:46 
	 */
	@Override
	public User phoneChange(String phone, Integer id) throws Exception {
		User user = new User();
		//创建新的对象，用以动态更新
		user.setPhone(phone);
		user.setId(id);
		//动态更新数据库，只更新不为空的值
		userMapper.updateByPrimaryKeySelective(user);
		//返回最新的用户信息
		user=userMapper.selectByPrimaryKey(id);
		return user;
	}

	/**  
	 * @Title: pwdChange修改密码
	 * @Description: 输入新旧密码进行修改
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param id 用户id
	 * @date 2020-09-23 11:25:46 
	 */
	@Override
	public String pwdChange(String oldPwd, String newPwd, Integer id) throws Exception {
		// TODO Auto-generated method stub
		//获取旧用户对象
		User check = userMapper.selectByPrimaryKey(id);
		//如果旧密码不匹配，则返回错误信息
		if(!check.getPwd().equals(oldPwd)) {
			return "旧密码输入错误";
		}
		//新旧密码不能相同，返回信息
		if(oldPwd.equals(newPwd)) {
			return "新旧密码不能相同！";
		}
		//新建用户对象用以更新
		User user = new User();
		user.setId(id);
		user.setPwd(newPwd);
		userMapper.updateByPrimaryKeySelective(user);
		return null;
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

}
