package com.tz.oa.sysmanage.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tz.oa.framework.dto.TreeDto;
import com.tz.oa.framework.util.TreeUtils;
import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.entity.Area;
import com.tz.oa.sysmanage.service.IAreaService;

/**
 * 区域管理的controller
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/sysmgr/area")
public class AreaController {
	
	private static Logger logger = Logger.getLogger(AreaController.class);
	
	@Autowired
	private IAreaService areaService;
	//进入区域查询列表功能
	@RequestMapping("/gotoAreaList")
	public String gotoAreaList(Model model){
		//进入功能初始化所有的列表
		List<Area> areaList = this.areaService.getAreaListByUserId(UserUtils.getCurrrentUserId());
		//List<Area> areaList = this.areaService.getAllAreaList();
		List<Area> sortAreaList = new ArrayList<Area>();
		//因为前台组件treeTable正常显示树形结构的数据,就必须让我们的列表按照树形的结构顺序摆放
		TreeUtils.sortTreeList(sortAreaList, areaList, 0l);
		
		model.addAttribute("areaList", sortAreaList);
		return "sysmanage/area/areaList";   
	}
	
	//进入字典编辑功能
	//进入编辑功能的时候，要根据id获取编辑对象的明细记录
	@RequestMapping("/gotoAreaEdit")
	public String gotoAreaEdit(@ModelAttribute("editFlag") int editFlag,
					Long areaId,Long parentId,Model model){
		//如果editFlag = 2 则是进入修改页面，我们需要查询待修改记录的明细信息
		if(editFlag ==2 ){
			Area area=areaService.getAreaById(areaId);
			model.addAttribute("area",area);			
		} 
		if(editFlag==1){//进入增加页面
			if(parentId!=null){
				Area parentArea = areaService.getAreaById(parentId);
				Area area = new Area();
				area.setParentId(parentArea.getId());
				area.setParentName(parentArea.getName());
				model.addAttribute("area",area);			
			}
			
		}
		return "sysmanage/area/areaEdit";   
	}
	
	//获取所有树形结构 区域节点信息
	@RequestMapping("/getParentAreaTreeData")
	public @ResponseBody  List<TreeDto> getParentAreaTreeData(Long areaId){
		List<TreeDto> treeList = new ArrayList<TreeDto>();
		List<Area> areaList = this.areaService.getAllAreaList();
		for(Area area : areaList){
			TreeDto tree = new TreeDto();
			tree.setId(area.getId());
			tree.setName(area.getName()); 
			tree.setParentId(area.getParentId());
			treeList.add(tree);
		}
		
		//如果是进入修改页面,为放置死循环 ,我们必须本节点以及本节点所有的儿子,孙子,全部过滤掉
		//1:把他的儿子,孙子全部找出来
		//2:把他的儿子,孙子从列表里面remove
		if(areaId!=null){//代表进入的是修改页面
			Map<Long,TreeDto> childrenMap = new HashMap<Long,TreeDto>();
			//1:把他的儿子,孙子全部找出来
			TreeUtils.getAllChildrenMap(childrenMap, treeList, areaId);
			//2:把他的儿子,孙子从列表里面remove
			Iterator<TreeDto> treeIterator = treeList.iterator();
			while(treeIterator.hasNext()){
				TreeDto tree = treeIterator.next();
				//如果子节点列表里面存在这个对象,则删除
				if(childrenMap.get(tree.getId())!=null){
					treeIterator.remove();
				}
				//删除本身
				if(tree.getId().equals(areaId)){
					treeIterator.remove();
				}
			}			
		}
		return treeList;
	}
	
	@RequestMapping("/saveArea") 
	public @ResponseBody Map<String,Object> saveArea(@RequestBody Area area,HttpSession session){
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		try{
			if(area!=null&&area.getId()!=null){//修改 
				areaService.updateArea(area);
				resultMap.put("result","修改区域信息成功");
			}else{
				areaService.addArea(area);
				resultMap.put("result","增加区域信息成功");
			}
	  		 
		}catch(Exception e){
			logger.error("操作区域信息失败",e);
			resultMap.put("result","操作区域信息失败");
		}
		return resultMap;
	}  
	//删除区域
	@RequestMapping("/delArea")
	public @ResponseBody Map<String,Object>  delArea(Long areaId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//对于树形结构的数据,我们在删除的时候要注意 ,必须确保无子节点才可以直接删除,否则要给出提示
		if(areaService.getChildCount(areaId).intValue()>0){
			resultMap.put("result", "此区域下面还有子区域,请确定删除所有的子区域后再进行此操作");			 	
			return resultMap;
		}
		
		try{
			if(areaService.delArea(areaId))
				resultMap.put("result", "删除区域信息成功");			 	
		}catch(Exception e){
			resultMap.put("result", "删除区域信息失败");
			logger.error("删除区域信息失败",e);
		}
 		return resultMap;
	}
	
	 
	
	
	
	
}
