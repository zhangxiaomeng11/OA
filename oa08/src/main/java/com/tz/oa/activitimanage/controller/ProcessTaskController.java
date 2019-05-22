package com.tz.oa.activitimanage.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tz.oa.activitimanage.service.IWorkFlowService;
import com.tz.oa.framework.util.UserUtils;


/**
 * 流程部署增删改功能
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/activitimgr/processTask")
public class ProcessTaskController {
	
	private static Logger logger = Logger.getLogger(ProcessTaskController.class);
	
	@Autowired
	private IWorkFlowService workFlowService;
	
	@RequestMapping("/gotoProcessTaskList")
	public String gotoProcessTaskList(Model model){
		List<Task> taskList = new ArrayList<Task>();
		String assingnee = UserUtils.getCurrrentUserId().toString();	
		taskList = this.workFlowService.getTaskList(assingnee);
		model.addAttribute("taskList", taskList);
		return "activitimanage/processTask/processTaskList";	
	}
	
	//进入用户个人密码修改页面
		@RequestMapping("/gotoProcessTaskDetail")
		public String gotoProcessTaskDetail(String taskId){
			String taskDetailUrl = this.workFlowService.getTaskFormKeyByTaskId(taskId);
			//这里因为每个任务的明细页面不一样,可以通过流程设计的formkey重定向到每类任务的明细页面
			//通过formService可以获取每个任务对应的url
			return "redirect:"+taskDetailUrl+"?taskId="+taskId;
			//return "redirect:/activitimgr/leaveProcess/gotoLeaveProcessTaskDetail?taskId="+taskId;
	 	}
	
		
		
		@RequestMapping("/completeTask")
		public @ResponseBody Map<String,Object> completeTask(String taskId,String outcome,String commentMsg){
			System.out.println("taskId="+taskId);
			System.out.println("outcome="+outcome);
			System.out.println("commentMsg="+commentMsg);
			 
			Map<String,Object> resultMap = new HashMap<String,Object>();	
			try{		
				this.workFlowService.completeTask(taskId, outcome, commentMsg);
				resultMap.put("result","任务处理成功");
			}catch(Exception e){
				logger.error("任务处理失败",e);
				resultMap.put("result","任务处理失败");
			}		
			return resultMap;
			
		}
}
