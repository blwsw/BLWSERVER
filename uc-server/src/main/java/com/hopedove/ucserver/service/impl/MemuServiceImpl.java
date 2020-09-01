package com.hopedove.ucserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IMenuDao;
import com.hopedove.ucserver.service.IMenuService;
import com.hopedove.ucserver.vo.DeptVO;
import com.hopedove.ucserver.vo.MenuVO;
import com.hopedove.ucserver.vo.PermissionVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "权限菜单管理")
@RestController
@Transactional
public class MemuServiceImpl implements IMenuService{

    private final static Logger log = LoggerFactory.getLogger(MemuServiceImpl.class);

    @Autowired
    private IMenuDao menuDao;

    @ApiOperation(value = "查询权限列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    
    //获取权限菜单列表
    @GetMapping("/menus")
	public RestPageResponse<List<MenuVO>> getMenus(@RequestParam(required = false) String name, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if(currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        name = name!=null?name.trim():name;
        param.put("name", name);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if(page != null) {
            int count =menuDao.getMenusCount(param);
            page.setPage_total(count);
        }

        List<MenuVO> datas = menuDao.getMenus(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }


    //获取某一个权限菜单
    @GetMapping("/menus/{id}")
	public RestResponse<MenuVO> getMenu(@PathVariable Integer id){
    	MenuVO param = new MenuVO();
        param.setId(id);
        MenuVO node = menuDao.getMenu(param);
        return new RestResponse<>(node);
    }

     //创建权限菜单
    @PostMapping("/menus")
    public RestResponse<Integer> createMenu(@RequestBody MenuVO menuVO) {
    	if (StringUtils.isEmpty(menuVO.getName()) || StringUtils.isEmpty(menuVO.getCode())) {
            throw new BusinException(ErrorCode.EXP_PARAMETER);
        }
    	VOUtil.fillCreate(menuVO);
    	VOUtil.fillUpdate(menuVO);
        Integer node = menuDao.addMenu(menuVO);
        return new RestResponse<>(node);
    }
    //修改菜单权限
    @PutMapping("/menus/{id}")
    public RestResponse<Integer> modifyMenu(@PathVariable Integer id, @RequestBody MenuVO menuVO) {
    	menuVO.setId(id);
    	VOUtil.fillUpdate(menuVO);
        Integer node = menuDao.modifyMenu(menuVO);
        return new RestResponse<>(node);
    }

    //删除一个权限菜单
    @DeleteMapping("/menus/{id}")
	public RestResponse<Integer> removeMenu(@PathVariable Integer id) {
        MenuVO menuVO = new MenuVO();
        menuVO.setId(id);
        VOUtil.fillUpdate(menuVO);
        menuVO.setDisabled(1);
        Integer node = menuDao.modifyMenu(menuVO);
        return new RestResponse<>(node);
    }
    
    
    //权限菜单树
    @ApiOperation(value = "查询菜单树", notes = "暂不支持搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "权限菜单code", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping("/menus/tree")
    public RestResponse<List<MenuVO>> getMenusTree(
            @RequestParam(required = false) String code) {

        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        List<MenuVO> datas = menuDao.getMenusTree(param);
        //返回
        return new RestResponse<>(datas);
    }
}
