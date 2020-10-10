package com.example.demo.controller;

import com.example.demo.domain.MyFriends;
import com.example.demo.domain.User;
import com.example.demo.mapper.MyFriendsMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.impl.MyFriendsServiceImpl;
import com.example.demo.vo.JsonData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pri/myFriends/")
public class MyFriendsController {
	@PostMapping("add")
	public JsonData add(@RequestBody Map<String,Integer> id) throws Exception {
		MyFriendsServiceImpl my= new MyFriendsServiceImpl();
		Integer myId=id.get("myId");
		Integer userId=id.get("userId"); 
		if(myId==null) {
			return JsonData.buildError(null,"添加好友失败0");
		}
		if(userId==null) {
			return JsonData.buildError(null,"添加好友失败1");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"添加好友失败2");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper myFriendsMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"添加好友失败3");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"添加好友失败4");
		}
		MyFriendsMapper myFriendsMapper0=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriends=new MyFriends();
		myFriends.setMyId(myId);
		myFriends.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId", myFriends);
		if(myFriends1!=null) {
			return JsonData.buildError(null,"添加好友失败5");
		}
		int c0=my.addMyFriend(myId,userId);
		int c1=my.addMyFriend(userId,myId);
		if(c0==1&&c1==1) {
			return JsonData.buildSuccess("添加好友成功");
		}
		else {
			return JsonData.buildError(null,"添加好友失败6");
		}
	}
	@PostMapping("del")
	public JsonData del(@RequestBody Map<String,Integer> id) throws Exception {
		MyFriendsServiceImpl my= new MyFriendsServiceImpl();
		Integer myId=id.get("myId");
		Integer userId=id.get("userId");
		if(myId==null) {
			return JsonData.buildError(null,"删除好友失败0");
		}
		if(userId==null) {
			return JsonData.buildError(null,"删除好友失败1");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"删除好友失败2");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper myFriendsMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"删除好友失败3");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"删除好友失败4");
		}
		MyFriendsMapper myFriendsMapper0=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriends=new MyFriends();
		myFriends.setMyId(myId);
		myFriends.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId", myFriends);
		if(myFriends1==null) {
			return JsonData.buildError(null,"删除好友失败5");
		}
		int c0=my.delMyFriend(myId,userId);
		int c1=my.delMyFriend(userId,myId);
		if(c0==1&&c1==1) {
			return JsonData.buildSuccess("删除好友成功");
		}
		else {
			return JsonData.buildError(null,"删除好友失败6");
		}
	}
	@PostMapping("list")
	public JsonData list(@RequestBody Map<String,Integer> id) throws Exception {
		Integer myId=id.get("myId");
		if(myId==null) {
			return JsonData.buildError(null,"列出好友失败0");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper myFriendsMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"列出好友失败1");
		}
		MyFriendsServiceImpl my= new MyFriendsServiceImpl();
		return my.listMyFriends(myId);
	}
	@PostMapping("find")
	public JsonData find(@RequestBody Map<String,Integer> id) throws Exception {
		Integer myId=id.get("myId");
		Integer userId=id.get("userId");
		if(myId==null) {
			return JsonData.buildError(null,"查找好友失败0");
		}
		if(userId==null) {
			return JsonData.buildError(null,"查找好友失败1");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"查找好友失败2");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper myFriendsMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"查找好友失败3");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"查找好友失败4");
		}
		MyFriendsServiceImpl my= new MyFriendsServiceImpl();
		return my.findMyFriendById(myId,userId);
	}
}
