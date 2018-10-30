package io.ideploy.deployment.admin.po.global;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: code4china
 * @description:
 * @date: Created in 14:37 2018/10/12
 */
public class RoleAuthRelationPO implements Serializable{

    private int id;

    private int roleId;

    private int authId;

    private Date createTime;

    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
