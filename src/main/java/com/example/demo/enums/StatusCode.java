package com.example.demo.enums;

/**
 * 
 * @ClassName ErrorCode
 * @Description 错误代码和信息
 * @author fuling
 * @date 2020年9月21日 上午9:43:32
 */
public enum StatusCode {
	
	DEFAULT_ERROR(10000, "默认错误代码"),
	
	SUCCESS(20000, "成功"),
	
	PARAM_IS_EMPTY(30001, "参数不能为空"),
	
	FILE_IS_NOT_EXISTED(30002, "文件/文件夹不存在"),
	
	FILE_IS_EXISTED(30003, "文件/文件夹已经存在！"),
	
	NOT_ACCESS(30004, "没有执行该操作的权限"),
	
	IS_OVER_DIR_MAX_LEVEL(30005, "已经超过最大层数，不能再新建文件夹！"),
	
	AUTHORITY_IS_NOT_EXISTED(30006, "权限不存在！"),
	
	PARAM_VALIDATE_FAILED(40000, "参数校验不通过"),
	
	DATABASE_ERROR(50000, "数据库错误！"),
	
	OBS_ERROR(60000, "obs错误代码"),
	
	CLIENT_ERROR(70000, "客户端请求发生错误"),
	
	SERVER_ERROR(80000, "服务端发生错误"),

	MAIL_OR_PHONE_IS_EXISTED(90000,"邮箱或电话号码已存在"),

	FILE_FORMAT_ERROR(90001,"文件格式错误"),

	FILE_READ_ERROR(90002,"文件读取失败"),

	FILE_IS_EMPTY(90003,"文件内容为空"),

	USER_IS_EMPTY(90004,"员工为空"),

	MAIL_IS_EXISTED(90005,""),

	PHONE_IS_EXISTED(90006,""),

	PHONE_FORMAT_ERROR(90007,""),

	CONTENT_HAVE_EMPTY(90008,"");
	;
	
	
	private Integer code;
	
	private String message;
	
	StatusCode(Integer code, String message){
		this.code = code;
		this.message = message;
	}
	
	public Integer code() {
		return code;
	}
	
	public String message() {
		return message;
	}
}
