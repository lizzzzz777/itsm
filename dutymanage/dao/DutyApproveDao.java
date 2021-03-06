package com.vlinksoft.ves.dutymanage.dao;

import com.vlinksoft.ves.dutymanage.Vo.DutyMyQuery;
import com.vlinksoft.ves.dutymanage.bo.DutyMain;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/1/4.
 */
public interface DutyApproveDao {
    void list(Page<DutyMy,DutyMyQuery> page);

    void leaveList(Page<DutyMy,DutyMyQuery> page);
    void leftList(Page<DutyMy,DutyMyQuery> page);
    void exchangeList(Page<DutyMy,DutyMyQuery> page);
    void exchangedList(Page<DutyMy,DutyMyQuery> page);

    int passApproveShift(DutyMy dutyMy);  //审核通过
    int passApproveNoShift(DutyMy dutyMy);  //审核不通过
    int passApproveLevel(DutyMy dutyMy);//请假同意
    int passApproveNoLevel(DutyMy dutyMy);//请假不同意
    int changeDutyUserState(long typeUserId, long dutyMainId,long dutyClassId,int dutyState); ////修改值班人员信息表

    DutyMy findApproveById(long id);

    int changeDutyUser(long dutyMainId, long dutyClassId, long dutyPeopleId,long shiftPeopleId,String shiftUserName);

    void myApplyList(Page page);

    DutyMy getShiftDutyInfoById(Long id);

    DutyMy getLeaveInfoById(long id);
}
