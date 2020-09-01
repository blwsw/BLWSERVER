package com.hopedove.commons.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EncryptUtils {
	
	private static final Logger log = LoggerFactory.getLogger(EncryptUtils.class);
	
	private EncryptUtils() {}
	
	/**
	 * 进行MD5加密
	 * @param info 要加密的信息
	 * @return String 加密后的字符串
	 * @throws OpenSdkException 
	 */
	public static String encryptToMD5(String info) throws Exception {
		return encryptToMD5(info, null);
	}
	
	/**
	 * 进行MD5加密
	 * @param info 要加密的信息
	 * @param encoding 要加密信息的字符集
	 * @return String 加密后的字符串
	 * @throws OpenSdkException 
	 * @since 1.0.7
	 */
	public static String encryptToMD5(String info, Charset encoding) throws Exception {
		byte[] digesta = null;
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			if (encoding == null)
				alga.update(info.getBytes());
			else
				alga.update(info.getBytes(encoding));
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e);
		}
		// to string
		return byte2hex(digesta);
	}

	/**
	 * 将二进制转化为16进制字符串(小写)
	 * @param b 二进制字节数组
	 * @return string
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toLowerCase();
	}

	/**
	 * 十六进制字符串转化为2进制
	 * @param hex string
	 * @return 二进制字节数组
	 */
	public static byte[] hex2byte(String hex) {
		byte[] ret = new byte[8];
		byte[] tmp = hex.getBytes();
		for (int i = 0; i < 8; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * 将两个ASCII字符合成一个字节； 如：'EF' to '0xEF'
	 * @param src0 byte
	 * @param src1 byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	
	/**
	 * 获取一定长度的随机字符串
	 * @param length 指定字符串长度
	 * @return 一定长度的字符串
	 * @since 1.0.7
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String rsa256Sign(String content, String key) throws Exception {
		String SHA256withRSA = "SHA256withRSA";
		String CHARSET_UTF_8 = "UTF-8";
		try {
			//IPayUtil.getLogger().info("私钥字符串： "+key);
			PrivateKey priKey = restorePrivateKey(org.apache.commons.codec.binary.Base64.decodeBase64(key));
			Signature signature = Signature.getInstance(SHA256withRSA);
			signature.initSign(priKey);
			signature.update(content.getBytes(CHARSET_UTF_8));
			byte[] signed = signature.sign();
			String sign = new String(base64Encode(signed), CHARSET_UTF_8);
			System.out.println("生成签名sign : "+sign);
			return sign;
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + "; charset = " + CHARSET_UTF_8, e);
		}
	}
	public static byte[] base64Encode(byte[] inputByte) throws IOException {
		return org.apache.commons.codec.binary.Base64.encodeBase64(inputByte);
	}
	/**
	 * 还原私钥
	 *
	 * @param keyBytes
	 * @return
	 */
	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				keyBytes);
		try {
			String KEY_ALGORITHM = "RSA";
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory
					.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean checkSign(Map<String, String> map) throws Exception {
		String signStr = map.get("sign");
		//验签公钥
		String content = getSignContent(map, "sign");
		//验签
		//boolean result = rsa256CheckContent(content, signStr, publicKey);
		return true;
	}

	/**
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String generateSignature(Map<String, String> data, String key) throws Exception {
		String stringData = getSignContent(data, "sign");
		return rsa256Sign(stringData, key);
	}

	/**
	 * @param sortedParams
	 * @param values
	 * @return
	 */
	public static String getSignContent(Map<String, String> sortedParams, String... values) {
		for (String value : values) {
			sortedParams.remove(value);
		}
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = sortedParams.get(key);
			if (StringUtils.isNotBlank(key)
					&& StringUtils.isNotBlank(value)) {
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				++index;
			}
		}
		String stringData = content.toString();
		log.info("待签名或验签内容 : " + stringData);
		return stringData;
	}
}