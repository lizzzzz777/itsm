package com.vlinksoft.ves.inspection.bo;

import java.util.Date;

public class InspectionTaskQuery {
    private String inspectionReport;//巡检报告名|巡检计划名称
    private int inspStateSearch;
    private int timeStateSearch;
    private Long userId;       //当前登录用户Id
    //所属域Id
    private String domainId;
    //jwt 巡检开始事件
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getInspStateSearch() {
        return inspStateSearch;
    }

    public void setInspStateSearch(int inspStateSearch) {
        this.inspStateSearch = inspStateSearch;
    }

    public int getTimeStateSearch() {
        return timeStateSearch;
    }

    public void setTimeStateSearch(int timeStateSearch) {
        this.timeStateSearch = timeStateSearch;
    }

    public String getInspectionReport() {
        return inspectionReport;
    }

    public void setInspectionReport(String inspectionReport) {
        this.inspectionReport = inspectionReport;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
}
