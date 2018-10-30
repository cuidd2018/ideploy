<%@ page import="io.ideploy.deployment.admin.vo.account.AdminAccount" %>
<%@ page import="io.ideploy.deployment.common.enums.ModuleRepoType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/include/meta.html"%>
	<title>仓库访问</title>
</head>
<body class="skin-blue sidebar-mini">
<div class="wrapper">

	<!-- header -->
	<jsp:include page="/include/header.jsp"/>

	<!-- sidebar -->
	<jsp:include page="/include/sidebar.jsp" />

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>
				仓库访问配置
				<small></small>
			</h1>
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" data-toggle="modal" data-target="#configModal" onclick="addRepoAuth()"><i class="fa fa-dashboard"></i> 新增访问配置</a></li>
			</ol>
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-body">
							<div class="row">
								<div class="col-sm-12">
									<table id="listTable" class="table table-bordered table-hover">
										<thead>
										<tr>
											<th>ID</th>
											<th>名称</th>
                                            <th>所有者</th>
                                            <th>仓库</th>
											<th>描述</th>
											<th>操作</th>
										</tr>
										</thead>
										<script id="listTableTpl" type="text/html">
											{{each object as value i}}
											<tr>
												<td>
													{{value.authId}}
												</td>
												<td>{{value.authName}}</td>
                                                <td>
                                                    {{if value.ownType == 0}}
                                                    私有
                                                    {{/if}}
                                                    {{if value.ownType == 1}}
                                                    公共
                                                    {{/if}}
                                                </td>
                                                <td>
                                                    {{if value.repoType == <%=ModuleRepoType.GIT.getValue()%>}}
                                                    GIT
                                                    {{/if}}
                                                    {{if value.repoType == <%=ModuleRepoType.SVN.getValue()%>}}
                                                    SVN
                                                    {{/if}}
                                                </td>
												<td>{{value.authDesc}}</td>
												<td>
                                                    {{if value.ownType == 0}}
													<a href="javascript:void(0)" data-toggle="modal" data-target="#configModal"  onclick="editRepoAuth({{value.authId}})">修改</a>
                                                    {{/if}}
                                                </td>
											</tr>
											{{/each}}
										</script>
									</table>

								</div>
							</div>
							<div class="row col-sm-12" id="listPaginator">
							</div>
						</div><!-- /.box-body -->
					</div><!-- /.box -->
				</div><!-- /.col -->
			</div><!-- /.row -->

			<!-- Modal -->
			<div class="modal fade" id="configModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							<h4 class="modal-title" id="myModalLabel">新增仓库权限</h4>
						</div>
						<div class="modal-body">
							<form class="form-horizontal">
								<div class="form-group">
									<label class="control-label col-sm-3">名称:</label>
									<div class="col-sm-9">
										<input type="hidden" name="authId" class="form-control" id="authId" placeholder="" value="0">
										<input type="text" name="authName" class="form-control" id="authName" placeholder="英文字符串，全局唯一" maxlength="20">
									</div>
								</div>

                                <div class="form-group">
                                    <label class="control-label col-sm-3">仓库类型:</label>
                                    <div class="radio col-sm-9">
                                        <label>
                                            <input type="radio" id="gitRepoType" name="repoType" value="<%=ModuleRepoType.GIT.getValue()%>" checked="checked">GIT
                                        </label>
                                        <label>
                                            <input type="radio" id="svnRepoType"  name="repoType" value="<%=ModuleRepoType.SVN.getValue()%>" >SVN
                                        </label>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-sm-3">角色关联:</label>
                                    <div class="col-sm-9">
                                        <input type="hidden" name="roleId" id="roleId" style="width: 100%;" required="true"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-sm-3">account:</label>
                                    <div class="col-sm-9">
                                        <input id="account" name="account" type="text" class="form-control"  placeholder="帐号"></input>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-sm-3">password:</label>
                                    <div class="col-sm-9">
                                        <input id="password" name="password" type="password" class="form-control"  placeholder="密码"></input>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-sm-3">权限描述:</label>
                                    <div class="col-sm-9">
                                        <textarea id="authDesc" name="authDesc" class="form-control" placeholder="权限描述"></textarea>
                                    </div>
                                </div>



							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<button type="button" class="btn btn-primary" onclick="save()">保存</button>
						</div>
					</div>
				</div>
			</div>
		</section><!-- /.content -->
	</div><!-- /.content-wrapper -->

	<!-- footer -->
	<jsp:include page="/include/footer.jsp"/>

	<!-- control sidebar -->
	<jsp:include page="/include/control-sidebar.jsp"/>
</div><!-- ./wrapper -->

<!-- bottom js -->
<%@ include file="/include/bottom-js.jsp"%>
<script type="text/javascript">
    function search() {
        $('#listTable').artPaginate({
            // 获取数据的地址
            url: "/admin/repoAuth/list",
            // 显示页码的位置
            paginator: 'listPaginator',
            // 模版ID
            tpl: 'listTableTpl',
            // 请求的参数表，默认page=1, pageSize=20
            params: {}
        });
    }

	function addRepoAuth() {
        $('#myModalLabel').html('新增仓库权限');
        $('#authId').val('0');
        $('#authName').val('');
        $('#authName').attr("readonly", false);
        $('#authDesc').val('');
        $('#password').val('');
        $('#account').val('');

        $.get('/admin/repoAuth/getAuth', {
            authId: "0"
        }, function (json) {
          loadRepoRoles(json);
        }, 'json');
    }

    // 修改
    function editRepoAuth(authId) {
        $('#myModalLabel').html('编辑仓库权限');
        $('#authId').val(authId);
        $.get('/admin/repoAuth/getAuth', {
            authId: $('#authId').val()
        }, function (json) {
            if (json.success) {
              var repoAuth = json.object.repoAuth;
              $('#account').val(repoAuth.account);
              $('#authName').val(repoAuth.authName);
              $('#authName').attr("readonly", true);
              $('#authDesc').val(repoAuth.authDesc);
              if(repoAuth.repoType == <%=ModuleRepoType.GIT.getValue()%>){
                $('#gitRepoType').attr("checked","checked");
              }
              else{
                $('#svnRepoType').attr("checked","checked");
              }

              loadRepoRoles(json);

            } else {
              BootstrapDialog.alert(json.message);
            }
        }, 'json');
    }

    function loadRepoRoles(json) {
        var roles = json.object.roles;
        var name = $('#roleId');
        name.empty();
        var data = [];

        if (roles.length > 0) {
        for (var i = 0; i < roles.length; i++) {
          var item = roles[i];
          data.push({
            id: item.roleId,
            text: item.roleName
          });
        }
        }
        name.select2({
        'allowClear' : true,
        'data': data,
        'multiple': true,
        'placeholder': '请选择一个角色'
        });
        var existRoleIds = json.object.existRoleIds;
        if(existRoleIds){
          $('#roleId').select2('val', existRoleIds).trigger("change");
          /*for (var i = 0; i < existRoleIds.length; i++) {
            var item = existRoleIds[i];

          }*/
        }

    }

    function save() {
        var authId = $('#authId').val();
        var authName = $('#authName').val();
		var repoType = $('input:radio[name="repoType"]:checked').val();
        var account = $('#account').val();
        var password = $('#password').val();
        var authDesc = $('#authDesc').val();
        var roleIds = $('#roleId').val();
        $.post('/admin/repoAuth/save.do', {
            authId: authId,
            authName: authName,
			repoType: repoType,
            account: account,
            password: password,
            authDesc: authDesc,
            roleIds: roleIds,
        }, function (json) {
            $('#configModal').modal('hide');
            if (json.success) {
                BootstrapDialog.alert(json.message, function () {
                    search();
                });
            } else {
                BootstrapDialog.alert(json.message);
            }
        }, 'json');
    }

	$(function(){
        search();
	});
</script>
</body>
</html>
