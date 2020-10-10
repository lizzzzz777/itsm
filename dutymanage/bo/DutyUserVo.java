package com.vlinksoft.ves.dutymanage.bo;

import java.util.List;

/**
 * Created by admin on 2017/12/21.
 */
public class DutyUserVo {
    private long id;
    private Long userId;
    private Long typeId;
    private String userName;
    private List<String> typeUnit;
    public List<String> getTypeUnit() {
        return typeUnit;
    }
    public void setTypeUnit(List<String> typeUnit) {
        this.typeUnit = typeUnit;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString(){
        return "ListMan [userId="+userId+",typeId="+typeId+",userName="+userName+"]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
