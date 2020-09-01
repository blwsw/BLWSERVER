package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel("工厂角色实体")
public class RoleVO extends BasicVO {

    private Integer id;

    private String name;

    private Integer factoryId;

    private RoleTemplateVO roleTemplate;

    private List<RoleFunctionVO> functions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public RoleTemplateVO getRoleTemplate() {
        return roleTemplate;
    }

    public void setRoleTemplate(RoleTemplateVO roleTemplate) {
        this.roleTemplate = roleTemplate;
    }

    public List<RoleFunctionVO> getFunctions() {
        return functions;
    }

    public void setFunctions(List<RoleFunctionVO> functions) {
        this.functions = functions;
    }
}
