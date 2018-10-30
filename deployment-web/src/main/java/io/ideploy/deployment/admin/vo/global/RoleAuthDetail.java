package io.ideploy.deployment.admin.vo.global;

import io.ideploy.deployment.admin.vo.account.Role;
import java.util.List;

/**
 * @author: code4china
 * @description:
 * @date: Created in 11:43 2018/10/25
 */
public class RoleAuthDetail {

    private boolean adminUser;

    private List<Role> roles;

    private List<Integer> existRoleIds;

    private RepoAuth repoAuth;

    public boolean isAdminUser() {
        return adminUser;
    }

    public void setAdminUser(boolean adminUser) {
        this.adminUser = adminUser;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public RepoAuth getRepoAuth() {
        return repoAuth;
    }

    public void setRepoAuth(RepoAuth repoAuth) {
        this.repoAuth = repoAuth;
    }

    public List<Integer> getExistRoleIds() {
        return existRoleIds;
    }

    public void setExistRoleIds(List<Integer> existRoleIds) {
        this.existRoleIds = existRoleIds;
    }
}
