package com.hopedove.coreserver.vo.core;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("基础编码规则分段实体")
public class CodeRuleSegmentVO extends BasicVO {

    private Integer id;

    @ApiModelProperty("编码规则ID")
    private Integer codeRuleId;

    @ApiModelProperty("工厂ID")
    private Integer factoryId;

    @ApiModelProperty("编码类型")
    private String type;

    @ApiModelProperty("编码前缀")
    private String prefix;

    @ApiModelProperty("序列最大值")
    private Long maxSequence;

    @ApiModelProperty("序列最小值")
    private Long minSequence;

    @ApiModelProperty("编码后缀")
    private String suffix;

    @ApiModelProperty("增长步长")
    private Long incrment;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("月份")
    private String month;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodeRuleId() {
        return codeRuleId;
    }

    public void setCodeRuleId(Integer codeRuleId) {
        this.codeRuleId = codeRuleId;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
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

    public Long getMinSequence() {
        return minSequence;
    }

    public void setMinSequence(Long minSequence) {
        this.minSequence = minSequence;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
