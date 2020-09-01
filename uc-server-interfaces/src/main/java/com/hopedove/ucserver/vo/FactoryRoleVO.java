package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;

import java.util.List;

public class FactoryRoleVO extends BasicVO {

    private Integer id;

    private Integer factoryId;

    private Integer roleTemplateId;

    private String name;

    private String factoryName;

    private String roleTemplateName;

    private List<FactoryRoleFunctionVO> roleFunctions;

    public List<FactoryRoleFunctionVO> getRoleFunctions() {
        return roleFunctions;
    }

    public void setRoleFunctions(List<FactoryRoleFunctionVO> roleFunctions) {
        this.roleFunctions = roleFunctions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getRoleTemplateId() {
        return roleTemplateId;
    }

    public void setRoleTemplateId(Integer roleTemplateId) {
        this.roleTemplateId = roleTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getRoleTemplateName() {
        return roleTemplateName;
    }

    public void setRoleTemplateName(String roleTemplateName) {
        this.roleTemplateName = roleTemplateName;
    }
}
