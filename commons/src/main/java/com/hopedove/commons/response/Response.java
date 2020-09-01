package com.hopedove.commons.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public abstract class Response<T> implements IResponse<T> {

    @JsonIgnore
    public final static boolean isSuccess(IResponse response) {
        RestResponse restResponse = (RestResponse) response;
        return HttpStatus.OK.value() == restResponse.getCode();
    }

    @JsonIgnore
    public final static boolean isFail(IResponse response) {
        return !isSuccess(response);
    }
}
