package com.tz.oa.sysmanage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.entity.Area;
import com.tz.oa.sysmanage.entity.RoleToArea;
import com.tz.oa.sysmanage.mapper.AreaMapper;
import com.tz.oa.sysmanage.mapper.RoleMapper;
import com.tz.oa.sysmanage.service.IAreaService;

@Service
public class AreaService implements IAreaService{
	
	@Autowired
	private AreaMapper areaMapper;
	
	
	@Autowired
	private RoleMapper roleMapper;
	
	
	public List<Area> getAreaListByUserId(Long currrentUserId) {
		return this.areaMapper.getAreaListByUserId(currrentUserId);
	}
	
	
	public List<Area> getAllAreaList() {
		return areaMapper.getAllAreaList();
	}
	
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean delArea(Long areaId) {
		boolean flag = false;
		//除了删除本身,还是删除角色区域对应表的记录
		flag = roleMapper.delRoleAreaByAreaId(areaId);
		flag =  areaMapper.delArea(areaId);		
		return flag;		
	}

	public Integer getChildCount(Long areaId) {
		return areaMapper.getChildCount(areaId);
	}

	public Area getAreaById(Long areaId) {
		return areaMapper.getAreaById(areaId);
	}
	
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean addArea(Area area) {
		boolean flag = false;
		area.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			area.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}	
		flag  = areaMapper.addArea(area);
		//在增加区域的时候,同时需要给超级管理增加一条映射记录
		RoleToArea roleArea = new RoleToArea();
		roleArea.setRoleId(1l);
		roleArea.setAreaId(area.getId());
		flag= this.roleMapper.addRoleToArea(roleArea);
				
		return flag;
	}

	public boolean updateArea(Area area ) {
		area.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			area.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}	
 		return areaMapper.updateArea(area);
	}
	

	 



}
