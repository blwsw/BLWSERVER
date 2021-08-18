package com.hopedove.coreserver.vo.core;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;

@ApiModel
public class ApplicationUpdateVO extends BasicVO {
    private Integer id;

    private Integer factoryId;

    private String factoryName;

    private String clientType;

    private String title;

    private String updateContent;

    private String version;

    private String subVersion;

    private String files;

    private String state;

    private Integer disabled;

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubVersion() {
        return subVersion;
    }

    public void setSubVersion(String subVersion) {
        this.subVersion = subVersion;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public Integer getDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
}
