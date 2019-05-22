package com.tz.oa.sysmanage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.entity.Menu;
import com.tz.oa.sysmanage.entity.RoleToMenu;
import com.tz.oa.sysmanage.mapper.MenuMapper;
import com.tz.oa.sysmanage.mapper.RoleMapper;
import com.tz.oa.sysmanage.service.IMenuService;

@Service
public class MenuService implements IMenuService{
	
	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	public List<Menu> getAllMenuList() {
		return menuMapper.getAllMenuList();
	}

	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean delMenu(Long menuId) {
		boolean flag = false;
		flag = roleMapper.delRoleMenuByMenuId(menuId);
		flag= menuMapper.delMenu(menuId);
		return flag;
	}

	public Integer getChildCount(Long menuId) {
		return menuMapper.getChildCount(menuId);
	}

	public Menu getMenuById(Long menuId) {
		return menuMapper.getMenuById(menuId);
	}
	
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean addMenu(Menu menu) {
		boolean flag = false;
		menu.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			menu.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}
		
		flag = menuMapper.addMenu(menu);
		//在增加菜单的时候,同时需要给超级管理增加一条映射记录
		RoleToMenu roleMenu = new RoleToMenu();
		roleMenu.setRoleId(1l);
		roleMenu.setMenuId(menu.getId());		
		
		flag= this.roleMapper.addRoleToMenu(roleMenu);
		
		return flag;
	}

	public boolean updateMenu(Menu menu) {
		menu.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			menu.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}	
 		return menuMapper.updateMenu(menu);
	}

	public List<Menu> getMenuListByUserId(Long userId) {
		return menuMapper.getMenuListByUserId(userId);
	}

	 



}
