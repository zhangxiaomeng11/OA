package com.tz.oa.sysmanage.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.tz.oa.framework.dto.PageParam;
import com.tz.oa.framework.util.PageUtils;
import com.tz.oa.sysmanage.entity.Dict;
import com.tz.oa.sysmanage.service.IDictService;

/**
 * 字典值相关的控制器转发
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/sysmgr/dict")
public class DictController {
	
	private static Logger logger = Logger.getLogger(DictController.class);
	
	@Autowired
	private IDictService dictService;
  	
	//进入字典查询列表功能
	@RequestMapping("/gotoDictList")
	public String gotoDictList(Model model){	
		//进入利比饿哦功能的时候，初始化下拉菜单
		List<String> dictTypeList = dictService.getAllDictType();
		model.addAttribute("dictTypeList", dictTypeList);
		return "sysmanage/dict/dictList";   
	}
	
	//进入字典编辑功能
	//进入编辑功能的时候，要根据id获取编辑对象的明细记录
	@RequestMapping("/gotoDictEdit")
	public String gotoDictEdit(@ModelAttribute("editFlag") int editFlag,Long dictId,Model model){
		//如果editFlag = 2 则是进入修改页面，我们需要查询待修改记录的明细信息
		if(editFlag ==2 ){
			Dict dict = dictService.getDictById(dictId);
			model.addAttribute("dict",dict);			
		} 
		return "sysmanage/dict/dictEdit";   
	}
	
	//查询字典列表
	@RequestMapping("/getDictList")
	public @ResponseBody List<Dict> getDictList(String type ,String description){	
		//获取查询条件
		Dict dict = new Dict();
		if(StringUtils.isNotEmpty(type))dict.setType(type);
		if(StringUtils.isNotEmpty(description))dict.setDescription(description);
		
		List<Dict> dictList = dictService.getDictList(dict);
		 
		
		return dictList;
	}
	
	
	//查询字典列表
	@RequestMapping("/getDictListPage")
	public @ResponseBody Map<String,Object> getDictListPage
					(String type ,String description,int pageNo,int pageSize){	
		Map<String,Object> resultMap = new HashMap<String,Object>();

		
		//获取查询条件
		Dict dict = new Dict();
		if(StringUtils.isNotEmpty(type))dict.setType(type);
		if(StringUtils.isNotEmpty(description))dict.setDescription(description);
		//构造分页对象
		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo);
		pageParam.setPageSize(pageSize);
		//获取返回的分页数据
		PageInfo<Dict> pageInfo = dictService.getDictListPage(dict, pageParam);
		List<Dict> dictList = pageInfo.getList();
		resultMap.put("dictList", dictList);
		//获取返回的分页条
		String pageStr = PageUtils.pageStr(pageInfo, "dictMgr.getDictListPage");
		resultMap.put("pageStr", pageStr);
		
		return resultMap;
	}
	
	 
	//修改和增加字典
	@RequestMapping("/saveDict")
	public @ResponseBody Map<String,Object>  saveDict(@RequestBody Dict dict){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			if(dict!=null&&dict.getId()!=null){
				//修改字典
				dictService.updateDict(dict);
				resultMap.put("result", "修改字典信息成功");
			}else{
				//增加字典
				dictService.addDict(dict);
				resultMap.put("result", "增加字典信息成功");
			}			
		}catch(Exception e){
			resultMap.put("result", "修改字典信息失败");
			logger.error("修改字典信息失败",e);
		}
		
		return resultMap;
	}
	
	//删除字典
	@RequestMapping("/delDict")
	public @ResponseBody Map<String,Object>  delDict(Long dictId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			if(dictService.delDict(dictId))
				resultMap.put("result", "删除字典信息成功");			 	
		}catch(Exception e){
			resultMap.put("result", "删除字典信息失败");
			logger.error("删除字典信息失败",e);
		}
		
		return resultMap;
	}
	
	
	
	
	
	
	
	
	
	
	
}
