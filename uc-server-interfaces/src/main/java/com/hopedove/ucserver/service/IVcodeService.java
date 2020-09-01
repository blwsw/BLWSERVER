package com.hopedove.ucserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.VcodeFormVO;
import com.hopedove.ucserver.vo.VcodeVO;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Vcode服务
 */
@FeignClient(value = "uc-server")
public interface IVcodeService {

    /**
     * 获取验证码
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/vcode")
    RestResponse<VcodeVO> getVcode(@RequestParam(required = false) String vid);

    /**
     * 验证验证码是否正确
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/vcode/validate")
    void validateVcode(@RequestParam String vcode, @RequestParam String vid);
}
