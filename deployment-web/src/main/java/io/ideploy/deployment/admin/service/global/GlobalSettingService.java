package io.ideploy.deployment.admin.service.global;


import io.ideploy.deployment.admin.vo.global.GlobalSetting;
import io.ideploy.deployment.admin.po.global.GlobalSettingPO;

/**
 * 详情 : 全局配置service
 *
 * @author K-Priest
 */
public interface GlobalSettingService {

    /**
     * 获取全局配置
     * @return 第一个配置
     */
    GlobalSetting getGlobalSetting();

    /**
     * 保存配置
     * @param setting
     * @return
     */
    void saveGlobalSetting(GlobalSettingPO setting);

    /**
     * 更新配置
     * @param setting
     * @return
     */
    int updateGlobalSetting(GlobalSettingPO setting);
}
