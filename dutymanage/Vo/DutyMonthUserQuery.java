package com.vlinksoft.ves.dutymanage.Vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhaoyr on 2018/5/30.
 */
public class DutyMonthUserQuery extends DutyMonthUser{
    private Date startDate;
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
