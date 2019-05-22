package com.tz.oa.sysmanage.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.tz.oa.sysmanage.entity.Area;

/**
 * 区域管理的业务层接口
 * @author Administrator
 *
 */
public interface IAreaService {
 
	/**
	 * 根据用户id查询用户所拥有的区域权限
	 * @param currrentUserId
	 * @return
	 */
	public List<Area> getAreaListByUserId(Long currrentUserId);
	/**
	 * 查询所有区域列表
	 * @return
	 */
	public List<Area> getAllAreaList();
	
	/**
	 * 查询区域明细
	 * @return
	 */
	public Area getAreaById(Long areaId);
	
	/**
	 * 增加区域
	 * @param area
	 * @return
	 */
	public boolean addArea(Area area);
	/**
	 * 删除区域
	 * @param areaId
	 * @return
	 */
	public boolean delArea(Long areaId);
	
	/**
	 * 修改区域
	 * @param area
	 * @return
	 */
	public boolean updateArea(Area area);
	
	/**
	 * 获取某个节点的子节点数目,用于删除的特殊判断
	 * @param areaId
	 * @return
	 */
	public Integer getChildCount(Long areaId);

	
}
