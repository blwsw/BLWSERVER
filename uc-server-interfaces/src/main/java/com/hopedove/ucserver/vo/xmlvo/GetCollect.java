package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "seqno",
        "SubItem",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="GetCollect")
public class GetCollect {

    @XmlAttribute
    private String seqno;

    private List<SubItem> SubItem;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public List<SubItem> getSubItem() {
        return SubItem;
    }

    public void setSubItem(List<SubItem> subItem) {
        this.SubItem = subItem;
    }
}
