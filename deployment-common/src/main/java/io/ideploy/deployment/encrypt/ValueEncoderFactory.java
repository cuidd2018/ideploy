package io.ideploy.deployment.encrypt;

/**
 * @author: dylanli
 * @description:
 * @date: Created in 23:56 2018/6/12
 */
public class ValueEncoderFactory {

    /***
     * 根据不同的名称处理不同的加密信息
     * @param name
     * @param encryptKey
     * @return
     */
    public static ValueEncoder getValueEncoder(String name, String encryptKey){
        if(MysqlAesEncoder.ENCODER_NAME.equalsIgnoreCase(name)){
            return new MysqlAesEncoder(encryptKey);
        }
        else if(DefaultAesEncoder.ENCODER_NAME.equalsIgnoreCase(name)){
            return new DefaultAesEncoder(encryptKey);
        }
        throw new IllegalArgumentException("ValueEncoder's name not exist");
    }

}
