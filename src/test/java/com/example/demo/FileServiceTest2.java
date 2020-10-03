package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.File;
import com.example.demo.domain.User;
import com.example.demo.service.FileService;
import com.example.demo.utils.ToolUtils;
import com.example.demo.vo.GroupDirOrFileInfos;


@SpringBootTest
class FileServiceTest2 {
	
	@Autowired
	FileService fileService;

	@Test
	void testGetUploadParam() {
		Map<String, Object> uploadParam = fileService.getUploadParam();
		System.out.println(uploadParam);
	}

	@Test
	void testCreateDir() {
		File file = new File();
		file.setParentId(0);
		file.setName("ggggggt");
		fileService.createDir(file , 1);
	}

	@Test
	void testCreateFile() {
		File file = new File();
		file.setParentId(0);
		file.setName("文件1~");
		file.setObjectName("llllll");
		fileService.createFile(file, 1);
	}

	@Test
	void testDownloadFile(HttpServletRequest request, HttpServletResponse response) {
		fileService.downloadFile(58, 1, request, response);
	}

	@Test
	void testBatchRemoveFileAndDir() {
		fileService.batchRemoveFileAndDir(Arrays.asList(72, 86), 1);
	}

	@Test
	void testRenameFileOrDir() {
		File file = new File();
		file.setId(65);
		file.setName("yyyy.png");
		fileService.renameFileOrDir(file , 1);
	}

	@Test
	void testGetShareUrl() {
		String shareUrl = fileService.getShareUrl(58, 1);
		System.out.println(shareUrl);
	}

	@Test
	void testGetDirAndFileListByParentId() {
		Map<String, Object> map = fileService.getDirAndFileListByParentId(0, 1);
		System.out.println("parh" + map.get("path"));
		List<File> files = (List<File>)map.get("files");
		files.stream().forEach(f->{
			System.out.println(f);
		});
	}

	@Test
	void testGetDirAndFileListByName() {
		List<Map<String, Object>> dirAndFileListByName = fileService.getDirAndFileListByName("文件", 0, 1);
		System.out.println(dirAndFileListByName);
	}

	@Test
	void testGetPathById() {
		String pathById = fileService.getPathById(58, 1);
		System.out.println(pathById);
	}


	@Test
	void testGetGroupDirAndFileListByParentId() {
		User user = new User();
		user.setId(1);
		user.setDepartment(1);
		user.setLevel(1);
		
		GroupDirOrFileInfos infos = fileService.getGroupDirAndFileListByParentId(130, user);
		System.out.println("当前路径：" + infos.getCurrentPath());
		System.out.println("能否创建文件夹：" + infos.getCreateDir());
		System.out.println("能否删除文件和文件夹：" + infos.getDelete());
		System.out.println("能否上传文件" + infos.getUpload());
		List<Map<String, Object>> fileAndAuthorityInfos = infos.getFileAndAuthorityInfos();
		fileAndAuthorityInfos.forEach(f->{
			System.out.println("文件：" + f.get("file"));
			System.out.println("权限：" + f.get("authority"));
		});
	}

	@Test
	void testCreateGroupDir() {
		File file = new File();
		file.setParentId(0);
		file.setName("uuuu");
		User user = new User();
		user.setId(1);
		user.setDepartment(1);
		user.setLevel(1);
		fileService.createGroupDir(file, user);
	}

	@Test
	void testCreateGroupFile() {
		File file = new File();
		file.setParentId(0);
		file.setName("文件2");
		file.setObjectName("ffff");
		User user = new User();
		user.setId(1);
		user.setDepartment(2);
		user.setLevel(1);
		fileService.createGroupFile(file, user);
	}

	@Test
	void testDownloadGroupFile() {
		
	}

	@Test
	void testGetGroupFileShareUrl() {
		User user = new User();
		user.setId(1);
		user.setDepartment(1);
		user.setLevel(2);
		String shareUrl = fileService.getGroupFileShareUrl(140, user);
		System.out.println(shareUrl);
	}
	
	@Test
	void testGetShareContent() {
		String rawKey = "123~mybatis-config.xml~2a6da467911545a49208a0c46869c039";
		String enKey = ToolUtils.Encryption(rawKey);
		rawKey = ToolUtils.Decryption(enKey);
		System.out.println(rawKey);
	}

	@Test
	void testRenameGroupFileOrDir() {
		File file = new File();
		file.setId(140);
		file.setName("文件2");
		User user = new User();
		user.setId(1);
		user.setDepartment(2);
		user.setLevel(1);
		fileService.renameGroupFileOrDir(file, user);
	}

	@Test
	void testGetGroupUploadParam() {
		User user = new User();
		user.setId(1);
		user.setDepartment(1);
		user.setLevel(2);
		Map<String, Object> map = fileService.getGroupUploadParam(130, user);
		System.out.println(map);
	}

	@Test
	void testGetGroupDirAndFileListByName() {
		User user = new User();
		user.setId(1);
		user.setDepartment(2);
		user.setLevel(2);
		List<Map<String, Object>> infos = fileService.getGroupDirAndFileListByName("g", 0, user);
		
		infos.forEach(f->{
			System.out.println("path:" + f.get("path"));
			System.out.println("file" + f.get("file"));
			System.out.println("authority" + f.get("authority"));
		});
	}
	
	@Test
	void testBatchRemoveGroupDirAndFile() {
		User user = new User();
		user.setId(1);
		user.setDepartment(2);
		user.setLevel(1);
		fileService.batchRemoveGroupFileAndDir(Arrays.asList(136), 130, user);
	}
	
	

}
