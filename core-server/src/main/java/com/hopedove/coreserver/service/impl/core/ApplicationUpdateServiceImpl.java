package com.hopedove.coreserver.service.impl.core;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.IResponse;
import com.hopedove.commons.response.Response;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.coreserver.dao.core.IApplicationUpdateDao;
import com.hopedove.coreserver.service.core.IApplicationUpdateService;
import com.hopedove.coreserver.vo.core.ApplicationBasicVO;
import com.hopedove.coreserver.vo.core.ApplicationUpdateVO;
import com.hopedove.ucserver.service.IFactoryService;
import com.hopedove.ucserver.vo.FactoryVO;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "应用更新服务")
@RestController
public class ApplicationUpdateServiceImpl implements IApplicationUpdateService {

    @Autowired
    private IApplicationUpdateDao applicationUpdateDao;

    @Autowired
    private IFactoryService factoryService;

    @GetMapping("/applications/updates")
    @Override
    public RestPageResponse<List<ApplicationUpdateVO>> getApplicationUpdates(@RequestParam(required = false) Integer factoryId,
                                                                             @RequestParam(required = false) String clientType,
                                                                             @RequestParam(required = false) String version,
                                                                             @RequestParam(required = false) String subVersion,
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
        param.put("factoryId", factoryId);
        param.put("clientType", clientType);
        param.put("version", version);
        param.put("subVersion", subVersion);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = applicationUpdateDao.getApplicationUpdatesCount(param);
            page.setPage_total(count);
        }

        List<ApplicationUpdateVO> datas = applicationUpdateDao.getApplicationUpdates(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @GetMapping("/applications/updates/{id}")
    @Override
    public RestResponse<ApplicationUpdateVO> getApplicationUpdates(@PathVariable Integer id) {
        ApplicationUpdateVO param = new ApplicationUpdateVO();
        param.setId(id);
        ApplicationUpdateVO node = applicationUpdateDao.getApplicationUpdate(param);
        return new RestResponse<>(node);
    }

    @PostMapping("/applications/updates")
    @Override
    public RestResponse<ApplicationUpdateVO> addApplicationUpdates(@RequestBody ApplicationUpdateVO param) {
        VOUtil.fillCreate(param);
        param.setState("1");
        Integer node = applicationUpdateDao.addApplicationUpdate(param);

        return new RestResponse<>(param);
    }

    @PutMapping("/applications/updates/{id}")
    @Override
    public RestResponse<ApplicationUpdateVO> modifyApplicationUpdates(@PathVariable Integer id, @RequestBody ApplicationUpdateVO param) {
        VOUtil.fillUpdate(param);
        param.setId(id);

        Integer node = applicationUpdateDao.modifyApplicationUpdate(param);
        return new RestResponse<>(param);
    }

    @DeleteMapping("/applications/updates/{id}")
    @Override
    public RestResponse<Integer> removeApplicationUpdates(@PathVariable Integer id) {
    	ApplicationUpdateVO param = new ApplicationUpdateVO();
        param.setId(id);
        ApplicationUpdateVO applicationUpdateVO = applicationUpdateDao.getApplicationUpdate(param);
        VOUtil.fillUpdate(applicationUpdateVO);
        applicationUpdateVO.setDisabled(1);
        Integer node = applicationUpdateDao.removeApplicationUpdate(applicationUpdateVO);
        return new RestResponse<>(node);
    }

    @GetMapping("/applications/updates/news")
    @Override
    public RestResponse<List<ApplicationUpdateVO>> getApplicationUpdatesNews(@RequestParam String factoryCode, @RequestParam String clientType) {
        RestPageResponse<List<FactoryVO>> factoryResponse = factoryService.getFactories(null, null, factoryCode, null, null, null);
        if(Response.isSuccess(factoryResponse)) {
            List<FactoryVO> factorys = factoryResponse.getResponseBody();
            FactoryVO factory = factorys.get(0);

            ApplicationUpdateVO updateParam = new ApplicationUpdateVO();
            updateParam.setFactoryId(factory.getId());
            updateParam.setClientType(clientType);
            List<ApplicationUpdateVO> updates = applicationUpdateDao.getApplicationUpdatesNews(updateParam);
            return new RestResponse<>(updates);
        } else {
            throw new BusinException(ErrorCode.EXP_NODATA);
        }

    }
}
