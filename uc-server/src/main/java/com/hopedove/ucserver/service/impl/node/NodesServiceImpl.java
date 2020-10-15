package com.hopedove.ucserver.service.impl.node;

import com.hopedove.commons.exception.BusinException;
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
import com.hopedove.ucserver.vo.xmlvo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private IEventLogDao eventLogDao;
    @Autowired
    private ISocketService iSocketService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private String seqKey = "blwkey";

    //创建一个节点
    @PostMapping("/nodes")
    public RestResponse<Integer> addNodes(@RequestBody NodesVO nodesVO){
        NodesVO nodesVO1 = this.iNodeDao.getNodesVO(nodesVO);
        if(nodesVO1 != null){
            throw new BusinException("","已存在节点编号为["+nodesVO.getAddr()+"]的设备节点");
        }
        this.iNodeDao.addNodes(nodesVO);
        return new RestResponse<>(nodesVO.getAddr());
    }

    //更新一个节点
    @PutMapping("/nodes/{addr}")
    public RestResponse<Integer> modifyNodes(@PathVariable String addr, @RequestBody NodesVO nodesVO){
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

    //表示前端下发设备参数
    // 1.3  0x33前端下发设备参数（XMLSetParams）
    @PutMapping("/send/nodes/{seqNo}")
    public RestResponse<Integer> setParams(@PathVariable String seqNo, @RequestBody NodesVO nodesVO){
        RestResponse<Integer> restResponse = new RestResponse<>();
        SubItem item = new SubItem();
        BeanUtils.copyProperties(nodesVO,item);

        item.setAddr(nodesVO.getAddr().toString());
        item.setTCurrentAlarm(nodesVO.getTCurrentAlarm().toString());
        item.setTAlarm(nodesVO.getTAlarm().toString());
        item.setTRiseMax(nodesVO.getTRiseMax().toString());
        item.setLCurrentMax(nodesVO.getLCurrentMax().toString());
        seqNo = this.getSeqNo(1);
        SetParams p = new SetParams();
        p.setSeqno(seqNo);
        List<SubItem> items = new ArrayList<>();

        items.add(item);
        p.setSubItem(items);
        String xml  =XMLParser.convertToXml(p);
        byte type = (byte) 0x33;
        Integer eventLogId =this.iSocketService.addSendClientLog(xml,type,seqNo);//记录日志

        RestResponse response =iSocketService.client(xml,type);
        if(response.getCode() !=200){
            restResponse.setCode(response.getCode());
            restResponse.setMessage(response.getMessage());
            EventLogVO eventLogVO = new EventLogVO();
            eventLogVO.setSeqNo(seqNo);
            eventLogVO.setResponseBody(response.getMessage());
            eventLogVO.setId(eventLogId);
            eventLogVO.setStatus("3");//异常
            this.iSocketService.modifyEventLog(seqNo,eventLogVO);
        }
        return  restResponse;
    }
    //1.4  0x34前端通知服务初始化（XMLInitHint）
    @PutMapping("nodes/initHint")
    public RestResponse<Integer> initHint(@RequestBody NodesVO NodesVO){
        RestResponse<Integer> restResponse = new RestResponse<>();
        InitHint initHint = new InitHint();
        String seqNo = this.getSeqNo(1);
        initHint.setSeqno(seqNo);
        String xml  =XMLParser.convertToXml(initHint);
        byte type = (byte) 0x34;
        Integer eventLogId =this.iSocketService.addSendClientLog(xml,type,seqNo);//记录日志

        RestResponse response =iSocketService.client(xml,type);
        if(response.getCode() !=200){
            restResponse.setCode(response.getCode());
            restResponse.setMessage(response.getMessage());
            EventLogVO eventLogVO = new EventLogVO();
            eventLogVO.setSeqNo(seqNo);
            eventLogVO.setResponseBody(response.getMessage());
            eventLogVO.setId(eventLogId);
            eventLogVO.setStatus("3");//异常
            this.iSocketService.modifyEventLog(seqNo,eventLogVO);
        }
        return restResponse;
    }

    //1.5  0x35前端通知服务清除设备故障（XMLClearFault）
    @PutMapping("nodes/clearFault")
    public RestResponse<Integer> clearFault(@RequestParam(required = false) String addrs){
        RestResponse<Integer> restResponse = new RestResponse<>();
        ClearFault clearFault = new ClearFault();
        String seqNo = this.getSeqNo(1);
        clearFault.setSeqno(seqNo);

        if(StringUtils.isNotBlank(addrs)){
            String [] addrArr = addrs.split(",");
            List<SubItem> subItemList = new ArrayList<>();
            SubItem subItem =null;
            for(String addr : addrArr){
                subItem = new SubItem();
                subItem.setAddr(addr);
                subItemList.add(subItem);
            }
            clearFault.setSubItem(subItemList);
        }

        String xml  =XMLParser.convertToXml(clearFault);
        byte type = (byte) 0x35;

        Integer eventLogId =this.iSocketService.addSendClientLog(xml,type,seqNo);//记录日志

        RestResponse response =iSocketService.client(xml,type);
        if(response.getCode() !=200){
            restResponse.setCode(response.getCode());
            restResponse.setMessage(response.getMessage());
            EventLogVO eventLogVO = new EventLogVO();
            eventLogVO.setSeqNo(seqNo);
            eventLogVO.setResponseBody(response.getMessage());
            eventLogVO.setId(eventLogId);
            eventLogVO.setStatus("3");//异常
            this.iSocketService.modifyEventLog(seqNo,eventLogVO);
        }
        return restResponse;
    }
    // 1.2  0x32前端召唤设备参数（XMLGetParams）
    @PutMapping("nodes/getParams")
    public RestResponse<Integer> getParams(@RequestParam(required = false) String addrs){
        RestResponse<Integer> restResponse = new RestResponse<>();
        GetParams getParams = new GetParams();
        String seqNo = this.getSeqNo(1);
        getParams.setSeqno(seqNo);
        if(StringUtils.isNotBlank(addrs)){
            String [] addrArr = addrs.split(",");
            List<SubItem> subItemList = new ArrayList<>();
            SubItem subItem =null;
            for(String addr : addrArr){
                subItem = new SubItem();
                subItem.setAddr(addr);
                subItemList.add(subItem);
            }
            getParams.setSubItem(subItemList);
        }
        String xml  =XMLParser.convertToXml(getParams);
        byte type = (byte) 0x32;

        Integer eventLogId =this.iSocketService.addSendClientLog(xml,type,seqNo);//记录日志

        RestResponse response =iSocketService.client(xml,type);
        if(response.getCode() !=200){
            restResponse.setCode(response.getCode());
            restResponse.setMessage(response.getMessage());
            EventLogVO eventLogVO = new EventLogVO();
            eventLogVO.setSeqNo(seqNo);
            eventLogVO.setResponseBody(response.getMessage());
            eventLogVO.setId(eventLogId);
            eventLogVO.setStatus("3");//异常
            this.iSocketService.modifyEventLog(seqNo,eventLogVO);
        }
        return restResponse;
    }
    @GetMapping("get/seqno")
    public String getSeqNo(int id){
        String seqNo = LocalDateTimeUtil.formatTime(LocalDateTime.now(),"yyyyMMddHHmmSS");
        DecimalFormat df=new DecimalFormat("000000");
        String codeRuleCache = stringRedisTemplate.opsForValue().get(seqKey);
        String seqId="0";
        if (StringUtils.isEmpty(codeRuleCache)) {
            //不存在信息，直接创建新的规则缓存
            stringRedisTemplate.opsForValue().set(seqKey,seqId);
        } else {
            //已有信息的话，需要比对规则是否一致，如果不一致则覆盖。
            //注意这里不覆盖更新当前序列，因为会有并发问题
        }
        //获取当前序列
        Long sequence = stringRedisTemplate.opsForValue().increment(seqKey, 1L);
        seqNo+=df.format(sequence);
        return  seqNo;
    }
}
