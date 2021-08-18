package com.hopedove.coreserver.dao.jcsjgl;

import java.util.List;
import java.util.Map;

import com.hopedove.coreserver.vo.jcsjgl.SysSetup;

public interface ISysSetupDao {

	SysSetup getOne(Map<String, Object> paramMap);

	List<SysSetup> getList(Map<String, Object> paramMap);

	void insert(SysSetup param);

	void updateById(SysSetup param);

	void update(Map<String, Object> paramMap);

	void updateOnly(Map<String, Object> paramMap);

}
