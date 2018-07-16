package io.ideploy.deployment.admin.service.account.impl;

import com.alibaba.fastjson.JSON;
import io.ideploy.deployment.admin.configure.vo.LdapConfigVO;
import io.ideploy.deployment.admin.dao.account.AdminAccountDao;
import io.ideploy.deployment.admin.enums.AccountType;
import io.ideploy.deployment.admin.po.account.AdminAccountPO;
import io.ideploy.deployment.admin.service.account.LdapAccountService;
import io.ideploy.deployment.admin.vo.account.AdminAccount;
import io.ideploy.deployment.common.util.VOUtil;
import java.util.Hashtable;
import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: code4china
 * @description:
 * @date: Created in 23:33 2018/7/12
 */
@Service
public class LdapAccountServiceImpl implements LdapAccountService{

    private static Logger logger = LoggerFactory.getLogger(LdapAccountServiceImpl.class);

    @Autowired
    private LdapConfigVO ldapConfigVO;

    @Autowired
    private AdminAccountDao adminAccountDao;

    /***
     * ldap操作基本变量
     * @return
     */
    private Hashtable<String,String> buildBasicEnv(){
        Hashtable<String,String> env= new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,ldapConfigVO.getFactory());
        env.put(Context.PROVIDER_URL,ldapConfigVO.getUrl());
        env.put(Context.SECURITY_AUTHENTICATION,"simple");
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        return env;
    }

    /***
     * 创建ldap连接上下文对象
     * @return
     */
    private LdapContext createContext(){
        Hashtable env= buildBasicEnv();
        env.put(Context.SECURITY_PRINCIPAL,ldapConfigVO.getAdmin());
        env.put(Context.SECURITY_CREDENTIALS,ldapConfigVO.getPassword());
        LdapContext ldapContext=null;
        try {
            ldapContext = new InitialLdapContext(env, null);
        }catch(Exception e){
            logger.error("init LdapAccountServiceImpl failed,env:{}", JSON.toJSONString(env),e);
        }
        return ldapContext;
    }

    /***
     * 关闭ldap连接上下文对象
     * @param ldapContext
     */
    private void closeContext(LdapContext ldapContext){
        if(ldapContext == null){
            return;
        }
        try {
            ldapContext.close();
        }catch(Exception e){
            logger.error("closeContext error",e);
        }
    }

    /***
     * 在ldap中搜索账号
     * @param ldapContext
     * @param account
     * @return
     * @throws NamingException
     */
    public SearchResult searchAccount(LdapContext ldapContext,String account) throws NamingException {
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> en = ldapContext.search(ldapConfigVO.getBaseDN(),
                "sAMAccountName="+account , constraints);

        if (en == null || !en.hasMoreElements()) {
            logger.info("account:{} 不存在",account);
            return null;
        }
        while (en != null && en.hasMoreElements()) {
            Object obj = en.nextElement();
            if (obj instanceof SearchResult) {
                SearchResult si = (SearchResult) obj;
                return si;
            }
        }
        return null;
    }


    @Override
    public AdminAccount login(String ldapId, String password) {
        LdapContext ldapContext= createContext();
        if(ldapContext == null){
            logger.warn("连接ldap服务失败，ldapId:{},password.length:{}",
                    ldapId, StringUtils.length(password));
            return null;
        }

        LdapContext verifyContext= null;
        try{
            SearchResult searchResult= searchAccount(ldapContext, ldapId);
            if(searchResult == null){
                logger.warn("ldapId:{} 不存在", ldapId);
                return null;
            }
            String userDN= searchResult.getName()+ ","+ ldapConfigVO.getBaseDN();

            /*** 使用用户名+密码去AD域登录验证 ***/
            Hashtable env= buildBasicEnv();
            env.put(Context.SECURITY_PRINCIPAL,userDN);
            env.put(Context.SECURITY_CREDENTIALS,password);
            verifyContext= new InitialLdapContext(env, null);
            logger.info("ldap登录成功，ldapId:{}", ldapId);

            /*** 第一次登录，需把用户资料同步过来 ***/
            AdminAccountPO po= adminAccountDao.getByAccount(ldapId, AccountType.LDAP_USER.getValue());
            if(po != null){
                //TODO 异步刷新用户ldap资料
                return VOUtil.from(po, AdminAccount.class);
            }


        }catch (Exception e){
            logger.error("登录ldap异常，message:{},ldapId:{},password.length:{}",
                    e.getMessage(), ldapId, StringUtils.length(password));
        }finally {
            /*** 释放连接 ***/
            closeContext(ldapContext);
            closeContext(verifyContext);
        }
        return null;
    }
}
