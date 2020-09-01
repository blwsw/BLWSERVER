package com.hopedove.ucserver.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.ModuleVO;

@FeignClient(value = "uc-server")
public interface IModuleService {

    @PostMapping("/modules")
    RestResponse<Integer> createModule(@RequestBody ModuleVO moduleVO) throws Exception;

    @PutMapping("/modules/{id}")
    RestResponse<Integer> updateModule(@PathVariable Integer id, @RequestBody ModuleVO moduleVO) throws Exception;

    //删除一个模板
    @DeleteMapping("/modules/{id}")
    RestResponse<Integer> removeModule(@PathVariable Integer id);
    
    //获取模板列表
    @GetMapping("/modules")
    RestPageResponse<List<ModuleVO>> getModules(@RequestParam(required = false) String name, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize,  @RequestParam(required = false) String sort);

    //获取模板列表（包含下属得权限集）
    @GetMapping("/modules/functions")
    RestResponse<List<ModuleVO>> getModuleAndFunctions();
}
