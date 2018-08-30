package io.ideploy.deployment.admin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author: code4china
 * @description:
 * @date: Created in 10:28 2018/8/28
 */
public class StringTest {

    @Test
    public void testIP(){
        String ip = "^(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})\\s*.*";
        Pattern pattern = Pattern.compile(ip);
        String str="192.168.1.1";
        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.matches());
        System.out.println(str.split("\\s")[0]);
    }

    @Test
    public void join(){
        String pattern="([A-Za-z0-9_$]{1,40}\\.?)+.*jar$";
        System.out.println("pay-impl*.jar".matches(pattern));
    }

}
