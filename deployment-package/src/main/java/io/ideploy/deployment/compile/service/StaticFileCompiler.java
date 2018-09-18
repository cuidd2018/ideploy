package io.ideploy.deployment.compile.service;

import io.ideploy.deployment.cfg.AppConfigFileUtil;
import io.ideploy.deployment.cmd.AnsibleCommandResult;
import io.ideploy.deployment.cmd.CommandUtil;
import io.ideploy.deployment.common.enums.DeployResult;
import io.ideploy.deployment.compile.vo.CompileRequest;
import io.ideploy.deployment.log.PushLogService;

/**
 * 功能 :  静态项目的打包编译
 * <p>
 * 详细 : 步骤
 *
 * @author K-Priest
 */
public class StaticFileCompiler extends AbstractCompiler {

    public StaticFileCompiler(CompileRequest compileRequest, PushLogService pushLogService) {
        super(compileRequest, pushLogService);
    }

    @Override
    protected void doCompile() {
        executePackageShell(getScriptServerDir() + "/compile_" + shortModuleName + ".sh");
    }

    private String getMvnTargetProjectDir() {
        return AppConfigFileUtil.getCompilePackagedir() + request.getEnv() + "/" + request.getProjectName() + "/";
    }

    private void executePackageShell(String compileShellFilePath) {
        if (!result.isCompileSuccess()) {
            return;
        }
        logger.info("开始进行打包");
        long startTime = System.currentTimeMillis();
        String[] args = {"-i", compileConfig.getCompileServer(), "all", "-m", "shell", "-a", " sh " + compileShellFilePath};
        AnsibleCommandResult ansibleResult = CommandUtil.execAnsible(CommandUtil.ansibleCmdArgs(args, 1));
        logger.info("打包返回的结果是 :" + ansibleResult.getMessage());
        boolean executeCompileSuccess = (ansibleResult.getSuccessType() == DeployResult.SUCCESS);
        result.setCompileSuccess(executeCompileSuccess);
        if (!executeCompileSuccess) {
            String tmpMessage = ansibleResult.getMessage();
            if (tmpMessage != null && tmpMessage.length() > 2048) {
                tmpMessage = tmpMessage.substring(0, 1900) + " ...... " + tmpMessage.substring(tmpMessage.length() - 100);
            }
            String message = "打包失败: " + tmpMessage;
            writeStep(message);
        } else {
            String message = "打包成功，耗时 " + (System.currentTimeMillis() - startTime) + " ms";
            writeStep(message);
        }

        if (result.isCompileSuccess()) {
            // 返回最终生成的tar.gz 文件 的路径
            result.setCompiledFileName(getMvnTargetProjectDir() + shortModuleName + ".tar.gz");
        }
    }

}
