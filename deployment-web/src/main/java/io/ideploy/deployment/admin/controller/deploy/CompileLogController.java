package io.ideploy.deployment.admin.controller.deploy;

import io.ideploy.deployment.admin.annotation.authority.AllowAnonymous;
import io.ideploy.deployment.admin.service.deploy.DeployHistoryService;
import io.ideploy.deployment.admin.vo.deploy.CompileLogVO;
import io.ideploy.deployment.admin.vo.deploy.DeployHistory;
import io.ideploy.deployment.common.enums.DeployStatus;
import io.ideploy.deployment.common.util.RedisLogKey;
import io.ideploy.deployment.common.util.redis.Redis;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 功能：显示编译日志
 * 详细：
 *
 * @author linyi, 2017/2/20.
 */
@Controller
@RequestMapping("/admin/deploy/")
public class CompileLogController {
    private static final Logger logger = LoggerFactory.getLogger(CompileLogController.class);

    public static final String LOG_FINISHED = "logFinished";

    public static final String FETCH_LOG = "fetchNext";

    @Autowired
    private DeployHistoryService deployHistoryService;

    @Autowired
    private Redis redis;

    /**
     * 读取编译日志主页，xhtml 仅用于展示页面，ajax 调用接口获取参数
     *
     * @return
     */
    @RequestMapping("compileLog.xhtml")
    @AllowAnonymous
    public String index(int historyId,HttpServletRequest request) {
        if(historyId <=0){
            return historyNotFound(request);
        }
        DeployHistory deployHistory = deployHistoryService.getByHistoryId(historyId);
        if(deployHistory == null){
            return historyNotFound(request);
        }

        request.setAttribute("title", "编译中……");
        request.setAttribute(FETCH_LOG, "true");
        return "/deploy/compile_log";
    }


    @RequestMapping("getCompileLog")
    @AllowAnonymous
    @ResponseBody
    public CompileLogVO getCompileLog(int historyId, int offset) {
        CompileLogVO logVO = new CompileLogVO();
        logVO.setOffset(offset);

        String key = RedisLogKey.getModuleCompileKey(historyId);
        Long count = redis.llen(key);
        //logger.info("编译日志，总数:{}, 当前偏移量:{}", count, offset);
        if (offset == count) {
            stopIfFinished(historyId, logVO);
            return logVO;
        }
        if (count > 0) {
            List<String> logs = redis.lrange(key, offset, count);
            logVO.setLogs(logs);
            logVO.setOffset(count.intValue());
            return logVO;
        }

        stopIfFinished(historyId, logVO);
        return logVO;
    }

    private String historyNotFound(HttpServletRequest request){
        request.setAttribute("title", "发布历史不存在");
        request.setAttribute(FETCH_LOG, "false");
        return "/deploy/compile_log";
    }

    private void stopIfFinished(int historyId, CompileLogVO logVO){
        DeployHistory deployHistory = deployHistoryService.getByHistoryId(historyId);
        if(deployHistory != null && deployHistory.getDeployStatus() > DeployStatus.DEPLOYING.getValue()){
            logVO.setLogs(Arrays.asList(LOG_FINISHED));
        }
    }
}
