package io.ideploy.deployment.admin.service.global;

import io.ideploy.deployment.admin.vo.global.RepoAuth;
import java.util.List;

/**
 * @author: code4china
 * @description:
 * @date: Created in 13:28 2018/10/12
 */
public interface RepoAuthService {

    List<RepoAuth> findRepoAuthList(long uid, String authName, int page, int pageSize);

    int findAuthTotalCount(long uid, String authName);

    void save(RepoAuth repoAuth);

    RepoAuth get(int authId);
}
