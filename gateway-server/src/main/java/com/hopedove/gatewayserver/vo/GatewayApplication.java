package com.hopedove.gatewayserver.vo;

import java.io.Serializable;

public class GatewayApplication implements Serializable {
    private Integer id;
    private String appId;
    private String appPublicKey;
    private String appGateway;
    private Integer disabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPublicKey() {
        return appPublicKey;
    }

    public void setAppPublicKey(String appPublicKey) {
        this.appPublicKey = appPublicKey;
    }

    public String getAppGateway() {
        return appGateway;
    }

    public void setAppGateway(String appGateway) {
        this.appGateway = appGateway;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
}
