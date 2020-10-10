package com.vlinksoft.ves.dutymanage.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Author:   nierf
 * Date:     2020/4/22 10:23
 */
public class DutyTypeJobVo implements Serializable {
    private long dutyId;
    private long typeId;
    private String typeName;
    private Date dutyDate;
    private String startTime;
    private String endTime;
    private Integer acrossDay;
    private String cron;
    private List<DutyMainUserVo> userVoList;


    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public long getDutyId() {
        return dutyId;
    }

    public void setDutyId(long dutyId) {
        this.dutyId = dutyId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Date getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getAcrossDay() {
        return acrossDay;
    }

    public void setAcrossDay(Integer acrossDay) {
        this.acrossDay = acrossDay;
    }

    public List<DutyMainUserVo> getUserVoList() {
        return userVoList;
    }

    public void setUserVoList(List<DutyMainUserVo> userVoList) {
        this.userVoList = userVoList;
    }

    @Override
    public String toString() {
        return "DutyTypeJobVo{" +
                "dutyId=" + dutyId +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", dutyDate=" + dutyDate +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", acrossDay=" + acrossDay +
                ", cron='" + cron + '\'' +
                ", userVoList=" + userVoList +
                '}';
    }
}
