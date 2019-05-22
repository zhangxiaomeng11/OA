package com.tz.oa.sysmanage.entity;

import java.util.Date;

import com.tz.oa.framework.dto.TreeDto;
 
/**
 * 
 * 类描述 菜单对象bean：  
 * 类名称：com.tz.ssspm.sysmanage.bean.User       
 * 创建人：zxm  
 * 创建时间：2019年5月7日 下午5:37:03
 * @version   V1.0
 */
public class Menu extends TreeDto  implements java.io.Serializable{
	
 	private static final long serialVersionUID = -5177134212031885106L;
	
 
  	private String parentName;
   	private Integer sort;
  	private String href;
  	private String target;
  	private String icon;
  	private String isShow;
  	private String permission;
 
	private String	updateBy;
	private Date updateDate;
	private String remarks;
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
   
	
}