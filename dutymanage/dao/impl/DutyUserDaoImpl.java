package com.vlinksoft.ves.dutymanage.dao.impl;

import com.vlinksoft.ves.dutymanage.Vo.DutyTodayVo;
import com.vlinksoft.ves.dutymanage.Vo.DutyTypeVoQuery;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyTypeVo;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.dutymanage.dao.DutyUserDao;
import com.vlinksoft.ves.dutymanage.dao.po.DutyApproveCountPO;
import com.vlinksoft.ves.dutymanage.dao.po.DutyCountPO;
import com.vlinksoft.ves.platform.dao.BaseDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.apache.commons.collections.map.HashedMap;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/12/21.
 */
public class DutyUserDaoImpl extends BaseDao<DutyUserVo> implements DutyUserDao {

    @Override
    public int addDutyClass(DutyTypeVo typeVo) {
        return getSession().insert("addDutyClassInfoToTable",typeVo);
    }

    @Override
    public int insertShiftId(DutyUserVo dutyUserVo) {
        Map<String,Object> map = new HashMap<>();
        map.put("tid", dutyUserVo.getTypeId());
        map.put("list", dutyUserVo.getTypeUnit());
        return getSession().update("insertShiftIdTos",map);
    }

    @Override
    public int insertShiftIdUser(DutyUserVo dutyUserVo) {
        return getSession().insert("insertShiftIdUser",dutyUserVo);
    }

    @Override
    public void list(Page<DutyTypeVo,DutyTypeVoQuery> page) {
        this.select("selectDutyListType",page);
    }
    @Override
    public int delDutyClassType(String ids) {
        return getSession().delete("deleteShiftListTypeById",ids);
    }

    @Override
    public int delDutyClassUser(String ids) {
        return getSession().delete("deleteShiftListUserById",ids);
    }

    @Override
    public int updateDutyClassType(DutyTypeVo dutyTypeVo) {
        return getSession().update("updateDutyClassTypeByVo", dutyTypeVo);
    }

    @Override
    public int updateDutyClassUser(DutyUserVo dutyUserVo) {
        return 0;
    }

    @Override
    public int checkClassName(String name) {
        DutyTypeVo dutyUserVo=new DutyTypeVo();
        dutyUserVo.setName(name);
        int returnValue=getSession().selectOne("checkClassByName",dutyUserVo);
        if(returnValue==0){
            return  1;
        }else {
            return 0;
        }

    }

    @Override
    public int checkFlogClass(String classId) {
        /*DutyTypeVo dutyTypeVo=new DutyTypeVo();
        dutyTypeVo.setTypeId(Long.valueOf(classId));*/
        DutyMainUserVo dutyMainUserVo=new DutyMainUserVo();
        dutyMainUserVo.setDutyId(Long.valueOf(classId));
        dutyMainUserVo.setDutyDate(new Date());
        return getSession().selectOne("checkClassLine",dutyMainUserVo);
    }

    @Override
    public List<DutyUserVo> selectDutyUserByDutyId(long typeId) {
        return getSession().selectList("selectDutyUserByDutyId",typeId);
    }

    @Override
    public List<DutyTypeVo> dutyTypeList(int across,int dutyTypeId) {
        Map<String,Object> map = new HashMap<>();
        map.put("across",across);
        map.put("dutyTypeId",dutyTypeId);
        return getSession().selectList("dutyTypeListByAcross",map);
    }

    @Override
    public List<DutyTodayVo> selectDutyUserToday(Page page) {
        this.select("selectDutyUserToday",page);
        return getSession().selectList("selectDutyUserToday",page);
    }

    @Override
    public String getDepartmentNameByUserId(long userId) {
        String dd=getSession().selectOne("getDepartmentNameByUserId",userId);
        return getSession().selectOne("getDepartmentNameByUserId",userId);
    }

    @Override
    public List<DutyCountPO> summaryDutyCount(Date startDate, Date endDate) {
        Map<String,Date> param = new HashedMap(2);
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        return getSession().selectList(getNamespace()+"summaryDutyCount",param);
    }

    @Override
    public List<DutyApproveCountPO> summaryDutyApproveCount(Date startDate, Date endDate){
        Map<String,Date> param = new HashedMap(2);
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        return getSession().selectList(getNamespace()+"summaryDutyApproveCount",param);
    }

    @Override
    public List<DutyCountPO> getSummaryDutyCount(Long id, Date startDate, Date endDate) {
        Map param = new HashedMap(2);
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        param.put("id",id);
        return getSession().selectList(getNamespace()+"getSummaryDutyCount",param);
    }

    @Override
    public List<DutyApproveCountPO> getSummaryDutyApproveCount(Long id, Date startDate, Date endDate) {
        Map param = new HashedMap(2);
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        param.put("id",id);
        return getSession().selectList(getNamespace()+"getSummaryDutyApproveCount",param);
    }

    @Override
    public List<DutyTypeVo> selectByIds(List<Long> ids) {
        return getSession().selectList(getNamespace()+"selectByIds",ids);
    }

    @Override
    public List<DutyCountPO> getSummaryDutyCountByDomainId(Long domainId, Date startDate, Date endDate) {
        Map map = new HashMap();
        map.put("domainId",domainId);
        map.put("endDate",endDate);
        map.put("startDate",startDate);
        return getSession().selectList(getNamespace()+"getSummaryDutyCountByDomainId",map);
    }

    @Override
    public List<DutyApproveCountPO> getSummaryDutyApproveCountByDomainId(Long domainId, Date startDate, Date endDate) {
        Map map = new HashMap();
        map.put("domainId",domainId);
        map.put("endDate",endDate);
        map.put("startDate",startDate);
        return getSession().selectList(getNamespace()+"getSummaryDutyApproveCountByDomainId",map);
    }

    @Override
    public int checkClassName(String name, Long domainId) {
        Map map  = new HashMap();
        map.put("name",name);
        map.put("domainId",domainId);
        int returnValue=getSession().selectOne("checkClassByNameAndDomainId",map);
        if(returnValue==0){
            return  1;
        }else {
            return 0;
        }
    }

    @Override
    public void findDutyTypeByDomainId(Page<DutyTypeVo, DutyTypeVoQuery> page) {
        getSession().selectList("findDutyTypeByDomainId",page);
    }

    @Override
    public List<DutyTypeVo> dutyTypeList(int across, int dutyTypeId, Long domainId) {
        Map map = new HashMap();
        map.put("across",across);
        map.put("dutyTypeId",dutyTypeId);
        map.put("domainId",domainId);
        return getSession().selectList("findDutyListByDomainId",map);
    }


    @Autowired    //构造函数，写在最下面
    public DutyUserDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
        super(session, DutyUserDao.class.getName());
    }


}
