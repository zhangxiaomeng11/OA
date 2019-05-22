<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file= "/WEB-INF/pages/include/taglib.jsp" %>
    
<!DOCTYPE html>
<!-- saved from url=(0027)http://localhost:8080/index -->
<html style="overflow: hidden;"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>权限管理系统</title>
	
<meta charset="utf-8">
<meta name="renderer" content="webkit">
<%@include file = "/WEB-INF/pages/include/head.jsp" %>
 
<style type="text/css">
		#main {padding:0;margin:0;} #main .container-fluid{padding:0 4px 0 6px;}
		#header {margin:0 0 8px;position:static;} #header li {font-size:14px;_font-size:12px;}
		#header .brand {font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:26px;padding-left:33px;}
		#footer {margin:8px 0 0 0;padding:3px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #0663A2;}
		#footer, #footer a {color:#999;} #left{overflow-x:hidden;overflow-y:auto;} #left .collapse{position:static;}
		#userControl>li>a{/*color:#fff;*/text-shadow:none;} #userControl>li>a:hover, #user #userControl>li.open>a{background:transparent;}
	</style>
	<script type="text/javascript">
		
		window.onload = function(){
			
			$("#menu-all li").click(function(){                //给所有的二级菜单加一个点击事件
				$.each($("#menu-all li"),function(i){          //循环遍历所以的二级菜单
					//移除所有二级菜单的.active以及下面i标签的.icon-white
					$(this).removeClass("active").find("i").removeClass("icon-white");
				});
				$(this).addClass("active");                  //给当前点击的菜单加一个.active(比如让背景变蓝)
			});		
		
			$("#menu-all .accordion-heading").click(function(){  //给所有的一级菜单加点击事件
 		        if($(this).next("div").hasClass("in")){  //当前点击操作为向上拉
		            //把向下的图标改为向右的图标
		            $(this).find("i").removeClass("icon-chevron-down").addClass('icon-chevron-right');
		        }else{  //如果当前点击操作为向下展开
		            //把之前展开的图标改为向右
		            $("#menu-all .in").prev().find("i").removeClass("icon-chevron-down").addClass('icon-chevron-right');
		            //把当前展开的图标改为向下
		            $(this).find("a:first i").removeClass("icon-chevron-right").addClass('icon-chevron-down');
		        }		
			});
			
			// 鼠标移动到边界自动弹出左侧菜单
			$("#openClose").mouseover(function(){
				if($(this).hasClass("open")){
					$(this).click();
				}
			});
			
			$("#menu-all .accordion-body li:first i").click();  //初始化点击第一个以及菜单的第一个二级菜单
			
			
		};

	</script>
</head>
<body>
	<div id="main" style="width: auto;">
		<div id="header" class="navbar navbar-fixed-top" >
			<div class="navbar-inner" style="height:45px;padding: 5px">
				<div class="brand"><span id="productName">逗猛企业管理系统</span></div>
				<ul id="userControl" class="nav pull-right">

					<li id="userInfo" class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="个人信息">您好, ${userName}&nbsp;</a>
						<ul class="dropdown-menu">
							<li><a href="userInfo.html" target="mainFrame"><i class="icon-user"></i>&nbsp; 个人信息</a></li>
							<li><a href="changePwd.html" target="mainFrame"><i class="icon-lock"></i>&nbsp;  修改密码</a></li>
						</ul>
					</li>
					<li><a href="${ctx}/logout" title="退出登录">退出</a></li>
					<li>&nbsp;</li>
				</ul>

				 
			</div>
	    </div>
	    <div class="container-fluid">
			<div id="content" class="row-fluid">
				<div id="left" style="width: 160px; height: 604px;">	
					<div class="accordion" id = "menu-all">
					  <c:set var="parentId" value="1" />
					  <c:set var="menuList" value="${menuList}" />
		    		  <c:set var="first" value="true" />
					  
					  <c:forEach items="${menuList}" var="menu">
					  	
					    <c:if test="${menu.parentId eq parentId && menu.isShow eq '1'}" >
					      <div class="accordion-group">
					        <div class="accordion-heading">
					          <a class="accordion-toggle" data-toggle="collapse" data-parent="#menu-all"
					             data-href="#collapse-${menu.id}" href="#collapse-${menu.id}" title="${menu.remarks}">
					            <i class="icon-chevron-${first ? 'down' : 'right'}"></i>&nbsp;${menu.name}
					          </a>
					        </div>
					        <div id="collapse-${menu.id}" class="accordion-body ${first ? 'in' : ''} collapse">
					          <div class="accordion-inner">
					            <ul class="nav nav-list">
					              <c:forEach items="${menuList}" var="menu2" >
					                <c:if test="${menu2.parentId eq menu.id && menu2.isShow eq '1'}">
					                  <li>
					                    <a data-href=".menu3-${menu2.id}" href="${ctx}${menu2.href}"
					                       target="${empty menu2.target ? 'mainFrame' : menu2.target}">
					                      <i class="icon-${menu2.icon}"></i>&nbsp;${menu2.name}
					                    </a>
					                    <ul class="nav nav-list hide" style="margin:0;padding-right:0;">
					                      <c:forEach items="${menuList}" var="menu3">
					                        <c:if test="${menu3.parentId eq menu2.id && menu3.isShow eq '1'}">
					                        <li class="menu3-${menu3.id} hide">
					                          <a data-href=".menu3-${menu3.id}" href="${ctx}${menu3.href}"
					                             target="${empty menu3.target ? 'mainFrame' : menu3.target}">
					                            <i class="icon-${menu3.icon}"></i>&nbsp;${menu3.name}
					                          </a>
					                        </li>
					                        </c:if>
					                      </c:forEach>
					                    </ul>
					                  </li>
					                </c:if>
					              </c:forEach>
					            </ul>
					          </div>
					        </div>
	 					    <c:set var="first" value="false" />				     
					      </div>
					    </c:if>
					  </c:forEach>
					</div> 
				</div>
				<div id="openClose" class="close" style="height: 599px;">&nbsp;</div>
				<div id="right" style="height: 604px; width: 1243px;">
					<iframe id="mainFrame" name="mainFrame" src="" style="overflow: visible; height: 604px;" scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
				</div>
			</div>
		    <div id="footer" class="row-fluid">
				Copyright © 2019-2035 <a href="http://www.doumenghub.cn/" target="_blank">逗猛企业管理系统</a> - Powered By xm V1.0
			</div>
		</div>
	</div>
	<script type="text/javascript"> 
		var leftWidth = 160; // 左侧窗口大小
		var tabTitleHeight = 33; // 页签的高度
		var htmlObj = $("html"), mainObj = $("#main");
		var headerObj = $("#header"), footerObj = $("#footer");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize(){
			var minHeight = 500, minWidth = 980;
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({"overflow-x":strs[1] < minWidth ? "auto" : "hidden", "overflow-y":strs[0] < minHeight ? "auto" : "hidden"});
			mainObj.css("width",strs[1] < minWidth ? minWidth - 10 : "auto");
			frameObj.height((strs[0] < minHeight ? minHeight : strs[0]) - headerObj.height() - footerObj.height() - (strs[1] < minWidth ? 42 : 28));
			$("#openClose").height($("#openClose").height() - 5);
			wSizeWidth();
		}
		function wSizeWidth(){
			if (!$("#openClose").is(":hidden")){
				var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
				$("#right").width($("#content").width()- leftWidth - $("#openClose").width() -5);
			}else{
				$("#right").width("100%");
			}
		}
		
	</script>
	<script src="${ctxJsAndcss}/common/wsize.min.js" type="text/javascript"></script>
