package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("部门实体")
public class DeptVO extends BasicVO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("工厂主键")
    private Integer factoryId;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门类型")
    private String type;

    @ApiModelProperty("上级部门主键")
    private Integer parentId;

    @ApiModelProperty("层级路径")
    private String path;

    @ApiModelProperty("层级路径（名称）")
    private String pathName;

    @ApiModelProperty("子节点")
    private List<DeptVO> subDepts;

    public List<DeptVO> getSubDepts() {
        return subDepts;
    }

    public void setSubDepts(List<DeptVO> subDepts) {
        this.subDepts = subDepts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
