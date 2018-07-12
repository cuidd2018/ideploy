package io.ideploy.deployment.encrypt;

/**
 * 针对字符串进行加密、解密接口
 * @author tendy
 *
 */
public abstract class ValueEncoder {

    /**
     * the key
     */
    protected String encryptKey;

    public ValueEncoder(String encryptKey){
        this.encryptKey = encryptKey;
    }

    /**
     * 加密
     * @param str
     * @return 加密后的字符串
     */
    public abstract String encode(String str);
    
    /**
     * 解密
     * @param str
     * @return 解密后的字符串
     */
   public abstract String decode(String str);
}
