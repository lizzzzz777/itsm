package com.vlinksoft.ves.inspection.bo;

import java.io.Serializable;

public class InspectionAssetVo implements Serializable {

      //关联资产ID
      private long assetId;

      //资产名字
      private String assetName;

      public long getAssetId() {
            return assetId;
      }

      public void setAssetId(long assetId) {
            this.assetId = assetId;
      }

      public String getAssetName() {
            return assetName;
      }

      public void setAssetName(String assetName) {
            this.assetName = assetName;
      }
}
