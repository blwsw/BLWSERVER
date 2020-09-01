package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.FactoryRoleVO;
import com.hopedove.ucserver.vo.ModifyUserPwdVO;
import com.hopedove.ucserver.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "uc-server")
public interface IUserService {

    //获取用户列表
    @GetMapping("/users")
    RestPageResponse<List<UserVO>> getUsers(@RequestParam(required = false) String username, @RequestParam(required = false) String name, @RequestParam(required = false) String userType, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //获取某一个用户
    @GetMapping("/users/{id}")
    RestResponse<UserVO> getUser(@PathVariable Integer id);

    //创建一个用户
    @PostMapping("/users")
    RestResponse<Integer> addUser(@RequestBody UserVO userVO);

    //更新一个用户
    @PutMapping("/users/{id}")
    RestResponse<Integer> modifyUsers(@PathVariable Integer id, @RequestBody UserVO userVO);

    //删除一个用户
    @DeleteMapping("/users/{id}")
    RestResponse<Integer> removeUsers(@PathVariable Integer id);

    //获取当前可选的工厂角色
    @GetMapping("/users/factory/role")
    RestResponse<List<FactoryRoleVO>> getUserFactoryRole();

    //更新一个用户的密码
    @PutMapping("/users/{uid}/pwd")
    RestResponse<Integer> modifyUserPwd(@PathVariable String uid, @RequestBody ModifyUserPwdVO pwdVO);

    @GetMapping("/testDB")
	RestResponse<String> testDB();
}
