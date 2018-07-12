package io.ideploy.deployment.admin.vo.project;

import io.ideploy.deployment.admin.vo.account.AdminAccount;
import io.ideploy.deployment.admin.vo.global.GlobalSetting;
import io.ideploy.deployment.admin.vo.global.ProjectEnv;

import java.util.List;

/**
 * 详情 : 项目详情 VO
 *
 * @author K-Priest
 */
public class ProjectDetailInfo {

    /**
     * 项目
     */
    private Project project;

    /**
     * 所有用户
     */
    private List<AdminAccount> allAccounts;

    /**
     * 管理模块
     */
    private List<ProjectModule> modules;

    /**
     * 项目帐号关联关系
     */
    private List<ProjectAccountRelation> projectAccountRelations;

    /**
     * 全局配置
     */
    private GlobalSetting globalSetting;

    /**
     * 环境列表
     */
    private List<ProjectEnv> projectEnvs;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<AdminAccount> getAllAccounts() {
        return allAccounts;
    }

    public void setAllAccounts(List<AdminAccount> allAccounts) {
        this.allAccounts = allAccounts;
    }

    public List<ProjectModule> getModules() {
        return modules;
    }

    public void setModules(List<ProjectModule> modules) {
        this.modules = modules;
    }

    public List<ProjectAccountRelation> getProjectAccountRelations() {
        return projectAccountRelations;
    }

    public void setProjectAccountRelations(List<ProjectAccountRelation> projectAccountRelations) {
        this.projectAccountRelations = projectAccountRelations;
    }

    public GlobalSetting getGlobalSetting() {
        return globalSetting;
    }

    public List<ProjectEnv> getProjectEnvs() {
        return projectEnvs;
    }

    public void setProjectEnvs(List<ProjectEnv> projectEnvs) {
        this.projectEnvs = projectEnvs;
    }

    public void setGlobalSetting(GlobalSetting globalSetting) {
        this.globalSetting = globalSetting;
    }
}
