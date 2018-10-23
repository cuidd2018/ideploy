package io.ideploy.deployment.admin.po.global;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: code4china
 * @description:
 * @date: Created in 14:37 2018/10/12
 */
public class RepoAuthRelationPO implements Serializable{

    private int id;

    private long uid;

    private int repoAuthId;

    private Date createTime;

    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getRepoAuthId() {
        return repoAuthId;
    }

    public void setRepoAuthId(int repoAuthId) {
        this.repoAuthId = repoAuthId;
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
