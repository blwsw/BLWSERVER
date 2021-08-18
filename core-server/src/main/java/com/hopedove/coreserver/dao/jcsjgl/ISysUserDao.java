package com.hopedove.coreserver.dao.jcsjgl;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.jcsjgl.SysUser;

public interface ISysUserDao {

	SysUser getOne(Map<String, Object> paramMap);

	List<SysUser> getList(Map<String, Object> paramMap);

	void insert(SysUser param);

	void updateById(SysUser param);

	void update(Map<String, Object> paramMap);

	void updatePwd(SysUser param);


}
