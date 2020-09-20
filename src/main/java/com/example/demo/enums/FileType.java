package com.example.demo.enums;

/**
 * 
 * @ClassName FileType
 * @Description file类型:0代表个人文件夹，1代表个人文件，2代表群组文件夹，3代表群组文件
 * @author fuling
 * @date 2020年9月18日 下午8:17:16
 */
public enum FileType {
	USER_DIR(0),
	USER_FILE(1),
	GROUP_DIR(2),
	GROUP_FILE(3);
	
	private int value;
	
	FileType(int value){
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
}
