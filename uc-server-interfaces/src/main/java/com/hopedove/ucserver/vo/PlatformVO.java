package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;

public class PlatformVO extends BasicVO {

    private Integer id;

    private String code;

    private String codeValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
