package com.hopedove.commons.response;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("ErrorBody(错误信息实体)")
public class ErrorBody implements Serializable {

    //异常代码
    @ApiModelProperty("详细错误编码")
    private String errorCode;

    //异常描述信息
    @ApiModelProperty("详细错误信息")
    private String errorMessage;

    //异常发生时间
    @ApiModelProperty("异常发生时间")
    private Date exceptionDate;

    //异常类名
    @ApiModelProperty("异常类名")
    private String exceptionType;

    //异常描述
    @ApiModelProperty("异常描述")
    private String exceptionMessage;

    //异常堆栈
    @ApiModelProperty("异常堆栈")
    private String exceptionStackTrace;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }
}
