package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.File;
import com.example.demo.service.FileService;
import com.example.demo.vo.JsonData;

@RestController
@RequestMapping("/api/v1/pri/file")
public class FileController {
	
	@Autowired
	FileService fileService;
	
	
	@PostMapping("/dir")
	public JsonData createDir(File file, HttpHeaders headers) {
		//TODO 从header中拿token
		//TODO 根据token从redis中拿用户信息
		Integer userId = null;
		fileService.createDir(file, userId);
		return JsonData.buildSuccess(null);
	}
	
}
