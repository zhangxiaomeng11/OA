<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>区域管理</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
 
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0);">待处理任务列表</a></li>
		<li><a href="${ctx}/activitimgr/processTask/gotoProcessTaskDetail">任务处理</a></li>
	</ul>

	<form id="processTaskListForm" method="post" action="${ctx}/activitimgr/processTask/gotoProcessTaskList">
		<table id="processDeployListTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>任务ID</th>
					<th>任务名称</th>
					<th>任务创建时间</th>				 
					<th>任务办理人</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				 
			<c:forEach items="${taskList}" var="task">
				<tr>										 
					<td>${task.id}</td>
					<td>${task.name}</td>
					<td><fmt:formatDate value="${task.createTime}" type="date" pattern="yyyy-MM-dd hh:mm:ss"/></td>					
					<td>${task.assignee}</td>
					<td><a href="${ctx}/activitimgr/processTask/gotoProcessTaskDetail?taskId=${task.id}">处理任务</a></td>
 				</tr>
			</c:forEach>
				 
			</tbody>
		</table>
		 
	 </form>
	 
</body>
</html>