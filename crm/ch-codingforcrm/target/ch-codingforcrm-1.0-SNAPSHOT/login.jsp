<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script>
		$(function () {
			if(window.top!=window){
				window.top.location=window.location;
			}

			//每次刷新，清空输入框中的数据
			$("#loginUserName").val("");

			// 为输入框绑定自动获取焦点事件
			$("#loginUserName").focus();

			//为登录按钮绑定登录验证事件
			$("#loginBtn").click(function () {
				login();
			})

			//按回车键进行登录
			//键盘在这个窗口按下回车，就执行登录操作
			$(window).keydown(function (even) {
				// alert(even.keyCode);//13
				if(even.keyCode==13){
					login();
				}
			})

		})

		function login() {
					// 获取到用户输入文本框中的值，并且调用trim取出空格
			let loginUserName=$.trim($("#loginUserName").val());
			let loginPwd=$.trim($("#loginPwd").val());

			if(loginUserName==""|| loginPwd==""){
				$("#msg").html("用户名和密码不能为空！")
				//此时账号和密码为空的话，眼终止程序的运行
				//否则会继续执行下面的ajax请求
				return false;
			}
			$.ajax({
				url: "setting/user/login.do",
				data: {
					"loginUserName": loginUserName,
					"loginPwd": loginPwd
				},
				type: "post",
				dataType: "json",
				success: function(data) {

					if(data.success){
						//登录成功，跳转到登录成功页面
						window.location.href="workbench/index.jsp";
					}else{
						$("#msg").html(data.msg);
					}
				}
			})

		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/loginimg.jpg" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: pink; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">@2021Rice</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid pink;">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header ">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg" style="color: pink">
					<div style="width: 350px;">
						<input id="loginUserName" class="form-control" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="loginPwd" class="form-control" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<%--按钮写在表单中，默认的行为就是提交表单(type="submit")
						将type的类型改成button，那么时候提交表单就由我们自己控制--%>
					<button id="loginBtn" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>