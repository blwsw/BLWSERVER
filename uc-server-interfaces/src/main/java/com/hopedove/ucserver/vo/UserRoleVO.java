package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("用户的角色实体")
public class UserRoleVO extends BasicVO {
    private Integer roleId;

    private String roleName;

    private Integer roleTemplateId;

    private String roleTemplateName;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleTemplateId() {
        return roleTemplateId;
    }

    public void setRoleTemplateId(Integer roleTemplateId) {
        this.roleTemplateId = roleTemplateId;
    }

    public String getRoleTemplateName() {
        return roleTemplateName;
    }

    public void setRoleTemplateName(String roleTemplateName) {
        this.roleTemplateName = roleTemplateName;
    }
}
