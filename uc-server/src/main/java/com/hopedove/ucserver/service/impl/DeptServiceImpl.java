package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.UserUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IDeptDao;
import com.hopedove.ucserver.service.IDeptService;
import com.hopedove.ucserver.vo.DeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "部门资源服务")
@RestController
public class DeptServiceImpl implements IDeptService {

    private final static Logger log = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Autowired
    private IDeptDao deptDao;

    @ApiOperation(value = "查询部门列表", notes = "支持分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "部门名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "部门类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/depts")
    public RestPageResponse<List<DeptVO>> getDept(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
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
        param.put("factoryId", UserUtil.get("factoryId", Integer.class));
        param.put("name", name);
        param.put("type", type);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = deptDao.getDeptsCount(param);
            page.setPage_total(count);
        }

        List<DeptVO> datas = deptDao.getDepts(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @ApiOperation(value = "查询部门", notes = "根据部门主键查询，返回单条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门主键", required = true, dataType = "Integer", paramType = "path"),
    })
    @GetMapping("/depts/{id}")
    public RestResponse<DeptVO> getDept(@PathVariable Integer id) {
        DeptVO node = deptDao.getDept(id);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "新增部门", notes = "新增一条部门数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body"),
    })
    @PostMapping("/depts")
    public RestResponse<Integer> addDept(@RequestBody DeptVO param) {
        param.setFactoryId(UserUtil.get("factoryId", Integer.class));
        VOUtil.fillCreate(param);

        Integer node = deptDao.addDept(param);

        return new RestResponse<>(node);
    }

    @ApiOperation(value = "修改部门", notes = "根据部门主键查询更新部门数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门主键", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body"),
    })
    @PutMapping("/depts/{id}")
    public RestResponse<Integer> modifyDept(@PathVariable Integer id, @RequestBody DeptVO param) {
        VOUtil.fillUpdate(param);
        param.setId(id);

        Integer node = deptDao.modifyDept(param);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "删除部门", notes = "根据部门主键删除部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门主键", required = true, dataType = "Integer", paramType = "path"),
    })
    @DeleteMapping("/depts/{id}")
    public RestResponse<Integer> removeDept(@PathVariable Integer id) {
        Integer node = deptDao.removeDept(id);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "查询部门树", notes = "暂不支持搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "factoryId", value = "用户所属工厂主键", required = false, dataType = "Integer", paramType = "query")
    })
    @GetMapping("/depts/tree")
    public RestResponse<List<DeptVO>> getDeptsTree(
            @RequestParam(required = false) Integer factoryId) {

        Map<String, Object> param = new HashMap<>();
        param.put("factoryId", factoryId);

        List<DeptVO> datas = deptDao.getDeptsTree(param);

        //返回
        return new RestResponse<>(datas);
    }
}
