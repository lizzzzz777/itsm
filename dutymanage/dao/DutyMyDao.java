package com.vlinksoft.ves.dutymanage.dao;

import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;

import java.util.Date;

/**
 * Created by admin on 2018/1/4.
 */
public interface DutyMyDao {
    int insertShift(DutyMy dutyMy);   //换班信息插入数据表中
    int insertLevel(DutyMy dutyMy);   //换班信息插入数据表中
    DutyMainUserVo checkFlogDuty(long userId, Date  dutyDate,long dutyTypeId);  //根据人员id喝时间查询当前人是否有值班
    int changeState(long id,int state);  //更新值班人员的值班状态，看是否已经申请
}
