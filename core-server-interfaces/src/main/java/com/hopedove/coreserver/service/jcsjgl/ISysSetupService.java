package com.hopedove.coreserver.service.jcsjgl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.jcsjgl.SysSetup;

@FeignClient(value = "core-server")
public interface ISysSetupService {
	
	@GetMapping("/sys/SysSetup/getOne")
    public RestResponse<SysSetup> getOne(@RequestParam(required = false) String systemName) ;
	
    @PostMapping("/sys/SysSetup/update")
    public RestResponse<String> updateSysSetup(@RequestBody SysSetup sysSetup);
    
    

}
