package com.tz.oa.sysmanage.controller;



import java.util.ArrayList;
import java.util.HashMap;
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

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.tz.oa.framework.dto.PageParam;
import com.tz.oa.framework.util.PageUtils;
import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.Vo.UserVo;
import com.tz.oa.sysmanage.dto.UserDto;
import com.tz.oa.sysmanage.entity.Role;
import com.tz.oa.sysmanage.entity.User;
import com.tz.oa.sysmanage.entity.UserToRole;
import com.tz.oa.sysmanage.service.IRoleService;
import com.tz.oa.sysmanage.service.IUserService;

/**
 * 用户用户相关的控制器转发
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/sysmgr/user")
public class UserController {
	
	private static Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRoleService roleService;
	
	@RequestMapping("/gotoUserInfo")
	public String gotoUserInfo(){		 
		return "sysmanage/user/userInfo";   
	}
	
	
	@RequestMapping("/gotoChangePwd")
	public String gotoChangePwd(){		 
		return "sysmanage/user/changePwd";   
	}
	
	
	@RequestMapping("/gotoUserList")
	public String gotoUserList(){		 
		return "sysmanage/user/userList";   
	}
	
	//进入用户编辑(修改-增加)页面
	@RequestMapping("/gotoUserEdit")
	public String gotoUserEdit(@ModelAttribute("editFlag") int editFlag,
			Long userId,Model model){
		//不管是进入修改页面还是增加页面,都要将所有的角色列表查询出来
		List<Role> roleList = roleService.getAllRoleList();
		model.addAttribute("roleList", roleList);
		//如果是进入修改页面,我们必须将用户的角色信息查询出来返回给页面显示
		if(editFlag==2){
			UserDto userDto = this.userService.getUserInfoById(userId);
			model.addAttribute("userDto", userDto);
			//还需要将当前用户的角色信息查询出来,并给页面默认勾选上 pm_sys_user_role
			List<UserToRole> userRoleList = userService.getUserRoleByUserId(userId);
			if(userRoleList!=null){
				Map<Long,Long> roleCheckMap = new HashMap<Long,Long>();
				for(UserToRole userRole :userRoleList){
					roleCheckMap.put(userRole.getRoleId(), userRole.getRoleId());
				}
				model.addAttribute("roleCheckMap",roleCheckMap);
			}
		}
		return "sysmanage/user/userEdit";   
	}
	
	//保存用户信息
	@RequestMapping("/saveUser")
	public @ResponseBody Map<String,Object> saveUser(@RequestBody UserVo userVo){		 
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		try{
			if(userVo!=null&&userVo.getUser()!=null&&userVo.getUser().getUserId()!=null){
				userService.updateUserVo(userVo);				
				resultMap.put("result", "修改用户信息成功");
			}else{//增加
				userService.addUser(userVo);
				resultMap.put("result", "增加用户信息成功");
			}	
		}catch(Exception e){
			resultMap.put("result", "操作用户失败");
			logger.error("操作用户失败",e);
		}		
		return resultMap;
	}
	
	//分页查询用户列表
	@RequestMapping("/getUserList")
	public @ResponseBody Map<String,Object> getUserList(Long deptId,String userName,
							int pageNo,int pageSize){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//获取查询条件
		User user = new User();
		if(deptId!=null)user.setDeptId(deptId);
		if(userName!=null)user.setUserName(userName);
		//获取分页条件
		PageParam pageParam = new PageParam();
		pageParam.setPageNo(pageNo);
		pageParam.setPageSize(pageSize);
		//获取分页数据和
		List<UserDto> userList = new ArrayList<UserDto>();
		PageInfo<UserDto> pageInfo= this.userService.getUserDtoList(user, pageParam);
		userList = pageInfo.getList();
		resultMap.put("userList", userList);
		//获取分页条的java代码
		String pageStr = PageUtils.pageStr(pageInfo, "userMgr.getUserListPage");
		resultMap.put("pageStr", pageStr);
		
		return resultMap;
	}
	
	
	//删除用户信息
	@RequestMapping("/delUser") 
	public @ResponseBody Map<String,Object> delUser(Long userId){
		Map<String,Object> resultMap = new HashMap<String,Object>();	
		try{		
			if(userService.delUser(userId))
				resultMap.put("result","删除用户信息成功");
		}catch(Exception e){
			logger.error("删除用户失败",e);
			resultMap.put("result","删除用户信息失败");
		}		  	 	
		return resultMap;
	}
	
	@RequestMapping("/saveChangePwd")
	public @ResponseBody Map<String,Object> saveChangePwd(String oldPassword,String newPassword){		 
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//1:可以通过session获取id ,同样也可以封装一个统一的方法来获取id
		Long userId= UserUtils.getCurrrentUserId();
		//第一步:确保我们的旧密码是正确的 getUserById()
		//第二步:如果旧密码 则进行更新密码的操作
		User user = userService.getUserById(userId);
		//2:校验原密码是否有效
		boolean validOldPass = userService.vaildatePassword(oldPassword, user.getPassword());
		if(!validOldPass){
			resultMap.put("result", "修改密码失败,原密码错误");
		}else{
			if(StringUtil.isNotEmpty(newPassword)){
				try{
					if(userService.updateUserPassword(userId, newPassword)){
						resultMap.put("result", "修改密码成功");
					}else{
						resultMap.put("result", "修改密码失败");
					}
					
				}catch(Exception e){
					resultMap.put("result", "修改密码失败");
					logger.error("修改密码失败",e);
				}
			}else{
				resultMap.put("result", "新密码不能为空");

			}
		}
		
		return resultMap;
	}
	 
	@RequestMapping("/getUserInfoById")
	public @ResponseBody UserDto  getUserInfoById(){		 
		//1:可以通过session获取id ,同样也可以封装一个统一的方法来获取id
		Long userId= UserUtils.getCurrrentUserId();
		UserDto userDto = userService.getUserInfoById(userId);		
		return userDto;  
	}
	
	
	@RequestMapping("/saveSelfUserInfo")
	public @ResponseBody Map<String,Object>  saveSelfUserInfo(@RequestBody User user){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			if(userService.updateUser(user)){
				resultMap.put("result", "修改用户信息成功");
			}else{
				resultMap.put("result", "修改用户信息失败");
			}
			
		}catch(Exception e){
			resultMap.put("result", "修改用户信息失败");
			logger.error("修改用户信息失败",e);
		}
		
		return resultMap;
	}
	
	
	
	
	
	
	
	
	
	
	
}
