package com.hopedove.gatewayserver.global;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.response.ErrorBody;
import com.hopedove.commons.response.RestResponse;
import com.netflix.zuul.exception.ZuulException;

/**
 * 全局异常控制类
 */
@RestController
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @GetMapping(value = "/error")
    public RestResponse error(HttpServletRequest request) {
        log.debug("@@@@ 执行错误统一处理函数");
        String message = request.getAttribute("javax.servlet.error.message").toString();
        Exception exp = (Exception) request.getAttribute("javax.servlet.error.exception");
        return buildErrorResponse(HttpStatus.BAD_GATEWAY.value() + "", exp);
    }

    /**
     * 构建错误返回体
     *
     * @param code
     * @param exception
     * @return
     */
    private RestResponse buildErrorResponse(String code, Exception exception) {
        ErrorBody errorBody = new ErrorBody();

        if (exception instanceof BusinException) {
            BusinException exp = (BusinException) exception;
            errorBody.setErrorCode(exp.getErrorCode());
            errorBody.setErrorMessage(exp.getErrorMessage());
        } else if (exception instanceof ZuulException) {
            errorBody.setErrorCode("BAD_GATEWAY");
            errorBody.setErrorMessage("网关内部错误");
        } else {
            errorBody.setErrorCode("ERROR_SERVER");
            errorBody.setErrorMessage("系统错误");
        }

        errorBody.setExceptionMessage(ExceptionUtils.getMessage(exception));
        errorBody.setExceptionDate(new Date());
        errorBody.setExceptionType(exception.getClass().getName());
        errorBody.setExceptionStackTrace(ExceptionUtils.getStackTrace(exception));

        return new RestResponse(errorBody);
    }
}
