package com.hopedove.coreserver.dao.core;

import com.hopedove.coreserver.vo.core.ApplicationBasicVO;
import com.hopedove.coreserver.vo.core.ApplicationUpdateVO;

import java.util.List;
import java.util.Map;

public interface IApplicationUpdateDao {

    List<ApplicationUpdateVO> getApplicationUpdatesNews(ApplicationUpdateVO param);

    List<ApplicationUpdateVO> getApplicationUpdates(Map<String, Object> param);

    int getApplicationUpdatesCount(Map<String, Object> param);

    ApplicationUpdateVO getApplicationUpdate(ApplicationUpdateVO param);

    int addApplicationUpdate(ApplicationUpdateVO param);

    int modifyApplicationUpdate(ApplicationUpdateVO param);

    int removeApplicationUpdate(ApplicationUpdateVO param);
}
