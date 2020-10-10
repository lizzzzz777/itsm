package com.vlinksoft.ves.dutymanage.dao.impl;

import com.vlinksoft.ves.dutymanage.Vo.DutyMyQuery;
import com.vlinksoft.ves.dutymanage.bo.DutyMain;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.dutymanage.dao.DutyApproveDao;
import com.vlinksoft.ves.platform.dao.BaseDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/1/4.
 */
public class DutyApproveDaoImpl extends BaseDao<DutyMy> implements DutyApproveDao{

    @Autowired    //构造函数，写在最下面
    public DutyApproveDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
        super(session, DutyApproveDao.class.getName());
    }

    @Override
    public void list(Page<DutyMy,DutyMyQuery> page) {
        this.select("approveList",page);
    }

    @Override
    public void leaveList(Page<DutyMy, DutyMyQuery> page) {
        this.select("leaveList",page);
    }

    @Override
    public void exchangeList(Page<DutyMy, DutyMyQuery> page) {
        this.select("exchangeList",page);
    }

    @Override
    public void leftList(Page<DutyMy, DutyMyQuery> page) {
        this.select("leftList",page);
    }

    @Override
    public void exchangedList(Page<DutyMy, DutyMyQuery> page) {
        this.select("exchangedList",page);
    }

    @Override
    public int passApproveShift(DutyMy dutyMy) {
        return getSession().update(getNamespace()+"passApproveShiftInfo",dutyMy);
    }

    @Override
    public int passApproveNoShift(DutyMy dutyMy) {
        return getSession().update("passApproveNoShiftInfo",dutyMy);
    }

    @Override
    public int passApproveLevel(DutyMy dutyMy) {
        return getSession().update("passApproveLevelInfo",dutyMy);
    }

    @Override
    public int passApproveNoLevel(DutyMy dutyMy) {
        return getSession().update("passApproveNoLevelInfo",dutyMy);
    }

    @Override
    public int changeDutyUserState(long typeUserId, long dutyMainId,long dutyClassId,int dutyState) {
        Map<String,Object> map=new HashMap<>();
        map.put("typeUserId",typeUserId);
        map.put("dutyId",dutyMainId);
        map.put("dutyClassId",dutyClassId);
        map.put("dutyState",dutyState);
        return getSession().update("changeDutyUserState",map);
    }

    @Override
    public DutyMy findApproveById(long id) {
        return getSession().selectOne("findApproveById",id);
    }

    @Override
    public int changeDutyUser(long dutyMainId, long dutyClassId, long dutyPeopleId,long shiftPeopleId,String shiftUserName) {
        Map<String,Object> map = new HashMap<>();
        map.put("dutyId",dutyMainId);
        map.put("dutyUserId",dutyPeopleId);
        map.put("dutyTypeId",dutyClassId);
        map.put("shiftUserId",shiftPeopleId);
        map.put("shiftUserName",shiftUserName);
        return getSession().update("updateDutyUser",map);
    }

    @Override
    public void myApplyList(Page page) {
        this.select("myApplyList",page);
    }

    @Override
    public DutyMy getShiftDutyInfoById(Long id) {
        return getSession().selectOne(getNamespace()+"getShiftDutyInfoById",id);
    }

    @Override
    public DutyMy getLeaveInfoById(long id) {
        return getSession().selectOne(getNamespace()+"getLeaveInfoById",id);
    }
}
