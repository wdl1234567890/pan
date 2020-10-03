package com.example.demo.vo;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DeleteParam {
	
	@NotNull(message = "ids不能为空")
	private List<Integer> ids;
	
	@NotNull(message = "parentId不能为空")
	@Min(message = "parentId必须是大于等于0的整数", value = 0)
	private Integer parentId;
	
	public DeleteParam() {}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	
	
	

}
