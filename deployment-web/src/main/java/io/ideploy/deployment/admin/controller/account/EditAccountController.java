package io.ideploy.deployment.admin.controller.account;

import io.ideploy.deployment.admin.annotation.log.AdminLog;
import io.ideploy.deployment.admin.common.RestResult;
import io.ideploy.deployment.admin.service.account.AdminAccountService;
import io.ideploy.deployment.admin.utils.resource.Menu;
import io.ideploy.deployment.admin.utils.resource.MenuResource;
import io.ideploy.deployment.admin.vo.account.AccountRoleRelation;
import io.ideploy.deployment.admin.vo.account.AdminAccount;
import io.ideploy.deployment.base.ApiCode;
import io.ideploy.deployment.encrypt.ValueEncoder;
import io.ideploy.deployment.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 新增/修改管理员帐号
 * @author linyi 2015/6/7.
 */
@Controller
@RequestMapping("admin/account")
@Menu(name="增加/修改帐号", parent = "帐号管理", sequence = 390000)
public class EditAccountController {

    private static final Logger logger = LoggerFactory.getLogger(EditAccountController.class);

    @Autowired
    private ValueEncoder valueEncoder;


    @Autowired
    private AdminAccountService adminAccountService;

    /**
     * 增加帐号主页，xhtml 仅用于展示页面，
     * @return
     */
    @RequestMapping("editAccount.xhtml")
    @MenuResource("增加/修改管理员主页")
    public String index() {
        return ("/account/edit_account");
    }

    /**
     * 增加、修改帐号信息
     *
     * @param account 帐号数据，包括uid、mobileNo、roleId
     * @return result.success=true表示成功
     */
    @RequestMapping(value = "updateAccount.do", method = RequestMethod.POST)
    @MenuResource("增加/修改帐号信息")
    @ResponseBody
    @AdminLog
    public RestResult<Object> updateAccount(@NotNull(message="account不能为空")
                                                @Valid @ModelAttribute AdminAccount account) {
        logger.info("account: " + JsonUtil.toJSONString(account));

        if (StringUtils.isBlank(account.getAccount())) {
            return new RestResult<>(ApiCode.PARAMETER_ERROR, "帐号不能为空");
        }
        if (StringUtils.isBlank(account.getMobileNo())) {
            return new RestResult<>(ApiCode.PARAMETER_ERROR, "手机号码不能为空");
        }
        if (StringUtils.isBlank(account.getRealname())) {
            return new RestResult<>(ApiCode.PARAMETER_ERROR, "真实姓名不能为空");
        }
        if (StringUtils.isBlank(account.getPassword())){
            return new RestResult<>(ApiCode.PARAMETER_ERROR,"密码不能为空");
        }
        if (account.getRoleId() <= 0) {
            return new RestResult<>(ApiCode.PARAMETER_ERROR, "请选择一个角色");
        }
        /**
         * 对更新的密码用MD5加密
         */
        account.setPassword(valueEncoder.encode(account.getPassword()));

        Set<Integer> roles = new HashSet<>();
        roles.add(account.getRoleId());
        adminAccountService.addOrUpdate(account, roles);

        RestResult<Object> result = new RestResult<>(ApiCode.SUCCESS, "操作成功.");

        return result;
    }

    /**
     * 读取帐号信息
     * @param uid 管理员ID
     * @return 返回管理员信息
     */
    @RequestMapping("getAdmin")
    @MenuResource("读取帐号信息")
    @ResponseBody
    public RestResult<AdminAccount> getAdmin(long uid) {
        AdminAccount account = adminAccountService.getById(uid);
        RestResult<AdminAccount> result;
        if (account == null) {
            result = new RestResult<>(ApiCode.FAILURE, "管理员不存在：uid=" + uid);
        }
        else {
            List<AccountRoleRelation> list = adminAccountService.listAccountRoles(uid);
            if (list.size() == 1) {
                account.setRoleId(list.get(0).getRoleId());
            }
            account.setPassword(valueEncoder.decode(account.getPassword()));
            result = new RestResult<>(account);
        }
        return result;
    }

}
