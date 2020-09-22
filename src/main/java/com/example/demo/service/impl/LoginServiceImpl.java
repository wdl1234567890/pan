package com.example.demo.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.service.LoginService;
import com.example.demo.domain.User;

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
	@Autowired
    private JavaMailSender javaMailSender;
	/**
	 * 
	 * @Title: loginUser
	 * @Description: 用来验证输入的用户名与密码是否正确
	 * @param user 带有用户输入的用户名与密码的对象
	 * @date 2020-09-16 08:44:33
	 */
	@Override
	public UserLog loginUser(User user){
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
	public void logOffUser(String token){
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
	public String pwdToMail(String mail, Integer id){
		User user = userMapper.selectByPrimaryKey(id);
		//若不存在该用户，或是邮箱不正确都返回错误信息
		if(user==null) {
			return "不存在该用户名";
		}else if(!mail.equals(user.getMail())){
			return "邮箱不匹配！";
		}
		//用户名与邮箱都正确,发送密码邮件
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("2420355525@qq.com");//发送者
        msg.setTo("\n" + mail);//接收者
        msg.setSubject("你的密码已找回,请牢记密码");//标题
        msg.setText(user.getPwd());//邮件内容为该用户的秒
        javaMailSender.send(msg);
        System.out.println(msg);
        //发送成功则返回null
        return null;
	}

}
