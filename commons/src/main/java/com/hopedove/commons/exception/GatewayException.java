package com.hopedove.commons.exception;

/**
 * 网关异常
 * 所有网关内使用的异常从此处延伸
 */
public class GatewayException extends BaseException {
    public GatewayException() {
    }

    public GatewayException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public GatewayException(ErrorCode errorCode) {
        super(errorCode);
    }
}
