package com.tz.oa.sysmanage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.entity.Dept;
import com.tz.oa.sysmanage.entity.RoleToDept;
import com.tz.oa.sysmanage.entity.RoleToMenu;
import com.tz.oa.sysmanage.mapper.DeptMapper;
import com.tz.oa.sysmanage.mapper.RoleMapper;
import com.tz.oa.sysmanage.service.IDeptService;

@Service
public class DeptService implements IDeptService{
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	public List<Dept> getDeptListByUserId(Long currrentUserId) {
		return deptMapper.getDeptListByUserId(currrentUserId);
	}
	
	public List<Dept> getAllDeptList() {
		return deptMapper.getAllDeptList();
	}

	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean delDept(Long deptId) {
		boolean flag = false;
		//除了删除本身,还是删除角色部门对应表的记录
		flag = roleMapper.delRoleDeptByDeptId(deptId);
		flag = deptMapper.delDept(deptId);
		return flag;
		 
	}

	public Integer getChildCount(Long deptId) {
		return deptMapper.getChildCount(deptId);
	}

	public Dept getDeptById(Long deptId) {
		return deptMapper.getDeptById(deptId);
	}
	
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean addDept(Dept dept) {
		boolean flag = false;
		dept.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			dept.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}
		flag = deptMapper.addDept(dept);
		
		//在增加部门的时候,同时需要给超级管理增加一条映射记录
		RoleToDept roleDept = new RoleToDept();
		roleDept.setRoleId(1l);
		roleDept.setDeptId(dept.getId());			
		flag= this.roleMapper.addRoleToDept(roleDept);
				
		return flag;		
	}

	public boolean updateDept(Dept dept) {
		dept.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			dept.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}	
 		return deptMapper.updateDept(dept);
	}

	
  
}
