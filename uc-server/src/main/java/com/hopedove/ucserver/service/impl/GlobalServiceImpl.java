package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.dao.IGlobalDao;
import com.hopedove.ucserver.vo.DictTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "基础公共服务")
@RestController
public class GlobalServiceImpl {

    @Autowired
    private IGlobalDao globalDao;

    @ApiOperation(value = "查询全局字典", notes = "查询全集数据字典，客户端登录以后需要获取一份字典维护在本地，方便实现业务功能")
    @GetMapping("/global/dicts")
    public RestResponse<List<DictTypeVO>> getGlobalDicts() {
        List<DictTypeVO> dictTypeVOS = globalDao.getDicts();
        return new RestResponse<>(dictTypeVOS);
    }

}
