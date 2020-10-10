package com.vlinksoft.ves.dutymanage.bo;

import java.util.Date;
import java.util.List;

public class DutyWeek {

    private Long dutyId;
    private Date dutyDate;
    private List<String> getTypeId;
    private List<DutyTypeVo> dutyTypeVoList;
    private  List<DutyWeekUserVo> dutyWeekUser;
    private String userName;

    public Long getDutyId() {
        return dutyId;
    }

    public void setDutyId(Long dutyId) {
        this.dutyId = dutyId;
    }

    public Date getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    public List<String> getGetTypeId() {
        return getTypeId;
    }

    public void setGetTypeId(List<String> getTypeId) {
        this.getTypeId = getTypeId;
    }

    public List<DutyTypeVo> getDutyTypeVoList() {
        return dutyTypeVoList;
    }

    public void setDutyTypeVoList(List<DutyTypeVo> dutyTypeVoList) {
        this.dutyTypeVoList = dutyTypeVoList;
    }

    public List<DutyWeekUserVo> getDutyWeekUser() {
        return dutyWeekUser;
    }

    public void setDutyWeekUser(List<DutyWeekUserVo> dutyWeekUser) {
        this.dutyWeekUser = dutyWeekUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
