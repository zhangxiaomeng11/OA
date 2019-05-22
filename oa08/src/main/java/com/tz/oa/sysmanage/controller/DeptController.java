package com.tz.oa.sysmanage.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.tz.oa.sysmanage.entity.Dept;
import com.tz.oa.sysmanage.service.IDeptService;

/**
 * 部门管理的controller
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/sysmgr/dept")
public class DeptController {
	
	private static Logger logger = Logger.getLogger(DeptController.class);
	
	@Autowired
	private IDeptService deptService;
	//进入部门查询列表功能
	@RequestMapping("/gotoDeptList")
	public String gotoDeptList(Model model){
		//进入功能初始化所有的列表
		List<Dept> deptList= this.deptService.getDeptListByUserId(UserUtils.getCurrrentUserId());
		//List<Dept> deptList = this.deptService.getAllDeptList();
		List<Dept> sortDeptList = new ArrayList<Dept>();
		//因为前台组件treeTable正常显示树形结构的数据,就必须让我们的列表按照树形的结构顺序摆放
		TreeUtils.sortTreeList(sortDeptList, deptList, 0l);
		
		model.addAttribute("deptList", sortDeptList);
		return "sysmanage/dept/deptList";   
	}
	
	//进入字典编辑功能
	//进入编辑功能的时候，要根据id获取编辑对象的明细记录
	@RequestMapping("/gotoDeptEdit")
	public String gotoDeptEdit(@ModelAttribute("editFlag") int editFlag,
					Long deptId,Long parentId,Model model){
		//如果editFlag = 2 则是进入修改页面，我们需要查询待修改记录的明细信息
		if(editFlag ==2 ){
			Dept dept=deptService.getDeptById(deptId);
			model.addAttribute("dept",dept);			
		} 
		if(editFlag==1){//进入增加页面
			if(parentId!=null){
				Dept parentDept = deptService.getDeptById(parentId);
				Dept dept = new Dept();
				dept.setParentId(parentDept.getId());
				dept.setParentName(parentDept.getName());
				model.addAttribute("dept",dept);			
			}
			
		}
		return "sysmanage/dept/deptEdit";   
	}
	
	//获取所有树形结构 部门节点信息
	@RequestMapping("/getParentDeptTreeData")
	public @ResponseBody  List<TreeDto> getParentDeptTreeData(Long deptId){
		List<TreeDto> treeList = new ArrayList<TreeDto>();
		List<Dept> deptList = this.deptService.getAllDeptList();
		for(Dept dept : deptList){
			TreeDto tree = new TreeDto();
			tree.setId(dept.getId());
			tree.setName(dept.getName()); 
			tree.setParentId(dept.getParentId());
			treeList.add(tree);
		}
		
		//如果是进入修改页面,为放置死循环 ,我们必须本节点以及本节点所有的儿子,孙子,全部过滤掉
		//1:把他的儿子,孙子全部找出来
		//2:把他的儿子,孙子从列表里面remove
		if(deptId!=null){//代表进入的是修改页面
			Map<Long,TreeDto> childrenMap = new HashMap<Long,TreeDto>();
			//1:把他的儿子,孙子全部找出来
			TreeUtils.getAllChildrenMap(childrenMap, treeList, deptId);
			//2:把他的儿子,孙子从列表里面remove
			Iterator<TreeDto> treeIterator = treeList.iterator();
			while(treeIterator.hasNext()){
				TreeDto tree = treeIterator.next();
				//如果子节点列表里面存在这个对象,则删除
				if(childrenMap.get(tree.getId())!=null){
					treeIterator.remove();
				}
				//删除本身
				if(tree.getId().equals(deptId)){
					treeIterator.remove();
				}
			}			
		}
		return treeList;
	}
	
	@RequestMapping("/saveDept") 
	public @ResponseBody Map<String,Object> saveDept(@RequestBody Dept dept){
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		try{
			if(dept!=null&&dept.getId()!=null){//修改 
				deptService.updateDept(dept);
				resultMap.put("result","修改部门信息成功");
			}else{
				deptService.addDept(dept);
				resultMap.put("result","增加部门信息成功");
			}
	  		 
		}catch(Exception e){
			logger.error("操作部门信息失败",e);
			resultMap.put("result","操作部门信息失败");
		}
		return resultMap;
	}  
	//删除部门
	@RequestMapping("/delDept")
	public @ResponseBody Map<String,Object>  delDept(Long deptId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//对于树形结构的数据,我们在删除的时候要注意 ,必须确保无子节点才可以直接删除,否则要给出提示
		if(deptService.getChildCount(deptId).intValue()>0){
			resultMap.put("result", "此部门下面还有子部门,请确定删除所有的子部门后再进行此操作");			 	
			return resultMap;
		}
		
		try{
			if(deptService.delDept(deptId))
				resultMap.put("result", "删除部门信息成功");			 	
		}catch(Exception e){
			resultMap.put("result", "删除部门信息失败");
			logger.error("删除部门信息失败",e);
		}
 		return resultMap;
	}
	
	 
	//获取所有部门树
	@RequestMapping("/getAllDeptList")
	public @ResponseBody List<TreeDto> getAllDeptList(){
		List<TreeDto> treeList = new ArrayList<TreeDto>();
		List<Dept> deptList = this.deptService.getAllDeptList();
		for(Dept dept:deptList){
			TreeDto tree = new TreeDto();
			tree.setId(dept.getId());tree.setName(dept.getName()); tree.setParentId(dept.getParentId());
			treeList.add(tree);
		}
		return treeList;
	}
	
	
	
	
}
