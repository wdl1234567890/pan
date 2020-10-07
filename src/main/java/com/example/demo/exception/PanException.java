package com.example.demo.exception;

/**
 *
 * @ClassName PanException
 * @Description 自定义错误类
 * @author fuling
 * @date 2020年9月21日 上午10:06:40
 */
public class PanException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private Integer code;
	private String message;

	public PanException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
