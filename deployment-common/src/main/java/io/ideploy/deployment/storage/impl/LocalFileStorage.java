package io.ideploy.deployment.storage.impl;

import io.ideploy.deployment.cfg.Configuration;
import io.ideploy.deployment.cmd.CommandResult;
import io.ideploy.deployment.cmd.LocalCommand;
import io.ideploy.deployment.common.util.IpAddressUtils;
import io.ideploy.deployment.storage.FileStorageUtil;
import io.ideploy.deployment.storage.ProjectFileStorage;
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
        else if(!file.exists()){
            /** 下载文件夹有临时文件，部署目录没有问题 **/
            LocalCommand localCommand  = new LocalCommand();
            File destFile = new File(FileStorageUtil.getLocalFileStorageName(filename));
            String[] cpShell = {"cp", "-rf", destFile.getAbsolutePath(), file.getAbsolutePath()};
            CommandResult result = localCommand.exec(cpShell);
            logger.info("复制文件到发布目录结果：{}", result.isSuccess());
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

        String compileServerIp= Configuration.getCompileServerIp();
        /*** 编译服务器在本地 ***/
        if(IpAddressUtils.isLocalIP(compileServerIp)){
            logger.info("编译服务器在本地:{}，直接cp文件 " + compileServerIp);
            LocalCommand localCommand  = new LocalCommand();
            String[] cpShell = {"cp", "-rf", destFile, file.getAbsolutePath()};
            CommandResult result= localCommand.exec(cpShell);
            logger.info("本地服务器下载文件: " + result.isSuccess());
            return result.isSuccess();
        }

        /*** 编译服务器在远程 ***/
        String[] scpShell = {"scp", "-P" + Configuration.getCompileServerSshPort(),
                "web@" + Configuration.getCompileServerIp() + ":" + destFile, file.getAbsolutePath()};

        LocalCommand localCommand  = new LocalCommand();
        CommandResult result = localCommand.exec(scpShell);
        logger.info("SCP命令是：" + StringUtils.join(scpShell, " "));
        logger.info("scp结果: " + result.isSuccess());
        return result.isSuccess();
    }
}
