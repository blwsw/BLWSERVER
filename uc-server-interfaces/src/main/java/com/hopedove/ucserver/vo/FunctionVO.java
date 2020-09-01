package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("数据功能权限实体")
public class FunctionVO extends BasicVO {

    private Integer id;

    private String code;

    private String name;

    private Integer moduleId;

    private String dataPermOptions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
