package com.hopedove.ucserver.service;

import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.ucserver.vo.FileVO;
import com.hopedove.ucserver.vo.PlatformVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FeignClient(value = "uc-server")
public interface IPlatformService {

    //获取平台列表
    @GetMapping("/platforms")
    RestPageResponse<List<PlatformVO>> getPlatforms(@RequestParam(required = false) String code, @RequestParam(required = false) String codeValue, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sort);

    //获取某一个平台
    @GetMapping("/platforms/{code}")
    RestResponse<PlatformVO> getPlatform(@PathVariable String code);

    //创建一个平台
    @PostMapping("/platforms")
    RestResponse<Integer> addPlatform(@RequestBody PlatformVO platformVo);

    //更新一个平台
    @PutMapping("/platforms/{id}")
    RestResponse<Integer> modifyPlatforms(@PathVariable Integer id, @RequestBody PlatformVO platformVo);

    //删除一个平台
    @DeleteMapping("/platforms/{id}")
    RestResponse<Integer> removePlatforms(@PathVariable Integer id);


    //客户端上传文件
    @PostMapping("/files")
    RestResponse<FileVO> files(@RequestParam("file") MultipartFile file, @RequestParam(required = false) Integer businId) throws IOException;
}
