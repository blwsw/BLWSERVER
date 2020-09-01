package com.hopedove.commons.exception;

import java.io.Serializable;

public class BaseException extends RuntimeException {
    private String errorCode;

    private String errorMessage;

    public BaseException() {

    }

    public BaseException(String errorCode, String errorMessage) {
        super(null, null, false, false);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseException(ErrorCode errorCode) {
        super(null, null, false, false);
        this.errorCode = errorCode.name();
        this.errorMessage = errorCode.getValue();
    }

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
}
