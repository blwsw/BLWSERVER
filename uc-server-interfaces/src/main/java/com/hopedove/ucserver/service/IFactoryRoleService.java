package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.FactoryRoleFunctionVO;
import com.hopedove.ucserver.vo.FactoryRoleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工厂角色管理
 */
@FeignClient(value = "core-server")
public interface IFactoryRoleService {

    //查询工厂角色列表
    @GetMapping("/factory/roles")
    RestPageResponse<List<FactoryRoleVO>> getUcFactoryRoles(@RequestParam(required = false) Integer factoryId, @RequestParam(required = false) String factoryName, @RequestParam(required = false) Integer roleTemplateId, @RequestParam(required = false) String name, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //查询工厂角色信息
    @GetMapping("/factory/roles/{id}")
    RestResponse<FactoryRoleVO> getUcFactoryRole(@PathVariable Integer id);

    //创建工厂角色
    @PostMapping("/factory/roles")
    RestResponse<Integer> addUcFactoryRole(@RequestBody FactoryRoleVO factoryRoleVO);

    //更新工厂角色
    @PutMapping("/factory/roles/{id}")
    RestResponse<Integer> modifyUcFactoryRole(@PathVariable Integer id, @RequestBody FactoryRoleVO factoryRoleVO);

    //删除工厂角色
    @DeleteMapping("/factory/roles/{id}")
    RestResponse<Integer> removeUcFactoryRole(@PathVariable Integer id);

    //获得某个工厂角色功能权限
    @GetMapping("/factory/roles/{id}/functions")
    RestResponse<List<FactoryRoleFunctionVO>> getRoleFunctions(@PathVariable Integer id);

}
