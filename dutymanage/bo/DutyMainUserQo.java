package com.vlinksoft.ves.dutymanage.bo;

import java.util.Date;

/**
 * Author:   nierf
 * Date:     2020/4/21 13:42
 */
public class DutyMainUserQo {

    private Date startTime;
    private Date endTime;
    private long domainId;
    private long userId;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }
}
