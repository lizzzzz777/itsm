package com.vlinksoft.ves.dutymanage.dao.po;

/**
 * @Description
 * @Author liushy <1522903818@qq.com>
 * @Version ves6.x
 * @Since 1.0
 * @Date 2018/9/29 0029
 */
public class DutyCountPO {
    private String dutyMonth;
    private int userId;
    private String userName;
    private int dutyCount;
    private int state;//1,已值,2:未值

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDutyMonth() {
        return dutyMonth;
    }

    public void setDutyMonth(String dutyMonth) {
        this.dutyMonth = dutyMonth;
    }
}
