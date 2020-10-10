package com.vlinksoft.ves.dutymanage.Vo;

import java.io.Serializable;

/**
 * Created by zhaoyr on 2018/5/29.
 */
public class DutyTodayQuery implements Serializable {
    public final static long serialVersionUID = 1L;

    private String today;

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }
}
