package com.hopedove.ucserver.dao.node;
import com.hopedove.ucserver.vo.node.NodesVO;
import com.hopedove.ucserver.vo.node.PDCVO;

import java.util.List;
import java.util.Map;

public interface IPDCDao {

  List<PDCVO> getPCDs(Map<String, Object> param);

  int getPCDsCount(Map<String, Object> param);

  PDCVO getPCDsVO(PDCVO PCDsVO);

  int addPCDs(PDCVO PCDsVO);

  int modifyPCDs(PDCVO PCDsVO);

  int removePCDs(String code, String typeCode);
}