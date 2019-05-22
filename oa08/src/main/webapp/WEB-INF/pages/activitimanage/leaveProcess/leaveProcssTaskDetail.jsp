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
 
var leaveProcessTaskDetailMgr ={		 
		 
		completeTask:function(obj){
			alert(obj.value);
			alert($("#taskId").val());
			var taskId = $("#taskId").val();
			var outcome = obj.value;
			var commentMsg = $("#commentMsg").val();
			  
			 $.ajax({
					type:'post',//请求方式
					url:'${ctx}/activitimgr/processTask/completeTask', 
					dataType:'json', //返回数据的几种格式 xml html json text 等常用					
					//data传值的另外一种方式 form的序列化
					data: {"taskId":taskId,"outcome":outcome,"commentMsg":commentMsg},//传递给服务器的参数					
					success:function(data){//与服务器交互成功调用的回调函数
						//后台返回则关掉提示
						top.$.jBox.closeTip();
						alert(data.result);
						history.go(-1);
	 				}
				}); 
			 
		 }
};
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/activitimgr/processTask/gotoProcessTaskList">待处理任务列表</a></li>
		<li class="active"><a href="javascript:void(0);">任务处理</a></li>
	</ul><br/>
	<form  class="form-horizontal" id = "leaveDetail">
		<input id="leaveId" name="leaveId" type="hidden" value="${leaveBean.leaveId}"/>
		<input id="taskId" name="taskId" type="hidden" value="${taskId}"/>
		 
		<div class="control-group">
			<label class="control-label">请假人:</label>
			<div class="controls">
				<input id="leaveUserName" name="leaveUserName"   type="text" value="${leaveBean.leaveUserName}" readonly/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假日期:</label>
			<div class="controls">
				<input id="leaveDate" name="leaveDate"   type="text" 
				value='<fmt:formatDate value="${leaveBean.leaveDate}" type="date" pattern="yyyy-MM-dd"/>'				
 				readonly/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假天数:</label>
			<div class="controls">
				<input id="leaveDays" name="leaveDays" type="text" value="${leaveBean.leaveDays}"
				maxlength="5" readonly/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假原因:</label>
			<div class="controls">
				<textarea id="leaveReason" name="leaveReason" maxlength="200"  rows="3" readonly>${leaveBean.leaveReason}</textarea>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">批注:</label>
			<div class="controls">
				<textarea id="commentMsg" name="commentMsg" maxlength="200"  rows="3" ></textarea>
			</div>
		</div>  
		
		<div class="form-actions">				 
			<c:forEach items="${buttonNameList}" var="buttonName">
				<input id="btnSubmit" class="btn btn-primary" type="button" 
				value="${buttonName}" onclick ="leaveProcessTaskDetailMgr.completeTask(this)" />&nbsp;
			</c:forEach>			
 		</div>
	</form>
	
	<br>
	<form id = "commentList">
		<table id="processDeployListTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>批注ID</th>
					<th>批注人</th>
					<th>批注时间</th>				 
					<th>批注</th>
 				</tr>
			</thead>
			<tbody>
				 
			<c:forEach items="${commentList}" var="comment">
				<tr>										 
					<td>${comment.id}</td>
					<td>${comment.userId}</td>
					<td><fmt:formatDate value="${comment.time}" type="date" pattern="yyyy-MM-dd hh:mm:ss"/></td>					
					<td>${comment.message}</td>
  				</tr>
			</c:forEach>
				 
			</tbody>
		</table>		 
	 </form>
	
</body>
</html>