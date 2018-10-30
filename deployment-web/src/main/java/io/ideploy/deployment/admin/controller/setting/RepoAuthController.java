package io.ideploy.deployment.admin.controller.setting;

import io.ideploy.deployment.admin.common.PageResult;
import io.ideploy.deployment.admin.common.RestResult;
import io.ideploy.deployment.admin.context.AdminContext;
import io.ideploy.deployment.admin.service.account.AdminAccountService;
import io.ideploy.deployment.admin.service.account.RoleService;
import io.ideploy.deployment.admin.service.global.RepoAuthService;
import io.ideploy.deployment.admin.utils.resource.Menu;
import io.ideploy.deployment.admin.utils.resource.MenuResource;
import io.ideploy.deployment.admin.vo.account.Role;
import io.ideploy.deployment.admin.vo.global.RepoAuth;
import io.ideploy.deployment.admin.vo.global.RoleAuthDetail;
import io.ideploy.deployment.admin.vo.global.RoleAuthRelation;
import io.ideploy.deployment.base.ApiCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: code4china
 * @description:
 * @date: Created in 13:26 2018/10/12
 */
@Menu(name="仓库访问", parent = "全局配置管理", sequence = 500039)
@Controller
@RequestMapping("admin/repoAuth")
public class RepoAuthController {

    @Autowired
    private RepoAuthService repoAuthService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminAccountService adminAccountService;

    /**
     * 查看环境
     */
    @RequestMapping("listRepoAuth.xhtml")
    @MenuResource("权限列表")
    public String index() {
        return ("/setting/list_repo_auth");
    }

    /**
     * 获取所有的环境
     */
    @ResponseBody
    @RequestMapping("list")
    @MenuResource("读取权限列表")
    public PageResult<List<RepoAuth>> listEnv(String authName, int page, int pageSize) {
        long uid = AdminContext.getAccountId();
        List<RepoAuth> allRepoAuth = repoAuthService.findRepoAuthList(uid, authName, page, pageSize);

        PageResult<List<RepoAuth>> pageResult = new PageResult<>(allRepoAuth);
        pageResult.setCurrentPage(page);
        pageResult.setPageSize(pageSize);
        pageResult.setCount(repoAuthService.findAuthTotalCount(uid, authName));
        return pageResult;
    }

    /**
     * 获取所有的环境
     */
    @ResponseBody
    @RequestMapping("getAuth")
    @MenuResource("查询配置详情")
    public RestResult<RoleAuthDetail> getAuth(int authId) {
        RoleAuthDetail detail = new RoleAuthDetail();
        if(authId > 0){
            RepoAuth repoAuth = repoAuthService.get(authId);
            detail.setRepoAuth(repoAuth);
        }

        boolean adminUser =  adminAccountService.isSuperAdmin(AdminContext.getAccountId());
        if(adminUser){
            List<Role> roles = roleService.listAll();
            detail.setAdminUser(adminUser);
            detail.setRoles(roles);

            List<RoleAuthRelation> roleAuths = repoAuthService.listByAuthId(authId);
            if(CollectionUtils.isNotEmpty(roleAuths)){
                List<Integer> existRoleIds = new ArrayList<>();
                for(RoleAuthRelation relation: roleAuths){
                    existRoleIds.add(relation.getRoleId());
                }
                detail.setExistRoleIds(existRoleIds);
            }
        }
        return new RestResult<>(detail);
    }

    /**
     * save
     */
    @ResponseBody
    @RequestMapping("save.do")
    @MenuResource("保存权限配置")
    public RestResult<Object> save(int authId, String authName, String authDesc, int repoType, String roleIds, String account, String password) {
        if (StringUtils.isBlank(authName) || StringUtils.isBlank(authDesc) || StringUtils.isBlank(account)) {
            return new RestResult<>(ApiCode.PARAMETER_ERROR, "参数不能为空");
        }
        long uid = AdminContext.getAccountId();
        RepoAuth repoAuth = new RepoAuth();
        repoAuth.setAuthId(authId);
        repoAuth.setCreatorId(uid);
        repoAuth.setAuthName(authName.trim());
        repoAuth.setAuthDesc(authDesc.trim());
        repoAuth.setRepoType(repoType);
        repoAuth.setAccount(account);
        if (StringUtils.isNotBlank(password)) {
            repoAuth.setPassword(password);
        }
        repoAuthService.save(repoAuth);

        if(adminAccountService.isSuperAdmin(AdminContext.getAccountId())){
            Set<Integer> roleIdSet = new HashSet<>();
            String[] roleIdStrs = roleIds.split(",");
            for(String str: roleIdStrs){
                roleIdSet.add(Integer.parseInt(str));
            }
            repoAuthService.saveRoleAuth(uid, authId, roleIdSet);
        }
        return new RestResult<>(null);
    }

}
