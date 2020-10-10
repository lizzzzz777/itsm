package com.vlinksoft.ves.dutymanage.bo;


import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/12/26.
 */
public class DutyMain {
    private Long dutyId;
    private Date dutyDate;
    private Long listId;
    private List<String> getTypeId;
    private List<DutyTypeVo> dutyTypeVoList;
    private List<DutyMainUserVo> dutyMainUser;
    private  List<DutyWeekUserVo> dutyWeekUser;
    private String userName;
    private Integer dutyStates;


    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public Integer getDutyStates() {
        return dutyStates;
    }

    public void setDutyStates(Integer dutyStates) {
        this.dutyStates = dutyStates;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<DutyWeekUserVo> getDutyWeekUser() {
        return dutyWeekUser;
    }

    public void setDutyWeekUser(List<DutyWeekUserVo> dutyWeekUser) {
        this.dutyWeekUser = dutyWeekUser;
    }

    public List<DutyMainUserVo> getDutyMainUser() {
        return dutyMainUser;
    }

    public void setDutyMainUser(List<DutyMainUserVo> dutyMainUser) {
        this.dutyMainUser = dutyMainUser;
    }

    public List<DutyTypeVo> getDutyTypeVoList() {
        return dutyTypeVoList;
    }

    public void setDutyTypeVoList(List<DutyTypeVo> dutyTypeVoList) {
        this.dutyTypeVoList = dutyTypeVoList;
    }

    public List<String> getGetTypeId() {
        return getTypeId;
    }

    public void setGetTypeId(List<String> getTypeId) {
        this.getTypeId = getTypeId;
    }

    public Date getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    public Long getDutyId() {
        return dutyId;
    }

    public void setDutyId(Long dutyId) {
        this.dutyId = dutyId;
    }

}
