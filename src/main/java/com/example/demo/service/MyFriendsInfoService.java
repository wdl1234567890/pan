package com.example.demo.service;

import com.example.demo.vo.JsonData;

public interface MyFriendsInfoService {
	int addMyFriendInfo(Integer myId,Integer userId,String info) throws Exception;
	
	int delMyFriendInfo(Integer myId,Integer userId) throws Exception;
	
	int changeMyFriendInfo(Integer myId,Integer userId,String info) throws Exception;
	
	JsonData queryMyFriendInfo(Integer myId,Integer userId) throws Exception;
}
