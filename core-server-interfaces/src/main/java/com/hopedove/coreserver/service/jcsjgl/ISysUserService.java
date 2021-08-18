package com.hopedove.coreserver.service.jcsjgl;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.jcsjgl.SysUser;

@FeignClient(value = "core-server")
public interface ISysUserService {

	@GetMapping("/sysuser")
	RestResponse<SysUser> getSysUser(@RequestParam(required = false) String userId, 
											   @RequestParam(required = false) String loginAccount, 
											   @RequestParam(required = false) String userName, 
											   @RequestParam(required = false) String password,
											   @RequestParam(required = false) String roleId);
	
	@GetMapping("/sysusers")
	RestResponse<List<SysUser>> getSysUsers(@RequestParam(required = false) String userId, 
												   @RequestParam(required = false) String loginAccount, 
												   @RequestParam(required = false) String userName, 
												   @RequestParam(required = false) String password,
												   @RequestParam(required = false) String roleId);
	
	@PostMapping("/sysuser")
    RestResponse<Integer> addSysUser(@RequestBody SysUser param);
	
	@PutMapping("/sysuser/{id}")
    RestResponse<String> modifyById(@PathVariable Integer id, @RequestBody SysUser param);
	
	@PutMapping("/sysuser/modify")
    RestResponse<String> modify(Map<String, Object> paramMap);

    @DeleteMapping("/sysuser/{id}")
    RestResponse<String> remove(@PathVariable Integer id);
	
}
