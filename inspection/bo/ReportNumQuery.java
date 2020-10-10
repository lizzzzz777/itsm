package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;
import java.util.Date;

public class ReportNumQuery implements Serializable {

      private Date startTime;
      private Date endTime;
      private int userId;
      private InspectionTaskState state;
      private int serviceId;

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

      public int getUserId() {
            return userId;
      }

      public void setUserId(int userId) {
            this.userId = userId;
      }

      public InspectionTaskState getState() {
            return state;
      }

      public void setState(InspectionTaskState state) {
            this.state = state;
      }

      public int getServiceId() {
            return serviceId;
      }

      public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
      }
}
