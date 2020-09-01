package com.hopedove.commons.exception;

/**
 * 业务异常
 * 所有的业务异常从此处延伸
 */
public class BusinException extends BaseException {
    public BusinException() {
        super();
    }

    public BusinException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public BusinException(ErrorCode errorCode) {
        super(errorCode);
    }
}
