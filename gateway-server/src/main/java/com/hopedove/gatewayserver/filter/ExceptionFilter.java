package com.hopedove.gatewayserver.filter;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.response.ErrorBody;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class ExceptionFilter extends SendErrorFilter {

    private static final Logger log = LoggerFactory.getLogger(SendErrorFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return false;
    }

    @Override
    public Object run() {
        RestResponse restResponse = null;
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            SendErrorFilter.ExceptionHolder exception = this.findZuulException(ctx.getThrowable());
            HttpServletRequest request = ctx.getRequest();
            request.setAttribute("javax.servlet.error.status_code", exception.getStatusCode());

            log.warn("Error during filtering", exception.getThrowable());
            request.setAttribute("javax.servlet.error.exception", exception.getThrowable());

            if (StringUtils.hasText(exception.getErrorCause())) {
                request.setAttribute("javax.servlet.error.message", exception.getErrorCause());
            }

            restResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", (Exception) ctx.getThrowable());

            ctx.setResponseBody(JsonUtil.writeValueAsString(restResponse));
            ctx.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            ctx.setSendZuulResponse(false);
        } catch (Exception exp) {
            ReflectionUtils.rethrowRuntimeException(exp);
        }

        return null;
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
        } else {
            errorBody.setErrorCode("ERROR_SERVER");
            errorBody.setErrorMessage("系统错误");
            log.error("errorCode = " + code, exception);
        }

        errorBody.setExceptionMessage(ExceptionUtils.getMessage(exception));
        errorBody.setExceptionDate(new Date());
        errorBody.setExceptionType(exception.getClass().getName());
        errorBody.setExceptionStackTrace(ExceptionUtils.getStackTrace(exception));

        return new RestResponse(errorBody);
    }
}
