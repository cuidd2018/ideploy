<%@ page import="io.ideploy.deployment.common.enums.ModuleType" %>
<%@ page import="io.ideploy.deployment.common.enums.ModuleRepoType" %>
<%@ page import="io.ideploy.deployment.common.Constants" %>
<%@ page import="io.ideploy.deployment.cfg.AppConfigFileUtil" %>
<%@ page import="io.ideploy.deployment.common.util.JvmArgUtil" %>
<%@ page import="io.ideploy.deployment.common.ModuleUserShellArgs" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/include/meta.html" %>
    <title>编辑或添加模块</title>
    <style>
        textarea.form-control{
            height:80px;
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
                编辑或添加模块
                <small></small>
            </h1>
            <ol class="breadcrumb">
                <li><a id="viewProjectLink" href="#"><i class="fa fa-dashboard"></i> 返回项目</a></li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <section class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-body">
                            <form class="form-horizontal">
                                <div class="row">
                                    <div class="form-group">
                                        <input type="hidden" value="" id="projectId"/>
                                        <input type="hidden" value="" id="moduleId"/>
                                        <label class="col-md-2 control-label text-right">项目</label>
                                        <div class="col-md-3 checkbox" id="projectName">
                                        </div>

                                        <label class="col-md-2 control-label text-right">负责人</label>
                                        <div class="col-md-3 checkbox" id="projectManagerName">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label text-right">模块名称</label>
                                        <div class="col-md-3">
                                            <input id="moduleNameZh" placeholder="模块中文名称，比如：优惠券" class="form-control"
                                                   type="text"/>
                                        </div>

                                        <label class="col-md-2 control-label text-right">类型</label>
                                        <div class="col-md-4">
                                            <div class="radio">
                                                <label><input type="radio" name="moduleType"
                                                              value="<%=ModuleType.SERVICE.getValue()%>"
                                                              onclick="setDefaultShell()"/><%=ModuleType.SERVICE.getName()%>
                                                </label>
                                                <label><input type="radio" name="moduleType"
                                                              value="<%=ModuleType.WEB_PROJECT.getValue()%>"
                                                              onclick="setDefaultShell()"/><%=ModuleType.WEB_PROJECT.getName()%>
                                                </label>
                                                <label><input type="radio" name="moduleType"
                                                              value="<%=ModuleType.STATIC.getValue()%>"
                                                              onclick="setDefaultShell()"/><%=ModuleType.STATIC.getName()%>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label text-right">仓库地址</label>
                                        <div class="col-md-6">
                                            <input type="text" name="repoUrl" id="repoUrl"  class="form-control" value=""
                                                   placeholder="svn基地址,不要包含tags/、trunk/、branches"
                                                   title="svn基地址,不要包含tags/、trunk/、branches"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label text-right">仓库认证</label>
                                        <div class="col-md-2" style="margin-top: 5px">
                                            <input type="hidden" name="repoAuth" id="repoAuth"
                                                   style="width: 100%;" required="true"/>
                                        </div>
                                        <div class="col-md-1" style="padding-left: 0px">
                                            <a href="/admin/repoAuth/listRepoAuth.xhtml" class="btn btn-link">新增认证</a>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label text-right">服务名</label>
                                        <div class="col-md-2">
                                            <input type="text" name="moduleName" id="moduleName"  class="form-control"
                                                   value=""
                                                   placeholder="服务名，通常跟模块名一致"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="form-group">
                                        <label class="col-md-2 control-label text-right">流程控制</label>
                                        <%--是否是发布上线 --%>
                                        <div class="col-md-2">
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="needAudit" id="needAudit"
                                                              value="1"/>
                                                    发布审核(生产)</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </section>
            </div>

            <div class="row">
                <section class="col-md-5">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">通用配置</h3>
                            <div class="box-tools pull-right">
                                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="form-group">
                                <label>自定义变量：</label><i class="fa fa-question-circle" id="helpDeployArgs"></i><br/>
                                <textarea id="deployArgs" name="deployArgs" class="form-control" placeholder="targetDir=/data/project/pay-order-impl #部署目标路径"></textarea>
                            </div>
                            <div class="form-group">
                                <label>编译脚本：</label><i class="fa fa-question-circle" id="helpCompileShell"></i><br/>
                                <textarea class="form-control" id="compileShell" placeholder="编译脚本"
                                          onkeypress="changeCompileShell()"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">部署配置</h3>
                            <div class="box-tools pull-right">
                                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="form-group">
                                <label>preDeploy</label><i class="fa fa-question-circle" id="helpPreDeploy"></i><br/>
                                <textarea class="form-control" id="preDeploy" placeholder="开始部署执行操作"></textarea>
                            </div>
                            <div class="form-group">
                                <label>postDeploy</label><i class="fa fa-question-circle" id="helpPostDeploy"></i><br/>
                                <textarea class="form-control" id="postDeploy" placeholder="部署完成执行操作"></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">启动配置</h3>
                            <div class="box-tools pull-right">
                                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="form-group">
                                <label>pre start：</label><i class="fa fa-question-circle" id="helpPreShell"></i><br/>
                                <textarea id="preShell" class="form-control" placeholder="重启前执行"></textarea>

                            </div>
                            <div class="form-group">
                                <label>启动入口：</label><i class="fa fa-question-circle" id="helpRestartShell"></i><br/>
                                <textarea class="form-control" id="restartShell"
                                          placeholder="com.alibaba.dubbo.container.Main" onblur="showJvmArgs()"
                                          onkeypress="changeRestartShell()"></textarea>
                                <br/>
                            </div>
                            <div class="form-group">
                                <label>post start：</label><i class="fa fa-question-circle" id="helpPostShell"></i><br/>
                                <textarea class="form-control" id="postShell" placeholder="重启后执行"></textarea>
                            </div>
                            <div class="form-group">
                                <label>停止服务脚本</label><i class="fa fa-question-circle" id="helpStopShell"></i><br/>
                                <textarea class="form-control" id="stopShell" placeholder="停止服务脚本，启动入口填了Main Class可忽略"
                                          onkeypress="changeStopShell()"></textarea>
                            </div>
                        </div>
                    </div>
                </section>

                <section class="col-md-7" style="padding-left: 0px;">
                    <%--环境与jvm参数--%>
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">参数配置</h3>
                            <div class="box-tools pull-right">
                                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <ul class="nav nav-tabs">
                                        <li class="active"><a href="#jvmConf" data-toggle="tab" aria-expanded="true">JVM参数</a>
                                        </li>
                                        <li><a href="#resinConf" data-toggle="tab" aria-expanded="true">Resin参数</a></li>
                                    </ul>
                                    <%--内容--%>
                                    <div class="tab-content">
                                        <div class="tab-pane active" id="jvmConf">
                                            <div class="row checkbox" id="argTipDiv">
                                                <div class="col-md-12 text-danger">
                                                    &gt;&gt;「<%=ModuleType.SERVICE.getName()%>」自定义脚本
                                                    和「<%=ModuleType.STATIC.getName()%>」 不用填写JVM参数<br/>
                                                    &gt;&gt;「<%=ModuleType.WEB_PROJECT.getName()%>
                                                    」和「<%=ModuleType.SERVICE.getName()%>
                                                    」MainClass启动，系统默认添加以下JVM参数：<%=JvmArgUtil.getDefaultArgs()%>
                                                </div>
                                            </div>
                                            <form class="form-horizontal">
                                                <div id="jvmArgsContent">
                                                    <script id="jvmTpl" type="text/html">
                                                        {{each moduleJvms as value i}}
                                                        <div class="form-group" data-envId="{{value.envId}}"
                                                             data-envName="{{value.envName}}"
                                                             data-moduleJvmId="{{value.moduleJvmId}}">
                                                            <label class="col-md-1 control-label text-left">{{value.envName}}</label>
                                                            <div class="col-md-11">
                                                                <textarea class="form-control jvmArgs"
                                                                          placeholder="JVM参数表">{{value.jvmArgs}}</textarea>
                                                                <%--<input type="text" class="form-control jvmArgs"
                                                                       placeholder="JVM参数表" value="{{value.jvmArgs}}"/>--%>
                                                            </div>
                                                        </div>
                                                        {{/each}}
                                                    </script>
                                                </div>
                                            </form>
                                        </div>
                                        <div class="tab-pane" id="resinConf">
                                            <form class="form-horizontal checkbox">
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label text-left">域名</label>
                                                    <div class="col-md-5">
                                                        <input id="domain" name="domain" type="text"
                                                               class="form-control" placeholder="不包含http/https"
                                                               title="不包含http/https" value=""/>
                                                    </div>
                                                    <div class="col-md-5">
                                                        <p class="text-left help-block">
                                                            比如pf.ideploy.io，不需要写dev/test</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label text-left">域名别名</label>
                                                    <div class="col-md-5">
                                                        <input id="aliasDomain" name="aliasDomain" type="text"
                                                               class="form-control" placeholder="不包含http/https"
                                                               title="不包含http/https" value=""/>
                                                    </div>
                                                    <div class="col-md-5">
                                                        <p class="text-left help-block">域名别名，多个则用空格隔开，不需要写dev/test</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label text-left">端口</label>
                                                    <div class="col-md-2">
                                                        <input id="httpPort" name="httpPort" type="text"
                                                               class="form-control" placeholder="HTTP端口" title="HTTP端口"
                                                               value=""/>
                                                    </div>
                                                    <div class="col-md-2">
                                                        <input id="serverPort" name="serverPort" type="text"
                                                               class="form-control" placeholder="Server端口"
                                                               title="Server端口" value=""/>
                                                    </div>
                                                    <div class="col-md-2">
                                                        <input id="watchdogPort" name="watchdogPort" type="text"
                                                               class="form-control" placeholder="Watchdog端口"
                                                               title="Watchdog端口" value=""/>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <p class="text-left help-block">范围分别是808x/680x/660x</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-3 control-label text-left">thread-max</label>
                                                    <div class="col-md-2">
                                                        <input id="threadMax" name="threadMax" type="text"
                                                               class="form-control" placeholder="可以不填" value=""/>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p class="text-left help-block">默认不填写</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-3 control-label text-left">keepalive-max</label>
                                                    <div class="col-md-2">
                                                        <input id="keepaliveMax" name="keepaliveMax" type="text"
                                                               class="form-control" placeholder="可以不填" value=""/>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p class="text-left help-block">默认不填写</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-3 control-label text-left">keepalive-timeout</label>
                                                    <div class="col-md-2">
                                                        <input id="keepaliveTimeout" name="keepaliveTimeout" type="text"
                                                               class="form-control" placeholder="默认15s" value=""/>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p class="text-left help-block">单位：秒，默认15s</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-3 control-label text-left">socket-timeout</label>
                                                    <div class="col-md-2">
                                                        <input id="socketTimeout" name="socketTimeout" type="text"
                                                               class="form-control" placeholder="socketTimeout"
                                                               value=""/>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p class="text-left help-block">单位：秒，默认30s</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-3 control-label text-left">resin.xml</label>
                                                    <div class="col-md-4">
                                                        <label>
                                                            <input id="createEveryTime" name="createEveryTime"
                                                                   type="checkbox" checked/>每次发布都重新生成resin.xml</label>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <p class="text-left help-block">如果为false，只生成一次</p>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>

                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>

                    <%--服务器组--%>
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">服务器配置</h3>
                            <div class="box-tools pull-right">
                                <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <ul id="serverGroups" class="nav nav-tabs">
                                    </ul>
                                    <%--内容--%>
                                    <div id="groupContent" class="tab-content">

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="box-footer">
                        <div class="row">
                            <div class="col-md-12">
                                <h4>“配置脚本”可用变量：</h4>
                                <!-- 请勿修改下面的空格 -->
                                <pre>
&#36;{compileDir}：编译服务器代码目录，比如 /data/project/pay-parent/
&#36;{targetDir}：编译结果文件存放目录，发布系统会把目录下文件部署到&#36;{deployDir}中
&#36;{env}：哪个环境，例如dev/test
                                </pre>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

        </section>


        <div class="row" style="text-align: center; padding-bottom: 30px;">
            <button class="btn btn-primary"
                    type="button" id="submit" onclick="saveModule()">提交
            </button>
        </div>

        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->

    <%--模态窗口 添加服务器组--%>
    <div class="modal" id="addGroup">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">添加服务器组</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <form class="form-horizontal col-md-12">
                            <input type="hidden" value="" id="currentGroupId"/>
                            <%--<input id="hiddenText" type="text" style="display:none" />--%>
                            <div class="form-group">
                                <label class="control-label col-sm-2"> 环境: </label>
                                <div class="col-md-6">
                                    <div id="envs" class="radio">

                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2"> 名称: </label>
                                <div class="col-md-6">
                                    <input type="text" id="groupName" class="form-control" maxlength="45"/>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>

                <div class="modal-footer">
                    <div class="col-md-3"></div>
                    <div class="col-md-2">
                        <button type="button" class="btn btn-primary" id="addBtn" onclick="addServerGroup()"
                                data-loading-text="添加中...">
                            保存
                        </button>
                    </div>
                    <div class="col-md-3">
                        <button type="button" class="btn" data-dismiss="modal">取消
                        </button>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <!-- 添加服务器模态窗口 -->
    <div class="modal" id="addServerModel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">添加服务器IP</h4>
                </div>
                <div class="modal-body form-horizontal">
                    <input type="hidden" id="currentServerId" />
                    <div class="form-group">
                        <label class="col-md-2 control-label">实例名称</label>
                        <div class="col-md-10">
                            <input class="form-control" type="text" id="serverName" name="serverName" title="实例名称" placeholder="实例名称（唯一值）" maxlength="60">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">实例IP</label>
                        <div class="col-md-10">
                            <input class="form-control" type="text" id="serverIP" name="serverIP" title="实例IP" placeholder="实例IP" maxlength="60">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">启动参数<i class='fa fa-question-circle' id="helpIpShellArgs"></i></label>
                        <div class="col-md-10">
                            <textarea class="form-control" id="ipShellArgs" name="ipShellArgs" placeholder="当前ip特定shell启动参数（可为空）" ></textarea>
                            <%--<input class="form-control" type="text" id="ipShellArgs" name="ipShellArgs" title="实例shell启动参数" placeholder="当前ip特定shell启动参数（可为空）" maxlength="60">--%>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="col-md-3"></div>
                    <div class="col-md-2">
                        <button type="button" class="btn btn-primary" onclick="addServer()"
                                data-loading-text="添加中...">
                            确定
                        </button>
                    </div>
                    <div class="col-md-3">
                        <button type="button" class="btn" data-dismiss="modal">取消
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="serverListDiv" class="dropdown-menu"
         style="z-index:10000;display:none;width:300px;height:240px;overflow:scroll;border:1px solid #3c8dbc;">
        <table class="table table-bordered table-hover dataTable" role="grid" style="font-size: 12px;">
            <thead>
            <tr>
                <%--<td>区</td>--%>
                <td>名称</td>
                <td>内网IP</td>
            </tr>
            </thead>
            <tbody id="serverListDivContent">

            </tbody>
        </table>
    </div>
    <!-- footer -->
    <jsp:include page="/include/footer.jsp"/>

    <!-- control sidebar -->
    <jsp:include page="/include/control-sidebar.jsp"/>
</div><!-- ./wrapper -->

<!-- bottom js -->
<%@ include file="/include/bottom-js.jsp" %>
<script id="chooseServerTpl" type="text/html">

    {{each data as value i}}
    <tr title="公网IP: {{value.publicIpAddress}}, 规格: {{value.spec}}"
        onclick="chooseServer('{{value.instanceName}}','{{value.innerIpAddress}}')">
        <%--<td>{{value.regionId}}</td>--%>
        <td>{{value.instanceName}}</td>
        <td>{{value.innerIpAddress}}</td>
    </tr>
    {{/each}}

</script>
<script type="text/javascript">

    //全局的 环境列表
    var globalEnvs = "";
    var restartShellChanged = false;
    var stopShellChanged = false;
    var compileShellChanged = false;
    var pageX = 0;
    var pageY = 0;

    $(function () {
        loadModuleInfo();

        $(document).mousemove(function (e) {
            pageX = e.pageX;
            pageY = e.pageY;
        });
    });

    function loadModuleInfo() {
        var projectId, moduleId;
        if ($.getUrlParam("projectId")) {
            projectId = $.getUrlParam("projectId");
            getModuleBasicInfo(projectId);
            $('#viewProjectLink').prop('href', '/admin/project/viewProject.xhtml?projectId=' + projectId);
            return;
        }

        if ($.getUrlParam("moduleId")) {
            moduleId = $.getUrlParam("moduleId");
            getModuleInfo(moduleId);
        } else {
            BootstrapDialog.alert('请选择一个模块进行编辑');
        }
    }

    function changeRestartShell() {
        restartShellChanged = true;
    }

    function changeStopShell() {
        stopShellChanged = true;
    }

    function changeCompileShell() {
        compileShellChanged = true;
    }

    function showJvmArgs() {

    }

    function setDefaultShell() {
        var containerShell = '<%=AppConfigFileUtil.getWebContainerShell()%>';
        var moduleType = $('input:radio[name="moduleType"]:checked').val();

        if (moduleType) {
            var restartShell = $.trim($('#restartShell').val());
            var stopShell = $.trim($('#stopShell').val());
            var compileShell = $.trim($('#compileShell').val());
            if (moduleType == <%=ModuleType.WEB_PROJECT.getValue()%>) {
                if (restartShell == '' || !restartShellChanged) {
                    $('#restartShell').val("# web项目重启指令\n" + containerShell + " stop\n" + containerShell + " start");
                }
                if (stopShell == '' || !stopShellChanged) {
                    $('#stopShell').val("# web项目stop指令\n" + containerShell + " stop");
                }
                if (compileShell == '' || !compileShellChanged) {
                    $('#compileShell').val("mvn -P=$" + "{env} -Dmaven.test.skip=true -U clean install\ncp -f "+calcModulePath()+"/target/*.war $" + "{targetDir}");
                }
            } else if (moduleType == <%=ModuleType.SERVICE.getValue()%>) {
                if (restartShell == '' || !restartShellChanged) {
                    $('#restartShell').val("com.alibaba.dubbo.container.Main");
                }
                if (compileShell == '' || !compileShellChanged) {
                    $('#compileShell').val("mvn -P=$" + "{env} -Dmaven.test.skip=true -U clean install\ncp `find "+calcModulePath()+"/target/ -name \"*.jar\"` $" + "{targetDir}");
                }
                if (!stopShellChanged && isMainClass($('#restartShell').val())) {
                    $('#stopShell').val('');
                }
            } else if (moduleType == <%=ModuleType.STATIC.getValue()%>) {
                $('#restartShell').val("");
                $('#compileShell').val("cp -rf "+calcModulePath()+" $" + "{targetDir}");
                $('#stopShell').val('');
            }
        }

        showJvmArgs();
    }

    function calcModulePath() {
      var path = "$" + "{<%= ModuleUserShellArgs.compileDir%>}";
      var moduleName = $('#moduleName').val();
      if(moduleName && moduleName!=''){
        moduleName="/"+moduleName;
      }
      path += moduleName;
      return path;
    }

    function isMainClass(restartShell) {
        var mainClassPattern = /^[A-Za-z0-9_$\.]+$/; // /([A-Za-z0-9_$]{1,40}\.?)+/;
        return (restartShell && restartShell.match(mainClassPattern));
    }

    function getModuleBasicInfo(projectId) {
        $.get("/admin/project/moduleBaseInfo", {'projectId': projectId}, function (json) {
            if (json.success) {
                var moduleDetail = json.object;
                var project = moduleDetail.project;
                if (project) {
                    $('#projectId').val(projectId);
                    $('#projectName').text(project.projectName);
                    $('#projectManagerName').text(project.managerName);
                }
                var serverGroups = moduleDetail.serverGroups;
                globalEnvs = moduleDetail.envs;
                initGroup(serverGroups);
                initEnv(moduleDetail.envs);
                initJvmArgs(moduleDetail);
                initRepoAuth(moduleDetail);
            } else {
                BootstrapDialog.alert(json.message);
            }
        });
    }

    function initJvmArgs(moduleInfo) {
        var jvmHtml = template('jvmTpl', moduleInfo);
        $("#jvmArgsContent").html(jvmHtml);
        if (isMainClass(moduleInfo.restartShell)) {
            $('#jvmArgBox').show();
        }
    }

    function initRepoAuth(moduleDetail,repoAuthId) {
      var auths = moduleDetail.auths;
      var name = $('#repoAuth');
      name.empty();
      var data = [];

      if (auths.length > 0) {
        for (var i = 0; i < auths.length; i++) {
          var item = auths[i];
          data.push({
            id: item.authId,
            text: item.authName
          });
        }
      }

      name.select2({
        'allowClear' : true,
        'data': data,
        'placeholder': '请选择一个认证信息'
      });

      loadRepoAuth(repoAuthId);

    }

    // 读取当前要编辑的帐号信息
    function loadRepoAuth(authId) {
      if (authId && authId > 0) {
         $('#repoAuth').select2('val', authId);
      }
    }

    function initEnv(envs) {
        for (var i in envs) {
            $("#envs").append("<label style='margin-right:5px;'><input type='radio' name='groupEnv'  value='" + envs[i].envId + "'/>" + envs[i].envName + "</label>");
        }
    }

    function initResinConf(projectModule) {
        if (projectModule.resinConf) {
            var resinConf = projectModule.resinConf;
            $('#domain').val(resinConf.domain);
            $('#aliasDomain').val(resinConf.aliasDomain);
            if (resinConf.httpPort > 0) {
                $('#httpPort').val("" + resinConf.httpPort);
            }
            if (resinConf.serverPort > 0) {
                $('#serverPort').val("" + resinConf.serverPort);
            }
            if (resinConf.watchdogPort > 0) {
                $('#watchdogPort').val("" + resinConf.watchdogPort);
            }
            if (resinConf.threadMax > 0) {
                $('#threadMax').val("" + resinConf.threadMax);
            }
            if (resinConf.keepaliveMax > 0) {
                $('#keepaliveMax').val("" + resinConf.keepaliveMax);
            }
            if (resinConf.keepaliveTimeout > 0) {
                $('#keepaliveTimeout').val("" + resinConf.keepaliveTimeout);
            }
            if (resinConf.socketTimeout > 0) {
                $('#socketTimeout').val("" + resinConf.socketTimeout);
            }
            if (resinConf.createEveryTime) {
                $('#createEveryTime').prop('checked', true);
            }
        }
    }

    function initGroup(serverGroups) {
        if (serverGroups) {
            var groupsHtml = "", groupContentHtml = "";
            for (var i in serverGroups) {
                var group = serverGroups[i];
                var contentId = "serverGroup" + group.groupId;
                //初始化服务器
                var serverHtml = "";
                for (var j in group.servers) {
                    var serverId = "oldServer_" + group.servers[j].serverId;
                    var ipShellArgs = group.servers[j].ipShellArgs?group.servers[j].ipShellArgs:"";
                    if(ipShellArgs && ipShellArgs.length > 20){
                       ipShellArgs = ipShellArgs.substring(0, 19) + "...";
                    }
                    serverHtml = serverHtml + "<div style='margin-top: 10px;' class='row oldServer' data-serverId='" + group.servers[j].serverId +
                        "' data-serverName='" + group.servers[j].serverName +
                        "' data-groupId='" + group.servers[j].groupId +
                        "' data-serverIp='" + group.servers[j].ip +
                        "' data-ipShellArgs='" + ipShellArgs +
                        "' id='" + serverId + "'>" +
                        "<div class='col-xs-4'>" + group.servers[j].serverName + "(" + group.servers[j].ip + ")</div>" +
                        "<div class='col-xs-4'>参数:" + ipShellArgs + "</div>" +
                        "<div class='col-xs-2'><a  onclick='javascript:deleteServer(" + "\"" + serverId + "\"" + ");return false;' href='#' >删除</a></div>" +
                        "<div class='col-xs-2'><a onclick='javascript:editServer(" + "\"" + serverId + "\"" + ");return false;' href='#' >编辑</a></div>" +
                        "</div>";
                }

                var envName = getEnvName(group.envId);
                if (i == 0) {
                    groupsHtml = groupsHtml + "<li style='width: 120px; height: 59px;' id='group_" + group.groupId + "' data-groupId='" + group.groupId + "'data-groupname='" + group.groupName + "' data-groupenv='" + group.envId +
                        "'class='active'><a style='text-align: center;' href='#" + contentId + "' data-toggle='tab'><b class='groupEnvName'>" + group.groupName +
                        "</b><br/><span class='groupEnvChName' style='font-size: 9px;'>" + envName + "</span><br/>" +
                        "<span style='float: left;text-align: left;font-size: 9px;cursor: pointer;' onclick='editGroup(" + group.groupId + ");return false;' ><i class='fa fa-fw fa-edit'></i></span>" +
                        "<span style='float: right;text-align: left;font-size: 9px;cursor: pointer;' onclick='removeGroup(" + group.groupId + ");return false;' > <i class='fa fa-fw fa-remove'></i></span>" +
                        "</a>" +
                        "</a></li>"
                    groupContentHtml = groupContentHtml + "<div class='tab-pane fade in active' id='" + contentId + "'>" + serverHtml +
                        "<div class='row'  style='margin-top: 30px; margin-left: 10px;'>" +
                        "<button class='btn btn-primary' type='button' onclick='openAddServerModel();'>增加服务器</button>" +
                        "</div></div>";
                } else {
                    groupsHtml = groupsHtml + "<li  style='width: 120px; height: 59px;' id='group_" + group.groupId + "' data-groupId='" + group.groupId + "'data-groupname='" + group.groupName + "' data-groupenv='" + group.envId + "'><a style='text-align: center;' href='#" + contentId + "' data-toggle='tab'><b class='groupEnvName'>" + group.groupName +
                        "</b><br/><span class='groupEnvChName' style='font-size: 9px;'>" + envName + "</span><br/>" +
                        "<span style='float: left;text-align: left;font-size: 9px;cursor: pointer;' onclick='editGroup(" + group.groupId + ");return false;' ><i class='fa fa-fw fa-edit'></i></span>" +
                        "<span style='float: right;text-align: left;font-size: 9px;cursor: pointer;' onclick='removeGroup(" + group.groupId + ");return false;'> <i class='fa fa-fw fa-remove'></i></span>" +
                        "</a></a></li>"
                    groupContentHtml = groupContentHtml + "<div class='tab-pane fade' id='" + contentId + "'>" + serverHtml + "<div class='row' style='margin-top: 30px; margin-left: 10px;'>" +
                        "<button class='btn btn-primary' type='button' onclick='openAddServerModel();'>增加服务器</button>" +
                        "</div></div>";
                }
            }
            groupsHtml = groupsHtml + " <button class='btn'  id='addGroupTab' onclick='addNewGroup();'>添加组</button> "
            $("#serverGroups").html(groupsHtml);
            $("#groupContent").html(groupContentHtml);

        } else {
            $("#serverGroups").html(" <button class='btn'   id='addGroupTab' onclick='addNewGroup();'>添加组</button> ");
        }
    }

    function getEnvName(envId) {
        var envName = "";
        for (var i in globalEnvs) {
            if (globalEnvs[i].envId == envId) {
                envName = globalEnvs[i].envName;
            }
        }
        return envName;
    }

    function getModuleInfo(moduleId) {
        $.get("/admin/project/getModule", {'moduleId': moduleId}, function (json) {
            if (json.success) {
                var moduleDetail = json.object;
                var project = moduleDetail.project;
                if (project) {
                    $('#projectId').val(project.projectId);
                    $('#projectName').text(project.projectName);
                    $('#projectManagerName').text(project.managerName);
                    $('#viewProjectLink').prop('href', '/admin/project/viewProject.xhtml?projectId=' + project.projectId);
                }

                var projectModule = moduleDetail.projectModule;
                globalEnvs = moduleDetail.envs;
                if (projectModule) {
                    $('#moduleId').val(projectModule.moduleId);
                    $('#moduleNameZh').val(projectModule.moduleNameZh);
                    $('#moduleName').val(projectModule.moduleName);
                    $("input[name='moduleType'][value='" + projectModule.moduleType + "']").prop("checked", true);
                    $('#repoUrl').val(projectModule.repoUrl);
                    $('#srcPath').val(projectModule.srcPath);
                    $('#deployArgs').val(projectModule.deployArgs);
                    $('#preDeploy').val(projectModule.preDeploy);
                    $('#postDeploy').val(projectModule.postDeploy);
                    $('#preShell').val(projectModule.preShell);
                    $('#restartShell').val(projectModule.restartShell);
                    $('#deployArgs').val(projectModule.deployArgs);
                    $('#postShell').val(projectModule.postShell);
                    $('#compileShell').val(projectModule.compileShell);
                    $('#stopShell').val(projectModule.stopShell);
                    $('#repoType').val(projectModule.repoType);

                    if (projectModule.needAudit == <%=Constants.TRUE%>) {
                        $('#needAudit').prop("checked", 'checked');
                    }

                    initGroup(projectModule.serverGroups);
                    initJvmArgs(projectModule);
                    initResinConf(projectModule);
                }
                initEnv(moduleDetail.envs);
                initRepoAuth(moduleDetail, projectModule.repoAuthId)
            } else {
                BootstrapDialog.alert(json.message);
            }
        });
    }

    //提交
    function saveModule() {
        var validateResult = validateForm();
        if (validateResult != '') {
            BootstrapDialog.alert(validateResult);
            return;
        }
        var postData = buildPostData();
        var occupyInfo = checkResinPort(postData);
        if (occupyInfo != '') {
            BootstrapDialog.alert(occupyInfo);
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/admin/project/saveModule.do',
            data: JSON.stringify(postData),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (data.success) {
                    BootstrapDialog.alert("保存成功", function () {
                        window.location.href = "/admin/project/viewProject.xhtml?projectId=" + $("#projectId").val();
                    });
                } else {
                    BootstrapDialog.alert(data.message);
                }
            },
            error: function (data) {
                BootstrapDialog.alert("保存失败：" + data);
            }

        });

    }

    function buildModuleBaseInfo(projectModule) {
        projectModule.moduleId = $('#moduleId').val();
        projectModule.projectId = $('#projectId').val();
        projectModule.moduleNameZh = $('#moduleNameZh').val().trim();
        projectModule.moduleName = $('#moduleName').val().trim();
        projectModule.moduleType = $('input:radio[name="moduleType"]:checked').val();
        projectModule.repoUrl = $('#repoUrl').val().trim();
        projectModule.srcPath = $('#srcPath').val();
        projectModule.repoType = $('#repoType').val();

        projectModule.deployArgs = $('#deployArgs').val();
        projectModule.preDeploy = $('#preDeploy').val();
        projectModule.postDeploy = $('#postDeploy').val();
        projectModule.preShell = $('#preShell').val();
        projectModule.postShell = $('#postShell').val();
        projectModule.compileShell = $('#compileShell').val();
        projectModule.stopShell = $('#stopShell').val();
        projectModule.restartShell = $('#restartShell').val();

        projectModule.needAudit = $('#needAudit').is(':checked') ? 1 : 0;
        projectModule.repoAuthId = $('#repoAuth').val();
    }
    function buildPostData() {
        var projectModule = {};
        buildModuleBaseInfo(projectModule);
        buildServerGroupData(projectModule);
        buildModuleJvmData(projectModule);
        buildResinConfData(projectModule);
        return projectModule;
    }

    function needJvmArgs() {
        return true;
    }

    function buildModuleJvmData(projectModule) {
        var moduleJvms = [];
        var moduleId = $("#moduleId").val();

        if (needJvmArgs()) {
            $("#jvmArgsContent").children(".form-group").each(function (index, e) {
                var moduleJvm = {};
                moduleJvm.moduleId = moduleId;
                moduleJvm.envId = $(this).attr("data-envId");
                moduleJvm.envName = $(this).attr("data-envName");
                moduleJvm.moduleJvmId = $(this).attr("data-moduleJvmId");
                moduleJvm.jvmArgs = $(this).find(".jvmArgs").first().val();
                moduleJvms.push(moduleJvm);
            });
        }
        projectModule.moduleJvms = moduleJvms;
    }

    function buildServerGroupData(module) {
        var groups = new Array();
        $("#serverGroups").children("li").each(function (index, e) {
            var group = {};
            group.envId = $(this).attr("data-groupenv");
            group.groupName = $(this).attr("data-groupname");
            if ($("#moduleId").val()) {
                group.moduleId = $("#moduleId").val();
            }
            if ($(this).attr("data-groupId")) {
                group.groupId = $(this).attr("data-groupId");
            }

            var targetId = $(this).children("a").attr("href");
            var serverId = targetId.substring(1, targetId.length);
            var servers = new Array();
            $("#" + serverId).children(".newServer").each(function () {
                var server = {};
                server.serverName = $(this).attr("data-servername");
                server.ip = $(this).attr("data-serverip");
                server.ipShellArgs=$(this).attr("data-ipShellArgs");
                servers.push(server);
            });
            $("#" + serverId).children(".oldServer").each(function () {
                var server = {};
                server.serverId = $(this).attr("data-serverid");
                server.groupId = $(this).attr("data-groupId");
                server.serverName = $(this).attr("data-servername");
                server.ip = $(this).attr("data-serverip");
                server.ipShellArgs=$(this).attr("data-ipShellArgs");
                servers.push(server);
            });
            group.servers = servers;
            groups.push(group);
        });
        module.serverGroups = groups;

    }

    function buildResinConfData(module) {
        var domain = $.trim($('#domain').val());
        var aliasDomain = $.trim($('#aliasDomain').val());
        var httpPort = $.trim($('#httpPort').val());
        var serverPort = $.trim($('#serverPort').val());
        var watchdogPort = $.trim($('#watchdogPort').val());
        var threadMax = $.trim($('#threadMax').val());
        var keepaliveMax = $.trim($('#keepaliveMax').val());
        var keepaliveTimeout = $.trim($('#keepaliveTimeout').val());
        var socketTimeout = $.trim($('#socketTimeout').val());
        var createEveryTime = $('#createEveryTime').prop('checked');
        var resinConf = {
            domain: domain,
            aliasDomain: aliasDomain,
            httpPort: (httpPort == '' ? '0' : httpPort),
            serverPort: (serverPort == '' ? '0' : serverPort),
            watchdogPort: (watchdogPort == '' ? '0' : watchdogPort),
            threadMax: (threadMax == '' ? '0' : threadMax),
            keepaliveMax: (keepaliveMax == '' ? '0' : keepaliveMax),
            keepaliveTimeout: (keepaliveTimeout == '' ? '0' : keepaliveTimeout),
            socketTimeout: (socketTimeout == '' ? '0' : socketTimeout),
            createEveryTime: (createEveryTime ? true : false)
        };
        module.resinConf = resinConf;
    }

    function ping(ip) {

        $.ajax({
            type: 'GET',
            url: '/admin/project/ping?ip=' + ip,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (!data.success) {
                    BootstrapDialog.alert("服务器 : " + ip + " ping不通,请检测是否正确");
                }
            }
        });
    }

    //检测resin的http端口是否被占用
    function checkResinPort(projectModule) {
        var occupyInfo = "";
        $.ajax({
            type: 'post',
            url: '/admin/project/checkResinPort',
            async: false,
            data: JSON.stringify(projectModule),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (!data.success) {
                    occupyInfo = data.message;
                }
            }
        });
        return occupyInfo;
    }
    // 校验参数
    function validateForm() {
        var message = "";

        if ($("#moduleNameZh").val() == '') {
            message = '模块名称不能为空';
        }
        if ($("#moduleName").val() == '') {
            message = '服务名不能为空';
        }
        if ($("#svnCheckoutDir").val() == '') {
            message = 'svn/git 地址不能为空';
        }
        if (!$("input[name=moduleType]:checked").val()) {
            message = '请选择模块类型';
        }
        return message;
    }


    // 服务器组创建
    function addServerGroup() {
        //在tab创建一个 将数据存储在属性
        var groupEnv = $('input:radio[name="groupEnv"]:checked').val();
        if (!groupEnv) {
            BootstrapDialog.alert('请选择环境');
            return;
        }
        var envName = $('input:radio[name="groupEnv"]:checked').parent().text();
        var groupName = $("#groupName").val();
        var currentGroupId = $('#currentGroupId').val();

        if (currentGroupId) {
            // 编辑
            $('#' + currentGroupId).attr("data-groupenv", groupEnv);
            $('#' + currentGroupId).attr("data-groupname", groupName);
            $('#' + currentGroupId).find(".groupEnvName").text(groupName);
            $('#' + currentGroupId).find(".groupEnvChName").text(envName);

            $("#groupEnv").val("");
            $("#groupName").val("");
            $("#currentGroupId").val("");
            $('input:radio[name="groupEnv"]').removeAttr("checked");

            $("#addGroup").modal('hide');
        } else {
            // 新增
            var currentTime = (new Date()).getTime();
            var groupId = "newGroup_" + currentTime;
            var newGroupTab = "<li style='width: 120px; height: 59px;' id='group_" + currentTime + "' data-groupEnv='" + groupEnv + "' data-groupName='" + groupName + "' class='newGroup'>" +
                "<a style='text-align: center;' href='#" + groupId + "'  data-toggle='tab'><b class='groupEnvName'>" + groupName + "</b><br/><span class='groupEnvChName' style='font-size: 9px;'>" + envName + "</span><br/>" +
                "<span style='float: left;text-align: left;font-size: 9px;cursor: pointer;' onclick='editGroup(" + currentTime + ");return false;' ><i class='fa fa-fw fa-edit'></i></span>" +
                "<span style='float: right;text-align: left;font-size: 9px;cursor: pointer;' onclick='removeGroup(" + currentTime + ");return false;' > <i class='fa fa-fw fa-remove'></i></span>" +
                "</a></li>";
            $("#addGroupTab").before(newGroupTab);
            $("#groupContent").append("<div class='tab-pane fade' id='" + groupId + "'>" +
                "<div class='row'  style='margin-top: 30px; margin-left: 10px;'>" +
                "<button class='btn btn-primary' type='button' onclick='openAddServerModel();'>增加服务器</button>" +
                "</div></div>");
            //清空
            $("#groupEnv").val("");
            $("#groupName").val("");
            $("#currentGroupId").val("");
            $("#addGroup").modal('hide');
            $('input:radio[name="groupEnv"]').removeAttr("checked");

            // 跳转到新增的 tab
            $('#serverGroups li:last a').tab('show');
        }

    }
    //创建服务器
    function addServer() {
        var currentServerId = $("#currentServerId").val();
        if (currentServerId) {
            var serverIP = $.trim($("#serverIP").val());
            var serverName = $.trim($("#serverName").val());
            var ipShellArgs = $.trim($("#ipShellArgs").val());
            if (serverIP && serverName) {
                $("#" + currentServerId).attr("data-serverName", serverName);
                $("#" + currentServerId).attr("data-serverIP", serverIP);
                $("#" + currentServerId).attr("data-ipShellArgs", ipShellArgs);
                $("#" + currentServerId).children("div").first().text(serverName + "(" + serverIP + ")");
                $("#addServerModel").modal('hide');
                $("#serverIP").val('');
                $("#serverName").val('');
                $("#ipShellArgs").val('');
                $("#currentServerId").val('');
            } else {
                BootstrapDialog.alert('请输入完整的IP和名称, ip: ' + serverIP + ', 名称: ' + serverName);
            }
        } else if (checkServerNameAndIP()) {
            var serverIP = $.trim($("#serverIP").val());
            var serverName = $.trim($("#serverName").val());
            var ipShellArgs = $.trim($("#ipShellArgs").val());
              if(ipShellArgs && ipShellArgs.length > 20){
                ipShellArgs = ipShellArgs.substring(0, 19) + "...";
              }
            if (serverIP && serverName) {
                var serverId = "newServer_" + (new Date()).getTime();
                var serverHtml = "<div style='margin-top: 10px;' class='row newServer' data-serverName='" + serverName + "' data-serverIp='" + serverIP + "' data-ipShellArgs='" + ipShellArgs + "' id='" + serverId + "'>" +
                    "<div class='col-xs-4'>" + serverName + "(" + serverIP + ")</div>" +
                    "<div class='col-xs-4'>参数:" + ipShellArgs + "</div>" +
                    "<div class='col-xs-2'><a  onclick='javascript:deleteServer(" + "\"" + serverId + "\"" + ");return false;' href='#' >删除</a></div>" +
                    "<div class='col-xs-2'><a onclick='javascript:editServer(" + "\"" + serverId + "\"" + ");return false;' href='#' >编辑</a></div>" +
                    "</div>";
                $("#groupContent").children(".active").children("div").first().before(serverHtml);
                $("#serverIP").val('');
                $("#serverName").val('');
                $("#ipShellArgs").val('');
            }
            $("#addServerModel").modal('hide');
        }

//        ping(serverIP);
    }

    function checkServerNameAndIP() {
        var serverIP = $.trim($("#serverIP").val());
        var serverName = $.trim($("#serverName").val());
        if ((serverIP == '' && serverName != '')
            || (serverIP != '' && serverName == '')) {
            BootstrapDialog.alert('请输入完整的IP和名称, ip: ' + serverIP + ", 名称: " + serverName);
            return false;
        }
        return true;
    }

    function deleteServer(serverId) {
        $("#" + serverId).remove();
    }

    // 弹窗
    function addNewGroup() {
        $("#addGroup").modal('show');
        $("#addGroup").find('.modal-title').first().text("添加服务器组");
        $('#groupName').val("");
        $("#currentGroupId").val("");
        $(':radio[name="groupEnv"]').removeAttr("checked");
    }

    function editGroup(groupId) {
        $("#addGroup").modal('show');
        $("#addGroup").find('.modal-title').first().text("编辑服务器组");

        var env = $('#group_' + groupId).attr("data-groupenv");
        $(':radio[name="groupEnv"][value="' + env + '"]')[0].checked = true;
        var groupName = $('#group_' + groupId).attr("data-groupName");
        $('#groupName').val(groupName);
        $("#currentGroupId").val('group_' + groupId);
    }

    function removeGroup(groupId) {
        BootstrapDialog.confirm('确定要删除服务器组？', function (result) {
            if (result) {
                $("#group_" + groupId).remove();
                $("#serverGroup" + groupId).remove();
                // 新增的服务器组
                $("#newGroup_" + groupId).remove();
            }
        });
    }

    function editServer(serverId) {
        $(".moreIp").hide();
        $("#addServerModel").modal('show');
        $("#currentServerId").val(serverId);
        $("#serverName").val($("#" + serverId).attr("data-serverName"));
        $("#serverIP").val($("#" + serverId).attr("data-serverIP"));
        $("#ipShellArgs").val($("#" + serverId).attr("data-ipShellArgs"));
    }
    function openAddServerModel() {
        $(".moreIp").show();
        $("#serverName").val("");
        $("#serverIP").val("");
        $("#ipShellArgs").val("");
        $("#addServerModel").modal('show');
    }

    var _allServers = [];
    var _valueObj = null;
    // 加载服务器列表
    function loadAllServers(valueObj) {
        $.getJSON("/admin/project/getAllServers", {}, function (json) {
            if (json.success) {
                _allServers = json.object;
                filterServer(valueObj);
            } // end if
        });
    }

    function filterServer(valueObj) {
//        console.log('filterServer, value: ' + $(valueObj).val());
        if (_allServers && _allServers.length) {
            var value = $.trim($(valueObj).val());
            var filteredServers = [];
            for (var i = 0; i < _allServers.length; i++) {
                var server = _allServers[i];
                var match = true;
                if (value) {
                    match = (server.innerIpAddress.indexOf(value) >= 0) || (server.instanceName.indexOf(value) >= 0);
                }
                if (match) {
                    filteredServers.push(server);
                }
            }

            var data = {'data': filteredServers};
            var tmpHtml = template('chooseServerTpl', data);
            $('#serverListDivContent').html(tmpHtml);

            var offset = $(valueObj).offset();
            var top = offset.top;
            var left = offset.left;
            var height = $(valueObj).height() + 11;
            $('#serverListDiv').css({"position": "absolute", left: left, top: top + height})
                .show();

            _valueObj = valueObj;
        } // end if
    }

    function hideServerList(event) {
        var x = pageX;
        var y = pageY;
        var offset = $('#serverListDiv').offset();
        var x2 = offset.left + $('#serverListDiv').width();
        var y2 = offset.top + $('#serverListDiv').height();
        console.log('x: ' + x + ", y: " + y + ', left: ' + offset.left + ', top: ' + offset.top + ', x2: ' + x2 + ', y2: ' + y2);
        if (x < offset.left || y < offset.top || x > x2 || y > y2) {
            $('#serverListDiv').hide();
        }
    }

    // 选择服务器
    function chooseServer(instanceName, ip) {
        if (_valueObj) {
            var divObj = $(_valueObj).parent().parent();
            var inputs = $(divObj).find('input[type=text]');
            inputs[0].value = instanceName;
            inputs[1].value = ip;
            _valueObj = null;
            $('#serverListDiv').hide();
        }
    }



    $(function() {
      $('#helpDeployArgs').popover({
        content: '<span class="text-danger">properties文件变量格式</span>，比如：<br/>' +
        '#部署业务目录<br/>' +
        'deployDir=/data0/project/ideploy<br/>' +
        '#历史版本目录，回滚使用<br/>' +
        'backupDir=/data0/backup/ideploy<br/>' +
        '#发布系统在业务机器运行部署脚本存放目录<br/>' +
        'shellDir=/data0/project/shell/ideploy <br/>' +
        '#指定gc日志路径，发布时备份gc日志防丢失<br/>' +
        'gcFilePath=/data0/logs/ideploy/gc.log <br/>',
        trigger: 'click',
        placement: 'bottom',
        html: 'true',
        template: '<div class="popover" role="tooltip"><div class="arrow">' +
        '</div><div class="popover-content" style="background-color:#f5f5f5;width:400px"></div></div>'
      })
    })

    $(function() {
      $('#helpCompileShell').popover({
        content: '参考如下，<span class="text-danger">每行一条指令</span>：<br/>' +
        'mvn -P=&#36;{env} -Dmaven.test.skip=true -U clean install<br/>' +
        'cp -f &#36;{compileDir}/pay-impl/target/*.<b>jar</b> &#36;{targetDir}<br/>' +
        '或cp -f &#36;{compileDir}/pay-impl/target/*.<b>war</b> &#36;{targetDir}',
        trigger: 'click',
        placement: 'bottom',
        html: 'true',
        template: '<div class="popover" role="tooltip"><div class="arrow">' +
        '</div><div class="popover-content" style="background-color:#f5f5f5;width:400px"></div></div>'
      })
    })

    $(function() {
      $('#helpPreShell').popover({
        content: '启动完前执行的脚本，可以不填写',
        trigger: 'click',
        placement: 'bottom',
        html: 'true',
        template: '<div class="popover" role="tooltip"><div class="arrow">' +
        '</div><div class="popover-content" style="background-color:#f5f5f5;width:400px"></div></div>'
      })
    })

    $(function() {
      $('#helpRestartShell').popover({
        content: '支持多种启动方式：<br/>' +
        '1.脚本启动：/usr/local/resinpro/bin/resin.sh restart<br/>' +
        '2.Main类启动：com.alibaba.dubbo.container.Main<br/>' +
        '3.Jar启动：pay-order-impl.jar',
        trigger: 'click',
        placement: 'bottom',
        html: 'true',
        template: '<div class="popover" role="tooltip"><div class="arrow">' +
        '</div><div class="popover-content" style="background-color:#f5f5f5;width:400px"></div></div>'
      })
    })

    $(function() {
      $('#helpPostShell').popover({
        content: '启动完成后执行的脚本，可以不填写',
        trigger: 'click',
        placement: 'bottom',
        html: 'true',
        template: '<div class="popover" role="tooltip"><div class="arrow">' +
        '</div><div class="popover-content" style="background-color:#f5f5f5;width:400px"></div></div>'
      })
    })

    $(function() {
      $('#helpStopShell').popover({
        content: '比如:<br/>' +
        '/usr/local/resinpro/bin/resin.sh stop<br/>' +
        'Main类启动 或 Jar启动 都有stop的脚本，可以不填写',
        trigger: 'click',
        placement: 'bottom',
        html: 'true',
        template: '<div class="popover" role="tooltip"><div class="arrow">' +
        '</div><div class="popover-content" style="background-color:#f5f5f5;width:400px"></div></div>'
      })
    })

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
