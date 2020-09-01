package com.hopedove.ucserver.dao;

import java.util.List;
import java.util.Map;

import com.hopedove.ucserver.vo.PermissionVO;

public interface IPermissionDao {

    List<PermissionVO> getPermissions(Map<String, Object> param);

    int getPermissionsCount(Map<String, Object> param);

    PermissionVO getPermission(PermissionVO permissionVO);

    int addPermission(PermissionVO permissionVO);

    int modifyPermission(PermissionVO permissionVO);

    int removePermission(Integer id);
    
}
