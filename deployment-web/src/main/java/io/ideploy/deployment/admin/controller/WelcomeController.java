package io.ideploy.deployment.admin.controller;

import io.ideploy.deployment.admin.annotation.authority.AllowAnonymous;
import io.ideploy.deployment.admin.common.RestResult;
import io.ideploy.deployment.admin.context.AdminContext;
import io.ideploy.deployment.admin.context.AdminLoginUser;
import io.ideploy.deployment.admin.context.AppConstants;
import io.ideploy.deployment.admin.enums.AccountType;
import io.ideploy.deployment.admin.service.account.AdminAccountService;
import io.ideploy.deployment.admin.service.account.LdapAccountService;
import io.ideploy.deployment.admin.utils.resource.MenuResource;
import io.ideploy.deployment.admin.vo.account.AdminAccount;
import io.ideploy.deployment.base.ApiCode;
import io.ideploy.deployment.common.CallResult;
import io.ideploy.deployment.common.util.ParameterUtil;
import io.ideploy.deployment.encrypt.ValueEncoder;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * 欢迎界面
 * @author ten 2015/10/17.
 */
@Controller
@RequestMapping("/admin/")
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    @Autowired
    private ValueEncoder valueEncoder;

    @Autowired
    AdminAccountService adminAccountService;

    @Autowired
    LdapAccountService ldapAccountService;

    /**
     * 登录界面
     */
    @RequestMapping("login")
    @MenuResource("登录界面")
    @AllowAnonymous
    public String toLogin() {
        return "login";
    }


    /**
     * 增加管理员主页，xhtml 仅用于展示页面
     * @return
     */
    @RequestMapping("welcome")
    @MenuResource("欢迎界面")
    @AllowAnonymous
    public String index() {
        return "redirect:/admin/deploy/list.xhtml";
    }
    
    /**
     * 登录接口,sso登录时回调
     * @param request
     * @param response
     * @param account
     * @param password
     * @return
     */
    @RequestMapping("/login.do")
    @MenuResource("授权登录")
    @AllowAnonymous
    @ResponseBody
    public RestResult login(HttpServletRequest request, HttpServletResponse response, String account, String password, short loginType){

        ParameterUtil.assertNotBlank(account, "账户不能为空");
        ParameterUtil.assertNotBlank(password, "密码不能为空");

        AccountType accountType= AccountType.from(loginType);
        AdminAccount adminAccount = null;
        if(accountType == AccountType.SYSTEM_USER) {
            adminAccount = adminAccountService.getByAccount(account, accountType);
        }
        else{
            CallResult<AdminAccount> callResult = ldapAccountService.login(account, password);
            if(callResult.isFailue()){
                return new RestResult(ApiCode.FAILURE, callResult.getMessage());
            }
            adminAccount= callResult.getObject();
        }

        if (adminAccount == null){
            return new RestResult(ApiCode.FAILURE, "帐号不存在");
        }

        if (accountType == AccountType.SYSTEM_USER &&
                !Objects.equals(valueEncoder.encode(password), adminAccount.getPassword())) {
            return new RestResult(ApiCode.FAILURE, "帐号密码不正确");
        }

        AdminLoginUser loginUser = new AdminLoginUser();
        loginUser.setAccountId(adminAccount.getUid());
        loginUser.setAppId(AppConstants.APP_ID_DEFAULT);

        AdminContext.saveToCookie(response, loginUser);
        logger.info("登录成功 | uid: {}", adminAccount.getUid());

        return new RestResult(ApiCode.SUCCESS, "");
    }

    /**
     * 退出登录接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/logout.do")
    @MenuResource("退出登录")
    @AllowAnonymous
    public String logout(HttpServletRequest request, HttpServletResponse response){
        AdminContext.clearCookie(response);
        AdminContext.clear();
        return "redirect:" + AppConstants.SSO_LOGIN_URL;
    }


    @RequestMapping("/error.xhtml")
    @AllowAnonymous
    public String errorMsg(HttpServletRequest request, HttpServletResponse response){
        String message = ServletRequestUtils.getStringParameter(request, "message", "");
        try {
            request.setAttribute("message", URLDecoder.decode(message, "UTF-8"));
        }catch (Exception e){
            logger.error("错误页面发生错误 | msg:{}", message, e);
        }
        return "exception";
    }

    @RequestMapping("/exception.xhtml")
    @AllowAnonymous
    public String  exceptionMsg(HttpServletRequest request, HttpServletResponse response){
        return "exception";
    }


}
