package io.ideploy.deployment.compile.config;

import io.ideploy.deployment.cfg.AppConfigFileUtil;
import io.ideploy.deployment.cfg.ModuleConfig;

/**
 * @author: code4china
 * @description:
 * @date: Created in 17:54 2018/9/17
 */
public class CompileConfig extends ModuleConfig{

    public String getCompileServer(){
        if(AppConfigFileUtil.isCompileServerLocal()){
            return "127.0.0.1,";
        }
        else{
            return AppConfigFileUtil.getCompileHostFile();
        }
    }

}
