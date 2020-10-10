package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;
import java.util.Date;

public class InspectionCycleVo implements Serializable {

      //计划Id
      private long planId;

      //计划更新的时间点
      private Date updateDate;

      //计划周期时间点
      private Date cycleDate;

      public long getPlanId() {
            return planId;
      }

      public void setPlanId(long planId) {
            this.planId = planId;
      }

      public Date getUpdateDate() {
            return updateDate;
      }

      public void setUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
      }

      public Date getCycleDate() {
            return cycleDate;
      }

      public void setCycleDate(Date cycleDate) {
            this.cycleDate = cycleDate;
      }
}
