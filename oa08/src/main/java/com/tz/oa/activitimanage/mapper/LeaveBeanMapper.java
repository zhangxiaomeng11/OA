package com.tz.oa.activitimanage.mapper;

import java.util.List;
import com.tz.oa.activitimanage.bean.LeaveBean;
 
public interface LeaveBeanMapper {
	
	public List<LeaveBean> getLeaveBeanList(LeaveBean leaveBean);
	 
	public LeaveBean getLeaveBeanById(Long leaveId);
	
	public boolean addLeaveBean(LeaveBean leaveBean);
	
	public boolean delLeaveBean(Long leaveId);
	 
	public boolean updateLeaveBeanState(Long leaveId,Integer state);
	
	public boolean updateLeaveBeanProcessInstanceId(Long leaveId,String processInstanceId);
	
	public boolean updateLeaveBean(LeaveBean leaveBean);
	
	
}
