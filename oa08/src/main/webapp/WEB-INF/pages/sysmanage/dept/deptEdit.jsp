<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>部门编辑</title>
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
				idKey:"id",
				pIdKey:"parentId",
				rootPId:0
			}
		},
		callback: {
			beforeClick: beforeClick,
			onClick: onClick/* ,
			onDblClick: hideMenu */
		}
	};
  

	function beforeClick(treeId, treeNode) {
		/* alert(treeNode.children);
		var check = (treeNode && !treeNode.isParent);
		if (!check) alert("只能选择城市...");
		return check; */
	};

	function onClick(e, treeId, treeNode) {
		//树上元素的点击事件我们需要做几件事件，需要将点击的元素赋值给我们指定的元素  然后点击完隐藏树结构
		var zTree = $.fn.zTree.getZTreeObj("deptTree");
		var node = zTree.getSelectedNodes();
		var nodeName = node[0].name;
		var nodeId = node[0].id;
		//alert(nodeId);
		$("#parentId").attr("value", nodeId);
		$("#parentName").attr("value", nodeName);
		hideMenu();
		 
	}

	function onDblClick(e, treeId, treeNode) {
		 	
	}

	function showMenu() {
		var parentNameObj = $("#parentName");
		var parentNameOffset = $("#parentName").offset();
		$("#deptContent").css({left:parentNameOffset.left + "px", top:parentNameOffset.top + parentNameObj.outerHeight() + "px"}).slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	}
	function hideMenu() {
		$("#deptContent").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "deptBtn" || event.target.id == "deptContent" || $(event.target).parents("#deptContent").length>0)) {
			hideMenu();
		}
	}

	$(document).ready(function(){	
		 //此地方要注意，考虑不将本身的下级节点显示出来，怕选择错误，造成连环套
		 var deptId  = $("#id").val(); 
	     $.get("${ctx}/sysmgr/dept/getParentDeptTreeData?deptId="+deptId, function (zNodes) {           	
	        // 初始化树结构
			//var jsonObj = $.parseJSON(zNodes.jsonObj);	
			var tree = $.fn.zTree.init($("#deptTree"), setting, zNodes);       
	         // 默认展开一级节点
	        var nodes = tree.getNodesByParam("level", 2);
	        for (var i = 0; i < nodes.length; i++) {
	            tree.expandNode(nodes[i], true, false, false);
	        }
	        
	        //如果是修改页面，定位到当前选中的节点
	        //alert($("#parentId").val());
	        var selectNodeId =$("#parentId").val();
	        if(selectNodeId!=null){
	        	tree.selectNode(tree.getNodeByParam("id",selectNodeId, null)); 
	        }	

	    });
	   	
	});


	$(function() {
		$("#name").focus();
		$("#deptEditForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
		 		var editFlag = ${editFlag};
		 		//获取字典修改后的信息数据,组装成json字符串
				var formObject = {};
				var formArray =$("#deptEditForm").serializeArray();
				$.each(formArray, function(i, item){
					formObject[item.name]=item.value;
				 });
			 	var jsonObj = JSON.stringify(formObject);
				
				alert(jsonObj);			 
				loading('正在提交，请稍等...');
					$.ajax({
					type:'post',//请求方式
					url:'${ctx}/sysmgr/dept/saveDept', 
					contentType :"application/json;charset=UTF-8",
					dataType:'json', //返回数据的几种格式 xml html json text 等常用
					//data传值的另外一种方式 form的序列化
					data: jsonObj,//传递给服务器的参数					
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
	});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sysmgr/dept/gotoDeptList.action">部门列表</a></li>
		<li class="active">
			<a href="javascript:void(0);">部门
				<c:choose>
					   <c:when test="${editFlag==1}">添加
					   </c:when>
					   <c:otherwise>修改</c:otherwise>
				</c:choose>	
			</a></li>
	</ul><br/>
	<form id="deptEditForm" class="form-horizontal" action="#" method="post">
		<input id="id" name="id" type="hidden" value="${dept.id}"/>

		<div class="control-group">
			<label class="control-label">上级部门:</label>
			<div class="controls">

				<div class="input-append">
					<input id="parentId" name="parentId" class="required" type="hidden" value="${dept.parentId}"/>
					<input id="parentName" name="parenName" readonly="readonly" type="text" value="${dept.parentName}"   style=""/>
						<a id="deptButton" href="javascript:showMenu();" class="btn" style="">
						&nbsp;<i class="icon-search" ></i>&nbsp;</a>&nbsp;&nbsp;
				</div>
			</div>
		</div>
		<div id="deptContent"  style="display:none; position: absolute; background: #f0f6e4;">
			<ul id="deptTree" class="ztree" style="margin-top:0; width:260px;"></ul>
		</div>
		 
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<input id="name" name="name" class="required input-xlarge" type="text" value="${dept.name}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<input id="sort" name="sort" class="required digits input-small" type="text" value="${dept.sort}" maxlength="50"/>
				<span class="help-inline"><span class="help-inline"><span style="color:red">*</span> </span>排列顺序，升序。</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">部门编号:</label>
			<div class="controls">
				<input id="code" name="code" class="input-small" type="text" value="${dept.code}" maxlength="50"/>
 			</div>
		</div>
		<div class="control-group">
			<label class="control-label">地址:</label>
			<div class="controls">
				<input id="address" name="address" class="input-small" type="text" value="${dept.master}" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人:</label>
			<div class="controls">
				<input id="master" name="master" class="input-small" type="text" value="${dept.master}" maxlength="50"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<input id="phone" name="phone" class="input-small" type="text" value="${dept.phone}" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">传真:</label>
			<div class="controls">
				<input id="fax" name="fax" class="input-small" type="text" value="${dept.fax}" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<input id="email" name="email" class="email" type="text" value="${dept.email}" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea id="remarks" name="remarks" maxlength="200" class="input-xxlarge" rows="3">${dept.remarks}</textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>