package com.vlinksoft.ves.dutymanage.bo;


import java.util.Date;

/**
 * 换班、请假申请记录表
 */
public class DutyMy {
    private long id;
    private long dutyPeopleId;  //换班、请假人id
    private String dutyType;   //换班或者请假
    private long  dutyClassId;  //换班，请假班次id
    private long shiftClassId; //换班班次id
    private long  shiftPeopleId; //换班人id
    private String reason;   //换班,请假原因
    private long dutyState; //我要申请的值班状态，0表示待审核，1表示已通过，2 表示已驳回
    private  String dutyOpinion; //审核意见
    private Date approveDate;    //申请日期
    private long dutyMainId;
    private long shiftMainId;
    private String noPassReason;
    //页面元素
    private String dutyPeople;
    private String shiftPeople;
    private String dutyClass;
    private String shiftClass;
    private Date dutyDate;
    private Date shiftDate;

    public String getNoPassReason() {
        return noPassReason;
    }

    public void setNoPassReason(String noPassReason) {
        this.noPassReason = noPassReason;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Date getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyClass() {
        return dutyClass;
    }

    public void setDutyClass(String dutyClass) {
        this.dutyClass = dutyClass;
    }

    public String getShiftClass() {
        return shiftClass;
    }

    public void setShiftClass(String shiftClass) {
        this.shiftClass = shiftClass;
    }

    public String getDutyPeople() {
        return dutyPeople;
    }

    public void setDutyPeople(String dutyPeople) {
        this.dutyPeople = dutyPeople;
    }

    public String getShiftPeople() {
        return shiftPeople;
    }

    public void setShiftPeople(String shiftPeople) {
        this.shiftPeople = shiftPeople;
    }


    public long getDutyPeopleId() {
        return dutyPeopleId;
    }

    public void setDutyPeopleId(long dutyPeopleId) {
        this.dutyPeopleId = dutyPeopleId;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public long getShiftClassId() {
        return shiftClassId;
    }

    public void setShiftClassId(long shiftClassId) {
        this.shiftClassId = shiftClassId;
    }

    public long getDutyClassId() {
        return dutyClassId;
    }

    public void setDutyClassId(long dutyClassId) {
        this.dutyClassId = dutyClassId;
    }

    public long getShiftPeopleId() {
        return shiftPeopleId;
    }

    public void setShiftPeopleId(long shiftPeopleId) {
        this.shiftPeopleId = shiftPeopleId;
    }

    public String getDutyOpinion() {
        return dutyOpinion;
    }

    public void setDutyOpinion(String dutyOpinion) {
        this.dutyOpinion = dutyOpinion;
    }

    public long getDutyState() {
        return dutyState;
    }

    public void setDutyState(long dutyState) {
        this.dutyState = dutyState;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getDutyMainId() {
        return dutyMainId;
    }

    public void setDutyMainId(long dutyMainId) {
        this.dutyMainId = dutyMainId;
    }

    public long getShiftMainId() {
        return shiftMainId;
    }

    public void setShiftMainId(long shiftMainId) {
        this.shiftMainId = shiftMainId;
    }
}

