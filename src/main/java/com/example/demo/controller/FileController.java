package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.domain.File;
import com.example.demo.domain.FileExample;
import com.example.demo.mapper.FileMapper;
import com.example.demo.vo.JsonData;

@RestController
@RequestMapping("/api/v1/pri/file")
public class FileController {
	
	@Autowired
	FileMapper fileMapper;
	
	
	@GetMapping("/test")
	public List<File> test() {
		FileExample fileExample = new FileExample();
		return fileMapper.selectByExample(fileExample);
	}
	
	
	@PostMapping("/dir")
	public JsonData createDir(File file) {
		return null;
	}
	
	@PostMapping("/upload")
	public void upload(MultipartHttpServletRequest request) {
		List<MultipartFile> files = request.getFiles("file");
		String parameter = request.getParameter("www");
		System.out.println(parameter);
		files.stream().forEach(f->{
			
			System.out.println(f.getOriginalFilename());
		});
	}
	
	
	
	
	
}
