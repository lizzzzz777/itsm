package com.vlinksoft.ves.dutymanage.dao.po;

/**
 * @Description
 * @Author liushy <1522903818@qq.com>
 * @Version ves6.x
 * @Since 1.0
 * @Date 2018/9/29 0029
 */
public class DutyApproveCountPO {
    private String dutyMonth;
    private int userId;
    private String userName;
    private int approveCount;
    private String dutyType;//1,已值,2:未值

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

    public int getApproveCount() {
        return approveCount;
    }

    public void setApproveCount(int approveCount) {
        this.approveCount = approveCount;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getDutyMonth() {
        return dutyMonth;
    }

    public void setDutyMonth(String dutyMonth) {
        this.dutyMonth = dutyMonth;
    }
}
