package com.hopedove.commons.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/***
 * 证书信任管理器（用于https请求）
 * @author feng_wei
 *
 * 证书管理器的作用就是让它信任我们指定的证书，
 * 本管理器的实现意味着信任所有证书，不管是否权威机构颁发
 *
 */
public class SelfX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {

	}

	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {

	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
