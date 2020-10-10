package com.vlinksoft.ves.dutymanage.bo;

import java.io.Serializable;

/**
 * Created by zhaoyr on 2018/5/18.
 */
public class UserDutyInfoVo implements Serializable {
    private String dutyMonth;
    private String userId;
    private String userName;
    private int dutyCount;
    private int leaveCount;
    private int changeCount;
    private int isOnDuty;
    private int notOnDuty;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDutyCount() {
        return dutyCount;
    }

    public void setDutyCount(int dutyCount) {
        this.dutyCount = dutyCount;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public int getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(int changeCount) {
        this.changeCount = changeCount;
    }

    public int getIsOnDuty() {
        return isOnDuty;
    }

    public void setIsOnDuty(int isOnDuty) {
        this.isOnDuty = isOnDuty;
    }

    public int getNotOnDuty() {
        return notOnDuty;
    }

    public void setNotOnDuty(int notOnDuty) {
        this.notOnDuty = notOnDuty;
    }

    public String getDutyMonth() {
        return dutyMonth;
    }

    public void setDutyMonth(String dutyMonth) {
        this.dutyMonth = dutyMonth;
    }
}
