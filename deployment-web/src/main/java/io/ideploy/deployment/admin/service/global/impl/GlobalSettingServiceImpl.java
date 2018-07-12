package io.ideploy.deployment.admin.service.global.impl;

import io.ideploy.deployment.admin.dao.global.GlobalSettingDao;
import io.ideploy.deployment.admin.vo.global.GlobalSetting;
import io.ideploy.deployment.admin.po.global.GlobalSettingPO;
import io.ideploy.deployment.admin.service.global.GlobalSettingService;
import io.ideploy.deployment.common.util.VOUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 详情 :  GlobalSettingService 实现类
 *
 * @author K-Priest
 */
@Service
public class GlobalSettingServiceImpl implements GlobalSettingService {

    @Autowired
    private GlobalSettingDao globalSettingDao;

    @Override
    public GlobalSetting getGlobalSetting() {
        List<GlobalSettingPO> globalSettingPOs = globalSettingDao.findAll();
        if (CollectionUtils.isEmpty(globalSettingPOs)) {
            return new GlobalSetting();
        }
        return VOUtil.from(globalSettingPOs.get(0), GlobalSetting.class);
    }

    @Override
    public void saveGlobalSetting(GlobalSettingPO setting) {
        globalSettingDao.insert(setting);
    }

    @Override
    public int updateGlobalSetting(GlobalSettingPO setting) {
        return globalSettingDao.update(setting);
    }
}
