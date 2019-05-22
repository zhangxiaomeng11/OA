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
			beforeClick: beforeClick,
			onClick: onClick/* ,
			onDblClick: hideArea */
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
		var zTree = $.fn.zTree.getZTreeObj("areaTree");
		var node = zTree.getSelectedNodes();
		var nodeName = node[0].name;
		var nodeId = node[0].id;
		//alert(nodeId);
		$("#parentId").attr("value", nodeId);
		$("#parentName").attr("value", nodeName);
		hideArea();
		 
	}

	function onDblClick(e, treeId, treeNode) {
		 	
	}

	function showArea() {
		var parentNameObj = $("#parentName");
		var parentNameOffset = $("#parentName").offset();
		$("#areaContent").css({left:parentNameOffset.left + "px", top:parentNameOffset.top + parentNameObj.outerHeight() + "px"}).slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	}
	function hideArea() {
		$("#areaContent").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "areaBtn" || event.target.id == "areaContent" || $(event.target).parents("#areaContent").length>0)) {
			hideArea();
		}
	}

	$(document).ready(function(){	
		 //此地方要注意，考虑不将本身的下级节点显示出来，怕选择错误，造成连环套
		 var areaId  = $("#id").val(); 
	     $.get("${ctx}/sysmgr/area/getParentAreaTreeData?areaId="+areaId, function (zNodes) {           	
	        // 初始化树结构
			//var jsonObj = $.parseJSON(zNodes.jsonObj);
			 
			var tree = $.fn.zTree.init($("#areaTree"), setting, zNodes);       
	         // 默认展开一级节点
	        var nodes = tree.getNodesByParam("level", 1);
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
		$("#areaEditForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
		 		var editFlag = ${editFlag};
		 		//获取字典修改后的信息数据,组装成json字符串
				var formObject = {};
				var formArray =$("#areaEditForm").serializeArray();
				$.each(formArray, function(i, item){
					formObject[item.name]=item.value;
				 });
			 	var jsonObj = JSON.stringify(formObject);
				
				alert(jsonObj);			 
				loading('正在提交，请稍等...');
					$.ajax({
					type:'post',//请求方式
					url:'${ctx}/sysmgr/area/saveArea', 
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
		<li><a href="${ctx}/sysmgr/area/gotoAreaList">区域列表</a></li>
		<li class="active">
			<a href="javascript:void(0);">区域
				<c:choose>
					   <c:when test="${editFlag==1}">添加
					   </c:when>
					   <c:otherwise>修改</c:otherwise>
				</c:choose>	
			</a></li>
	</ul><br/>
	<form id="areaEditForm" class="form-horizontal" action="#" method="post">
		<input id="id" name="id" type="hidden" value="${area.id}"/>

		<div class="control-group">
			<label class="control-label">上级区域:</label>
			<div class="controls">

				<div class="input-append">
					<input id="parentId" name="parentId" class="required" type="hidden" value="${area.parentId}"/>
					<input id="parentName" name="parentName" readonly="readonly" type="text" value="${area.parentName}" class="required" style=""/>
						<a id="areaButton" href="javascript:showArea();" class="btn" style="">
						&nbsp;<i class="icon-search" ></i>&nbsp;</a>&nbsp;&nbsp;
				</div>
			</div>
		</div>
		<div id="areaContent"  style="display:none; position: absolute; background: #f0f6e4;">
			<ul id="areaTree" class="ztree" style="margin-top:0; width:260px;"></ul>
		</div>
		 
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<input id="name" name="name" class="required input-xlarge" type="text" value="${area.name}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<input id="sort" name="sort" class="required digits input-small" type="text" value="${area.sort}" maxlength="50"/>
				<span class="help-inline"><span class="help-inline"><span style="color:red">*</span> </span>排列顺序，升序。</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">区域编号:</label>
			<div class="controls">
				<input id="code" name="code" class="input-small" type="text" value="${area.code}" maxlength="50"/>
 			</div>
		</div>
		 
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea id="remarks" name="remarks" maxlength="200" class="input-xxlarge" rows="3">${area.remarks}</textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>