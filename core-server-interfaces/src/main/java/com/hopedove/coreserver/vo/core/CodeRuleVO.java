package com.hopedove.coreserver.vo.core;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@ApiModel("基础编码规则实体")
public class CodeRuleVO extends BasicVO {

    private Integer id;

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

    @ApiModelProperty("是否包含天")
    private Boolean hasDay;

    private String prefixYear;

    private String prefixMonth;

    private String prefixDay;

    private String seqKey;

    public String getSeqKey() {
        return seqKey;
    }

    public void setSeqKey(String seqKey) {
        this.seqKey = seqKey;
    }

    public Boolean getHasDay() {
        return hasDay;
    }

    public void setHasDay(Boolean hasDay) {
        this.hasDay = hasDay;
    }

    public String getPrefixDay() {
        return prefixDay;
    }

    public void setPrefixDay(String prefixDay) {
        this.prefixDay = prefixDay;
    }

    public String getPrefixYear() {
        return prefixYear;
    }

    public void setPrefixYear(String prefixYear) {
        this.prefixYear = prefixYear;
    }

    public String getPrefixMonth() {
        return prefixMonth;
    }

    public void setPrefixMonth(String prefixMonth) {
        this.prefixMonth = prefixMonth;
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
