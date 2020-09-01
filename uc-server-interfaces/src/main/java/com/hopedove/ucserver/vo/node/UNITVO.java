package com.hopedove.ucserver.vo.node;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("行政单位表")
public class UNITVO extends BasicVO {
    private static final long serialVersionUID = 2L;
    private Integer id;//序号
    private String name;//名称
    private String descript;//说明

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

}
