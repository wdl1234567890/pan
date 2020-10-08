package com.example.demo.service.impl;

import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.MessageService;
import com.example.demo.vo.JsonData;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class MessageServiceImpl implements MessageService {

	@Override
	public int sendMessageOneToOne(Integer sendId, Integer recId, String messageCon) throws IOException {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MessageMapper messageMapper=session.getMapper(MessageMapper.class);
		Message message=new Message();
		UUID uuid = UUID.randomUUID();
		message.setmId(uuid.toString());
		message.setSendId(sendId);
		message.setRecId(recId);
		message.setMessageCon(messageCon);
		message.setmType(true);
		Calendar calendar = Calendar.getInstance();
		Long long0=calendar.getTimeInMillis();
		message.setcTime(long0.toString());
		int c=session.insert("com.example.demo.mapper.MessageMapper.insert", message);
		session.commit();
		session.close();
		return c;
	}

	@Override
	public JsonData sendMessageAll(Integer sendId, String messageCon, Integer depId) throws IOException {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MessageMapper messageMapper=session.getMapper(MessageMapper.class);
		UserMapper userMapper=session.getMapper(UserMapper.class);
		List<User> userList=session.selectList("com.example.demo.mapper.UserMapper.selectByDepId", depId);
		session.commit();
		if(userList!=null) {
			for(int i=0;i<userList.size();i++) {
				Message message=new Message();
				UUID uuid = UUID.randomUUID();
				message.setmId(uuid.toString());
				message.setSendId(sendId);
				message.setRecId(userList.get(i).getId());
				message.setMessageCon(messageCon);
				message.setmType(false);
				Calendar calendar = Calendar.getInstance();
				Long long0=calendar.getTimeInMillis();
				message.setcTime(long0.toString());
				session.insert("com.example.demo.mapper.MessageMapper.insert", message);
				session.commit();
			}
			return JsonData.buildSuccess(userList);
		}
		else if(userList==null) {
			return JsonData.buildError(null,null);
		}
		return JsonData.buildError(null,null);
	}

	@Override
	public JsonData receiveMessage(Integer myId) throws IOException {
		// TODO Auto-generated method stub
		String resource="mybatis-config.xml";
		InputStream inputStream=Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session=sqlSessionFactory.openSession();
		MessageMapper messageMapper=session.getMapper(MessageMapper.class);
		List<Message> messageList=session.selectList("com.example.demo.mapper.MessageMapper.selectByRecId",myId);
		return JsonData.buildSuccess(messageList);
	}

}
