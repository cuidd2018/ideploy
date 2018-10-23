package io.ideploy.deployment.admin.websocket.comp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.ideploy.deployment.admin.service.deploy.DeployHistoryService;
import io.ideploy.deployment.admin.utils.RedisKey;
import io.ideploy.deployment.admin.websocket.context.LogIdToSessionHolder;
import io.ideploy.deployment.admin.websocket.request.ShellLogResponseMessage;
import io.ideploy.deployment.admin.websocket.request.WebSocketRequestType;
import io.ideploy.deployment.base.ApiCode;
import io.ideploy.deployment.common.util.redis.Redis;
import io.ideploy.deployment.common.vo.ServerCollectLog;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

/**
 * 功能：订阅日志并发送给服务端的线程
 * 详细：
 *
 * @author linyi, 2017/2/9.
 */
public class SubscribeLogThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(SubscribeLogThread.class);

    private DeployHistoryService deployHistoryService;

    private Redis redis;

    private JedisPubSub jedisPubSub;

    public SubscribeLogThread(ApplicationContext context) {
        super("logSubscriberThread");
        this.redis = context.getBean(Redis.class);
        this.deployHistoryService = context.getBean(DeployHistoryService.class);
    }

    public void stopThread() {
        interrupt();
        if (jedisPubSub != null) {
            jedisPubSub.unsubscribe();
        }
    }

    @Override
    public void run() {
        try {
            jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    logger.info("接收到通道:{} 发布的信息, message: {}", channel, message);
                    try {

                        /** 解析收到订阅推送过来的报文 **/
                        List<ServerCollectLog> logList = JSONArray.parseArray(message, ServerCollectLog.class);

                        for (ServerCollectLog log : logList) {

                            /*** 当前发布ID关联的所有的websocket session ***/
                            Set<WebSocketSession> sessions = LogIdToSessionHolder.getInstance().get(log.getId());

                            /*** 当前websocket链接不存在，丢弃日志消息 ***/
                            if (CollectionUtils.isEmpty(sessions)) {
                                continue;
                            }

                            /** 组装待推送的消息报文 **/
                            ShellLogResponseMessage responseMessage = new ShellLogResponseMessage();
                            ShellLogResponseMessage.ServerShellLog shellLog = new ShellLogResponseMessage.ServerShellLog();
                            shellLog.setServerDeployId(log.getId());
                            shellLog.setLog(log.getContent());
                            List<ShellLogResponseMessage.ServerShellLog> shellLogList = new ArrayList<>();
                            shellLogList.add(shellLog);
                            responseMessage.setServerLogs(shellLogList);
                            responseMessage.setCode(ApiCode.SUCCESS);
                            responseMessage.setType(WebSocketRequestType.DEPLOY_SHELL_LOG.getName());

                            String messageStr = JSONObject.toJSONString(responseMessage);

                            /** 向监听用户推送报文 **/
                            for (WebSocketSession session : sessions) {
                                if (!session.isOpen()) {
                                   continue;
                                }
                                try {
                                    session.sendMessage(new TextMessage(messageStr));
                                } catch (Exception e) {
                                    logger.error("发送推送日志消息失败 | msg:{}", e.getMessage(), e);
                                }
                            }

                        } // end for
                    } catch (Exception e) {
                        logger.error("处理订阅的日志更改信息失败 | msg:{}", e.getMessage(), e);
                    }

                }
            };
            logger.info("订阅开始……");
            redis.subscribe(jedisPubSub, RedisKey.getDeploySubscribeChannelKey());
            logger.info("订阅结束 ...");
        }catch (Exception e){
            logger.error("订阅失败", e);
        }
    }

}
