package com.vlinksoft.ves.dutymanage.service;

import com.vlinksoft.ves.dutymanage.Vo.DutyMyQuery;
import com.vlinksoft.ves.dutymanage.api.IDutyApproveApi;
import com.vlinksoft.ves.dutymanage.bo.DutyMain;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.dutymanage.dao.DutyApproveDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.sequence.service.ISequence;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/1/4.
 */
public class DutyApproveApiImpl implements IDutyApproveApi{
    private DutyApproveDao dutyApproveDao;
    private ISequence seq;

    @Override
    public void leaveList(Page<DutyMy, DutyMyQuery> page) {
        dutyApproveDao.leaveList(page);
    }

    @Override
    public void exchangeList(Page<DutyMy, DutyMyQuery> page) {
        dutyApproveDao.exchangeList(page);
    }

    @Override
    public void leftList(Page<DutyMy, DutyMyQuery> page) {
        dutyApproveDao.leftList(page);
    }

    @Override
    public void exchangedList(Page<DutyMy, DutyMyQuery> page) {
        dutyApproveDao.exchangedList(page);
    }

    @Override
    public void list(Page page) {
        dutyApproveDao.list(page);
    }

    @Override
    public int passApproveShift(DutyMy dutyMy) {
        return dutyApproveDao.passApproveShift(dutyMy);
    }

    @Override
    public int passApproveNoShift(DutyMy dutyMy) {
        return dutyApproveDao.passApproveNoShift(dutyMy);
    }

    @Override
    public int passApproveLevel(DutyMy dutyMy) {
        return dutyApproveDao.passApproveLevel(dutyMy);
    }

    @Override
    public int passApproveNoLevel(DutyMy dutyMy) {
        return dutyApproveDao.passApproveNoLevel(dutyMy);
    }

    @Override
    public int changeDutyUserState(long typeUserId, long dutyMainId,long dutyClassId,int dutyState) {
        return dutyApproveDao.changeDutyUserState(typeUserId,dutyMainId,dutyClassId,dutyState);
    }

    @Override
    public DutyMy findApproveById(long id) {
        return dutyApproveDao.findApproveById(id);
    }

    @Override
    public int changeDutyUser(long dutyMainId, long dutyClassId, long dutyPeopleId,long shiftPeopleId,String shiftUserName) {
        return dutyApproveDao.changeDutyUser(dutyMainId,dutyClassId,dutyPeopleId,shiftPeopleId,shiftUserName);
    }

    @Override
    public void myLeaveApplyList(Page page) {
        ((DutyMyQuery)page.getCondition()).setDutyType("请假");
        dutyApproveDao.myApplyList(page);
    }

    @Override
    public void myExchangeApplyList(Page page) {
        ((DutyMyQuery)page.getCondition()).setDutyType("换班");
        dutyApproveDao.myApplyList(page);
    }

    @Override
    public DutyMy getShiftDutyInfoById(Long id) {
        return dutyApproveDao.getShiftDutyInfoById(id);
    }

    @Override
    public DutyMy getLeaveInfoById(long id) {
        return dutyApproveDao.getLeaveInfoById(id);
    }

    public DutyApproveDao getDutyApproveDao() {
        return dutyApproveDao;
    }

    public void setDutyApproveDao(DutyApproveDao dutyApproveDao) {
        this.dutyApproveDao = dutyApproveDao;
    }

    public ISequence getSeq() {
        return seq;
    }

    public void setSeq(ISequence seq) {
        this.seq = seq;
    }


}
