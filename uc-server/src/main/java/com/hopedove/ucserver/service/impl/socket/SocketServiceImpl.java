package com.hopedove.ucserver.service.impl.socket;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.*;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IEnterpriseDao;
import com.hopedove.ucserver.dao.IEventLogDao;
import com.hopedove.ucserver.dao.socket.ISocketDataDao;
import com.hopedove.ucserver.service.ISocketService;
import com.hopedove.ucserver.service.impl.node.NodesServiceImpl;
import com.hopedove.ucserver.service.impl.websocket.WebSocket;

import com.hopedove.ucserver.vo.EnterpriseVO;
import com.hopedove.ucserver.vo.EventLogVO;
import com.hopedove.ucserver.vo.UserVO;
import com.hopedove.ucserver.vo.node.RealVO;
import com.hopedove.ucserver.vo.xmlvo.SubItem;
import com.hopedove.ucserver.vo.xmlvo.UploadCollect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.stripStart;

@Api(tags = "Socket管理")
@RestController
@Transactional
public class SocketServiceImpl implements ISocketService {

    private final static Logger log = LoggerFactory.getLogger(SocketServiceImpl.class);

    @Value("${socket.port}")
    private Integer port;

    @Value("${socket.pool-keep}")
    private Integer poolKeep;

    @Value("${socket.pool-core}")
    private Integer poolCore;

    @Value("${socket.pool-max}")
    private Integer poolMax;

    @Value("${socket.pool-queue-init}")
    private Integer poolQueueInit;

    @Value("${socket.server-ip}")
    private String sererIp;

    @Value("${socket.server-port}")
    private Integer sererPort;

    @Autowired
    private IEventLogDao eventLogDao;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    ISocketDataDao iSocketDataDao;

    @ApiOperation(value = "server服务端", notes = "server服务端")// 使用该注解描述接口方法信息
    @GetMapping("/server")
    public RestResponse server() {
        try  {

            //创建一个服务器socket，即serversocket,指定绑定的端口，并监听此端口
            ServerSocket serverSocket = new ServerSocket(8888);
            //调用accept()方法开始监听，等待客户端的连接
            System.out.println("***服务器即将启动，等待客户端的连接***");
            Socket socket = serverSocket.accept();
            //获取输入流，并读入客户端的信息
            InputStream in = socket.getInputStream(); //字节输入流
            InputStreamReader inreader = new InputStreamReader(in); //把字节输入流转换为字符流
            BufferedReader br = new BufferedReader(inreader); //为输入流添加缓冲
            String info = null;
            while((info = br.readLine())!=null){
                System.out.println("我是服务器，客户端说："+info);

            }
            socket.shutdownInput();//关闭输入流

            //获取输出流，相应客户端的信息
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);//包装为打印流
            printWriter.write("欢迎您！");
            printWriter.flush(); //刷新缓冲
            socket.shutdownOutput();

            //关闭资源
            printWriter.close();
            outputStream.close();

            br.close();
            inreader.close();
            in.close();
            socket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.返回
        return new RestPageResponse<>();
    }

    @PostMapping("/add/event/logs")
    public Integer addSendClientLog(@RequestParam(required = false)String xmlData,@RequestParam(required = false) byte type,@RequestParam(required = false) String seqNo){
        EventLogVO eventLogVO = new EventLogVO();
        eventLogVO.setEventType(type+"");
        eventLogVO.setRequestBody(xmlData);
        eventLogVO.setSeqNo(seqNo);
        eventLogVO.setRequestTime(LocalDateTime.now());
        eventLogVO.setStatus("0");
        this.eventLogDao.addEventLog(eventLogVO);
        return eventLogVO.getId();
    }

    public void addSendClientLog(String xmlData,byte type){
        EventLogVO eventLogVO = new EventLogVO();
        eventLogVO.setEventType(type+"");
        eventLogVO.setRequestBody(xmlData);
        eventLogVO.setRequestTime(LocalDateTime.now());
        this.eventLogDao.addEventLog(eventLogVO);
//        String seqNo = this.getSeqNo(eventLogVO.getId());
//        eventLogVO.setId(eventLogVO.getId());
//        eventLogVO.setSeqNo(seqNo);
//        eventLogVO.setResponseBody("ok");
//        this.modifyEventLog(seqNo,eventLogVO);
    }

    @GetMapping("/client")
    public RestResponse client(String xmlData,byte type) {
        RestResponse response = new RestResponse();

        //记录日志
       // this.addSendClientLog(xmlData,type);

        //创建客户端socket建立连接，指定服务器地址和端口
        try {
            if(sererPort == null){
                sererPort = 9006;
            }
            if(StringUtils.isEmpty(sererIp)){
                sererIp = "127.0.0.1";
            }
            Socket socket = new Socket(sererIp,sererPort);//port);
            //获取输出流，向服务器端发送信息
            OutputStream outputStream = socket.getOutputStream();//字节输出流
            PrintWriter pw = new PrintWriter(outputStream); //将输出流包装为打印流
            String instr = xmlData;

            byte[] b= instr.getBytes();
            int xmlleng=b.length;
            int pacLen= 4+4+1+4+xmlleng+4;
            byte[] content=new byte[pacLen];
            System.out.println("xmlleng="+xmlleng);
            System.out.println("pacLen="+pacLen);
            System.out.println("content="+new String(content));
            //包头　　　　　　
            content[0]=(byte) 0x11;
            content[1]=(byte) 0x22;
            content[2]=(byte) 0x33;
            content[3]=(byte) 0x44;
            //总长
            content[4] = (byte)(pacLen & 0xff);
            content[5] = (byte)((pacLen>>8) & 0xff);
            content[6] = (byte)((pacLen>>16) & 0xff);
            content[7] = (byte)((pacLen>>24) & 0xff);
            //消息类型
            content[8]=type;//(byte) 0x31;

            //XML流长度
            content[9] = (byte) (xmlleng & 0xff);
            content[10] = (byte) ((xmlleng >> 8) & 0xff);
            content[11] = (byte) ((xmlleng >> 16) & 0xff);
            content[12] = (byte) ((xmlleng >> 24) & 0xff);
         //xml内容
            for(int i=0;i<b.length;i++){
                content[i+13]=b[i];
            }
     //包尾
            content[pacLen-4] = (byte)  0xFF;
            content[pacLen-3] = (byte)  0xFF;
            content[pacLen-2] = (byte)  0xFF;
            content[pacLen-1] = (byte)  0xFF;

//            //传输字节流
//            DataOutputStream output=new DataOutputStream(socket.getOutputStream());
//            output.write(content, 0, content.length);
//            output.close();

           // System.out.println(byte2HexStr(content));
           // System.out.println(Hex.encodeHexStr(content));
//         //传输字节流
            outputStream.write(content, 0, content.length);
//            pw.write(content);
            pw.flush();
            socket.shutdownOutput();



            //获取输入流，读取服务器端的响应
            InputStream inputStream = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String info = null;
            while((info = br.readLine())!=null){
                System.out.println("我是客户端，服务器说："+info);

            }
            socket.shutdownInput();

            //关闭资源
            br.close();
            inputStream.close();
            pw.close();
            outputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            response.setMessage("socket 发送异常"+e.getMessage());
            response.setCode(500);
        }
        return response;
    }


    //创建一个交互日志
    @PostMapping("/eventLog")
    public RestResponse<Integer> addEventLog(@RequestBody EventLogVO eventLogVO){
        int node = this.eventLogDao.addEventLog(eventLogVO);
        return new RestResponse<>(eventLogVO.getId());
    }

    @GetMapping("/eventLog/entity")
    public RestResponse<EventLogVO> getEventLog(@RequestBody EventLogVO eventLogVO){
        eventLogVO = this.eventLogDao.getEventLog(eventLogVO);
        return new RestResponse<>(eventLogVO);
    }
    
    //更新一个交互日志
    @PutMapping("/eventLog}")
    public RestResponse<Integer> modifyEventLog(@PathVariable String seqNo, @RequestBody EventLogVO eventLogVO){
        int node = this.eventLogDao.modifyEventLog(eventLogVO);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "查询交互日志")
    @GetMapping("/eventLog")
    public RestPageResponse<List<EventLogVO>> getEventLogs(@RequestParam(required = false) String eventType,
                                                       @RequestParam(required = false) String seqNo,
                                                       @RequestParam(required = false) Integer currentPage,
                                                       @RequestParam(required = false) Integer pageSize,
                                                       @RequestParam(required = false) String sort){
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);
        Map<String, Object> param = new HashMap<>();
        if(StringUtils.isNotEmpty(eventType)){
            param.put("eventType", eventType);
        }

        param.put("seqNo", seqNo);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = this.eventLogDao.getEventLogCount(param);
            page.setPage_total(count);
        }
        param.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());
        List<EventLogVO> datas = this.eventLogDao.getEventLogs(param);
        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @GetMapping("/sendAllWebSocket")
    public String sendWebSocket(String jsonData) {
        String text="你们好！这是websocket群体发送！";
        log.debug("=sendWebSocket向前台发送信息=="+jsonData);
        if(StringUtils.isEmpty(jsonData)){
            jsonData = text;
        }
        webSocket.sendAllMessage(jsonData);
        return text;
    }

    @GetMapping("/sendOneWebSocket/{userName}")
    public String sendOneWebSocket(@PathVariable("userName") String userName) {
        String text=userName+" 你好！ 这是websocket单人发送！";
        webSocket.sendOneMessage(userName,text);
        return text;
    }
    //发送接收的数据
    @PostMapping("/new/real")
    public void sendRealNewData(@RequestBody UploadCollect uploadCollect){
        List<SubItem> subItems = uploadCollect.getSubItem();
        RealVO realVO = new RealVO();
        for(SubItem s:subItems){
            s.setSeqNo(uploadCollect.getSeqno());
            //this.eventLogDao.addSubitem(s);
            BeanUtils.copyProperties(s,realVO);
            realVO.setAddr(Integer.parseInt(s.getAddr()));
            realVO.setIn_Time(LocalDateTime.now());
            realVO.setOTemp(Integer.parseInt(s.getOTemp()));
            realVO.setTCurrent(Integer.parseInt(s.getTCurrent()));
            realVO.setLCurrent1(Integer.parseInt(s.getLCurrent1()));
            realVO.setLCurrent2(Integer.parseInt(s.getLCurrent2()));
            realVO.setLCurrent3(Integer.parseInt(s.getLCurrent3()));
           // this.iSocketDataDao.addReals(realVO);
            this.sendWebSocket(JsonUtil.writeValueAsString(realVO));
        }

        //this.sendWebSocket(JsonUtil.writeValueAsString(uploadCollect));
    }

    //获取实时的数据
    @GetMapping("/get/reals")
    public RestPageResponse<List<RealVO>> getReals(@RequestParam(required = false) Integer currentPage,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String sort){
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);
        Map<String, Object> param = new HashMap<>();

//        param.put("eventType", eventType);
//        param.put("seqNo", seqNo);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = this.iSocketDataDao.getRealsCount(param);
            page.setPage_total(count);
        }
        param.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());
        List<RealVO> datas = this.iSocketDataDao.getReals(param);
        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    public static void main(String[] args) {
//        byte[] content = new byte[4];
//        int pacLen = 135;
//        content[0] = (byte)(pacLen & 0xff);
//        content[1] = (byte)((pacLen>>8) & 0xff);
//        content[2] = (byte)((pacLen>>16) & 0xff);
//        content[3] = (byte)((pacLen>>24) & 0xff);
//        System.out.println(pacLen+"系统方法："+Integer.toHexString(pacLen));
//        System.out.println(byte2HexStr(content));
//        SubItem item = new SubItem();
//        item.setAddr("33");
//        GetCollect p = new GetCollect();
//        p.setSeq_no("4444444");
//        List<SubItem> items = new ArrayList<>();
//
//        items.add(item);
//        p.setSubItem(items);
//        String xml  =XMLParser.convertToXml(p);
//        System.out.println(xml);
//        byte type = (byte) 0x31;
//        SocketServiceImpl socketService = new SocketServiceImpl();
//        socketService.client(xml,type);

        NodesServiceImpl i = new NodesServiceImpl();
        System.out.println(i.getSeqNo(2));
         String a= "<UploadCollect seqno=\"20200812122023000001\">\n" +
                 "    <SubItem addr=\"1\">\n" +
                 "        <ErrFlag>F</ErrFlag>\n" +
                 "        <TCurrent>400</TCurrent>\n" +
                 "        <OTemp>15</OTemp>\n" +
                 "        <LCurrent1>15</LCurrent1>\n" +
                 "        <LCurrent2>15</LCurrent2>\n" +
                 "        <LCurrent3>15</LCurrent3>\n" +
                 "        <ErrThunder>00</ErrThunder>\n" +
                 "        <ErrTemp>00</ErrTemp>\n" +
                 "        <ErrTempLeihua>00</ErrTempLeihua>\n" +
                 "        <ErrLCLeihua1>00</ErrLCLeihua1>\n" +
                 "        <ErrLCLeihua2>00</ErrLCLeihua2>\n" +
                 "        <ErrLCLeihua3>00</ErrLCLeihua3>\n" +
                 "        <Switch1>0</Switch1>\n" +
                 "        <Switch2>0</Switch2>\n" +
                 "        <Switch3>0</Switch3>\n" +
                 "        <Switch4>0</Switch4>\n" +
                 "    </SubItem>\n" +
                 "</UploadCollect>\n";
        UploadCollect uploadCollect= (UploadCollect)XMLParser.convertXmlStrToObject(UploadCollect.class,a);
        System.out.println(JsonUtil.writeValueAsString(uploadCollect));
        String gh = "5z43535353453";
        if(StringUtils.isNotEmpty(gh) && gh.toUpperCase().startsWith("Z")) {
            System.out.println(gh);
        }
    }

    public static void dolsit(List<String> b){
        List<String>a = new ArrayList<>();
        a.add("2");
        b =a;
        System.out.println("3" + b.isEmpty());
    }


    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }


}
