package com.example.demo.vo;

import com.example.demo.enums.StatusCode;

public class JsonData {

	private boolean success;
	private Integer code;
	private Object message;
	private Object data;
	
	
	private JsonData() {}
	
	public static JsonData buildSuccess(Object data) {
		return buildSuccess(StatusCode.SUCCESS.code(), "成功", data);
	}
	
	public static JsonData buildSuccess(Integer code, Object message, Object data) {
		JsonData jsonData = new JsonData();
		jsonData.setSuccess(true);
		jsonData.setCode(code);
		jsonData.setMessage(message);
		jsonData.setData(data);
		return jsonData;
	}
	
	public static JsonData buildError() {
		return buildError(StatusCode.DEFAULT_ERROR.code(), "失败");
	}
	
	public static JsonData buildError(Integer code, Object message) {
		JsonData jsonData = new JsonData();
		jsonData.setSuccess(false);
		jsonData.setCode(code);
		jsonData.setMessage(message);
		return jsonData;
	}
	
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
