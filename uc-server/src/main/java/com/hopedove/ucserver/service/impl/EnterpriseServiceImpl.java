package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IEnterpriseDao;
import com.hopedove.ucserver.service.IEnterpriseService;
import com.hopedove.ucserver.vo.EnterpriseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "企业管理")
@RestController
@Transactional
public class EnterpriseServiceImpl implements IEnterpriseService {

    private final static Logger log = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    @Autowired
    private IEnterpriseDao enterpriseDao;

    @ApiOperation(value = "查询企业列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "企业编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/enterprises")
    @Override
    public RestPageResponse<List<EnterpriseVO>> getEnterprises(@RequestParam(required = false) String name, @RequestParam(required = false) String code, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if(currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("code", code);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if(page != null) {
            int count = enterpriseDao.getEnterprisesCount(param);
            page.setPage_total(count);
        }

        List<EnterpriseVO> datas = enterpriseDao.getEnterprises(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @GetMapping("/enterprises/{id}")
    @Override
    public RestResponse<EnterpriseVO> getEnterprise(@PathVariable Integer id) {
        EnterpriseVO param = new EnterpriseVO();
        param.setId(id);
        EnterpriseVO node = enterpriseDao.getEnterprise(param);
        return new RestResponse<>(node);
    }

    @PostMapping("/enterprises")
    @Override
    public RestResponse<Integer> addEnterprise(@RequestBody EnterpriseVO enterpriseVO) {
        String code = enterpriseVO.getCode();
        EnterpriseVO param = new EnterpriseVO();
        param.setCode(code);
        param = enterpriseDao.getEnterprise(param);
        if (null != param) {
            throw new BusinException(ErrorCode.EXP_CODE_EXIST);
        }
        enterpriseVO = (EnterpriseVO) VOUtil.fillCreate(enterpriseVO);
        enterpriseDao.addEnterprise(enterpriseVO);
        Integer node = enterpriseVO.getId();
        return new RestResponse<>(node);
    }

    @PutMapping("/enterprises/{id}")
    @Override
    public RestResponse<Integer> modifyEnterprises(@PathVariable Integer id, @RequestBody EnterpriseVO enterpriseVO) {
        String code = enterpriseVO.getCode();
        if (null != code && !"".equals(code)) {
            EnterpriseVO voById = new EnterpriseVO();
            voById.setId(id);
            voById = enterpriseDao.getEnterprise(voById);
            if (!code.equals(voById.getCode())) {
                EnterpriseVO param = new EnterpriseVO();
                param.setCode(code);
                param = enterpriseDao.getEnterprise(param);
                if (null != param) {
                    throw new BusinException(ErrorCode.EXP_CODE_EXIST);
                }
            }

        }
        enterpriseVO.setId(id);
        enterpriseVO = (EnterpriseVO) VOUtil.fillUpdate(enterpriseVO);
        Integer node = enterpriseDao.modifyEnterprise(enterpriseVO);
        return new RestResponse<>(node);
    }

    @DeleteMapping("/enterprises/{id}")
    @Override
    public RestResponse<Integer> removeEnterprises(@PathVariable Integer id) {
        EnterpriseVO param = new EnterpriseVO();
        param = (EnterpriseVO) VOUtil.fillUpdate(param);
        param.setId(id);
        param.setDisabled(1);
        Integer node = enterpriseDao.modifyEnterprise(param);
        return new RestResponse<>(node);
    }
}
