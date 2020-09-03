package com.hopedove.ucserver.service.nodes;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.node.NodesVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface INodesService {
    
    //创建一个节点
    @PostMapping("/nodes")
    RestResponse<Integer> addNodes(@RequestBody NodesVO NodesVO);

    //更新一个节点
    @PutMapping("/nodes")
    RestResponse<Integer> modifyNodes(@PathVariable String seqNo, @RequestBody NodesVO NodesVO);

    @ApiOperation(value = "查询节点")
    @GetMapping("/nodes")
    RestPageResponse<List<NodesVO>> getNodes(@RequestParam(required = false) String querySring, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

}
