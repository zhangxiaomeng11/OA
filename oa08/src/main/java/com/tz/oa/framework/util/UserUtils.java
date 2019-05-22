package com.tz.oa.framework.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tz.oa.sysmanage.entity.User;

/**
 * 获取当前登录用户信息的工具类
 * @author Administrator
 *
 */
public class UserUtils {

	/**
	 * 获取当前登录的用户id
	 * @return
	 */
	public static Long getCurrrentUserId(){
		HttpServletRequest request = ((ServletRequestAttributes)
							RequestContextHolder.getRequestAttributes()).getRequest();
		if(request.getSession().getAttribute("user")!=null){
			User user = (User) request.getSession().getAttribute("user");
			return user.getUserId();
		}else{
			return null;
		}
	}
	
	/**
	 * 获取当前登录的用户名称,显示在主页,以示友好
	 * @return
	 */
	public static String getCurrrentUserName(){
		HttpServletRequest request = ((ServletRequestAttributes)
							RequestContextHolder.getRequestAttributes()).getRequest();
		if(request.getSession().getAttribute("user")!=null){
			User user = (User) request.getSession().getAttribute("user");
			return user.getUserName();
		}else{
			return null;
		}
	}
}
