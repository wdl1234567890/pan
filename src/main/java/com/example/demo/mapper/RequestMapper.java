package com.example.demo.mapper;

import com.example.demo.domain.Request;

public interface RequestMapper {
    int deleteByPrimaryKey(String rId);

    int insert(Request record);

    int insertSelective(Request record);

    Request selectByPrimaryKey(String rId);

    int updateByPrimaryKeySelective(Request record);

    int updateByPrimaryKey(Request record);
}