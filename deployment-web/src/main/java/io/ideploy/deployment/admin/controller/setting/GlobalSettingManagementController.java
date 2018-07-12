package io.ideploy.deployment.admin.controller.setting;

import io.ideploy.deployment.admin.utils.resource.Menu;
import org.springframework.stereotype.Controller;

/**
 * 全局配置controller,仅用于菜单
 *
 * @author 梁广龙 2017/1/13.
 */
@Controller
@Menu(name = "全局配置管理", parent = "", sequence = 900000)
public class GlobalSettingManagementController {
}
