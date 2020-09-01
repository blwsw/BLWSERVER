package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.RoleTemplateAuthorizationVO;
import com.hopedove.ucserver.vo.RoleTemplateFunctionVO;
import com.hopedove.ucserver.vo.RoleTemplateMenuVO;
import com.hopedove.ucserver.vo.RoleTemplateVO;

import java.util.List;
import java.util.Map;

public interface IRoleTemplateDao {

    List<RoleTemplateVO> getRoleTemplates(Map<String, Object> param);

    int getRoleTemplatesCount(Map<String, Object> param);

    RoleTemplateVO getRoleTemplate(Integer id);

    int addRoleTemplate(RoleTemplateVO param);

    int modifyRoleTemplate(RoleTemplateVO param);

    int removeRoleTemplate(Integer id);

    List<RoleTemplateMenuVO> getRoleTemplateMenus(Integer id);

    List<RoleTemplateFunctionVO> getRoleTemplateFunctions(Integer id);

    void batchAddRoleTemplateMenus(List<RoleTemplateMenuVO> params);

    void removeRoleTemplateMenus(Integer roleTemplateId);

    void batchRoleTemplateFunctions(List<RoleTemplateFunctionVO> params);

    void removeRoleTemplateFunctions(Integer roleTemplateId);

    List<RoleTemplateAuthorizationVO> getRoleTemplateAuthorizations(Integer roleTemplateId);

    void batchAddRoleTemplateAuthorizations(List<RoleTemplateAuthorizationVO> params);

    void removeRoleTemplateAuthorizations(Integer roleTemplateId);

    List<RoleTemplateAuthorizationVO> getRoleTemplateAuthorizationsByFactoryId(Integer factoryId);
}
