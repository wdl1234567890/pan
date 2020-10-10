package com.example.demo.service.impl;

import com.example.demo.domain.Request;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.service.RequestService;
import com.example.demo.vo.JsonData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class RequestServiceImpl implements RequestService {

	@Override
	public int addMyToFriendRequest(Integer myId, Integer userId) throws IOException {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		RequestMapper myFriendInfoMapper=session.getMapper(RequestMapper.class);
		UUID uuid=UUID.randomUUID();
		Request request=new Request();
		request.setrId(uuid.toString());
		request.setMyId(myId);
		request.setFriendId(userId);
		int c=session.insert("com.example.demo.mapper.RequestMapper.insert",request);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public int delMyToFriendRequest(Integer myId, Integer userId) throws IOException {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		RequestMapper myFriendInfoMapper=session.getMapper(RequestMapper.class);
		Request request=new Request();
		request.setMyId(myId);
		request.setFriendId(userId);
		int c=session.delete("com.example.demo.mapper.RequestMapper.deleteByMyIdAndFId",request);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public int changeRequest(Integer myId,Integer userId,Boolean flag) throws IOException {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		RequestMapper myFriendInfoMapper=session.getMapper(RequestMapper.class);
		Request request =new Request();
		request.setMyId(myId);
		request.setFriendId(userId);
		Request request0=session.selectOne("com.example.demo.mapper.RequestMapper.selectByMyIdAndFId",request);
		request0.setFlag(flag);
		int c=session.update("com.example.demo.mapper.RequestMapper.updateByPrimaryKey", request0);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public JsonData queryRequest(Integer myId) throws IOException {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		RequestMapper myFriendInfoMapper=session.getMapper(RequestMapper.class);
		List<Request> reqList=session.selectList("com.example.demo.mapper.RequestMapper.selectByMyId", myId);
		session.commit();
		session.close();
		return JsonData.buildSuccess(reqList);
	}

}
