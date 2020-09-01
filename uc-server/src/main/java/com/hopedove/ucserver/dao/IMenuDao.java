package com.hopedove.ucserver.dao;

import java.util.List;
import java.util.Map;

import com.hopedove.ucserver.vo.DeptVO;
import com.hopedove.ucserver.vo.MenuVO;
import com.hopedove.ucserver.vo.PermissionVO;

public interface IMenuDao {

    List<MenuVO> getMenus(Map<String, Object> param);

    int getMenusCount(Map<String, Object> param);

    MenuVO getMenu(MenuVO menuVO);

    int addMenu(MenuVO menuVO);

    int modifyMenu(MenuVO menuVO);

    int removeMenu(Integer id);
 
    List<MenuVO> getMenusTree(Map<String, Object> param);
}
