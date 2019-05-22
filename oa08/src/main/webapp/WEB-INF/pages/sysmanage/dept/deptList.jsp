<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>部门管理</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<script type="text/javascript">
	$(function() {
		$("#deptTreeTable").treeTable({expandLevel : 3}).show();
	});
	
    var deptMgr = {
    		
   		delDept:function(deptId){	
 	 		if(confirm("您确定要删除此部门吗?")){  	 			 
		 		  $.ajax({
						type:'post',//请求方式
						url:'${ctx}/sysmgr/dept/delDept', 
						dataType:'json', //有几种格式 xml html json text 等常用
						//data传值的另外一种方式 form的序列化
						data: {"deptId":deptId},//传递给服务器的参数				
						success:function(data){//与服务器交互成功调用的回调函数
							//data就是out.print输出的内容
							alert(data.result);
						  	document.getElementById("deptListForm").submit();			
						}
					});
				}
   	 	}	
    		
    };
    
    	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0);">部门列表</a></li>
		<li><a href="${ctx}/sysmgr/dept/gotoDeptEdit?editFlag=1">部门添加</a></li>
	</ul>

	<form id="deptListForm" method="post" action="${ctx}/sysmgr/dept/gotoDeptList.action">
		<table id="deptTreeTable" class="table table-striped table-bordered table-condensed hide">
			<thead>
				<tr>
					<th>名称</th>
					<th>编号</th>
					<th style="text-align:center;">排序</th>
					<th>负责人</th>
					<th>电话</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				 
			<c:forEach items="${deptList}" var="dept">
				<tr id="${dept.id}" pId="${dept.parentId}">										 
					<td><a href="${ctx}/sysmgr/deptEdit.action?editFlag=2&deptId=${dept.id}">${dept.name}</a></td>
					<td>${dept.code}</td>
					<td style="text-align:center;">${dept.sort}</td>
					<td>${dept.master}</td>
					<td>${dept.phone}</td>	
					<td nowrap>
						<a href="${ctx}/sysmgr/dept/gotoDeptEdit?editFlag=2&deptId=${dept.id}">修改</a>
						<a href="javascript:deptMgr.delDept(${dept.id})">删除</a>
						<a href="${ctx}/sysmgr/dept/gotoDeptEdit?editFlag=1&parentId=${dept.id}">添加下级部门</a>
					</td>		 
				</tr>
				</c:forEach>
				 
			</tbody>
		</table>
		 
	 </form>
</body>
</html>