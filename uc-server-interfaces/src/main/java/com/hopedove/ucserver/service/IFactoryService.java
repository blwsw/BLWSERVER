package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.FactoryBootstrapVO;
import com.hopedove.ucserver.vo.FactoryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface IFactoryService {

    //获取工厂列表
    @GetMapping("/factories")
    RestPageResponse<List<FactoryVO>> getFactories(@RequestParam(required = false) String name, @RequestParam(required = false) Integer enterpriseId, @RequestParam(required = false) String code, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //获取某一个工厂
    @GetMapping("/factories/{id}")
    RestResponse<FactoryVO> getFactory(@PathVariable Integer id);

    //创建一个工厂
    @PostMapping("/factories")
    RestResponse<Integer> addFactory(@RequestBody FactoryVO factoryVO);

    //更新一个工厂
    @PutMapping("/factories/{id}")
    RestResponse<Integer> modifyFactories(@PathVariable Integer id, @RequestBody FactoryVO factoryVO);

    //删除一个工厂
    @DeleteMapping("/factories/{id}")
    RestResponse<Integer> removeFactories(@PathVariable Integer id);

    //获取用户当前工厂
    @GetMapping("/factories/current")
    RestResponse<FactoryVO> getFactoryCurrent();

    //工厂入驻执行
    @PostMapping("/factories/bootstrap")
    RestResponse<FactoryVO> factoryBootstrap(@RequestBody FactoryBootstrapVO factoryBootstrap);

    //获取工厂入驻执行进度
    @GetMapping("/factories/bootstrap")
    RestResponse<Integer> factoryBootstrap(@RequestParam String code);
}
