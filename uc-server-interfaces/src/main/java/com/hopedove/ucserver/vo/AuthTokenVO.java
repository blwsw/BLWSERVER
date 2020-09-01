package com.hopedove.ucserver.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("登录凭证实体")
public class AuthTokenVO implements Serializable {
    private UserVO user;

    @ApiModelProperty("用户拥有的菜单集合")
    private List<UserMenuVO> userMenus;

    @ApiModelProperty("用户拥有的角色集合")
    private List<UserRoleVO> userRoles;

    @ApiModelProperty("用户拥有的权限集合")
    private List<UserPermissionVO> userPermissions;

    @ApiModelProperty("登录凭证")
    private String token;

    @ApiModelProperty("登录凭证有效期")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime tokenValidTime;

    @ApiModelProperty("刷新凭证")
    private String refreshToken;

    @ApiModelProperty("刷新凭证有效期")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime refreshTokenTime;

    public AuthTokenVO() {
    }

    public AuthTokenVO(UserVO user, List<UserRoleVO> userRoles, List<UserMenuVO> userMenus, List<UserPermissionVO> userPermissions, String token, LocalDateTime tokenValidTime, String refreshToken, LocalDateTime refreshTokenTime) {
        this.user = user;
        this.userMenus = userMenus;
        this.userRoles = userRoles;
        this.userPermissions = userPermissions;
        this.token = token;
        this.tokenValidTime = tokenValidTime;
        this.refreshToken = refreshToken;
        this.refreshTokenTime = refreshTokenTime;
    }

    public List<UserPermissionVO> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(List<UserPermissionVO> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public List<UserRoleVO> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleVO> userRoles) {
        this.userRoles = userRoles;
    }

    public List<UserMenuVO> getUserMenus() {
        return userMenus;
    }

    public void setUserMenus(List<UserMenuVO> userMenus) {
        this.userMenus = userMenus;
    }


    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenValidTime() {
        return tokenValidTime;
    }

    public void setTokenValidTime(LocalDateTime tokenValidTime) {
        this.tokenValidTime = tokenValidTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getRefreshTokenTime() {
        return refreshTokenTime;
    }

    public void setRefreshTokenTime(LocalDateTime refreshTokenTime) {
        this.refreshTokenTime = refreshTokenTime;
    }
}
