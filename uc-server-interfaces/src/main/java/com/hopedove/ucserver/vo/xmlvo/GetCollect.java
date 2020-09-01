package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "seq_no",
        "subItem",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="GetCollect")
public class GetCollect {

    @XmlAttribute
    private String seq_no;

    private List<SubItem> subItem;

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public List<SubItem> getSubItem() {
        return subItem;
    }

    public void setSubItem(List<SubItem> subItem) {
        this.subItem = subItem;
    }
}
