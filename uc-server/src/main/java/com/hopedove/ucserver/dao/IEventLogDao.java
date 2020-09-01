package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.DictVO;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.node.RealVO;
import com.hopedove.ucserver.vo.xmlvo.SubItem;

import java.util.List;
import java.util.Map;

public interface IEventLogDao {

    List<EventLogVO> getEventLogs(Map<String, Object> param);

    int getEventLogCount(Map<String, Object> param);

    EventLogVO getEventLog(EventLogVO dictVO);

    int addEventLog(EventLogVO dictVO);

    int modifyEventLog(EventLogVO dictVO);

    int removeEventLogVO(String code,String typeCode);

    int addSubitem(SubItem subItem);

    int addReal(RealVO realVO);

}
