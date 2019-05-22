package com.tz.oa.activitimanage.controller;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tz.oa.activitimanage.service.IWorkFlowService;


/**
 * 流程定义的查看
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/activitimgr/processDefinition")
public class ProcessDefinitionController {
	
	private static Logger logger = Logger.getLogger(ProcessDefinitionController.class);
	
	@Autowired
	private IWorkFlowService workFlowService;
	
	/**
	 * 进入流程定义列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/gotoProcessDefinitionList")
	public String gotoProcessDefinitionList(Model model){
		List<ProcessDefinition> processDefinitionList = new ArrayList<ProcessDefinition>();
		processDefinitionList = workFlowService.getProcessDefinitions();
		model.addAttribute("processDefinitionList", processDefinitionList);		
		return "activitimanage/processDefinition/processDefinitionList";		
	}
	

	/**
	 * 进入流程图片查看页面
	 * @param deploymentId
	 * @param imageName
	 * @return
	 */
	@RequestMapping("/gotoProcessDefinitionImage")
	public String gotoProcessDefinitionImage(@ModelAttribute("deploymentId") String deploymentId,
			@ModelAttribute("imageName") String imageName){
 		return "activitimanage/processDefinition/processDefinitionImage";	
	}
	
	/**
	 * 获取流程图,用response.outputStream输出到页面
	 * @param deploymentId
	 * @param imageName
	 * @param response
	 */
	@RequestMapping("/getProcessDefinitionImage")
	public void getProcessDefinitionImage(String deploymentId,
			String imageName,HttpServletResponse response){
	 
		InputStream inputStream = this.workFlowService.
							getProcessDefinitionImageStream(deploymentId, imageName);
		response.setContentType("img/png");
		response.setCharacterEncoding("UTF-8");
		try {
			OutputStream outputStream = response.getOutputStream();			
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len=inputStream.read(buffer,0,1024))!=-1){
				outputStream.write(buffer, 0, len);
			}			
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 
	}
	
 
	
	
}
