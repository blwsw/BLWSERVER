package com.hopedove.ucserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IPermissionDao;
import com.hopedove.ucserver.service.IPermissionService;
import com.hopedove.ucserver.vo.PermissionVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "权限管理")
@RestController
@Transactional
public class PermissionServiceImpl implements IPermissionService {

    private final static Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private IPermissionDao permissionDao;

    @ApiOperation(value = "查询权限列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })

    //查询权限列表
    @GetMapping("/permissions")
    @Override
    public RestPageResponse<List<PermissionVO>> getPermissions(@RequestParam(required = false) String code, @RequestParam(required = false) String name, @RequestParam(required = false) String router, @RequestParam(required = false) String type, @RequestParam(required = false) Integer moduleId, @RequestParam(required = false) String dataPermOptions, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        code = code!=null?code.trim():code;
        name = name!=null?name.trim():name;
        router = router!=null?router.trim():router;
        type = type!=null?type.trim():type;
        dataPermOptions = dataPermOptions!=null?dataPermOptions.trim():dataPermOptions;
        param.put("code", code);
        param.put("name", name);
        param.put("router", router);
        param.put("type", type);
        param.put("moduleId", moduleId);
        param.put("dataPermOptions", dataPermOptions);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = permissionDao.getPermissionsCount(param);
            page.setPage_total(count);
        }

        List<PermissionVO> datas = permissionDao.getPermissions(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    //查询某一权限
    @GetMapping("/permissions/{id}")
    @Override
    public RestResponse<PermissionVO> getPermission(@PathVariable Integer id) {
        PermissionVO param = new PermissionVO();
        param.setId(id);
        PermissionVO node = permissionDao.getPermission(param);
        return new RestResponse<>(node);
    }

    //创建权限
    @PostMapping("/permissions")
    @Override
    public RestResponse<Integer> createPermission(@RequestBody PermissionVO permissionVO) {
        if (StringUtils.isEmpty(permissionVO.getName())) {
            throw new BusinException(ErrorCode.EXP_PARAMETER);
        }
        String code = permissionVO.getCode();
        PermissionVO param = new PermissionVO();
        param.setCode(code);
        param = permissionDao.getPermission(param);
        if (null != param) {
            throw new BusinException(ErrorCode.EXP_CODE_EXIST);
        }
        VOUtil.fillCreate(permissionVO);
        Integer node = permissionDao.addPermission(permissionVO);
        return new RestResponse<>(node);
    }

    //修改权限
    @PutMapping("/permissions/{id}")
    @Override
    public RestResponse<Integer> modifyPermission(@PathVariable Integer id, @RequestBody PermissionVO permissionVO) {
        String code = permissionVO.getCode();
        if (null != code && !"".equals(code)) {
            PermissionVO voById = new PermissionVO();
            voById.setId(id);
            voById = permissionDao.getPermission(voById);
            if (!code.equals(voById.getCode())) {
                PermissionVO param = new PermissionVO();
                param.setCode(code);
                param = permissionDao.getPermission(param);
                if (null != param) {
                    throw new BusinException(ErrorCode.EXP_CODE_EXIST);
                }
            }

        }
        permissionVO.setId(id);
        VOUtil.fillUpdate(permissionVO);
        Integer node = permissionDao.modifyPermission(permissionVO);
        return new RestResponse<>(node);
    }

    //删除权限
    @DeleteMapping("/permissions/{id}")
    @Override
    public RestResponse<Integer> removePermission(@PathVariable Integer id, HttpServletRequest request) {
        Integer node = permissionDao.removePermission(id);
        return new RestResponse<>(node);
    }

}
