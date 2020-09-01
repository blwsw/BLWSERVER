package com.hopedove.ucserver.dao;
import java.util.Map;
import com.hopedove.ucserver.vo.DictTypeVO;
import java.util.List;
public interface IDictTypeDao {

    List<DictTypeVO> getDictTypes(Map<String, Object> param);

    int getDictTypesCount(Map<String, Object> param);

    List<DictTypeVO> getDictTypeAndDicts(Map<String, Object> param);

}
