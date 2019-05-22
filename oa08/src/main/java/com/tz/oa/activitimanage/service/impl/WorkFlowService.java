package com.tz.oa.activitimanage.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tz.oa.activitimanage.service.IWorkFlowService;
import com.tz.oa.framework.util.UserUtils;

@Service
public class WorkFlowService implements IWorkFlowService {
 
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private FormService formService;
	

	public List<Deployment> getDeployments() {
		 return repositoryService.createDeploymentQuery()
				 				.orderByDeploymenTime().desc().list();
	}


	public Deployment addDeployment(InputStream inputStream, String deploymentName) {
		Deployment deploy = repositoryService
				.createDeployment()
				.addZipInputStream(new ZipInputStream(inputStream))				
				.name(deploymentName)
				.deploy();
		return deploy;
	}


	public void delDeployment(String deploymentId, boolean flag) {
		repositoryService.deleteDeployment(deploymentId, flag);
	}
	
	/**
	 * 从这里可以还进行一步操作,
	 * 当我们在实际的应用中,查询流程定义的时候,其实只需要查询最新版本的流程定义信息
	 */
	public List<ProcessDefinition> getProcessDefinitions(){
		return repositoryService.createProcessDefinitionQuery()
					.orderByProcessDefinitionKey().orderByProcessDefinitionVersion().desc()
					.list();
	}


	public InputStream getProcessDefinitionImageStream(String deploymentId, String imageName) {
 		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}


	public ProcessInstance startProcess(String leaveProcessKey, String businessKey, Map<String, Object> variables) {
 		return runtimeService.startProcessInstanceByKey(leaveProcessKey, businessKey, variables);
	}


	public List<Task> getTaskList(String assingnee){
		return taskService.createTaskQuery()
				 .taskAssignee(assingnee)
				 .list();
	}
	
	public String getTaskFormKeyByTaskId(String taskId){
		TaskFormData taskFormData= formService.getTaskFormData(taskId);
		String url = taskFormData.getFormKey();
		
		return url;
	}


	public Task getTaskById(String taskId) {
		return taskService.createTaskQuery()
							.taskId(taskId)
							.singleResult();
	}


	public ProcessInstance getProcessInstanceById(String ProcessInstanceId) {
		return runtimeService.createProcessInstanceQuery()
							 .processInstanceId(ProcessInstanceId)							
							.singleResult();
	}

	/**
	 * 根据任务ID,获取当前任务完成之后的连线名称,返回页面生成处理按钮
	 * @Title: getOutcomeListByTaskId  
	 * @Description: TODO
	 * @param taskId
	 * @return
	 */
	public List<PvmTransition> getOutcomeListByTaskId(String taskId){		
		/* 1:根据任务ID 查询任务对象 ,获取流程定义ID和流程实例ID
		 * 2:根据流程实例id找到流程实例对象,查询当前活动节点
		 * 3:根据流程定义id,查找流程定义对象(processDfinition->processDefinitionEntity)
		 * 4:根据当前活动节点从processDefinitonEntity对象里面获取当前活动后面的连线信息
		 */
		//1:根据任务ID 查询任务对象 ,获取流程定义ID和流程实例ID
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		String processInstanceId = task.getProcessInstanceId();
		//2:根据流程实例id找到流程实例对象,查询当前活动节点
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
											.processInstanceId(processInstanceId)
											.singleResult();
		String activityId = processInstance.getActivityId();
		//3:根据流程定义id,查找流程定义对象(processDfinition->processDefinitionEntity)
		/*ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
												.processDefinitionId(processDefinitionId)
												.singleResult();*/
		//??流程定义没有这个信息
		ProcessDefinitionEntity processDefinitionEntity =(ProcessDefinitionEntity)
												repositoryService.getProcessDefinition(processDefinitionId);
		//4:根据当前活动节点从processDefinitonEntity对象里面获取当前活动后面的连线信息
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		List<PvmTransition>  pvmTransitionList = activityImpl.getOutgoingTransitions();
		
		if(pvmTransitionList!=null&&pvmTransitionList.size()>0){
			for(PvmTransition pvm:pvmTransitionList){
				System.out.println(pvm.getId());
				System.out.println(pvm.getSource());
			}
		}
 		return pvmTransitionList;
		
	}

	public List<Comment> getCommentListByTaskId(String taskId){
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
 		String processInstanceId = task.getProcessInstanceId();
		return taskService.getProcessInstanceComments(processInstanceId);
	}
	
	/**
	 * 根据任务id完成任务
	 */
	/*@Transactional(isolation=Isolation.DEFAULT ,propagation = Propagation.REQUIRED)
	public void completeTask(String taskId, String outcome, String commentMsg) {
		
		 * 完成任务的同时我们需要做以下几件事情
		 * 1: 根据页面按钮操作的不同来决定流程的走向(流程变量)
		 * 2: 下一个任务的处理人(根据当前操作用户通过监听器来确定下一任务的处理人)
		 * 3: 添加评论记录
		 * 4: 执行完任务以后还要判断是否此任务结束,需要将请假单状态更改
		 
		//1: 2
		Map<String,Object> variables = new HashMap<String,Object>();
		if(StringUtils.isNoneEmpty(outcome)&&(!outcome.equals("确认提交"))){
			variables.put("outcome", outcome);
		}
		//3 注意:需要给用户id设置
		Authentication.setAuthenticatedUserId(UserUtils.getCurrrentUserName());
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
 		String processInstanceId = task.getProcessInstanceId();
		if(StringUtils.isNotEmpty(commentMsg))
			taskService.addComment(taskId, processInstanceId, commentMsg);
		//4 完成任务
		taskService.complete(taskId, variables);
		//5:完成任务之后,需要判断是否是最后一步,如果是流程结束,需要更改请假单状态1->2
		ProcessInstance  processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
		
		 
	}*/
	
	public ProcessInstance completeTask(String taskId, String outcome, String commentMsg) {
		/*
		 * 完成任务的同时我们需要做以下几件事情
		 * 1: 根据页面按钮操作的不同来决定流程的走向(流程变量)
		 * 2: 下一个任务的处理人(根据当前操作用户通过监听器来确定下一任务的处理人)
		 * 3: 添加评论记录
		 * 4: 执行完任务以后还要判断是否此任务结束,需要将请假单状态更改
		 */
		//1: 2
		Map<String,Object> variables = new HashMap<String,Object>();
		if(outcome!=null&&(!outcome.equals("确认提交"))){ //代表有多个连线选择
			variables.put("outcome", outcome);
		}
		//3 注意:需要给用户id设置
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		Authentication.setAuthenticatedUserId(UserUtils.getCurrrentUserName());
 		String processInstanceId = task.getProcessInstanceId();
		if(StringUtils.isNotEmpty(commentMsg))
			taskService.addComment(taskId, processInstanceId, commentMsg);
		//4 完成任务
		taskService.complete(taskId, variables);
		//5:完成任务之后,需要判断是否是最后一步,如果是流程结束,需要更改请假单状态1->2
		ProcessInstance  processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
		
		return processInstance;
		 
	}
	
	
	
	public ProcessDefinition getProcessDefinitonByInstanceId(String processInstanceId){
		//查找流程实例对象
		ProcessInstance  processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
		String processDefinitionId ="";
		if(processInstance==null){//代表流程已经结束,需要去历史库查询
			 HistoricProcessInstance historicProcessInstance=  historyService.createHistoricProcessInstanceQuery()
									.processInstanceId(processInstanceId)
									.singleResult();
			 processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		}else{
			processDefinitionId = processInstance.getProcessDefinitionId();
		}
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId)
				.singleResult();
		return processDefinition;
	}
	
	public ActivityImpl getActivitiCoordinate(String processInstanceId){
		//查找流程实例对象
		ProcessInstance  processInstance = runtimeService.createProcessInstanceQuery()
										.processInstanceId(processInstanceId)
										.singleResult();
		if(processInstance!=null){
			//获取此流程实例当前的活动id
			String activityId = processInstance.getActivityId();			
			//通过流程定义的实现类ProcessDefinitionEntity 来获取当前活动的坐标
			String processDefinitionId = processInstance.getProcessDefinitionId();
			ProcessDefinitionEntity processDefinitionEntity =(ProcessDefinitionEntity)
					repositoryService.getProcessDefinition(processDefinitionId);
			ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);	
			return activityImpl;
		}
		else{
			return null;
		}
		
	}
	
	
}
