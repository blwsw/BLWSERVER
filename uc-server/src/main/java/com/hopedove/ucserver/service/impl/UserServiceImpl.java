package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.RSAUtils;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.UserUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IFactoryRoleDao;
import com.hopedove.ucserver.dao.IUserDao;
import com.hopedove.ucserver.service.IUserService;
import com.hopedove.ucserver.vo.FactoryRoleVO;
import com.hopedove.ucserver.vo.ModifyUserPwdVO;
import com.hopedove.ucserver.vo.UserFactoryRoleRecVO;
import com.hopedove.ucserver.vo.UserVO;
import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.hopedove.commons.exception.ErrorCode.EXP_PWD_OLDPWD;

@Api(tags = "用户管理")
@RestController
@Transactional
public class UserServiceImpl implements IUserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IFactoryRoleDao factoryRoleDao;

    @GetMapping("/users")
    @Override
    public RestPageResponse<List<UserVO>> getUsers(@RequestParam(required = false) String username, @RequestParam(required = false) String name, @RequestParam(required = false) String userType, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if (currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Integer factoryId = UserUtil.get("factoryId", Integer.class);

        Map<String, Object> param = new HashMap<>();
        username = username != null ? username.trim() : username;
        name = name != null ? name.trim() : name;
        param.put("username", username);
        param.put("name", name);
        param.put("userType", userType);
        param.put("factoryId", factoryId);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if (page != null) {
            int count = userDao.getUsersCount(param);
            page.setPage_total(count);
        }
        param.put("pageIndex",(page.getPage_currentPage()-1)*page.getPage_pages());
        List<UserVO> datas = userDao.getUsers(param);


        //3.返回
        return new RestPageResponse<>(datas, page);
    }

    @GetMapping("/users/{id}")
    @Override
    public RestResponse<UserVO> getUser(@PathVariable Integer id) {
        UserVO userVO = new UserVO();
        userVO.setId(id);
        return new RestResponse<>(userDao.getUser(userVO));
    }

    @PostMapping("/users")
    @Override
    public RestResponse<Integer> addUser(@RequestBody UserVO userVO) {
        Integer enterpriseId = UserUtil.get("enterpriseId", Integer.class);
        Integer factoryId = UserUtil.get("factoryId", Integer.class);
//        Integer deptId = UserUtil.get("deptId", Integer.class);
        Integer workcenterId = UserUtil.get("workcenterId", Integer.class);
        userVO.setEnterpriseId(enterpriseId);
        userVO.setFactoryId(factoryId);
//        userVO.setDeptId(deptId);
        userVO.setWorkcenterId(workcenterId);
        userVO.setPassword("123456");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        userVO.setUid(uuid);


        userVO = (UserVO) VOUtil.fillCreate(userVO);

        //加密密码
        try {
            String enPwd = RSAUtils.encryptContent(userVO.getPassword(), RSAUtils.AESKEY);
            userVO.setPassword(enPwd);
        } catch (Exception e) {
            log.error("密码加密失败", e);
            throw new BusinException(ErrorCode.EXP_PASSWORD_ENCODE);
        }

        userDao.addUser(userVO);
        Integer node = userVO.getId();

//        List<Integer> roleRecs = userVO.getRoleRecs();
//        List<UserFactoryRoleRecVO> recVOS = new ArrayList<>();
//        for (Integer roleId : roleRecs) {
//            UserFactoryRoleRecVO recVO = new UserFactoryRoleRecVO();
//            recVO.setRoleId(roleId);
//            recVO.setUserId(node);
//            recVOS.add(recVO);
//        }
//        if (recVOS.size() > 0) {
//            userDao.batchAddRoleRecs(recVOS);
//        }

        return new RestResponse<>(node);
    }

    @PutMapping("/users/{id}")
    @Override
    public RestResponse<Integer> modifyUsers(@PathVariable Integer id, @RequestBody UserVO userVO) {
        userVO.setId(id);
        userVO = (UserVO) VOUtil.fillUpdate(userVO);
        if (!StringUtils.isBlank(userVO.getPassword())) {
        	try {
    			String newPwd = RSAUtils.encryptContent(userVO.getPassword(), RSAUtils.AESKEY);
    			userVO.setPassword(newPwd);
    		} catch (Exception e) {
    			log.error("密码加密失败", e);
                throw new BusinException(ErrorCode.EXP_PASSWORD_ENCODE);
    		}
        }
        Integer node = userDao.modifyUser(userVO);
        return new RestResponse<>(node);
    }

    @DeleteMapping("/users/{id}")
    @Override
    public RestResponse<Integer> removeUsers(@PathVariable Integer id) {
        UserVO param = new UserVO();
        param = (UserVO) VOUtil.fillUpdate(param);
        param.setId(id);
        param.setDisabled(1);
        Integer node = userDao.modifyUser(param);
        userDao.removeUserFactoryRoleRec(id);
        return new RestResponse<>(node);
    }

    @GetMapping("/users/factory/role")
    @Override
    public RestResponse<List<FactoryRoleVO>> getUserFactoryRole() {
        Integer factoryId = UserUtil.get("factoryId", Integer.class);

        Map<String, Object> param = new HashMap<>();
        param.put("factoryId", factoryId);

        List<FactoryRoleVO> datas = factoryRoleDao.getFactoryRoles(param);

        return new RestResponse<>(datas);
    }

    @PutMapping("/users/{uid}/pwd")
    @Override
    public RestResponse<Integer> modifyUserPwd(@PathVariable String uid, @RequestBody ModifyUserPwdVO pwdVO) {
        String sessionUId = UserUtil.get("uid", String.class);
        Integer id = Integer.parseInt(pwdVO.getUserId());

        if(StringUtils.isEmpty(pwdVO.getUserId())){
            id = UserUtil.get("id", Integer.class);
        }

        if (!sessionUId.equals(uid)) {
            throw new BusinException(ErrorCode.EXP_USER_NOMATCH);
        }

//        if (!pwdVO.getNewPwd().equals(pwdVO.getNewPwd2())) {
//            throw new BusinException(ErrorCode.EXP_PWD_PWD2);
//        }

        UserVO userVO = new UserVO();
        userVO.setId(id);
        userVO = userDao.getUser(userVO);

        //加密密码
//        String oldPwd = null;
//        try {
//            oldPwd = RSAUtils.encryptContent(pwdVO.getOldPwd(), RSAUtils.AESKEY);
//        } catch (Exception e) {
//            log.error("密码加密失败", e);
//
//            throw new BusinException(ErrorCode.EXP_PASSWORD_ENCODE);
//        }
//
//        if (!userVO.getPassword().equals(oldPwd)) {
//            throw new BusinException(ErrorCode.EXP_PWD_OLDPWD);
//        }

        try {
            String newPwd = RSAUtils.encryptContent(pwdVO.getNewPwd(), RSAUtils.AESKEY);
            userVO.setPassword(newPwd);
        } catch (Exception e) {
            log.error("密码加密失败", e);

            throw new BusinException(ErrorCode.EXP_PASSWORD_ENCODE);
        }

//        userVO.setPassword(pwdVO.getNewPwd());

        VOUtil.fillUpdate(userVO);


        int counts = userDao.modifyUserPwd(userVO);
        return new RestResponse<>(counts);
    }

    @Override
	@GetMapping("/testDB")
	public RestResponse<String> testDB() {
		Integer count = userDao.testDB();

		return new RestResponse(count.toString());
	}
}
