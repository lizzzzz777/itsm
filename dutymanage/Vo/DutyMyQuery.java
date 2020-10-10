package com.vlinksoft.ves.dutymanage.Vo;

import com.vlinksoft.ves.dutymanage.bo.DutyMy;

import java.util.Date;

/**
 * Created by admin on 2018/3/16.
 */
public class DutyMyQuery extends DutyMy {
    private static final Long serialVersionUID = 1L;
    private String dutyPeople;

    public String getDutyPeople() {
        return dutyPeople;
    }

    public void setDutyPeople(String dutyPeople) {
        this.dutyPeople = dutyPeople;
    }

}
