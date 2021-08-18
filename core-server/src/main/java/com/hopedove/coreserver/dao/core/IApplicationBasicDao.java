package com.hopedove.coreserver.dao.core;

import com.hopedove.coreserver.vo.core.ApplicationBasicVO;

import java.util.List;
import java.util.Map;

public interface IApplicationBasicDao {

    List<ApplicationBasicVO> getApplicationBasics(Map<String, Object> param);

    int getApplicationBasicsCount(Map<String, Object> param);

    ApplicationBasicVO getApplicationBasic(ApplicationBasicVO param);

    int addApplicationBasic(ApplicationBasicVO param);

    int modifyApplicationBasic(ApplicationBasicVO param);

    int removeApplicationBasic( ApplicationBasicVO applicationBasicVO);
}
