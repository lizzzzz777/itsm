package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;
import java.util.Date;

public class InspectionMouldVo implements Serializable {

    private int id;
    private String mouldName;
    private Date createTime;
    private int state;
    private long mouldFileId;
    private String mouldFileName;

    private String domainId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMouldName() {
        return mouldName;
    }

    public void setMouldName(String mouldName) {
        this.mouldName = mouldName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getMouldFileId() {
        return mouldFileId;
    }

    public void setMouldFileId(long mouldFileId) {
        this.mouldFileId = mouldFileId;
    }

    public String getMouldFileName() {
        return mouldFileName;
    }

    public void setMouldFileName(String mouldFileName) {
        this.mouldFileName = mouldFileName;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
}
