package com.tz.oa.framework.dto;

/**
 * 树形结构对象的父类
 * @author Administrator
 *
 */
public class TreeDto {

	private Long id;
 	private String name;
 	private Long parentId;
 	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
 	
 	
}
