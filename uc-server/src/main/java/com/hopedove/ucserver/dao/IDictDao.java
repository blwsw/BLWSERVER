package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.DictVO;

import java.util.List;
import java.util.Map;

public interface IDictDao {

    List<DictVO> getDicts(Map<String, Object> param);

    int getDictsCount(Map<String, Object> param);

    DictVO getDict(DictVO dictVO);

    int addDict(DictVO dictVO);

    int modifyDict(DictVO dictVO);

    int removeDict(String code,String typeCode);
}
