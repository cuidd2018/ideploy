package io.ideploy.deployment.transfer.vo;

import io.ideploy.deployment.common.enums.ModuleType;

import java.util.List;
import java.util.Map;

/**
 * 详情 : 传输请求
 *
 * @author K-Priest
 */
public class TransferRequest {

    /**
     * oss 要下载的文件 key
     */
    private String saveFileName;

    /***
     * 保存编译结果文件ip地址，去编译服务器下载
     */
    private String compileFileHost;

    /**
     * 目标部署服务器ip
     */
    private List<String> targetServerIps;

    /***
     * 各个ip自定义的参数，解决不同服务器作用，如 ip1只运行定时任务，不对外服务，ip2对外服务，通过shell参数控制
     */
    private Map<String,String> ipShellArgsMap;

    /**
     * 发布历史的id
     */
    private int historyId;

    /**
     * 回滚版本的发布历史id
     */
    private int rollBackDeployId;

    /**
     * 模块名称
     */
    private String moduleName;

    private String appName;


    /**
     * 环境
     */
    private String env;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 部署前执行的脚本
     */
    private String preDeploy;

    /**
     * 部署后执行的脚本
     */
    private String postDeploy;

    /**
     * 启动前执行的脚本
     */
    private String preStartShell;

    /**
     * 部署启动执行的脚本 (当为独立进程的项目时 为启动的Main类路径)
     */
    private String restartShell;

    /**
     * 停止服务的脚本
     */
    private String stopShell;

    /**
     * 启动后执行的脚本
     */
    private String postStartShell;

    /**
     * module自定义参数
     */
    private String deployArgs;

    /**
     * JAVA_OPT 参数,用于设置jvm参数 (java 独立进程的项目特有)
     */
    private String jvmArgs;

    /**
     * 模块类型，1代表web项目 2代表dubbo服务
     */
    private short moduleType = ModuleType.WEB_PROJECT.getValue();

    /**
     * 模块最终编译的名字，只针对 MAVEN 项目，moduleType = ModuleType.WEB_PROJECT 时有效
     */
    private String moduleFinalName;

    /**
     * Resin 配置，moduleType = ModuleType.WEB_PROJECT 时有效
     */
    private ResinConf resinConf;

    /**
     * 是否重新生成 resin 配置
     */
    private boolean createResinConf;

    /**
     * 编程语言
     */
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public short getModuleType() {
        return moduleType;
    }

    public void setModuleType(short moduleType) {
        this.moduleType = moduleType;
    }

    public String getJvmArgs() {
        return jvmArgs;
    }

    public void setJvmArgs(String jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    public String getPreStartShell() {
        return preStartShell;
    }

    public void setPreStartShell(String preStartShell) {
        this.preStartShell = preStartShell;
    }

    public String getPostStartShell() {
        return postStartShell;
    }

    public void setPostStartShell(String postStartShell) {
        this.postStartShell = postStartShell;
    }

    public String getRestartShell() {
        return restartShell;
    }

    public void setRestartShell(String restartShell) {
        this.restartShell = restartShell;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public String getStopShell() {
        return stopShell;
    }

    public void setStopShell(String stopShell) {
        this.stopShell = stopShell;
    }

    public List<String> getTargetServerIps() {
        return targetServerIps;
    }

    public void setTargetServerIps(List<String> targetServerIps) {
        this.targetServerIps = targetServerIps;
    }

    public Map<String, String> getIpShellArgsMap() {
        return ipShellArgsMap;
    }

    public void setIpShellArgsMap(Map<String, String> ipShellArgsMap) {
        this.ipShellArgsMap = ipShellArgsMap;
    }

    public String getModuleFinalName() {
        return moduleFinalName;
    }

    public void setModuleFinalName(String moduleFinalName) {
        this.moduleFinalName = moduleFinalName;
    }

    public ResinConf getResinConf() {
        return resinConf;
    }

    public void setResinConf(ResinConf resinConf) {
        this.resinConf = resinConf;
    }

    public boolean isCreateResinConf() {
        return createResinConf;
    }

    public void setCreateResinConf(boolean createResinConf) {
        this.createResinConf = createResinConf;
    }

    public int getRollBackDeployId() {
        return rollBackDeployId;
    }

    public void setRollBackDeployId(int rollBackDeployId) {
        this.rollBackDeployId = rollBackDeployId;
    }

    public String getDeployArgs() {
        return deployArgs;
    }

    public void setDeployArgs(String deployArgs) {
        this.deployArgs = deployArgs;
    }

    public String getCompileFileHost() {
        return compileFileHost;
    }

    public void setCompileFileHost(String compileFileHost) {
        this.compileFileHost = compileFileHost;
    }

    public String getPreDeploy() {
        return preDeploy;
    }

    public void setPreDeploy(String preDeploy) {
        this.preDeploy = preDeploy;
    }

    public String getPostDeploy() {
        return postDeploy;
    }

    public void setPostDeploy(String postDeploy) {
        this.postDeploy = postDeploy;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
