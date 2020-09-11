package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "seqno",
        "subItem",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="SetParams")
public class SetParams {//前端下发设备参数

    @XmlAttribute
    private String seqno;

    private SubItem subItem;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public SubItem getSubItem() {
        return subItem;
    }

    public void setSubItem(SubItem subItem) {
        this.subItem = subItem;
    }
}
