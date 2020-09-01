package com.hopedove.ucserver.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("验证码实体")
public class VcodeVO implements Serializable {
    
	/**
	 * 验证码实体
	 */
	private static final long serialVersionUID = 1L;

	public VcodeVO() {
	}

	public VcodeVO(String vcode, String vid) {
		this.vcode = vcode;
		this.vid = vid;
	}

	@ApiModelProperty("验证码")
	private String vcode;
	
	@ApiModelProperty("验证码唯一序列")
    private String vid;

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}
}
