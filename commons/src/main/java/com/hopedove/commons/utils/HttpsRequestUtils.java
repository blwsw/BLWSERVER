package com.hopedove.commons.utils;

import com.hopedove.commons.exception.BusinException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * So slow:<br>
 * HttpsURLConnection post of HttpsRequestUtils.sendPost to wechat-pay take time: 1406ms.<br>
 * More fast after use static object.<br>
 * Modified by Hilbert at 20160324
 */
public class HttpsRequestUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpsRequestUtils.class);
    
    private static TrustManager[] trustManager = { new SelfX509TrustManager() };
    //解决XStream对出现双下划线的bug
    private static XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
    
    // 创建SSLContext对象，并使用我们指定的信任管理器初始化
 	private static SSLContext sslContext;
 	static {
 		try {
			sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchProviderException e) {
			logger.error(e.getMessage(), e);
		}
 	}
 	
    public static String sendPost(String requestUrl, Object xmlObj) {
    	String result = null;
        //将要提交给API的数据对象转换成XML格式数据Post给API
        String outputStr = xStreamForRequestPostData.toXML(xmlObj);
        // BizLoggerWechatPay.logPayRequest(outputStr);
         logger.info("API，POST过去的数据是：");
         logger.info(outputStr);
        
        StringBuffer buffer = new StringBuffer();
		try {
			sslContext.init(null, trustManager, new java.security.SecureRandom());
			URL url = new URL(requestUrl);
			
			// long t1 = System.currentTimeMillis();
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			// 从上述SSLContext对象中得到SSLSocketFactory对象
		 	SSLSocketFactory ssf = sslContext.getSocketFactory();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			result = buffer.toString();
			// logger.info("@@HttpsURLConnection post of HttpsRequestUtils.sendPost take time: " + (System.currentTimeMillis() - t1));
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.");
		} catch (Exception e) {
		    logger.error("https request error:{}", e);
		}
		return result;
    }

	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {

		String gson = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new SelfX509TrustManager()};
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			gson = buffer.toString();
		} catch (Exception e) {
			throw new BusinException("500",e.getMessage());
		}
		return gson;
	}
	/**
	 * 发起http get请求
	 *
	 * @param requestUrl
	 * @return
	 */
	public static byte[] httpRequestBytes(String requestUrl, String reqMethod, String postData) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream inputStream = null;
		try {


			// 获取输入流
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(600000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(600000);

			PostMethod post = new PostMethod(requestUrl);

			StringRequestEntity requestEntity = new StringRequestEntity(
					postData,
					"application/json",
					"UTF-8");
			post.setRequestEntity(requestEntity);

			httpClient.executeMethod(post);
			inputStream = post.getResponseBodyAsStream();

			// 读取返回结果
			byte[] buffer = new byte[1024];
			int index;
			while ((index = inputStream.read(buffer)) != -1) {
				out.write(buffer, 0, index);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinException("600",e.getMessage());
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new BusinException("600",e.getMessage());
				}
			}
		}
		return out.toByteArray();
	}

}
