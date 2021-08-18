package com.hopedove.coreserver.service.impl.real;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.coreserver.dao.real.IRealDao;
import com.hopedove.coreserver.service.impl.common.RequestUtil;
import com.hopedove.coreserver.service.real.IRealService;
import com.hopedove.coreserver.vo.real.NodesVO;
import com.hopedove.coreserver.vo.real.RealVO;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "实时数据服务")
@RestController("realService")
public class RealServiceImpl implements IRealService {

    private final static Logger log = LoggerFactory.getLogger(RealServiceImpl.class);
    @Autowired
    private IRealDao iRealDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private String realsKey = "blwRealskey";

    @GetMapping("/reals")
    public RestPageResponse<List<RealVO>> getReals(@RequestParam(required = false) String queryString,
                                               @RequestParam(required = false) Integer currentPage,
                                               @RequestParam(required = false) Integer pageSize,
                                               @RequestParam(required = false) String sort){
        RestResponse<List<RealVO>> restResponse = new RestResponse<>();
        Map<String, Object> paramMap = RequestUtil.stringToMap(queryString);
        String nodeType = paramMap.get("nodeType").toString();
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);
        paramMap.put(QueryEnum.PAGES.getValue(), page);
        paramMap.put(QueryEnum.SORTS.getValue(), sort);

        String status ="";
        if(paramMap.containsKey("status")){
            status = paramMap.get("status").toString();
        }
        log.debug("status="+status);
        if(StringUtils.isNotEmpty(status) && !status.contains("1,2,3,4")){
            if(status.contains("1")){//正常
                paramMap.put("ztStaus","true");
            }
            if(status.contains("2")){//故障
                paramMap.put("gzStaus","true");
            }
            if(status.contains("3")){//报警
                paramMap.put("bjStaus","true");
            }
            if(status.contains("4")){//预警
                paramMap.put("yjStaus","true");
            }
        }

        List<RealVO> realVOList = null;
        if("ly".equals(nodeType)){
            //2.查询总记录数，用于计算出总分页数
            if (page != null) {
                int count = this.iRealDao.getRealsCountLy(paramMap);
                page.setPage_total(count);
            }
            paramMap.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());
            realVOList = this.iRealDao.getRealsLy(paramMap);
        }else{
            //2.查询总记录数，用于计算出总分页数
            if (page != null) {
                int count = this.iRealDao.getRealsCountResister(paramMap);
                page.setPage_total(count);
            }
            paramMap.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());
            realVOList =this.iRealDao.getRealsResister(paramMap);
        }
        return new RestPageResponse<>(realVOList, page);
    }

    @GetMapping("/nodes")
    public RestPageResponse<List<NodesVO>> getNodes(@RequestParam(required = false) String queryString,
                                                @RequestParam(required = false) Integer currentPage,
                                                @RequestParam(required = false) Integer pageSize,
                                                @RequestParam(required = false) String sort){
        RestPageResponse<List<NodesVO>> restResponse = new RestPageResponse<>();
        Map<String, Object> paramMap = RequestUtil.stringToMap(queryString);
        // String nodeType = paramMap.get("nodeType").toString();
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);
        paramMap.put(QueryEnum.PAGES.getValue(), page);
        paramMap.put(QueryEnum.SORTS.getValue(), sort);
        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = this.iRealDao.getNodesCount(paramMap);
            page.setPage_total(count);
        }
        paramMap.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());

        List<NodesVO> nodeVOList = this.iRealDao.getNodes(paramMap);

        restResponse.setResponseBody(nodeVOList);
        restResponse.setPage(page);
        return restResponse;
    }
    @GetMapping("/initData")
    public RestResponse<List<RealVO>> InitPageData(){
        Map<String, Object> paramMap = new HashMap<>();

        String realListString =  this.stringRedisTemplate.opsForValue().get("realVOList");
        List<RealVO> realVOList = this.iRealDao.getxjztList(paramMap);
        if(StringUtils.isNotEmpty(realListString)){
           // log.debug("realListString isNotEmpty : "+realListString);
            // List<RealVO> realVOList1 = JsonUtil.readValueList(realListString,RealVO.class);


        }else{
            this.stringRedisTemplate.opsForValue().set("realVOList",JsonUtil.writeValueAsString(realVOList));
        }
        return new RestResponse<>(realVOList);
    }

    @GetMapping("/getTimeTJ")
    public RestResponse<List<Map>> getTimeTJ(){
        Map<String, Object> paramMap = new HashMap<>();
        String realsHH = this.stringRedisTemplate.opsForValue().get("RealsHH");
        List<Map> realVOList = new ArrayList<>();
        if(StringUtils.isNotEmpty(realsHH)){
            realVOList = JsonUtil.readValueList(realsHH,Map.class);
        }
        String [] HHArr = {"01","02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        List<Map> retList = new ArrayList<>();
        for(String HH:HHArr){
            boolean haveExist = false;
            B:for (Map m:realVOList){
                if((m.get("HH")+"").equals(HH)){
                    retList.add(m);
                    haveExist =true;
                    break B;
                }
            }
            if(!haveExist){
                paramMap = new HashMap<>();
                paramMap.put("HH",HH);
                paramMap.put("zccount",0);
                paramMap.put("gzcount",0);
                paramMap.put("yjcount",0);
                paramMap.put("bjcount",0);
                retList.add(paramMap);
            }
        }

//      List<Map<String,Object>> realVOList = this.iRealDao.getRealsTJ(paramMap);
        return new RestResponse<>(retList);
    }

    @GetMapping("/dlist")
    public RestPageResponse<List<RealVO>> getDLists(@RequestParam(required = false) String queryString,
                                                    @RequestParam(required = false) Integer currentPage,
                                                    @RequestParam(required = false) Integer pageSize,
                                                    @RequestParam(required = false) String sort){
        RestPageResponse<List<RealVO>> restResponse = new RestPageResponse<>();
        Map<String, Object> paramMap = RequestUtil.stringToMap(queryString);

         String status ="";
         if(paramMap.containsKey("status")){
             status = paramMap.get("status").toString();
         }
         log.debug(status);
         if(StringUtils.isNotEmpty(status) && !status.contains("1,2,3,4")){
             if(status.contains("1")){//正常
                 paramMap.put("ztStaus","true");
             }
             if(status.contains("2")){//故障
                 paramMap.put("gzStaus","true");
             }
             if(status.contains("3")){//报警
                 paramMap.put("bjStaus","true");
             }
             if(status.contains("4")){//预警
                 paramMap.put("yjStaus","true");
             }
         }

        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);
        paramMap.put(QueryEnum.PAGES.getValue(), page);
        paramMap.put(QueryEnum.SORTS.getValue(), sort);
        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = this.iRealDao.getRealsCount(paramMap);
            page.setPage_total(count);
        }
        paramMap.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());

        List<RealVO> nodeVOList = this.iRealDao.getReals(paramMap);

        restResponse.setResponseBody(nodeVOList);
        restResponse.setPage(page);
        return restResponse;
    }

    @GetMapping("/clean/history")
    public RestResponse<Integer> cleanHistroy(@RequestParam(required = false) String PID){
        int i=0;
        if ("1".equals(PID)){
            i = this.iRealDao.cleanHistroyLY(null);
        }else{
            i = this.iRealDao.cleanHistroyresister(null);
        }


        return new RestResponse<>(i);
    }
}
