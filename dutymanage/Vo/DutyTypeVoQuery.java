package com.vlinksoft.ves.dutymanage.Vo;

import com.vlinksoft.ves.dutymanage.bo.DutyTypeVo;

/**
 * Created by admin on 2018/3/16.
 */
public class DutyTypeVoQuery extends DutyTypeVo {
    private static final Long serialVersionUID = 1L;
    private String name;
    private Long domainId;

    @Override
    public Long getDomainId() {
        return domainId;
    }

    @Override
    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
