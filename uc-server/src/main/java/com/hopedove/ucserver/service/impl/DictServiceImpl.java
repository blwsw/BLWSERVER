package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.vo.DictVO;
import com.hopedove.ucserver.vo.DictTypeVO;
import com.hopedove.ucserver.vo.MultipleDictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hopedove.ucserver.dao.IDictTypeDao;
import com.hopedove.ucserver.dao.IDictDao;
import com.hopedove.ucserver.service.IDictService;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;

@Api(tags = "数据字典管理")
@RestController
@Transactional
public class DictServiceImpl implements IDictService {

    private final static Logger log = LoggerFactory.getLogger(DictServiceImpl.class);

    @Autowired
    private IDictDao dictDao;
    @Autowired
    private IDictTypeDao dictTypeDao;

    @ApiOperation(value = "查询字典类型列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "类型名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "类型编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "hasDicts", value = "返回值是否携带字典列表", required = false, dataType = "Boolean", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/dictTypes")
    @Override
    public RestPageResponse<List<DictTypeVO>> getDictTypes(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) String code,
                                                           @RequestParam(required = false) Boolean hasDicts,
                                                           @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {

        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        code = code!=null?code.trim():code;
        name = name!=null?name.trim():name;
        param.put("name", name);
        param.put("code", code);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = dictTypeDao.getDictTypesCount(param);
            page.setPage_total(count);
        }

        List<DictTypeVO> datas = null;
        if (hasDicts != null && hasDicts) {
            datas = dictTypeDao.getDictTypeAndDicts(param);
        } else {
            datas = dictTypeDao.getDictTypes(param);
        }

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @ApiOperation(value = "查询数据字典列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "typeCode", value = "字典类型编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/dicts")
    @Override
    public RestPageResponse<List<DictVO>> getDicts(@RequestParam(required = false) String name, @RequestParam(required = false) String typeCode, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("typeCode", typeCode);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = dictDao.getDictsCount(param);
            page.setPage_total(count);
        }

        List<DictVO> datas = dictDao.getDicts(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @ApiOperation(value = "查询数据字典信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "字典编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "typeCode", value = "字典类型编码", required = true, dataType = "String", paramType = "path"),
    })
    @GetMapping("/dicts/{code}/{typeCode}")
    @Override
    public RestResponse<DictVO> getDict(@PathVariable String code, @PathVariable String typeCode) {
        DictVO param = new DictVO();
        param.setCode(code);
        param.setTypeCode(typeCode);
        DictVO node = dictDao.getDict(param);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "新增数据字典信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictVOs", value = "字典对象数组", required = true, dataType = "String", paramType = "body"),
    })
    @PostMapping("/dicts")
    @Override
    public RestResponse<Integer> addDict(@RequestBody MultipleDictVO multipleDictVO) {

        Integer node = dictDao.removeDict(null, multipleDictVO.getTypeCode());
        for (DictVO dictVO : multipleDictVO.getDictVOS()) {
            DictVO param = new DictVO();
            param.setCode(dictVO.getCode());
            param.setTypeCode(multipleDictVO.getTypeCode());
            dictVO.setTypeCode(multipleDictVO.getTypeCode());
            VOUtil.fillCreate(dictVO);
            dictDao.addDict(dictVO);
        }

        return new RestResponse<>(node);
    }

    @ApiOperation(value = "修改数据字典信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "字典编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "typeCode", value = "字典类型编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "dictVO", value = "字典对象", required = true, dataType = "String", paramType = "body"),
    })
    @PutMapping("/dicts/{code}/{typeCode}")
    @Override
    public RestResponse<Integer> modifyDict(@PathVariable String code, @PathVariable String typeCode, @RequestBody DictVO dictVO) {
        dictVO.setCode(code);
        dictVO.setTypeCode(typeCode);
        dictVO.setUpdateDatetime(LocalDateTime.now());
        Integer node = dictDao.modifyDict(dictVO);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "删除数据字典信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "字典编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "typeCode", value = "字典类型编码", required = true, dataType = "String", paramType = "path"),
    })
    @DeleteMapping("/dicts/{code}/{typeCode}")
    @Override
    public RestResponse<Integer> removeDict(@PathVariable String code, @PathVariable String typeCode) {
        DictVO dictVO = new DictVO();
        dictVO.setCode(code);
        dictVO.setTypeCode(typeCode);
        dictVO = (DictVO) VOUtil.fillUpdate(dictVO);
        dictVO.setDisabled(1);
        Integer node = dictDao.modifyDict(dictVO);
        return new RestResponse<>(node);
    }
}
