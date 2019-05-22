package com.tz.oa.sysmanage.dto;

import java.util.Map;
import com.tz.oa.sysmanage.entity.Role;
 
/**
 * 
 * 类描述 用户对象bean：  
 * 类名称：com.tz.ssspm.sysmanage.bean.User       
 * 创建人：zxm  
 * 创建时间：2019年5月7日 下午5:37:03
 * @version   V1.0
 */
public class RoleDto  implements java.io.Serializable{

 	private static final long serialVersionUID = 3573926540976059111L;
 	
 	private Role role;
 	private Map<Long,Long> deptIds;
 	private Map<Long,Long> menuIds;
 	private Map<Long,Long> areaIds;
 	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Map<Long, Long> getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(Map<Long, Long> deptIds) {
		this.deptIds = deptIds;
	}
	public Map<Long, Long> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(Map<Long, Long> menuIds) {
		this.menuIds = menuIds;
	}
	public Map<Long, Long> getAreaIds() {
		return areaIds;
	}
	public void setAreaIds(Map<Long, Long> areaIds) {
		this.areaIds = areaIds;
	}
	
	
 	
	
}