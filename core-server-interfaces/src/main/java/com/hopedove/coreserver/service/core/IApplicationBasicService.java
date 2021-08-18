package com.hopedove.coreserver.service.core;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.core.ApplicationBasicVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@FeignClient(value = "core-server")
public interface IApplicationBasicService {

    @GetMapping("/applications")
    RestPageResponse<List<ApplicationBasicVO>> getApplications(@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String version,
                                                               @RequestParam(required = false) String subVersion,
                                                               @RequestParam(required = false) String factoryCode,
                                                               @RequestParam(required = false) String clientType,
                                                               @RequestParam(required = false) Integer currentPage,
                                                               @RequestParam(required = false) Integer pageSize,
                                                               @RequestParam(required = false) String sort);

    @GetMapping("/applications/{id}")
    RestResponse<ApplicationBasicVO> getApplicationBasic(@PathVariable Integer id);

    @PostMapping("/applications")
    RestResponse<ApplicationBasicVO> addApplicationBasic(@RequestBody ApplicationBasicVO param);

    @PutMapping("/applications/{id}")
    RestResponse<ApplicationBasicVO> modifyApplicationBasic(@PathVariable Integer id, @RequestBody ApplicationBasicVO param);

    @DeleteMapping("/applications/{id}")
    RestResponse<Integer> removeApplicationBasic(@PathVariable Integer id);

    //获得最新客户端版本
    @Deprecated
    @GetMapping("/applications/last")
    RestResponse<ApplicationBasicVO> getApplicationBasicLast(HttpServletResponse response);
}
