package com.hopedove.commons.utils;

import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 网络资源下载工具类
 */
public class URIDownloadUtil {

    private final static Logger log = LoggerFactory.getLogger(URIDownloadUtil.class);

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 下载资源(异步)
     *
     * @param url
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public final static Future<byte[]> asyncDownloadURIToBytes(String url) throws MalformedURLException, IOException {
        return executorService.submit(() -> {
            byte[] result = null;

            try {
                result = downloadURIToBytes(url);
            } catch (Exception e) {
                log.error("异步下载资源异常", e);
            }
            return result;
        });
    }

    /**
     * 下载资源
     *
     * @param url
     * @return 资源的二进制数据
     */
    public final static byte[] downloadURIToBytes(String url) throws MalformedURLException, IOException {
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        ByteArrayOutputStream arrayOutputStream = null;
        try {
            URL url_ = new URL(url);

            httpURLConnection = (HttpURLConnection) url_.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setUseCaches(true);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            log.debug("httpURLConnection ResponseCode = {}", code);
            if (code == 200) {
                // 将得到的数据转化成InputStream
                inputStream = httpURLConnection.getInputStream();

                //读取输入流中的数据
                arrayOutputStream = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int len;
                while ((len = inputStream.read(buff)) != -1) {
                    arrayOutputStream.write(buff, 0, len);
                }

                inputStream.close();

                arrayOutputStream.close();

                return arrayOutputStream.toByteArray();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            log.error("URL错误", e);
            throw e;
        } catch (IOException e) {
            log.error("I/O流异常", e);
            throw e;
        } finally {
            if (arrayOutputStream != null) {
                try {
                    arrayOutputStream.close();
                } catch (IOException e) {
                    log.error("关闭arrayOutputStream出现异常", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭inputStream出现异常", e);
                }
            }
        }
    }

    public static void main(String[] args) {
//        try {
//            byte[] bys = downloadURIToBytes("http://img.duoziwang.com/2019/04/07061414590831.jpg");
//            FileOutputStream fileOutputStream = new FileOutputStream("/Users/liaochente/downloadURIToBytes.png");
//            fileOutputStream.write(bys);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //保存pos系统的图片步骤
//        //1.先获得单号、POS域名拼成图片URL
//        //假设单号是20190201
//        String orderNumber = "20190201";
//
//        //POS域名通过平台参数接口获取（IPlatformService.getPlatform）
//        RestPageResponse<PlatformVO> platformResponse = platformService.getPlatform("POS_IMAGE_URL");
//        if (Response.isFail(platformResponse)) {
//            throw new BusinException(ErrorCode.EXP_NODATA);
//        }
//
//        PlatformVO platform = platformResponse.getResponseBody();
//        if (platform == null) {
//            throw new BusinException(ErrorCode.EXP_NODATA);
//        }
//
//        //拼接url, 固定读取3次，如果不存在，结果返回null
//        String url1 = platform.getCodeValue() + orderNumber + "-01.jpg";
////        String url2 = platform.getCodeValue() + orderNumber + "-02.jpg";
////        String url3 = platform.getCodeValue() + orderNumber + "-03.jpg";
//
//        //调用下载方法，异常需要自行捕捉并做相应处理（比如重试或者跳过），主要是MalformedURLException - URL格式不正确，IOException 输入输出异常
//        byte[] bytes = URIDownloadUtil.downloadURIToBytes(url1);
//
//        if(bytes != null && bytes.length > 0) {
//            //todo 最后使用 IFileService.textBytes() 把bytes上传到我们的服务器，并且获得fileId等信息
//        }
    }
}
