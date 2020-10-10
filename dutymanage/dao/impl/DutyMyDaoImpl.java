package com.vlinksoft.ves.dutymanage.dao.impl;
import com.vlinksoft.ves.dutymanage.bo.DutyMain;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;
import com.vlinksoft.ves.dutymanage.dao.DutyMyDao;
import com.vlinksoft.ves.platform.dao.BaseDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/1/4.
 */
public class DutyMyDaoImpl extends BaseDao<DutyMy> implements DutyMyDao {


    @Override
    public int insertShift(DutyMy dutyMy) {
        return getSession().insert("insertShiftInfo", dutyMy);
    }

    @Override
    public int insertLevel(DutyMy dutyMy) {
        return getSession().insert("insertLevelInfo", dutyMy);
    }

    @Override
    public DutyMainUserVo checkFlogDuty(long userId, Date dutyDate,long dutyTypeId) {
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        map.put("dutyDate", new SimpleDateFormat("yyyy-MM-dd").format(dutyDate));
        map.put("dutyTypeId",dutyTypeId);
        List<Object> userVoList = getSession().selectList("checkFlogDuty",map);
        if(userVoList != null && userVoList.size() > 0){
            return (DutyMainUserVo) userVoList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int changeState(long id,int state) {
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        map.put("state",state);
        return getSession().update("changeDutyState",map);
    }

    @Autowired    //构造函数，写在最下面
    public DutyMyDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
        super(session, DutyMyDao.class.getName());
    }
}
