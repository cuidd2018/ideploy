package io.ideploy.deployment.admin.controller;

import io.ideploy.deployment.admin.common.RestResult;
import io.ideploy.deployment.base.ApiCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: code4china
 * @description:
 * @date: Created in 16:38 2018/8/29
 */
@ControllerAdvice
public class DefaultExpAdvice {

    private static Logger logger= LoggerFactory.getLogger(DefaultExpAdvice.class);

    /**
     * 全局异常捕捉处理
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RestResult errorHandler(Exception e) {
        logger.error("系统内部异常", e);
        return new RestResult(ApiCode.FAILURE, "服务器内部错误");
    }

}
