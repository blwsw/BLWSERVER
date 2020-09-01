package com.hopedove.ucserver.vo;

import com.hopedove.commons.vo.BasicVO;

import java.util.List;

public class FactoryBootstrapVO extends BasicVO {

    private Integer enterpriseId;

    private String code;

    private String oldCode;

    private String name;

    private String deptName;

    private List<Integer> roles;

    private String username;

    private String passowrd;

    private String passowrd2;

    private List<Integer> userRoles;

    private List<String> workTypes;

    public List<String> getWorkTypes() {
        return workTypes;
    }

    public void setWorkTypes(List<String> workTypes) {
        this.workTypes = workTypes;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    public String getPassowrd2() {
        return passowrd2;
    }

    public void setPassowrd2(String passowrd2) {
        this.passowrd2 = passowrd2;
    }

    public List<Integer> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<Integer> userRoles) {
        this.userRoles = userRoles;
    }
}
