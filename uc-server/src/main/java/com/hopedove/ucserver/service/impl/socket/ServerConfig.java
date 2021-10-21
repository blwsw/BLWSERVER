package com.hopedove.ucserver.service.impl.socket;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.XMLParser;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.xmlvo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

/**
 * @author wsj
 * @Description
 * @create 2019-04-14 23:21
 */
public class ServerConfig extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    private Socket socket;
    public int [] messageType ={49,50,51};
    public byte[] allowbt = { 0x11,0x22,0x33,0x44};

    //消息类型(十六进制)	说明	XML格式
    //49= 0x31	表示前端召唤采集数据  XMLGetCollect
//        0x32	表示前端召唤设备参数	XMLGetParams
//        0x33	表示前端下发设备参数	XMLSetParams
//        0x34	表示前端通知服务初始化	XMLInitHint
//        0x41	表示服务反馈/推送采集数据  XMLUploadCollect
//        0x42	表示服务召唤设备参数反馈 XMLGetParamsRet
//        0x43	表示服务下发设备参数反馈  XMLSetParamsRet
//  56=  0x44	表示服务初始化反馈  XMLInitHintRet

    public ServerConfig(Socket socket) {
        this.socket = socket;
    }
    // 获取spring容器管理的类，可以获取到sevrice的类
    private SocketServiceImpl service = SpringUtil.getBean(SocketServiceImpl.class);
    private StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

    private String handle(InputStream inputStream) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.debug("handle--------------startTime="+startTime);
        int inSize = 0;
        int connectionCount =0;
        while (inSize == 0) {
            inSize = inputStream.available();
            connectionCount++;
            if(connectionCount >100){//防止非法访问
                break;
            }
        }
        byte[] bytes = new byte[inSize];
        logger.debug(" inputStream.available();="+ inSize);
        int len = inputStream.read(bytes);
        if (len != -1) {
            //前4位包头 4位总长，1位类型，4位xml长度  xml内容。4位包尾
            byte btype = bytes[8];//类型
            int type =(int) (btype & 0xFF);
            if(type < 65 || type >70){
                //又有SB攻击我了滚蛋
                logger.debug("非法访问类型不一致,抛出异常");
                throw new Exception("========");
            }
            System.out.println(type);
            logger.debug("type="+type);
            logger.debug("bytes.length="+bytes.length);
            //判断包头是否一致
            for(int i=0;i<allowbt.length;i++){
                if(allowbt[i] != bytes[i]){
                    logger.debug("非法访问包头不一致,抛出异常");
                    throw new Exception("========");
                }
            }
//          bytes：byte源数组
//          srcPos：截取源byte数组起始位置（0位置有效）
//          dest,：byte目的数组（截取后存放的数组）
//          destPos：截取后存放的数组起始位置（0位置有效）
//          length：截取的数据长度
            byte[] xmllengArr = new byte[4];
            System.arraycopy(bytes, 9, xmllengArr, 0, 4);
            int xmlLent =bytesToInt(xmllengArr,0);
            logger.debug("xmlLent="+xmlLent);

            byte[] allLenArr = new byte[4];
            System.arraycopy(bytes, 4, allLenArr, 0, 4);
            int allLent =bytesToInt(allLenArr,0);
            logger.debug("allLent="+allLent);
            if(allLent > 70000){//非正常请求
                logger.debug("非法访问请求内容过大,抛出异常");
                throw new Exception("========");
            }
            int btw =17;
            if((allLent-xmlLent) != btw){//非正常请求
                logger.debug("非法访问请求规则不对,抛出异常");
                throw new Exception("========");
            }

            byte[]xmlb = new byte[xmlLent];
            System.arraycopy(bytes, 13, xmlb, 0, xmlLent);

            StringBuffer request = new StringBuffer();
            request.append(new String(xmlb, 0, xmlLent, "UTF-8"));

            //处理消息内容
            this.dispatch(btype,request.toString());

            long endTime = System.currentTimeMillis();
            logger.debug("dispatch-formatdata-------------endTime-startTime(ms)="+(endTime-startTime));
            return "数据接收成功";
        } else {
            logger.error(" int len = inputStream.read(bytes); ==-1");
            throw new BusinException("500","数据处理异常");
        }

    }

    @Override
    public void run() {
        BufferedWriter writer = null;
        InputStream inputStream = null;
        long startTime = System.currentTimeMillis();
        logger.debug("ServerConfig-run--------------startTime="+startTime);
        try {
            // 设置连接超时5秒
            socket.setSoTimeout(5000);
            logger.debug("客户 - " + socket.getRemoteSocketAddress() + " -> 机连接成功");
            inputStream = socket.getInputStream();
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String result = this.handle(inputStream);
            writer.write(result);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            System.out.println("发生异常");
            try {
                writer.close();
                inputStream.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                writer.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                if(socket != null) socket.close(); //断开连接
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        logger.debug("ServerConfig-run-end-getData-------------endTime-startTime(ms)="+(endTime-startTime));
    }

    /**
     * 任务中心，
     * 0x31	表示前端召唤采集数据
     * 0x32	表示前端召唤设备参数
     * 0x33	表示前端下发设备参数
     * 0x34	表示前端通知服务初始化
     * 0x41	表示服务反馈/推送采集数据
     * 0x42	表示服务召唤设备参数反馈
     * 0x43	表示服务下发设备参数反馈
     * 0x44	表示服务初始化反馈
     */
    public void dispatch(byte btype,String xmlData) throws Exception{
        logger.debug("xmlData===="+xmlData);
        int type = (int) (btype & 0xFF);
        String seqNo = "";
        if (type == 65){//0x41	表示服务反馈/推送采集数据
            UploadCollect uploadCollect= (UploadCollect)XMLParser.convertXmlStrToObject(UploadCollect.class,xmlData);
            seqNo = uploadCollect.getSeqno();
            // this.service.sendRealNewData(uploadCollect);
        }else if(type == 66){//0x42	表示服务召唤设备参数反馈
            GetParamsRet getParamsRet= (GetParamsRet)XMLParser.convertXmlStrToObject(GetParamsRet.class,xmlData);
            seqNo = getParamsRet.getSeqno();
            this.service.sendNodeNewData(getParamsRet);
        }else if(type == 67){//0x43	表示服务下发设备参数反馈
            SetParamsRet setParamsRet= (SetParamsRet)XMLParser.convertXmlStrToObject(SetParamsRet.class,xmlData);
            seqNo = setParamsRet.getSeqno();
        }else if(type == 68){//0x44	表示服务初始化反馈
            InitHintRet initHintRet= (InitHintRet)XMLParser.convertXmlStrToObject(InitHintRet.class,xmlData);
            seqNo = initHintRet.getSeqno();
        }else if(type == 69){//0x45	0x45	表示服务清除设备故障反馈
            ClearFaultRet clearFault= (ClearFaultRet)XMLParser.convertXmlStrToObject(ClearFaultRet.class,xmlData);
            seqNo = clearFault.getSeqno();
        }else if(type == 70){//0x46	采集服务发送心跳（XMLDTCollectorRet）
            DTCollectorRet DTCollectorRet = (DTCollectorRet)XMLParser.convertXmlStrToObject(DTCollectorRet.class,xmlData);
            seqNo = DTCollectorRet.getDTCollectorRet();
            stringRedisTemplate.opsForValue().set("blwheatxt01","1");
        }

        EventLogVO eventLogVO = new EventLogVO();
        eventLogVO.setSeqNo(seqNo);
        //
        if (type != 65){
            RestResponse<EventLogVO> logResponse = this.service.getEventLog(eventLogVO);
            if(logResponse != null && logResponse.getResponseBody() != null){
                EventLogVO sendEventLog = logResponse.getResponseBody();
                sendEventLog.setStatus("1");//数据返回
                sendEventLog.setResponseBody(xmlData);
                sendEventLog.setResponseTime(LocalDateTime.now());
                this.service.modifyEventLog(sendEventLog.getSeqNo(),sendEventLog);
            }else{
                eventLogVO.setEventType(type+"");
                eventLogVO.setStatus("6");//数据返回
                eventLogVO.setResponseBody(xmlData);
                eventLogVO.setResponseTime(LocalDateTime.now());
//                RestResponse<Integer> restResponse = this.service.addEventLog(eventLogVO);
                this.service.addEventLogBatch(eventLogVO);
            }
        }
//        else{
//            eventLogVO.setEventType(type+"");
//            eventLogVO.setStatus("6");//数据返回
//            eventLogVO.setResponseBody(xmlData);
//            eventLogVO.setResponseTime(LocalDateTime.now());
//            //RestResponse<Integer> restResponse = this.service.addEventLog(eventLogVO);
//            this.service.addEventLogBatch(eventLogVO);
//        }
    }


    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }
}
