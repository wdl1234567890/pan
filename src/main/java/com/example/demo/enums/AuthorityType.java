package com.example.demo.enums;

/**
 * 
 * @ClassName AuthorityType
 * @Description 权限类型:1表示有权限，0表示无权限
 * @author fuling
 * @date 2020年9月18日 下午8:18:23
 */
public enum AuthorityType {
	
	UNAUTHORIZED(0),
	
	AUTHORIZE(1),
	
	READ(2),
	
	CREATE_DIR(3),
	
	UPLOAD(4),
	
	DELETE(5),
	
	SHARE(6),
	
	DOWNLOAD(7),
	
	RENAME(8);
	
	int value;
	
	AuthorityType(int value){
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
}
