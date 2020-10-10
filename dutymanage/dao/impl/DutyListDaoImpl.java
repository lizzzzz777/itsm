package com.vlinksoft.ves.dutymanage.dao.impl;

import com.vlinksoft.ves.dutymanage.bo.DutyListVo;
import com.vlinksoft.ves.dutymanage.bo.DutyMain;
import com.vlinksoft.ves.dutymanage.dao.DutyListDao;
import com.vlinksoft.ves.dutymanage.dao.DutyManageDao;
import com.vlinksoft.ves.platform.dao.BaseDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Author:   nierf
 * Date:     2020/3/13 16:23
 */
public class DutyListDaoImpl extends BaseDao<DutyListVo> implements DutyListDao {

    @Autowired    //构造函数，写在最下面
    public DutyListDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
        super(session, DutyListDao.class.getName());
    }


    @Override
    public void insertDutyList(DutyListVo dutyListVo) {
        getSession().insert("insertDutyList",dutyListVo);
    }

    @Override
    public void updateDutyList(DutyListVo dutyListVo) {
        getSession().update("updateDutyList",dutyListVo);
    }

    @Override
    public void deleteDutyList(Long id) {
        getSession().delete("deleteDutyListById",id);
    }

    @Override
    public List<DutyListVo> findAllDutyList(DutyListVo dutyListVo) {
        return this.getSession().selectList("findAllDutyList",dutyListVo);
    }

    @Override
    public void findAllDutyPage(Page<DutyListVo, DutyListVo> page) {
        this.getSession().selectList("findAllDutyPage",page);
    }

    @Override
    public DutyListVo findAllDutyListById(Long id) {
        return this.getSession().selectOne("findAllDutyListById",id);
    }
}
