package io.ideploy.deployment.admin.service.account.impl;

import com.alibaba.fastjson.JSON;
import io.ideploy.deployment.admin.configure.vo.LdapConfigVO;
import io.ideploy.deployment.admin.constant.DeployConstant;
import io.ideploy.deployment.admin.dao.account.AccountRoleRelationDao;
import io.ideploy.deployment.admin.dao.account.AdminAccountDao;
import io.ideploy.deployment.admin.enums.AccountType;
import io.ideploy.deployment.admin.po.account.AccountRoleRelationPO;
import io.ideploy.deployment.admin.po.account.AdminAccountPO;
import io.ideploy.deployment.admin.service.account.LdapAccountService;
import io.ideploy.deployment.admin.vo.account.AdminAccount;
import io.ideploy.deployment.base.ApiCode;
import io.ideploy.deployment.common.CallResult;
import io.ideploy.deployment.common.util.VOUtil;
import java.util.Date;
import java.util.Hashtable;
import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AccountRoleRelationDao accountRoleRelationDao;

    /***
     * ldap操作基本变量
     * @return
     */
    private Hashtable<String,String> buildBasicEnv(){
        Hashtable<String,String> env= new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,ldapConfigVO.getFactory());
        env.put(Context.PROVIDER_URL,ldapConfigVO.getUrl());
        env.put(Context.SECURITY_AUTHENTICATION,"simple");
        env.put("com.sun.jndi.ldap.connect.timeout", "1000"); //ldap连接超时，单位毫秒
        env.put("com.sun.jndi.ldap.read.timeout", "1000"); //读超时，单位毫秒
        /*env.put(Context.SECURITY_PROTOCOL, "ssl");*/
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
    public CallResult<AdminAccount> login(String ldapId, String password) {
        LdapContext ldapContext= createContext();
        if(ldapContext == null){
            logger.warn("连接ldap服务失败，ldapId:{},password.length:{}",
                    ldapId, StringUtils.length(password));
            return CallResult.make(ApiCode.FAILURE, "ldap服务异常");
        }

        LdapContext verifyContext= null;
        try{
            SearchResult searchResult= searchAccount(ldapContext, ldapId);
            if(searchResult == null){
                logger.warn("ldapId:{} 不存在", ldapId);
                return CallResult.make(ApiCode.FAILURE, "账号不存在");
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
                return CallResult.make(VOUtil.from(po, AdminAccount.class));
            }

            /*** 初始化ldap账号资料到发布系统 ***/
            po= createAccount(ldapId, searchResult);
            return CallResult.make(VOUtil.from(po, AdminAccount.class));

        }catch (Exception e){
            logger.error("登录ldap异常，message:{},ldapId:{},password.length:{}",
                    e.getMessage(), ldapId, StringUtils.length(password));
        }finally {
            /*** 释放连接 ***/
            closeContext(ldapContext);
            closeContext(verifyContext);
        }
        return CallResult.make(ApiCode.FAILURE,"ldap未知错误");
    }

    @Transactional
    private AdminAccountPO createAccount(String ldapId, SearchResult searchResult){
        AdminAccountPO po= new AdminAccountPO();
        po.setAccount(ldapId);
        po.setAccountStatus(AdminAccount.NOMAL);
        po.setAccountType(AccountType.LDAP_USER.getValue());
        po.setMobileNo("");
        po.setPassword("");
        po.setRealname(resolveAttr(searchResult, "displayname", "未知用户"));
        po.setOperator(1);
        po.setLastModify(new Date());
        po.setCreateTime(new Date());
        adminAccountDao.save(po);

        AccountRoleRelationPO relation = new AccountRoleRelationPO();
        relation.setUid(po.getUid());
        relation.setRoleId(DeployConstant.DEFAULT_ROLE_ID);
        logger.info("添加用户角色关系，uid: {}, roleId: {}", po.getUid(), DeployConstant.DEFAULT_ROLE_ID);
        accountRoleRelationDao.save(relation);
        return po;
    }

    private String resolveAttr(SearchResult searchResult, String attrName, String defaultValue){
        if(searchResult == null){
            return  defaultValue;
        }
        try {
            Attributes attributes = searchResult.getAttributes();
            Attribute attr = null;
            if (attributes != null && (attr = attributes.get(attrName)) != null) {
                String result = String.valueOf(attr.get());
                return result;
            }
        }catch (Exception e){
            logger.error("attrName:{}", attrName,  e);
        }
        return defaultValue;
    }
}
