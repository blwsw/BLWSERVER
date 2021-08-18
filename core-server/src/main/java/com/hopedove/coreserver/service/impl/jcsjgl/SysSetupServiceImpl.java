package com.hopedove.coreserver.service.impl.jcsjgl;

import java.util.HashMap;
import java.util.Map;

import com.hopedove.commons.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.dao.jcsjgl.ISysSetupDao;
import com.hopedove.coreserver.service.jcsjgl.ISysSetupService;
import com.hopedove.coreserver.vo.jcsjgl.SysSetup;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
@Api(tags = "系统设置服务")
@RestController
public class SysSetupServiceImpl implements ISysSetupService {
	private final static Logger log = LoggerFactory.getLogger(SysSetupServiceImpl.class);

    @Autowired
    private ISysSetupDao sysSetupDao;
    
    @GetMapping("/sys/SysSetup/getOne")
    @Override
    public RestResponse<SysSetup> getOne(@RequestParam(required = false) String systemName) {
        if (log.isDebugEnabled()) {
            log.debug("SysSetupServiceImpl.getOne----------systemName={}",
                    systemName);
        }
        RestResponse<SysSetup> resp = new RestResponse<SysSetup>();
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("systemName", systemName);
        SysSetup sysSetup =sysSetupDao.getOne(paramMap);
        resp.setResponseBody(sysSetup);
        return resp;
    }
	
	/**
     * 系统设置如下字段的修改
     * 充值最小金额、充值最大金额、单次消费最大金额、
	 * 早餐开始时间、早餐结束时间
	 * 午餐开始时间、午餐结束时间
	 * 晚餐开始时间、晚餐结束时间
     * 
     */
    @ApiOperation(value = "修改系统设置", notes = "修改系统设置")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
		@ApiImplicitParam(name = "param", value = "JSON结构体", required = true, dataType = "String", paramType = "body"), })
    @PostMapping("/sys/SysSetup/update")
	@Override
	public RestResponse<String> updateSysSetup(@RequestBody SysSetup sysSetup) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
        SysSetup sysSetupOld =sysSetupDao.getOne(paramMap);
        if(sysSetupOld!=null) {
        	sysSetup.setPkid(sysSetupOld.getPkid());
        	sysSetupDao.updateById(sysSetup);
        }else {
        	sysSetup.setSystemName("1");
        	sysSetup.setTerminalName("1");
        	sysSetup.setDecimalPlaces(1);
        	sysSetupDao.insert(sysSetup);
        }
        UserUtil.setCache("sysSetup", sysSetup);
    	 return new RestResponse<>("");
	}

}
