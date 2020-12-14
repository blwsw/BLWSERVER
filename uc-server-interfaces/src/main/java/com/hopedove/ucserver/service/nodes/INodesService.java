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
    @PostMapping("/nodes/batch")
    public RestResponse<Integer> addNodesBatch(@RequestBody List<NodesVO> nodesVOs);
    //更新一个节点
    @PutMapping("/nodes/{addr}")
    RestResponse<Integer> modifyNodes(@PathVariable String addr, @RequestBody NodesVO NodesVO);

    @ApiOperation(value = "查询节点")
    @GetMapping("/nodes")
    RestPageResponse<List<NodesVO>> getNodes(@RequestParam(required = false) String querySring, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //表示前端下发设备参数33
    @PutMapping("/send/nodes/{seqNo}")
    RestResponse<Integer> setParams(@PathVariable String seqNo, @RequestBody NodesVO NodesVO);

    //1.4  0x34前端通知服务初始化（XMLInitHint）
    @PutMapping("nodes/initHint")
    RestResponse<Integer> initHint(@RequestBody NodesVO NodesVO);

    @PutMapping("nodes/initHint/ns")
    RestResponse<Integer> initHintNs(@RequestBody NodesVO NodesVO);

    //1.5  0x35前端通知服务清除设备故障（XMLClearFault）
    @PutMapping("nodes/clearFault")
    RestResponse<Integer> clearFault(@RequestParam(required = false) String addrs);

    // 1.2  0x32前端召唤设备参数（XMLGetParams）
    @PutMapping("nodes/getParams")
    RestResponse<Integer> getParams(@RequestParam(required = false) String addrs);

    @GetMapping("get/seqno")
    String getSeqNo(int id);
}
