package com.example.demo.service.impl;

import com.example.demo.domain.MyFriends;
import com.example.demo.mapper.MyFriendsMapper;
import com.example.demo.service.MyFriendsService;
import com.example.demo.vo.JsonData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class MyFriendsServiceImpl implements MyFriendsService {
	@Override
	public int addMyFriend(Integer myId,Integer userId) throws Exception {
		// TODO Auto-generated method stub
		UUID uuid = UUID.randomUUID();
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriend=new MyFriends();
		myFriend.setmFId(uuid.toString());
		myFriend.setMyId(myId);
		myFriend.setFriendId(userId);
		int c=session.insert("com.example.demo.mapper.MyFriendsMapper.insert",myFriend);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public int delMyFriend(Integer myId,Integer userId) throws Exception {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriend=new MyFriends();
		myFriend.setMyId(myId);
		myFriend.setFriendId(userId);
		int c=session.delete("com.example.demo.mapper.MyFriendsMapper.deleteByMyIdAndFId", myFriend);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public JsonData listMyFriends(Integer myId) throws Exception {
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
		List<MyFriends> mfl=session.selectList("com.example.demo.mapper.MyFriendsMapper.selectByMyId",myId);
		session.commit();
		session.close();
		return JsonData.buildSuccess(mfl);
	}

	@Override
	public JsonData findMyFriendById(Integer myId,Integer userId) throws Exception {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendsMapper myFriendsMapper=session.getMapper(MyFriendsMapper.class);
		MyFriends myFriends=new MyFriends();
		myFriends.setMyId(myId);
		myFriends.setFriendId(userId);
		MyFriends myFriends1=session.selectOne("com.example.demo.mapper.MyFriendsMapper.selectByMyIdAndFId", myFriends);
		return JsonData.buildSuccess(myFriends1);
	}
}
