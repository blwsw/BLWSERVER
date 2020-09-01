package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户的权限实体")
public class UserPermissionVO extends BasicVO {

    private Integer id;

    private String name;

    private String code;

//    private String router;
//
//    private String type;

    private Integer moduleId;

    @ApiModelProperty("可用数据权限类型字典编码字符串")
    private String dataPermOptions;

    @ApiModelProperty("数据权限类型字典编码（默认从角色模板带过来）")
    private String dataPermType;

    @ApiModelProperty("数据权限字符串（指定部门）（默认从角色模板带过来）")
    private String dataPermJSON;

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

//    public String getRouter() {
//        return router;
//    }
//
//    public void setRouter(String router) {
//        this.router = router;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getDataPermOptions() {
        return dataPermOptions;
    }

    public void setDataPermOptions(String dataPermOptions) {
        this.dataPermOptions = dataPermOptions;
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
}
