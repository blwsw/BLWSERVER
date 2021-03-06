package com.hopedove.ucserver.vo.xmlvo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "seqno",
        "SubItem",
        }) //控制JAXB 绑定类中属性和字段的排序
@XmlRootElement(name="UploadCollect")
public class UploadCollect {

    @XmlAttribute
    public String seqno;

    @XmlElement
    private List<SubItem> SubItem;


    public List<com.hopedove.ucserver.vo.xmlvo.SubItem> getSubItem() {
        return SubItem;
    }

    public void setSubItem(List<com.hopedove.ucserver.vo.xmlvo.SubItem> subItem) {
        SubItem = subItem;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }
}
