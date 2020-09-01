package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.UserUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IRoleTemplateDao;
import com.hopedove.ucserver.service.IRoleTemplateService;
import com.hopedove.ucserver.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "角色模板资源服务")
@RestController
@Transactional
public class RoleTemplateServiceImpl implements IRoleTemplateService {

    @Autowired
    private IRoleTemplateDao roleTemplateDao;

    @ApiOperation(value = "查询角色模板列表", notes = "支持分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "noCode", value = "需要过滤掉的角色模板编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/roletemplates")
    @Override
    public RestPageResponse<List<RoleTemplateVO>> getRoleTemplates(@RequestParam(required = false) String name, @RequestParam(required = false) String noCode, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("noCode", noCode);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = roleTemplateDao.getRoleTemplatesCount(param);
            page.setPage_total(count);
        }

        List<RoleTemplateVO> datas = roleTemplateDao.getRoleTemplates(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @ApiOperation(value = "查询角色模板", notes = "根据主键查询，返回单条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Integer", paramType = "path"),
    })
    @GetMapping("/roletemplates/{id}")
    @Override
    public RestResponse<RoleTemplateVO> getRoleTemplate(@PathVariable Integer id) {
        RoleTemplateVO node = roleTemplateDao.getRoleTemplate(id);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "新增角色模板", notes = "新增一条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body"),
    })
    @PostMapping("/roletemplates")
    @Override
    public RestResponse<Integer> addRoleTemplate(@RequestBody RoleTemplateVO param) {
        VOUtil.fillCreate(param);

        Integer node = roleTemplateDao.addRoleTemplate(param);
//        node = param.getId();
        //保存关联的菜单资源
        if (param.getRoleTemplateMenus() != null && param.getRoleTemplateMenus().size() > 0) {
            roleTemplateDao.batchAddRoleTemplateMenus(param.getRoleTemplateMenus().stream().map((e) -> {
                e.setRoleTemplateId(param.getId());
                return e;
            }).collect(Collectors.toList()));
        }

        if (param.getRoleTemplateFunctions() != null && param.getRoleTemplateFunctions().size() > 0) {
            roleTemplateDao.batchRoleTemplateFunctions(param.getRoleTemplateFunctions().stream().map((e) -> {
                e.setRoleTemplateId(param.getId());
                return e;
            }).collect(Collectors.toList()));
        }

        //保存关联的权限资源
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "修改角色模板", notes = "根据主键更新数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body"),
    })
    @PutMapping("/roletemplates/{id}")
    @Override
    public RestResponse<Integer> modifyRoleTemplate(@PathVariable Integer id, @RequestBody RoleTemplateVO param) {
        VOUtil.fillUpdate(param);
        param.setId(id);

        Integer node = roleTemplateDao.modifyRoleTemplate(param);

        //保存关联的菜单资源
        if (param.getRoleTemplateMenus() != null && param.getRoleTemplateMenus().size() > 0) {
            for (RoleTemplateMenuVO roleTemplateMenuVO : param.getRoleTemplateMenus()) {
                roleTemplateMenuVO.setRoleTemplateId(id);
            }

            roleTemplateDao.removeRoleTemplateMenus(id);
            roleTemplateDao.batchAddRoleTemplateMenus(param.getRoleTemplateMenus());
        }else{
            roleTemplateDao.removeRoleTemplateMenus(id);
        }

        if (param.getRoleTemplateFunctions() != null && param.getRoleTemplateFunctions().size() > 0) {
            for (RoleTemplateFunctionVO roleTemplateFunctionVO : param.getRoleTemplateFunctions()) {
                roleTemplateFunctionVO.setRoleTemplateId(id);
            }

            roleTemplateDao.removeRoleTemplateFunctions(id);
            roleTemplateDao.batchRoleTemplateFunctions(param.getRoleTemplateFunctions());
        }else{
            roleTemplateDao.removeRoleTemplateFunctions(id);
        }

        return new RestResponse<>(node);
    }

    @ApiOperation(value = "删除角色模板", notes = "根据主键删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Integer", paramType = "path"),
    })
    @DeleteMapping("/roletemplates/{id}")
    @Override
    public RestResponse<Integer> removeRoleTemplate(@PathVariable Integer id) {
        roleTemplateDao.removeRoleTemplateMenus(id);
        roleTemplateDao.removeRoleTemplateFunctions(id);
        Integer node = roleTemplateDao.removeRoleTemplate(id);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "删除角色模板", notes = "根据主键批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "主键数组", required = true, dataType = "string", paramType = "body"),
    })
    @DeleteMapping("/roletemplates")
    @Override
    public RestResponse<Integer> removeRoleTemplate(@RequestBody Integer[] param) {
        Integer count = 0;
        if (param != null && param.length > 0) {
            for (Integer id : param) {
                roleTemplateDao.removeRoleTemplateMenus(id);
                roleTemplateDao.removeRoleTemplateFunctions(id);
                Integer node = roleTemplateDao.removeRoleTemplate(id);
                count += node;
            }
        }
        return new RestResponse<>(count);
    }

    @GetMapping("/roletemplates/{id}/menus")
    @Override
    public RestResponse<List<RoleTemplateMenuVO>> getRoleTemplateMenus(@PathVariable Integer id) {
        List<RoleTemplateMenuVO> nodes = roleTemplateDao.getRoleTemplateMenus(id);
        return new RestResponse<>(nodes);
    }

    @GetMapping("/roletemplates/{id}/functions")
    @Override
    public RestResponse<List<RoleTemplateFunctionVO>> getRoleTemplateFunctions(@PathVariable Integer id) {
        List<RoleTemplateFunctionVO> nodes = roleTemplateDao.getRoleTemplateFunctions(id);
        return new RestResponse<>(nodes);
    }

    @ApiOperation(value = "获取角色模板授权集", notes = "根据主键查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Integer", paramType = "path"),
    })
    @GetMapping("/roletemplates/{id}/authorizations")
    @Override
    public RestResponse<List<RoleTemplateAuthorizationVO>> getRoleTemplateAuthorizations(@PathVariable Integer id) {
        List<RoleTemplateAuthorizationVO> roleTemplateAuthorizations = roleTemplateDao.getRoleTemplateAuthorizations(id);
        return new RestResponse<>(roleTemplateAuthorizations);
    }

    @ApiOperation(value = "保存角色模板授权集", notes = "新增和修改都调用此接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "param", value = "权限集", required = true, dataType = "string", paramType = "body"),
    })
    @PostMapping("/roletemplates/{id}/authorizations")
    @Override
    public RestResponse<Integer> addRoleTemplateAuthorizations(@PathVariable Integer id, @RequestBody List<RoleTemplateAuthorizationVO> param) {
        roleTemplateDao.removeRoleTemplateAuthorizations(id);
        if (param != null && param.size() > 0) {
            for (RoleTemplateAuthorizationVO roleTemplateAuthorization : param) {
                roleTemplateAuthorization.setRoleTemplateId(id);
            }
            roleTemplateDao.batchAddRoleTemplateAuthorizations(param);
        }
        return new RestResponse<>();
    }

    @GetMapping("/roletemplates/factory/authorizations")
    @Override
    public RestResponse<List<RoleTemplateAuthorizationVO>> getRoleTemplateFactoryAuthorizations() {
        Integer factoryId = UserUtil.get("factoryId", Integer.class);
        List<RoleTemplateAuthorizationVO> roleTemplateAuthorizations = roleTemplateDao.getRoleTemplateAuthorizationsByFactoryId(factoryId);
        return new RestResponse<>(roleTemplateAuthorizations);
    }

}
