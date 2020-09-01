package com.hopedove.ucserver.global;

import com.hopedove.commons.exception.BaseException;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.response.ErrorBody;
import com.hopedove.commons.response.RestResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 全局异常控制类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 404异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestResponse errorHandler(HttpServletRequest request, NoHandlerFoundException exception, HttpServletResponse response) {
        return buildErrorResponse(HttpStatus.NOT_FOUND.value() + "", exception);
    }

    /**
     * 405异常处理
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public RestResponse errorHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException exception, HttpServletResponse response) {
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value() + "", exception);
    }

    /**
     * 415异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public RestResponse errorHandler(HttpServletRequest request, HttpMediaTypeNotSupportedException exception, HttpServletResponse response) {
        return buildErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value() + "", exception);
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    private RestResponse errorHandler(HttpServletRequest request, BaseException exception, HttpServletResponse response) {
        return buildErrorResponse("600", exception);
    }

    /**
     * 500异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RestResponse errorHandler(HttpServletRequest request, Exception exception, HttpServletResponse response) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", exception);
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
