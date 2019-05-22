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

var setting = {
		view: {
			dblClickExpand: false
		},
		data: {
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "parentId",
				rootPId: 0
			}
		},
		callback: {
 			onClick: onClick 
		}
	};
 
	function onClick(e, treeId, treeNode) {
		//树上元素的点击事件我们需要做几件事件，需要将点击的元素赋值给我们指定的元素  然后点击完隐藏树结构
		var zTree = $.fn.zTree.getZTreeObj("userDeptTree");
		var node = zTree.getSelectedNodes();
		var nodeName = node[0].name;
		var nodeId = node[0].id;
		//alert(nodeId);
		$("#deptId").attr("value", nodeId);
		$("#deptName").attr("value", nodeName);
		hideMenu();
		 
	}
 
	function showUserDept() {
 		var deptNameObj = $("#deptName");
		var deptNameOffset = $("#deptName").offset();
		$("#userDeptContent").css({left:deptNameOffset.left + "px", top:deptNameOffset.top + deptNameObj.outerHeight() + "px"}).slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	}
	function hideMenu() {
		$("#userDeptContent").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "userDeptBtn" || event.target.id == "userDeptContent" || $(event.target).parents("#userDeptContent").length>0)) {
			hideMenu();
		}
	}
	
  

$(document).ready(function() {
	$("#userName").focus();
	$("#userEditForm").validate({
		submitHandler: function(form){
			loading('正在提交，请稍等...');
	 		var editFlag = ${editFlag};
	 		//获取字典修改后的信息数据,组装成json字符串
			var formObject = {};
			var formArray =$("#userEditForm").serializeArray();
			$.each(formArray, function(i, item){
				formObject[item.name]=item.value;
			 });

			var userVo = {};
			userVo["user"]=formObject;
		 	//获取复选框的值
		 	var roleCheckObj = {};
		 	var roleIdArray = $("input[name='roleIdList']:checked").serializeArray();
 			$.each(roleIdArray, function(i, item){
 				roleCheckObj[item.value]=item.value;
			 });
			userVo["roleIds"]=roleCheckObj;
		 	
 			loading('正在提交，请稍等...');
				$.ajax({
				type:'post',//请求方式
				url:'${ctx}/sysmgr/user/saveUser', 
				dataType:'json', //返回数据的几种格式 xml html json text 等常用
				contentType :"application/json;charset=UTF-8",
				//data传值的另外一种方式 form的序列化
				data: JSON.stringify(userVo),//传递给服务器的参数					
				success:function(data){//与服务器交互成功调用的回调函数
					//后台返回则关掉提示
					top.$.jBox.closeTip();
					alert(data.result);
					history.go(-1);
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
	
	$.get("${ctx}/sysmgr/dept/getAllDeptList", function (zNodes) {           	
        // 初始化树结构
		var tree = $.fn.zTree.init($("#userDeptTree"), setting, zNodes);
		var selectNodeId =$("#deptId").val();
        if(selectNodeId!=null){
        	tree.selectNode(tree.getNodeByParam("id",selectNodeId, null)); 
        }	
		tree.expandAll(true);        

    });
});
</script>
</head>
<body>
<ul class="nav nav-tabs">
		<li><a href="${ctx}/sysmgr/user/gotoUserList">用户列表</a></li>
		<li class="active"><a href="javascript:void(0);">用户
			<c:choose>
			   <c:when test="${editFlag==1}">添加
			   </c:when>
			   <c:otherwise>修改</c:otherwise>
			 </c:choose>
		 </a></li>
	</ul><br/>
	<form id="userEditForm" class="form-horizontal" action="#" method="post">
		<input id="userId" name="userId" type="hidden" value="${userDto.userId}"/>
		<div class="control-group">
			<label class="control-label">部门名称:</label>
			<div class="controls" >
				<div class="input-append">
					<input id="deptId" name="deptId" class="required" type="hidden" value="${userDto.deptId}"/>
					<input id="deptName" name="deptName" readonly="readonly" class="required" type="text" value="${userDto.deptName}" />
						<a id="deptButton" href="javascript:showUserDept();" class="btn" >
						&nbsp;<i class="icon-search" ></i>&nbsp;</a>&nbsp;&nbsp;
				</div>
			</div>	
			 <div id="userDeptContent"  style="display:none; position: absolute; background: #f0f6e4;">
				<ul id="userDeptTree" class="ztree" style="margin-top:0; width:260px;"></ul>
			 </div>
		</div>
		<div class="control-group">
			<label class="control-label">工号:</label>
			<div class="controls">
				<input id="userNo" name="userNo" class="required" type="text" value="${userDto.userNo}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<input id="userName" name="userName" class="required" type="text" value="${userDto.userName}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">登录名:</label>
			<div class="controls">
				<input id="loginName" name="loginName" class="required" type="text" value="${userDto.loginName}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>		
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<input id="email" name="email" class="email" type="text" value="${userDto.email}" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<input id="phone" name="phone" type="text" value="${userDto.phone}" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机:</label>
			<div class="controls">
				<input id="mobile" name="mobile" type="text" value="${userDto.mobile}" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户角色:</label>
			<div class="controls">	
				<c:forEach items="${roleList}" var="role">				 			
					<span>						
						<input id="roleId${role.id}" name="roleIdList" class="required" type="checkbox" 
						<c:if test="${roleCheckMap[role.id] == role.id}">checked</c:if>  value="${role.id}"/>
						<label for="roleId${role.id}">${role.name}</label>
					</span>
					 
				</c:forEach>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea id="remarks" name="remarks" maxlength="200" class="input-xlarge" rows="3">${userDto.remarks}</textarea>
			</div>
		</div>	
		 
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html> 