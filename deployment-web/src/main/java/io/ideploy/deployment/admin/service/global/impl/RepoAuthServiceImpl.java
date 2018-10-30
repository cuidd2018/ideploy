package io.ideploy.deployment.admin.service.global.impl;

import com.google.common.collect.Lists;
import io.ideploy.deployment.admin.dao.account.RoleDao;
import io.ideploy.deployment.admin.dao.global.RepoAuthDao;
import io.ideploy.deployment.admin.dao.global.RoleAuthRelationDao;
import io.ideploy.deployment.admin.enums.AuthOwnType;
import io.ideploy.deployment.admin.po.account.RolePO;
import io.ideploy.deployment.admin.po.global.RepoAuthPO;
import io.ideploy.deployment.admin.po.global.RoleAuthRelationPO;
import io.ideploy.deployment.admin.service.global.RepoAuthService;
import io.ideploy.deployment.admin.vo.global.RepoAuth;
import io.ideploy.deployment.admin.vo.global.RoleAuthRelation;
import io.ideploy.deployment.common.util.StaticKeyHelper;
import io.ideploy.deployment.common.util.VOUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: code4china
 * @description:
 * @date: Created in 13:37 2018/10/12
 */
@Service
public class RepoAuthServiceImpl implements RepoAuthService{

    @Autowired
    private RepoAuthDao repoAuthDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleAuthRelationDao roleAuthRelationDao;

    @Override
    public List<RepoAuth> findRepoAuthList(long uid, String authName, int page, int pageSize) {
        int startIndex = (page - 1) * pageSize;
        List<RepoAuthPO> list =  repoAuthDao.list(uid,authName,startIndex,pageSize);
        List<RepoAuth> auths =  VOUtil.fromList(list, RepoAuth.class);
        for (RepoAuth repoAuth : auths){
            repoAuth.setOwnType(AuthOwnType.USER.getValue());
            decodeAccountAndPassword(repoAuth);
        }

        List<RepoAuth> roleAuths = listRoleAuths(uid);
        if(CollectionUtils.isNotEmpty(roleAuths)){
            for(RepoAuth repoAuth: roleAuths){
                repoAuth.setOwnType(AuthOwnType.COMMON.getValue());
            }
            auths.addAll(roleAuths);
        }

        return auths;
    }

    private List<RepoAuth> listRoleAuths(long uid){
        List<RolePO> allRoles = roleDao.listByAccount(uid);
        if(CollectionUtils.isEmpty(allRoles)){
            return Lists.newArrayList();
        }
        List<RepoAuth> result = new ArrayList<>();
        for(RolePO po: allRoles){
            List<RepoAuthPO> poList = roleAuthRelationDao.listByRoleId(po.getRoleId());
            if(CollectionUtils.isEmpty(poList)){
                continue;
            }
            List<RepoAuth> repoAuths =  VOUtil.fromList(poList, RepoAuth.class);
            result.addAll(repoAuths);
        }
        return result;
    }

    @Override
    public int findAuthTotalCount(long uid, String authName) {
        return repoAuthDao.getTotalCnt(uid,authName);
    }

    @Override
    public void save(RepoAuth repoAuth) {
        RepoAuthPO po = VOUtil.from(repoAuth, RepoAuthPO.class);
        po.setAccount(StaticKeyHelper.encryptKey(repoAuth.getAccount()));
        if(StringUtils.isNotBlank(repoAuth.getPassword())) {
            po.setPassword(StaticKeyHelper.encryptKey(repoAuth.getPassword()));
        }
        if(repoAuth.getAuthId() == 0) {
            repoAuthDao.save(po);
        }
        else{
            repoAuthDao.update(po);
        }
    }

    @Override
    public RepoAuth get(int authId) {
        RepoAuth repoAuth =  VOUtil.from(repoAuthDao.getById(authId), RepoAuth.class);
        decodeAccountAndPassword(repoAuth);
        return repoAuth;
    }

    @Override
    public List<RoleAuthRelation> listByAuthId(int authId) {
        List<RoleAuthRelationPO> poList = roleAuthRelationDao.listByAuthId(authId);
        return VOUtil.fromList(poList, RoleAuthRelation.class);
    }

    @Override
    public void saveRoleAuth(long uid, int authId, Collection<Integer> roleIds) {
        roleAuthRelationDao.deleteByAuthId(authId);
        if(CollectionUtils.isEmpty(roleIds)){
            return;
        }
        for(Integer roleId: roleIds){
           RoleAuthRelationPO po = new RoleAuthRelationPO();
           po.setAuthId(authId);
           po.setRoleId(roleId);
           roleAuthRelationDao.save(po);
        }
    }

    private void decodeAccountAndPassword(RepoAuth repoAuth) {
        if (repoAuth != null) {
            if (StringUtils.isNotEmpty(repoAuth.getAccount())) {
                repoAuth.setAccount(StaticKeyHelper.descryptKey(repoAuth.getAccount()));
            }
            if (StringUtils.isNotEmpty(repoAuth.getAccount())) {
                repoAuth.setPassword(StaticKeyHelper.descryptKey(repoAuth.getPassword()));
            }
        }
    }
}
