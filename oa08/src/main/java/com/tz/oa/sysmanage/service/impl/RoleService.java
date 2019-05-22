package com.tz.oa.sysmanage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.dto.RoleDto;
import com.tz.oa.sysmanage.entity.Role;
import com.tz.oa.sysmanage.entity.RoleToArea;
import com.tz.oa.sysmanage.entity.RoleToDept;
import com.tz.oa.sysmanage.entity.RoleToMenu;
import com.tz.oa.sysmanage.mapper.RoleMapper;
import com.tz.oa.sysmanage.service.IRoleService;

@Service
public class RoleService implements IRoleService{
	
	@Autowired
	private RoleMapper roleMapper;
	
	public List<Role> getAllRoleList() {
		return  roleMapper.getAllRoleList();
	}
	
	/*
	 * 1:保存角色信息,返回角色ID
	 * 2:根据角色id,部门id组合角色部门对应列表,进行批量插入
	 * 3:根据角色id,区域id组合角色区域对应列表,进行批量插入
	 * 4:根据角色id,菜单id组合角色菜单对应列表,进行批量插入
	 * */
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean addRole(RoleDto roleDto) {
		boolean flag = false;
		//增加角色对象
		Role role = roleDto.getRole();
		role.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		role.setUpdateDate(new Date());
		flag= roleMapper.addRole(role);
		
		Long roleId = role.getId();
		//增加完角色对象以后,根据新增对象的id (角色id) 菜单id组合角色菜单对应列表,进行批量插入
		List<RoleToMenu> roleMenuList = new ArrayList<RoleToMenu>();
		RoleToMenu roleMenu;
		for(Long menuId:roleDto.getMenuIds().values()){
			roleMenu = new RoleToMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menuId);
			roleMenuList.add(roleMenu);
		}
		//批量插入菜单角色对应表
		flag = roleMapper.addRoleToMenuBatch(roleMenuList);
		
		//增加完角色对象以后,根据新增对象的id (角色id) 组合角色部门对应列表,进行批量插入
		List<RoleToDept> roleDeptList = new ArrayList<RoleToDept>();
		RoleToDept roleDept;
		for(Long deptId:roleDto.getDeptIds().values()){
			roleDept = new RoleToDept();
			roleDept.setRoleId(roleId);
			roleDept.setDeptId(deptId);
			roleDeptList.add(roleDept);			
		}
		//批量插入部门角色对应表
		flag = roleMapper.addRoleToDeptBatch(roleDeptList);
				
		//增加完角色对象以后,根据新增对象的id (角色id) 区域id组合角色区域对应列表,进行批量插入
		List<RoleToArea> roleAreaList = new ArrayList<RoleToArea>();
		RoleToArea roleArea;
		for(Long areaId:roleDto.getAreaIds().values()){
			roleArea = new RoleToArea();
			roleArea.setRoleId(roleId);
			roleArea.setAreaId(areaId);
			roleAreaList.add(roleArea);
		}
		//批量插入部门角色对应表
		flag = roleMapper.addRoleToAreaBatch(roleAreaList);
				
		return flag;
	}
	/*
	 * 1:删除角色菜单对应表,删除角色部门对应表,删除角色区域对应表
	 * 2: 删除角色信息
	 */
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean delRole(Long roleId) {
		boolean flag = false;
		flag = roleMapper.delRoleMenuByRoleId(roleId);
		flag = roleMapper.delRoleDeptByRoleId(roleId);
		flag = roleMapper.delRoleAreaByRoleId(roleId);		
		flag = roleMapper.delRole(roleId);	 
		return flag ;
	}

	public List<RoleToMenu> getMenuListByRoleId(Long roleId) {
		return roleMapper.getMenuListByRoleId(roleId);
	}

	public List<RoleToDept> getDeptListByRoleId(Long roleId) {
		return roleMapper.getDeptListByRoleId(roleId);
	}

	public List<RoleToArea> getAreaListByRoleId(Long roleId) {
		return roleMapper.getAreaListByRoleId(roleId);
	}
	
	/*
	 * 1:保存修改的角色信息
	 * 2:根据角色id删除角色部门对应关系,角色菜单对应关系,角色区域对应关系
	 * 3:根据角色id,部门id组合角色部门对应列表,进行批量插入
	 * 4:根据角色id,区域id组合角色区域对应列表,进行批量插入
	 * 5:根据角色id,菜单id组合角色菜单对应列表,进行批量插入
	 * */
	public boolean updateRole(RoleDto roleDto) {
		boolean flag = false;
		//修改角色对象
		Role role = roleDto.getRole();
		role.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		role.setUpdateDate(new Date());
		flag= roleMapper.updateRole(role);
		
		Long roleId = role.getId();
		//根据角色id删除角色部门对应关系,角色菜单对应关系,角色区域对应关系
		flag= this.roleMapper.delRoleMenuByRoleId(roleId);
		flag = this.roleMapper.delRoleDeptByRoleId(roleId);
		flag = this.roleMapper.delRoleAreaByRoleId(roleId);
				
		
		//根据新增对象的id (角色id) 菜单id组合角色菜单对应列表,进行批量插入
		List<RoleToMenu> roleMenuList = new ArrayList<RoleToMenu>();
		RoleToMenu roleMenu;
		for(Long menuId:roleDto.getMenuIds().values()){
			roleMenu = new RoleToMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menuId);
			roleMenuList.add(roleMenu);
		}
		//批量插入菜单角色对应表
		flag = roleMapper.addRoleToMenuBatch(roleMenuList);
		
		//根据新增对象的id (角色id) 组合角色部门对应列表,进行批量插入
		List<RoleToDept> roleDeptList = new ArrayList<RoleToDept>();
		RoleToDept roleDept;
		for(Long deptId:roleDto.getDeptIds().values()){
			roleDept = new RoleToDept();
			roleDept.setRoleId(roleId);
			roleDept.setDeptId(deptId);
			roleDeptList.add(roleDept);			
		}
		//批量插入部门角色对应表
		flag = roleMapper.addRoleToDeptBatch(roleDeptList);
				
		//根据新增对象的id (角色id) 区域id组合角色区域对应列表,进行批量插入
		List<RoleToArea> roleAreaList = new ArrayList<RoleToArea>(); 
		RoleToArea roleArea;
		for(Long areaId:roleDto.getAreaIds().values()){
			roleArea = new RoleToArea();
			roleArea.setRoleId(roleId);
			roleArea.setAreaId(areaId);
			roleAreaList.add(roleArea);
		}
		
		//批量插入部门角色对应表
		flag = roleMapper.addRoleToAreaBatch(roleAreaList);
				
		return flag;
		
	}

	public Role getRoleById(Long roleId) {
		return roleMapper.getRoleById(roleId);
	}

	 


}
