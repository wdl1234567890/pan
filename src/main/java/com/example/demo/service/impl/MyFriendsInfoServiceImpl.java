package com.example.demo.service.impl;

import com.example.demo.domain.MyFriendInfo;
import com.example.demo.mapper.MyFriendInfoMapper;
import com.example.demo.service.MyFriendsInfoService;
import com.example.demo.vo.JsonData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.UUID;

public class MyFriendsInfoServiceImpl implements MyFriendsInfoService {

	@Override
	public int addMyFriendInfo(Integer myId, Integer userId,String info) throws Exception {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendInfoMapper myFriendInfoMapper=session.getMapper(MyFriendInfoMapper.class);
		MyFriendInfo myFriendInfo=new MyFriendInfo();
		UUID uuid=UUID.randomUUID();
		myFriendInfo.setmFIId(uuid.toString());
		myFriendInfo.setMyId(myId);
		myFriendInfo.setFriendId(userId);
		myFriendInfo.setInfo(info);
		int c=session.insert("com.example.demo.mapper.MyFriendInfoMapper.insert", myFriendInfo);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public int delMyFriendInfo(Integer myId, Integer userId) throws Exception {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendInfoMapper myFriendInfoMapper=session.getMapper(MyFriendInfoMapper.class);
		MyFriendInfo myFriendInfo=new MyFriendInfo();
		myFriendInfo.setMyId(myId);
		myFriendInfo.setFriendId(userId);
		int c=session.delete("com.example.demo.mapper.MyFriendInfoMapper.deleteByMyIdAndFId", myFriendInfo);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public int changeMyFriendInfo(Integer myId, Integer userId,String info) throws Exception {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendInfoMapper myFriendInfoMapper=session.getMapper(MyFriendInfoMapper.class);
		MyFriendInfo myFriendInfo=new MyFriendInfo();
		myFriendInfo.setMyId(myId);
		myFriendInfo.setFriendId(userId);
		MyFriendInfo myFriendInfo1=session.selectOne("com.example.demo.mapper.MyFriendInfoMapper.selectByMyIdAndFId", myFriendInfo);
		myFriendInfo1.setInfo(info);
		int c=session.update("com.example.demo.mapper.MyFriendInfoMapper.updateByPrimaryKeyWithBLOBs", myFriendInfo1);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public JsonData queryMyFriendInfo(Integer myId, Integer userId) throws Exception {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MyFriendInfoMapper myFriendInfoMapper=session.getMapper(MyFriendInfoMapper.class);
		MyFriendInfo myFriendInfo=new MyFriendInfo();
		myFriendInfo.setMyId(myId);
		myFriendInfo.setFriendId(userId);
		MyFriendInfo myFriendInfo1=session.selectOne("com.example.demo.mapper.MyFriendInfoMapper.selectByMyIdAndFId", myFriendInfo);
		session.commit();
		session.close();
		return JsonData.buildSuccess(myFriendInfo1);
	}

}
