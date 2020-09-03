package com.hopedove.ucserver.vo.node;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("节点表")
public class NodesVO extends BasicVO {
    private static final long serialVersionUID = 2L;

    @ApiModelProperty("节点编号")
    private Integer addr;
    @ApiModelProperty("0禁用 1启用")
    private String delete;

    private String name;//名称
    private String descript;//说明

    @ApiModelProperty("安装位置")
    private String InstallPos;

    private String serialserver_ip;//串口服务器IP
    private Integer pdcid;//配电柜序号
    private Integer serialserver_port;//串口服务器端口


    @ApiModelProperty("故障标志位，T失败，F成功，D离线")
    private String  ErrFlag;

    @ApiModelProperty("雷击电流报警设定值，单位：A")
    private Integer TCurrentAlarm;

    @ApiModelProperty("温度报警设定值，单位：摄氏度")
    private Integer TAlarm;

    @ApiModelProperty("温升限值设定值，单位：摄氏度")
    private Integer TRiseMax;

    @ApiModelProperty("漏电流限值，单位：A")
    private Integer LCurrentMax;

    @ApiModelProperty("脱扣，E:开启 D:关闭")
    private String  BOut;

    @ApiModelProperty("开关量1，E:开启 D:关闭")
    private String  Switch1;

    @ApiModelProperty("开关量2，E:开启 D:关闭")
    private String  Switch2;

    @ApiModelProperty("开关量3，E:开启 D:关闭")
    private String  Switch3;

    @ApiModelProperty("开关量4，E:开启 D:关闭")
    private String  Switch4;

    @ApiModelProperty("漏电流1，E:开启 D:关闭")
    private String  TCurrent1;

    @ApiModelProperty("漏电流2，E:开启 D:关闭")
    private String  TCurrent2;

    @ApiModelProperty("漏电流3，E:开启 D:关闭")
    private String  TCurrent3;


    public Integer getAddr() {
        return addr;
    }

    public void setAddr(Integer addr) {
        this.addr = addr;
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

    public String getSerialserver_ip() {
        return serialserver_ip;
    }

    public void setSerialserver_ip(String serialserver_ip) {
        this.serialserver_ip = serialserver_ip;
    }

    public Integer getPdcid() {
        return pdcid;
    }

    public void setPdcid(Integer pdcid) {
        this.pdcid = pdcid;
    }

    public Integer getSerialserver_port() {
        return serialserver_port;
    }

    public void setSerialserver_port(Integer serialserver_port) {
        this.serialserver_port = serialserver_port;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getInstallPos() {
        return InstallPos;
    }

    public void setInstallPos(String installPos) {
        InstallPos = installPos;
    }

    public String getErrFlag() {
        return ErrFlag;
    }

    public void setErrFlag(String errFlag) {
        ErrFlag = errFlag;
    }

    public Integer getTCurrentAlarm() {
        return TCurrentAlarm;
    }

    public void setTCurrentAlarm(Integer TCurrentAlarm) {
        this.TCurrentAlarm = TCurrentAlarm;
    }

    public Integer getTAlarm() {
        return TAlarm;
    }

    public void setTAlarm(Integer TAlarm) {
        this.TAlarm = TAlarm;
    }

    public Integer getTRiseMax() {
        return TRiseMax;
    }

    public void setTRiseMax(Integer TRiseMax) {
        this.TRiseMax = TRiseMax;
    }

    public Integer getLCurrentMax() {
        return LCurrentMax;
    }

    public void setLCurrentMax(Integer LCurrentMax) {
        this.LCurrentMax = LCurrentMax;
    }

    public String getBOut() {
        return BOut;
    }

    public void setBOut(String BOut) {
        this.BOut = BOut;
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

    public String getTCurrent1() {
        return TCurrent1;
    }

    public void setTCurrent1(String TCurrent1) {
        this.TCurrent1 = TCurrent1;
    }

    public String getTCurrent2() {
        return TCurrent2;
    }

    public void setTCurrent2(String TCurrent2) {
        this.TCurrent2 = TCurrent2;
    }

    public String getTCurrent3() {
        return TCurrent3;
    }

    public void setTCurrent3(String TCurrent3) {
        this.TCurrent3 = TCurrent3;
    }
}
