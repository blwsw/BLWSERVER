package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("获取登录令牌的请求实体")
public class AuthTokenFormVO extends BasicVO {

    @ApiModelProperty("工厂编码")
    private String factoryCode;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("密码")
    private String password;
    
    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("验证码ID")
    private String codeId;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
