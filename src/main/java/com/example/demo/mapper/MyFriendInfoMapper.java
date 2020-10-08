package com.example.demo.mapper;

import com.example.demo.domain.MyFriendInfo;

public interface MyFriendInfoMapper {
    int deleteByPrimaryKey(String mFIId);

    int insert(MyFriendInfo record);

    int insertSelective(MyFriendInfo record);

    MyFriendInfo selectByPrimaryKey(String mFIId);

    int updateByPrimaryKeySelective(MyFriendInfo record);

    int updateByPrimaryKeyWithBLOBs(MyFriendInfo record);

    int updateByPrimaryKey(MyFriendInfo record);
}