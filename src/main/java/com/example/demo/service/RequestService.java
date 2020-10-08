package com.example.demo.service;



import com.example.demo.vo.JsonData;

import java.io.IOException;

public interface RequestService {
	int addMyToFriendRequest(Integer myId,Integer userId) throws IOException;
	int delMyToFriendRequest(Integer myId,Integer userId) throws IOException ;
	int changeRequest(Integer myId,Integer userId,Boolean flag) throws IOException;
	JsonData queryRequest(Integer myId) throws IOException;
}
