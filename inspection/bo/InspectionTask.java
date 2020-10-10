package com.vlinksoft.ves.inspection.bo;


import java.io.Serializable;
import java.util.Date;
//任务表
public class InspectionTask implements Serializable {
    private Long id;    //主键
    private Long planId;  //计划id
    private String planSheetId;//计划编号
    private String taskSheetId;  //任务编号
    private String inspectionReport;    //巡检报告
    private Date reportTime;  //报告时间
    private String userName;    //巡检人
    private Long userId;    //巡检人ID
    private Long timeOver;  //巡检时限
    private Date inspectionStartTime;    //巡检开始时间
    private Date inspectionEndDate; //巡检结束时间
    private int serviceId;  //所属业务Id
    private int fileId;//巡检报告文件Id
    private int mouldId;//模板文件Id
    private Long remindTime;    //巡检提醒
    //是否邮箱提醒，0否；1是
    private int isEmail;
    //是否短信提醒，0否；1是
    private int isTelphone;
    //是否站内提醒
    private int isInsider;
    //巡检状态
    private InspectionTaskState taskState;

    //所属域Id
    private String domainId;

    public Long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Long remindTime) {
        this.remindTime = remindTime;
    }

    public int getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(int isEmail) {
        this.isEmail = isEmail;
    }

    public int getIsTelphone() {
        return isTelphone;
    }

    public void setIsTelphone(int isTelphone) {
        this.isTelphone = isTelphone;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getInspectionReport() {
        return inspectionReport;
    }

    public void setInspectionReport(String inspectionReport) {
        this.inspectionReport = inspectionReport;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTimeOver() {
        return timeOver;
    }

    public void setTimeOver(Long timeOver) {
        this.timeOver = timeOver;
    }

    public String getPlanSheetId() {
        return planSheetId;
    }

    public void setPlanSheetId(String planSheetId) {
        this.planSheetId = planSheetId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInspectionStartTime() {
        return inspectionStartTime;
    }

    public void setInspectionStartTime(Date inspectionStartTime) {
        this.inspectionStartTime = inspectionStartTime;
    }

    public Date getInspectionEndDate() {
        return inspectionEndDate;
    }

    public void setInspectionEndDate(Date inspectionEndDate) {
        this.inspectionEndDate = inspectionEndDate;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getIsInsider() {
        return isInsider;
    }

    public void setIsInsider(int isInsider) {
        this.isInsider = isInsider;
    }

    public String getTaskSheetId() {
        return taskSheetId;
    }

    public void setTaskSheetId(String taskSheetId) {
        this.taskSheetId = taskSheetId;
    }

    public InspectionTaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(InspectionTaskState taskState) {
        this.taskState = taskState;
    }

    @Override
    public String toString() {
        return "InspectionTask{" +
                "id=" + id +
                ", planId=" + planId +
                ", planSheetId='" + planSheetId + '\'' +
                ", taskSheetId='" + taskSheetId + '\'' +
                ", inspectionReport='" + inspectionReport + '\'' +
                ", reportTime=" + reportTime +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", timeOver=" + timeOver +
                ", inspectionStartTime=" + inspectionStartTime +
                ", inspectionEndDate=" + inspectionEndDate +
                ", serviceId=" + serviceId +
                ", fileId=" + fileId +
                ", mouldId=" + mouldId +
                ", remindTime=" + remindTime +
                ", isEmail=" + isEmail +
                ", isTelphone=" + isTelphone +
                ", isInsider=" + isInsider +
                ", taskState=" + taskState +
                '}';
    }

    public int getMouldId() {
        return mouldId;
    }

    public void setMouldId(int mouldId) {
        this.mouldId = mouldId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
}

