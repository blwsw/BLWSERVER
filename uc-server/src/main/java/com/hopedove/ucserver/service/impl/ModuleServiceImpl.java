package com.hopedove.ucserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IModuleDao;
import com.hopedove.ucserver.service.IModuleService;
import com.hopedove.ucserver.vo.ModuleVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "权限模块管理")
@RestController
public class ModuleServiceImpl implements IModuleService {

	private final static Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);
	
	@Autowired
    private IModuleDao moduleDao;
	
	@ApiOperation(value = "查询模块列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
	 //获取模板列表
    @GetMapping("/modules")
	@Override
	public RestPageResponse<List<ModuleVO>> getModules(@RequestParam(required = false) String name, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
    	//1.查询主数据
        BasicPageVO page = null;
        if(currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        name = name != null?name.trim():name;
        param.put("name", name);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if(page != null) {
            int count = moduleDao.getModulesCount(param);
            page.setPage_total(count);
        }

        List<ModuleVO> datas =moduleDao.getModules(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
	}

	//创建一个模块
	@PostMapping("/modules")
	@Override
	public RestResponse<Integer> createModule(@RequestBody ModuleVO moduleVO) throws Exception {
		if (StringUtils.isEmpty(moduleVO.getName())) {
            throw new BusinException(ErrorCode.EXP_PARAMETER);
        }
		VOUtil.fillCreate(moduleVO);
		Integer node = moduleDao.addModule(moduleVO);
		node = moduleVO.getId();
        return new RestResponse<>(node);
	}

    @PutMapping("/modules/{id}")
    @Override
    public RestResponse<Integer> updateModule(@PathVariable Integer id, @RequestBody ModuleVO moduleVO) throws Exception {
        moduleVO.setId(id);
        VOUtil.fillUpdate(moduleVO);
        Integer node = moduleDao.modifyModule(moduleVO);
        return new RestResponse<>(node);
    }

    //删除一个模板
    @DeleteMapping("/modules/{id}")
	@Override
	public RestResponse<Integer> removeModule(@PathVariable Integer id) {
        ModuleVO moduleVO = new ModuleVO();
        moduleVO.setId(id);
        moduleVO = (ModuleVO) VOUtil.fillUpdate(moduleVO);
        moduleVO.setDisabled(1);
		Integer node = moduleDao.modifyModule(moduleVO);
		return new RestResponse<>(node);
	}

    //获取模板列表（包含下属得权限集）
    @GetMapping("/modules/functions")
    @Override
    public RestResponse<List<ModuleVO>> getModuleAndFunctions() {
        List<ModuleVO> nodes = moduleDao.getModuleAndFunctions();
        return new RestResponse(nodes);
    }
}
