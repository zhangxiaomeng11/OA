package com.tz.oa.activitimanage.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
 
public interface IWorkFlowService {

	/**
	 * 获取所有的流程部署对象
	 * @return
	 */
	public List<Deployment> getDeployments();

	/**
	 * 执行流程部署(zip格式)
	 * @param file
	 * @param deploymentName
	 */
	public Deployment addDeployment(InputStream inputStream,String deploymentName);
	
	/**
	 * 删除流程部署信息
	 * @param deploymentId
	 * @param flag
	 */
	public void delDeployment(String deploymentId,boolean flag);
	
	/**
	 * 获取所有的流程定义信息
	 * @return
	 */
	public List<ProcessDefinition> getProcessDefinitions();
	
	/**
	 * 根据部署id和流程图的名称获取流程图的inputStream
	 * @param deploymentId
	 * @param imageName
	 * @return
	 */
	public InputStream getProcessDefinitionImageStream(String deploymentId,String imageName);

	/**
	 * 根据流程定义的key和业务关系key和流程变量启动流程
	 * @param leaveProcessKey
	 * @param businessKey
	 * @param variables
	 */
	public ProcessInstance startProcess(String leaveProcessKey, String businessKey, Map<String, Object> variables);

	public List<Task> getTaskList(String assingnee);

	public String getTaskFormKeyByTaskId(String taskId);

	public Task getTaskById(String taskId);

	public ProcessInstance getProcessInstanceById(String processInstanceId);

	public List<PvmTransition> getOutcomeListByTaskId(String taskId);

	public List<Comment> getCommentListByTaskId(String taskId);

	public ProcessInstance completeTask(String taskId, String outcome, String commentMsg);

	public ProcessDefinition getProcessDefinitonByInstanceId(String processInstanceId);

	public ActivityImpl getActivitiCoordinate(String processInstanceId);

	
}