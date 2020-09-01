package com.hopedove.ucserver.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("验证码实体")
public class VcodeFormVO implements Serializable {

    /**
     * 验证码实体
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("验证码")
    private String vcode;

    @ApiModelProperty("工厂编码")
    private String factoryCode;

    @ApiModelProperty("用户账号")
    private String username;


    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
