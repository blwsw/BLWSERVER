package com.hopedove.coreserver.service.core;

import com.hopedove.commons.response.RestResponse;
import com.hopedove.coreserver.vo.core.FileUploadVO;
import com.hopedove.coreserver.vo.core.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FeignClient(value = "core-server")
public interface IFileService {

    //客户端上传文件
    @PostMapping("/files")
    RestResponse<FileVO> files(@RequestParam("file") MultipartFile file, @RequestParam(required = false) Integer businId) throws IOException;

    //删除文件
    @DeleteMapping("/files/{id}")
    RestResponse<FileVO> deleteFiles(@PathVariable Integer id);

    //上传文本文件，以文件内容作为字符串得形式
    @PostMapping("/files/text")
    RestResponse<FileVO> textFiles(@RequestBody FileUploadVO param) throws IOException;

    //上传二进制文件，以整个文件的二进制数据形式上传
    @PostMapping("/files/bytes")
    RestResponse<FileVO> textBytes(@RequestBody FileUploadVO param) throws IOException;

    //客户端下载文件
    @GetMapping("/files/{id}")
    void downloadFile(HttpServletResponse response, @PathVariable String id);

    //获得文本文件的内容
    @GetMapping("/files/{id}/text")
    RestResponse<String> getFiles(@PathVariable Integer id);

    //获得文件的二进制数据
    @GetMapping("/files/inside/{id}")
    RestResponse<byte[]> getFile(@PathVariable String id);

    //获得文件得URL
    @GetMapping("/files/{id}/url")
    RestResponse<String> getFileUrl(@PathVariable Integer id);

    //二维码输出相关
    //获得某个文件的二维码图片的数据流
    @GetMapping("/files/{id}/qrcode/stream")
    void filesQrCodesStream(HttpServletResponse response, @PathVariable Integer id);

    //获取文件数据信息
    @GetMapping("/fileData/{id}")
    FileVO getFileVO(@PathVariable Integer id);
   
}
