package io.ideploy.deployment.admin.dao.global;

import io.ideploy.deployment.admin.po.global.RepoAuthPO;
import io.ideploy.deployment.admin.po.global.RoleAuthRelationPO;
import io.ideploy.deployment.admin.vo.global.RoleAuthRelation;
import io.ideploy.deployment.datasource.MyBatisDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author: code4china
 * @description:
 * @date: Created in 14:36 2018/10/12
 */
@MyBatisDao
public interface RoleAuthRelationDao {

    void save(RoleAuthRelationPO roleAuthRelationPO);

    int deleteById(@Param("id")int id);

    int deleteByAuthId(@Param("authId")int authId);

    int update(@Param("id") int id, @Param("authId") int authId);

    List<RepoAuthPO> listByRoleId(@Param("roleId")long roleId);

    List<RoleAuthRelationPO> listByAuthId(@Param("authId") int authId);
}
