package com.hopedove.ucserver.service.impl;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.SortUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.vo.BasicPageVO;
import com.hopedove.commons.vo.QueryEnum;
import com.hopedove.ucserver.dao.IPlatformDao;
import com.hopedove.ucserver.service.IPlatformService;
import com.hopedove.ucserver.vo.FileVO;
import com.hopedove.ucserver.vo.PlatformVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(tags = "平台管理")
@RestController
@Transactional
public class PlatformServiceImpl implements IPlatformService {

    private final static Logger log = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Autowired
    private IPlatformDao platformDao;
    @Autowired
    private HttpServletRequest request;

    @Value("filePath")
    private String filePath;

    @Autowired
    private HttpServletResponse response;

    @ApiOperation(value = "查询平台列表", notes = "支持分页查询")// 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "配置字段编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "codeValue", value = "配置字段", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "当前页大小", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序字段", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping("/platforms")
    @Override
    public RestPageResponse<List<PlatformVO>> getPlatforms(@RequestParam(required = false) String code, @RequestParam(required = false) String codeValue, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, String sort) {
        //1.查询主数据
        BasicPageVO page = null;
        if(currentPage != null) {
            page = new BasicPageVO(currentPage, pageSize);
        }

        //2.获得前端排序数据
        sort = SortUtil.format(sort);

        Map<String, Object> param = new HashMap<>();
        code = code!=null?code.trim():null;
        codeValue = codeValue!=null?codeValue.trim():null;
        param.put("code", code);
        param.put("codeValue", codeValue);
        param.put(QueryEnum.PAGES.getValue(), page);
        param.put(QueryEnum.SORTS.getValue(), sort);

        //2.查询总记录数，用于计算出总分页数
        if(page != null) {
            int count = platformDao.getPlatformsCount(param);
            page.setPage_total(count);
        }

        List<PlatformVO> datas = platformDao.getPlatforms(param);

        //3.返回
        return new RestPageResponse<>(datas, page);
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Integer", paramType = "path")
//    })
    @GetMapping("/platforms/{code}")
    @Override
    public RestResponse<PlatformVO> getPlatform(@PathVariable String code) {
        PlatformVO param = new PlatformVO();
        param.setCode(code);
        PlatformVO node = platformDao.getPlatform(param);
        return new RestResponse<>(node);
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "platformVo", value = "平台配置字段编码和配置字段", required = true, dataType = "PlatformVO", paramType = "body")
//    })
    @PostMapping("/platforms")
    @Override
    public RestResponse<Integer> addPlatform(@RequestBody PlatformVO platformVo) {
        String code = platformVo.getCode();
        PlatformVO param = new PlatformVO();
        param.setCode(code);
        param = platformDao.getPlatform(param);
        if (null != param) {
            throw new BusinException(ErrorCode.EXP_CODE_EXIST);
        }
        platformVo = (PlatformVO) VOUtil.fillCreate(platformVo);
        platformDao.addPlatform(platformVo);
        Integer node = platformVo.getId();
        return new RestResponse<>(node);
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "平台id", required = true, dataType = "Integer", paramType = "path"),
//            @ApiImplicitParam(name = "platformVo", value = "平台配置字段编码和配置字段", required = true, dataType = "PlatformVO", paramType = "body")
//    })
    @PutMapping("/platforms/{id}")
    @Override
    public RestResponse<Integer> modifyPlatforms(@PathVariable Integer id, @RequestBody PlatformVO platformVo) {
        String code = platformVo.getCode();
        if (null != code && !"".equals(code)) {
            PlatformVO voById = new PlatformVO();
            voById.setId(id);
            voById = platformDao.getPlatform(voById);
            if (!code.equals(voById.getCode())) {
                PlatformVO param = new PlatformVO();
                param.setCode(code);
                param = platformDao.getPlatform(param);
                if (null != param) {
                    throw new BusinException(ErrorCode.EXP_CODE_EXIST);
                }
            }

        }
        platformVo.setId(id);
        platformVo = (PlatformVO) VOUtil.fillUpdate(platformVo);
        Integer node = platformDao.modifyPlatform(platformVo);
        return new RestResponse<>(node);
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "平台id", required = true, dataType = "Integer", paramType = "path")
//    })
    @Override
    @DeleteMapping("/platforms/{id}")
    public RestResponse<Integer> removePlatforms(@PathVariable Integer id) {
        PlatformVO param = new PlatformVO();
        param = (PlatformVO) VOUtil.fillUpdate(param);
        param.setId(id);
        param.setDisabled(1);
        Integer node = platformDao.modifyPlatform(param);
        return new RestResponse<>(node);
    }

    //客户端上传文件
    @PostMapping("/files")
    public RestResponse<FileVO> files(@RequestParam("file") MultipartFile file, @RequestParam(required = false) Integer businId) throws IOException{
        FileVO fileVO = new FileVO();
        String realPath = filePath;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        file.transferTo(new File(folder,newName));
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/upload/" + realPath + newName;
        System.out.println(url);
        return new RestResponse<>(fileVO);
    }
}
