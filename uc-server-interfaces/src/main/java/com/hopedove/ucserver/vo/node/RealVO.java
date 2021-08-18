package com.hopedove.ucserver.vo.node;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel("实时数据表")
public class RealVO extends BasicVO {
    private static final long serialVersionUID = 3L;
    @ApiModelProperty("节点编号")
    private Integer  addr;

    @ApiModelProperty("主键")
    private Integer  Id;

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
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String In_Time;


    private String pdcNo;//配电箱号

    private String InstallPos;//安装地址

    @ApiModelProperty("劣化度，单位：%")
    private String Deterior;


    private String longitude;//经纬度

    private String latitude;//经纬度

    private String RVal;
    private String ErrR;
    @ApiModelProperty("产品id")
    private Integer PID;
    @ApiModelProperty("节点类型")
    private Integer NodeType;

    @ApiModelProperty("0禁用 1启用")
    private String delete;

    private String name;//名称
    private String descript;//说明

    private String prodName;
    private String serialserver_ip;//串口服务器IP
    private Integer pdcid;//配电柜序号
    private Integer serialserver_port;//串口服务器端口
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

    public String getIn_Time() {
        return In_Time;
    }

    public void setIn_Time(String in_Time) {
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

    public String getInstallPos() {
        return InstallPos;
    }

    public void setInstallPos(String installPos) {
        InstallPos = installPos;
    }

    public String getDeterior() {
        return Deterior;
    }

    public void setDeterior(String deterior) {
        Deterior = deterior;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getRVal() {
        return RVal;
    }

    public void setRVal(String RVal) {
        this.RVal = RVal;
    }

    public String getErrR() {
        return ErrR;
    }

    public void setErrR(String errR) {
        ErrR = errR;
    }

    public Integer getPID() {
        return PID;
    }

    public void setPID(Integer PID) {
        this.PID = PID;
    }

    public Integer getNodeType() {
        return NodeType;
    }

    public void setNodeType(Integer nodeType) {
        NodeType = nodeType;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
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

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
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
}
