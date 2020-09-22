package com.example.demo.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * 
 * @ClassName FileSearch
 * @Description 封装file的查找条件
 * @author fuling
 * @date 2020年9月22日 下午12:23:34
 */
public class FileSearch {
	
	@NotNull(message = "parentId不能为空")
	@Min(message = "parentId必须是大于等于0的整数", value = 0)
	private Integer parentId;
	
	
	@NotBlank(message = "name不能为空")
    @Size(message="name允许的长度为1~20", min=1, max=20)
	private String name;
	
	public FileSearch() {}
	
	public FileSearch(Integer parentId, String name) {
		super();
		this.parentId = parentId;
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}
	
	

}
