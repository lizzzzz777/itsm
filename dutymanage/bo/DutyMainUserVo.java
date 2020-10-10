package com.vlinksoft.ves.dutymanage.bo;

import java.util.Date;

/**
 * Created by admin on 2017/12/27.
 */
public class DutyMainUserVo {
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
    private int endTime ; //值班班次的结束时间
    private String typeName;
    private String dutyName;//值班名称

    private Date bigenDate;
    private Date endDate;

    private long contactsId;
    private String contactsName;
    private String contactsPhone;
    private String contactsEmail;
    private Integer dutyStatus;//0未开始  1上班中 2已下班 3迟到打卡
    private Date onDutyDate;
    private Date offDutyDate;


    public Date getOnDutyDate() {
        return onDutyDate;
    }

    public void setOnDutyDate(Date onDutyDate) {
        this.onDutyDate = onDutyDate;
    }

    public Date getOffDutyDate() {
        return offDutyDate;
    }

    public void setOffDutyDate(Date offDutyDate) {
        this.offDutyDate = offDutyDate;
    }

    public Integer getDutyStatus() {
        return dutyStatus;
    }

    public void setDutyStatus(Integer dutyStatus) {
        this.dutyStatus = dutyStatus;
    }

    public long getContactsId() {
        return contactsId;
    }

    public void setContactsId(long contactsId) {
        this.contactsId = contactsId;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
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

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
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
        return "DutyMainUserVo{" +
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
                ", endTime=" + endTime +
                ", typeName='" + typeName + '\'' +
                ", dutyName='" + dutyName + '\'' +
                ", bigenDate=" + bigenDate +
                ", endDate=" + endDate +
                '}';
    }
}
