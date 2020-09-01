package com.hopedove.ucserver.dao;

import com.hopedove.ucserver.vo.UserFactoryRoleRecVO;
import com.hopedove.ucserver.vo.UserVO;

import java.util.List;
import java.util.Map;

public interface IUserDao {

    List<UserVO> getUsers(Map<String, Object> param);

    int getUsersCount(Map<String, Object> param);

    UserVO getUser(UserVO userVO);

    int addUser(UserVO userVO);

    int modifyUser(UserVO userVO);

    int removeUserFactoryRoleRec(Integer userId);

    List<UserFactoryRoleRecVO> getUserFactoryRoleRec(Integer userId);

    void batchAddRoleRecs(List<UserFactoryRoleRecVO> params);

    int modifyUserPwd(UserVO userVO);

	Integer testDB();
}
