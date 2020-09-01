package com.hopedove.ucserver.dao.socket;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.node.RealVO;
import com.hopedove.ucserver.vo.xmlvo.SubItem;

import java.util.List;
import java.util.Map;

public interface ISocketDataDao {

  List<RealVO> getReals(Map<String, Object> param);

  int getRealsCount(Map<String, Object> param);

  RealVO getReal(RealVO dictVO);

  int addReals(RealVO dictVO);

  int removeRealVO(String code, String typeCode);
}