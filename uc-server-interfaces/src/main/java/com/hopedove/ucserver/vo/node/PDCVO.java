package com.hopedove.ucserver.vo.node;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("配电柜表")
public class PDCVO extends BasicVO {
    private static final long serialVersionUID = 2L;
    private Integer id;//节点编号
    private String name;//名称
    private String descript;//说明
    private String unitid;//行政单位序号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }
}
