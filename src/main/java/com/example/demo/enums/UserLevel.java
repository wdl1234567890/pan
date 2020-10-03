package com.example.demo.enums;

public enum UserLevel {
	
	GENERAL_USER(0),
	FILE_ADMIN(1),
	SUPEE_ADMIN(2);
	
	int value;
	
	UserLevel(int value){
		this.value = value;
	}
	
	public int value(){
		return value;
	}
}
