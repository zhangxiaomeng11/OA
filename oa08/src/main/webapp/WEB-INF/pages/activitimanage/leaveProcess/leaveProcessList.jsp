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
 
var leaveProcessMgr ={		 
	 getleaveProcessList:function(){
	 		loading('正在提交，请稍等...'); 		
	 			$.ajax({
				type:'post',//请求方式
				url:'${ctx}/activitimgr/leaveProcess/getLeaveProcessList', 
				dataType:'json', //返回数据的几种格式 xml html json text 等常用
				//data传值的另外一种方式 form的序列化
				data: {},//传递给服务器的参数					
				success:function(data){//与服务器交互成功调用的回调函数
					//后台返回则关掉提示
					top.$.jBox.closeTip();
					//获取返回的json字符串
					//var jsonObj =  $.parseJSON(data.jsonObj);
					//获取数据
					var leaveBeanList = data.leaveBeanList;	
					if(leaveBeanList!=null&&leaveBeanList.length>0){				
							var htmlTable = "";
							for(var i=0;i<leaveBeanList.length;i++){
								
								htmlTable = htmlTable+"<tr>";							
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+leaveBeanList[i].leaveId;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+leaveBeanList[i].leaveUserName;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+leaveBeanList[i].leaveDate;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+leaveBeanList[i].leaveDays;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+leaveBeanList[i].leaveReason;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								if(leaveBeanList[i].remark!=null)
									htmlTable = htmlTable+leaveBeanList[i].remark;
								htmlTable = htmlTable+"</td>";	
								htmlTable = htmlTable+"<td>";
								htmlTable = htmlTable+leaveBeanList[i].leaveStateDesc;
								htmlTable = htmlTable+"</td>";
								htmlTable = htmlTable+"<td>";
	 			    			var editButton ="<a href='${ctx}/activitimgr/leaveProcess/gotoLeaveProcessEdit?editFlag=2&&leaveId="+leaveBeanList[i].leaveId+"'>修改</a>";
								var delButton = "<a href='javascript:leaveProcessMgr.delLeaveProcess("+leaveBeanList[i].leaveId+")'>删除</a>";
								var leaveButton = "<a href='javascript:leaveProcessMgr.doLeaveProcess("+leaveBeanList[i].leaveId+")'>申请</a>";
								var processImageButton = "<a href='${ctx}/activitimgr/leaveProcess/gotoLeaveProcessImage?processInstanceId="+leaveBeanList[i].processInstanceId+"'>查看流程图</a>";
 
								//在这里需要做几个判断 ,当请假单还是初始录入的时候,可以修改和删除,以及申请 
								//如果已经是审核状态,其他按钮都需要屏蔽, 只能查看流程审核图 
								if(leaveBeanList[i].leaveState==0)
									htmlTable = htmlTable+editButton+"&nbsp;&nbsp;&nbsp;&nbsp;"+delButton+"&nbsp;&nbsp;&nbsp;&nbsp;"+leaveButton;
								else
									htmlTable = htmlTable+"&nbsp;&nbsp;&nbsp;&nbsp;"+processImageButton;

								htmlTable = htmlTable+"</td>";					
								htmlTable = htmlTable+"</tr>";
						}
						$('#leaveProcessListTable').find('tbody').html(htmlTable);					 						
	 				} else{
						$('#leaveProcessListTable').find('tbody').html("<tr><td colspan='7' align='right'>没有查询到数据</td><tr>");
 	 				}
					
				}
			});	
	 	} ,
	 	
	 	doLeaveProcess:function(leaveId){		
	 		 
	 		  $.ajax({
					type:'post',//请求方式
					url:'${ctx}/activitimgr/leaveProcess/doLeaveProcess', 
					dataType:'json', //有几种格式 xml html json text 等常用
					//data传值的另外一种方式 form的序列化
					data: {"leaveId":leaveId},//传递给服务器的参数				
					success:function(data){//与服务器交互成功调用的回调函数
						//data就是out.print输出的内容
						alert(data.result);
						//任何操作，会重新查询一次证明给用户看
						leaveProcessMgr.getleaveProcessList();					
					}
				});
				 
	 	},
	 	
	 	delLeaveProcess:function(leaveId){		
	 		if(confirm("您确定要删除此请假单吗?")){
		 		  $.ajax({
						type:'post',//请求方式
						url:'${ctx}/activitimgr/leaveProcess/delLeaveProcess', 
						dataType:'json', //有几种格式 xml html json text 等常用
						//data传值的另外一种方式 form的序列化
						data: {"leaveId":leaveId},//传递给服务器的参数				
						success:function(data){//与服务器交互成功调用的回调函数
							//data就是out.print输出的内容
							alert(data.result);
							//任何删除操作，要提示用户已经删除外，会重新查询一次证明给用户看
							leaveProcessMgr.getleaveProcessList();					
						}
					});
				}
	 	}
	 	
	}; 

	


</script>


<body>

<ul class="nav nav-tabs">
    <li class="active"><a href="javascript:void(0);">请假单列表</a></li>
    <li><a href="${ctx}/activitimgr/leaveProcess/gotoLeaveProcessEdit?editFlag=1">请假单录入</a></li>
    <li><a href="${ctx}/activitimgr/leaveProcess/gotoLeaveProcessImage">流程图</a></li>
    
</ul>
<form id="leaveProcessSearchForm" class="breadcrumb form-search" action="#" method="post">  
    <div class="controls">
		<label>用户名称</label>
        <input id="userName" name="userName" class="input-medium" type="text" value="" maxlength="50"/>&nbsp;
        <input id="btnSubmit" class="btn btn-primary" type="button" value="查询" onclick = "leaveProcessMgr.getleaveProcessList();"/>
		</div>   
</form>
<table id="leaveProcessListTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>ID</th>
        <th>请假人</th>
        <th>请假日期</th>
        <th>请假天数</th>
        <th>请假原因</th>
        <th>备注</th>
        <th>请假单状态</th>
        <th>操作</th>
        </tr>
    </thead>
    <tbody>
    </tbody>
</table>
 
</body>

</body>
 
</html>