package io.ideploy.deployment.encrypt;
import io.ideploy.deployment.common.util.MysqlAESUtil;

/**
 * @author: dylanli
 * @description:
 * @date: Created in 22:15 2018/6/12
 */
public class MysqlAesEncoder extends ValueEncoder{

    public final static String ENCODER_NAME = "MYSQL-AES";

    public MysqlAesEncoder(String encryptKey){
       super(encryptKey);
    }

    @Override
    public String encode(String str) {
        String s = "";
        try {
            s = MysqlAESUtil.encrypt(encryptKey, str);
        } catch (Exception e) {
            throw new RuntimeException("加密错误", e);
        }
        return s;
    }

    @Override
    public String decode(String str) {
        String s = "";
        try {
            s = MysqlAESUtil.decrypt(encryptKey, str);
        } catch (Exception e) {
            throw new RuntimeException("解密错误: " + str, e);
        }
        return s;
    }
}
