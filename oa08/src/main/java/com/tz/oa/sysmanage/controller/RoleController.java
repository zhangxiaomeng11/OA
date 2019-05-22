package com.tz.oa.sysmanage.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tz.oa.sysmanage.dto.RoleDto;
import com.tz.oa.sysmanage.entity.Area;
import com.tz.oa.sysmanage.entity.Dept;
import com.tz.oa.sysmanage.entity.Menu;
import com.tz.oa.sysmanage.entity.Role;
import com.tz.oa.sysmanage.entity.RoleToArea;
import com.tz.oa.sysmanage.entity.RoleToDept;
import com.tz.oa.sysmanage.entity.RoleToMenu;
import com.tz.oa.sysmanage.service.IAreaService;
import com.tz.oa.sysmanage.service.IDeptService;
import com.tz.oa.sysmanage.service.IMenuService;
import com.tz.oa.sysmanage.service.IRoleService;

/**
 * 角色管理的controller
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/sysmgr/role")
public class RoleController {
	
	private static Logger logger = Logger.getLogger(RoleController.class);
	
	@Autowired
	private IRoleService roleService;	
	@Autowired
	private IMenuService menuService;
	@Autowired
	private IDeptService deptService;
	@Autowired
	private IAreaService areaService;
	
  	//进入区域查询列表功能
	@RequestMapping("/gotoRoleList")
	public String gotoRoleList(Model model){
		//进入功能初始化所有的角色数据
		List<Role> roleList = new ArrayList<Role>();
		roleList = roleService.getAllRoleList();
		model.addAttribute("roleList", roleList);
		return "sysmanage/role/roleList";   
	}
	
	 
	
	@RequestMapping("/gotoRoleEdit")
	public String gotoRoleEdit(@ModelAttribute("editFlag") int editFlag,
			Long roleId,Model model){
		//不管是修改还是新增,我们都需要在编辑页面将部门树,菜单树,区域树显示出来
		List<Menu> menuList = menuService.getAllMenuList();
		List<Dept> deptList = deptService.getAllDeptList();
		List<Area> areaList = areaService.getAllAreaList();
		model.addAttribute("menuList", menuList);
		model.addAttribute("deptList", deptList);
		model.addAttribute("areaList", areaList);
		
		//修改操作的时候,我们需要查询出该角色本身拥有的各项权限信息
		if(editFlag==2){
			List<RoleToMenu> roleMenuList= roleService.getMenuListByRoleId(roleId);
			List<RoleToDept> roleDeptList= roleService.getDeptListByRoleId(roleId);
			List<RoleToArea> roleAreaList= roleService.getAreaListByRoleId(roleId);
			model.addAttribute("roleMenuList", roleMenuList);
			model.addAttribute("roleDeptList", roleDeptList);
			model.addAttribute("roleAreaList", roleAreaList);

			Role role= roleService.getRoleById(roleId);
			model.addAttribute("role", role);

		}
		return "sysmanage/role/roleEdit";   
	}
	
	@RequestMapping("/saveRole")	
	public @ResponseBody Map<String,Object> saveRole(@RequestBody RoleDto roleDto){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//判断新增还是修改,根据role对象是否有ID
			if(roleDto!=null&&roleDto.getRole()!=null&&roleDto.getRole().getId()!=null){
				roleService.updateRole(roleDto);
				resultMap.put("result", "修改角色信息成功");
			}else{
				roleService.addRole(roleDto);
				resultMap.put("result", "增加角色信息成功");
			}
		}catch(Exception e){	
			logger.error("操作角色信息失败",e);	
			resultMap.put("result", "增加角色信息失败");
		}
		return resultMap;
	}
	
	@RequestMapping("/delRole")
	public @ResponseBody Map<String,Object> delRole(Long roleId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
	 		roleService.delRole(roleId);
			resultMap.put("result", "删除角色信息成功");
			 
		}catch(Exception e){	
			logger.error("删除角色信息失败",e);	
			resultMap.put("result", "删除角色信息失败");
		}
		
		return resultMap;
	}

	
	
	
	
	
	
	
	
	
	
	
  
}
