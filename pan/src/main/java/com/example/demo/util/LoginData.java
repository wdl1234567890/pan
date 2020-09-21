/**  

* @Title: 

* @Description: (这里用一句话描述这个方法的作用)  

* @param    参数  

* @return    返回类型  

* @throws  

*/  
package com.example.demo.util;

/**
 * @author 白开水
 *
 */
public class LoginData {
	private int code;  
	private Object token;
	private Object data;
	private String msg;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getToken() {
		return token;
	}
	public void setToken(Object token) {
		this.token = token;
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
	public LoginData(int code, Object token, Object data, String msg) {
		super();
		this.code = code;
		this.token = token;
		this.data = data;
		this.msg = msg;
	}
	public LoginData() {
		super();
	}
	
	/**
	 * 返回业务成功的结果json
	 * @param object 成功后的对象
	 * @return 状态码code 1， 以及data包含返回对象
	 */
	public static  LoginData buildSuccess(Object object1,Object object2) {
		return new LoginData(0, object1,object2, "");		
	}
	/**
	 * 返回业务错误的结果json
	 * @param err 错误信息
	 * @return 状态码code -1， 以及错误信息
	 */
	public static  LoginData buildError(String err) {
		return new LoginData(-1, null,null, err);		
	}
	
	/**
	 * 自定义返回业务错误的结果json
	 * @param code 状态码
	 * @param object 返回的对象
	 * @param msg 返回的消息
	 * @return
	 */
	public static  LoginData buildCustom(int code,Object object,String msg) {
		return new LoginData(code, object,object, msg);		
	}
}
