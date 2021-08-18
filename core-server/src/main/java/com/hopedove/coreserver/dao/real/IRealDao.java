package com.hopedove.coreserver.dao.real;

import com.hopedove.coreserver.vo.core.FileVO;
import com.hopedove.coreserver.vo.real.NodesVO;
import com.hopedove.coreserver.vo.real.RealVO;

import java.util.List;
import java.util.Map;

public interface IRealDao {

    List<RealVO> getRealsLy(Map<String,Object> param);

    int getRealsCountLy(Map<String,Object> param);

    int getRealsCountResister(Map<String,Object> param);

    List<RealVO> getRealsResister(Map<String,Object> param);

    List<NodesVO> getNodes(Map<String,Object> param);

    int getNodesCount(Map<String,Object> param);

    List<RealVO> getxjztList(Map<String,Object> param);

    List<Map<String,Object>> getRealsTJ(Map<String,Object> param);

    List<RealVO> getReals(Map<String,Object> param);

    int getRealsCount(Map<String,Object> param);

    int cleanHistroyLY(Map<String,Object> param);

    int cleanHistroyresister(Map<String,Object> param);
}
