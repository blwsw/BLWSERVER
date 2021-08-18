package com.hopedove.coreserver.service.core;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.core.CodeRuleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "core-server")
public interface ICodeRuleService {

    //查询编码规则列表
    @GetMapping("/coderules")
    RestPageResponse<List<CodeRuleVO>> getCodeRules(
            @RequestParam(required = false) Integer factoryId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sort);

    //查询编码规则
    @GetMapping("/coderules/{id}")
    RestResponse<CodeRuleVO> getCodeRule(@PathVariable Integer id);

    //新增编码规则
    @PostMapping("/coderules")
    RestResponse<Integer> addCodeRule(@RequestBody CodeRuleVO param);

    //修改编码规则
    @PutMapping("/coderules/{id}")
    RestResponse<Integer> modifyCodeRule(@PathVariable Integer id, @RequestBody CodeRuleVO param);

    //删除编码规则
    @DeleteMapping("/coderules/{id}")
    RestResponse<Integer> removeCodeRule(@PathVariable Integer id);

}
