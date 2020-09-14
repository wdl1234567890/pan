package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.File;
import com.example.demo.domain.FileExample;
import com.example.demo.mapper.FileMapper;

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
}
