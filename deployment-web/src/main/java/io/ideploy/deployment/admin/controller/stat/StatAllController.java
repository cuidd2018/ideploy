package io.ideploy.deployment.admin.controller.stat;

import io.ideploy.deployment.admin.common.RestResult;
import io.ideploy.deployment.admin.service.global.ProjectEnvService;
import io.ideploy.deployment.admin.service.stat.StatService;
import io.ideploy.deployment.admin.utils.resource.Menu;
import io.ideploy.deployment.admin.utils.resource.MenuResource;
import io.ideploy.deployment.admin.vo.stat.StatAll;
import io.ideploy.deployment.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 功能：查询所有项目的发布情况
 * 详细：
 *
 * @author linyi, 2017/2/28.
 */
@Controller
@RequestMapping("/admin/stat")
@Menu(name = "按环境统计", parent = "统计", sequence = 900000)
public class StatAllController {

    @Autowired
    private StatService statService;

    @Autowired
    private ProjectEnvService projectEnvService;

    /**
     * 按环境统计主页
     */
    @RequestMapping("statAll.xhtml")
    @MenuResource("按环境统计主页")
    public String index(HttpServletRequest request) {
        request.setAttribute("envList", projectEnvService.findAllEnv());
        return ("/stat/stat_all");
    }

    @RequestMapping("queryStatAll")
    @MenuResource("查询所有项目的发布情况")
    @ResponseBody
    public RestResult<List<StatAll>> query(@RequestParam(value="envId", defaultValue="1") int envId,
                                                        @DateTimeFormat(pattern=Constants.DATE_FORMAT) Date start,
                                                 @DateTimeFormat(pattern=Constants.DATE_FORMAT) Date end) {

        List<StatAll> statAllList = statService.queryStatAll(envId, start, end);

        return new RestResult<>(statAllList);
    }

}
