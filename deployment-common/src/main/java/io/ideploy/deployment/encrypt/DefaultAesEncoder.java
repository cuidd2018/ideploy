package io.ideploy.deployment.encrypt;


import io.ideploy.deployment.common.util.AESUtil;

/**
 * AES实现加解密
 * @author linyi
 *
 */
public class DefaultAesEncoder extends ValueEncoder {

    public final static String ENCODER_NAME = "DEFAULT-AES";

    public final static String DEFAULT_KEY = "hello6666";

    public DefaultAesEncoder(String encryptKey){
        super(encryptKey);
    }

    @Override
    public String encode(String str) {
        String s = "";
		try {
			s = AESUtil.encrypt(encryptKey, str);
		} catch (Exception e) {
			throw new RuntimeException("加密错误", e);
		}
        return s;
    }

    @Override
    public String decode(String str) {
        String s = "";
		try {
			s = AESUtil.decrypt(encryptKey, str);
		} catch (Exception e) {
			throw new RuntimeException("解密错误: " + str, e);
		}
        return s;
    }

}

