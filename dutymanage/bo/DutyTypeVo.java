package com.vlinksoft.ves.dutymanage.bo;

import java.util.List;

/**
 * Created by admin on 2017/12/21.
 */
public class DutyTypeVo {
    private Long typeId;
    private Long dutyId;
    private  String name;
    private String startTime;
    private String endTime;
    private String phone;
    private int acrossDay;//是否跨天1是0否
    private int flogClass;  //是否排班
    private Long domainId; //域ID

    //jwt 值班班次的开始时间
    private String typeStartTime;
    private String typeEndTime;

    public String getTypeStartTime() {
        return typeStartTime;
    }

    public void setTypeStartTime(String typeStartTime) {
        this.typeStartTime = typeStartTime;
    }

    public String getTypeEndTime() {
        return typeEndTime;
    }

    public void setTypeEndTime(String typeEndTime) {
        this.typeEndTime = typeEndTime;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getDutyId() {
        return dutyId;
    }

    public void setDutyId(Long dutyId) {
        this.dutyId = dutyId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getFlogClass() {
        return flogClass;
    }

    public void setFlogClass(int flogClass) {
        this.flogClass = flogClass;
    }

    private List<DutyUserVo> listMan;
    public List<DutyUserVo> getListMan() {
        return listMan;
    }

    public void setListMan(List<DutyUserVo> listMan) {
        this.listMan = listMan;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public int getAcrossDay() {
        return acrossDay;
    }

    public void setAcrossDay(int acrossDay) {
        this.acrossDay = acrossDay;
    }
}
