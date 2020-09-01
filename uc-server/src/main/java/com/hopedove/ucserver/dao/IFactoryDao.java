package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.FactoryVO;

import java.util.List;
import java.util.Map;

public interface IFactoryDao {

    List<FactoryVO> getFactories(Map<String, Object> param);

    int getFactoriesCount(Map<String, Object> param);

    FactoryVO getFactory(FactoryVO factoryVO);

    int addFactory(FactoryVO factoryVO);

    int modifyFactory(FactoryVO factoryVO);
}
