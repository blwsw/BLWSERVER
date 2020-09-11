package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.*;
import com.hopedove.ucserver.vo.node.RealVO;
import com.hopedove.ucserver.vo.xmlvo.UploadCollect;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface ISocketService {

    //server
    @GetMapping("/server")
    RestResponse server();

    @GetMapping("/client")
    RestResponse client(String xmlData,byte type);

    //创建一个交互日志
    @PostMapping("/eventLog")
    RestResponse<Integer> addEventLog(@RequestBody EventLogVO eventLogVO);

    @GetMapping("/eventLog/entity")
    RestResponse<EventLogVO> getEventLog(@RequestBody EventLogVO eventLogVO);

    @PostMapping("/add/event/logs")
    Integer addSendClientLog(@RequestParam(required = false)String xmlData,@RequestParam(required = false) byte type,@RequestParam(required = false) String seqNo);

    //更新一个交互日志
    @PutMapping("/eventLog")
    RestResponse<Integer> modifyEventLog(@PathVariable String seqNo, @RequestBody EventLogVO eventLogVO);

    @ApiOperation(value = "查询交互日志")
    @GetMapping("/eventLog")
    RestPageResponse<List<EventLogVO>> getEventLogs(@RequestParam(required = false) String eventType, @RequestParam(required = false) String seqNo, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    @GetMapping("/sendAllWebSocket")
    public String sendWebSocket(String jsonData);

    @GetMapping("/sendOneWebSocket/{userName}")
    public String sendOneWebSocket(@PathVariable("userName") String userName);

    //发送接收的数据
    @PostMapping("/new/real")
    public void sendRealNewData(@RequestBody UploadCollect uploadCollect);

    //获取实时的数据
    @GetMapping("/get/reals")
    public RestPageResponse<List<RealVO>> getReals(@RequestParam(required = false) Integer currentPage,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String sort);

}
