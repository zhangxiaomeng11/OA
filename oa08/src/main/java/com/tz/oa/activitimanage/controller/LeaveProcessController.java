package com.tz.oa.activitimanage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tz.oa.activitimanage.bean.LeaveBean;
import com.tz.oa.activitimanage.service.ILeaveBeanService;
import com.tz.oa.activitimanage.service.IWorkFlowService;
import com.tz.oa.framework.util.UserUtils;
/**
 * 
 * 类描述：springmvc的第一个程序 登陆验证controller  
 * 类名称：com.tz.springmvc.sysmanage.controller.LoginController       
 * 创建人：zxm  
 * 创建时间：2019年5月7日 下午5:37:03
 * @version  V1.0
 * /activitimgr/leaveProces/gotoLeaveProcessList
 */
@Controller
@RequestMapping("/activitimgr/leaveProcess")
public class LeaveProcessController {
	
	private static Logger logger = Logger.getLogger(LeaveProcessController.class);
	
	@Autowired
	private ILeaveBeanService leaveBeanService;
	
	@Autowired
	private IWorkFlowService workFlowService;
	 
	 
	@RequestMapping("/gotoLeaveProcessList")
	public String gotoLeaveProcessList(){
		return "activitimanage/leaveProcess/leaveProcessList";
	}
	
	@RequestMapping("/gotoLeaveProcessEdit")
	public String gotoLeaveProcessEdit(@ModelAttribute("editFlag") int editFlag,Long leaveId,Model model){
		
		//进入修改页面需要将用户明细对象查询出来返回给页面显示
		if(editFlag==2){
			LeaveBean leaveBean = this.leaveBeanService.getLeaveBeanById(leaveId);
			leaveBean.setLeaveDate(new Date());
			model.addAttribute("leaveBean",leaveBean);
		}
		return "activitimanage/leaveProcess/leaveProcessEdit";
	}

	 
	 
	@RequestMapping("/getLeaveProcessList")
	public @ResponseBody Map<String,Object> getLeaveProcessList(){
		//返回对象
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		List<LeaveBean> leaveBeanList = new ArrayList<LeaveBean>();
		LeaveBean leaveBean = new LeaveBean();
		leaveBean.setLeaveUserId(UserUtils.getCurrrentUserId());
		leaveBeanList = this.leaveBeanService.getLeaveBeanList(leaveBean);
		resultMap.put("leaveBeanList", leaveBeanList);
		return resultMap;
	}
	
	 
	@RequestMapping("/saveLeaveProcess") 
	public @ResponseBody Map<String,Object> saveLeaveProcess(@RequestBody LeaveBean leaveBean){
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		
		try{
			if(leaveBean.getLeaveId()!=null){//修改 
				leaveBeanService.updateLeaveBean(leaveBean);
				resultMap.put("result","修改请假单成功");
			}else{
				leaveBeanService.addLeaveBean(leaveBean);
				resultMap.put("result","增加请假单成功");
			}
	  		 
		}catch(Exception e){
			logger.error("操作请假单失败",e);
			resultMap.put("result","操作请假单失败");
		}
		return resultMap;
	} 
		
	@RequestMapping("/delLeaveProcess") 
	public @ResponseBody Map<String,Object> delLeaveProcess(Long leaveId){
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		try{		
			if(leaveBeanService.delLeaveBean(leaveId))
				resultMap.put("result","删除请假单成功");
		}catch(Exception e){
			logger.error("删除请假单失败",e);
			resultMap.put("result","删除请假单失败");
		}	  	 	
		return resultMap;
	}
	
	
	@RequestMapping("/doLeaveProcess") 
	public @ResponseBody Map<String,Object> doLeaveProcess(Long leaveId){
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		try{		
			leaveBeanService.doLeaveProcess(leaveId);
			resultMap.put("result","申请请假成功,请转往任务处理功能提交请假单");

		}catch(Exception e){
			logger.error("申请请假失败",e);
			resultMap.put("result","申请请假失败");

		}
		
		return resultMap;
	}
	
	 
	@RequestMapping("/gotoLeaveProcessTaskDetail")
	public String gotoLeaveProcessTaskDetail(@ModelAttribute("taskId") String taskId,Model model){
		//进入明细页面,我们需要做到以下事情
		System.out.println("taskId="+taskId);
		//1:使用任务id 查询相关的某个流程定义相关的表单(请假单,审批单,,,,`)
		LeaveBean leaveBean = this.leaveBeanService.getLeaveBeanByTaskId(taskId);
		model.addAttribute("leaveBean", leaveBean);
		
		//2:根据此任务id,查询当前任务完成之后的连线名称,返回给页面动态生成处理按钮
		List<PvmTransition>  pvmTransitionList = this.workFlowService.getOutcomeListByTaskId(taskId);
		//构造页面按钮列表
		List<String> buttonNameList = new ArrayList<String>(); 
		if(pvmTransitionList!=null&&pvmTransitionList.size()>0){
			for(PvmTransition pvm:pvmTransitionList){
				String outcomeName = (String) pvm.getProperty("name");
				if(StringUtils.isNotBlank(outcomeName)){
					buttonNameList.add(outcomeName);
				}else{
					buttonNameList.add("确认提交");
				}			
			}
		}
		model.addAttribute("buttonNameList", buttonNameList);
 		
		//3:查询历史的审核信息 待查询
		List<Comment> commentList = this.workFlowService.getCommentListByTaskId(taskId);
		model.addAttribute("commentList",commentList);
				
		return "activitimanage/leaveProcess/leaveProcssTaskDetail";
	}
	
	
	@RequestMapping("/gotoLeaveProcessImage")
	public String gotoProcessDefinitionImage(String processInstanceId,Model model){
		if(StringUtils.isNotEmpty(processInstanceId)){
			ProcessDefinition processDefinition = 
						this.workFlowService.getProcessDefinitonByInstanceId(processInstanceId);
			model.addAttribute("deploymentId",processDefinition.getDeploymentId());
			model.addAttribute("imageName",processDefinition.getDiagramResourceName());
			
			ActivityImpl activitImpl = this.workFlowService.getActivitiCoordinate(processInstanceId);
			model.addAttribute("activitImpl", activitImpl);
		}
		
		return "activitimanage/leaveProcess/leaveProcessImage";
	}
	
	
}
