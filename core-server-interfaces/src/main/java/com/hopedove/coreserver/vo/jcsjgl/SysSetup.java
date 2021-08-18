package com.hopedove.coreserver.vo.jcsjgl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
/**
 * 系统设置
 *
 */
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class SysSetup implements Serializable {

	private static final long serialVersionUID = 1752684280491624266L;

	@ApiModelProperty("主键id")
	private Long pkid;

	@ApiModelProperty("系统名称")
	private String systemName;

	@ApiModelProperty("小数精确位数")
	private Integer decimalPlaces;

	@ApiModelProperty("收银方式")
	private String terminalName;

	@ApiModelProperty("早餐开始时间")
	private String breakfastStartTime;

	@ApiModelProperty("早餐结束时间")
	private String breakfastEndTime;

	@ApiModelProperty("午餐开始时间")
	private String lunchStartTime;

	@ApiModelProperty("午餐结束时间")
	private String lunchEndTime;

	@ApiModelProperty("晚餐开始时间")
	private String dinnerStartTime;

	@ApiModelProperty("晚餐结束时间")
	private String dinnerEndTime;

	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("删除标记")
	private String disabled;

	@ApiModelProperty("创建人")
	private String createUserId;

	@ApiModelProperty("创建人姓名")
	private String createUserName;

	@ApiModelProperty("创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDatetime;

	@ApiModelProperty("创建IP")
	private String createIp;

	@ApiModelProperty("修改人ID")
	private String updateUserId;

	@ApiModelProperty("修改人姓名")
	private String updateUserName;

	@ApiModelProperty("修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateDatetime;

	@ApiModelProperty("修改IP")
	private String updateIp;
	
	@ApiModelProperty("充值最小金额")	
	private BigDecimal  minRechargeAmount;
	
	@ApiModelProperty("充值最大金额")
	private BigDecimal  maxRechargeAmount;
	
	@ApiModelProperty("消费最大金额")
	private BigDecimal  maxConsumptionAmount;
	
	

	public BigDecimal getMaxConsumptionAmount() {
		return maxConsumptionAmount;
	}

	public void setMaxConsumptionAmount(BigDecimal maxConsumptionAmount) {
		this.maxConsumptionAmount = maxConsumptionAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getMinRechargeAmount() {
		return minRechargeAmount;
	}

	public void setMinRechargeAmount(BigDecimal minRechargeAmount) {
		this.minRechargeAmount = minRechargeAmount;
	}

	public BigDecimal getMaxRechargeAmount() {
		return maxRechargeAmount;
	}

	public void setMaxRechargeAmount(BigDecimal maxRechargeAmount) {
		this.maxRechargeAmount = maxRechargeAmount;
	}

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getBreakfastStartTime() {
		return breakfastStartTime;
	}

	public void setBreakfastStartTime(String breakfastStartTime) {
		this.breakfastStartTime = breakfastStartTime;
	}

	public String getBreakfastEndTime() {
		return breakfastEndTime;
	}

	public void setBreakfastEndTime(String breakfastEndTime) {
		this.breakfastEndTime = breakfastEndTime;
	}

	public String getLunchStartTime() {
		return lunchStartTime;
	}

	public void setLunchStartTime(String lunchStartTime) {
		this.lunchStartTime = lunchStartTime;
	}

	public String getLunchEndTime() {
		return lunchEndTime;
	}

	public void setLunchEndTime(String lunchEndTime) {
		this.lunchEndTime = lunchEndTime;
	}

	public String getDinnerStartTime() {
		return dinnerStartTime;
	}

	public void setDinnerStartTime(String dinnerStartTime) {
		this.dinnerStartTime = dinnerStartTime;
	}

	public String getDinnerEndTime() {
		return dinnerEndTime;
	}

	public void setDinnerEndTime(String dinnerEndTime) {
		this.dinnerEndTime = dinnerEndTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public LocalDateTime getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(LocalDateTime createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public LocalDateTime getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(LocalDateTime updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public String getUpdateIp() {
		return updateIp;
	}

	public void setUpdateIp(String updateIp) {
		this.updateIp = updateIp;
	}
	
	

}
