package com.hopedove.ucserver.vo.node;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("节点表")
public class NodesVO extends BasicVO {
    private static final long serialVersionUID = 2L;
    private Integer addr;//节点编号
    private String name;//名称
    private String descript;//说明
    private String serialserver_ip;//串口服务器IP
    private Integer pdcid;//配电柜序号
    private Integer serialserver_port;//串口服务器端口

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
}
