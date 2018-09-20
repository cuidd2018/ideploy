package io.ideploy.deployment.constant.tpl;

import io.ideploy.deployment.cfg.ModuleConfig;

/**
 * @author: code4china
 * @description:
 * @date: Created in 18:42 2018/9/17
 */
public interface ModuleVarArgs {

    String deployDir = "\\$\\{" + ModuleConfig.KEY_DEPLOY_DIR+ "\\}";

    String backupDir = "\\$\\{" + ModuleConfig.KEY_BACKUP_DIR + "\\}";

    String gcFilePath = "\\$\\{" + ModuleConfig.KEY_GC_FILE + "\\}";

    String shellDir = "\\$\\{" + ModuleConfig.KEY_SHELL_DIR + "\\}";

}
