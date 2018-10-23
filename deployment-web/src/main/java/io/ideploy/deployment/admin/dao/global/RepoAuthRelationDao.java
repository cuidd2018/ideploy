package io.ideploy.deployment.admin.dao.global;

import io.ideploy.deployment.admin.po.global.RepoAuthPO;
import io.ideploy.deployment.admin.po.global.RepoAuthRelationPO;
import io.ideploy.deployment.datasource.MyBatisDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author: code4china
 * @description:
 * @date: Created in 14:36 2018/10/12
 */
@MyBatisDao
public interface RepoAuthRelationDao {

    void save(RepoAuthRelationPO repoAuthRelationPO);

    int deleteById(@Param("id")int id);

    int update(@Param("id") int id, @Param("authId") int authId);

    List<RepoAuthPO> listAllAuths(@Param("uid")long uid);

}
