package io.ideploy.deployment.admin.service.account;

import io.ideploy.deployment.admin.vo.account.AdminAccount;
import io.ideploy.deployment.common.ResultEntity;

/**
 * @author: code4china
 * @description: ldap操作相关
 * @date: Created in 23:32 2018/7/12
 */
public interface LdapAccountService {

    /***
     * ldapId登录，并返回用户信息
     * @param ldapId
     * @return
     */
    ResultEntity<AdminAccount> login(String ldapId, String password);

    
}
