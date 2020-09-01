package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.EmployeeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface IEmployeeService {

    //获取员工列表
    @GetMapping("/employees")
    RestPageResponse<List<EmployeeVO>> getEmployees(@RequestParam(required = false) String code,@RequestParam(required = false) String name, @RequestParam(required = false) Integer deptId
            , @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //获取某一个员工
    @GetMapping("/employees/{id}")
    RestResponse<EmployeeVO> getEmployee(@PathVariable Integer id);

    //创建一个员工
    @PostMapping("/employees")
    RestResponse<Integer> addEmployee(@RequestBody EmployeeVO employeeVO);

    //更新一个员工
    @PutMapping("/employees/{id}")
    RestResponse<Integer> modifyEmployee(@PathVariable Integer id, @RequestBody EmployeeVO employeeVO);

    //删除一个员工
    @DeleteMapping("/employees/{id}")
    RestResponse<Integer> removeEmployee(@PathVariable Integer id);
}
