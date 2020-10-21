package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.XMLParser;
import com.hopedove.ucserver.service.ISocketService;
import com.hopedove.ucserver.service.impl.socket.SocketServiceImpl;
import com.hopedove.ucserver.service.nodes.INodesService;
import com.hopedove.ucserver.vo.xmlvo.SetParams;
import com.hopedove.ucserver.vo.xmlvo.XMLDTCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Scheduler {
    private final static Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    private ISocketService iSocketService;
    @Autowired
    private INodesService iNodesService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    //每隔2秒执行一次
    @Scheduled(fixedDelay = 5000)
    public void testTasks() {
        System.out.println("定时任务执行时间：" + dateFormat.format(new Date()));
        String seqNo = this.iNodesService.getSeqNo(1);
        XMLDTCollector p = new XMLDTCollector();
        p.setDTCollector("");
        String xml  = XMLParser.convertToXml(p);
        byte type = (byte) 0x36;
        RestResponse response =iSocketService.client(xml,type);
        log.debug("response={}"+ JsonUtil.writeValueAsString(response));
        if(response.getCode() ==500){//异常调用不通
            this.iNodesService.initHint(null);
        }
    }
}
