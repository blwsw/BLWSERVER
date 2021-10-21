package com.hopedove.ucserver.dao.node;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.node.NodesVO;
import com.hopedove.ucserver.vo.node.RealVO;
import com.hopedove.ucserver.vo.xmlvo.SubItem;

import java.util.List;
import java.util.Map;

public interface INodeDao {

  List<NodesVO> getNodes(Map<String, Object> param);

  int getNodesCount(Map<String, Object> param);

  NodesVO getNodesVO(NodesVO nodesVO);

  int addNodes(NodesVO nodesVO);

  int modifyNodes(NodesVO nodesVO);

  int removeNodes(NodesVO nodesVO);

  int addNodeParamsLY(NodesVO nodesVO);

  int addNodeParamsResister(NodesVO nodesVO);

  int removeNodeParams(NodesVO nodesVO);

  int removeNodeParamsLy(NodesVO nodesVO);

  List<RealVO> getxjztList(Map<String,Object> param);

    List<RealVO> getLYList(final Map<String, Object> param);
    
    int getLYListCount(final Map<String, Object> param);
    
    List<RealVO> getRESList(final Map<String, Object> param);
    int getRESListCount(final Map<String, Object> param);
}
