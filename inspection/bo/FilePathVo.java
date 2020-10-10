package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;

public class FilePathVo implements Serializable {
    private Long id;
    private String annexId;//附件关联的ID
    private String filePath;//附件的地址
    private Long fileId;//附件id
    private int appendixType;//

    public int getAppendixType() {
        return appendixType;
    }

    public void setAppendixType(int appendixType) {
        this.appendixType = appendixType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnexId() {
        return annexId;
    }

    public void setAnnexId(String annexId) {
        this.annexId = annexId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
