package com.tz.oa.activitimanage.service;

import java.util.List;

import com.tz.oa.activitimanage.bean.LeaveBean;

 
public interface ILeaveBeanService {
 
	/**
	 * 获取请假单列表
	 * @param leaveBean
	 * @return
	 */
	public List<LeaveBean> getLeaveBeanList(LeaveBean leaveBean);
	 
	/**
	 * 获取请假单明细
	 * @param leaveId
	 * @return
	 */
	public LeaveBean getLeaveBeanById(Long leaveId);
  
	/**
	 * 请假单录入
	 * @param leaveBean
	 * @return
	 */
	public boolean addLeaveBean(LeaveBean leaveBean);
	
	/**
	 * 删除请假单
	 * @param leaveId
	 * @return
	 */
	public boolean delLeaveBean(Long leaveId);
	 
	/**
	 * 修改请假单状态
	 * @param leaveId
	 * @param state
	 * @return
	 */
	public boolean updateLeaveBeanState(Long leaveId,Integer state);
	  
	/**
	 * 修改请假单
	 * @param leaveBean
	 * @return
	 */
	public boolean updateLeaveBean(LeaveBean leaveBean);
	
	/**
	 * 处理请假单(请假流程的启动)
	 * @param leaveId
	 */
	public void doLeaveProcess(Long leaveId);

	public LeaveBean getLeaveBeanByTaskId(String taskId);
	 
	
}
