package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("角色模板关联权限实体")
public class RoleFunctionVO extends BasicVO {

    private Integer roleId;

    private Integer functionId;

    private String dataPermType;

    private String dataPermJSON;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDataPermJSON() {
        return dataPermJSON;
    }

    public void setDataPermJSON(String dataPermJSON) {
        this.dataPermJSON = dataPermJSON;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public String getDataPermType() {
        return dataPermType;
    }

    public void setDataPermType(String dataPermType) {
        this.dataPermType = dataPermType;
    }
}
