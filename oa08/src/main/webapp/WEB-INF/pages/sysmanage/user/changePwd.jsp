<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>修改密码</title>	
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<%@ include file="/WEB-INF/pages/include/head.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#userChangePwdForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					var oldPassword = $("#oldPassword").val();
					var newPassword = $("#newPassword").val();
					loading('正在提交，请稍等...');
 					$.ajax({
						type:'post',//请求方式
						url:'${ctx}/sysmgr/user/saveChangePwd', 
						dataType:'json', //返回数据的几种格式 xml html json text 等常用
						//data传值的另外一种方式 form的序列化
						data: {"oldPassword":oldPassword,"newPassword":newPassword},//传递给服务器的参数					
						success:function(data){//与服务器交互成功调用的回调函数
							//后台返回则关掉提示							
							top.$.jBox.closeTip();
							alert(data.result);
						}
					});	
					//top.$.jBox.closeTip();
					form.reset();
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
		<li><a href="${ctx}/sysmgr/user/gotoUserInfo">个人信息</a></li>
		<li class="active"><a href="javascript:void(0)">修改密码</a></li>
	</ul><br/>
	<form id="userChangePwdForm" class="form-horizontal" action="#" method="post">
  
		<div class="control-group">
			<label class="control-label">旧密码:</label>
			<div class="controls">
				<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新密码:</label>
			<div class="controls">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">确认新密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="required" equalTo="#newPassword"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
		</div>
	</form>
</body>
</html>