package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface IRoleTemplateService {

    //查询角色模板列表
    @GetMapping("/roletemplates")
    RestPageResponse<List<RoleTemplateVO>> getRoleTemplates(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String noCode,
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sort);

    //查询角色模板
    @GetMapping("/roletemplates/{id}")
    RestResponse<RoleTemplateVO> getRoleTemplate(@PathVariable Integer id);

    //新增角色模板
    @PostMapping("/roletemplates")
    RestResponse<Integer> addRoleTemplate(@RequestBody RoleTemplateVO param);

    //修改角色模板
    @PutMapping("/roletemplates/{id}")
    RestResponse<Integer> modifyRoleTemplate(@PathVariable Integer id, @RequestBody RoleTemplateVO param);

    //删除角色模板
    @DeleteMapping("/roletemplates/{id}")
    RestResponse<Integer> removeRoleTemplate(@PathVariable Integer id);

    //批量删除角色模板
    @DeleteMapping("/roletemplates")
    RestResponse<Integer> removeRoleTemplate(@RequestBody Integer[] param);

    @GetMapping("/roletemplates/{id}/menus")
    RestResponse<List<RoleTemplateMenuVO>> getRoleTemplateMenus(@PathVariable Integer id);
//
    @GetMapping("/roletemplates/{id}/functions")
    RestResponse<List<RoleTemplateFunctionVO>> getRoleTemplateFunctions(@PathVariable Integer id);

    //获取角色模板的授权集
    @GetMapping("/roletemplates/{id}/authorizations")
    RestResponse<List<RoleTemplateAuthorizationVO>> getRoleTemplateAuthorizations(@PathVariable Integer id);

    //保存角色模板授权集
    @PostMapping("/roletemplates/{id}/authorizations")
    RestResponse<Integer> addRoleTemplateAuthorizations(@PathVariable Integer id, @RequestBody List<RoleTemplateAuthorizationVO> param);

    //获取当前工厂的角色模板授权集
    @GetMapping("/roletemplates/factory/authorizations")
    RestResponse<List<RoleTemplateAuthorizationVO>> getRoleTemplateFactoryAuthorizations();
}
