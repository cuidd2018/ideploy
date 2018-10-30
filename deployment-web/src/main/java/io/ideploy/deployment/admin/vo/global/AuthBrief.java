package io.ideploy.deployment.admin.vo.global;

/**
 * @author: code4china
 * @description:
 * @date: Created in 10:34 2018/10/18
 */
public class AuthBrief {

    private int authId;

    private long creatorId;

    /** 认证类型 0-私有  1-角色（角色关联引进来的仓库，减少仓库权限配置） **/
    private int ownType;

    private String authName;

    private String authDesc;

    private int repoType;

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthDesc() {
        return authDesc;
    }

    public void setAuthDesc(String authDesc) {
        this.authDesc = authDesc;
    }

    public int getRepoType() {
        return repoType;
    }

    public void setRepoType(int repoType) {
        this.repoType = repoType;
    }

    public int getOwnType() {
        return ownType;
    }

    public void setOwnType(int ownType) {
        this.ownType = ownType;
    }
}
