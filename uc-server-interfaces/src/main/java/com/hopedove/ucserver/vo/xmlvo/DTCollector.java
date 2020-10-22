package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "DTCollector",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="DTCollector")
public class DTCollector {

    private String DTCollector;

    public String getDTCollector() {
        return DTCollector;
    }

    public void setDTCollector(String DTCollector) {
        this.DTCollector = DTCollector;
    }
}
