package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.DictVO;
import com.hopedove.ucserver.vo.DictTypeVO;
import com.hopedove.ucserver.vo.MultipleDictVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.openfeign.FeignClient;
import java.util.List;

/**
 * 数据字典管理
 */
@FeignClient(value = "uc-server")
public interface IDictService {

    //查询字典类型列表
    @GetMapping("/dicttypes")
    RestPageResponse<List<DictTypeVO>> getDictTypes(@RequestParam(required = false) String name, @RequestParam(required = false) String code, @RequestParam(required = false) Boolean hasDicts, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //查询数据字典列表
    @GetMapping("/dicts")
    RestPageResponse<List<DictVO>> getDicts(@RequestParam(required = false) String name, @RequestParam(required = false) String typeCode, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //查询一个数据字典
    @GetMapping("/dicts/{code}/{typeCode}")
    RestResponse<DictVO> getDict(@PathVariable String code,@PathVariable String typeCode);

    //创建一个数据字典
    @PostMapping("/dicts")
    RestResponse<Integer> addDict(@RequestBody MultipleDictVO multipleDictVO);

    //更新一个数据字典
    @PutMapping("/dicts/{code}/{typeCode}")
    RestResponse<Integer> modifyDict(@PathVariable String code,@PathVariable String typeCode, @RequestBody DictVO dictVO);

    //删除一个数据字典
    @DeleteMapping("/dicts/{code}/{typeCode}")
    RestResponse<Integer> removeDict(@PathVariable String code,@PathVariable String typeCode);

}
