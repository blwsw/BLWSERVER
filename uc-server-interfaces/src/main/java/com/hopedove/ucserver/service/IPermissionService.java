package com.hopedove.ucserver.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.PermissionVO;

@FeignClient(value = "uc-server")
public interface IPermissionService {

    //获取权限列表
    @GetMapping("/permissions")
    RestPageResponse<List<PermissionVO>> getPermissions(@RequestParam(required = false) String code, @RequestParam(required = false) String name, @RequestParam(required = false) String router, @RequestParam(required = false) String type, @RequestParam(required = false) Integer moduleId, @RequestParam(required = false) String dataPermOptions, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize,  @RequestParam(required = false) String sort);

    //获取某一个权限
    @GetMapping("/permissions/{id}")
    RestResponse<PermissionVO> getPermission(@PathVariable Integer id);
    
    //创建权限
    @PostMapping("/permissions")
    RestResponse<Integer> createPermission(@RequestBody PermissionVO permissionVO);
    
    //修改权限
    @PutMapping("/permissions/{id}")
    RestResponse<Integer> modifyPermission(@PathVariable Integer id, @RequestBody PermissionVO permissionVO);
    
    //删除一个权限
    @DeleteMapping("/permissions/{id}")
    RestResponse<Integer> removePermission(@PathVariable Integer id,HttpServletRequest request);
    
}
