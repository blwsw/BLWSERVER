package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("角色模板关联菜单实体")
public class RoleTemplateMenuVO extends BasicVO {

    private Integer roleTemplateId;

    private Integer menuId;

    public Integer getRoleTemplateId() {
        return roleTemplateId;
    }

    public void setRoleTemplateId(Integer roleTemplateId) {
        this.roleTemplateId = roleTemplateId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
