<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>字典编辑</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#dictEditForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
			 		//var editFlag = ${editFlag};
			 		//通过有没有dictId来判断是修改还是新增,新增的时候是没有dictId的值
			 		//获取字典修改后的信息数据,组装成json字符串
					var formObject = {};
					var formArray =$("#dictEditForm").serializeArray();
					$.each(formArray, function(i, item){
						formObject[item.name]=item.value;
					 });
				 	var jsonObj = JSON.stringify(formObject);
					
					alert(jsonObj);		 
					loading('正在提交，请稍等...');
						$.ajax({
						type:'post',//请求方式
						url:'${ctx}/sysmgr/dict/saveDict', 
						dataType:'json', //返回数据的几种格式 xml html json text 等常用
						contentType :"application/json;charset=UTF-8",
						//data传值的另外一种方式 form的序列化
						data: jsonObj,//传递给服务器的参数					
						success:function(data){//与服务器交互成功调用的回调函数
							//后台返回则关掉提示
							top.$.jBox.closeTip();
							alert(data.result);
						}
					});
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
		
var dictEdit ={		 
	saveDict :function(){
	 		
	 	}
}; 
		
		
</script>
</head>
<body>
<ul class="nav nav-tabs">
		<li><a href="${ctx}/sysmgr/dict/gotoDictList">字典列表</a></li>
		<li class="active"><a href="javascript:void(0);">字典
			<c:choose>
			   <c:when test="${editFlag==1}">添加
			   </c:when>
			   <c:otherwise>修改</c:otherwise>
			 </c:choose>		 
		 </a></li>
	</ul><br/>
	<form id="dictEditForm" class="form-horizontal" action="#" method="post">
		<input id="id" name="id" type="hidden" value="${not empty dict.id ? dict.id : ''}"/>
		<div class="control-group">
			<label class="control-label">键值:</label>
			<div class="controls">
				<input id="value" name="value" class="required" type="text" value="${not empty dict.value ? dict.value : ''}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标签:</label>
			<div class="controls">
				<input id="label" name="label" class="required" type="text" value="${not empty dict.label ? dict.label: ''}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类型:</label>
			<div class="controls">
				<input id="type" name="type" class="required abc" type="text" value="${not empty dict.type ? dict.type: ''}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述:</label>
			<div class="controls">
				<input id="description" name="description" class="required" type="text" value="${not empty dict.description ? dict.description: ''}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<input id="sort" name="sort" class="required digits" type="text" value="${not empty dict.sort ? dict.sort: ''}" maxlength="11"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea id="remarks" name="remarks" maxlength="200" class="input-xlarge" rows="3">${not empty dict.remarks ? dict.remarks: ''}</textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" "/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html> 