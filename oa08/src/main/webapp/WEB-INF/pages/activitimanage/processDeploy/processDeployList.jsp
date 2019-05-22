<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>区域管理</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<script type="text/javascript">
	 
    var processDeployMgr = {    		
   		delprocessDeploy:function(deploymentId){	
  	 		if(confirm("您确定要删除此流程部署吗?")){  	 			 
  		 		  $.ajax({
  						type:'post',//请求方式
  						url:'${ctx}/activitimgr/processDeploy/delProcessDeploy', 
  						dataType:'json', //有几种格式 xml html json text 等常用
  						//data传值的另外一种方式 form的序列化
  						data: {"deploymentId":deploymentId},//传递给服务器的参数				
  						success:function(data){//与服务器交互成功调用的回调函数
  							//data就是out.print输出的内容
  							alert(data.result);
  						  	document.getElementById("processDeployListForm").submit();			
  						}
  					});
   			}  			
   	 	}	
    		
    };
    
    	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0);">流程部署列表</a></li>
		<li><a href="${ctx}/activitimgr/processDeploy/gotoProcessDeployAdd">流程部署</a></li>
	</ul>

	<form id="processDeployListForm" method="post" action="${ctx}/activitimgr/processDeploy/gotoProcessDeployList">
		<table id="processDeployListTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>流程部署ID</th>
					<th>流程名称</th>
					<th>流程发布时间</th>				 
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				 
			<c:forEach items="${deploymentList}" var="deployment">
				<tr>										 
					<td>${deployment.id}</td>
					<td>${deployment.name}</td>
					<%-- <td>${deployment.deploymentTime}</td> --%>
					<td><fmt:formatDate value="${deployment.deploymentTime}" type="date" pattern="yyyy-MM-dd hh:mm:ss"/></td>
					
					<td><a href="javascript:processDeployMgr.delprocessDeploy(${deployment.id})">删除</a></td>
 				</tr>
			</c:forEach>
				 
			</tbody>
		</table>
		 
	 </form>
</body>
</html>