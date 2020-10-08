package com.example.demo.controller;

import com.example.demo.domain.Department;
import com.example.demo.domain.MyFriends;
import com.example.demo.domain.User;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.mapper.MyFriendsMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.impl.MessageServiceImpl;
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
@RequestMapping("/api/v1/pri/chat/")
public class MessageController {
	@SuppressWarnings("unused")
	@PostMapping("sendOne")
	public JsonData sendOne(@RequestBody Map<String,String> id) throws Exception {
		MessageServiceImpl messageServiceImpl=new MessageServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		String tmp2=id.get("messageCon");
		if(tmp0==null) {
			return JsonData.buildError(null, "发送好友信息失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null, "发送好友信息失败1");
		}
		if(tmp2==null) {
			return JsonData.buildError(null, "发送好友信息失败2");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null, "发送好友信息失败3");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null, "发送好友信息失败4");
		}
		if(tmp2.equals("")) {
			return JsonData.buildError(null, "发送好友信息失败5");
		}
		Integer sendId=Integer.parseInt(tmp0);
		Integer recId= Integer.parseInt(tmp1);
		String messageCon=tmp2;
		if(sendId==null) {
			return JsonData.buildError(null, "发送好友信息失败6");
		}
		if(recId==null) {
			return JsonData.buildError(null, "发送好友信息失败7");
		}
		if(sendId.intValue()==recId.intValue()) {
			return JsonData.buildError(null,"发送好友信息失败8");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",sendId);
		if(user==null) {
			return JsonData.buildError(null,"发送好友信息失败9");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",recId);
		if(user1==null) {
			return JsonData.buildError(null,"发送好友信息失败10");
		}
		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriend=new MyFriends();
		myFriend.setMyId(sendId);
		myFriend.setFriendId(recId);
		MyFriends myFriend1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriend);
		if(myFriend1==null) {
			return JsonData.buildError(null,"发送好友信息失败11");
		}
		int c=messageServiceImpl.sendMessageOneToOne(sendId,recId,messageCon);
		if(c==1) {
			return JsonData.buildSuccess("发送好友信息成功");
		}
		else {
			return JsonData.buildError(null,"发送好友信息失败12");
		}
	}
	@SuppressWarnings("unused")
	@PostMapping("sendAll")
	public JsonData sendAll(@RequestBody Map<String,String> id) throws Exception {
		MessageServiceImpl messageServiceImpl=new MessageServiceImpl();
		String tmp0=id.get("sendId");
		String tmp1=id.get("messageCon");
		String tmp2=id.get("depId");
		if(tmp0==null) {
			return JsonData.buildError(null,"群发信息失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null,"群发信息失败1");
		}
		if(tmp2==null) {
			return JsonData.buildError(null,"群发信息失败2");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"群发信息失败3");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"群发信息失败4");
		}
		if(tmp2.equals("")) {
			return JsonData.buildError(null,"群发信息失败5");
		}
		Integer sendId=Integer.parseInt(tmp0);
		String messageCon= tmp1;
		Integer depId=Integer.parseInt(tmp2);
		if(sendId==null) {
			return JsonData.buildError(null,"群发信息失败6");
		}
		if(depId==null) {
			return JsonData.buildError(null,"群发信息失败7");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",sendId);
		if(user==null) {
			return JsonData.buildError(null,"群发信息失败8");
		}
		if(user.getDepartment().intValue()!=depId.intValue()) {
			return JsonData.buildError(null,"群发信息失败9");
		}
		DepartmentMapper departmentMapper=session.getMapper(DepartmentMapper.class);
		Department department=session.selectOne("com.example.demo.mapper.DepartmentMapper.selectByPrimaryKey", depId);
		if(department==null) {
			return JsonData.buildError(null,"群发信息失败10");
		}
		return messageServiceImpl.sendMessageAll(sendId,messageCon,depId);
	}
	@SuppressWarnings("unused")
	@PostMapping("rec")
	public JsonData rec(@RequestBody Map<String,String> id) throws Exception {
		MessageServiceImpl messageServiceImpl=new MessageServiceImpl();
		String tmp0=id.get("myId");
		if(tmp0==null) {
			return JsonData.buildError(null,"接收信息失败0");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"接收信息失败1");
		}
		Integer recId=Integer.parseInt(tmp0);
		if(recId==null) {
			return JsonData.buildError(null,"接收信息失败2");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",recId);
		if(user==null) {
			return JsonData.buildError(null,"接收信息失败3");
		}
		return messageServiceImpl.receiveMessage(recId);
	}
}
