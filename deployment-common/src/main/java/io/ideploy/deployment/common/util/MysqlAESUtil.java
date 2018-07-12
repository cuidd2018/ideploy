package io.ideploy.deployment.common.util;

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author: dylanli
 * @description: mysql默认的AES加/解密，为了便于部署提供AES加密方法
 * @date: Created in 22:00 2018/6/12
 */
public class MysqlAESUtil {

    public static String decrypt(String key, String src) throws Exception {
        byte[] keyBytes = Arrays.copyOf(key.getBytes("UTF-8"), 16);

        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher decipher = Cipher.getInstance("AES");

        decipher.init(Cipher.DECRYPT_MODE, secretKey);

        //char[] cleartext = src.toCharArray();

        byte[] decodeHex = HexStrUtil.parseHexStr2Byte(src);

        byte[] ciphertextBytes = decipher.doFinal(decodeHex);

        return new String(ciphertextBytes);
    }

    public static String encrypt(String key, String src) throws Exception {
        byte[] keyBytes = Arrays.copyOf(key.getBytes("ASCII"), 16);

        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] cleartext = src.getBytes("UTF-8");
        byte[] ciphertextBytes = cipher.doFinal(cleartext);

        return HexStrUtil.parseByte2HexStr(ciphertextBytes);
    }
}
