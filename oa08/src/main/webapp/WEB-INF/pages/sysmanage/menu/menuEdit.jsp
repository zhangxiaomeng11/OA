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
					idKey:"id",
					pIdKey:"parentId",
					rootPId:0
				}
			},
			callback: {
				beforeClick: beforeClick,
				onClick: onClick
			}
		};

	 
		function beforeClick(treeId, treeNode) {
			/* var check = (treeNode && !treeNode.isParent);
			if (!check) alert("只能选择城市...");
			return check; */
		};
		
		//点击树节点 时将选中节点返回给页面
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("menuTree");
			var nodes = zTree.getSelectedNodes();
			var nodeName = nodes[0].name;
			var nodeId = nodes[0].id;
 			$("#parentId").attr("value", nodeId);
			$("#parentName").attr("value", nodeName);
			//为使得用户体验更加，选中节点后，影藏数结构
			hideMenu() ;
		};
		//点击搜索框，显示树
		function showMenu() {
			var parentObj = $("#parentName");
			var parentOffset = parentObj.offset();
			$("#menuContent").css({left:parentOffset.left + "px", top:parentOffset.top + parentObj.outerHeight() + "px"}).slideDown("fast");
			$("body").bind("mousedown", onBodyDown);
		};
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		};
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		};

		$(document).ready(function(){
			//此地方要稍微注意下，考虑不将本身以及下级节点显示出来，怕选择错误，造成连环套
			var menuId = $("#id").val();
			$.get("${ctx}/sysmgr/menu/getParentMenuTreeData.action?menuId="+menuId,
					function(zNodes){
						//初始化数结构
						//var jsonObj= $.parseJSON(zNodes.jsonObj);
						var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
						//默认展开节点
						var nodes = tree.getNodesByParam("level",2);
						for(var i=0;i<nodes.length;i++){
							tree.expandNode(nodes[i],true,false,false);
						}
						//如果是进入修改页面，定位到当前选中的节点
						var selectNodeId = $("#parentId").val();
						if(selectNodeId!=null){
							tree.selectNode(tree.getNodeByParam("id",selectNodeId,null));
						}
				
			});
		});
	
	
		$(function() {
			$("#name").focus();
			$("#menuEditForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
			 		var editFlag = ${editFlag};
			 		//获取字典修改后的信息数据,组装成json字符串
					var formObject = {};
					var formArray =$("#menuEditForm").serializeArray();
					$.each(formArray, function(i, item){
						formObject[item.name]=item.value;
					 });
				 	var jsonObj = JSON.stringify(formObject);
					
					alert(jsonObj);			 
					loading('正在提交，请稍等...');
						$.ajax({
						type:'post',//请求方式
						url:'${ctx}/sysmgr/menu/saveMenu', 
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
		<li><a href="${ctx}/sysmgr/menu/gotoMenuList">菜单列表</a></li>
		<li class="active"><a href="javascript:void(0);">菜单
			<c:choose>
			   <c:when test="${editFlag==1}">添加
			   </c:when>
			   <c:otherwise>修改</c:otherwise>
			</c:choose>	
		</a></li>
	</ul><br/>
	<form id="menuEditForm" class="form-horizontal" action="#" method="post">
		<input id="id" name="id" type="hidden" value="${menu.id}"/>
 		<div class="control-group">
			<label class="control-label">上级菜单:</label>
			<div class="controls">

				<div class="input-append">
					<input id="parentId" name="parentId" class="required" type="hidden" value="${menu.parentId}"/>
					<input id="parentName" name="parenName" readonly="readonly" type="text" value="${menu.parentName}"  style=""/>
						<a id="menuButton" href="javascript:showMenu();" class="btn" style="">
						&nbsp;<i class="icon-search" ></i>&nbsp;</a>&nbsp;&nbsp;
				</div>
			</div>
		</div>
		<div id="menuContent"  style="display:none; position: absolute; background: #f0f6e4;">
			<ul id="menuTree" class="ztree" style="margin-top:0; width:260px;"></ul>
		</div>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<input id="name" name="name" class="required input-xlarge" type="text" value="${menu.name}" maxlength="50"/>
				<span class="help-inline"><span style="color:red">*</span> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接:</label>
			<div class="controls">
				<input id="href" name="href" class="input-xxlarge" type="text" value="${menu.href}" maxlength="2000"/>
				<span class="help-inline">点击菜单跳转的页面</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">目标:</label>
			<div class="controls">
				<input id="target" name="target" class="input-small" type="text" value="${menu.target}" maxlength="10"/>
				<span class="help-inline">链接地址打开的目标窗口，默认：mainFrame</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图标:</label>
			<div class="controls">
				<!-- <i id="iconIcon" class="icon- hide"></i>&nbsp;<
				label id="iconIconLabel">无</label>&nbsp;
				<input id="icon" name="icon" type="hidden" value=""/>
				<a id="iconButton" href="javascript:" class="btn">选择</a>&nbsp;&nbsp; -->
				<sys:iconselect id="icon" name="icon" value="${menu.icon}" />
				
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<input id="sort" name="sort" class="required digits input-small" type="text" value="${menu.sort}" maxlength="50"/>
				<span class="help-inline"><span class="help-inline"><span style="color:red">*</span> </span>排列顺序，升序。</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">可见:</label>
			<div class="controls">
				<span>
					<input id="isShow1" name="isShow" class="required" type="radio" value="1" <c:if test="${menu.isShow==1}">checked</c:if> />
					<label for="isShow1">显示</label>
				</span>
				<span>
					<input id="isShow2" name="isShow" class="required" type="radio" value="0" <c:if test="${menu.isShow==0}">checked</c:if>/>
					<label for="isShow2">隐藏</label>
				</span>
				<span class="help-inline">该菜单或操作是否显示到系统菜单中</span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">权限标识:</label>
			<div class="controls">
				<input id="permission" name="permission" class="input-xxlarge" type="text" value="${menu.permission}" maxlength="100"/>
				<span class="help-inline">控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</span>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<textarea id="remarks" name="remarks" maxlength="200" class="input-xxlarge" rows="3">${menu.remarks}</textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>