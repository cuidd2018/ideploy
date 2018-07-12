package io.ideploy.deployment.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加密解密工具类
 * 
 * @author sky
 * 
 */
public class AESUtil {

	/**
	 * default charset
	 */
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 加密
	 * 
	 * 
	 * @param key 密钥
	 * @param src 需要加密的源
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String key, String src) throws Exception {
		byte[] rawKey = getRawKey(key.getBytes("UTF-8"));
		byte[] encrypted = encrypt(rawKey, src.getBytes(CHARSET_NAME));
		return HexStrUtil.parseByte2HexStr(encrypted);
	}

	/**
	 * 解密
	 * 
	 * @param key 密钥
	 * @param src 需要解密的源
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String key, String src) throws Exception {
		byte[] encrypted = HexStrUtil.parseHexStr2Byte(src);
		byte[] rawKey = getRawKey(key.getBytes("UTF-8"));
		byte[] result = decrypt(rawKey, encrypted);
		return new String(result, CHARSET_NAME);
	}

	/**
	 * 获取128位的加密密钥
	 * 
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");

		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		// 256 bits or 128 bits,192bits
		kgen.init(128, sr);
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	/**
	 * 真正的加密过程
	 * 
	 * @param key
	 * @param src
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(src);
		return encrypted;
	}

	/**
	 * 真正的解密过程
	 * 
	 * @param key
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
}