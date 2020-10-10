package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;
import java.util.Date;

public class InspectionPlanQuery implements Serializable {

      //计划是否启用
      private int isStart;

      //当前时间
      private Date nowTime;

      //巡检类型
      private int type;

      public Date getNowTime() {
            return nowTime;
      }

      public void setNowTime(Date nowTime) {
            this.nowTime = nowTime;
      }

      public int getType() {
            return type;
      }

      public void setType(int type) {
            this.type = type;
      }

      public int getIsStart() {
            return isStart;
      }

      public void setIsStart(int isStart) {
            this.isStart = isStart;
      }
}
