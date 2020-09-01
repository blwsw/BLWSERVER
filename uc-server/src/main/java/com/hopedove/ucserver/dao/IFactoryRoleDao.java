package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.FactoryRoleFunctionVO;
import com.hopedove.ucserver.vo.FactoryRoleVO;

import java.util.List;
import java.util.Map;

public interface IFactoryRoleDao {

    List<FactoryRoleVO> getFactoryRoles(Map<String, Object> param);

    int getFactoryRolesCount(Map<String, Object> param);

    FactoryRoleVO getFactoryRole(FactoryRoleVO factoryRoleVO);

    int addFactoryRole(FactoryRoleVO factoryRoleVO);

    int modifyFactoryRole(FactoryRoleVO factoryRoleVO);

    int removeFactoryRole(Integer id);

    void batchAddFactoryRoleFunctions(List<FactoryRoleFunctionVO> factoryRoleFunctions);

    List<FactoryRoleFunctionVO> getFactoryRoleFunctions(Integer id);

    void removeFactoryRoleFunction(FactoryRoleFunctionVO param);

}
