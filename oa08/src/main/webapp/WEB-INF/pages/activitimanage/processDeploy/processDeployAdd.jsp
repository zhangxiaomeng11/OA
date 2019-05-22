<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>区域编辑</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<script type="text/javascript">

$(document).ready(function() {
	$("#name").focus();
	$("#processDeployAddForm").validate({
		submitHandler: function(form){
			form.submit(); 
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#messageBox").text("输入有误，请先更正。");
			if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
				error.appendTo(element.parent().parent());
			} else {
				error.insertAfter(element);
			}
		}	
	});		 
});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/activitimgr/processDeploy/gotoProcessDeployList">流程部署列表</a></li>
		<li class="active"><a href="javascript:void(0);">流程部署</a></li>
	</ul><br/>
	<form id="processDeployAddForm" class="form-horizontal" 
				action="${ctx}/activitimgr/processDeploy/addProcessDeploy" method="post" enctype="multipart/form-data">  
		<div class="control-group">
			<label class="control-label">流程部署名称:</label>
			<div class="controls">
				<input id="name" name="name" class="required input-xlarge" type="text"  maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>		
		<div class="control-group">
			<label class="control-label">流程部署文件:</label>
			<div class="controls">
				<input id="file" name="file" class="required input-xlarge" type="file" />
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		
		<div id="messageBox" class="alert alert-error ${empty processAddErrorMsg ? 'hide' :''} ">
			<button data-dismiss="alert" class="close">×</button>
			<label id="processAddErrorMsg" class="error">${processAddErrorMsg}</label>
		</div>
		
				 
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>