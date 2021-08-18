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
    private Integer addr;

    @ApiModelProperty("序列号")
    private Integer seqNo;
    private String name;//名称
    private String descript;//说明

    @ApiModelProperty("安装位置")
    private String InstallPos;
    private String pdcNo;//配电箱号

    @ApiModelProperty("故障标志位，T有故障，F无故障，D离线")
    private String ErrFlag;

    @ApiModelProperty("接地电阻警报 0正常，1警报")
    private String ErrR;

    @ApiModelProperty("雷击故障代码，00正常01预警10报警")
    private String  ErrThunder;

    @ApiModelProperty("温度故障代码，00正常01预警10报警")
    private String ErrTemp;

    @ApiModelProperty("温度劣化故障代码，00正常01预警10报警")
    private String ErrTempLeihua;

    @ApiModelProperty("漏电劣化1故障代码，00正常01预警10报警")
    private String ErrLCLeihua1;

    @ApiModelProperty("漏电劣化2故障代码，00正常01预警10报警")
    private String ErrLCLeihua2;

    @ApiModelProperty("漏电劣化3故障代码，00正常01预警10报警")
    private String ErrLCLeihua3;

    @ApiModelProperty("录入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime In_Time;


    @ApiModelProperty("温度劣化故障代码，00正常01预警10报警")
    private String   ErrLeihua;

    @ApiModelProperty("漏电劣化1故障代码，00正常01预警10报警")
    private String   ErrLC1;

    @ApiModelProperty("漏电劣化2故障代码，00正常01预警10报警")
    private String   ErrLC2;

    @ApiModelProperty("漏电劣化3故障代码，00正常01预警10报警")
    private String   ErrLC3;
    @ApiModelProperty("开关量1，0关，1开")
    private String   Switch1;

    @ApiModelProperty("开关量2，0关，1开")
    private String   Switch2;

    @ApiModelProperty("开关量3，0关，1开")
    private String   Switch3;

    @ApiModelProperty("开关量4，0关，1开")
    private String   Switch4;
    public Integer getAddr() {
        return addr;
    }

    public void setAddr(Integer addr) {
        this.addr = addr;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getErrFlag() {
        return ErrFlag;
    }

    public void setErrFlag(String errFlag) {
        ErrFlag = errFlag;
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

    public LocalDateTime getIn_Time() {
        return In_Time;
    }

    public void setIn_Time(LocalDateTime in_Time) {
        In_Time = in_Time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getInstallPos() {
        return InstallPos;
    }

    public void setInstallPos(String installPos) {
        InstallPos = installPos;
    }

    public String getPdcNo() {
        return pdcNo;
    }

    public void setPdcNo(String pdcNo) {
        this.pdcNo = pdcNo;
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

    public String getErrR() {
        return ErrR;
    }

    public void setErrR(String errR) {
        ErrR = errR;
    }
}
