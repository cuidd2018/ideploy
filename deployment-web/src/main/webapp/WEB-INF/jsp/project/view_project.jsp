<%@ page import="io.ideploy.deployment.common.enums.ModuleType" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/include/meta.html" %>
    <title>查看项目</title>
    <style type="text/css">
        #moduleListTable th, td {
            padding: 4px;
        }
    </style>
</head>
<body class="skin-blue sidebar-mini">
<div class="wrapper">

    <!-- header -->
    <jsp:include page="/include/header.jsp"/>

    <!-- sidebar -->
    <jsp:include page="/include/sidebar.jsp"/>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1 id="titleMessage">
                项目详情
                <small></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="/admin/project/listProject.xhtml"><i class="fa fa-dashboard"></i> 项目列表</a></li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <section class="col-md-4">
                    <div class="box box-primary">
                        <div class="box-header with-border text-center">
                            <h3 class="box-title" id="projectName"></h3>
                        </div>
                        <div class="box-body">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>模块</th>
                                    <th style="width: 90px;">操作</th>
                                </tr>
                                </thead>
                                <script id="moduleListTpl" type="text/html">
                                    {{each modules as value i}}
                                    <tr id="trModule{{value.moduleId}}">
                                        <td style="padding:4px;"><a href="#" onclick="viewModule({{value.moduleId}})"
                                                                    class="btn btn-block">{{value.moduleNameZh}}</a>
                                        </td>
                                        <td style="padding:4px;"><a
                                                href="/admin/project/editModule.xhtml?moduleId={{value.moduleId}}"
                                                data-skin="skin-blue" class="btn btn-primary btn-sm"><i
                                                class="fa fa-edit"></i></a>
                                            &nbsp;
                                            <a href="#" onclick="javascript:deleteModule({{value.moduleId}})"
                                               data-skin="skin-blue" class="btn btn-primary btn-sm"><i
                                                    class="fa fa-trash-o"></i></a>
                                        </td>
                                    </tr>
                                    {{/each}}
                                </script>
                                <tbody id="moduleListTable">

                                </tbody>
                            </table>
                        </div>
                        <div class="box-footer">
                            <div class="text-center">
                                <button class="btn btn-warning" id="addModule"
                                        type="button" onclick="addModule()">新增模块
                                </button>
                            </div>
                        </div>
                    </div>
                </section>
                <section class="col-md-8" style="padding-left: 0px;">
                    <div class="box box-primary">
                        <div class="box-body">

                            <div class="row">
                                <input type="hidden" value="" id="projectId"/>
                                <input type="hidden" value="" id="currentModuleId"/>


                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">模块</label>
                                    <div class="col-md-6" id="moduleNameDiv">

                                    </div>

                                    <div class="col-md-43">
                                        <button class="btn btn-warning pull-right"
                                                style="margin-left: 10px;margin-right:15px;"
                                                type="button" onclick="deployModule();">发布版本
                                        </button>
                                        <a class="btn btn-warning pull-right offset1" type="button" href="#"
                                           id="editModuleHref">编辑模块
                                        </a>
                                    </div>

                                </div>

                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">SVN/GIT地址</label>
                                    <div class="col-md-10" id="repoUrl">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">类型</label>
                                    <div class="col-md-10" id="moduleType">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">pre deploy</label>
                                    <div class="col-md-10">
                                        <pre id="preDeploy">

                                        </pre>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">restart</label>
                                    <div class="col-md-10">
                                        <pre id="restartShell">

                                        </pre>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">post deploy</label>
                                    <div class="col-md-10">
                                        <pre id="postDeploy">

                                        </pre>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">compile</label>
                                    <div class="col-md-10">
                                        <pre id="compileShell">

                                        </pre>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">stop shell</label>
                                    <div class="col-md-10">
                                        <pre id="stopShell">

                                        </pre>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label text-right">JVM参数</label>
                                    <div class="col-md-10">
                                        <div class="row" id="jvmArgsContent">
                                            <script id="jvmTpl" type="text/html">
                                                {{each moduleJvms as value i}}
                                                <div class="col-sm-12">{{value.envName}}</div>
                                                <div class="col-sm-12">
                                                    <pre class="jvmArgs">{{value.jvmArgs}}</pre>
                                                </div>
                                                {{/each}}
                                            </script>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" id="groups">
                                <ul id="serverGroups" class="nav nav-tabs">
                                </ul>
                                <div id="groupContent" class="tab-content" style="padding-top: 10px;">
                                </div>
                            </div>
                            <%--<div class="row text-center">--%>
                            <%--<div class="form-group">--%>
                            <%--<button class="btn btn-primary"--%>
                            <%--type="button" onclick="batchRestart();">批量重启--%>
                            <%--</button>--%>
                            <%--</div>--%>
                            <%--</div>--%>

                        </div>
                    </div>
                </section>

            </div>

        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->

    <!-- 添加服务器模态窗口 -->
    <div class="modal" id="viewServerModel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">查看服务器</h4>
                </div>
                <div class="modal-body form-horizontal">
                    <input type="hidden" id="currentServerId" />
                    <div class="form-group">
                        <label class="col-md-2 control-label">实例名称</label>
                        <div class="col-md-10">
                            <input class="form-control"  readonly="readonly"  type="text" id="serverName" name="serverName" title="实例名称" placeholder="实例名称（唯一值）" maxlength="60">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">实例IP</label>
                        <div class="col-md-10">
                            <input class="form-control"  readonly="readonly"  type="text" id="serverIP" name="serverIP" title="实例IP" placeholder="实例IP" maxlength="60">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">启动参数<i class='fa fa-question-circle' id="helpIpShellArgs"></i></label>
                        <div class="col-md-10">
                            <textarea class="form-control" readonly="readonly" id="ipShellArgs" placeholder="当前ip特定shell启动参数（可为空）" ></textarea>
                            <%--<input class="form-control" type="" id="ipShellArgs" name="ipShellArgs" title="实例shell启动参数" placeholder="当前ip特定shell启动参数（可为空）" maxlength="60">--%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- footer -->
    <jsp:include page="/include/footer.jsp"/>

    <!-- control sidebar -->
    <jsp:include page="/include/control-sidebar.jsp"/>
</div><!-- ./wrapper -->

<!-- bottom js -->
<%@ include file="/include/bottom-js.jsp" %>
<script type="text/javascript">
    // 项目所有模块信息,包含服务器组和服务器
    var modulesInfo, envs;

    $(function () {
        loadProject();
    });

    function loadProject() {
        var projectId;
        if ($.getUrlParam("projectId")) {
            projectId = $.getUrlParam("projectId");
            $("#projectId").val(projectId);

            $.get("/admin/project/projectDetail", {'projectId': projectId}, function (json) {
                if (json.success) {
                    var projectDetail = json.object;
                    var project = projectDetail.project;
                    $('#projectName').text(project.projectName + "项目");

                    envs = projectDetail.projectEnvs;

                    modulesInfo = projectDetail.modules;
                    initModules(projectDetail);
                    if (projectDetail.modules) {
                        $(".moduleName").css("color", "black");
                        viewModule(projectDetail.modules[0].moduleId);
                    }
                } else {
                    BootstrapDialog.alert(json.message);
                }
            });
        }
        else {
            BootstrapDialog.alert('请在【项目列表】选择一个项目进行查看');
        }
    }

    function initModules(projectDetail) {
        var htmlTxt = template('moduleListTpl', projectDetail);
        $('#moduleListTable').html(htmlTxt);
        if (projectDetail.modules && projectDetail.modules.length && projectDetail.modules.length > 0) {
            setModuleTextActive(projectDetail.modules[0].moduleId);
        }
    }

    function initJvmArgs(moduleInfo) {
        var jvmHtml = template('jvmTpl', moduleInfo);
        $("#jvmArgsContent").html(jvmHtml);
    }

    function deleteModule(moduleId) {
        BootstrapDialog.confirm('确定要删除模块？', function (result) {
            if (result) {
                $.post("/admin/project/deleteModule.do", {'moduleId': moduleId}, function (data) {
                    if (data.success) {
                        BootstrapDialog.alert("删除成功");
                        $('#module' + moduleId).remove();
                    } else {
                        BootstrapDialog.alert("删除失败");
                    }
                });
            }
        });

    }

    function addModule() {
        window.location.href = "/admin/project/editModule.xhtml?projectId=" + $('#projectId').val();
    }

    function setModuleTextActive(moduleId) {
        var tr = $('#trModule' + moduleId);
        tr.find("a").first().css("color", "white");
        tr.addClass('bg-light-blue-active');
    }

    function setModuleTextInactive(moduleId) {
        if (moduleId) {
            var tr = $('#trModule' + moduleId);
            if (tr) {
                tr.removeClass('bg-light-blue-active');
                tr.find("a").first().css("color", "#337ab7");
            }
        }
    }

    function viewModule(moduleId) {
        var currentModule = $("#currentModuleId");
        setModuleTextInactive(currentModule.val());
        setModuleTextActive(moduleId);
        currentModule.val(moduleId);
        $('#editModuleHref').prop('href', '/admin/project/editModule.xhtml?moduleId=' + moduleId);

        for (var k in modulesInfo) {
            var module = modulesInfo[k];
            if (moduleId == module.moduleId) {
                $("#repoUrl").html(module.repoUrl);
                var moduleTypeName;
                if (module.moduleType == <%=ModuleType.WEB_PROJECT.getValue()%>) {
                    moduleTypeName = "<%=ModuleType.WEB_PROJECT.getName()%>";
                }
                if (module.moduleType == <%=ModuleType.STATIC.getValue()%>) {
                    moduleTypeName = "<%=ModuleType.STATIC.getName()%>";
                }
                if (module.moduleType == <%=ModuleType.SERVICE.getValue()%>) {
                    moduleTypeName = "<%=ModuleType.SERVICE.getName()%>";
                }
                $("#moduleType").html(moduleTypeName);
                $('#moduleNameDiv').html(module.moduleName + '(' + module.moduleNameZh + ')');
                $('#preDeploy').html(module.preShell);
                $('#postDeploy').html(module.postShell);
                $('#restartShell').html(module.restartShell);
                $('#compileShell').html(module.compileShell);
                $('#stopShell').html(module.stopShell);
                $('#jvmArgs').html(module.jvmArgs);
                buildGroup(module.serverGroups, module.moduleType);

                initJvmArgs(module);
            }
        }
    }

    function buildGroup(serverGroups, moduleType) {
        var groupsHtml = "", groupContentHtml = "";
        for (var i in serverGroups) {
            var group = serverGroups[i];
            var contentId = "serverGroup" + group.groupId;
            //初始化服务器
            var serverHtml = "";
            for (var j in group.servers) {
                serverHtml = serverHtml + "<div class='row' id='oldServer_" + group.servers[j].serverId +"' data-servername='" + group.servers[j].serverName  + "' data-serverip='" + group.servers[j].ip + "' data-ipShellArgs='" + group.servers[j].ipShellArgs  + "'>" +
                    "<div class='col-xs-4'><label><input class='server' type='checkbox'  /><span style='font-weight: normal;font-size: 14px;'>" + group.servers[j].serverName + "(" + group.servers[j].ip + ")</span></label></div>";
                if (moduleType != <%=ModuleType.STATIC.getValue()%>) {
                    var ipShellArgs = group.servers[j].ipShellArgs;
                    if(ipShellArgs && ipShellArgs.length > 20){
                       ipShellArgs = ipShellArgs.substring(0, 19) + "...";
                    }
                    serverHtml = serverHtml + "<div class='col-xs-4'>参数：<a onclick='javascript:viewServer(\"oldServer_" + group.servers[j].serverId + "\");return false;' href='#' >"+ ipShellArgs +"</a></div>";
                    serverHtml = serverHtml + "<div class='col-xs-2'><a onclick='javascript:restartServer(" + group.servers[j].serverId + ',\"' + group.servers[j].ip + "\");return false;' href='#' ><i class='fa fa-fw fa-refresh'></i>Restart</a></div>";
                    serverHtml = serverHtml + "<div class='col-xs-2'><a onclick='javascript:stopServer(" + group.servers[j].serverId + ',\"' + group.servers[j].ip + "\");return false;' href='#' ><i class='fa fa-fw fa-power-off'></i>Stop</a></div>";
                }
                serverHtml = serverHtml + "</div>";
            }

            var envName = getEnvName(group.envId);
            if (i == 0) {
                groupsHtml = groupsHtml + "<li class='active'><a style=' text-align: center;' href='#" + contentId + "' data-toggle='tab'>" + group.groupName +
                    "<br/><span style='font-size: 9px;'>" + envName + "</span></a></li>";
                groupContentHtml = groupContentHtml + "<div class='tab-pane fade in active' id='" + contentId + "'>" + serverHtml +
                    "</div>";
            } else {
                groupsHtml = groupsHtml + "<li  data-groupId='" + group.groupId + "'data-groupname='" + group.groupName + "' data-groupenv='" + group.envId +
                    "'><a style=' text-align: center;' href='#" + contentId + "' data-toggle='tab'>" + group.groupName + "<br/><span style='font-size: 9px;'>" + envName + "</span></a></li>"
                groupContentHtml = groupContentHtml + "<div class='tab-pane fade' id='" + contentId + "'>" + serverHtml + "</div>";
            }
        }
        $("#serverGroups").html(groupsHtml);
        $("#groupContent").html(groupContentHtml);
    }

    function getEnvName(envId) {
        var envName = "";
        for (var i in envs) {
            if (envs[i].envId == envId) {
                envName = envs[i].envName;
            }
        }
        return envName;
    }

    function deployModule() {
        var currentModuleId = $("#currentModuleId").val();
        window.location.href = "/admin/deploy/create.xhtml?moduleId=" + currentModuleId;
    }

    function restartServer(serverId, ip) {
        BootstrapDialog.confirm('确认要restart服务' + ip + '？', function (result) {
            if (result) {
                showTips(ip + '正在restart服务，请等待...');
                $.post("/admin/project/restartServer.do", {'serverId': serverId}, function (data) {
                    console.log('success: ' + data.success + ', message: ' + data.message);
                    BootstrapDialog.alert(data.message);
                });
            }
        });

    }

    function viewServer(serverId) {
      $("#viewServerModel").modal('show');
      $("#serverName").val($("#" + serverId).attr("data-serverName"));
      $("#serverIP").val($("#" + serverId).attr("data-serverIP"));
      $("#ipShellArgs").val($("#" + serverId).attr("data-ipShellArgs"));
    }

    function stopServer(serverId, ip) {
        BootstrapDialog.confirm('确认要stop服务' + ip + '？', function (result) {
            if (result) {
                showTips(ip + '正在stop服务，请等待...');
                $.post("/admin/project/stopServer.do", {'serverId': serverId}, function (data) {
                    console.log('success: ' + data.success + ', message: ' + data.message);
                    BootstrapDialog.alert(data.message);
                });
            }
        });
    }

    function showTips(message) {
        var stack_bottomright = {"dir1": "up", "dir2": "left", "firstpos1": 25, "firstpos2": 25};
        var opts = {
            title: "通知",
            text: message,
            addclass: "stack-bottomright",
//            type: "info",
            stack: stack_bottomright
        };
        new PNotify(opts);
    }

    $(function() {
      $('#helpIpShellArgs').popover({
        content: 'ip自定义启动shell参数，解决服务器职责分配问题，场景:<br/>' +
        '192.168.10.25（对外服务，也运行定时任务）<br/>' +
        '192.168.10.24（对外服务）<br/>' +
        '192.168.10.25的启动参数配置 -DstartJob=true 完成职责分配<br/>' +
        '<br/>' +
        '启动参数可以通过变量名（IP_SHELL_ARGS）引用，比如在 启动入口 这样传递：<br/>' +
        '/data/project/pay-order-impl/start.sh &#36;{IP_SHELL_ARGS}<br/>',
        trigger: 'click',
        placement: 'bottom',
        html: 'true',
        template: '<div class="popover" role="tooltip"><div class="arrow">' +
        '</div><div class="popover-content" style="background-color:#f5f5f5;width:400px"></div></div>'
      })
    })
</script>
</body>
</html>
