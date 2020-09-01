package com.hopedove.commons.utils;
import org.bouncycastle.jcajce.provider.symmetric.AES;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA算法工具（目前只支持AES）
 * Created by Administrator on 2017/1/4.
 */
public class RSAUtils {

    public final static String AESKEY = "gimsgimsgimsgims";

    //加密、签名算法
    /**
     * 算法名称
     */
    private static final String AES_ALG = "AES";

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALG = "MD5withRSA";

    //生成钥匙对算法
    private static final String KEY_ALG = "RSA";

    private static final String PUBLIC_KEY = "publicKey";

    private static final String PRIVATE_KEY = "privateKey";

    /**
     * 填充类型
     */
    private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";

    private static final byte[] AES_IV = initIv("AES/CBC/PKCS5Padding");

    private static final Base64.Decoder decoder = Base64.getDecoder();

    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 加密内容
     *
     * @param content
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String encryptContent(String content, String encryptKey)
            throws Exception {
        return aesEncrypt(content, encryptKey, "UTF-8");

    }

    /**
     * 加密内容
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String encryptContent(String content, String encryptType, String encryptKey, String charset)
            throws Exception {
        if ("AES".equals(encryptType)) {
            return aesEncrypt(content, encryptKey, charset);
        }
        throw new Exception("目前不支持此算法类型encrypeType=" + encryptType);
    }

    /**
     * AES算法加密
     *
     * @param content
     * @param aesKey
     * @param charset
     * @return
     * @throws Exception
     */
    private static String aesEncrypt(String content, String aesKey, String charset)
            throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(1, new SecretKeySpec(aesKey.getBytes(), AES_ALG), iv);
            byte[] encryptBytes = cipher.doFinal(content.getBytes(charset));
            return new String(encoder.encode(encryptBytes), charset);
        } catch (Exception e) {
            throw new Exception("AES加密失败 Aescontent = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * 解密内容
     *
     * @param content
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String decryptContent(String content, String encryptKey)
            throws Exception {
        return aesDecrypt(content, encryptKey, "UTF-8");
    }

    /**
     * 解密内容
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String decryptContent(String content, String encryptType, String encryptKey, String charset)
            throws Exception {
        if ("AES".equals(encryptType)) {
            return aesDecrypt(content, encryptKey, charset);
        }
        throw new Exception("目前不支持此算法类型encrypeType=" + encryptType);
    }

    /**
     * AES算法解密
     *
     * @param content
     * @param key
     * @param charset
     * @return
     * @throws Exception
     */
    private static String aesDecrypt(String content, String key, String charset)
            throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(2, new SecretKeySpec(key.getBytes(), AES_ALG), iv);
            byte[] cleanBytes = cipher.doFinal(decoder.decode(content.getBytes()));
            return new String(cleanBytes, charset);
        } catch (Exception e) {
            throw new Exception("AES解密失败 Aescontent = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decoder.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        Signature signature = null;
        String sign = null;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        signature = Signature.getInstance(SIGNATURE_ALG);
        signature.initSign(privateK);
        signature.update(data);
        sign = new String(encoder.encode(signature.sign()));
        return sign;
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = decoder.decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        Signature signature = null;
        boolean isSuccess = false;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        signature = Signature.getInstance(SIGNATURE_ALG);
        signature.initVerify(publicK);
        signature.update(data);
        isSuccess = signature.verify(decoder.decode(sign.getBytes()));
        return isSuccess;
    }

    /**
     * 获取公钥、私钥对
     *
     * @return publicKey，privateKey
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> getKeys() throws NoSuchAlgorithmException {
        Map<String, String> keysMap = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALG);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        keysMap.put(PUBLIC_KEY, new String(encoder.encode(publicKey.getEncoded())));
        keysMap.put(PRIVATE_KEY, new String(encoder.encode(privateKey.getEncoded())));
        return keysMap;
    }

    /**
     * @param fullAlg
     * @return
     */
    private static byte[] initIv(String fullAlg) {
        try {
            Cipher cipher = Cipher.getInstance(fullAlg);
            int blockSize = cipher.getBlockSize();
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; i++) {
                iv[i] = 0;
            }
            return iv;
        } catch (Exception e) {
            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; i++) {
                iv[i] = 0;
            }
            return iv;
        }
    }

    public static void main(String[] args) {
        //获取钥匙对
        try {
            Map<String, String> map = RSAUtils.getKeys();
            System.out.println("publicKey:");
            System.out.println(map.get(PUBLIC_KEY));
            System.out.println("privateKey:");
            System.out.println(map.get(PRIVATE_KEY));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //签名算法测试
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCeEEnD3Qzqha9N9FHuBXQaC4N4scgN7PdlOTNMTkm2/g0nZQTueH0TxpsNYZKGR3kFAz/nWLD+STCUch32BY9MnFF/KZHtxmq2y8uVXawMhJK72sp+WllgdFbXIHSPeR0b+k+9Q8q2ACo2W4KvOMRMJrZ2QjDNzM3kDSoecRfwYwIDAQAB";

        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ4QScPdDOqFr030Ue4FdBoLg3ixyA3s92U5M0xOSbb+DSdlBO54fRPGmw1hkoZHeQUDP+dYsP5JMJRyHfYFj0ycUX8pke3GarbLy5VdrAyEkrvayn5aWWB0VtcgdI95HRv6T71DyrYAKjZbgq84xEwmtnZCMM3MzeQNKh5xF/BjAgMBAAECgYBXI5+nDRxrxluI3GK2l+cT3LkUNt6VXshc05YGQTq+WXyi9Twsw9wgpOMiE2KWCJ0dOFdokSqyZWMU03zU/ueqclHe54R07DyC/3YIIGKq6pPi/wu1qBOCPNxf0kITcgBE/JKRb/c79o4srV3UlLXXi6LV4YcOQHf+NLFNb7+jcQJBAOmk+YjEj1Pow4hDoyngTx351bgLtl83VsFhxfmwooumzMNksp9jiWkPLAgtIDqwVvs4aZL/WmV65lgdmzCzXFUCQQCtL/4HmoGEBuwxUF7DhvVhuxxj2zBsUrL0l9+TnSfWChehNENoWwQTo8vggKrSEug/5V/EQkX9H+ZOU5C/xNHXAkEAtcIgWTRIJ8eQrSWo/b/A7JnZl8aMxj+/nsrX/2lY2fkVm/9vMa12TSN/ZpDsXJun3uFAyBkcEovsO6o0e0tczQJAOSYPE38+ocyPAC7fNnJAYsGVurXHotcfSmsbUeCYFp858O4IGFWAYOK6EhkjRTMMBJmyIlckNUTjgJE+wKk3/QJBAN/E05QAv8YlS8NVfqylS9OCcbuH+pSkYJHc14P7+cNG2mrcWh5Y10QWhV+aHruutOLTtBQBN71tsvUu2l3O4Go=";

        String signData = "G20190001{\"userId\":\"1\",\"token\":\"TLPctAkHQAGVM1HbC9Jb8LRcnqlXEWEa0YUKgiKCGWrrePuRBPlntF4jndgQ9Daz\"}101001101010";

        try {
            String signStr = sign(signData.getBytes(), privateKey);
            System.out.println("签名:" + signStr);
            System.out.println("验签:" + verify(signData.getBytes(), publicKey, signStr));

            String key = "asdfghjklzxcvbnm";
            String uid = "1";
            String enStr = encryptContent(uid, key);
            System.out.println(enStr);
            System.out.println(decryptContent(enStr, key));


            System.out.println(encryptContent("123456", AESKEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
