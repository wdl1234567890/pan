package com.example.demo.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.config.ObsProperties;
import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;
import com.example.demo.service.ObsService;
import com.obs.services.ObsClient;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.PostSignatureRequest;
import com.obs.services.model.PostSignatureResponse;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;

@Service
public class ObsServiceImpi implements ObsService{
	
	private ObsClient obsClient;
	
	@Autowired
	ObsProperties obsProperties;
	
	@Value("${obs.config.expires}")
	private Long expires;
	
	@Value("${obs.config.bucketName}")
	private String bucketName;
	
	public void createObsClicent() {
		obsClient = new ObsClient(obsProperties.accessKey, obsProperties.secretAccessKey, obsProperties.endPoint);
	}
	
	@Override
	public PostSignatureResponse getPostSignature() {
	    this.createObsClicent();
		PostSignatureRequest request = new PostSignatureRequest();

		// 设置上传文件的大小
		Map<String, Object> formParams = new HashMap<String, Object>();
		formParams.put("content-length", 30 * 1024 * 1024L);
		request.setFormParams(formParams);
		
		request.setExpires(expires);
		PostSignatureResponse createPostSignature = obsClient.createPostSignature(request);
		this.closeObsClient();
		return createPostSignature;
	}

	@Override
	public String getObjectUrl(String objectKey) {
		
		TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expires);
		request.setBucketName(bucketName);
		request.setObjectKey(objectKey);

		TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
		return response.getSignedUrl();
	}
	
	

	@Override
	public boolean deleteObsject(String objectKey) {
		DeleteObjectResult deleteObject = obsClient.deleteObject(bucketName, objectKey);
		
		if(deleteObject.getStatusCode() == HttpStatus.OK.value())return true;
		return false;
	}
	
	@Override
	public void closeObsClient() {
		try {
			if(null != obsClient)obsClient.close();
		} catch (IOException e) {
			throw new PanException(StatusCode.OBS_ERROR.code(), e.getMessage());
		}
	}
	
	
}
