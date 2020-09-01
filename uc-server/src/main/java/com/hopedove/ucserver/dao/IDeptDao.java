package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.DeptVO;

import java.util.List;
import java.util.Map;

public interface IDeptDao {

    List<DeptVO> getDepts(Map<String, Object> param);

    int getDeptsCount(Map<String, Object> param);

    DeptVO getDept(Integer id);

    int addDept(DeptVO param);

    int modifyDept(DeptVO param);

    int removeDept(Integer id);

    List<DeptVO> getDeptsTree(Map<String, Object> param);
}
