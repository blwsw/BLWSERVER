package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.EnterpriseVO;

import java.util.List;
import java.util.Map;

public interface IEnterpriseDao {

    List<EnterpriseVO> getEnterprises(Map<String, Object> param);

    int getEnterprisesCount(Map<String, Object> param);

    EnterpriseVO getEnterprise(EnterpriseVO enterpriseVo);

    int addEnterprise(EnterpriseVO enterpriseVo);

    int modifyEnterprise(EnterpriseVO enterpriseVo);
}
