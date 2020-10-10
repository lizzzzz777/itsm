package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;
import java.util.Date;

public class InspectionUsersVo implements Serializable {

      //巡检时间，格式为：00：00
      private String startTime;

      private String endTime;

      private String timeOver;

      //巡检人ID
      private long userId;

      //巡检人名字
      private String userName;

      //联系人ID
      private long contactsId;

      //巡检人名称
      private String contactsName;

      //巡检类型时间表达式
      private String timeType;

      //巡检类型时间表达式
      private Date inspectDate;

      public Date getInspectDate() {
            return inspectDate;
      }

      public void setInspectDate(Date inspectDate) {
            this.inspectDate = inspectDate;
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

      public String getTimeType() {
            return timeType;
      }

      public void setTimeType(String timeType) {
            this.timeType = timeType;
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

      public String getTimeOver() {
            return timeOver;
      }

      public void setTimeOver(String timeOver) {
            this.timeOver = timeOver;
      }

      public long getUserId() {
            return userId;
      }

      public void setUserId(long userId) {
            this.userId = userId;
      }

      public String getUserName() {
            return userName;
      }

      public void setUserName(String userName) {
            this.userName = userName;
      }
}
