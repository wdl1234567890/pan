package com.example.demo.vo;

import java.util.Map;

public class JsonData {

	private Integer code;
	private String message;
	private Object data;
	
	
	private JsonData() {}
	
	public static JsonData buildSuccess(Object data) {
		return buildSuccess(10000, "成功", data);
	}
	
	public static JsonData buildSuccess(Integer code, String message, Object data) {
		JsonData jsonData = new JsonData();
		jsonData.setCode(code);
		jsonData.setMessage(message);
		jsonData.setData(data);
		return jsonData;
	}
	
	public static JsonData buildError() {
		return buildError(20000, "失败");
	}
	
	public static JsonData buildError(Integer code, String message) {
		JsonData jsonData = new JsonData();
		jsonData.setCode(code);
		jsonData.setMessage(message);
		return jsonData;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
