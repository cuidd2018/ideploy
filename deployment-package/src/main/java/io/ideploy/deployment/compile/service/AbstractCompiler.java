package io.ideploy.deployment.compile.service;

import com.google.common.collect.Lists;
import io.ideploy.deployment.base.ApiCode;
import io.ideploy.deployment.cfg.Configuration;
import io.ideploy.deployment.cmd.AnsibleCommandResult;
import io.ideploy.deployment.cmd.CommandUtil;
import io.ideploy.deployment.common.enums.DeployResult;
import io.ideploy.deployment.common.util.FileCompressUtil;
import io.ideploy.deployment.common.util.ModuleUtil;
import io.ideploy.deployment.compile.vo.CompileRequest;
import io.ideploy.deployment.compile.vo.CompileResult;
import io.ideploy.deployment.compile.vo.CompileShellTemplate;
import io.ideploy.deployment.exception.ServiceException;
import io.ideploy.deployment.log.PushLogService;
import io.ideploy.deployment.storage.FileStorageUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 详情 : 编译处理的
 * <p>
 * 详细 :
 *
 * @author K-Priest 17/5/9
 */
public abstract class AbstractCompiler {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractCompiler.class);


    protected static final String LOG_SCRIPT_NAME = "monitor_shell_log_upload.py";

    protected static final String OSS_SCRIPT_NAME = "oss_upload.py";


    protected CompileRequest request;

    protected CompileResult result = new CompileResult();

    protected PushLogService pushLogService;

    protected int historyId;

    /**
     * 缩短后的模块名，比如 services/xxx-impl 变成 xxx-impl
     */
    protected String shortModuleName;

    /**
     * oss file key
     */
    protected String saveFileName;

    public AbstractCompiler(CompileRequest compileRequest, PushLogService pushLogService) {
        Assert.notNull(compileRequest);
        Assert.isTrue(compileRequest.getProjectId() > 0, "projectId没有设置");
        Assert.isTrue(compileRequest.getModuleId() > 0, "mobuleId没有设置");
        Assert.notNull(pushLogService, "pushLogService没有设置");
        Assert.hasText(compileRequest.getModuleName(), "moduleName 没有设置");
        Assert.hasText(compileRequest.getEnv(), "env 没有设置");
        Assert.hasText(compileRequest.getTagName(), "tagname 没有设置");
        Assert.hasText(compileRequest.getSvnUserName(), "svn/git帐号 没有设置");
        Assert.hasText(compileRequest.getSvnPassword(), "svn/git密码 没有设置");
        Assert.hasText(compileRequest.getSvnAddr(), "svn地址 没有设置");
        Assert.hasText(compileRequest.getVersion(), "版本号没有设置");

        this.request = compileRequest;
        this.historyId = this.request.getHistoryId();
        this.pushLogService = pushLogService;
        this.shortModuleName = ModuleUtil.getShortModuleName(request.getModuleName());
        this.saveFileName = FileStorageUtil.getSaveFileName(request.getProjectId(), request.getModuleId(),
                request.getTagName(), request.getEnv(), request.getVersion());
    }

    /**
     * 执行编译打包的操作
     */
    public CompileResult compileModule() {

        beforeCompile();

        doCompile();

        afterCompile();

        return result;
    }

    protected void beforeCompile() {
        writeStep("开始编译打包");

        preparedDir();

        transferScriptToServer();
    }

    protected abstract void doCompile();

    protected void afterCompile() {
        if (!result.isCompileSuccess()) {
            return;
        }
    }

    /**
     * 在服务器上建立相应的目录
     */
    protected void preparedDir() {
        writeStep("创建相关目录");

        String allScriptDir = getScriptServerDir();

        List<String> needBuildDirs = Lists.newArrayList("mkdir -p " + allScriptDir);

        String[] args = {"-i", Configuration.getAnsibleHostFile(), "all", "-m", "shell", "-a", StringUtils.join(needBuildDirs, " && ")};

        AnsibleCommandResult commandResult = CommandUtil.execAnsible(args);
        if(commandResult != null && !commandResult.isSuccess()){
            writeStep("服务器创建目录异常，message: "+ commandResult.getErrorMessage());
            throw new ServiceException(ApiCode.FAILURE, "ansible创建服务器目录失败");
        }
    }

    protected void transferScriptToServer() {
        if (!result.isCompileSuccess()) {
            return;
        }
        long beginTime = System.currentTimeMillis();

        writeStep("传输编译相关脚本到服务器");
        //1.日志收集脚本
        String logFilePath = JavaCompiler.class.getResource("/").getPath() + "shell/" + LOG_SCRIPT_NAME;
        //2.编译脚本
        String compileShellFilePath = generateCompileShell();
        //3.oss脚本
        String ossScriptFilePath = JavaCompiler.class.getResource("/").getPath() + "shell/" + OSS_SCRIPT_NAME;

        ArrayList<String> allScriptFilePath = Lists.newArrayList(logFilePath, compileShellFilePath, ossScriptFilePath);
        String tarFilePath = FileUtils.getTempDirectoryPath() + "/" + request.getEnv() + "_" + shortModuleName + ".tar";

        boolean tarResult = FileCompressUtil.archive(allScriptFilePath, tarFilePath);
        result.setCompileSuccess(tarResult);

        if (tarResult) {
            String[] args = {"-i", Configuration.getAnsibleHostFile(), "all", "-m", "unarchive", "-a", "src=" + tarFilePath + " dest=" + getScriptServerDir() + " mode=755"};
            CommandUtil.execAnsible(args);
        } else {
            writeStep("传输编译相关脚本到服务器失败");
        }

        logger.info("传输解压耗时 :" + String.valueOf(System.currentTimeMillis() - beginTime));
        FileUtils.deleteQuietly(new File(tarFilePath));
        FileUtils.deleteQuietly(new File(compileShellFilePath));
    }

    protected String generateCompileShell() {
        if (!result.isCompileSuccess()) {
            return "";
        }
        logger.info("开始生成编译脚本文件");

        CompileShellTemplate compileTemplateCreator = new CompileShellTemplate(shortModuleName, request, LOG_SCRIPT_NAME);
        String compileShellFile = null;
        try {
            compileShellFile = compileTemplateCreator.generate();
            writeStep("生成执行编译的shell成功");
        } catch (IOException e) {
            result.setCompileSuccess(false);
            writeStep("生成执行编译的shell失败: " + e.getMessage());
        }

        return compileShellFile;
    }

    protected void writeStep(String message) {
        pushLogService.writeStep(historyId, message);
    }


    protected String getScriptServerDir() {
        return Configuration.getCompileServerScriptDir() + request.getEnv() + "/" + request.getProjectName() + "/" + shortModuleName + "/";
    }
}
