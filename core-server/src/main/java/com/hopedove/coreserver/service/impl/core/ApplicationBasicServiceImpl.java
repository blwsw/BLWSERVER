package com.hopedove.coreserver.service.impl.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.Response;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.UserUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.coreserver.dao.core.IApplicationBasicDao;
import com.hopedove.coreserver.service.core.IApplicationBasicService;
import com.hopedove.coreserver.service.core.IFileService;
import com.hopedove.coreserver.vo.core.ApplicationBasicVO;
import com.hopedove.coreserver.vo.core.FileVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RestController
public class ApplicationBasicServiceImpl implements IApplicationBasicService {

    private final static Logger log = LoggerFactory.getLogger(ApplicationBasicServiceImpl.class);

    @Autowired
    private IApplicationBasicDao applicationBasicDao;

    @Resource(name = "fileService")
    private IFileService fileService;

    @GetMapping("/applications")
    @Override
    public RestPageResponse<List<ApplicationBasicVO>> getApplications(@RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) String version,
                                                                      @RequestParam(required = false) String subVersion,
                                                                      @RequestParam(required = false) String factoryCode,
                                                                      @RequestParam(required = false) String clientType,
                                                                      @RequestParam(required = false) Integer currentPage,
                                                                      @RequestParam(required = false) Integer pageSize,
                                                                      @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("version", version);
        param.put("subVersion", subVersion);
        param.put("factoryCode", factoryCode);
        param.put("clientType", clientType);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = applicationBasicDao.getApplicationBasicsCount(param);
            page.setPage_total(count);
        }

        List<ApplicationBasicVO> datas = applicationBasicDao.getApplicationBasics(param);

        if(datas != null && datas.size() > 0) {
            for(ApplicationBasicVO vo : datas) {
                if(vo.getFileId() != null) {
                    RestResponse<String> response = fileService.getFileUrl(vo.getFileId());
                    if(Response.isSuccess(response)) {
                        vo.setFileUrl(response.getResponseBody());
                    }
                }
            }
        }
        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @GetMapping("/applications/{id}")
    @Override
    public RestResponse<ApplicationBasicVO> getApplicationBasic(@PathVariable Integer id) {
        ApplicationBasicVO param = new ApplicationBasicVO();
        param.setId(id);
        ApplicationBasicVO node = applicationBasicDao.getApplicationBasic(param);
        return new RestResponse<>(node);
    }

    @PostMapping("/applications")
    @Override
    public RestResponse<ApplicationBasicVO> addApplicationBasic(@RequestBody ApplicationBasicVO param) {
        VOUtil.fillCreate(param);

        Integer node = applicationBasicDao.addApplicationBasic(param);

        return new RestResponse<>(param);
    }

    @PutMapping("/applications/{id}")
    @Override
    public RestResponse<ApplicationBasicVO> modifyApplicationBasic(@PathVariable Integer id, @RequestBody ApplicationBasicVO param) {
        VOUtil.fillUpdate(param);
        param.setId(id);
        if(StringUtils.isEmpty(param.getFileName())) {
        	param.setFileId(null);
        }
        Integer node = applicationBasicDao.modifyApplicationBasic(param);
        return new RestResponse<>(param);
    }

    @DeleteMapping("/applications/{id}")
    @Override
    public RestResponse<Integer> removeApplicationBasic(@PathVariable Integer id) {
    	 ApplicationBasicVO param = new ApplicationBasicVO();
         param.setId(id);
         ApplicationBasicVO applicationBasicVO = applicationBasicDao.getApplicationBasic(param);
    	 VOUtil.fillUpdate(applicationBasicVO);
    	 applicationBasicVO.setDisabled(1);
        Integer node = applicationBasicDao.removeApplicationBasic(applicationBasicVO);
        return new RestResponse<>(node);
    }

    @GetMapping("/applications/last")
    @Override
    public RestResponse<ApplicationBasicVO> getApplicationBasicLast(HttpServletResponse response) {
        //查询出出最新的客户端信息
        ApplicationBasicVO applicationBasic = applicationBasicDao.getApplicationBasic(null);
        return new RestResponse<>(applicationBasic);
    }
}
