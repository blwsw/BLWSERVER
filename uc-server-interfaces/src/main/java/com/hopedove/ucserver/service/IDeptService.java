package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.DeptVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface IDeptService {

    //查询部门列表
    @GetMapping("/depts")
    RestPageResponse<List<DeptVO>> getDept(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sort);

    //查询部门
    @GetMapping("/depts/{id}")
    RestResponse<DeptVO> getDept(@PathVariable Integer id);

    //新增部门
    @PostMapping("/depts")
    RestResponse<Integer> addDept(@RequestBody DeptVO param);

    //修改部门
    @PutMapping("/depts/{id}")
    RestResponse<Integer> modifyDept(@PathVariable Integer id, @RequestBody DeptVO param);

    //删除部门
    @DeleteMapping("/depts/{id}")
    RestResponse<Integer> removeDept(@PathVariable Integer id);

    //查询部门树
    @GetMapping("/depts/tree")
    RestResponse<List<DeptVO>> getDeptsTree(@RequestParam(required = false) Integer factoryId);
}
