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

    public String getDeployDir(TransferRequest request){
        if(moduleDefines.containsKey(KEY_DEPLOY_DIR)){
            return moduleDefines.get(KEY_DEPLOY_DIR);
        }
        return AppConfigFileUtil.getServerFileDir() + request.getProjectName() + "/" + ModuleUtil.getShortModuleName(request.getModuleName());
    }

    public String getBackUpDir(TransferRequest request){
        if(moduleDefines.containsKey(KEY_BACKUP_DIR)){
            return moduleDefines.get(KEY_BACKUP_DIR);
        }
        return AppConfigFileUtil.getServerBackupFileDir() + request.getProjectName() + "/" + ModuleUtil.getShortModuleName(request.getModuleName()) + "/";
    }

    public String getServerUploadDir(TransferRequest request){
        if(moduleDefines.containsKey(KEY_DEPLOY_DIR)){
            return moduleDefines.get(KEY_DEPLOY_DIR)+"/../";
        }
        return AppConfigFileUtil.getServerFileDir() + request.getProjectName();
    }

}
