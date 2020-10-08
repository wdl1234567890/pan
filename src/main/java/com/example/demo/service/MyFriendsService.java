package com.example.demo.service;

import com.example.demo.vo.JsonData;

public interface MyFriendsService {

    int addMyFriend(Integer myId,Integer userId) throws Exception;

    int delMyFriend(Integer myId,Integer userId) throws Exception;
    
    JsonData listMyFriends(Integer myId) throws Exception;
    
    JsonData findMyFriendById(Integer myId,Integer userId) throws Exception;
    
}
