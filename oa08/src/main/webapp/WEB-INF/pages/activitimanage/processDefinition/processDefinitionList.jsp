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
		<li class="active"><a href="javascript:void(0);">流程定义列表</a></li>
		<li><a href="${ctx}/activitimgr/processDefinition/gotoProcessDefinitionImage">流程图</a></li>
	</ul>

	<form id="processDefinitionListForm" method="post">
		<table id="processDefinitionListTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>流程定义ID</th>
					<th>流程定义名称</th>
					<th>流程定义key</th>
					<th>流程定义版本</th>
					<th>流程定义bpmn文件</th>
					<th>流程定义png图片</th>
					<th>流程部署ID</th>				 
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				 
			<c:forEach items="${processDefinitionList}" var="processDefinition">
				<tr>										 
					<td>${processDefinition.id}</td>
					<td>${processDefinition.name}</td>
					<td>${processDefinition.key}</td>
					<td>${processDefinition.version}</td>
					<td>${processDefinition.resourceName}</td>
					<td>${processDefinition.diagramResourceName}</td>
					<td>${processDefinition.deploymentId}</td>
					<td><a href="${ctx}/activitimgr/processDefinition/gotoProcessDefinitionImage?deploymentId=${processDefinition.deploymentId}&imageName=${processDefinition.diagramResourceName}">流程图</a></td>
 				</tr>
			</c:forEach>
				 
			</tbody>
		</table>
		 
	 </form>
</body>
</html>