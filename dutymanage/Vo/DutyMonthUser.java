package com.vlinksoft.ves.dutymanage.Vo;

import java.io.Serializable;

/**
 * Created by zhaoyr on 2018/5/30.
 * 本月值班概览
 */
public class DutyMonthUser implements Serializable{

    public final static long serialVersionUID = 1L;

    private long userId;
    private String userName;
    private int dutySum;
    private int leaveSum;
    private int changeSum;
    private int hasDuty;
    private int unDuty;

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

    public int getDutySum() {
        return dutySum;
    }

    public void setDutySum(int dutySum) {
        this.dutySum = dutySum;
    }

    public int getLeaveSum() {
        return leaveSum;
    }

    public void setLeaveSum(int leaveSum) {
        this.leaveSum = leaveSum;
    }

    public int getChangeSum() {
        return changeSum;
    }

    public void setChangeSum(int changeSum) {
        this.changeSum = changeSum;
    }

    public int getHasDuty() {
        return hasDuty;
    }

    public void setHasDuty(int hasDuty) {
        this.hasDuty = hasDuty;
    }

    public int getUnDuty() {
        return unDuty;
    }

    public void setUnDuty(int unDuty) {
        this.unDuty = unDuty;
    }
}
