package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.File;
import com.example.demo.domain.User;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.FileService;
import com.example.demo.service.LoginService;
import com.example.demo.service.ObsService;
import com.example.demo.validate.group.Group;
import com.example.demo.vo.DeleteParam;
import com.example.demo.vo.FileSearch;
import com.example.demo.vo.GroupDirOrFileInfos;
import com.example.demo.vo.JsonData;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@Validated
@RequestMapping("/api/v1/pri/file")
public class FileController {
	
	@Autowired
	FileService fileService;
	
	@Autowired
	ObsService obsService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	AuthorityService authorityService;
	
	@PostMapping
	public JsonData createFile(@RequestBody @Validated(Group.CreateFile.class) File file, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		
		fileService.createFile(file, 1);
		Map<String, Integer> map = new HashMap<>();
		map.put("fileId", file.getId());
		return JsonData.buildSuccess(map);
	}
	
	@PostMapping("/dir")
	public JsonData createDir(@RequestBody @Validated(Group.CreateDir.class) File file, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		
		fileService.createDir(file, 1);
		return JsonData.buildSuccess(null);
	}
	
	
	@GetMapping("/upload/param")
	public JsonData getUploadParam() {
		Map<String, Object> uploadParam = fileService.getUploadParam();
		return JsonData.buildSuccess(uploadParam);
	}
	
	
	@DeleteMapping
	public JsonData deleteDirAndFile(@RequestBody @NotEmpty(message = "参数不能为空") List<Integer> ids, @RequestHeader HttpHeaders headers) {
		
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		
		fileService.batchRemoveFileAndDir(ids, 1);
		return JsonData.buildSuccess(null);
	}
	
	@PutMapping
	public JsonData renameDirOrFile(@RequestBody @Validated(Group.UpdateDirOrFile.class) File file, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		
		fileService.renameFileOrDir(file, 1);
		return JsonData.buildSuccess(null);
	}
	
	
	@GetMapping("/{id}")
	public JsonData getDownloadFile(@PathVariable("id") @NotNull(message = "id不能为空") @Min(value = 1, message="id必须为大于等于1的整数") Integer fileId, @RequestHeader HttpHeaders headers, HttpServletRequest request,  HttpServletResponse response) {
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		Integer userId = 1;
		fileService.downloadFile(fileId, userId, request, response);
		return JsonData.buildSuccess(null);
	}
	
	@GetMapping("/share/{id}")
	public JsonData getShareFileUrl(@PathVariable("id") @NotNull(message = "id不能为空") @Min(value = 1, message="id必须为大于等于1的整数") Integer fileId, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		
		String shareUrl = fileService.getShareUrl(fileId, 1);
		Map<String,String> map = new HashMap<>();
		map.put("shareUrl", shareUrl);
		return JsonData.buildSuccess(map);
	}
	
	@GetMapping("/sharecontent/{key}")
	public JsonData getShareContent(@PathVariable("key") @NotNull(message = "key不能为空")String key, HttpServletRequest request,  HttpServletResponse response) {
		fileService.getShareContent(key, request, response);
		return JsonData.buildSuccess(null);
	}
	
	
	@GetMapping("/list/{id}")
	public JsonData getDirAndFileListByParentId(@PathVariable("id") @NotNull(message = "parentId不能为空") @Min(value = 0, message="parentId必须为大于等于0的整数") Integer parentId, @RequestHeader HttpHeaders headers) {
		
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		
		Map<String, Object> map = fileService.getDirAndFileListByParentId(parentId, 1);
		
		return JsonData.buildSuccess(map);
	
	}
	
	
	@PostMapping("/list/search")
	public JsonData getDirAndFileListByName(@RequestBody @Validated FileSearch fileSearch, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		//User user = loginService.getUser(headers.get("token").get(0));
		
		List<Map<String, Object>> files = fileService.getDirAndFileListByName(fileSearch.getName(), fileSearch.getParentId(), 1);
		return JsonData.buildSuccess(files);
	}
	
	
	
	
	
	
	
	@GetMapping("/group/upload/param/{id}")
	public JsonData getGroupUploadParam(@PathVariable("id") @NotNull(message = "parentId不能为空") @Min(value = 0, message="parentId必须为大于等于0的整数")Integer parentId, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		Map<String, Object> map = fileService.getGroupUploadParam(parentId, user1);
		return JsonData.buildSuccess(map);
	}
	
	
	
	@PostMapping("/group/dir")
	public JsonData createGroupDir(@RequestBody @Validated(Group.CreateDir.class)File file, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		fileService.createGroupDir(file, user1);
		return JsonData.buildSuccess(null);
	}
	
	
	@PostMapping("/group")
	public JsonData createGroupFile(@RequestBody @Validated(Group.CreateFile.class) File file, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		fileService.createGroupFile(file, user1);
		Map<String, Integer> map = new HashMap<>();
		map.put("fileId", file.getId());
		return JsonData.buildSuccess(map);
	}
	
	
	@DeleteMapping("/group")
	public JsonData deleteGroupDirAndFile(@RequestBody @Validated DeleteParam deleteParam, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		fileService.batchRemoveGroupFileAndDir(deleteParam.getIds(), deleteParam.getParentId(), user1);
		return JsonData.buildSuccess(null);
	}
	
	
	@PutMapping("/group")
	public JsonData renameGroupDirOrFile(@RequestBody @Validated(Group.UpdateDirOrFile.class) File file, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		fileService.renameGroupFileOrDir(file, user1);
		return JsonData.buildSuccess(null);
	}
	
	
	@GetMapping("/group/{id}")
	public JsonData getGroupDownloadFile(@PathVariable("id") @NotNull(message = "id不能为空") @Min(value = 1, message="id必须为大于等于1的整数") Integer fileId, @RequestHeader HttpHeaders headers, HttpServletRequest request,  HttpServletResponse response) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		fileService.downloadGroupFile(fileId, user1, request, response);
		return JsonData.buildSuccess(null);
	}
	
	
	@GetMapping("/group/share/{id}")
	public JsonData getGroupShareFileUrl(@PathVariable("id") @NotNull(message = "id不能为空") @Min(value = 1, message="id必须为大于等于1的整数") Integer fileId, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		String shareUrl = fileService.getGroupFileShareUrl(fileId, user1);
		Map<String,String> map = new HashMap<>();
		map.put("shareUrl", shareUrl);
		return JsonData.buildSuccess(map);
	}
	
	
	@GetMapping("/group/list/{id}")
	public JsonData getGroupDirAndFileListByParentId(@PathVariable("id") @NotNull(message = "parentId不能为空") @Min(value = 0, message="parentId必须为大于等于0的整数") Integer parentId, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));

		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		GroupDirOrFileInfos infos = fileService.getGroupDirAndFileListByParentId(parentId, user1);
		
		return JsonData.buildSuccess(infos);
	}
	
	
	@PostMapping("/group/list/search")
	public JsonData getGroupDirAndFileListByName(@RequestBody @Validated FileSearch fileSearch, @RequestHeader HttpHeaders headers) {
		//User user = loginService.getUser(headers.get("token").get(0));
		
		User user1 = new User();
		user1.setDepartment(1);
		user1.setId(1);
		user1.setLevel(1);
		
		List<Map<String, Object>> files = fileService.getGroupDirAndFileListByName(fileSearch.getName(), fileSearch.getParentId(), user1);
		return JsonData.buildSuccess(files);
	}
	
}
