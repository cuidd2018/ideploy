package io.ideploy.deployment.admin.dao.global;

import io.ideploy.deployment.admin.po.global.RepoAuthPO;
import io.ideploy.deployment.datasource.MyBatisDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author: code4china
 * @description:
 * @date: Created in 14:25 2018/10/12
 */
@MyBatisDao
public interface RepoAuthDao {

    void save(RepoAuthPO repoAuthPO);

    List<RepoAuthPO> list(@Param("uid")long uid, @Param("authName")String authName, @Param("page")int page,@Param("pageSize")int pageSize);

    int getTotalCnt(@Param("uid")long uid, @Param("authName")String authName);

    int deleteById(int authId);

    int update(RepoAuthPO repoAuthPO);

    RepoAuthPO getById(int authId);

}
