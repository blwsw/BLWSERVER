package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel("用于批量操作的字典实体")
public class MultipleDictVO extends BasicVO {

    private String typeCode;

    private List<DictVO> dictVOS;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public List<DictVO> getDictVOS() {
        return dictVOS;
    }

    public void setDictVOS(List<DictVO> dictVOS) {
        this.dictVOS = dictVOS;
    }
}
