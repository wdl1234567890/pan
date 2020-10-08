package com.example.demo.mapper;

import com.example.demo.domain.MyFriends;

public interface MyFriendsMapper {
    int deleteByPrimaryKey(String mFId);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String mFId);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);
}