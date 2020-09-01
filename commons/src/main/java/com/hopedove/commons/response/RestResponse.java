package com.hopedove.commons.response;


import com.hopedove.commons.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

public class RestResponse<T> implements IResponse<T> {

    private Long respId = new Date().getTime();

    private Integer code = HttpStatus.OK.value();

    private String message = "请求成功";

    private T responseBody;

    private ErrorBody errorBody;

    //构造函数
    public RestResponse() {
    }

    //构造函数
    public RestResponse(T t) {
        this.responseBody = t;
    }

    //构造函数
    public RestResponse(ErrorBody errorBody) {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = "请求失败";
        this.errorBody = errorBody;
    }

    public Long getRespId() {
        return respId;
    }

    public void setRespId(Long respId) {
        this.respId = respId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }

    public ErrorBody getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(ErrorBody errorBody) {
        this.errorBody = errorBody;
    }
}
