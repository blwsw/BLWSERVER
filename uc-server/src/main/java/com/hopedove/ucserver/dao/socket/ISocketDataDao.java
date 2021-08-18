package com.hopedove.ucserver.dao.socket;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.node.HistoryVO;
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

  int getHistorysCount(Map<String, Object> param);

  List<HistoryVO> getHistorys(Map<String, Object> param);

  int addHistorys(HistoryVO historyVO);

  int removeHistory(HistoryVO historyVO);

  int addHistorysBatch(Map<String, Object> param);

  List<Map<String,Object>> getHistorysTJCount(Map<String, Object> param);

  int copyRealsHistroys(Map<String, Object> param);

  int copyRealsHistroys2(Map<String, Object> param);

  List<Map<String,Object>> getRealHisTJCount(Map<String, Object> param);

  Map<String,Object> getRealsHH(Map<String, Object> param);
}
