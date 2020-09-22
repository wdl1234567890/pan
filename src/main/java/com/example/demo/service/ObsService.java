package com.example.demo.service;

import com.example.demo.domain.File;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PostSignatureResponse;

public interface ObsService {
	
	/**
	 * 
	 * @Title createObsClicent
	 * @Description 创建一个obsClient
	 * @return 没有返回值
	 */
	void createObsClicent();
	
	/**
	 * 
	 * @Title getPostSignature
	 * @Description 获取obs签名信息
	 * @return 包装了Signature和Policy的类
	 * @throws ObsException
	 */
	PostSignatureResponse getPostSignature();
	
	
	/**
	 * 
	 * @Title getObjectUrl
	 * @Description 获取obs对象的地址
	 * @param objectKey 对象名
	 * @return obs对象的地址
	 * @throws ObsException
	 */
	String getObjectUrl(String objectKey);
	
	
	
	/**
	 * 
	 * @Title deleteObsject
	 * @Description 删除obs对象
	 * @param objectKey 对象名
	 * @return 删除成功返回true,否则返回false
	 * @throws ObsException
	 */
	boolean deleteObsject(String objectKey);
	
	
	/**
	 * 
	 * @Title closeObsClient
	 * @Description 关闭obs连接
	 * @return 没有返回值
	 * @throws
	 */
	void closeObsClient();

}
