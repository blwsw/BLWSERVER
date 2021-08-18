package com.hopedove.coreserver.service.impl.core;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import com.hopedove.commons.response.Response;
import com.hopedove.commons.response.RestPageResponse;
import com.hopedove.commons.response.RestResponse;
import com.hopedove.commons.utils.JsonUtil;
import com.hopedove.commons.utils.VOUtil;
import com.hopedove.commons.utils.zxing.LogoConfig;
import com.hopedove.coreserver.dao.core.IFileDao;
import com.hopedove.coreserver.files.ftp.FTPClient;
import com.hopedove.coreserver.files.ftp.FTPResult;
import com.hopedove.coreserver.service.core.IFileService;
import com.hopedove.coreserver.vo.core.FileUploadVO;
import com.hopedove.coreserver.vo.core.FileVO;
import com.hopedove.ucserver.service.IPlatformService;
import com.hopedove.ucserver.vo.PlatformVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Api(tags = "文件服务")
@RestController("fileService")
public class FileServiceImpl implements IFileService {

    private final static Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private IPlatformService platformService;

    @Autowired
    private FastFileStorageClient fileStorageClient;

    @Autowired
    private FTPClient ftpClient;

    @Autowired
    private IFileDao fileDao;

    @Value("${fileServerType}")
    private String fileServerType;

    private final static String FILE_SERVER_FTP = "ftp";


    @ApiOperation(value = "上传文件") // 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", dataType = "MultipartFile", paramType = "body")
    })
    @PostMapping("/files")
    @Override
    public RestResponse<FileVO> files(@RequestParam("file") MultipartFile file, @RequestParam(required = false) Integer businId) throws IOException {
        FileVO fileVO = new FileVO();
        VOUtil.fillCreate(fileVO);

        String fileExtName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        String fullPath = null;
        String path = null;
        String groupName = null;

        if (FILE_SERVER_FTP.equals(fileServerType)) {
            FTPResult storePath = ftpClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
            path = storePath.getPath();
            fullPath = storePath.getFullPath();
            groupName = storePath.getGroup();
        } else {
            StorePath storePath = fileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
            path = storePath.getPath();
            fullPath = storePath.getFullPath();
            groupName = storePath.getGroup();
        }

        fileVO.setFileName(file.getOriginalFilename());
        fileVO.setFullPath(fullPath);
        fileVO.setPath(path);
        fileVO.setFileExtName(fileExtName);
        fileVO.setGroupName(groupName);

        fileDao.addFile(fileVO);

        fileVO.setBusinId(businId);
        return new RestResponse<>(fileVO);
    }

    @DeleteMapping("/files/{id}")
    @Override
    public RestResponse<FileVO> deleteFiles(Integer id) {
        FileVO fileVO = fileDao.getFile(id);

        if (fileVO != null) {
            fileVO = (FileVO) VOUtil.fillUpdate(fileVO);
            fileVO.setDisabled(1);
            fileDao.removeFile(fileVO);
        }
        return new RestResponse<>(fileVO);
    }

    @ApiOperation(value = "上传文本文件") // 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件内容", dataType = "String", paramType = "param")
    })
    @PostMapping("/files/text")
    @Override
    public RestResponse<FileVO> textFiles(@RequestBody FileUploadVO param) throws IOException {
        log.debug("上传文本文件 fileName={}", param.getFileName());
        FileVO fileVO = new FileVO();
        String fileExtName = param.getFileType();
        byte[] fileSource = param.getFile().getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(param.getFile().getBytes("UTF-8"));

        String fullPath = null;
        String path = null;
        String groupName = null;

        if (FILE_SERVER_FTP.equals(fileServerType)) {
            FTPResult storePath = ftpClient.uploadFile(inputStream, fileSource.length, fileExtName, null);
            path = storePath.getPath();
            fullPath = storePath.getFullPath();
            groupName = storePath.getGroup();
        } else {
            StorePath storePath = fileStorageClient.uploadFile(inputStream, fileSource.length, fileExtName, null);
            path = storePath.getPath();
            fullPath = storePath.getFullPath();
            groupName = storePath.getGroup();
        }

        fileVO.setFileName(param.getFileName() + "." + param.getFileType());
        fileVO.setFullPath(fullPath);
        fileVO.setPath(path);
        fileVO.setFileExtName(fileExtName);
        fileVO.setGroupName(groupName);
        if (param.getDisabled() != null) {
            fileVO.setDisabled(param.getDisabled());
            fileVO.setCreateUserId(param.getCreateUserId());
            fileVO.setCreateUserName(param.getCreateUserName());
            fileVO.setCreateDeptId(param.getCreateDeptId());
            fileVO.setCreateDatetime(LocalDateTime.now());
            fileVO.setCreateIp(param.getCreateIp());
            fileVO.setUpdateUserId(param.getUpdateUserId());
            fileVO.setUpdateUserName(param.getUpdateUserName());
            fileVO.setUpdateDeptId(param.getUpdateDeptId());
            fileVO.setUpdateDatetime(LocalDateTime.now());
            fileVO.setUpdateIp(param.getUpdateIp());
        } else {
            VOUtil.fillCreate(fileVO);
        }

        fileDao.addFile(fileVO);
        return new RestResponse<>(fileVO);
    }

    @PostMapping("/files/bytes")
    @Override
    public RestResponse<FileVO> textBytes(@RequestBody FileUploadVO param) throws IOException {
        FileVO fileVO = new FileVO();
        String fileExtName = param.getFileType();
        byte[] fileSource = param.getFileBytes();
        log.debug("上传二进制文件 fileSource={}", fileSource);
        InputStream inputStream = new ByteArrayInputStream(fileSource);

        String fullPath = null;
        String path = null;
        String groupName = null;

        if (FILE_SERVER_FTP.equals(fileServerType)) {
            FTPResult storePath = ftpClient.uploadFile(inputStream, fileSource.length, fileExtName, null);
            path = storePath.getPath();
            fullPath = storePath.getFullPath();
            groupName = storePath.getGroup();
        } else {
            StorePath storePath = fileStorageClient.uploadFile(inputStream, fileSource.length, fileExtName, null);
            path = storePath.getPath();
            fullPath = storePath.getFullPath();
            groupName = storePath.getGroup();
        }

        fileVO.setFileName(param.getFileName() + "." + param.getFileType());
        fileVO.setFullPath(fullPath);
        fileVO.setPath(path);
        fileVO.setFileExtName(fileExtName);
        fileVO.setGroupName(groupName);
        if (param.getDisabled() != null) {
            fileVO.setDisabled(param.getDisabled());
            fileVO.setCreateUserId(param.getCreateUserId());
            fileVO.setCreateUserName(param.getCreateUserName());
            fileVO.setCreateDeptId(param.getCreateDeptId());
            fileVO.setCreateDatetime(LocalDateTime.now());
            fileVO.setCreateIp(param.getCreateIp());
            fileVO.setUpdateUserId(param.getUpdateUserId());
            fileVO.setUpdateUserName(param.getUpdateUserName());
            fileVO.setUpdateDeptId(param.getUpdateDeptId());
            fileVO.setUpdateDatetime(LocalDateTime.now());
            fileVO.setUpdateIp(param.getUpdateIp());
        } else {
            VOUtil.fillCreate(fileVO);
        }

        fileDao.addFile(fileVO);
        return new RestResponse<>(fileVO);
    }

    @ApiOperation(value = "下载文件") // 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件ID,即文件上传后获得的ID", dataType = "string", paramType = "path")
    })
    @GetMapping("/files/{id}")
    @Override
    public void downloadFile(HttpServletResponse response, @PathVariable String id) {
        FileVO fileVO = fileDao.getFile(Integer.parseInt(id));
        if (fileVO == null) {
            throw new BusinException(ErrorCode.EXP_NODATA);
        }

        byte[] b = null;

        if(FILE_SERVER_FTP.equals(fileServerType)) {
            b = ftpClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        } else {
            b = fileStorageClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        }

        try {
            OutputStream outputStream = response.getOutputStream();
            response.setCharacterEncoding("UTF-8"); // 重点突出
            response.setContentType("multipart/form-data");// 不同类型的文件对应不同的MIME类型
            //response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileVO.getFileName(), "utf-8"));

            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileVO.getFileName().getBytes("utf-8"),"ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(b.length));
            System.out.println(new String(fileVO.getFileName().getBytes("utf-8"),"ISO8859-1"));
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
        	 System.out.println(e.getMessage());
            log.error("文件下载异常", e);
        }

    }

    @GetMapping("/files/{id}/text")
    @Override
    public RestResponse<String> getFiles(@PathVariable Integer id) {
        FileVO fileVO = fileDao.getFile(id);
        if (fileVO == null) {
            throw new BusinException(ErrorCode.EXP_NODATA);
        }

        byte[] b = null;

        if(FILE_SERVER_FTP.equals(fileServerType)) {
            b = ftpClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        } else {
            b = fileStorageClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        }

        return new RestResponse<>(new String(b));
    }

    @ApiOperation(value = "内部服务获取文件") // 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件ID,即文件上传后获得的ID", dataType = "string", paramType = "path")
    })
    @GetMapping("/files/inside/{id}")
    @Override
    public RestResponse<byte[]> getFile(@PathVariable String id) {
        FileVO fileVO = fileDao.getFile(Integer.parseInt(id));
        if (fileVO == null) {
            throw new BusinException(ErrorCode.EXP_NODATA);
        }

        byte[] b = null;

        if(FILE_SERVER_FTP.equals(fileServerType)) {
            b = ftpClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        } else {
            b = fileStorageClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        }
        return new RestResponse<>(b);
    }

    @ApiOperation(value = "在线展示文档") // 使用该注解描述接口方法信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件fdfs路径", dataType = "文件ID,即文件上传后获得的ID", paramType = "path")
    })
    @GetMapping("/inlineFile/{id}")
    public void inlineFile(HttpServletResponse response, @PathVariable String id) {
        FileVO fileVO = fileDao.getFile(Integer.parseInt(id));
        if (fileVO == null) {
            throw new BusinException(ErrorCode.EXP_NODATA);
        }
        String fileName = fileVO.getFileName();
        String content_type = "application/octet-stream";
        if (fileName.endsWith("pdf")) {
            content_type = "application/pdf";
        } else if (fileName.endsWith("txt")) {
            content_type = "text/plain";
        }

        byte[] b = null;

        if(FILE_SERVER_FTP.equals(fileServerType)) {
            b = ftpClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        } else {
            b = fileStorageClient.downloadFile(fileVO.getGroupName(), fileVO.getPath(), inputStream -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] b1 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(b1)) != -1) {
                    byteArrayOutputStream.write(b1, 0, len);
                }

                return byteArrayOutputStream.toByteArray();
            });
        }

        try {
            OutputStream outputStream = response.getOutputStream();
            response.setCharacterEncoding("gbk"); // 重点突出
            response.setContentType(content_type);// 不同类型的文件对应不同的MIME类型
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);

            outputStream.write(b);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "获得文件URL") // 使用该注解描述接口方法信息
    @GetMapping("/files/{id}/url")
    @Override
    public RestResponse<String> getFileUrl(@PathVariable Integer id) {
        RestPageResponse<List<PlatformVO>> platformResponse = platformService.getPlatforms("FILE_URL", null, null, null, null);
        if (Response.isFail(platformResponse)) {
            throw new BusinException(ErrorCode.EXP_NODATA);
        }

        List<PlatformVO> platforms = platformResponse.getResponseBody();
        if (platforms == null || platforms.size() == 0) {
            throw new BusinException(ErrorCode.EXP_NODATA);
        }

        PlatformVO platform = platforms.get(0);

        return new RestResponse<>(platform.getCodeValue() + id);
    }

    @GetMapping("/files/{id}/qrcode/stream")
    @Override
    public void filesQrCodesStream(HttpServletResponse response, @PathVariable Integer id) {
        RestResponse<String> restResponse = this.getFileUrl(id);

        if (Response.isSuccess(restResponse)) {
            if (StringUtils.isNotEmpty(restResponse.getResponseBody())) {
                //生成二维码
                String contents = restResponse.getResponseBody(); // 二维码内容
                int width = 430; // 二维码图片宽度 300
                int height = 430; // 二维码图片高度300

                String format = "jpg";// 二维码的图片格式 gif

                Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
                // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                // 内容所使用字符集编码
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                //      hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值
                //      hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值
                hints.put(EncodeHintType.MARGIN, 1);//设置二维码边的空度，非负数
                try {
                    BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,//要编码的内容
                            //编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code 93 1D ,Code 128 1D,
                            //Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two of Five) 1D,
                            //MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN extension,UPC_EAN_EXTENSION
                            BarcodeFormat.QR_CODE,
                            width, //条形码的宽度
                            height, //条形码的高度
                            hints);//生成条形码时的一些配置,此项可选

                    //输出给客户端
                    //读取logo文件
                    ClassPathResource classPathResource = new ClassPathResource("images/client_logo.png");

                    InputStream logoInputStream = classPathResource.getInputStream();

                    OutputStream outputStream = response.getOutputStream();

//                    MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);

                    BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
                    //设置logo图标
                    LogoConfig logoConfig = new LogoConfig();
                    image = logoConfig.LogoMatrix(image, ImageIO.read(logoInputStream));

                    if (!ImageIO.write(image, format, outputStream)) {
                        throw new IOException("Could not write an image of format " + format);
                    }

                    response.setCharacterEncoding("UTF-8"); // 重点突出
                    response.setContentType("image/jpeg");// 不同类型的文件对应不同的MIME类型;
                } catch (Exception e) {
                    log.error("二维码输出异常", e);
                }
            } else {
                //todo 输出失败图片
                log.error("二维码生成失败，输出失败图片");
            }
        } else {
            //todo 输出失败图片
            log.error("二维码生成失败，输出失败图片, restResponse={}", JsonUtil.writeValueAsString(restResponse));
        }
    }

    @ApiOperation(value = "获得文件数据信息")
    @GetMapping("/fileData/{id}")
    public FileVO getFileVO(@PathVariable Integer id) {
        if (id == null) {
            return null;
        }
        return fileDao.getFile(id);
    }


}
