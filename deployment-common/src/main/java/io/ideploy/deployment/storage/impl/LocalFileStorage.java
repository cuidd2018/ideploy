package io.ideploy.deployment.storage.impl;

import io.ideploy.deployment.cfg.Configuration;
import io.ideploy.deployment.cmd.AnsibleCommand;
import io.ideploy.deployment.cmd.AnsibleCommandResult;
import io.ideploy.deployment.cmd.CommandResult;
import io.ideploy.deployment.cmd.CommandUtil;
import io.ideploy.deployment.cmd.LocalCommand;
import io.ideploy.deployment.common.util.IpAddressUtils;
import io.ideploy.deployment.storage.FileStorageUtil;
import io.ideploy.deployment.storage.ProjectFileStorage;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 功能：本地存储，针对编译服务器是 <strong>1台</strong> 的情况！<br/>
 * 详细：存储文件的位置见 {@link FileStorageUtil#getLocalFileStorageName(String)}
 *
 * @author linyi, 2017/5/8.
 */
public class LocalFileStorage implements ProjectFileStorage {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorage.class);

    @Override
    public boolean exists(String filename) {
        return false;
    }

    @Override
    public void download(String filename, File file) {
        // 1. 判断本地有没有文件
        if (!isFileExist(filename)) {
            // 2. 如果没有，scp 编译服务器 /data/storage/filename 到本地
            doDownload(filename, file);
        }
    }

    @Override
    public boolean save(String source, String shell, String filename) {
        // 0. 编译脚本执行 cp 到 /data/storage/文件名
        // 1. scp 到本地
        // 2. 返回
        return doDownload(filename, new File(FileStorageUtil.getLocalFileStorageName(filename)));
    }

    private boolean isFileExist(String filename) {
        File file = new File(FileStorageUtil.getLocalFileStorageName(filename));
        logger.info("检测文件是否存在：{}", file.getAbsolutePath());
        return file.exists();
    }

    private boolean doDownload(String filename, File file) {
        String destFile = FileStorageUtil.getLocalFileStorageName(filename);

        logger.info("ansible下载编译文件到本地");
        String[] args = {"-i", Configuration.getAnsibleHostFile(), "all", "-m", "fetch", "-a",  "src=" + destFile + " dest=" + file.getAbsolutePath() + " flat=yes"};

        String compileServerIp= Configuration.getCompileServerIp();
        /*** 编译服务器在本地 ***/
        if(IpAddressUtils.isLocalIP(compileServerIp)){
            args=Arrays.copyOf(args, args.length + 1);
            args[args.length]="remote_src=False";
        }
        AnsibleCommandResult result= CommandUtil.execAnsible(args);
        logger.info("ansible下载文件结果:{}", result.isSuccess());
        return result.isSuccess();
    }
}
