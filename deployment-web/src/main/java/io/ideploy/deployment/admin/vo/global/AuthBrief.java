package io.ideploy.deployment.admin.vo.global;

/**
 * @author: code4china
 * @description:
 * @date: Created in 10:34 2018/10/18
 */
public class AuthBrief {

    private int authId;

    private long creatorId;

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
}
