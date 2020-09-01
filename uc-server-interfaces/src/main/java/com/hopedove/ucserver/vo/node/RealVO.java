package com.hopedove.ucserver.vo.node;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel("实时数据表")
public class RealVO extends BasicVO {
    private static final long serialVersionUID = 3L;
    @ApiModelProperty("节点编号")
    private Integer  addr;

    @ApiModelProperty("序列号")
    private String  seqNo;

    @ApiModelProperty("故障标志位，T有故障，F无故障，D离线")
    private String   ErrFlag;

    @ApiModelProperty("雷击电流，单位：A")
    private Integer  TCurrent;

    @ApiModelProperty("外部温度，单位：摄氏度")
    private Integer  OTemp;

    @ApiModelProperty("漏电流1，单位：A")
    private Integer  LCurrent1;

    @ApiModelProperty("漏电流2，单位：A")
    private Integer  LCurrent2;

    @ApiModelProperty("漏电流3，单位：A")
    private Integer  LCurrent3;

    @ApiModelProperty("雷击故障代码，00正常01预警10报警")
    private String   ErrThunder;

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

    @ApiModelProperty("温度劣化故障代码，00正常01预警10报警")
    private String   ErrLeihua;

    @ApiModelProperty("漏电劣化1故障代码，00正常01预警10报警")
    private String   ErrLC1;

    @ApiModelProperty("漏电劣化2故障代码，00正常01预警10报警")
    private String   ErrLC2;

    @ApiModelProperty("漏电劣化3故障代码，00正常01预警10报警")
    private String   ErrLC3;

    @ApiModelProperty("雷击次数")
    private Integer  TTime;

    @ApiModelProperty("开关量1，0关，1开")
    private String   Switch1;

    @ApiModelProperty("开关量2，0关，1开")
    private String   Switch2;

    @ApiModelProperty("开关量3，0关，1开")
    private String   Switch3;

    @ApiModelProperty("开关量4，0关，1开")
    private String   Switch4;

    @ApiModelProperty("录入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime In_Time;


    private String pdcNo;//配电箱号

    public Integer getAddr() {
        return addr;
    }

    public void setAddr(Integer addr) {
        this.addr = addr;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getErrFlag() {
        return ErrFlag;
    }

    public void setErrFlag(String errFlag) {
        ErrFlag = errFlag;
    }

    public Integer getTCurrent() {
        return TCurrent;
    }

    public void setTCurrent(Integer TCurrent) {
        this.TCurrent = TCurrent;
    }

    public Integer getOTemp() {
        return OTemp;
    }

    public void setOTemp(Integer OTemp) {
        this.OTemp = OTemp;
    }

    public Integer getLCurrent1() {
        return LCurrent1;
    }

    public void setLCurrent1(Integer LCurrent1) {
        this.LCurrent1 = LCurrent1;
    }

    public Integer getLCurrent2() {
        return LCurrent2;
    }

    public void setLCurrent2(Integer LCurrent2) {
        this.LCurrent2 = LCurrent2;
    }

    public Integer getLCurrent3() {
        return LCurrent3;
    }

    public void setLCurrent3(Integer LCurrent3) {
        this.LCurrent3 = LCurrent3;
    }

    public String getErrThunder() {
        return ErrThunder;
    }

    public void setErrThunder(String errThunder) {
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

    public String getSwitch1() {
        return Switch1;
    }

    public void setSwitch1(String switch1) {
        Switch1 = switch1;
    }

    public String getSwitch2() {
        return Switch2;
    }

    public void setSwitch2(String switch2) {
        Switch2 = switch2;
    }

    public String getSwitch3() {
        return Switch3;
    }

    public void setSwitch3(String switch3) {
        Switch3 = switch3;
    }

    public String getSwitch4() {
        return Switch4;
    }

    public void setSwitch4(String switch4) {
        Switch4 = switch4;
    }

    public LocalDateTime getIn_Time() {
        return In_Time;
    }

    public void setIn_Time(LocalDateTime in_Time) {
        In_Time = in_Time;
    }

    public String getErrLeihua() {
        return ErrLeihua;
    }

    public void setErrLeihua(String errLeihua) {
        ErrLeihua = errLeihua;
    }

    public String getErrLC1() {
        return ErrLC1;
    }

    public void setErrLC1(String errLC1) {
        ErrLC1 = errLC1;
    }

    public String getErrLC2() {
        return ErrLC2;
    }

    public void setErrLC2(String errLC2) {
        ErrLC2 = errLC2;
    }

    public String getErrLC3() {
        return ErrLC3;
    }

    public void setErrLC3(String errLC3) {
        ErrLC3 = errLC3;
    }

    public Integer getTTime() {
        return TTime;
    }

    public void setTTime(Integer TTime) {
        this.TTime = TTime;
    }

    public String getPdcNo() {
        return pdcNo;
    }

    public void setPdcNo(String pdcNo) {
        this.pdcNo = pdcNo;
    }
}
