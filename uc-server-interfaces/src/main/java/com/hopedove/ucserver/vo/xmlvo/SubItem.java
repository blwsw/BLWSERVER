package com.hopedove.ucserver.vo.xmlvo;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="SubItem")
public class SubItem {

    @XmlAttribute
    private String addr;//addr为节点编号
    @XmlAttribute
    private String id;//节点ID

    private String PID;
    private String TCurrentAlarm;//TCurrentAlarm雷击电流报警设定值，单位：A
    private String TAlarm;//TAlarm温度报警设定值，单位：摄氏度
    private String TRiseMax;//TRiseMax温升限值设定值，单位：摄氏度
    private String LCurrentMax;//LCurrentMax漏电流限值，单位：A
    private String BOut;//脱扣，E:开启 D:关闭

    //Switch1~Switch4，4路开关量，E:开启 D:关闭
//    开关量1，0关，1开
//    开关量2，0关，1开
//    开关量3，0关，1开
//    开关量4，0关，1开
    private String Switch1;
    private String Switch2;
    private String Switch3;
    private String Switch4;
    private String Switch5;

    //TCurrent1~TCurrent4，4路漏电流，E:开启 D:关闭
    private String TCurrent1;
    private String TCurrent2;
    private String TCurrent3;
    private String TCurrent4;
    //
    private String ErrFlag;//故障标志位，T有故障，F无故障
    //雷击电流，单位：A
    private String TCurrent;
    private String OTemp;//外部温度，单位：摄氏度
    //漏电流1，单位：A
    //漏电流2，单位：A
    //漏电流3，单位：A
    private String LCurrent1;
    private String LCurrent2;
    private String LCurrent3;

    //雷击故障代码，00正常01预警10报警
    private String ErrThunder;
    //温度故障代码，00正常01预警10报警
    private String ErrTemp;
    //温度劣化故障代码，00正常01预警10报警
    private String ErrTempLeihua;

    //漏电劣化1故障代码，00正常01预警10报警
    private String ErrLCLeihua1;
    //漏电劣化2故障代码，00正常01预警10报警
    private String ErrLCLeihua2;
    //漏电劣化3故障代码，00正常01预警10报警
    private String ErrLCLeihua3;

    //OK表示设置成功，ERR表示设置失败
    private String OptFlag;
    //失败原因
    private String FaultHint;//

    private String seqNo;//序列号

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

    @ApiModelProperty("劣化度，单位：%")
    private String Deterior;

    @ApiModelProperty("接地电阻测量值，单位：欧姆")
    private String RVal;

    @ApiModelProperty("RAlarm，接地电阻报警值，单位：欧姆")
    private String RAlarm;

    private String ErrR;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTCurrentAlarm() {
        return TCurrentAlarm;
    }

    public void setTCurrentAlarm(String TCurrentAlarm) {
        this.TCurrentAlarm = TCurrentAlarm;
    }

    public String getTAlarm() {
        return TAlarm;
    }

    public void setTAlarm(String TAlarm) {
        this.TAlarm = TAlarm;
    }

    public String getTRiseMax() {
        return TRiseMax;
    }

    public void setTRiseMax(String TRiseMax) {
        this.TRiseMax = TRiseMax;
    }

    public String getLCurrentMax() {
        return LCurrentMax;
    }

    public void setLCurrentMax(String LCurrentMax) {
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

    public String getSwitch5() {
        return Switch5;
    }

    public void setSwitch5(String switch5) {
        Switch5 = switch5;
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

    public String getSwitch2() {
        return Switch2;
    }

    public void setSwitch2(String switch2) {
        Switch2 = switch2;
    }

    public String getErrFlag() {
        return ErrFlag;
    }

    public void setErrFlag(String errFlag) {
        ErrFlag = errFlag;
    }

    public String getTCurrent() {
        return TCurrent;
    }

    public void setTCurrent(String TCurrent) {
        this.TCurrent = TCurrent;
    }

    public String getOTemp() {
        return OTemp;
    }

    public void setOTemp(String OTemp) {
        this.OTemp = OTemp;
    }

    public String getLCurrent1() {
        return LCurrent1;
    }

    public void setLCurrent1(String LCurrent1) {
        this.LCurrent1 = LCurrent1;
    }

    public String getLCurrent2() {
        return LCurrent2;
    }

    public void setLCurrent2(String LCurrent2) {
        this.LCurrent2 = LCurrent2;
    }

    public String getLCurrent3() {
        return LCurrent3;
    }

    public void setLCurrent3(String LCurrent3) {
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

    public String getOptFlag() {
        return OptFlag;
    }

    public void setOptFlag(String optFlag) {
        OptFlag = optFlag;
    }

    public String getFaultHint() {
        return FaultHint;
    }

    public void setFaultHint(String faultHint) {
        FaultHint = faultHint;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getTCurrent4() {
        return TCurrent4;
    }

    public void setTCurrent4(String TCurrent4) {
        this.TCurrent4 = TCurrent4;
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

    public String getDeterior() {
        return Deterior;
    }

    public void setDeterior(String deterior) {
        Deterior = deterior;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getRVal() {
        return RVal;
    }

    public void setRVal(String RVal) {
        this.RVal = RVal;
    }

    public String getRAlarm() {
        return RAlarm;
    }

    public void setRAlarm(String RAlarm) {
        this.RAlarm = RAlarm;
    }

    public String getErrR() {
        return ErrR;
    }

    public void setErrR(String errR) {
        ErrR = errR;
    }
}
