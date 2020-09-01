package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("角色模板关联权限实体")
public class RoleTemplateFunctionVO extends BasicVO {

    private Integer roleTemplateId;

    private Integer functionId;

//    private String dataPermType;

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
//
//    public String getDataPermType() {
//        return dataPermType;
//    }
//
//    public void setDataPermType(String dataPermType) {
//        this.dataPermType = dataPermType;
//    }
}
