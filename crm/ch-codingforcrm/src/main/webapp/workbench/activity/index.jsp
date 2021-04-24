<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head><base href="<%=basePath%>">
<meta charset="UTF-8">


	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){

		$("#addBtn").click(function () {

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			//需要操作模态窗口的jquery对象，调用modal方法，为这个方法传递参数：show:打开模态窗口    hide:关闭模态窗口
			// $("#createActivityModal").modal("show");

			$.ajax({
				url: "workbench/activity/getUserList.do",
				type: "post",
				dataType: "json",
				success :function(data){
					//从后台查询成功，获取到user的每一条信息，将这个信息放入的<option>中
					var html="<option></option>";
					//对获取到的信息进行遍历
					$.each(data,function(i,n){
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})

					//遍历万完之后，添加进去
					$("#create-owner").html(html);
					// 处理完下拉列表之后，在打开模态窗口

					//是当前登录的用户默认选中(一定要放在数据都加进去之后在写默认的)
					<%--var id=${sessionScope.user.id};--%>
					var id="${sessionScope.user.id}";
					$("#create-owner").val(id);
					$("#createActivityModal").modal("show");

					}
			})
		})

		//为保存按钮绑定单机事件，执行添加操作
		$("#saveBtn").click(function () {
			//使用ajax发送请求
			$.ajax({
				url: "workbench/activity/saveActivity.do",
				data: {
					"owner": $.trim($("#create-owner").val()),
					"name": $.trim($("#create-name").val()),
					"startDate": $.trim($("#create-startDate").val()),
					"endDate": $.trim($("#create-endDate").val()),
					"cost": $.trim($("#create-cost").val()),
					"description": $.trim($("#create-description").val())
					},
				type: "post",
				dataType: "json",
				success: function (data) {
					//需要的json
						// data:
						// {"success":true/false}
					if(data.success){

						//舒心列表活动信息
						// pageList(1,2);
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						//添加成功后，要将模态窗口中之前添加景来的数据删除掉
						$("#activityAddForm")[0].reset();



						//添加活动成功，列表页局部刷新，模态窗口关闭
						$("#createActivityModal").modal("hide");


						//添加活动失败，弹出添加市场活动失败的信息
					}else{
						alert("添加市场活动失败");
					}
				}
			})
		})
		/*
		* 页面加载完成之后出发一个方法，默认展开页面的第一页，每一页展现两条数据
		* */

		//点击左侧的市场活动时，调用一次刷新常常活动列表
		pageList(1,2);

		//点击查询按钮使，调用一次getPageList，刷新市场活动信息
		$("#searchBtn").click(function () {


			/*

				点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中


			 */

			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1,2);
		})

		//为全选的按钮绑定时间，触发全选的操作
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		})
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})


		//为删除按钮绑定单机事件，执行市场活动删除的操作
		$("#deleteBtn").click(function(){
			var $xz=$("input[name=xz]:checked");

			if($xz.length==0){
				alert("请选择需要删除的活动");

				//如果进入了else,就说明这个地方是一定有值得
			}else{
				//url:workbench/activity/deleteActivityDelete.do?id=xxx&id=xxx...

				if(confirm("确定删除所选中的记录吗？")){

					var param="";
					for(var i=0;i<$xz.length;i++){
						param+="id="+$($xz[i]).val();

						//如果不是最后一个元素，要加上&
						if(i<$xz.length-1){
							param+='&';
						}
					}
					$.ajax({
						url: "workbench/activity/deleteActivityList.do",
						data: param,
						type: "post",
						dataType: "json",
						success :function(data){
							if(data.success){
								//删除成功
								//刷新活动列表信息
								// pageList(1,2)
								pageList(1
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
								//删除失败
							}else{
								alert("删除市场活动失败");
							}
						}
					})
				}

				}
				// alert(param);

				//发送一个删除的ajax请求

		})

		//为修改按钮绑定单机事件，打开修改操作的模态窗口
		$("#editBtn").click(function(){
			var $xz=$("input[name=xz]:checked");

			if($xz.length==0){
				alert("请选择要修改的记录！")
			}else if($xz.length>1){
				alert("最多只能选择一条记录修改哦！")


				//进入到这里，就说明值选择了一条记录数进行修改
			}else{
				var id=$xz.val();
				$.ajax({
					url: "workbench/activity/editActivity.do",
					data: {
						"id": id
					},
					type: "get",
					dataType: "json",
					success: function(data){
						/*
						data:用户列表与市场活动信息列表
						 {"uList":[{用户1}，{2}，{3}]，“a":{市场活动}}
						 */
						var html="<option></option>";
						$.each(data.uList,function(i,n){
							html+="<option value='"+n.id+"'>"+n.name+"</option>";
						})
						$("#edit-owner").html(html);

						//为修改窗口赋值，值是从后台查询出来的数据
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);


						//这些值都写好之后，打开修改模态窗口
						$("#editActivityModal").modal("show");
					}
				})
			}
		})


		/*
		* 给修改按钮绑定单击事件
		* 在实际的项目开发中，一般情况写都是先做添加，在做修改
		* 着两者有许多的相似之处*/
		$("#updateBtn").click(function(){
			//使用ajax发送请求
			$.ajax({
				url: "workbench/activity/updateActivity.do",
				data: {
					"id": $.trim($("#edit-id").val()),
					"owner": $.trim($("#edit-owner").val()),
					"name": $.trim($("#edit-name").val()),
					"startDate": $.trim($("#edit-startDate").val()),
					"endDate": $.trim($("#edit-endDate").val()),
					"cost": $.trim($("#edit-cost").val()),
					"description": $.trim($("#edit-description").val())
				},
				type: "post",
				dataType: "json",
				success: function (data) {
					//需要的json
					// data:
					// {"success":true/false}
					if(data.success){

						// pageList(1,2);
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//添加活动成功，列表页局部刷新，模态窗口关闭
						$("#editActivityModal").modal("hide");


						//添加活动失败，弹出添加市场活动失败的信息
					}else{
						alert("修改市场活动失败");
					}
				}
			})

		})


	});

	/*
	* 对于所有的关系型数据库，做前端的分页的相关操作的基础组件
	* 就是pageNo和pageSize
	* pageNo:页码，一般默认使用的就是第一页
	* pageSize:便是的是每一页显示的数据的天数
	* */

	/**
	 *
	 * pageList方法：就是发出ajax请求到后台，从后台取出罪行的市场活动信息列表数据
	 * 				通过响应回来的数据，数据刷新市场活动信息列表
	 * 	在那些情况下，需要调用pageList方法(也就是在什么情况下需要刷新一下市场活动列表)
	 * 		1：点击左侧的“市场活动的连接的时候”
	 * 		2：添加，修改，删除操作之后
	 * 		3：点击查询按钮时
	 * 		4：点击分页组件啊时
	 */
	function pageList(pageNo,pageSize){
		//刷新活动列表时，将全选的复选框按钮取消
		$("#qx").prop("checked",false);

		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		//为进行分页操作，发送一个ajax请求
		$.ajax({
			//写一下通用的查询市场活动信息列表，使用动态sql    where   if   进行查询
			url: "workbench/activity/getActivityList.do",
			data: {
				"pageNo": pageNo,
				"pageSize": pageSize,
				"search-name": $.trim($("#search-name").val()),
				"search-owner": $.trim($("#search-owner").val()),
				"search-startDate": $.trim($("#search-startDate").val()),
				"search-endDate": $.trim($("#search-endDate").val())
			},
			type: "get",
			dataType: "json",
			success: function (data) {
				/*
				data：
					我们需要的市场活动信息列表[{市场活动1}，{市场活动2}...]
					分页插件需要的，查询出来总记录数:{"total":100}

					{"total":100,"dataList":[{市场活动1}，{市场活动2}.......]}
				 */
				var html="";
				//每一个n代表的就是每一个从后台数据库中查询出来的市场活动对象
				$.each(data.dataList,function (i,n) {

				html+='<tr class="active">';
				html+='<td><input type="checkbox" value="'+n.id+'" name="xz"/></td>';
				html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/activityDetail.do?id='+n.id+'\';">'+n.name+'</a></td>';
				html+='<td>'+n.owner+'</td>';
				html+='<td>'+n.startDate+'</td>';
				html+='<td>'+n.endDate+'</td>';
				html+='</tr>';

				})
				$("#activityBody").html(html);
				// 计算中页数
				// var totalPages=data.total%pageSize==0?data.total/pageSize:(data.total/pageSize)+1;
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				//数据处理完毕之后，使用分页插件
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})

	}
	
</script>
</head>
<body>


<%--搜索框中的信息--%>
<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startDate">
<input type="hidden" id="hidden-endDate">


	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					<input type="hidden" id="edit-id"/>
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">


								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label time" >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--
								关于文本域textarea:
									一定要以标签对的形式来呈现，正常的状态下标签对要紧紧的挨着
									textarea虽然是以标签对的形式来呈现的，但是它也是属于表单的元素范畴
									我们对于所有的textarea的取值和赋值操作，应该统一使用val（）方法
								-->
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">

<%--					data-toggle="modal":表示的是出发一个按钮，将要打开一个模态窗口--%>
<%--					data-target="#editActivityModal"：表示要打开哪一个模态窗口，通过id的形式来找到这个窗口--%>
<%--					现在我们是以属性和属性值的方式写在dom元素中，用来打开模态窗口--%>
<%--					但是如果这个样子做：问题在于我们没有办法对按钮的功能进行扩充
						所以在未来的实际项目之中，对于模态窗口的操作，一定不要写死在元素之中
						应该由我们自己写模态窗口来操作

--%>


				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<div id="activityPage"></div>
				</div>

			</div>
			
		</div>
		
	</div>
</body>
</html>