package com.example.demo.controller;

import com.example.demo.domain.MyFriendInfo;
import com.example.demo.domain.MyFriends;
import com.example.demo.domain.User;
import com.example.demo.mapper.MyFriendInfoMapper;
import com.example.demo.mapper.MyFriendsMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.impl.MyFriendsInfoServiceImpl;
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
@RequestMapping("/api/v1/pri/myFriendsInfo/")
public class MyFriendsInfoController {
	@SuppressWarnings("unused")
	@PostMapping("add")
	public JsonData add(@RequestBody Map<String,String> id)  throws Exception{
		MyFriendsInfoServiceImpl myFriendsInfoServiceImpl=new MyFriendsInfoServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		if(tmp0==null) {
			return JsonData.buildError(null,"添加好友信息失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null,"添加好友信息失败1");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"添加好友信息失败2");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"添加好友信息失败3");
		}
		Integer myId=Integer.parseInt(tmp0);
		Integer userId= Integer.parseInt(tmp1);
		String info=id.get("info");
		if(myId==null) {
			return JsonData.buildError(null,"添加好友信息失败4");
		}
		if(userId==null) {
			return JsonData.buildError(null,"添加好友信息失败5");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"添加好友信息失败6");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"添加好友信息失败7");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"添加好友信息失败8");
		}
		MyFriendsMapper myFriendMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriends=new MyFriends();
		myFriends.setMyId(myId);
		myFriends.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriends);
		if(myFriends1==null) {
			return JsonData.buildError(null,"添加好友信息失败13"); 
		}
		MyFriendInfoMapper myFriendInfoMapper=session.getMapper(MyFriendInfoMapper.class);
		MyFriendInfo myFriendInfo=new MyFriendInfo();
		myFriendInfo.setMyId(myId);
		myFriendInfo.setFriendId(userId);
		MyFriendInfo myFriendInfo1=session.selectOne("com.example.demo.mapper.MyFriendInfoMapper.selectByMyIdAndFId", myFriendInfo);
		if(myFriendInfo1!=null) {
			return JsonData.buildError(null,"添加好友信息失败9");
		}
		if(info==null) {
			return JsonData.buildError(null,"添加好友信息失败10");
		}
		if(info.equals("")) {
			return JsonData.buildError(null,"添加好友信息失败11");
		}
		int c=myFriendsInfoServiceImpl.addMyFriendInfo(myId,userId,info );
		if(c==1) {
			return JsonData.buildSuccess("添加好友信息成功");
		}
		else {
			return JsonData.buildError(null,"添加好友信息失败12");
		}
	}
	@SuppressWarnings("unused")
	@PostMapping("del")
	public JsonData del(@RequestBody Map<String,String> id)  throws Exception{
		MyFriendsInfoServiceImpl myFriendsInfoServiceImpl=new MyFriendsInfoServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		if(tmp0==null) {
			return JsonData.buildError(null,"删除好友信息失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null,"删除好友信息失败1");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"删除好友信息失败2");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"删除好友信息失败3");
		}
		Integer myId=Integer.parseInt(tmp0);
		Integer userId= Integer.parseInt(tmp1);
		if(myId==null) {
			return JsonData.buildError(null,"删除好友信息失败4");
		}
		if(userId==null) {
			return JsonData.buildError(null,"删除好友信息失败5");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"删除好友信息失败6");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"删除好友信息失败7");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"删除好友信息失败8");
		}
		MyFriendsMapper myFriendMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriends=new MyFriends();
		myFriends.setMyId(myId);
		myFriends.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriends);
		if(myFriends1==null) {
			return JsonData.buildError(null,"删除好友信息失败11"); 
		}
		MyFriendInfoMapper myFriendInfoMapper=session.getMapper(MyFriendInfoMapper.class);
		MyFriendInfo myFriendInfo=new MyFriendInfo();
		myFriendInfo.setMyId(myId);
		myFriendInfo.setFriendId(userId);
		MyFriendInfo myFriendInfo1=session.selectOne("com.example.demo.mapper.MyFriendInfoMapper.selectByMyIdAndFId", myFriendInfo);
		if(myFriendInfo1==null) {
			return JsonData.buildError(null,"删除好友信息失败9");
		}
		int c=myFriendsInfoServiceImpl.delMyFriendInfo(myId,userId);
		if(c==1) {
			return JsonData.buildSuccess("删除好友信息成功");
		}
		else {
			return JsonData.buildError(null,"删除好友信息失败10");
		}
	}
	@SuppressWarnings("unused")
	@PostMapping("change")
	public JsonData change(@RequestBody Map<String,String> id)  throws Exception{
		MyFriendsInfoServiceImpl myFriendsInfoServiceImpl=new MyFriendsInfoServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		if(tmp0==null) {
			return JsonData.buildError(null,"修改好友信息失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null,"修改好友信息失败1");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"修改好友信息失败2");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"修改好友信息失败3");
		}
		Integer myId=Integer.parseInt(tmp0);
		Integer userId= Integer.parseInt(tmp1);
		String info=id.get("info");
		if(myId==null) {
			return JsonData.buildError(null,"修改好友信息失败4");
		}
		if(userId==null) {
			return JsonData.buildError(null,"修改好友信息失败5");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"修改好友信息失败6");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null,"修改好友信息失败7");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null,"修改好友信息失败8");
		}
		MyFriendsMapper myFriendMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriends=new MyFriends();
		myFriends.setMyId(myId);
		myFriends.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriends);
		if(myFriends1==null) {
			return JsonData.buildError(null,"修改好友信息失败13"); 
		}
		MyFriendInfoMapper myFriendInfoMapper=session.getMapper(MyFriendInfoMapper.class);
		MyFriendInfo myFriendInfo=new MyFriendInfo();
		myFriendInfo.setMyId(myId);
		myFriendInfo.setFriendId(userId);
		MyFriendInfo myFriendInfo1=session.selectOne("com.example.demo.mapper.MyFriendInfoMapper.selectByMyIdAndFId", myFriendInfo);
		if(myFriendInfo1==null) {
			return JsonData.buildError(null,"修改好友信息失败9");
		}
		if(info==null) {
			return JsonData.buildError(null,"修改好友信息失败10");
		}
		if(info.equals("")) {
			return JsonData.buildError(null,"修改好友信息失败11");
		}
		int c=myFriendsInfoServiceImpl.changeMyFriendInfo(myId,userId, info);
		if(c==1) {
			return JsonData.buildSuccess("修改好友信息成功");
		}
		else {
			return JsonData.buildError(null,"修改好友信息失败12");
		}
	}
	@SuppressWarnings("unused")
	@PostMapping("query")
	public JsonData query(@RequestBody Map<String,String> id)  throws Exception{
		MyFriendsInfoServiceImpl myFriendsInfoServiceImpl=new MyFriendsInfoServiceImpl();
		String tmp0=id.get("myId");
		String tmp1=id.get("userId");
		if(tmp0==null) {
			return JsonData.buildError(null,"查询好友信息失败0");
		}
		if(tmp1==null) {
			return JsonData.buildError(null,"查询好友信息失败1");
		}
		if(tmp0.equals("")) {
			return JsonData.buildError(null,"查询好友信息失败2");
		}
		if(tmp1.equals("")) {
			return JsonData.buildError(null,"查询好友信息失败3");
		}
		Integer myId=Integer.parseInt(tmp0);
		Integer userId= Integer.parseInt(tmp1);
		if(myId==null) {
			return JsonData.buildError(null,"查询好友信息失败4");
		}
		if(userId==null) {
			return JsonData.buildError(null,"查询好友信息失败5");
		}
		if(myId.intValue()==userId.intValue()) {
			return JsonData.buildError(null,"查询好友信息失败6");
		}
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		UserMapper userMapper=session.getMapper(UserMapper.class);
		User user=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",myId);
		if(user==null) {
			return JsonData.buildError(null, "查询好友信息失败7");
		}
		User user1=session.selectOne("com.example.demo.mapper.UserMapper.selectByPrimaryKey",userId);
		if(user1==null) {
			return JsonData.buildError(null, "查询好友信息失败8");
		}
		MyFriendsMapper myFriendMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriends=new MyFriends();
		myFriends.setMyId(myId);
		myFriends.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId",myFriends);
		if(myFriends1==null) {
			return JsonData.buildError(null, "查询好友信息失败9"); 
		}
		return myFriendsInfoServiceImpl.queryMyFriendInfo(myId,userId);
	}
}
