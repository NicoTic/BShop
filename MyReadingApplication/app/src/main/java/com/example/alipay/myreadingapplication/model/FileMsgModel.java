package com.example.alipay.myreadingapplication.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/20.
 */
public class FileMsgModel implements Serializable{
    private String fileName;
    private String fileSize;
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
