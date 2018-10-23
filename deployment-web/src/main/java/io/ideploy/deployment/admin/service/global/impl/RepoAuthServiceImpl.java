package io.ideploy.deployment.admin.service.global.impl;

import io.ideploy.deployment.admin.dao.global.RepoAuthDao;
import io.ideploy.deployment.admin.dao.global.RepoAuthRelationDao;
import io.ideploy.deployment.admin.po.global.RepoAuthPO;
import io.ideploy.deployment.admin.service.global.RepoAuthService;
import io.ideploy.deployment.admin.vo.global.RepoAuth;
import io.ideploy.deployment.common.util.StaticKeyHelper;
import io.ideploy.deployment.common.util.VOUtil;
import java.util.List;
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
    private RepoAuthRelationDao repoAuthRelationDao;

    @Override
    public List<RepoAuth> findRepoAuthList(long uid, String authName, int page, int pageSize) {
        int startIndex = (page - 1) * pageSize;
        List<RepoAuthPO> list =  repoAuthDao.list(uid,authName,startIndex,pageSize);
        List<RepoAuth> auths =  VOUtil.fromList(list, RepoAuth.class);
        for (RepoAuth repoAuth : auths){
            decodeAccountAndPassword(repoAuth);
        }
        return auths;
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
