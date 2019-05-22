<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户编辑页面</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
	
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<script type="text/javascript">
	
	var userInfoMgr = {
		init: function(){
			$.ajax({
				type:'post',//请求方式
				url:'${ctx}/sysmgr/user/getUserInfoById', 
				dataType:'json', //返回数据的几种格式 xml html json text 等常用
				//data传值的另外一种方式 form的序列化
				data: {},//传递给服务器的参数					
				success:function(data){//与服务器交互成功调用的回调函数
					//给页面元素赋值
					$("#deptName").html(data.deptName);  					
   					//表单元素赋值
   					$("#userId").val(data.userId); 
   					$("#loginName").val(data.loginName);
   					$("#userName").val(data.userName);
   					$("#userNo").val(data.userNo);
   					$("#email").val(data.email);
   					$("#phone").val(data.phone);
   					$("#mobile").val(data.mobile);
   					$("#remarks").val(data.remarks);
				}
			});	
		},
		
		saveSelfUserInfo : function(){
			loading('正在提交,请稍等...');

			//获取用户修改后的信息数据,组装成json字符串
			var formObject = {};
			var formArray =$("#userInfoForm").serializeArray();
			$.each(formArray, function(i, item){
				formObject[item.name]=item.value;
			 });
		  	var jsonObj = JSON.stringify(formObject);				
			//提交请求
			$.ajax({
					type:'post',//请求方式
					url:'${ctx}/sysmgr/user/saveSelfUserInfo', 
					contentType :"application/json;charset=UTF-8",
					dataType:'json', //返回数据的几种格式 xml html json text 等常用
					//data传值的另外一种方式 form的序列化
					data: jsonObj,//传递给服务器的参数					
					success:function(data){//与服务器交互成功调用的回调函数
						//后台返回则关掉提示							
						top.$.jBox.closeTip(); 
						alert(data.result);
					}
				});	
			}
				
	};
		
	</script>
</head>

<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">个人信息</a></li>
		<li><a href="${ctx}/sysmgr/user/gotoChangePwd">修改密码</a></li>
	</ul><br/>
	<form id="userInfoForm" class="form-horizontal" action="#" method="post">
 
		<input type = "hidden" id = "userId" name = "userId"/>
		
		<div class="control-group">
			<label class="control-label">所属部门:</label>
			<div class="controls">
				<label class="lbl"><div id = "deptName"></div></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">登陆名称:</label>
			<div class="controls">
				<input id="loginName" name="loginName" class="required" readonly="readonly" type="text"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户名称:</label>
			<div class="controls">
				<input id="userName" name="userName" class="required"  type="text"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">工号:</label>
			<div class="controls">
				<input id="userNo" name="userNo" class="required" type="text"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<input id="email" name="email" class="email" type="text"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<input id="phone" name="phone" type="text" value="" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机:</label>
			<div class="controls">
				<input id="mobile" name="mobile" type="text"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea id="remarks" name="remarks" maxlength="200" class="input-xlarge" rows="3"></textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户角色:</label>
			<div class="controls">
				<label class="lbl">公司管理员,系统管理员</label>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick = "userInfoMgr.saveSelfUserInfo();"/>
		</div>
	</form>
</body>
<script type="text/javascript">
	userInfoMgr.init();
</script>
</html>