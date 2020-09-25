package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.example.demo.service.FileService;
import com.example.demo.service.LoginService;
import com.example.demo.service.ObsService;
import com.example.demo.validate.group.Group;
import com.example.demo.vo.FileSearch;
import com.example.demo.vo.JsonData;
import com.obs.services.model.PostSignatureResponse;

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
	
	
	@Value("${obs.config.accessKey}")
	String ak;
	
	@PostMapping
	public JsonData createFile(@RequestBody @Validated(Group.CreateFile.class) File file, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		fileService.createFile(file, user.getId());
		Map<String, Integer> map = new HashMap<>();
		map.put("fileId", file.getId());
		return JsonData.buildSuccess(map);
	}
	
	@PostMapping("/dir")
	public JsonData createDir(@RequestBody @Validated(Group.CreateDir.class) File file, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		fileService.createDir(file, user.getId());
		return JsonData.buildSuccess(null);
	}
	
	
	@GetMapping("/upload/param")
	public JsonData getUploadParam() {
		obsService.createObsClicent();
		PostSignatureResponse postSignature = obsService.getPostSignature();
		obsService.closeObsClient();
		Map<String, Object> data = new HashMap<>();
		data.put("ak", ak);
		data.put("policy", postSignature.getPolicy());
		data.put("signature", postSignature.getSignature());
		data.put("objectKey",UUID.randomUUID().toString().replaceAll("-", ""));
		return JsonData.buildSuccess(data);
	}
	
	
	@DeleteMapping
	public JsonData deleteDirAndFile(@RequestBody @NotEmpty(message = "参数不能为空") List<Integer> ids, @RequestHeader HttpHeaders headers) {
		
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		fileService.batchRemoveFileAndDir(ids, user.getId());
		return JsonData.buildSuccess(null);
	}
	
	@PutMapping
	public JsonData renameDirOrFile(@RequestBody @Validated(Group.UpdateDirOrFile.class) File file, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		fileService.renameFileOrDir(file, user.getId());
		return JsonData.buildSuccess(null);
	}
	
	
	@GetMapping("/{id}")
	public JsonData getDownloadFileUrl(@PathVariable("id") @NotNull(message = "id不能为空") @Min(value = 1, message="id必须为大于等于1的整数") Integer fileId, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		String downloadUrl = fileService.getDownloadUrl(fileId, user.getId());
		Map<String,String> map = new HashMap<>();
		map.put("downloadUrl", downloadUrl);
		return JsonData.buildSuccess(map);
	}
	
	@GetMapping("/share/{id}")
	public JsonData getShareFileUrl(@PathVariable("id") @NotNull(message = "id不能为空") @Min(value = 1, message="id必须为大于等于1的整数") Integer fileId, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		String shareUrl = fileService.getShareUrl(fileId, user.getId());
		Map<String,String> map = new HashMap<>();
		map.put("shareUrl", shareUrl);
		return JsonData.buildSuccess(map);
	}
	
	
	@GetMapping("/list/{id}")
	public JsonData getDirAndFileListByParentId(@PathVariable("id") @NotNull(message = "parentId不能为空") @Min(value = 0, message="parentId必须为大于等于0的整数") Integer parentId, @RequestHeader HttpHeaders headers) {
		
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		List<File> files = fileService.getDirAndFileListByParentId(parentId, user.getId());
		String path = fileService.getPathById(parentId, user.getId());
		Map<String, Object> map = new HashMap<>();
		map.put("path", path);
		map.put("files", files);
		return JsonData.buildSuccess(map);
	
	}
	
	
	@GetMapping("/list/search")
	public JsonData getDirAndFileListByName(@RequestBody @Validated FileSearch fileSearch, @RequestHeader HttpHeaders headers) {
		//从header中拿token
		//根据token从redis中拿用户信息
		User user = loginService.getUser(headers.get("token").get(0));
		//Integer userId = 1;
		List<Map<String, Object>> files = fileService.getDirAndFileListByName(fileSearch.getName(), fileSearch.getParentId(), user.getId());
		return JsonData.buildSuccess(files);
	}
	
}
