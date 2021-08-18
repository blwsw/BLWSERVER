package com.hopedove.coreserver.vo.core;

import com.hopedove.commons.vo.BasicVO;
import io.swagger.annotations.ApiModel;

@ApiModel("文件实体")
public class FileVO extends BasicVO {
    private Integer id;

    private String fileName;

    private String fullPath;

    private String fileExtName;

    private String path;

    private String groupName;

    private Integer businId;

    public Integer getBusinId() {
        return businId;
    }

    public void setBusinId(Integer businId) {
        this.businId = businId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
