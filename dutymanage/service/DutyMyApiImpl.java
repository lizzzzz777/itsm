package com.vlinksoft.ves.dutymanage.service;

import com.vlinksoft.ves.dutymanage.api.IDutyMyApi;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;
import com.vlinksoft.ves.dutymanage.dao.DutyMyDao;
import com.vlinksoft.ves.platform.sequence.service.ISequence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/1/4.
 */
public class DutyMyApiImpl implements IDutyMyApi {
    private DutyMyDao dutyMyDao;
    private ISequence seq;



    @Override
    public Map<String,Long> insertShift(DutyMy dutyMy) {
        dutyMy.setId(seq.next());
        Map<String,Long>map=new HashMap<>(2);
        int i=dutyMyDao.insertShift(dutyMy);
        map.put("sid",dutyMy.getId());
        map.put("shiftValue",(long)i);
        return map;
    }

    @Override
    public int insertLevel(DutyMy dutyMy) {
        dutyMy.setId(seq.next());
        return dutyMyDao.insertLevel(dutyMy);
    }

    @Override
    public DutyMainUserVo checkFlogDuty(long userId, Date dutyDate,long dutyTypeId) {
        return dutyMyDao.checkFlogDuty(userId,dutyDate,dutyTypeId);
    }

    @Override
    public int changeState(long id,int state) {
        return dutyMyDao.changeState(id,state);
    }

    public DutyMyDao getDutyMyDao() {
        return dutyMyDao;
    }

    public void setDutyMyDao(DutyMyDao dutyMyDao) {
        this.dutyMyDao = dutyMyDao;
    }

    public ISequence getSeq() {
        return seq;
    }

    public void setSeq(ISequence seq) {
        this.seq = seq;
    }
}
