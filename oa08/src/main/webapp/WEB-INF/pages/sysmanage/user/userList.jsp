<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户查询页面</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
	
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
</head>
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
	
	
	$(document).ready(function(){
	     $.get("${ctx}/sysmgr/dept/getAllDeptList", function (zNodes) {           	
	        // 初始化树结构
 			var tree = $.fn.zTree.init($("#userDeptTree"), setting, zNodes);
			tree.expandAll(true);
	    });
	   	
	});
 
var userMgr ={		 
	 getUserListPage :function(pageNo,pageSize){
	 		loading('正在提交，请稍等...');
	 		var deptId = $("#deptId").val();
			var userName = $("#userName").val();
	 			$.ajax({
				type:'post',//请求方式
				url:'${ctx}/sysmgr/user/getUserList', 
				dataType:'json', //返回数据的几种格式 xml html json text 等常用
				//data传值的另外一种方式 form的序列化
				data: {"deptId":deptId,"userName":userName,"pageNo":pageNo,"pageSize":pageSize},//传递给服务器的参数					
				success:function(data){//与服务器交互成功调用的回调函数
					//后台返回则关掉提示
					top.$.jBox.closeTip();
					//获取返回的json字符串
					//var jsonObj =  $.parseJSON(data.jsonObj);
					//获取数据
					var userList = data.userList;	
					if(userList!=null&&userList.length>0){				
							var htmlTable = "";
							for(var i=0;i<userList.length;i++){
								htmlTable = htmlTable+"<tr>";							
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+userList[i].userName;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+userList[i].loginName;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+userList[i].deptName;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+userList[i].phone;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+userList[i].mobile;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+userList[i].email;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
	 			    			var editButton ="<a href='${ctx}/sysmgr/user/gotoUserEdit?editFlag=2&&userId="+userList[i].userId+"'>修改</a>";
								var delButton = "<a href='javascript:userMgr.delUser("+userList[i].userId+")'>删除</a>";
								var addButton = "<a href='${ctx}/sysmgr/user/gotoUserEdit.action?editFlag=1'>添加</a>";
								htmlTable = htmlTable+editButton+"&nbsp;&nbsp;&nbsp;&nbsp;"+delButton+"&nbsp;&nbsp;&nbsp;&nbsp;"+addButton;
								htmlTable = htmlTable+"</td>";					
								htmlTable = htmlTable+"</tr>";
						}
						$('#userListTable').find('tbody').html(htmlTable);	
						//取出分页条代码
						var pageStr = data.pageStr;
						$('#userPageInfo').html(pageStr);							
	 				} else{
						$('#userListTable').find('tbody').html("<tr><td colspan='7' align='right'>没有查询到数据</td><tr>");
						$('#userPageInfo').html("");		
	 				}
					
				}
			});	
	 	} ,
	 	
	 	
	 	delUser:function(userId){		
	 		if(confirm("您确定要删除此用户吗?")){
		 		  $.ajax({
						type:'post',//请求方式
						url:'${ctx}/sysmgr/user/delUser', 
						dataType:'json', //有几种格式 xml html json text 等常用
						//data传值的另外一种方式 form的序列化
						data: {"userId":userId},//传递给服务器的参数				
						success:function(data){//与服务器交互成功调用的回调函数
							//data就是out.print输出的内容
							alert(data.result);
							//任何删除操作，要提示用户已经删除外，会重新查询一次证明给用户看
							userMgr.getUserListPage(1,15);					
						}
					});
				}
	 	}
	 	
	}; 

	


</script>


<body>

<ul class="nav nav-tabs">
    <li class="active"><a href="javascript:void(0);">用户列表</a></li>
    <li><a href="${ctx}/sysmgr/user/gotoUserEdit?editFlag=1">用户添加</a></li>
    
</ul>
<form id="dictSearchForm" class="breadcrumb form-search" action="#" method="post">
    <input id="pageNo" name="pageNo" type="hidden" value="1"/>
    <input id="pageSize" name="pageSize" type="hidden" value="15"/>
    <div class="controls">
			<label class="control-label">部门名称:</label>
			<div class="input-append">
				<input id="deptId" name="deptId" class="required" type="hidden" value=""/>
				<input id="deptName" name="deptName" readonly="readonly" type="text" value="" />
					<a id="deptButton" href="javascript:showUserDept();" class="btn" >
					&nbsp;<i class="icon-search" ></i>&nbsp;</a>&nbsp;&nbsp;
			</div>
			&nbsp;&nbsp;<label>用户名称</label>
        <input id="userName" name="userName" class="input-medium" type="text" value="" maxlength="50"/>&nbsp;
        <input id="btnSubmit" class="btn btn-primary" type="button" value="查询" onclick = "userMgr.getUserListPage(1,15);"/>
		</div>
        <div id="userDeptContent"  style="display:none; position: absolute; background: #f0f6e4;">
			<ul id="userDeptTree" class="ztree" style="margin-top:0; width:260px;"></ul>
		</div>
    
</form>
<table id="userListTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>用户名称</th>
        <th>登陆名称</th>
        <th>所属部门</th>
        <th>电话</th>
        <th>手机</th>
        <th>邮箱</th>
        <th>操作</th>
        </tr>
    </thead>
    <tbody>
     
    
    </tbody>
</table>
<div class="pagination" id = "userPageInfo">

</div>
</body>

</body>
 
</html>