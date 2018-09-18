package io.ideploy.deployment.admin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author: code4china
 * @description:
 * @date: Created in 10:28 2018/8/28
 */
public class StringTest {

    private static final Pattern HOST_LINE_PATTERN=  Pattern.compile("^(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})\\s*,+");


    private static final String JAR_BOOT_PATTERN = "([A-Za-z0-9_$]{1,40}\\.?)+jar$";

    @Test
    public void testAnsible()throws Exception{
       /* Process process = Runtime.getRuntime().exec("ansible -i 127.0.0.1 all -m shell -a \"mkdir -p /data/project/shell/dev/stock-fundamentals/fundamentals-web/\" --connection local -f 5 -T 500");
        System.out.println(readMessage(process.getInputStream()));
        System.out.println(readMessage(process.getErrorStream()));*/
        System.out.println(HOST_LINE_PATTERN.matcher("127.0.0.1,").matches());
    }

    @Test
    public void testJar(){
        String appClass="com.tigerbrokers.stock.quote.TestService";
        System.out.println(appClass.trim().matches("([A-Za-z0-9_$]{1,40}\\.?)+jar$"));;
    }


    protected String readMessage(InputStream inputStream) throws IOException, InterruptedException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String s1;
            while ((s1 = reader.readLine()) != null) {
                builder.append(s1);
                System.out.println(s1);
            }
            return builder.toString();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
