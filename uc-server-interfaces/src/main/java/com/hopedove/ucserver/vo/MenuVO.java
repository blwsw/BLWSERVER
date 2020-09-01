package com.hopedove.ucserver.vo;

import java.util.List;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("权限菜单信息")
public class MenuVO extends BasicVO {

    @ApiModelProperty("主键")
    private Integer id;

    private String name;

    private String code;

    private Integer parentId;

    private String router;

    private String path;

    private String pathName;

    private String tips;

    private String sortSeq;

    @ApiModelProperty("子节点")
    private List<MenuVO> subMenus;

//    public boolean isHasChildren() {
//        return subMenus != null && subMenus.size() > 0;
//    }

    public List<MenuVO> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<MenuVO> subMenus) {
        this.subMenus = subMenus;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
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

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getSortSeq() {
        return sortSeq;
    }

    public void setSortSeq(String sortSeq) {
        this.sortSeq = sortSeq;
    }
}
