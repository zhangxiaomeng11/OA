package com.tz.oa.sysmanage.entity;

/**
 * 角色区域对应表
 * 类描述：  
 * 类名称：com.tz.ssspm.sysmanage.bean.RoleToMenu       
 * 创建人：zxm  
 * 创建时间：2019年5月7日 下午5:37:03
 * @version   V1.0
 */
public class UserToRole implements java.io.Serializable {

	private static final long serialVersionUID = 5740104362530862141L;
	
	private Long userId;
	private Long roleId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	 	 
}
