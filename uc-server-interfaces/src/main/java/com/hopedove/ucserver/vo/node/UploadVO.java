package com.hopedove.ucserver.vo.node;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("上传vo")
public class UploadVO {
    private static final long serialVersionUID = 3L;
    private String type;
    private List<NodesVO> dataList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NodesVO> getDataList() {
        return dataList;
    }

    public void setDataList(List<NodesVO> dataList) {
        this.dataList = dataList;
    }
}
