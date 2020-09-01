package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.EnterpriseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface IEnterpriseService {

    //获取企业列表
    @GetMapping("/enterprises")
    RestPageResponse<List<EnterpriseVO>> getEnterprises(@RequestParam(required = false) String name, @RequestParam(required = false) String code, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //获取某一个企业
    @GetMapping("/enterprises/{id}")
    RestResponse<EnterpriseVO> getEnterprise(@PathVariable Integer id);

    //创建一个企业
    @PostMapping("/enterprises")
    RestResponse<Integer> addEnterprise(@RequestBody EnterpriseVO enterpriseVO);

    //更新一个企业
    @PutMapping("/enterprises/{id}")
    RestResponse<Integer> modifyEnterprises(@PathVariable Integer id, @RequestBody EnterpriseVO enterpriseVO);

    //删除一个企业
    @DeleteMapping("/enterprises/{id}")
    RestResponse<Integer> removeEnterprises(@PathVariable Integer id);
}
