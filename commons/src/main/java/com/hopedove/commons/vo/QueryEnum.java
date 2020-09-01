package com.hopedove.commons.vo;

public enum QueryEnum {
    PAGES("pages"),
    PARAMS("params"),
    SORTS("sorts");

    QueryEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
