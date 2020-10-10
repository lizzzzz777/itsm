package com.vlinksoft.ves.dutymanage.bo;

import java.util.Date;

public class DutyWeekUserVo {

    private long id;
    private long dutyId;
    private long typeId;
    private long typeUserId;
    private long dutyTypeId;
    private String typeUserName;
    private long dutyState;
    private int dutyFlog;
    private Date dutyDate;   //值班时间
    private int startTime; //值班班次的开始时间
    private int overTime; //值班班次的结束时间
    private String typeName;
    private String dutyName;//值班名称
    private Integer arrangeState;//排班状态

    private Date bigenDate;
    private Date endDate;


    public int getOverTime() {
        return overTime;
    }

    public void setOverTime(int overTime) {
        this.overTime = overTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getTypeUserId() {
        return typeUserId;
    }

    public void setTypeUserId(long typeUserId) {
        this.typeUserId = typeUserId;
    }

    public long getDutyTypeId() {
        return dutyTypeId;
    }

    public void setDutyTypeId(long dutyTypeId) {
        this.dutyTypeId = dutyTypeId;
    }

    public String getTypeUserName() {
        return typeUserName;
    }

    public void setTypeUserName(String typeUserName) {
        this.typeUserName = typeUserName;
    }

    public long getDutyState() {
        return dutyState;
    }

    public void setDutyState(long dutyState) {
        this.dutyState = dutyState;
    }

    public int getDutyFlog() {
        return dutyFlog;
    }

    public void setDutyFlog(int dutyFlog) {
        this.dutyFlog = dutyFlog;
    }

    public Date getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public Integer getArrangeState() {
        return arrangeState;
    }

    public void setArrangeState(Integer arrangeState) {
        this.arrangeState = arrangeState;
    }

    public Date getBigenDate() {
        return bigenDate;
    }

    public void setBigenDate(Date bigenDate) {
        this.bigenDate = bigenDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DutyWeekUserVo{" +
                "id=" + id +
                ", dutyId=" + dutyId +
                ", typeId=" + typeId +
                ", typeUserId=" + typeUserId +
                ", dutyTypeId=" + dutyTypeId +
                ", typeUserName='" + typeUserName + '\'' +
                ", dutyState=" + dutyState +
                ", dutyFlog=" + dutyFlog +
                ", dutyDate=" + dutyDate +
                ", startTime=" + startTime +
                ", overTime=" + overTime +
                ", typeName='" + typeName + '\'' +
                ", dutyName='" + dutyName + '\'' +
                ", arrangeState='" + arrangeState + '\'' +
                ", bigenDate=" + bigenDate +
                ", endDate=" + endDate +
                '}';
    }
}
