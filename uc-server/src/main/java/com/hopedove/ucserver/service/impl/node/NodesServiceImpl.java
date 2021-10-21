package com.hopedove.ucserver.service.impl.node;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.*;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IEventLogDao;
import com.hopedove.ucserver.dao.node.INodeDao;
import com.hopedove.ucserver.dao.node.IPDCDao;
import com.hopedove.ucserver.dao.socket.ISocketDataDao;
import com.hopedove.ucserver.service.ISocketService;
import com.hopedove.ucserver.service.impl.websocket.WebSocket;
import com.hopedove.ucserver.service.nodes.INodesService;
import com.hopedove.ucserver.vo.EnterpriseVO;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.node.NodesVO;
import com.hopedove.ucserver.vo.node.PDCVO;
import com.hopedove.ucserver.vo.node.RealVO;
import com.hopedove.ucserver.vo.node.UploadVO;
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
    @Autowired
    private IPDCDao ipdcDao;

    private String seqKey = "blwkey";

    //创建一个节点
    @PostMapping("/nodes")
    public RestResponse<Integer> addNodes(@RequestBody NodesVO nodesVO){
        NodesVO nodesVO1 = this.iNodeDao.getNodesVO(nodesVO);
        if(nodesVO1 != null){
            logger.debug("已存在节点编号为["+nodesVO.getAddr()+"]的设备节点");
            this.iNodeDao.modifyNodes(nodesVO);
            RestResponse<Integer> response = new RestResponse<Integer>();
            response.setMessage("已存在节点编号为["+nodesVO.getAddr()+"]的设备节点");
            return response;
        }else{
            this.iNodeDao.addNodes(nodesVO);
        }

        return new RestResponse<>(nodesVO.getAddr());
    }
    //创建一个节点
    @PostMapping("/nodes/batch")
    public RestResponse<Integer> addNodesBatch(@RequestBody UploadVO uploadVO){

        List<NodesVO> nodesVOs = uploadVO.getDataList();
        String type = uploadVO.getType();
        if(StringUtils.isEmpty(type)){
            return  new RestResponse<>(500);
        }
        if("node".equals(type)){
            this.iNodeDao.removeNodes(null);
        }else if("LY".equals(type)){
            this.iNodeDao.removeNodeParamsLy(null);
        }else if("Params".equals(type)){
            this.iNodeDao.removeNodeParams(null);
        }

        List<PDCVO> pdcvos = this.ipdcDao.getProducts(null);
        Map<String,String> pMap = new HashMap<>();
        for(PDCVO p:pdcvos){
            pMap.put(p.getName(),p.getpId()+"");
        }
        if(nodesVOs != null && nodesVOs.size() >0){
            for(NodesVO nodesVO:nodesVOs){

                if(nodesVO.getAddr() == null){
                    continue;
                }
//                if(pMap.containsKey(nodesVO.getProdName())){
//                    nodesVO.setPID(Integer.parseInt(pMap.get(nodesVO.getProdName())));
//                }else{
//                    PDCVO p = new PDCVO();
//                    p.setName(nodesVO.getProdName());
//                    int Pid = this.ipdcDao.addPCDs(p);
//                    nodesVO.setPID(Pid);
//                    pMap.put(p.getName(),Pid+"");
//                }
                if("node".equals(type)){
                    this.iNodeDao.addNodes(nodesVO);
                }else if("LY".equals(type)){
                    this.iNodeDao.addNodeParamsLY(nodesVO);
                }else if("Params".equals(type)){
                    if(nodesVO.getRAlarm() == null || StringUtils.isEmpty(nodesVO.getRAlarm())){
                        nodesVO.setRAlarm("0");
                    }
                    this.iNodeDao.addNodeParamsResister(nodesVO);
                }
            }
        }
        return new RestResponse<>(2);
    }
    //更新一个节点
    @PutMapping("/nodes/{addr}")
    public RestResponse<Integer> modifyNodes(@PathVariable String addr, @RequestBody NodesVO nodesVO){
        this.iNodeDao.modifyNodes(nodesVO);
        return new RestResponse<>(nodesVO.getAddr());
    }

    @ApiOperation(value = "查询节点")
    @GetMapping("/nodes")
    public RestPageResponse<List<NodesVO>> getNodes(@RequestParam(required = false) String queryString, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort){
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);
        Map<String, Object> param = new HashMap<>();

        param.put("queryString", queryString);
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

        //item.setAddr(nodesVO.getAddr().toString());
        item.setId(nodesVO.getID().toString());
        item.setPID(nodesVO.getPID().toString());
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

        List<RealVO> realVOList = this.iNodeDao.getxjztList(null);
        if(realVOList != null && !realVOList.isEmpty()){
            this.stringRedisTemplate.opsForValue().set("realVOList",JsonUtil.writeValueAsString(realVOList));
        }

        return restResponse;
    }

    @PutMapping("nodes/initHint/ns")
    public RestResponse<Integer> initHintNs(@RequestBody NodesVO NodesVO){
        RestResponse<Integer> restResponse = new RestResponse<>();
        InitHint initHint = new InitHint();
        String seqNo = this.getSeqNo(1);
        initHint.setSeqno(seqNo);
        String xml  =XMLParser.convertToXml(initHint);
        byte type = (byte) 0x34;
        this.iSocketService.addSendClientLogNoS(xml,type,seqNo);//记录日志
        RestResponse response =iSocketService.client(xml,type);
        if(response.getCode() !=200){
            restResponse.setCode(response.getCode());
            restResponse.setMessage(response.getMessage());
            EventLogVO eventLogVO = new EventLogVO();
            eventLogVO.setSeqNo(seqNo);
            eventLogVO.setResponseBody(response.getMessage());
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
                subItem.setId(addr);
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
                subItem.setId(addr);
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

    @PutMapping({ "nodes/compare/reals" })
    public RestResponse<Integer> compareReals() {
        final List<RealVO> realVOList = this.iNodeDao.getxjztList(null);
        final String realListString = (String)this.stringRedisTemplate.opsForValue().get((Object)"realVOList");
        if (realVOList != null && !realVOList.isEmpty() && StringUtils.isEmpty((CharSequence)realListString)) {
            this.stringRedisTemplate.opsForValue().set("realVOList", JsonUtil.writeValueAsString(realVOList));
            return (RestResponse<Integer>)new RestResponse();
        }
        List<RealVO> sessionRealVOList = new ArrayList<RealVO>();
        if (org.apache.commons.lang.StringUtils.isNotEmpty(realListString)) {
            sessionRealVOList = (List<RealVO>)JsonUtil.readValueList(realListString, (Class)RealVO.class);
        }
        boolean isbh = false;
        for (final RealVO r : realVOList) {
            boolean isnotexits = true;
            for (final RealVO sr : sessionRealVOList) {
                if (r.getId() == sr.getId()) {
                    isnotexits = false;
                    if (StringUtils.isNotEmpty((CharSequence)sr.getIn_Time())) {
                        sr.setIn_Time(sr.getIn_Time().substring(0, 19));
                    }
                    if (StringUtils.isNotEmpty((CharSequence)r.getIn_Time())) {
                        r.setIn_Time(r.getIn_Time().substring(0, 19));
                    }
                    if (LocalDateTimeUtil.convertStringToLDT(sr.getIn_Time()).isBefore(LocalDateTimeUtil.convertStringToLDT(r.getIn_Time()))) {
                        BeanUtils.copyProperties((Object)r, (Object)sr);
                        isbh = true;
                        break;
                    }
                    break;
                }
            }
            if (isnotexits) {
                sessionRealVOList.add(r);
                isbh = true;
            }
        }
        if (isbh) {
            this.stringRedisTemplate.opsForValue().set("realVOList", JsonUtil.writeValueAsString((Object)sessionRealVOList));
        }
        return (RestResponse<Integer>)new RestResponse();
    }

    @GetMapping({ "/getAllReals" })
    public RestResponse<Map<String, List<RealVO>>> getAllReals() {
        final String currPageString = (String)this.stringRedisTemplate.opsForValue().get((Object)"currPage");
        NodesServiceImpl.logger.debug("currPage={}", (Object)currPageString);
        int currPage = 1;
        if (StringUtils.isEmpty((CharSequence)currPageString)) {
            currPage = 0;
        }
        else {
            currPage = Integer.parseInt(currPageString);
            ++currPage;
        }
        final int pageSize = 15;
        final int totalLy = this.iNodeDao.getLYListCount(null);
        int maxPage = totalLy / pageSize;
        final Map<String, Object> params = new HashMap<String, Object>();
        if (currPage * pageSize > totalLy) {
            params.put("page_startIndex", 0);
        }
        else {
            params.put("page_startIndex", currPage * pageSize);
        }
        params.put("page_pageSize", pageSize);
        final List<RealVO> lyList = this.iNodeDao.getLYList(params);
        final int totalre = this.iNodeDao.getRESListCount(null);
        if (currPage * pageSize > totalre) {
            params.put("page_startIndex", 0);
        }
        else {
            params.put("page_startIndex", currPage * pageSize);
        }
        if (maxPage < totalre / pageSize) {
            maxPage = totalre / pageSize;
        }
        if (currPage >= maxPage) {
            currPage = 0;
        }
        this.stringRedisTemplate.opsForValue().set("currPage", (currPage + ""));
        final List<RealVO> resList = this.iNodeDao.getRESList(params);
        final Map<String, List<RealVO>> retMap = new HashMap<String, List<RealVO>>();
        retMap.put("lyList", lyList);
        retMap.put("resList", resList);
        return (RestResponse<Map<String, List<RealVO>>>)new RestResponse((Object)retMap);
    }

    @GetMapping({ "/getRealsData" })
    public RestResponse<Map<String, Object>> getRealsData() {
        final RestResponse<List<Map<String, Object>>> retlist = (RestResponse<List<Map<String, Object>>>)this.iSocketService.getHistorysTJCount((String)null);
        final RestResponse<List<Map>> retHHData = (RestResponse<List<Map>>)this.iSocketService.getTimeTJ();
        final RestResponse<Map<String, List<RealVO>>> realResponse = this.getAllReals();
        final RestResponse<Map<String, Object>> restResponse = (RestResponse<Map<String, Object>>)this.iSocketService.getRealsNowData();
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("type", "TJDATA");
        paramMap.put("data", restResponse.getResponseBody());
        paramMap.put("hisData", retlist.getResponseBody());
        paramMap.put("HHData", retHHData.getResponseBody());
        paramMap.put("RealData", realResponse.getResponseBody());
        return (RestResponse<Map<String, Object>>)new RestResponse((Object)paramMap);
    }

    public static void main(final String[] args) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime end = LocalDateTimeUtil.convertStringToLDT("2021-08-20 15:25:30");
        System.out.println(now.isBefore(end));
    }
}
