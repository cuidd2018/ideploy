package io.ideploy.deployment.storage.impl;

import io.ideploy.deployment.cfg.ModuleConfig;
import io.ideploy.deployment.cmd.AnsibleCommandResult;
import io.ideploy.deployment.cmd.CommandUtil;
import io.ideploy.deployment.storage.FileStorageUtil;
import io.ideploy.deployment.storage.ProjectFileStorage;
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

    private ModuleConfig moduleConfig;

    public LocalFileStorage(){
        moduleConfig = new ModuleConfig();
    }

    @Override
    public boolean exists(String filename) {
        return false;
    }

    @Override
    public void download(String filename,String compileHost, File file) {
        // 1. 判断本地有没有文件
        if (!isFileExist(file.getAbsolutePath())) {
            // 2. 如果没有，下载编译服务器 /data/storage/filename 到本地
            doDownload(filename, compileHost, file);
        }
    }

    @Override
    public boolean save(String source, String compileHost, String shell, String filename) {
        // 0. 编译脚本执行 cp 到 /data/storage/文件名
        // 1. scp 到本地
        // 2. 返回
        return doDownload(filename, compileHost, new File(FileStorageUtil.getLocalFileStorageName(filename)));
    }

    private boolean isFileExist(String filename) {
        File file = new File(FileStorageUtil.getLocalFileStorageName(filename));
        logger.info("检测文件是否存在：{},exist:{}", file.getAbsolutePath(),file.exists());
        return file.exists();
    }

    private boolean doDownload(String filename,String compileHost, File file) {
        String destFile = FileStorageUtil.getLocalFileStorageName(filename);

        logger.info("ansible下载编译文件到本地");
        String[] args = {"-i", compileHost, "all", "-m", "fetch", "-a",  "src=" + destFile + " dest=" + file.getAbsolutePath() + " flat=yes"};
        AnsibleCommandResult result= CommandUtil.execAnsible(CommandUtil.ansibleCmdArgs(args,1));
        logger.info("ansible下载文件结果:{}", result.isSuccess());
        return result.isSuccess();
    }
}
