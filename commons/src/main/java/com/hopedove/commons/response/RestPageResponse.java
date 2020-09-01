package com.hopedove.commons.response;

import com.hopedove.commons.vo.BasicPageVO;

public class RestPageResponse<T> extends RestResponse<T> {

    private BasicPageVO page;

    public RestPageResponse() {
        super();
    }

    public RestPageResponse(T t) {
        super(t);
    }

    public RestPageResponse(ErrorBody errorBody) {
        super(errorBody);
    }

    public RestPageResponse(T t, BasicPageVO basicPageVo) {
        super(t);
        page = basicPageVo;
    }

    public BasicPageVO getPage() {
        return page;
    }

    public void setPage(BasicPageVO page) {
        this.page = page;
    }
}
