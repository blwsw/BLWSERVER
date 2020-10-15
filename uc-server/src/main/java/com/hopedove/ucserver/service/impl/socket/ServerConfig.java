package com.hopedove.ucserver.service.impl.socket;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.LocalDateTimeUtil;
import com.hopedove.commons.utils.XMLParser;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.xmlvo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author wsj
 * @Description
 * @create 2019-04-14 23:21
 */
public class ServerConfig extends Thread {
    private final static Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    private Socket socket;
    public int [] messageType ={49,50,51};
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

    private String handle(InputStream inputStream) throws IOException, ParserConfigurationException, Exception {
        int inSize = 0;
        while (inSize == 0) {
            inSize = inputStream.available();
        }
        byte[] bytes = new byte[inSize];
        logger.debug(" inputStream.available();="+ inSize);
        int len = inputStream.read(bytes);
        if (len != -1) {
            //前4位包头 4位总长，1位类型，4位xml长度  xml内容。4位包尾
            byte btype = bytes[8];//类型
            int type =(int) (btype & 0xFF);
            System.out.println(type);
            logger.debug("type="+type);
            logger.debug("bytes.length="+bytes.length);
//          bytes：byte源数组
//          srcPos：截取源byte数组起始位置（0位置有效）
//          dest,：byte目的数组（截取后存放的数组）
//          destPos：截取后存放的数组起始位置（0位置有效）
//          length：截取的数据长度
            byte[] xmllengArr = new byte[4];
            System.arraycopy(bytes, 9, xmllengArr, 0, 4);
            int xmlLent =bytesToInt(xmllengArr,0);
            logger.debug("xmlLent="+xmlLent);
            byte[]xmlb = new byte[xmlLent];
            System.arraycopy(bytes, 13, xmlb, 0, xmlLent);

            StringBuffer request = new StringBuffer();
            request.append(new String(xmlb, 0, xmlLent, "UTF-8"));

            //异步处理消息内容
            new Thread(() -> {
                this.dispatch(btype,request.toString());
            }).start();
            //System.out.println("接受的数据: " + request);
            //System.out.println("from client ... " + request + "当前线程" + Thread.currentThread().getName());
            //System.out.println("处理的数据" + request.toString());
           // this.service.modifyEventLog(seqNo,eventLogVO);
            return "数据接收成功";
        } else {
            throw new BusinException("500","数据处理异常");
        }
    }

    @Override
    public void run() {
        BufferedWriter writer = null;
        try {
            // 设置连接超时9秒
            socket.setSoTimeout(9000);
            System.out.println("客户 - " + socket.getRemoteSocketAddress() + " -> 机连接成功");

            InputStream inputStream = socket.getInputStream();

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String result = null;
            try {
                result = handle(inputStream);
                writer.write(result);
                writer.newLine();
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
                writer.write("error");
                writer.newLine();
                writer.flush();
                System.out.println("发生异常");
                //记录日志
                EventLogVO eventLogVO = new EventLogVO();
                eventLogVO.setResponseTime(LocalDateTime.now());
                int inSize = 0;
                while (inSize == 0) {
                    inSize = inputStream.available();
                }
                byte[] bytes = new byte[inSize];
                logger.debug(" inputStream.available();="+ inSize);
                int len = inputStream.read(bytes);
                String instr = new String(bytes, 0, inSize, "UTF-8");
                eventLogVO.setRequestBody(instr);
                eventLogVO.setResponseBody(e.getMessage());
                eventLogVO.setStatus("3");
                eventLogVO.setEventType("err");
                this.service.addEventLog(eventLogVO);
                try {
                    System.out.println("再次接受!");
                    result = handle(inputStream);
                    writer.write(result);
                    writer.newLine();
                    writer.flush();
                } catch (Exception  ex) {
                    ex.printStackTrace();
                    System.out.println("再次接受, 发生异常,连接关闭");

                }
            }
        } catch (SocketException socketException) {
            socketException.printStackTrace();
            try {
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public void dispatch(byte btype,String xmlData){
        int type = (int) (btype & 0xFF);
        String seqNo = "";
        if (type == 65){//0x41	表示服务反馈/推送采集数据
            UploadCollect uploadCollect= (UploadCollect)XMLParser.convertXmlStrToObject(UploadCollect.class,xmlData);
            seqNo = uploadCollect.getSeqno();
            this.service.sendRealNewData(uploadCollect);
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
            ClearFault clearFault= (ClearFault)XMLParser.convertXmlStrToObject(ClearFault.class,xmlData);
            seqNo = clearFault.getSeqno();
        }

        EventLogVO eventLogVO = new EventLogVO();
        eventLogVO.setSeqNo(seqNo);
        //
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
            RestResponse<Integer> restResponse = this.service.addEventLog(eventLogVO);
        }
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