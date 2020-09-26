package com.example.demo.service.impl;

import java.io.IOException;
import java.io.InputStream;


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
import com.obs.services.model.ObsObject;
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
		PostSignatureRequest request = new PostSignatureRequest();
		request.setExpires(expires);
		PostSignatureResponse createPostSignature = obsClient.createPostSignature(request);
		return createPostSignature;
	}

	@Override
	public InputStream getObsObject(String objectKey) {
		
		ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
		return obsObject.getObjectContent();
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

	@Override
	public String getObsObjectShareUrl(String objectKey) {
		
		TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expires);
		request.setBucketName(bucketName);
		request.setObjectKey(objectKey);
		
		TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
		return response.getSignedUrl();
		
	}
	
	
}
