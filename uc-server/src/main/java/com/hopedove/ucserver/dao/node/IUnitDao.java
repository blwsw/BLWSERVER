package com.hopedove.ucserver.dao.node;
import com.hopedove.ucserver.vo.node.PDCVO;

import java.util.List;
import java.util.Map;

public interface IUnitDao {

  List<PDCVO> getUnits(Map<String, Object> param);

  int getUnitsCount(Map<String, Object> param);

  PDCVO getUnitsVO(PDCVO UnitsVO);

  int addUnits(PDCVO UnitsVO);

  int modifyUnits(PDCVO UnitsVO);

  int removeUnits(String code, String typeCode);
}