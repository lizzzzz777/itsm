package com.vlinksoft.ves.dutymanage.bo;

public class DutyTypeUserVo {

    private Long typeId;
    private Long userId;
    private String userName;
    private int startTime; //值班班次的开始时间
    private int endTime;


    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int overTime) {
        this.endTime = overTime;
    }
}
