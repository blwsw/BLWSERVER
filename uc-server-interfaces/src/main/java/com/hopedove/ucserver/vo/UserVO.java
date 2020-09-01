package com.hopedove.ucserver.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class UserVO extends BasicVO {

    private Integer id;

    private String username;

    private String password;

    private String name;

    private String userType;

    private Integer enterpriseId;

    private Integer factoryId;

    private Integer deptId;

    private String deptCode;

    private String deptType;

    private Integer workcenterId;

    private String uid;

    private List<UserRoleVO> userRole;

    private String enterpriseName;

    private String factoryName;

    private String deptName;

    private List<Integer> roleRecs;
    
    private Integer KFID;
    
    private String KFBH;

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public List<UserRoleVO> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<UserRoleVO> userRole) {
        this.userRole = userRole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Integer getWorkcenterId() {
        return workcenterId;
    }

    public void setWorkcenterId(Integer workcenterId) {
        this.workcenterId = workcenterId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Integer> getRoleRecs() {
        return roleRecs;
    }

    public void setRoleRecs(List<Integer> roleRecs) {
        this.roleRecs = roleRecs;
    }

	public Integer getKFID() {
		return KFID;
	}

	public void setKFID(Integer kFID) {
		KFID = kFID;
	}

	public String getKFBH() {
		return KFBH;
	}

	public void setKFBH(String kFBH) {
		KFBH = kFBH;
	}
    
    
}
