package com.hopedove.coreserver.service.core;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.core.ApplicationBasicVO;
import com.hopedove.coreserver.vo.core.ApplicationUpdateVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@FeignClient(value = "core-server")
public interface IApplicationUpdateService {

    @GetMapping("/applications/updates")
    RestPageResponse<List<ApplicationUpdateVO>> getApplicationUpdates(@RequestParam(required = false) Integer factoryId,
                                                                      @RequestParam(required = false) String clientType,
                                                                      @RequestParam(required = false) String version,
                                                                      @RequestParam(required = false) String subVersion,
                                                                      @RequestParam(required = false) Integer currentPage,
                                                                      @RequestParam(required = false) Integer pageSize,
                                                                      @RequestParam(required = false) String sort);

    @GetMapping("/applications/updates/{id}")
    RestResponse<ApplicationUpdateVO> getApplicationUpdates(@PathVariable Integer id);

    @PostMapping("/applications/updates")
    RestResponse<ApplicationUpdateVO> addApplicationUpdates(@RequestBody ApplicationUpdateVO param);

    @PutMapping("/applications/updates/{id}")
    RestResponse<ApplicationUpdateVO> modifyApplicationUpdates(@PathVariable Integer id, @RequestBody ApplicationUpdateVO param);

    @DeleteMapping("/applications/updates/{id}")
    RestResponse<Integer> removeApplicationUpdates(@PathVariable Integer id);

    @GetMapping("/applications/updates/news")
    RestResponse<List<ApplicationUpdateVO>> getApplicationUpdatesNews(@RequestParam String factoryCode, @RequestParam String clientType);
}
