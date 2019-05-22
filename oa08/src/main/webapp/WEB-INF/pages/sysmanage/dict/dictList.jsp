<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户编辑页面</title>
	<%@ include file="/WEB-INF/pages/include/head.jsp"%>
	
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
</head>
<script type="text/javascript">


var dictMgr ={		 
	 	 getDictListPage :function(pageNo,pageSize){
	 		loading('正在提交，请稍等...');
	 		var type = $("#type").val();
			var description = $("#description").val();
	 			$.ajax({
				type:'post',//请求方式
				url:'${ctx}/sysmgr/dict/getDictListPage', 
				dataType:'json', //返回数据的几种格式 xml html json text 等常用
				//data传值的另外一种方式 form的序列化
				data: {"type":type,"description":description,"pageNo":pageNo,"pageSize":pageSize},//传递给服务器的参数					
				success:function(data){//与服务器交互成功调用的回调函数
					//后台返回则关掉提示
					top.$.jBox.closeTip();
					//获取返回的json字符串
					//var jsonObj =  $.parseJSON(data.jsonObj);
					//获取数据
					//var dictList = jsonObj.dataJsonArray;	
					var dictList = data.dictList;
					
					if(dictList!=null&&dictList.length>0){				
							var htmlTable = "";
							for(var i=0;i<dictList.length;i++){
								htmlTable = htmlTable+"<tr>";								
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+dictList[i].value;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+dictList[i].label;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+dictList[i].type;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+dictList[i].description;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+dictList[i].sort;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
	 			    			var editButton ="<a href='${ctx}/sysmgr/dict/gotoDictEdit?editFlag=2&&dictId="+dictList[i].id+"'>修改</a>";
								var delButton = "<a href='javascript:dictMgr.delDict("+dictList[i].id+")'>删除</a>";
								var addButton = "<a href='${ctx}/sysmgr/dict/gotoDictEdit?editFlag=1'>添加</a>";
								htmlTable = htmlTable+editButton+"&nbsp;&nbsp;&nbsp;&nbsp;"+delButton+"&nbsp;&nbsp;&nbsp;&nbsp;"+addButton;
								htmlTable = htmlTable+"</td>";					
								htmlTable = htmlTable+"</tr>";
						}
						$('#dictListTable').find('tbody').html(htmlTable);	
						//取出分页条代码
						var pageStr = data.pageStr;
						$('#dictPageInfo').html(pageStr);							
	 				} else{
						$('#dictListTable').find('tbody').html("<tr><td colspan='6' align='right'>没有查询到数据</td><tr>");
						$('#dictPageInfo').html("");		
	 				}
					
				}
				
			});	
	 	} ,
	 	
	 	
	 	delDict:function(dictId){		
	 		if(confirm("您确定要删除此字典吗?")){
		 		  $.ajax({
						type:'post',//请求方式
						url:'${ctx}/sysmgr/dict/delDict', 
						dataType:'json', //有几种格式 xml html json text 等常用
						//data传值的另外一种方式 form的序列化
						data: {"dictId":dictId},//传递给服务器的参数				
						success:function(data){//与服务器交互成功调用的回调函数
							//data就是out.print输出的内容
							alert(data.result);
							//任何删除操作，要提示用户已经删除外，会重新查询一次证明给用户看
							dictMgr.getDictListPage(1,10);					
						}
					});
				}
	 	}
	 	
	};
</script>


<body>

<ul class="nav nav-tabs">
    <li class="active"><a href="javascript:void(0);">字典列表</a></li>
    <li><a href="${ctx}/sysmgr/dict/gotoDictEdit?editFlag=1">字典添加</a></li>
    
</ul>
<form id="dictSearchForm" class="breadcrumb form-search" action="#" method="post">
   
    <label>类型：</label>
        <select id="type" name="type" class="input-medium">
            <option value="">所有类型</option>
             <c:forEach items="${dictTypeList}" var="dictType">
            	 <option value="${dictType}">${dictType}</option>
           	 </c:forEach>
         </select>
    &nbsp;&nbsp;<label>描述 ：</label>
        <input id="description" name="description" class="input-medium" type="text" value="" maxlength="50"/>
    &nbsp;
   
    <input id="btnSubmit" class="btn btn-primary" type="button" value="查询" onclick = "dictMgr.getDictListPage(1,10);"/>
</form>
<table id="dictListTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>键值</th>
        <th>标签</th>
        <th>类型</th>
        <th>描述</th>
        <th>排序</th>
        <th>操作</th>
        </tr>
    </thead>
    <tbody>
     
    
    </tbody>
</table>
<div class="pagination" id = "dictPageInfo">

</div>
</body>

</body>
 
</html>