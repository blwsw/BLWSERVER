package com.hopedove.commons.exception;

public class SignException extends GatewayException {
    public SignException() {
        super(ErrorCode.EXP_SIGN);
    }
}
