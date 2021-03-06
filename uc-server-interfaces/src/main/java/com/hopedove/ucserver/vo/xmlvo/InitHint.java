package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "seqno",
        "CFGPath",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="InitHint")
//1.4  0x34前端通知服务初始化（XMLInitHint）
public class InitHint {

    @XmlAttribute
    private String seqno;

    private String CFGPath;//提供配置文件的地址，服务会去下载并初始化

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getCFGPath() {
        return CFGPath;
    }

    public void setCFGPath(String CFGPath) {
        this.CFGPath = CFGPath;
    }
}
