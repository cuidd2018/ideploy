package io.ideploy.deployment.admin.controller.stat;

import io.ideploy.deployment.admin.common.RestResult;
import io.ideploy.deployment.admin.service.stat.StatService;
import io.ideploy.deployment.admin.utils.resource.Menu;
import io.ideploy.deployment.admin.utils.resource.MenuResource;
import io.ideploy.deployment.admin.vo.stat.LowQualityRank;
import io.ideploy.deployment.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 功能：查询低质量发布的项目
 * 详细：低质量发布的项目只统计生产环境的情况
 *
 * @author linyi, 2017/2/28.
 */
@Controller
@RequestMapping("/admin/stat")
@Menu(name = "低质量发布", parent = "统计", sequence = 700000)
public class LowQualityRankController {

    @Autowired
    private StatService statService;

    /**
     * 低质量发布主页
     */
    @RequestMapping("lowQualityRank.xhtml")
    @MenuResource("低质量发布主页")
    public String index() {
        return ("/stat/low_quality_rank");
    }

    @RequestMapping("queryLowQualityRank")
    @MenuResource("查询低质量发布数据")
    @ResponseBody
    public RestResult<List<LowQualityRank>> query(@DateTimeFormat(pattern= Constants.DATE_FORMAT) Date start,
                                                  @DateTimeFormat(pattern=Constants.DATE_FORMAT) Date end) {
        return new RestResult<>(statService.queryLowQualityRank(start, end));
    }
}
