package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.UserUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IFactoryRoleDao;
import com.hopedove.ucserver.service.IFactoryRoleService;
import com.hopedove.ucserver.vo.FactoryRoleFunctionVO;
import com.hopedove.ucserver.vo.FactoryRoleVO;
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

@Api(tags = "工厂角色管理")
@RestController
@Transactional
public class FactoryRoleServiceImpl implements IFactoryRoleService {

    private final static Logger log = LoggerFactory.getLogger(FactoryRoleServiceImpl.class);

    @Autowired
    private IFactoryRoleDao factoryRoleDao;
   
    @ApiOperation(value = "查询工厂角色列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "factoryId", value = "所属工厂id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "factoryName", value = "所属工厂名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleTemplateId", value = "角色模板id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "工作中心名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/factory/roles")
    @Override
    public RestPageResponse<List<FactoryRoleVO>> getUcFactoryRoles(@RequestParam(required = false) Integer factoryId,@RequestParam(required = false) String factoryName, @RequestParam(required = false) Integer roleTemplateId, @RequestParam(required = false) String name, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if(currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        if (null == factoryId) {
            factoryId = UserUtil.get("factoryId", Integer.class);
        }

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("roleTemplateId", roleTemplateId);
        param.put("factoryId", factoryId);
        param.put("factoryName", factoryName);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if(page != null) {
            int count = factoryRoleDao.getFactoryRolesCount(param);
            page.setPage_total(count);
        }

        List<FactoryRoleVO> datas = factoryRoleDao.getFactoryRoles(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @ApiOperation(value = "查询工厂角色信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工厂角色id",  dataType = "Integer", paramType = "path"),
    })
    @GetMapping("/factory/roles/{id}")
    @Override
    public RestResponse<FactoryRoleVO> getUcFactoryRole(@PathVariable Integer id) {
        FactoryRoleVO param = new FactoryRoleVO();
        param.setId(id);
        FactoryRoleVO node = factoryRoleDao.getFactoryRole(param);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "新增工厂角色信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "factoryRoleVO", value = "工厂角色对象", required = true, dataType = "String", paramType = "body"),
    })
    @PostMapping("/factory/roles")
    @Override
    public RestResponse<Integer> addUcFactoryRole(@RequestBody FactoryRoleVO factoryRoleVO) {
        if (null == factoryRoleVO.getFactoryId()) {
            Integer factoryId = UserUtil.get("factoryId", Integer.class);
            factoryRoleVO.setFactoryId(factoryId);
        }


        factoryRoleVO.setDisabled(0);
        factoryRoleVO.setCreateDatetime(LocalDateTime.now());
        factoryRoleVO.setUpdateDatetime(LocalDateTime.now());
        Integer node = factoryRoleDao.addFactoryRole(factoryRoleVO);

        if (factoryRoleVO.getRoleFunctions() != null && factoryRoleVO.getRoleFunctions().size() > 0) {
            for (FactoryRoleFunctionVO roleFunction : factoryRoleVO.getRoleFunctions()) {
                roleFunction.setRoleId(factoryRoleVO.getId());
                roleFunction.setRoleTemplateId(factoryRoleVO.getRoleTemplateId());
            }

            factoryRoleDao.batchAddFactoryRoleFunctions(factoryRoleVO.getRoleFunctions());
        }

        return new RestResponse<>(node);
    }

    @ApiOperation(value = "修改工厂角色信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工厂角色id", dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "FactoryRoleVO", value = "工厂角色对象", required = true, dataType = "String", paramType = "body"),
    })
    @PutMapping("/factory/roles/{id}")
    @Override
    public RestResponse<Integer> modifyUcFactoryRole(@PathVariable Integer id, @RequestBody FactoryRoleVO factoryRoleVO) {
        factoryRoleVO.setId(id);
        factoryRoleVO.setUpdateDatetime(LocalDateTime.now());

        FactoryRoleFunctionVO functionParam = new FactoryRoleFunctionVO();
        functionParam.setRoleId(id);
        factoryRoleDao.removeFactoryRoleFunction(functionParam);

        if (factoryRoleVO.getRoleFunctions() != null && factoryRoleVO.getRoleFunctions().size() > 0) {
            for (FactoryRoleFunctionVO roleFunction : factoryRoleVO.getRoleFunctions()) {
                roleFunction.setRoleId(id);
                roleFunction.setRoleTemplateId(factoryRoleVO.getRoleTemplateId());
            }
            factoryRoleDao.batchAddFactoryRoleFunctions(factoryRoleVO.getRoleFunctions());
        }

        Integer node = factoryRoleDao.modifyFactoryRole(factoryRoleVO);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "删除工厂角色信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工厂角色id",  dataType = "Integer", paramType = "path"),
    })
    @DeleteMapping("/factory/roles/{id}")
    @Override
    public RestResponse<Integer> removeUcFactoryRole(@PathVariable Integer id) {
        FactoryRoleFunctionVO functionParam = new FactoryRoleFunctionVO();
        functionParam.setRoleId(id);
        factoryRoleDao.removeFactoryRoleFunction(functionParam);

        Integer node = factoryRoleDao.removeFactoryRole(id);
        return new RestResponse<>(node);
    }

    @GetMapping("/factory/roles/{id}/functions")
    @Override
    public RestResponse<List<FactoryRoleFunctionVO>> getRoleFunctions(@PathVariable Integer id) {
        List<FactoryRoleFunctionVO> nodes = factoryRoleDao.getFactoryRoleFunctions(id);
        return new RestResponse<>(nodes);
    }
}
