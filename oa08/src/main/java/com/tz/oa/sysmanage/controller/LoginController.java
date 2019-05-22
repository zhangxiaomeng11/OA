package com.tz.oa.sysmanage.controller;



import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.entity.Menu;
import com.tz.oa.sysmanage.entity.User;
import com.tz.oa.sysmanage.service.IMenuService;
import com.tz.oa.sysmanage.service.IUserService;

/**
 * 用于注解开发的LoginController
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	
	private static Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IMenuService menuService;
	
	@RequestMapping("/gotoLogin")
	public String gotoLogin(){		 
		return "login";   
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam(value="loginName" ,required=true) String loginName,
						String password,Model model,HttpSession session){	
		logger.info("登陆用户名:"+loginName);
 
		//用redirect和forward标签返回
		//注意:用这两个标签必须写全路径,不适应视图解析的逻辑视图
		if(StringUtils.isNotEmpty(loginName)&&
				StringUtils.isNotEmpty(password)){
			User user = userService.loginUser(loginName, password);
			if(user!=null){
				logger.info("登陆成功");
				session.setAttribute("user", user);
				return "redirect:/main" ;
			}else{
				model.addAttribute("loginFlag","登陆失败,请输入正确的用户名和密码");
				return "forward:/WEB-INF/pages/login.jsp";
			}						
		}else{
			model.addAttribute("loginFlag","登陆失败,请输入正确的用户名和密码");
			return "forward:/WEB-INF/pages/login.jsp";
		}
 		
	}
	
	@RequestMapping("/main")
	public String main(Model model){	
		//将操作用户对应的所有菜单查询出来,返回给页面进行动态加载
		Long userId = UserUtils.getCurrrentUserId();
		List<Menu> menuList = this.menuService.getMenuListByUserId(userId);
		model.addAttribute("menuList",menuList);
		String userName = UserUtils.getCurrrentUserName();
		model.addAttribute("userName",userName);
		return "main/main";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "forward:/WEB-INF/pages/login.jsp";
	}

}
