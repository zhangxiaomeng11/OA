package com.tz.oa.sysmanage.service;

import java.util.List;

import com.tz.oa.sysmanage.dto.RoleDto;
import com.tz.oa.sysmanage.entity.Role;
import com.tz.oa.sysmanage.entity.RoleToArea;
import com.tz.oa.sysmanage.entity.RoleToDept;
import com.tz.oa.sysmanage.entity.RoleToMenu;

/**
 * 角色管理的业务层接口
 * @author Administrator
 *
 */
public interface IRoleService {
 
	/**
	 * 查询所有角色列表
	 * @return
	 */
	public List<Role> getAllRoleList();

	/**
	 * 增加角色信息
	 * @param roleDto
	 * @return
	 */
	public boolean addRole(RoleDto roleDto);

	/**
	 * 删除角色信息以及跟角色相关的对应关系表
	 * @param roleId
	 */
	public boolean delRole(Long roleId);

	/**
	 * 根据角色id查询角色菜单对应关系
	 * @param roleId
	 * @return
	 */
	public List<RoleToMenu> getMenuListByRoleId(Long roleId);

	/**
	 * 根据角色id查询角色部门对应关系
	 * @param roleId
	 * @return
	 */
	public List<RoleToDept> getDeptListByRoleId(Long roleId);

	/**
	 * 根据角色id查询角色区域对应关系
	 * @param roleId
	 * @return
	 */
	public List<RoleToArea> getAreaListByRoleId(Long roleId);

	/**
	 * 修改角色信息以及角色对应关系表
	 * @param roleDto
	 */
	public boolean updateRole(RoleDto roleDto);

	/**
	 * 根据角色id获取角色信息
	 * @param roleId
	 * @return
	 */
	public Role getRoleById(Long roleId);
	
	 
}
