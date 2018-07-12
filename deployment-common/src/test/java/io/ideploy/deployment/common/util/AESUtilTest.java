package io.ideploy.deployment.common.util;

import io.ideploy.deployment.encrypt.DefaultAesEncoder;
import org.junit.Test;

/**
 * 详情 : aes 加密解密工具测试
 * <p>
 * 详细 :
 *
 * @author K-Priest 2018/2/6
 */
public class AESUtilTest {

    /**
     * 测试对称加密解密
     */
    @Test
    public void test() throws Exception {
        DefaultAesEncoder aes = new DefaultAesEncoder("corgi666");
        /*String password = "root";
        String encode = aes.encode(password);
        System.out.println(encode);*/
/*
        String decode = aes.decode("44BE57C66C1145E16AA7D4EBB4C4BE3B");
        System.out.println(decode);*/

        System.out.println(MysqlAESUtil.decrypt("corgi666", "44BE57C66C1145E16AA7D4EBB4C4BE3B"));
        //assert Objects.equals(password, decode);
    }
}
