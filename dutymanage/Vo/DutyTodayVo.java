package com.vlinksoft.ves.dutymanage.Vo;

import java.io.Serializable;

/**
 * Created by zhaoyr on 2018/5/29.
 */
public class DutyTodayVo implements Serializable{

    public final static long serialVersionUID = 1L;
    private long userId;
    private String dutyName;
    private String dutyType;
    private String userMobile;
    private String departmentName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
