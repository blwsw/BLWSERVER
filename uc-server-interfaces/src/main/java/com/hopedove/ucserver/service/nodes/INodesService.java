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

    //表示前端下发设备参数33
    @PutMapping("/send/nodes/{seqNo}")
    RestResponse<Integer> setParams(@PathVariable String seqNo, @RequestBody NodesVO NodesVO);

    //1.4  0x34前端通知服务初始化（XMLInitHint）
    @PutMapping("nodes/initHint")
    RestResponse<Integer> initHint(@RequestBody NodesVO NodesVO);

    //1.5  0x35前端通知服务清除设备故障（XMLClearFault）
    @PutMapping("nodes/clearFault")
    RestResponse<Integer> clearFault(@RequestParam(required = false) String addrs);


    @GetMapping("get/seqno")
    String getSeqNo(int id);
}
