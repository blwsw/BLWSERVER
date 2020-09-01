package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.*;

import java.util.List;

public interface ITokenDao {

    UserVO login(AuthTokenFormVO param);

    UserVO getUserById(Integer id);

    List<UserMenuVO> getPMRMenus();

    List<UserMenuVO> getUserMenus(Integer userId);

    List<UserRoleVO> getUserRoles(Integer userId);

    List<UserPermissionVO> getUserUserPermissions(Integer userId);
}
