package com.hopedove.ucserver.dao;

import java.util.List;
import java.util.Map;

import com.hopedove.ucserver.vo.ModuleVO;

public interface IModuleDao {

    List<ModuleVO> getModules(Map<String, Object> param);

    int getModulesCount(Map<String, Object> param);

    int addModule(ModuleVO moduleVO);

    int modifyModule(ModuleVO moduleVO);

    int removeModule(Integer id);

    List<ModuleVO> getModuleAndFunctions();
}
