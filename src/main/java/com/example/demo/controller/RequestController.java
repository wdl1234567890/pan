package com.example.demo.controller;

import com.example.demo.domain.MyFriends;
import com.example.demo.domain.Request;
import com.example.demo.domain.User;
import com.example.demo.mapper.MyFriendsMapper;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.impl.RequestServiceImpl;
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
@RequestMapping("/api/v1/pri/Request/")
public class RequestController {
	@SuppressWarnings("unused")
	@PostMapping("add")
	public JsonData add(@RequestBody Map<String,String> id)  throws Exception{
		RequestServiceImpl requestServiceImpl=new RequestServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		if(tmp0==null) {
			return JsonData.buildError(null, "添加好友请求失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null, "添加好友请求失败1");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"添加好友请求失败2");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"添加好友请求失败3");
		}
		Integer myId=Integer.parseInt(tmp0);
		Integer userId= Integer.parseInt(tmp1);
		if(myId==null) {
			return JsonData.buildError(null,"添加好友请求失败4");
		}
		if(userId==null) {
			return JsonData.buildError(null,"添加好友请求失败5");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"添加好友请求失败6");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"添加好友请求失败7");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"添加好友请求失败8");
		}
		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriend=new MyFriends();
		myFriend.setMyId(myId);
		myFriend.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriend);
		if(myFriends1!=null) {
			return JsonData.buildError(null,"添加好友请求失败11"); 
		}
		RequestMapper requestMapper=session.getMapper(RequestMapper.class);
		Request request=new Request();
		request.setMyId(myId);
		request.setFriendId(userId);
		Request request1=session.selectOne("com.example.demo.mapper.RequestMapper.selectByMyIdAndFId", request);
		if(request1!=null) {
			return JsonData.buildError(null,"添加好友请求失败9");
		}
		int c=requestServiceImpl.addMyToFriendRequest(myId,userId);
		if(c==1) {
			return JsonData.buildSuccess("添加好友请求成功");
		}
		else {
			return JsonData.buildError(null,"添加好友请求失败10");
		}
	}
	@SuppressWarnings("unused")
	@PostMapping("del")
	public JsonData del(@RequestBody Map<String,String> id)  throws Exception{
		RequestServiceImpl requestServiceImpl=new RequestServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		if(tmp0==null) {
			return JsonData.buildError(null,"删除好友请求失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null,"删除好友请求失败1");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"删除好友请求失败2");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"删除好友请求失败3");
		}
		Integer myId=Integer.parseInt(tmp0);
		Integer userId= Integer.parseInt(tmp1);
		if(myId==null) {
			return JsonData.buildError(null,"删除好友请求失败4");
		}
		if(userId==null) {
			return JsonData.buildError(null,"删除好友请求失败5");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"删除好友请求失败6");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"删除好友请求失败7");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"删除好友请求失败8");
		}
//		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
//		MyFriends myFriend=new MyFriends();
//		myFriend.setMyId(myId);
//		myFriend.setFriendId(userId);
//		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriend);
//		if(myFriends1==null) {
//			return JsonData.buildError("删除好友请求失败11"); 
//		}
		RequestMapper requestMapper=session.getMapper(RequestMapper.class);
		Request request=new Request();
		request.setMyId(myId);
		request.setFriendId(userId);
		Request request1=session.selectOne("com.example.demo.mapper.RequestMapper.selectByMyIdAndFId", request);
		if(request1==null) {
			return JsonData.buildError(null,"删除好友请求失败9");
		}
		int c=requestServiceImpl.delMyToFriendRequest(myId,userId);
		if(c==1) {
			return JsonData.buildSuccess("删除好友请求成功");
		}
		else {
			return JsonData.buildError(null,"删除好友请求失败10");
		}
	}
	@SuppressWarnings("unused")
	@PostMapping("change")
	public JsonData change(@RequestBody Map<String,String> id)  throws Exception{
		RequestServiceImpl requestServiceImpl=new RequestServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		String tmp2=id.get("flag");
		if(tmp0==null) {
			return JsonData.buildError(null,"修改好友请求失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null,"修改好友请求失败1");
		}
		if(tmp2==null) {
			return JsonData.buildError(null,"修改好友请求失败2");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"修改好友请求失败3");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"修改好友请求失败4");
		}
		if(tmp2.equals("")) {
			return JsonData.buildError(null,"修改好友请求失败5");
		}
		Integer myId=Integer.parseInt(tmp0);
		Integer userId= Integer.parseInt(tmp1);
		Boolean flag=Boolean.parseBoolean(tmp2);
		if(myId==null) {
			return JsonData.buildError(null,"修改好友请求失败6");
		}
		if(userId==null) {
			return JsonData.buildError(null,"修改好友请求失败7");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"修改好友请求失败8");
		}
		if(flag==null) {
			return JsonData.buildError(null,"修改好友请求失败9");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"修改好友请求失败10");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"修改好友请求失败11");
		}
		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriend=new MyFriends();
		myFriend.setMyId(myId);
		myFriend.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriend);
		if(myFriends1!=null) {
			return JsonData.buildError(null,"修改好友请求失败12"); 
		}
		RequestMapper requestMapper=session.getMapper(RequestMapper.class);
		Request request=new Request();
		request.setMyId(myId);
		request.setFriendId(userId);
		Request request1=session.selectOne("com.example.demo.mapper.RequestMapper.selectByMyIdAndFId", request);
		if(request1==null) {
			return JsonData.buildError(null,"修改好友请求失败13");
		}
		int c=requestServiceImpl.changeRequest(myId,userId,flag);
		if(c==1) {
			return JsonData.buildSuccess("修改好友请求成功");
		}
		else {
			return JsonData.buildError(null,"修改好友请求失败14");
		}
	}
	@SuppressWarnings("unused")
	@PostMapping("query")
	public JsonData query(@RequestBody Map<String,String> id)  throws Exception{
		RequestServiceImpl requestServiceImpl=new RequestServiceImpl();
		String tmp0=id.get("myId");
		if(tmp0==null) {
			return JsonData.buildError(null,"查询好友请求失败0");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"查询好友请求失败1");
		}
		Integer myId=Integer.parseInt(tmp0);
		if(myId==null) {
			return JsonData.buildError(null,"查询好友请求失败2");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"查询好友请求失败3");
		}
		return requestServiceImpl.queryRequest(myId);
	}
}
