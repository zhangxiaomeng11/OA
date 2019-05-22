package com.tz.oa.sysmanage.Vo;

import java.util.Map;

import com.tz.oa.sysmanage.entity.User;
 
/**
 * 
 * 类描述 用户对象bean：  
 * 类名称：com.tz.ssspm.sysmanage.bean.User       
 * 创建人：zxm  
 * 创建时间：2019年5月7日 下午5:37:03
 * @version   V1.0
 */
public class UserVo  implements java.io.Serializable{

 	private static final long serialVersionUID = 3573926540976059111L;
 	
 	private User user ;
 	private Map<Long,Long> roleIds;
 	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Map<Long, Long> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(Map<Long, Long> roleIds) {
		this.roleIds = roleIds;
	}

	 
	 
	
}