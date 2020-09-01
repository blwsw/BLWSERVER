package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.AuthTokenFormVO;
import com.hopedove.ucserver.vo.AuthTokenVO;
import com.hopedove.ucserver.vo.RefreshAuthTokenFormVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Token服务
 */
@FeignClient(value = "uc-server")
public interface ITokenService {

    /**
     * 创建登录凭证
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/tokens")
    RestResponse<AuthTokenVO> createToken(@RequestBody AuthTokenFormVO param) throws Exception;

    /**
     * 刷新登录凭证
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("tokens/refresh")
    RestResponse<AuthTokenVO> refreshToken(@RequestBody RefreshAuthTokenFormVO param) throws Exception;
}
