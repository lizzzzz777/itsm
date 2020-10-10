package com.vlinksoft.ves.dutymanage.service;

import com.vlinksoft.ves.dutymanage.api.IDutyListApi;
import com.vlinksoft.ves.dutymanage.bo.DutyListVo;
import com.vlinksoft.ves.dutymanage.dao.DutyApproveDao;
import com.vlinksoft.ves.dutymanage.dao.DutyListDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.sequence.service.ISequence;

import java.util.List;

/**
 * Author:   nierf
 * Date:     2020/3/13 16:22
 */
public class DutyListApiImpl implements IDutyListApi {

    private DutyListDao dutyListDao;
    private ISequence seq;

    public DutyListDao getDutyListDao() {
        return dutyListDao;
    }

    public void setDutyListDao(DutyListDao dutyListDao) {
        this.dutyListDao = dutyListDao;
    }

    public ISequence getSeq() {
        return seq;
    }

    public void setSeq(ISequence seq) {
        this.seq = seq;
    }

    @Override
    public void insertDutyList(DutyListVo dutyListVo) {
        dutyListVo.setId(seq.next());
        dutyListDao.insertDutyList(dutyListVo);
    }

    @Override
    public void updateDutyList(DutyListVo dutyListVo) {
        dutyListDao.updateDutyList(dutyListVo);
    }

    @Override
    public void deleteDutyList(Long id) {
        dutyListDao.deleteDutyList(id);
    }

    @Override
    public List<DutyListVo> findAllDutyList(DutyListVo dutyListVo) {
        return  dutyListDao.findAllDutyList(dutyListVo);
    }


    @Override
    public DutyListVo findAllDutyListById(Long id) {
        return dutyListDao.findAllDutyListById(id);
    }

    @Override
    public void findAllDutyPage(Page<DutyListVo, DutyListVo> page) {
        dutyListDao.findAllDutyPage(page);
    }
}
