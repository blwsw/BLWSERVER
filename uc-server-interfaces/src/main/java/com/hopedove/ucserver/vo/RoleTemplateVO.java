package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel("角色模板实体")
public class RoleTemplateVO extends BasicVO {

    private Integer id;

    private String name;

    private String code;

    private List<RoleTemplateFunctionVO> roleTemplateFunctions;

    private List<RoleTemplateMenuVO> roleTemplateMenus;

    public List<RoleTemplateFunctionVO> getRoleTemplateFunctions() {
        return roleTemplateFunctions;
    }

    public void setRoleTemplateFunctions(List<RoleTemplateFunctionVO> roleTemplateFunctions) {
        this.roleTemplateFunctions = roleTemplateFunctions;
    }

    public List<RoleTemplateMenuVO> getRoleTemplateMenus() {
        return roleTemplateMenus;
    }

    public void setRoleTemplateMenus(List<RoleTemplateMenuVO> roleTemplateMenus) {
        this.roleTemplateMenus = roleTemplateMenus;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
