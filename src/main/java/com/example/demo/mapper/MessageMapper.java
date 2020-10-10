package com.example.demo.mapper;

import com.example.demo.domain.Message;

public interface MessageMapper {
    int deleteByPrimaryKey(String mId);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(String mId);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);

    int updateByPrimaryKey(Message record);
}