package com.example.demo.service;

import com.example.demo.vo.JsonData;

import java.io.IOException;

public interface MessageService {
	int sendMessageOneToOne(Integer sendId,Integer recId,String messageCon) throws IOException;
	JsonData sendMessageAll(Integer sendId,String messageCon,Integer depId) throws IOException;
	JsonData receiveMessage(Integer myId) throws IOException;
}
