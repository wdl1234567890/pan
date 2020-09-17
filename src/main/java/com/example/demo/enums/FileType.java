package com.example.demo.enums;

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
