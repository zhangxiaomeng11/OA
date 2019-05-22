package com.tz.oa.sysmanage.mapper;

import java.util.List;
import com.tz.oa.sysmanage.entity.Role;
import com.tz.oa.sysmanage.entity.RoleToArea;
import com.tz.oa.sysmanage.entity.RoleToDept;
import com.tz.oa.sysmanage.entity.RoleToMenu;

/**
 * 区域的增删改查的mapper代理接口
 * @author Administrator
 *
 */
public interface RoleMapper {
	
	/**
	 * 查询所有角色列表
	 * @return
	 */
	public List<Role> getAllRoleList();

	/**
	 * 增加角色对象
	 * @param role
	 * @return
	 */
	public boolean addRole(Role role);
	
	/**
	 * 批量插入角色菜单对应信息
	 * @param roleMenuList
	 * @return
	 */
	public boolean addRoleToMenuBatch(List<RoleToMenu> roleMenuList);
	/**
	 * 批量插入角色部门对应信息
	 * @param roleDeptList
	 * @return
	 */
	public boolean addRoleToDeptBatch(List<RoleToDept> roleDeptList);
	
	/**
	 * 批量插入角色区域对应信息
	 * @param roleAreaList
	 * @return
	 */
	public boolean addRoleToAreaBatch(List<RoleToArea> roleAreaList);

	/**
	 * 根据角色ID删除角色菜单对应关系
	 * @param roleId
	 * @return
	 */
	public boolean delRoleMenuByRoleId(Long roleId);

	/**
	 * 根据角色ID删除角色部门单对应关系
	 * @param roleId
	 * @return
	 */
	public boolean delRoleDeptByRoleId(Long roleId);

	/**
	 * 根据角色ID删除角色区域对应关系
	 * @param roleId
	 * @return
	 */
	public boolean delRoleAreaByRoleId(Long roleId);

	/**
	 * 删除角色
	 * @param roleId
	 * @return
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
	 * 根据角色id获取角色信息
	 * @param roleId
	 * @return
	 */
	public Role getRoleById(Long roleId);

	/**
	 * 修改角色信息
	 * @param role
	 * @return
	 */
	public boolean updateRole(Role role);

	/**
	 * 根据区域id删除所有区域角色对应关系记录
	 * @param areaId
	 * @return
	 */
	public boolean delRoleAreaByAreaId(Long areaId);
	/**
	 * 根据部门id删除所有部门角色对应关系记录
	 * @param deptId
	 * @return
	 */
	public boolean delRoleDeptByDeptId(Long deptId);
	/**
	 * 根据菜单id删除所有菜单角色对应关系记录
	 * @param menuId
	 * @return
	 */
	public boolean delRoleMenuByMenuId(Long menuId);

	/**
	 * 增加角色菜单对应记录
	 * @param roleMenu
	 * @return
	 */
	public boolean addRoleToMenu(RoleToMenu roleMenu);
	/**
	 * 增加角色区域对应记录
	 * @param roleArea
	 * @return
	 */
	public boolean addRoleToArea(RoleToArea roleArea);
	/**
	 * 增加角色部门对应记录
	 * @param roleDept
	 * @return
	 */
	public boolean addRoleToDept(RoleToDept roleDept);

}
