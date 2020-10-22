package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.XMLParser;
import com.hopedove.ucserver.service.ISocketService;
import com.hopedove.ucserver.service.nodes.INodesService;
import com.hopedove.ucserver.vo.xmlvo.DTCollector;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    //每隔60秒执行一次
    @Scheduled(fixedDelay = 60000)
    public void testTasks() {
        log.debug("定时任务执行时间：" + dateFormat.format(new Date()));
        String seqNo = this.iNodesService.getSeqNo(1);
        DTCollector p = new DTCollector();
        //p.setDTCollector("");
        String xml  = XMLParser.convertToXml(p);
        byte type = (byte) 0x36;
        RestResponse response =iSocketService.client(xml,type);
        String codeRuleCache = stringRedisTemplate.opsForValue().get("blwheatxt01");
        if (StringUtils.isEmpty(codeRuleCache)) {
            //不存在信息，直接创建新的规则缓存
            stringRedisTemplate.opsForValue().set("blwheatxt01","1");
        } else {
            Long sequence =stringRedisTemplate.opsForValue().increment("blwheatxt01", 1L);
            if(sequence >10L){
                stringRedisTemplate.opsForValue().set("blwheatxt01","1");
                this.iNodesService.initHintNs(null);
            }
        }
        log.debug("response={}"+ JsonUtil.writeValueAsString(response));
    }
}
