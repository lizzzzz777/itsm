package com.vlinksoft.ves.dutymanage.bo;

import java.util.Date;

public class DutyLog {
    private Long id;     //值班日志表id
    private Long dutyId; //值班表id
    private Long dutyTypeId; //值班班次表id
    private Long subDutyUserId;//提交值班日志人id
    private String dutyLog; //值班日志
    private Date subDutyLogTime;    //提交值班日志时间
    private Integer logStatus;//状态0-日志禁用


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDutyId() {
        return dutyId;
    }

    public void setDutyId(Long dutyId) {
        this.dutyId = dutyId;
    }

    public Long getDutyTypeId() {
        return dutyTypeId;
    }

    public void setDutyTypeId(Long dutyTypeId) {
        this.dutyTypeId = dutyTypeId;
    }

    public Long getSubDutyUserId() {
        return subDutyUserId;
    }

    public void setSubDutyUserId(Long subDutyUserId) {
        this.subDutyUserId = subDutyUserId;
    }

    public String getDutyLog() {
        return dutyLog;
    }

    public void setDutyLog(String dutyLog) {
        this.dutyLog = dutyLog;
    }

    public Date getSubDutyLogTime() {
        return subDutyLogTime;
    }

    public void setSubDutyLogTime(Date subDutyLogTime) {
        this.subDutyLogTime = subDutyLogTime;
    }

    public Integer getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }



    @Override
    public String toString() {
        return "DutyLog{" +
                "id=" + id +
                ", dutyId=" + dutyId +
                ", dutyLog='" + dutyLog + '\'' +
                ", subDutyLogTime=" + subDutyLogTime +
                ", subDutyUserId=" + subDutyUserId +
                ", logStatus=" + logStatus +
                '}';
    }
}
