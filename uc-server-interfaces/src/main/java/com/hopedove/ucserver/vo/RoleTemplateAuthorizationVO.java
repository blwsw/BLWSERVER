package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel(value = "角色模板授权实体", description = "描述哪些工厂可以使用角色模板")
public class RoleTemplateAuthorizationVO extends BasicVO {

    private Integer roleTemplateId;

    private Integer factoryId;

    private FactoryVO factory;

    private String roleName;

    public FactoryVO getFactory() {
        return factory;
    }

    public void setFactory(FactoryVO factory) {
        this.factory = factory;
    }

    public Integer getRoleTemplateId() {
        return roleTemplateId;
    }

    public void setRoleTemplateId(Integer roleTemplateId) {
        this.roleTemplateId = roleTemplateId;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
