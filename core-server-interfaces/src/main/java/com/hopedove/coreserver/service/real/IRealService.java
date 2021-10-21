package com.hopedove.coreserver.service.real;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.core.FileUploadVO;
import com.hopedove.coreserver.vo.core.FileVO;
import com.hopedove.coreserver.vo.real.NodesVO;
import com.hopedove.coreserver.vo.real.RealVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@FeignClient(value = "core-server")
public interface IRealService {

    @GetMapping("/reals")
    RestPageResponse<List<RealVO>> getReals(@RequestParam(required = false) String queryString,
                                            @RequestParam(required = false) Integer currentPage,
                                            @RequestParam(required = false) Integer pageSize,
                                            @RequestParam(required = false) String sort);

    @GetMapping("/nodes")
    RestPageResponse<List<NodesVO>> getNodes(@RequestParam(required = false) String queryString,
                                         @RequestParam(required = false) Integer currentPage,
                                         @RequestParam(required = false) Integer pageSize,
                                         @RequestParam(required = false) String sort);

    @GetMapping("/initData")
    RestResponse<List<RealVO>> InitPageData();

    @GetMapping("/getTimeTJ")
    RestResponse<List<Map>> getTimeTJ();

    @GetMapping("/dlist")
    public RestPageResponse<List<RealVO>> getDLists(@RequestParam(required = false) String queryString,
                                                    @RequestParam(required = false) Integer currentPage,
                                                    @RequestParam(required = false) Integer pageSize,
                                                    @RequestParam(required = false) String sort);

    @GetMapping("/clean/history")
    RestResponse<Integer> cleanHistroy(@RequestParam(required = false) String PID);
	
	 @GetMapping({ "/new/data" })
    public RestResponse<List<RealVO>> getNewData() ;
}
