package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.*;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.*;
import com.hopedove.ucserver.service.IFactoryService;
import com.hopedove.ucserver.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api(tags = "工厂管理")
@RestController
@Transactional
public class FactoryServiceImpl implements IFactoryService {

    private final static Logger log = LoggerFactory.getLogger(FactoryServiceImpl.class);

    @Autowired
    private IFactoryDao factoryDao;

    @Autowired
    private IDeptDao deptDao;

    @Autowired
    private IRoleTemplateDao roleTemplateDao;

    @Autowired
    private IFactoryRoleDao factoryRoleDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @ApiOperation(value = "查询工厂列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "enterpriseId", value = "所属企业Id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "工厂编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/factories")
    @Override
    public RestPageResponse<List<FactoryVO>> getFactories(@RequestParam(required = false) String name, @RequestParam(required = false) Integer enterpriseId, @RequestParam(required = false) String code, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("enterpriseId", enterpriseId);
        param.put("code", code);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = factoryDao.getFactoriesCount(param);
            page.setPage_total(count);
        }

        List<FactoryVO> datas = factoryDao.getFactories(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @GetMapping("/factories/{id}")
    @Override
    public RestResponse<FactoryVO> getFactory(@PathVariable Integer id) {
        FactoryVO param = new FactoryVO();
        param.setId(id);
        FactoryVO node = factoryDao.getFactory(param);
        return new RestResponse<>(node);
    }

    @PostMapping("/factories")
    @Override
    public RestResponse<Integer> addFactory(@RequestBody FactoryVO factoryVO) {
        String code = factoryVO.getCode();
        FactoryVO param = new FactoryVO();
        param.setCode(code);
        param = factoryDao.getFactory(param);
        if (null != param) {
            throw new BusinException(ErrorCode.EXP_FACTORY_CODE_EXIST);
        }
        factoryVO = (FactoryVO) VOUtil.fillCreate(factoryVO);
        factoryDao.addFactory(factoryVO);
        Integer node = factoryVO.getId();
        return new RestResponse<>(node);
    }

    @PutMapping("/factories/{id}")
    @Override
    public RestResponse<Integer> modifyFactories(@PathVariable Integer id, @RequestBody FactoryVO factoryVO) {
        String code = factoryVO.getCode();
        if (null != code && !"".equals(code)) {
            FactoryVO voById = new FactoryVO();
            voById.setId(id);
            voById = factoryDao.getFactory(voById);
            if (!code.equals(voById.getCode())) {
                FactoryVO param = new FactoryVO();
                param.setCode(code);
                param = factoryDao.getFactory(param);
                if (null != param) {
                    throw new BusinException(ErrorCode.EXP_FACTORY_CODE_EXIST);
                }
            }

        }
        factoryVO.setId(id);
        factoryVO = (FactoryVO) VOUtil.fillUpdate(factoryVO);
        Integer node = factoryDao.modifyFactory(factoryVO);
        return new RestResponse<>(node);
    }

    @DeleteMapping("/factories/{id}")
    @Override
    public RestResponse<Integer> removeFactories(@PathVariable Integer id) {
        FactoryVO param = new FactoryVO();
        param = (FactoryVO) VOUtil.fillUpdate(param);
        param.setId(id);
        param.setDisabled(1);
        Integer node = factoryDao.modifyFactory(param);
        return new RestResponse<>(node);
    }

    @Override
    @GetMapping("/factories/current")
    public RestResponse<FactoryVO> getFactoryCurrent() {
        Integer factoryId = UserUtil.get("factoryId", Integer.class);
        FactoryVO param = new FactoryVO();
        param.setId(factoryId);
        FactoryVO node = factoryDao.getFactory(param);
        return new RestResponse<>(node);
    }

    @ApiOperation(value = "工厂入驻", notes = "工厂入驻引导接口，创建一整套工厂基础数据")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "factoryBootstrap", value = "基础工厂数据", dataType = "String", paramType = "body"),
    })
    @PostMapping("/factories/bootstrap")
    @Override
    public RestResponse<FactoryVO> factoryBootstrap(@RequestBody FactoryBootstrapVO factoryBootstrap) {
        log.debug("工厂入驻 >>>> factoryBootstrap = {}", JsonUtil.writeValueAsString(factoryBootstrap));
        String progrecessKey = "factory:bootstrap:" + factoryBootstrap.getCode();
        //1.先创建工厂
        String code = factoryBootstrap.getCode();
        FactoryVO param = new FactoryVO();
        param.setCode(code);
        param = factoryDao.getFactory(param);
        if (null != param) {
            throw new BusinException(ErrorCode.EXP_FACTORY_CODE_EXIST);
        }

        FactoryVO factory = new FactoryVO();
        factory.setCode(factoryBootstrap.getCode());
        factory.setOldCode(factoryBootstrap.getOldCode());
        factory.setName(factoryBootstrap.getName());
        factory.setEnterpriseId(factoryBootstrap.getEnterpriseId());
        VOUtil.fillCreate(factory);
        factoryDao.addFactory(factory);

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);
        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

        //2.为工厂创建根部门节点
        DeptVO dept = new DeptVO();
        dept.setFactoryId(factory.getId());
        dept.setType("NL");
        dept.setName(factoryBootstrap.getDeptName());
        dept.setPathName(factoryBootstrap.getDeptName() + "/");
        VOUtil.fillCreate(dept);
        deptDao.addDept(dept);

        dept.setPath(dept.getId() + "/");

        deptDao.modifyDept(dept);

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);
        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

        //3.授权角色模板
        List<RoleTemplateAuthorizationVO> roleTemplateAuthorizations = new ArrayList<>();
        //工厂角色
        List<FactoryRoleVO> factoryRoles = new ArrayList<>();

        if (factoryBootstrap.getRoles() != null && factoryBootstrap.getRoles().size() > 0) {
            for (Integer roleTemplateId : factoryBootstrap.getRoles()) {
                RoleTemplateAuthorizationVO roleTemplateAuthorization = new RoleTemplateAuthorizationVO();
                roleTemplateAuthorization.setRoleTemplateId(roleTemplateId);
                roleTemplateAuthorization.setFactoryId(factory.getId());
                roleTemplateAuthorizations.add(roleTemplateAuthorization);

                //同时复制工厂角色信息
                RoleTemplateVO roleTemplate = roleTemplateDao.getRoleTemplate(roleTemplateId);
                FactoryRoleVO factoryRole = new FactoryRoleVO();
                factoryRole.setFactoryId(factory.getId());
                factoryRole.setName(roleTemplate.getName());
                factoryRole.setRoleTemplateId(roleTemplate.getId());
                VOUtil.fillCreate(factoryRole);
                factoryRoles.add(factoryRole);


            }
            roleTemplateDao.batchAddRoleTemplateAuthorizations(roleTemplateAuthorizations);

        }

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);
        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

        //4.复制角色模板（即创建工厂默认角色）
        for (FactoryRoleVO factoryRole : factoryRoles) {
            factoryRoleDao.addFactoryRole(factoryRole);

            //同时复制角色模板得数据权限，默认给角色的具体权限级别为空
            List<RoleTemplateFunctionVO> roleTemplateFunctions = roleTemplateDao.getRoleTemplateFunctions(factoryRole.getRoleTemplateId());

            if(roleTemplateFunctions != null && roleTemplateFunctions.size() > 0) {
                List<FactoryRoleFunctionVO> factoryRoleFunctions = roleTemplateFunctions.stream().map((e) -> {
                    FactoryRoleFunctionVO factoryRoleFunction = new FactoryRoleFunctionVO();
                    factoryRoleFunction.setRoleTemplateId(e.getRoleTemplateId());
                    factoryRoleFunction.setFunctionId(e.getFunctionId());
                    factoryRoleFunction.setRoleId(factoryRole.getId());
                    VOUtil.fillCreate(factoryRoleFunction);
                    return factoryRoleFunction;
                }).collect(Collectors.toList());

                //批量插入角色数据权限
                factoryRoleDao.batchAddFactoryRoleFunctions(factoryRoleFunctions);
            }
        }

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);
        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

        //5.创建用户
        UserVO user = new UserVO();
        user.setFactoryId(factory.getId());
        user.setUsername(factoryBootstrap.getUsername());
        user.setName("管理员");
        user.setEnterpriseId(factoryBootstrap.getEnterpriseId());
        user.setDeptId(dept.getId());
        user.setPassword(factoryBootstrap.getPassowrd());
        user.setUserType("FM");//代表工厂管理员
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        user.setUid(uuid);
        VOUtil.fillCreate(user);

        //加密密码
        try {
            String enPwd = RSAUtils.encryptContent(user.getPassword(), RSAUtils.AESKEY);
            user.setPassword(enPwd);
        } catch (Exception e) {
            log.error("密码加密失败", e);
            throw new BusinException(ErrorCode.EXP_PASSWORD_ENCODE);
        }

        userDao.addUser(user);

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);
        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

        //6.为用户赋予角色
        List<UserFactoryRoleRecVO> userFactoryRoles = new ArrayList<>();
        for (Integer roleTemplateId : factoryBootstrap.getUserRoles()) {
            //for (FactoryRoleVO factoryRole : factoryRoles) {
                //if (roleTemplateId.equals(factoryRole.getRoleTemplateId())) {
                    UserFactoryRoleRecVO userFactoryRole = new UserFactoryRoleRecVO();
                    userFactoryRole.setRoleId(roleTemplateId);
                    userFactoryRole.setUserId(user.getId());
                    userFactoryRoles.add(userFactoryRole);
                   // break;
               // }
            //}
        }
        if (userFactoryRoles.size() > 0) {
            userDao.batchAddRoleRecs(userFactoryRoles);
        }

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);
        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

//        if (factoryBootstrap.getWorkTypes() != null && factoryBootstrap.getWorkTypes().size() > 0) {
//            for (String wcTypeCode : factoryBootstrap.getWorkTypes()) {
//                FactoryWorkcenterTypeRecVO factoryWorkcenterTypeRec = new FactoryWorkcenterTypeRecVO();
//                factoryWorkcenterTypeRec.setFactoryId(factory.getId());
//                factoryWorkcenterTypeRec.setWcTypeCode(wcTypeCode);
//                factoryWorkcenterTypeRecDao.addFactoryWorkcenterTypeRec(factoryWorkcenterTypeRec);
//            }
//        }

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);
        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

        stringRedisTemplate.opsForValue().increment(progrecessKey, 1);

        stringRedisTemplate.expire(progrecessKey, 5, TimeUnit.MINUTES);

        return new RestResponse<>(factory);
    }

    @ApiOperation(value = "查询工厂入驻执行进度", notes = "返回工厂入驻的执行进度，数据为当前执行步骤，总共6步")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "工厂编码", dataType = "String", paramType = "query"),
    })
    @GetMapping("/factories/bootstrap")
    @Override
    public RestResponse<Integer> factoryBootstrap(String code) {
        String progrecessKey = "factory:bootstrap:" + code;
        String step = stringRedisTemplate.opsForValue().get(progrecessKey);
        return new RestResponse<>((StringUtils.isEmpty(step) ? 0 : Integer.parseInt(step)));
    }
}
