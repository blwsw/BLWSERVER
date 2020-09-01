package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("权限实体")
public class PermissionVO extends BasicVO {

    private Integer id;

    private String name;

    private String code;
    
    private Integer moduleId;

    @ApiModelProperty("可用数据权限类型字典编码字符串")
    private String dataPermOptions;

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

}
