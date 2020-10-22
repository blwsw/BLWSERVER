package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "DTCollectorRet",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="DTCollectorRet")
public class DTCollectorRet {

    private String DTCollectorRet;

    public String getDTCollectorRet() {
        return DTCollectorRet;
    }

    public void setDTCollectorRet(String DTCollectorRet) {
        this.DTCollectorRet = DTCollectorRet;
    }
}
