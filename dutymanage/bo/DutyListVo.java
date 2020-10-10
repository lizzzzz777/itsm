package com.vlinksoft.ves.dutymanage.bo;

import java.util.Date;

/**
 * Author:   nierf
 * Date:     2020/3/13 16:19
 */
public class DutyListVo {
    private Long id;
    private Date startTime;
    private Date endTime;
    private Long domainId;
    private Integer state;
    private Date afterStartTime;
    private Date afterEndTime;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getAfterStartTime() {
        return afterStartTime;
    }

    public void setAfterStartTime(Date afterStartTime) {
        this.afterStartTime = afterStartTime;
    }

    public Date getAfterEndTime() {
        return afterEndTime;
    }

    public void setAfterEndTime(Date afterEndTime) {
        this.afterEndTime = afterEndTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }
}
