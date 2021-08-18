package com.hopedove.coreserver.service.core;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.core.BusinCodeVO;
import com.hopedove.coreserver.vo.core.CodeRuleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(value = "core-server")
public interface ICodeGenerateService {

    //业务系统之间使用
    @PostMapping("/busincode/{codeType}")
    String getCode(@PathVariable String codeType, @RequestBody(required = false) BusinCodeVO businCodeVO);

    //业务系统之间使用--无token信息使用
    @PostMapping("/busincode/getCode2")
    public String getCode2(@RequestParam String codeType);
    @PostMapping("/busincode/getCode3")
    public String getCode3(@RequestParam String codeType,@RequestParam Integer factoryId,@RequestBody(required = false) BusinCodeVO businCodeVO);
    //生成某一种类型的业务编码
    @PostMapping("/busincode/{factoryId}/{codeType}")
    String getCode(@PathVariable String factoryId, @PathVariable String codeType, @RequestBody(required = false) BusinCodeVO businCodeVO);

    //初始化业务编码的编码规则缓存信息
    @PostMapping("/busincode/coderules")
    RestResponse<CodeRuleVO> initCodeRuleToCache(@RequestBody CodeRuleVO codeRule);

    @PostMapping("/busincode/coderules/temp")
    RestResponse<List<CodeRuleVO>> initCodeRuleToCacheTemp();
    
    @PostMapping("/busincode/getNoTokenCode")
    String getNoTokenCode(@RequestParam(required = false) String factoryId, @RequestParam(required = false) String codeType, @RequestParam(required = false) String operatorName);
}
