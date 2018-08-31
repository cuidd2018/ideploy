package io.ideploy.deployment.cfg;

import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能：项目配置
 * 详细：
 *
 * @author linyi, 2017/2/20.
 */
public class Configuration {

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    private static PropertiesConfiguration config;

    static {
        try {
            /** application.properties的位置可以通过环境变量传递 **/
            String configPath= System.getProperty("spring.config.location");
            if(StringUtils.isNotBlank(configPath)){
                File tmpConfigFile= new File(configPath);
                if(tmpConfigFile.isFile() && tmpConfigFile.exists()){
                    configPath=tmpConfigFile.getAbsolutePath();
                }
                else {
                    configPath=null;
                }
            }
            if(StringUtils.isBlank(configPath)){
                configPath= "application.properties";
            }
            /** 加入配置解析 **/
            config = new PropertiesConfiguration(configPath);
        } catch (ConfigurationException e) {
            logger.error("初始化服务器配置文件失败");
        }
    }

    /**
     * 上传Shell文件到服务器的目的路径
     * @return
     */
    public static String getServerShellDir() {
        return config.getString("server.shell.dir");
    }

    /**
     * 编译打包服务器对应的ansible host配置文件路径
     * @return
     */
    public static String getAnsibleHostFile() {
        return config.getString("compile.server.ansible.host.file");
    }

    /**
     * 打包服务器checkout项目的路径
     * @return
     */
    public static String getCompileServerCheckoutDir() {
        return config.getString("compile.server.checkout.dir");
    }

    /**
     * 编译打包的工作目录
     * @return
     */
    public static String getCompilePackagedir() {
        return config.getString("compile.server.compilePackage.dir");
    }

    /**
     * 编译日志存放路径
     * @return
     */
    public static String getCompileLogDir() {
        return config.getString("compile.server.compileLog.dir");
    }

    /**
     * 打包服务器上，脚本文件所放目录
     * @return
     */
    public static String getCompileServerScriptDir() {
        return config.getString("compile.server.script.dir");
    }

    /**
     * 上报日志的接口
     * @return
     */
    public static String getCollectLogUrl() {
        return config.getString("compile.server.log.collect.url");
    }

    /***
     * 发布系统deployment-web下载编译包到本地路径
     * @return
     */
    public static String getWebDownloadDir(){
        return config.getString("web.download.dir");
    }

    /**
     * 备份上一次发布文件的目录
     * @return
     */
    public static String getServerBackupFileDir() {
        return config.getString("server.backup.file.dir");
    }

    /**
     * ansible 上传项目打包好的文件到目标服务器的路径
     * @return
     */
    public static String getServerFileDir() {
        return config.getString("server.file.dir");
    }

    /**
     * 项目配置文件所在业务服务器目录
     * @return
     */
    public static String getServerConfDir() {
        return config.getString("server.conf.dir");
    }

    /**
     * web项目运行目录
     * @return
     */
    public static String getWebRunDir() {
        return config.getString("web.run.dir");
    }

    /**
     * web容器的执行脚本
     * @return
     */
    public static String getWebContainerShell() {
        return config.getString("web.container.shell");
    }

    /**
     * web容器类型
     * @return
     */
    public static String getWebContainerType() {
        return config.getString("web.container.type");
    }

    /**
     * resin日志存放目录
     * @return
     */
    public static String getResinLogDir() {
        return config.getString("web.container.resin.log.dir");
    }

    /**
     * 业务域名的正则表达式
     * @return
     */
    public static String getDomainRegx() {
        return config.getString("domain.regx");
    }

    /**
     * 每个环境的域名
     * @param env
     * @return
     */
    public static String getDomainStyle(String env) {
        return config.getString("domain.style." + env);
    }

    /**
     * gc目录
     * @return
     */
    public static String getGcLogDir() {
        return config.getString("server.gclog.dir");
    }
    /**
     * resin access log目录
     */
    public static String getAccessLogDir() {
        return config.getString("server.accessLog.dir");
    }

    /**
     * 保存文件的路径（针对LocalFileStorage和NAS）
     * @return
     */
    public static String getCompileStorageDir() {
        return config.getString("compile.server.storage.dir");
    }

    /***
     * websocket链接地址
     * @return
     */
    public static String getWebsocketUrl(){
        return config.getString("websocket.url");
    }

    /***
     * 保存服务器shell日志记录接口
     * @return
     */
    public static String getDeploySaveShellLogDir(){
        return config.getString("deployment.save.shell.log.url");
    }
}
