package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.UserUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IEmployeeDao;
import com.hopedove.ucserver.service.IEmployeeService;
import com.hopedove.ucserver.vo.EmployeeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.hopedove.commons.exception.ErrorCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.hopedove.commons.exception.BusinException;
@Api(tags = "员工维护")
@RestController
@Transactional
public class EmployeeServiceImpl implements IEmployeeService {

    private final static Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private IEmployeeDao employeeDao;

    @ApiOperation(value = "查询员工列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "员工编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptId", value = "部门id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/employees")
    @Override
    public RestPageResponse<List<EmployeeVO>> getEmployees(@RequestParam(required = false) String code,@RequestParam(required = false) String name, @RequestParam(required = false) Integer deptId
            ,@RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if(currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("code", code);
        param.put("deptId", deptId);
        param.put("factoryId", UserUtil.get("factoryId", Integer.class));
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if(page != null) {
            int count = employeeDao.getEmployeesCount(param);
            page.setPage_total(count);
        }

        List<EmployeeVO> datas = employeeDao.getEmployees(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }
    @ApiOperation(value = "查询员工信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "员工id",  dataType = "Integer", paramType = "path"),
    })
    @GetMapping("/employees/{id}")
    @Override
    public RestResponse<EmployeeVO> getEmployee(@PathVariable Integer id) {
        EmployeeVO param = new EmployeeVO();
        param.setId(id);
        EmployeeVO node = employeeDao.getEmployee(param);
        return new RestResponse<>(node);
    }
    @ApiOperation(value = "新增员工信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "employeeVO", value = "员工对象", required = true, dataType = "String", paramType = "body"),
    })
    @PostMapping("/employees")
    @Override
    public RestResponse<Integer> addEmployee(@RequestBody EmployeeVO employeeVO) {
        Integer factoryId=UserUtil.get("factoryId", Integer.class);
        employeeVO.setFactoryId(factoryId);
        if(employeeVO.getFactoryId()!=null&&employeeVO.getCode()!=null){
            Map<String, Object> param = new HashMap<>();
            param.put("factoryId", employeeVO.getFactoryId());
            param.put("code", employeeVO.getCode());
            List<EmployeeVO> datas = employeeDao.getEmployeesCountNoLike(param);
            if(datas.size()>0){
                throw new BusinException(ErrorCode.EXP_CODE_EXIST);
            }
        }
        employeeVO = (EmployeeVO) VOUtil.fillCreate(employeeVO);
        employeeDao.addEmployee(employeeVO);
        Integer node = employeeVO.getId();
        return new RestResponse<>(node);
    }
    @ApiOperation(value = "修改员工信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "员工id",  dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "employeeVO", value = "员工对象", required = true, dataType = "String", paramType = "body"),
    })
    @PutMapping("/employees/{id}")
    @Override
    public RestResponse<Integer> modifyEmployee(@PathVariable Integer id, @RequestBody EmployeeVO employeeVO) {
        Integer factoryId=UserUtil.get("factoryId", Integer.class);
        employeeVO.setFactoryId(factoryId);
        Map<String, Object> param = new HashMap<>();
        param.put("code", employeeVO.getCode());
        param.put("factoryId", employeeVO.getFactoryId());
        List<EmployeeVO> datas = employeeDao.getEmployeesCountNoLike(param);
        if(datas!=null && datas.size()>0 && !datas.get(0).getId().equals(id)){
            throw new BusinException(ErrorCode.EXP_CODE_EXIST);
        }
        employeeVO.setId(id);
        employeeVO = (EmployeeVO) VOUtil.fillUpdate(employeeVO);
        Integer node = employeeDao.modifyEmployee(employeeVO);
        return new RestResponse<>(node);
    }
    @ApiOperation(value = "删除员工信息")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "员工id",  dataType = "Integer", paramType = "path"),
    })
    @DeleteMapping("/employees/{id}")
    @Override
    public RestResponse<Integer> removeEmployee(@PathVariable Integer id) {
        EmployeeVO param = new EmployeeVO();
        param = (EmployeeVO) VOUtil.fillUpdate(param);
        param.setId(id);
        param.setDisabled(1);
        Integer node = employeeDao.modifyEmployee(param);
        return new RestResponse<>(node);
    }
}
