package com.hopedove.coreserver.service.impl.jcsjgl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.EncryptUtils;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.coreserver.dao.jcsjgl.ISysUserDao;
import com.hopedove.coreserver.service.jcsjgl.ISysUserService;
import com.hopedove.coreserver.vo.jcsjgl.SysUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "系统人员")
@RestController
@Deprecated
public class SysUserServiceImpl implements ISysUserService {
	
	private final static Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
	
    @Autowired
    private ISysUserDao sysUserDao;
    
    //TODO  password为明文还是密文
    @ApiOperation(value = "系统人员", notes = "系统人员")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginAccount", value = "登录账号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "登录人姓名", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "权限", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping("/sysuser")
    @Override
	public RestResponse<SysUser> getSysUser(@RequestParam(required = false) String userId, 
			   @RequestParam(required = false) String loginAccount, 
			   @RequestParam(required = false) String userName, 
			   @RequestParam(required = false) String password,
			   @RequestParam(required = false) String roleId) {
    	if (log.isDebugEnabled()) {
    		log.debug("SysCanteenInfoServiceImpl.getSysUser----------pkId={},canteenNumber={},canteenName={},ddress={},remark={}", userId,loginAccount,userName, password, roleId );
    	}
    	RestResponse<SysUser> resp = new RestResponse<SysUser>();
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("userId", userId);
    	paramMap.put("loginAccount", loginAccount);
    	paramMap.put("userName", userName);
    	String mPassword = "";
    	if (!StringUtils.isEmpty(password)) {
    		try {
    			mPassword = EncryptUtils.encryptToMD5(password);
    		} catch (Exception e) {
    			log.error("SysCanteenInfoServiceImpl.getSysUser.密码加密失败----------");
    			throw new RuntimeException();
    		}
    	}
    	if (!StringUtils.isEmpty(mPassword)) {
    		paramMap.put("password", mPassword);
    	}
    	paramMap.put("remark", roleId);
    	SysUser info = sysUserDao.getOne(paramMap);
    	resp.setResponseBody(info);
		return resp;
	}

    @ApiOperation(value = "系统人员列表", notes = "系统人员")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "id", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "loginAccount", value = "登录账号", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "userName", value = "登录人姓名", required = false, dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "roleId", value = "权限", required = false, dataType = "String", paramType = "query")
})
    @GetMapping("/sysusers")
	@Override
	public RestResponse<List<SysUser>> getSysUsers(@RequestParam(required = false) String userId, 
			   @RequestParam(required = false) String loginAccount, 
			   @RequestParam(required = false) String userName, 
			   @RequestParam(required = false) String password,
			   @RequestParam(required = false) String roleId) {
    	if (log.isDebugEnabled()) {
    		log.debug("SysCanteenInfoServiceImpl.getSysUsers----------pkId={},canteenNumber={},canteenName={},ddress={},remark={}", userId,loginAccount,userName, password, roleId );
    	}
    	RestResponse<List<SysUser>> resp = new RestResponse<List<SysUser>>();
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("userId", userId);
    	paramMap.put("loginAccount", loginAccount);
    	paramMap.put("userName", userName);
    	String mPassword = "";
    	if (!StringUtils.isEmpty(password)) {
    		try {
    			mPassword = EncryptUtils.encryptToMD5(password);
    		} catch (Exception e) {
    			log.error("SysCanteenInfoServiceImpl.getSysUsers.密码加密失败----------");
    			throw new RuntimeException();
    		}
    	}
    	if (!StringUtils.isEmpty(mPassword)) {
    		paramMap.put("password", mPassword);
    	}
    	paramMap.put("remark", roleId);
    	List<SysUser> users = sysUserDao.getList(paramMap);
    	resp.setResponseBody(users);
		return resp;
	}


    @ApiOperation(value = "新增系统人员", notes = "新增食堂")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
    		@ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body")
    })
    @PostMapping("/sysuser")
	@Override
	public RestResponse<Integer> addSysUser(@RequestBody SysUser param) {
    	if (StringUtils.isEmpty(param.getPassword())  || StringUtils.isEmpty(param.getLoginAccount()) || StringUtils.isEmpty(param.getUserName())) {
    		throw new RuntimeException("新增系统人员参数异常");
    	}
    	RestResponse<Integer> resp = new RestResponse<Integer>();
    	sysUserDao.insert(param);
    	resp.setResponseBody(param.getUserId());
    	return resp;
	}

    @ApiOperation(value = "修改系统人员", notes = "根据id修改系统人员")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
    		@ApiImplicitParam(name = "id", value = "id", required = false, dataType = "Integer", paramType = "query"),
    		@ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body")
    })
    @PutMapping("/sysuser/{id}")
	@Override
	public RestResponse<String> modifyById(@PathVariable Integer id, @RequestBody SysUser param) {
    	param.setUserId(id);
    	sysUserDao.updateById(param);
    	return new RestResponse<String>();
	}

    @ApiOperation(value = "修改系统人员", notes = "修改系统人员")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
    		@ApiImplicitParam(name = "paramMap", value = "JSON结构体", required = true, dataType = "String", paramType = "body")
    })
    @PutMapping("/sysuser/modify")
	public RestResponse<String> modify(@RequestBody Map<String, Object> paramMap) {
    	sysUserDao.update(paramMap);
    	return new RestResponse<String>();
	}
    
    
    @ApiOperation(value = "修改系统人员密码", notes = "修改系统人员密码")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
    		@ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body")
    })
    @PutMapping("/sysuser/pwd")
	public RestResponse<String> modifyPwd(@RequestBody SysUser param) {
    	if (log.isDebugEnabled()) {
    		log.debug("SysUserServiceImpl.modifyPwd.param======{}", JsonUtil.writeValueAsString(param));
    	}
    	if (StringUtils.isEmpty(param.getPassword()) || StringUtils.isEmpty(param.getNewPassword()) || StringUtils.isEmpty(param.getLoginAccount()) ) {
    		throw new RuntimeException("修改密码服务参数异常");
    	}
    	String mPassword = "";
    	String mNewPassword = "";
    	try {
			mPassword = EncryptUtils.encryptToMD5(param.getPassword());
			mNewPassword = EncryptUtils.encryptToMD5(param.getNewPassword());
		} catch (Exception e) {
			log.error("SysCanteenInfoServiceImpl.modifyPwd.密码加密失败----------");
			throw new RuntimeException();
		}
    	param.setPassword(mPassword);
    	param.setNewPassword(mNewPassword);
    	sysUserDao.updatePwd(param);
    	return new RestResponse<String>();
	}
    
    

    @ApiOperation(value = "删除系统人员", notes = "根据id删除系统人员")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
    		@ApiImplicitParam(name = "id", value = "id", required = false, dataType = "Integer", paramType = "query")
    })
    @DeleteMapping("/sysuser/{id}")
	@Override
	public RestResponse<String> remove(@PathVariable Integer id) {
    	if (null == id || id == 0) {
    		throw new RuntimeException("删除系统人员参数异常");
    	}
    	SysUser param = new SysUser();
    	param.setUserId(id);
    	param.setDisabled("1");
    	sysUserDao.updateById(param);
    	return  new RestResponse<String>();
	}

}
