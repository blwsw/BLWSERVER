package com.hopedove.coreserver.files.ftp;

import com.hopedove.coreserver.global.FtpConfig;
import com.hopedove.coreserver.service.impl.core.FileServiceImpl;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Component
public class FTPClient {

    private final static Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Resource
    private FtpConfig ftpConfig;

    public FTPResult uploadFile(InputStream inputStream, long fileSize, String extName, Object mateData) {
        FTPResult resObj = new FTPResult();

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + extName;
        org.apache.commons.net.ftp.FTPClient ftp = new org.apache.commons.net.ftp.FTPClient();
        ftp.setControlEncoding("UTF-8");

        try {
            int reply;
            ftp.connect(ftpConfig.getIp(), Integer.parseInt(ftpConfig.getPort()));// 连接FTP服务器
            ftp.login(ftpConfig.getUsername(), ftpConfig.getPassword());// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new RuntimeException("FTP连接错误: " + reply);
            }
            ftp.setFileType(org.apache.commons.net.ftp.FTPClient.BINARY_FILE_TYPE);
            ftp.makeDirectory(ftpConfig.getBasePath());
            ftp.changeWorkingDirectory(ftpConfig.getBasePath());
            ftp.storeFile(fileName, inputStream);
            ftp.logout();

            resObj.setPath(fileName);
            resObj.setGroup("");
            resObj.setFullPath(fileName);//group + filename
        } catch (Exception e) {
            resObj = null;
            log.error("FTP 操作异常", e);
            throw new RuntimeException(e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    log.error("关闭FTP连接异常", e);
                }
            }
        }
        return resObj;
    }

    public <T> T downloadFile(String groupName, String path, FTPCallback<T> callback) {
        org.apache.commons.net.ftp.FTPClient ftp = new org.apache.commons.net.ftp.FTPClient();
        ftp.setControlEncoding("UTF-8");

        try {
            int reply;
            ftp.connect(ftpConfig.getIp(), Integer.parseInt(ftpConfig.getPort()));// 连接FTP服务器
            ftp.login(ftpConfig.getUsername(), ftpConfig.getPassword());// 登录

            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new RuntimeException("FTP连接错误: " + reply);
            }
            // 设置上传文件的类型为二进制类型
//            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
//                LOCAL_CHARSET = "UTF-8";
//            }
            ftp.setControlEncoding("utf-8");
            ftp.enterLocalPassiveMode();// 设置被动模式
            ftp.setFileType(FTP.BINARY_FILE_TYPE);// 设置传输的模式

            // 上传文件
            //对中文文件名进行转码，否则中文名称的文件下载失败
            String fileNameTemp = path;

            ftp.changeWorkingDirectory(ftpConfig.getBasePath());

            InputStream retrieveFileStream = ftp.retrieveFileStream(fileNameTemp);

            return callback.recv(retrieveFileStream);
        } catch (Exception e) {
            log.error("FTP 操作异常", e);
            throw new RuntimeException(e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    log.error("关闭FTP连接异常", e);
                }
            }
        }
    }
}
