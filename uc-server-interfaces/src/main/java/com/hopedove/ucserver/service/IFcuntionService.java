package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.FunctionVO;
import com.hopedove.ucserver.vo.MenuVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface IFcuntionService {

    //获取权限列表
    @GetMapping("/functions")
    RestPageResponse<List<FunctionVO>> getFunctions(@RequestParam(required = false) String name, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //获取某一个权限
    @GetMapping("/functions/{id}")
    RestResponse<FunctionVO> getFunction(@PathVariable Integer id);

    //创建权限
    @PostMapping("/functions")
    public RestResponse<FunctionVO> createFunction(@RequestBody FunctionVO param);

    //修改权限
    @PutMapping("/functions/{id}")
    public RestResponse<FunctionVO> modifyFunction(@PathVariable Integer id, @RequestBody FunctionVO param);

    //删除一个权限
    @DeleteMapping("/functions/{id}")
    RestResponse<FunctionVO> removeFunction(@PathVariable Integer id);
}
