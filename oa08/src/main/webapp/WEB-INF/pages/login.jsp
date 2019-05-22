<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file= "/WEB-INF/pages/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file = "/WEB-INF/pages/include/head.jsp" %>
<title>逗猛OA系统</title>
<style type="text/css">
html,body{height:100%;}
/*canvas开始*/
#canvas {position: absolute;z-index: -1;display: block;}
/*canvas结束*/
#login {
	width: 400px;
	height: 320px;
	background: #fff;
	position: absolute; /*绝对定位*/
	margin:auto;
	top:0px;
	left:0px;
	right:0px;
	bottom:0px;
	box-shadow: 0px 0px 10px #666;
	border-radius: 4px; /*圆角*/
}
.well{background-color: #062A36;}
h2{font-family:"微软雅黑";font-size:24px;color:#B94846;margin-bottom:28px;}
#loginForm{color:#fff}
</style>

</head>
<body style = "border-radius: 10px;background-size:100% 100% ">

	<div class="row" id="login" >

		<div class="well col-md-12 center">
			<div class="title" style="padding: 5px;text-align:center;">
				<img src="jsAndCss/images/logo5.png">
				<h2>逗猛OA系统</h2>
 			</div>
 		 
			<div id="messageBoxLogin" class="alert alert-error ${empty loginFlag ? 'hide' :''} ">
				<button data-dismiss="alert" class="close">×</button>
				<label id="loginFlag" class="error">${loginFlag}</label>
			</div>
			
			<div id="messageBox" class="alert alert-error ${empty loginErrorMsg ? 'hide' :''} ">
				<button data-dismiss="alert" class="close">×</button>
				<label id="loginError" class="error">${loginErrorMsg}</label>
			</div>
			
			
			<form id="loginForm"  action="${ctx}/login" method="post">
				<label class="input-label" for="loginName">登录名</label>
				<input type="text" id="loginName" name="loginName" class="input-block-level required" value="">
				<label class="input-label" for="password">密码</label>
				<input type="password" id="password" name="password" class="input-block-level required">
				<input class="btn  btn-primary" type="submit" value="登 录" />&nbsp;&nbsp;
			</form>
		</div>

	</div>
	<canvas id="canvas" style="background: rgba(31, 0, 62,0.8);"></canvas>

    <script type="text/javascript" src="jsAndCss/canvas/ball.js"></script>
    <script type="text/javascript" src="jsAndCss/canvas/utils.js"></script>
	<script type="text/javascript">
	 $(document).ready(function() {
			$("#loginForm").validate({
				messages: {
					loginName: {required: "请填写用户名."},password: {required: "请填写密码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
		});
		// 如果在框架或在对话框中，则弹出提示并跳转到首页
		if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
			alert('未登录或登录超时。请重新登录，谢谢！');
			top.location = "";
		}
	 
	 
        //1.获取对象
        //2.获取绘画环境
        //3.开始绘画
        //4.定义api方法
        //5.实现绘画
        window.addEventListener("load",function(){
            var canvas = document.getElementById("canvas");
            var ctx = canvas.getContext("2d");
            var mouse = utils.captureMouse(canvas);

            var balls= [];
            var numBalls =30,
                minDist=200;
                // springAmount = 0.0005;
            canvas.width =window.innerWidth;
            canvas.height = window.innerHeight;

            //遍历实例化球，并初始化
            for(var size,ball,i=0;i<numBalls;i++){
                size = Math.random()*10 +2;
                ball = new Ball(size,"#fff");
                ball.x = Math.random()*canvas.width;
                ball.y = Math.random()*canvas.height;
                //随机向量
                ball.vx = Math.random()*6 -3;
                ball.vy = Math.random()*6 -3;
                ball.mass =size;
                balls.push(ball);
            };

            function spring(partA,partB){
                var dx =partB.x -partA.x,
                    dy =partB.y - partA.y,
                    dist = Math.sqrt(dx*dx+dy*dy);
                if(dist < minDist){
                    var alpha = 1- dist/minDist;
                    ctx.strokeStyle=utils.colorToRGB("#ffffff",alpha);
                    ctx.beginPath();
                    ctx.moveTo(partA.x,partA.y);
                    ctx.lineTo(partB.x,partB.y);
                    ctx.stroke();
                }
            }

            //移动的方法
            function move(partA){
                partA.x +=partA.vx;
                partA.y +=partA.vy;
                if(partA.x >canvas.width){
                    partA.x = 0;
                }else if(partA.x <0){
                    partA.x = canvas.width;

                }
                if(partA.y> canvas.height){
                    partA.y =0;
                }else if(partA.y <0){
                    partA.y =canvas.height;
                }
                for(var partB,i=0,j=i;j<numBalls;j++){
                    partB = balls[j];
                    spring(partA,partB);
                }

            }

            //绘画方法
            function draw(ball){
                ball.draw(ctx);
            };
            //实现绘画
            (function drawFrame(){
                window.requestAnimationFrame(drawFrame,canvas);
                ctx.clearRect(0,0,canvas.width,canvas.height);
                ctx.beginPath();
                balls.forEach(move);
                balls.forEach(draw);
            })();

        },false);

    </script>
</body>
</html>