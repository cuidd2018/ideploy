package io.ideploy.deployment.transfer.conf;

import io.ideploy.deployment.cfg.AppConfigFileUtil;
import io.ideploy.deployment.cfg.ModuleConfig;
import io.ideploy.deployment.common.util.ModuleUtil;
import io.ideploy.deployment.transfer.vo.TransferRequest;

/**
 * @author: code4china
 * @description:
 * @date: Created in 17:36 2018/9/17
 */
public class TransferConfig extends ModuleConfig{

    private TransferRequest request;

    public TransferConfig(TransferRequest request){
        this.request = request;
    }

    public String getDeployDir(){
        if(moduleDefines.containsKey(KEY_DEPLOY_DIR)){
            return moduleDefines.get(KEY_DEPLOY_DIR);
        }
        return AppConfigFileUtil.getServerFileDir() + request.getProjectName() + "/" + ModuleUtil.getShortModuleName(request.getModuleName());
    }

    public String getAccessLogDir(){
        return AppConfigFileUtil.getAccessLogDir() + request.getProjectName() + "/" + request.getModuleName() + "/";
    }

    public String getGCFilePath(){
        if(moduleDefines.containsKey(KEY_GC_FILE)){
            return moduleDefines.get(KEY_GC_FILE);
        }
        return AppConfigFileUtil.getServerBackupFileDir() + request.getProjectName() + "/" + ModuleUtil.getShortModuleName(request.getModuleName()) + "/gc.log";
    }

    public String getBackUpDir(){
        if(moduleDefines.containsKey(KEY_BACKUP_DIR)){
            return moduleDefines.get(KEY_BACKUP_DIR);
        }
        return AppConfigFileUtil.getServerBackupFileDir() + request.getProjectName() + "/" + ModuleUtil.getShortModuleName(request.getModuleName()) + "/";
    }

    public String getServerShellDir(){
        if(moduleDefines.containsKey(KEY_SHELL_DIR)){
            return moduleDefines.get(KEY_SHELL_DIR);
        }
        return AppConfigFileUtil.getServerShellDir()  + request.getProjectName() + "/" + ModuleUtil.getShortModuleName(request.getModuleName()) + "/";
    }

    public String getServerUploadDir(){
        if(moduleDefines.containsKey(KEY_DEPLOY_DIR)){
            return moduleDefines.get(KEY_DEPLOY_DIR)+"/../";
        }
        return AppConfigFileUtil.getServerFileDir() + request.getProjectName();
    }

}
