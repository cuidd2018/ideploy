package io.ideploy.deployment.admin.websocket.context;

import io.ideploy.deployment.admin.context.AdminLoginUser;

/**
 * 功能: 保存用户信息的session
 * <p>
 * 详细:
 *
 * @author jingyesi   17/2/3
 */
public class UserIdentitySessionHolder extends BaseSessionHolder<AdminLoginUser>  {

    private static UserIdentitySessionHolder holder = new UserIdentitySessionHolder();

    private UserIdentitySessionHolder(){

    }

    public static UserIdentitySessionHolder getInstance(){
        return holder;
    }
}
