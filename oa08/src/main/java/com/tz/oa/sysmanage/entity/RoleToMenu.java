package com.tz.oa.sysmanage.entity;

/**
 * 角色菜单对应表
 * 类描述：  
 * 类名称：com.tz.ssspm.sysmanage.bean.RoleToMenu       
 * 创建人：zxm  
 * 创建时间：2019年5月7日 下午5:37:03
 * @version   V1.0
 */
public class RoleToMenu {
	
	private Long roleId;
	private Long menuId;
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	
}
