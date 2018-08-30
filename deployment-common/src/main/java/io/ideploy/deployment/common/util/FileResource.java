package io.ideploy.deployment.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: code4china
 * @description:
 * @date: Created in 18:16 2018/8/30
 */
public class FileResource {

    private FileResource(){

    }

    /***
     * 0-服务器资源文件 1-war包中的资源文件
     */
    private int resourceType;

    private String fileName;

    private String filePath;

    public static FileResource war(String filePath){
        FileResource fileResource= new FileResource();
        fileResource.filePath = filePath;
        fileResource.fileName = filePath;
        int pos= filePath.lastIndexOf(File.separator);
        if(pos != -1 && pos+1 < filePath.length()){
            fileResource.fileName= filePath.substring(pos+1);
        }
        fileResource.resourceType= 1;
        return  fileResource;
    }

    public static FileResource file(String filePath){
        FileResource fileResource= new FileResource();
        fileResource.filePath = filePath;
        fileResource.fileName = new File(filePath).getName();
        fileResource.resourceType= 0;
        return  fileResource;
    }

    public int getResourceType() {
        return resourceType;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getInputStream()throws IOException {
        if(resourceType == 0){
            return new FileInputStream(filePath);
        }
        else{
            return this.getClass().getClassLoader().getResourceAsStream(filePath);
        }
    }

}
