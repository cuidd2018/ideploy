package io.ideploy.deployment.cmd;

import com.google.common.collect.Lists;
import io.ideploy.deployment.base.ApiCode;
import io.ideploy.deployment.common.util.IpAddressUtils;
import io.ideploy.deployment.exception.ServiceException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author: code4china
 * @description:
 * @date: Created in 12:39 2018/8/28
 */
public class AnsibleArgs {

    private static final Logger logger= LoggerFactory.getLogger(AnsibleArgs.class);

    private static final Pattern HOST_LINE_PATTERN=  Pattern.compile("^(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})\\s*.*");

    private String[]localArgs;

    private String[]remoteArgs;

    public AnsibleArgs(String[] args){
        resolveHostFile(args);
    }

    public String[] resolveLocalArgs(){
        return localArgs;
    }

    public String[] resolveRemoteArgs(){
        return remoteArgs;
    }

    /***
     * 根据host file提取满足条件的ip列表（本地+远程）不同服务器，处理方式不一样
     * @param args
     * @return
     */
    private void resolveHostFile(String[] args){
        if(args == null || args.length == 0){
            return;
        }
        String hostPath= "";
        String groupName= "";
        int posPath= -1;
        int posGroup= -1;
        for(int i=0; i < args.length; i++){
            if("-i".equalsIgnoreCase(args[i])){
                posPath= i + 1;
                posGroup= i + 2;
                Assert.isTrue(posPath < args.length, "ansible host file not found");
                Assert.isTrue(posGroup < args.length, "ansible groupName not found");
                hostPath= args[posPath];
                groupName= args[posGroup];
                break;
            }
        }
        if(StringUtils.isBlank(hostPath)){
            logger.error("ansible 命令错误，args:{}", String.join(",", args));
            throw new ServiceException(ApiCode.FAILURE, "ansible 命令错误");
        }
        File hostFile = new File(hostPath);
        if(BooleanUtils.isFalse(hostFile.isFile() && hostFile.exists())){
            logger.error("ansible host 文件不存在，args:{}", String.join(",", args));
            throw new ServiceException(ApiCode.FAILURE, "ansible host 文件不存在");
        }

        List<String> groupIps= extractGroupIps(groupName, hostFile);
        if(CollectionUtils.isEmpty(groupIps)){
            return;
        }
        List<String> localIps= new ArrayList<>();
        List<String> remoteIps= new ArrayList<>();
        for(String ip: groupIps){
            if(IpAddressUtils.isLocalIP(ip)){
                localIps.add(ip);
                continue;
            }
            remoteIps.add(ip);
        }
        if(CollectionUtils.isEmpty(localIps)){
            remoteArgs= args;
            return;
        }

        localArgs= new String[args.length+2];
        localArgs[args.length]= "--connection";
        localArgs[args.length+1]= "local";
        for(int i=0; i<args.length; i++){
            if(i == posGroup){
                localArgs[i] = String.join(",", localIps)+ ",";
                if(CollectionUtils.isNotEmpty(remoteIps)){
                    remoteArgs[i] = String.join(",", remoteIps)+ ",";
                }
                continue;
            }

            localArgs[i]=args[i];
            if(CollectionUtils.isNotEmpty(remoteIps)){
                remoteArgs[i] = args[i];
            }
        }

        if(CollectionUtils.isNotEmpty(remoteIps)){
            remoteArgs= new String[args.length];
            for(int i=0; i<args.length; i++){
                if(i == posGroup){
                    if(CollectionUtils.isNotEmpty(remoteIps)){
                        remoteArgs[i] = String.join(",", remoteIps)+ ",";
                    }
                    continue;
                }
                remoteArgs[i] = args[i];
            }
        }
    }

    /***
     * 根据组名提取所有的ip地址
     * @param groupName
     * @param hostFile
     * @return
     */
    private List<String> extractGroupIps(String groupName, File hostFile){
        List<String> allIps= new ArrayList<>();
        /***
         * host file格式如下：
         * [all] //组名
         * 198.23.100.1  ansible_ssh_port = 22 ansible_ssh_user=web
         * 198.23.100.2  ansible_ssh_port = 22 ansible_ssh_user=web
         *
         * [test]  //组名
         * 168.25.100.1 ansible_ssh_port = 22 ansible_ssh_user=web
         * 168.25.100.2 ansible_ssh_port = 22 ansible_ssh_user=web
         */

        String matchName= "["+ groupName+ "]";
        String line = null;
        BufferedReader br= null;
        try{
            br= new BufferedReader(new InputStreamReader(new FileInputStream(hostFile)));
            while((line= br.readLine())!=null ){
                line= line.trim();
                if(line.length() == 0){
                    continue;
                }
                if(line.startsWith("[") && matchName.equals(line)){
                    fillGroupIps(br, allIps);
                }
            }
        }catch (Exception e){
            logger.error("extractAllIps异常，groupName:{}", groupName, e);
        }finally {
            if(br != null){
                try {
                    br.close();
                }catch (Exception e){
                    logger.error("", e);
                }
            }
        }
        return allIps;
    }

    /***
     * 填充一个组的ip
     * @param br
     * @param allIps
     * @throws Exception
     */
    private void fillGroupIps(BufferedReader br, List<String> allIps)throws Exception{
        String line= null;
        while ((line= br.readLine()) != null){
            line= line.trim();
            if(line.length() == 0){
                continue;
            }
            if(line.startsWith("[")){ //已经开始下一组ip的读取
                break;
            }
            if(HOST_LINE_PATTERN.matcher(line).matches()){
                String ip= line.split("\\s")[0];
                allIps.add(ip);
            }
        }

    }


}
