package com.hopedove.coreserver.vo.core;

import com.hopedove.commons.vo.BasicVO;

public class FileUploadVO extends BasicVO {
    private String file;
    private byte[] fileBytes;
    private String fileName;
    private String fileType;
    private Integer len;

    public FileUploadVO(String file, String fileName, String fileType, Integer len) {
        this.file = file;
        this.fileName = fileName;
        this.fileType = fileType;
        this.len = len;
    }

    public FileUploadVO(byte[] fileBytes, String fileName, String fileType, Integer len) {
        this.fileBytes = fileBytes;
        this.fileName = fileName;
        this.fileType = fileType;
        this.len = len;
    }

    public FileUploadVO() {
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }
}
