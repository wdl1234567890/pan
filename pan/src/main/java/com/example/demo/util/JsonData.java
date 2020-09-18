package com.example.demo.util;

public class JsonData {
	private int code;  
	private Object data;
	private String msg;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private JsonData(int code, Object data, String msg) {
		super();
		this.code = code;
		this.data = data;
		this.msg = msg;
	}
	
	/**
	 * 返回业务成功的结果json
	 * @param object 成功后的对象
	 * @return 状态码code 1， 以及data包含返回对象
	 */
	public static  JsonData buildSuccess(Object object) {
		return new JsonData(0, object, "");		
	}
	/**
	 * 返回业务错误的结果json
	 * @param err 错误信息
	 * @return 状态码code -1， 以及错误信息
	 */
	public static  JsonData buildError(String err) {
		return new JsonData(-1, null, err);		
	}
	
	/**
	 * 自定义返回业务错误的结果json
	 * @param code 状态码
	 * @param object 返回的对象
	 * @param msg 返回的消息
	 * @return
	 */
	public static  JsonData buildCustom(int code,Object object,String msg) {
		return new JsonData(code, object, msg);		
	}

}
