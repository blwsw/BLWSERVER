package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "seqno",
        "subItem",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="GetCollect")
public class GetCollect {

    @XmlAttribute
    private String seqno;

    private List<SubItem> subItem;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public List<SubItem> getSubItem() {
        return subItem;
    }

    public void setSubItem(List<SubItem> subItem) {
        this.subItem = subItem;
    }
}
