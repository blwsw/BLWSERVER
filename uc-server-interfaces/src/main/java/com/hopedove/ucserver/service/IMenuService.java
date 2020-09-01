package com.hopedove.ucserver.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.MenuVO;

@FeignClient(value = "uc-server")
public interface IMenuService {

    //获取权限菜单列表
    @GetMapping("/menus")
    RestPageResponse<List<MenuVO>> getMenus(@RequestParam(required = false) String name, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize,  @RequestParam(required = false) String sort);

    //获取某一个权限菜单
    @GetMapping("/menus/{id}")
    RestResponse<MenuVO> getMenu(@PathVariable Integer id);
    
    //创建权限菜单
    @PostMapping("/menus")
    public RestResponse<Integer> createMenu(@RequestBody MenuVO menuVO);

    //修改菜单权限
    @PutMapping("/menus/{id}")
    public RestResponse<Integer> modifyMenu(@PathVariable Integer id, @RequestBody MenuVO menuVO);
    
    //删除一个权限菜单
    @DeleteMapping("/menus/{id}")
    RestResponse<Integer> removeMenu(@PathVariable Integer id);
    
    //权限菜单树
    @GetMapping("/menus/tree")
    public RestResponse<List<MenuVO>> getMenusTree(
            @RequestParam(required = false) String code);
}
