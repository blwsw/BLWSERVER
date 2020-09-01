package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("角色模板关联权限实体")
public class FactoryRoleFunctionVO extends BasicVO {

    private Integer roleId;

    private Integer roleTemplateId;

    private Integer functionId;

    private String dataPermType;

    private String dataPermJSON;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDataPermType() {
        return dataPermType;
    }

    public void setDataPermType(String dataPermType) {
        this.dataPermType = dataPermType;
    }

    public String getDataPermJSON() {
        return dataPermJSON;
    }

    public void setDataPermJSON(String dataPermJSON) {
        this.dataPermJSON = dataPermJSON;
    }

    public Integer getRoleTemplateId() {
        return roleTemplateId;
    }

    public void setRoleTemplateId(Integer roleTemplateId) {
        this.roleTemplateId = roleTemplateId;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }
}
