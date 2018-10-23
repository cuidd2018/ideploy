package io.ideploy.deployment.admin.utils;

import com.alibaba.fastjson.JSON;
import io.ideploy.deployment.cmd.CommandResult;
import io.ideploy.deployment.cmd.LocalCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * @author: code4china
 * @description:
 * @date: Created in 14:52 2018/10/23
 */
public class RuntimeTest {

    @Test
    public void test()throws Exception{

        /*LocalCommand localCommand = new LocalCommand();
        CommandResult commandResult = localCommand.exec(new String[]{
                "/bin/sh",
                "/data/compile/project/shell/dev/stock-fundamentals/fundamental-web//compile_fundamental-web.sh"
        });
        System.out.println(JSON.toJSONString(commandResult));*/

        Process process = Runtime.getRuntime().exec(new String[]{
                "ansible",
                "-i",
                "193.112.104.227,",
                "all",
                "-m",
                "shell",
                "-a",
                "sh ~/test.sh"
        },new String[]{"LANG=UTF-8"});
        process.waitFor(3, TimeUnit.SECONDS);
       System.out.println(readMessage(process.getInputStream()));
        System.out.println(readMessage(process.getErrorStream()));
    }

    protected String readMessage(InputStream inputStream) throws Exception, InterruptedException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            StringBuilder builder = new StringBuilder();
            String s1;
            while ((s1 = reader.readLine()) != null) {
                builder.append(s1);
                //System.out.println(s1);
            }
            return builder.toString();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
