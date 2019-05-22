<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户编辑页面</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
	
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
</head>
 <script type="text/javascript">
	$(function() {
		$("#treeTable").treeTable({expandLevel : 5}).show();
	});
    	
 	var menuMgr = { 	    		
		delMenu:function(menuId){	
 	 		if(confirm("您确定要删除此菜单吗?")){  	 			 
 		 		  $.ajax({
 						type:'post',//请求方式
 						url:'${ctx}/sysmgr/menu/delMenu', 
 						dataType:'json', //有几种格式 xml html json text 等常用
 						//data传值的另外一种方式 form的序列化
 						data: {"menuId":menuId},//传递给服务器的参数				
 						success:function(data){//与服务器交互成功调用的回调函数
 							//data就是out.print输出的内容
 							alert(data.result);
 						  	document.getElementById("menuListForm").submit();			
 						}
 					});
					}
			}    		    		
    };
    	
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0);">菜单列表</a></li>
		<li><a href="${ctx}/sysmgr/menu/gotoMenuEdit?editFlag=1">菜单添加</a></li>
	</ul>
	 
	<form id="menuListForm" method="post" action="${ctx}/sysmgr/menu/gotoMenuList.action">
		<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
			<thead>
				<tr>
					<th>名称</th><th>链接</th>
					<th style="text-align:center;">排序</th>
					<th>可见</th>
					<th>权限标识</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>				 
				<c:forEach items="${menuList}" var="menu">
					<tr id="${menu.id}" pId="${menu.parentId}">
						<td nowrap>
							<i class="icon-${not empty menu.icon?menu.icon:' hide'}"></i>
							<a href="${ctx}/sys/menu/toEdit?id=${menu.id}">${menu.name}</a></td>
						<td title="${menu.href}">${menu.href}</td>
						<td style="text-align:center;">
							${menu.sort}						
						</td>
						<td>
							<c:choose>
							   <c:when test="${menu.isShow==1}">显示
							   </c:when>
							   <c:otherwise>不显示</c:otherwise>
							 </c:choose>
						
						</td>
						<td title="${menu.permission}">${menu.permission}</td>
						<td nowrap>
							<a href="${ctx}/sysmgr/menu/gotoMenuEdit?editFlag=2&menuId=${menu.id}">修改</a>
							<a href="javascript:menuMgr.delMenu(${menu.id})">删除</a>
							<a href="${ctx}/sysmgr/menu/gotoMenuEdit?editFlag=1&parentId=${menu.id}">添加下级菜单</a>
						</td>
					</tr>
				</c:forEach>
				
			 
				 
			</tbody>
		</table>
		
	 </form>
</body>
</html>