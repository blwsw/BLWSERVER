package com.hopedove.ucserver.service.impl.node;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.LocalDateTimeUtil;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.XMLParser;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IEventLogDao;
import com.hopedove.ucserver.dao.node.INodeDao;
import com.hopedove.ucserver.dao.socket.ISocketDataDao;
import com.hopedove.ucserver.service.ISocketService;
import com.hopedove.ucserver.service.impl.websocket.WebSocket;
import com.hopedove.ucserver.service.nodes.INodesService;
import com.hopedove.ucserver.vo.EnterpriseVO;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.node.NodesVO;
import com.hopedove.ucserver.vo.node.RealVO;
import com.hopedove.ucserver.vo.xmlvo.SubItem;
import com.hopedove.ucserver.vo.xmlvo.UploadCollect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "Socket管理")
@RestController
@Transactional
public class NodesServiceImpl implements INodesService{

    private final static Logger logger = LoggerFactory.getLogger(NodesServiceImpl.class);
    @Autowired
    private INodeDao iNodeDao;

    //创建一个节点
    @PostMapping("/nodes")
    public RestResponse<Integer> addNodes(@RequestBody NodesVO nodesVO){
        this.iNodeDao.addNodes(nodesVO);
        return new RestResponse<>(nodesVO.getAddr());
    }

    //更新一个节点
    @PutMapping("/nodes")
    public RestResponse<Integer> modifyNodes(@PathVariable String seqNo, @RequestBody NodesVO nodesVO){
        this.iNodeDao.modifyNodes(nodesVO);
        return new RestResponse<>(nodesVO.getAddr());
    }

    @ApiOperation(value = "查询节点")
    @GetMapping("/nodes")
    public RestPageResponse<List<NodesVO>> getNodes(@RequestParam(required = false) String querySring, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort){
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);
        Map<String, Object> param = new HashMap<>();

        param.put("querySring", querySring);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = this.iNodeDao.getNodesCount(param);
            page.setPage_total(count);
        }
        param.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());
        List<NodesVO> datas = this.iNodeDao.getNodes(param);
        //3.返回
        return new RestPageResponse<>(datas, page);
    }

}
