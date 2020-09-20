package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.File;
import com.example.demo.service.FileService;
import com.obs.services.model.PostSignatureResponse;

@SpringBootTest
class FileServiceTest {
	
	@Autowired
	FileService fileService;

	@Test
	void testGetPostSignature() {
		PostSignatureResponse postSignature = fileService.getPostSignature();
		System.out.println(postSignature.getSignature());
	}

	@Test
	void testCreateDir() {
		int pre = 0;
		for(int i = 0; i < 11; i++) {
			File file = new File();
			file.setName("我的文件夹" + (10 + i));
			file.setParentId(pre);
			fileService.createDir(file , 1);
			pre = file.getId();
		}
		
	}

	@Test
	void testCreateFile() {
		File file = new File();
		file.setName("我的文件000");
		file.setParentId(18);
		fileService.createFile(file , 1);
	}

	@Test
	void testGetDownloadUrl() {
		String downloadUrl = fileService.getDownloadUrl(18, 1);
		System.out.println(downloadUrl);
	}

	@Test
	void testBatchRemoveFileAndDir() {
		fileService.batchRemoveFileAndDir(Arrays.asList(15, 21), 1);
	}

	@Test
	void testRenameFileOrDir() {
		File file = new File();
		file.setId(18);
		fileService.renameFileOrDir(file, "新名称3", 2);
	}
	
	@Test
	void testGetFileListByParentId() {
		List<File> files = fileService.getDirAndFileListByParentId(0, 1);
		System.out.println(files);
	}

	@Test
	void testGetFileListByName() {
		Map<String, File> files = fileService.getDirAndFileListByName("aa", 15, 1);
		files.entrySet().stream().forEach(f->{
			System.out.println(f.getKey());
			System.out.println(f.getValue());
		});
	}

	@Test
	void testGetPathById() {
		String path = fileService.getPathById(16, 1);
		System.out.println(path);
	}

}
