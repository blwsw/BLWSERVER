package com.hopedove.coreserver.vo.core;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("基础编码规则生成日志实体")
public class CodeRuleLogVO extends BasicVO {

    private Integer id;

    private Integer codeRuleId;

    @ApiModelProperty("工厂ID")
    private Integer factoryId;

    @ApiModelProperty("编码类型")
    private String type;

    @ApiModelProperty("编码前缀")
    private String prefix;

    @ApiModelProperty("序列最大值")
    private Long maxSequence;

    @ApiModelProperty("当前序列值")
    private Long currentSequence;

    @ApiModelProperty("编码后缀")
    private String suffix;

    @ApiModelProperty("增长步长")
    private Long incrment;

    @ApiModelProperty("是否包含年份")
    private Boolean hasYear;

    @ApiModelProperty("是否包含月份")
    private Boolean hasMonth;

    @ApiModelProperty("是否包含日期")
    private String resultCode;

    public Integer getCodeRuleId() {
        return codeRuleId;
    }

    public void setCodeRuleId(Integer codeRuleId) {
        this.codeRuleId = codeRuleId;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Boolean getHasYear() {
        return hasYear;
    }

    public void setHasYear(Boolean hasYear) {
        this.hasYear = hasYear;
    }

    public Boolean getHasMonth() {
        return hasMonth;
    }

    public void setHasMonth(Boolean hasMonth) {
        this.hasMonth = hasMonth;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getMaxSequence() {
        return maxSequence;
    }

    public void setMaxSequence(Long maxSequence) {
        this.maxSequence = maxSequence;
    }

    public Long getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(Long currentSequence) {
        this.currentSequence = currentSequence;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getIncrment() {
        return incrment;
    }

    public void setIncrment(Long incrment) {
        this.incrment = incrment;
    }
}
