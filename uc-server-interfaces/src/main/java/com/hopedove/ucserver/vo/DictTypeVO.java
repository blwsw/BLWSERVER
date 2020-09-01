package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;

import java.util.List;

public class DictTypeVO extends BasicVO {
    private String code;

    private String name;

    private Integer disabled;

    private List<DictVO> dicts;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public List<DictVO> getDicts() {
        return dicts;
    }

    public void setDicts(List<DictVO> dicts) {
        this.dicts = dicts;
    }
}
