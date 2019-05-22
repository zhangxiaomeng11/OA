package com.tz.oa.activitimanage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tz.oa.activitimanage.bean.LeaveBean;
import com.tz.oa.activitimanage.mapper.LeaveBeanMapper;
import com.tz.oa.activitimanage.service.ILeaveBeanService;
import com.tz.oa.activitimanage.service.IWorkFlowService;
import com.tz.oa.framework.util.UserUtils;
 
@Service
public class  LeaveBeanService implements ILeaveBeanService{
	
	@Autowired
	private LeaveBeanMapper leaveBeanMapper;
	
	@Autowired
	private IWorkFlowService workFlowService;

	 
	public List<LeaveBean> getLeaveBeanList(LeaveBean leaveBean) {
		List<LeaveBean> leaveBeanList = this.leaveBeanMapper.getLeaveBeanList(leaveBean);
		if(leaveBeanList!=null&&leaveBeanList.size()>0){
			for(LeaveBean leaveBeanTemp:leaveBeanList){
				if(leaveBeanTemp.getLeaveState().intValue()==0){
					leaveBeanTemp.setLeaveStateDesc("初始录入");
				}
				if(leaveBeanTemp.getLeaveState().intValue()==1){
					leaveBeanTemp.setLeaveStateDesc("审核中");
				}
			}
		}
		return leaveBeanList;
	}

	public LeaveBean getLeaveBeanById(Long leaveId) {
		return this.leaveBeanMapper.getLeaveBeanById(leaveId);
	}

	public boolean addLeaveBean(LeaveBean leaveBean) {
		//加入录入的用户id
		//初始化状态的录入
		leaveBean.setLeaveUserId(UserUtils.getCurrrentUserId());
		leaveBean.setLeaveState(0);
		return leaveBeanMapper.addLeaveBean(leaveBean);
	}

	public boolean delLeaveBean(Long leaveId) {
		return this.leaveBeanMapper.delLeaveBean(leaveId);
	}

	public boolean updateLeaveBeanState(Long leaveId, Integer state) {
		return this.leaveBeanMapper.updateLeaveBeanState(leaveId, state);
	}

	public boolean updateLeaveBean(LeaveBean leaveBean) {
		return this.leaveBeanMapper.updateLeaveBean(leaveBean);
	}

	/*
	 *请假单录入以后,根据流程图,我们要启动流程
	 *启动流程我们需要做一下几件事情
	 *	1:获取请假记录,更改请假单状态(流程一旦启动,请假单信息是不能再次编辑)	 
	 *  2:启动流程需要流程定义的key--LeaveBean
	 *  3:根据流程定义的key启动流程实例 
	 *  	1>将表单数据与流程实例关联
	 *  		两种方式: 
	 *  				a:将leaveBean对象以流程变量的方式存储
	 *  				b:将请假单与流程实例产生一个对应关系(利用act_ru_execution的business_key_字段)
	 *  					格式随意:但是必须包括两个内容(流程定义的key+"*"+leaveId)
	 *  	2> 获取当前的操作用户,设置下一步处理人的流程变量
	 *      3> 启动流程变量
	 *   4:加上事务
	 * 
	 */
	@Transactional(isolation=Isolation.DEFAULT ,propagation = Propagation.REQUIRED)
	public void doLeaveProcess(Long leaveId) {	 
		//获取请假记录,更改请假单状态
		this.leaveBeanMapper.updateLeaveBeanState(leaveId, 1);
		//获取流程定义的key
		LeaveBean leaveBean = this.getLeaveBeanById(leaveId);
		String leaveProcessKey = leaveBean.getClass().getSimpleName();
		//构造businessKey --将本次的请假单对象与流程实例产生一个对应关系
		String businessKey = leaveProcessKey+"."+leaveId;
		
		//构造下一步处理人的流程变量4
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("userId", UserUtils.getCurrrentUserId());
		
		//启动流程变量
		ProcessInstance processInstance = this.workFlowService.startProcess(leaveProcessKey,businessKey,variables);
		
		String processInstanceId = processInstance.getProcessInstanceId();
		this.leaveBeanMapper.updateLeaveBeanProcessInstanceId(leaveId, processInstanceId);
		
	}

	/*
	 * 思路:
	 * 	1:根据任务id找到实例id
	 *  2:根据实例id找到流程实例对象
	 *  3:流程实例对象解析出bussinessKey
	 *  4:根据biussine_key 获取请假单对象
	 */
	public LeaveBean getLeaveBeanByTaskId(String taskId) {
		//1:根据任务id找到实例id
		Task task = this.workFlowService.getTaskById(taskId);
		//2:根据实例id找到流程实例对象
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance =  this.workFlowService.getProcessInstanceById(processInstanceId);
		//3:流程实例对象解析出bussinessKey
		String businessKey = processInstance.getBusinessKey();
		String leaveId = businessKey.split("\\.")[1];
		//4:根据biussine_key 获取请假单对象
		return this.leaveBeanMapper.getLeaveBeanById(new Long(leaveId));
	}

	 
	 
	
}
