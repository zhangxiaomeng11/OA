<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>区域管理</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<script type="text/javascript">
	$(function() {
		$("#areaTreeTable").treeTable({expandLevel : 3}).show();
	});
	
    var areaMgr = {    		
   		delArea:function(areaId){	
  	 		if(confirm("您确定要删除此区域吗?")){  	 			 
  		 		  $.ajax({
  						type:'post',//请求方式
  						url:'${ctx}/sysmgr/area/delArea', 
  						dataType:'json', //有几种格式 xml html json text 等常用
  						//data传值的另外一种方式 form的序列化
  						data: {"areaId":areaId},//传递给服务器的参数				
  						success:function(data){//与服务器交互成功调用的回调函数
  							//data就是out.print输出的内容
  							alert(data.result);
  						  	document.getElementById("areaListForm").submit();			
  						}
  					});
   			}  			
   	 	}	
    		
    };
    
    	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0);">区域列表</a></li>
		<li><a href="${ctx}/sysmgr/area/gotoAreaEdit?editFlag=1">区域添加</a></li>
	</ul>

	<form id="areaListForm" method="post" action="${ctx}/sysmgr/area/gotoAreaList">
		<table id="areaTreeTable" class="table table-striped table-bordered table-condensed hide">
			<thead>
				<tr>
					<th>名称</th>
					<th>编号</th>
					<th style="text-align:center;">排序</th>				 
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				 
			<c:forEach items="${areaList}" var="area">
				<tr id="${area.id}" pId="${area.parentId}">										 
					<td><a href="${ctx}/sysmgr/area/gotoAreaEdit?editFlag=2&areaId=${area.id}">${area.name}</a></td>
					<td>${area.code}</td>
					<td style="text-align:center;">${area.sort}</td>
					 
					<td nowrap>
						<a href="${ctx}/sysmgr/area/gotoAreaEdit?editFlag=2&areaId=${area.id}">修改</a>
						<a href="javascript:areaMgr.delArea(${area.id})">删除</a>
						<a href="${ctx}/sysmgr/area/gotoAreaEdit?editFlag=1&parentId=${area.id}">添加下级区域</a>
					</td>		 
				</tr>
				</c:forEach>
				 
			</tbody>
		</table>
		 
	 </form>
</body>
</html>