package com.hopedove.commons.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class GPBasicVO extends BasicVO {
    @ApiModelProperty("创建人ID")
    private Long CJRID;

    @ApiModelProperty("创建人")
    private String CJR;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate CJRQ;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime CJSJ;

    @ApiModelProperty("更新人ID")
    private Long GXRID;

    @ApiModelProperty("更新人")
    private String GXR;

    @ApiModelProperty("更新日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate GXRQ;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime GXSJ;

    @ApiModelProperty("删除标记")
    private Integer SCBJ;

    public Long getCJRID() {
        return CJRID;
    }

    public void setCJRID(Long CJRID) {
        this.CJRID = CJRID;
    }

    public String getCJR() {
        return CJR;
    }

    public void setCJR(String CJR) {
        this.CJR = CJR;
    }


    public LocalDateTime getCJSJ() {
        return CJSJ;
    }

    public void setCJSJ(LocalDateTime CJSJ) {
        this.CJSJ = CJSJ;
    }

    public Long getGXRID() {
        return GXRID;
    }

    public void setGXRID(Long GXRID) {
        this.GXRID = GXRID;
    }

    public String getGXR() {
        return GXR;
    }

    public void setGXR(String GXR) {
        this.GXR = GXR;
    }

    public LocalDate getCJRQ() {
        return CJRQ;
    }

    public void setCJRQ(LocalDate CJRQ) {
        this.CJRQ = CJRQ;
    }

    public LocalDate getGXRQ() {
        return GXRQ;
    }

    public void setGXRQ(LocalDate GXRQ) {
        this.GXRQ = GXRQ;
    }

    public LocalDateTime getGXSJ() {
        return GXSJ;
    }

    public void setGXSJ(LocalDateTime GXSJ) {
        this.GXSJ = GXSJ;
    }

    public Integer getSCBJ() {
        return SCBJ;
    }

    public void setSCBJ(Integer SCBJ) {
        this.SCBJ = SCBJ;
    }
}
