package io.ideploy.deployment.constant.tpl;

import io.ideploy.deployment.cfg.ModuleConfig;

/**
 * @author: code4china
 * @description:
 * @date: Created in 18:42 2018/9/17
 */
public class ModuleVarArgs {

    public static String deployDir = "\\$\\{" + ModuleConfig.KEY_DEPLOY_DIR+ "\\}";

    public static String backupDir = "\\$\\{" + ModuleConfig.KEY_BACKUP_DIR + "\\}";

}
