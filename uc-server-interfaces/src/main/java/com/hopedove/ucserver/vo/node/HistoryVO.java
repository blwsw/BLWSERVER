package com.hopedove.ucserver.vo.node;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel("历史信息表")
public class HistoryVO extends BasicVO {
    private static final long serialVersionUID = 3L;
    @ApiModelProperty("节点编号")
    private Integer  addr;

    @ApiModelProperty("序列号")
    private Integer  seq_no;

    @ApiModelProperty("故障标志位，T有故障，F无故障，D离线")
    private String   ErrFlag;

    @ApiModelProperty("雷击故障代码，00正常01预警10报警")
    private Integer  ErrThunder;

    @ApiModelProperty("温度故障代码，00正常01预警10报警")
    private String   ErrTemp;

    @ApiModelProperty("温度劣化故障代码，00正常01预警10报警")
    private String   ErrTempLeihua;

    @ApiModelProperty("漏电劣化1故障代码，00正常01预警10报警")
    private String   ErrLCLeihua1;

    @ApiModelProperty("漏电劣化2故障代码，00正常01预警10报警")
    private String   ErrLCLeihua2;

    @ApiModelProperty("漏电劣化3故障代码，00正常01预警10报警")
    private String   ErrLCLeihua3;

    @ApiModelProperty("录入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime In_Time;

    public Integer getAddr() {
        return addr;
    }

    public void setAddr(Integer addr) {
        this.addr = addr;
    }

    public Integer getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(Integer seq_no) {
        this.seq_no = seq_no;
    }

    public String getErrFlag() {
        return ErrFlag;
    }

    public void setErrFlag(String errFlag) {
        ErrFlag = errFlag;
    }

    public Integer getErrThunder() {
        return ErrThunder;
    }

    public void setErrThunder(Integer errThunder) {
        ErrThunder = errThunder;
    }

    public String getErrTemp() {
        return ErrTemp;
    }

    public void setErrTemp(String errTemp) {
        ErrTemp = errTemp;
    }

    public String getErrTempLeihua() {
        return ErrTempLeihua;
    }

    public void setErrTempLeihua(String errTempLeihua) {
        ErrTempLeihua = errTempLeihua;
    }

    public String getErrLCLeihua1() {
        return ErrLCLeihua1;
    }

    public void setErrLCLeihua1(String errLCLeihua1) {
        ErrLCLeihua1 = errLCLeihua1;
    }

    public String getErrLCLeihua2() {
        return ErrLCLeihua2;
    }

    public void setErrLCLeihua2(String errLCLeihua2) {
        ErrLCLeihua2 = errLCLeihua2;
    }

    public String getErrLCLeihua3() {
        return ErrLCLeihua3;
    }

    public void setErrLCLeihua3(String errLCLeihua3) {
        ErrLCLeihua3 = errLCLeihua3;
    }

    public LocalDateTime getIn_Time() {
        return In_Time;
    }

    public void setIn_Time(LocalDateTime in_Time) {
        In_Time = in_Time;
    }
}
