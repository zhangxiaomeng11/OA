package com.tz.oa.sysmanage.service;

import java.util.List;
import com.tz.oa.sysmanage.entity.Dept;

/**
 * 部门管理的业务层接口
 * @author Administrator
 *
 */
public interface IDeptService {
 
	/**
	 * 根据用户id查询用户权限内的部门
	 * @param currrentUserId
	 * @return
	 */
	public List<Dept> getDeptListByUserId(Long currrentUserId);

	/**
	 * 查询所有部门列表
	 * @return
	 */
	public List<Dept> getAllDeptList();
	
	/**
	 * 查询部门明细
	 * @return
	 */
	public Dept getDeptById(Long deptId);
	
	/**
	 * 增加部门
	 * @param dept
	 * @return
	 */
	public boolean addDept(Dept dept);
	/**
	 * 删除部门
	 * @param deptId
	 * @return
	 */
	public boolean delDept(Long deptId);
	
	/**
	 * 修改部门
	 * @param dept
	 * @return
	 */
	public boolean updateDept(Dept dept);
	
	/**
	 * 获取某个节点的子节点数目,用于删除的特殊判断
	 * @param deptId
	 * @return
	 */
	public Integer getChildCount(Long deptId);

}
