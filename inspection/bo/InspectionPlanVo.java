package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InspectionPlanVo implements Serializable {

      public final static long serialVersionUID = 1L;

      //巡检计划ID
      private long id;

      //巡检计划ID
      private long planId;

      //计划编号
      private String planSheetId;

      //计划是否生效，1生效时间，0永久有效
      private int state;

      //计划开始时间
      private Date planStartTime;

      //计划结束时间
      private Date planEndTime;

      //巡检类型，0每天；1每周；2每月；3每季度
      private int type;

      //提醒时间
      private int remindTime;

      //是否邮箱提醒，0否；1是
      private int isEmail;

      //是否短信提醒，0否；1是
      private int isTelphone;

      //是否站内提醒
      private int isInsider;

      //巡检人巡检时间数据集合
      private List<InspectionUsersVo> userTimeList;

      //计划标题
      private String title;

      //计划巡检时限
      private String timeOver;

      //是否立即生成任务
      private int isNowTask;

      //巡检模板Id
      private int mouldId;

      //所属业务Id
      private int serviceId;

      //所属域Id
      private String domainId;

      public Integer getIsStart() {
            return isStart;
      }

      public void setIsStart(Integer isStart) {
            this.isStart = isStart;
      }

      //是否启用
      private Integer isStart;

      public int getIsNowTask() {
            return isNowTask;
      }

      public void setIsNowTask(int isNowTask) {
            this.isNowTask = isNowTask;
      }

      public long getId() {
            return id;
      }

      public void setId(long id) {
            this.id = id;
      }

      public long getPlanId() {
            return planId;
      }

      public void setPlanId(long planId) {
            this.planId = planId;
      }

      public String getPlanSheetId() {
            return planSheetId;
      }

      public void setPlanSheetId(String planSheetId) {
            this.planSheetId = planSheetId;
      }

      public int getState() {
            return state;
      }

      public void setState(int state) {
            this.state = state;
      }

      public Date getPlanStartTime() {
            return planStartTime;
      }

      public void setPlanStartTime(Date planStartTime) {
            this.planStartTime = planStartTime;
      }

      public Date getPlanEndTime() {
            return planEndTime;
      }

      public void setPlanEndTime(Date planEndTime) {
            this.planEndTime = planEndTime;
      }

      public int getType() {
            return type;
      }

      public void setType(int type) {
            this.type = type;
      }

      public int getRemindTime() {
            return remindTime;
      }

      public void setRemindTime(int remindTime) {
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

      public List<InspectionUsersVo> getUserTimeList() {
            return userTimeList;
      }

      public void setUserTimeList(List<InspectionUsersVo> userTimeList) {
            this.userTimeList = userTimeList;
      }

      public String getTitle() {
            return title;
      }

      public void setTitle(String title) {
            this.title = title;
      }

      //这里固定返回1，单位：小时
      public int getTimeOver() {
            return 1;
      }

      public void setTimeOver(String timeOver) {
            this.timeOver = timeOver;
      }

      public int getIsInsider() {
            return isInsider;
      }

      public void setIsInsider(int isInsider) {
            this.isInsider = isInsider;
      }

      public int getMouldId() {
            return mouldId;
      }

      public void setMouldId(int mouldId) {
            this.mouldId = mouldId;
      }

      public int getServiceId() {
            return serviceId;
      }

      public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
      }

      public String getDomainId() {
            return domainId;
      }

      public void setDomainId(String domainId) {
            this.domainId = domainId;
      }
}
