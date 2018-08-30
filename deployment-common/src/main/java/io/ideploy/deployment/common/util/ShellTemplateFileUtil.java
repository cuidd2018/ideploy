package io.ideploy.deployment.common.util;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 详情 : 读取shell脚本模板文件
 * <p>
 * 详细 :
 *
 * @author K-Priest 17/2/27
 */
public class ShellTemplateFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(ShellTemplateFileUtil.class);

    /**
     * java编译模板内容
     */
    private static List<String> javaCompileShellTpl;

    /**
     * 纯静态文件编译模板内容
     */
    private static List<String> staticCompileShellTpl;

    /**
     * 静态文件在java项目里面的编译模板内容
     */
    private static List<String> staticInJavaCompileShellTpl;

    /**
     * java 发布脚本模板内容
     */
    private static List<String> javaDeployShellTpl;

    /**
     * 静态文件 发布脚本模板内容
     */
    private static List<String> staticDeployShellTpl;

    /**
     * java项目中的静态文件 发布脚本模板内容
     */
    private static List<String> staticInJavaDeployShellTpl;

    /**
     * 生成的启动模板内容
     */
    private static List<String> startupShellTpl;

    static {
        try {
            javaCompileShellTpl = loadShellTpl("module_compile_shell_tpl_java.sh");

            staticCompileShellTpl = loadShellTpl("module_compile_shell_tpl_static.sh");

            staticInJavaCompileShellTpl = loadShellTpl("module_compile_shell_tpl_staticInJava.sh");

            javaDeployShellTpl = loadShellTpl("module_deploy_shell_tpl_java.sh");

            staticDeployShellTpl = loadShellTpl("module_deploy_shell_tpl_static.sh");

            staticInJavaDeployShellTpl = loadShellTpl("module_deploy_shell_tpl_staticJava.sh");

            startupShellTpl = loadShellTpl("module_startup_shell_tpl.sh");
        } catch (Exception e) {
            logger.error("初始化模板文件失败, {}", e);
        }
    }

    private static List<String> loadShellTpl(String shellTplName){
        List<String> lines= new ArrayList<>();
        InputStream input = null;
        BufferedReader br= null;
        try{
            input= ShellTemplateFileUtil.class.getClassLoader().getResourceAsStream("/resources/shell/"+ shellTplName);
            br= new BufferedReader(new InputStreamReader(input, Charsets.UTF_8));
            String line= null;
            while ((line= br.readLine())!= null){
                lines.add(line);
            }
        }catch (Exception e){
            logger.error("loadShellTpl异常，shellTplName:{}", shellTplName, e);
            return null;
        }finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(input);
        }
        return lines;
    }

    /**
     * 获取java编译脚本模板内容
     *
     * @return 模板内容
     */
    public static String getJavaCompileShellTpl() {
        return StringUtils.join(javaCompileShellTpl, "\n");
    }

    /**
     * 获取静态文件编译脚本模板内容
     *
     * @return 模板内容
     */
    public static String getStaticCompileShellTpl() {
        return StringUtils.join(staticCompileShellTpl, "\n");
    }

    /**
     * 获取在java项目的静态文件编译脚本模板内容
     *
     * @return 模板内容
     */
    public static String getStaticInJavaCompileShellTpl() {
        return StringUtils.join(staticInJavaCompileShellTpl, "\n");
    }

    /**
     * 获取java发布脚本模板内容
     *
     * @return 模板内容
     */
    public static String getJavaDeployShellTpl() {
        return StringUtils.join(javaDeployShellTpl, "\n");
    }

    /**
     * 获取静态文建发布脚本模板内容
     *
     * @return 模板内容
     */
    public static String getStaticDeployShellTpl() {
        return StringUtils.join(staticDeployShellTpl, "\n");
    }
    /**
     * 获取java项目中静态文建发布脚本模板内容
     *
     * @return 模板内容
     */
    public static String getStaticInJavaDeployShellTpl() {
        return StringUtils.join(staticInJavaDeployShellTpl, "\n");
    }


    /**
     * 获取启动脚本模板内容
     *
     * @return 模板内容
     */
    public static String getStartupShellTpl() {
        return StringUtils.join(startupShellTpl, "\n");
    }

}
