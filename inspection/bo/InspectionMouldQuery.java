package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;

public class InspectionMouldQuery implements Serializable {

      private int state;
      private String mouldName;

      //所属域Id
      private String domainId;

      public int getState() {
            return state;
      }

      public void setState(int state) {
            this.state = state;
      }

      public String getMouldName() {
            return mouldName;
      }

      public void setMouldName(String mouldName) {
            this.mouldName = mouldName;
      }

      public String getDomainId() {
            return domainId;
      }

      public void setDomainId(String domainId) {
            this.domainId = domainId;
      }
}
