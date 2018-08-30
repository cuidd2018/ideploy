package io.ideploy.deployment.common.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 详情 : 文件压缩的工具类
 * <p>
 * 详细 : 归档tar
 *
 * @author K-Priest 17/3/9
 */
public class FileCompressUtil {

    private static final Logger logger  = LoggerFactory.getLogger(FileCompressUtil.class);

    public static boolean archive(List<FileResource> fileResources, String tarFilePath) {
        try {
            TarArchiveOutputStream tao = new TarArchiveOutputStream(new FileOutputStream(tarFilePath));
            for (FileResource fileResource : fileResources) {
                archiveSingleFile(fileResource, tao);
            }
            return  true;
        } catch (IOException e) {
            logger.error("归档文件失败, {}", e);
        }
        return false;
    }

    private static void archiveSingleFile(FileResource fileResource, TarArchiveOutputStream taos) throws IOException{
        BufferedInputStream bis = null;
        try {
            TarArchiveEntry entry = new TarArchiveEntry(fileResource.getFileName());
            entry.setSize(fileResource.getInputStream().available());
            taos.putArchiveEntry(entry);

            bis = new BufferedInputStream(fileResource.getInputStream());
            int count;
            byte data[] = new byte[1024];
            while ((count = bis.read(data, 0, 1024)) != -1) {
                taos.write(data, 0, count);
            }

        } catch (IOException e) {
            logger.error("归档文件失败, {}", e);
            throw e;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("关闭流失败, {}", e);
                }
            }
            if (taos != null) {
                try {
                    taos.closeArchiveEntry();
                } catch (IOException e) {
                    logger.error("关闭归档输出流失败, {}", e);
                }
            }
        }
    }
}
