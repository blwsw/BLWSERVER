package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.EmployeeVO;

import java.util.List;
import java.util.Map;

public interface IEmployeeDao {

    List<EmployeeVO> getEmployees(Map<String, Object> param);

    int getEmployeesCount(Map<String, Object> param);

    EmployeeVO getEmployee(EmployeeVO EmployeeVO);

    int addEmployee(EmployeeVO EmployeeVO);

    int modifyEmployee(EmployeeVO EmployeeVO);

    List<EmployeeVO> getEmployeesCountNoLike(Map<String, Object> param);
}
