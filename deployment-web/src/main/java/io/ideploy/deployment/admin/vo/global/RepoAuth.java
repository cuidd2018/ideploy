package io.ideploy.deployment.admin.vo.global;

/**
 * @author: code4china
 * @description:
 * @date: Created in 13:29 2018/10/12
 */
public class RepoAuth extends AuthBrief{

    private String account;

    private  String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
