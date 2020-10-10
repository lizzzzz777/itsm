package com.vlinksoft.ves.inspection.dao.impl;

import com.vlinksoft.ves.inspection.bo.InspectionMouldVo;
import com.vlinksoft.ves.inspection.dao.IInspectionMouldDao;
import com.vlinksoft.ves.platform.dao.BaseDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InspectionMouldDaoImpl extends BaseDao<InspectionMouldVo> implements IInspectionMouldDao {

      @Autowired
      public InspectionMouldDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
            super(session, IInspectionMouldDao.class.getName());
      }

      @Override
      public void addInspectionMould(InspectionMouldVo mould) {
            getSession().insert("addInspectionMould", mould);
      }

      @Override
      public void getInspectionMouldByPage(Page page) {
            getSession().selectList(getNamespace()+"getInspectionMouldByPage", page);
      }

      @Override
      public void startInspectionMouldByIds(List<Integer> idList) {
            getSession().update(getNamespace()+"startInspectionMouldByIds", idList);
      }

      @Override
      public InspectionMouldVo getInspectionMouldById(long id) {
            return getSession().selectOne( getNamespace()+"getInspectionMouldById", id);
      }

      @Override
      public void deleteMouldAndPlanTaskById(long mouldId) {
            getSession().delete(getNamespace()+"deleteMouldAndPlanTaskById", mouldId);
      }

      @Override
      public void stopInspectionMouldByIds(List<Integer> idList) {
            getSession().update(getNamespace()+"stopInspectionMouldByIds", idList);
      }

      @Override
      public void updateInspectionMouldInfo(InspectionMouldVo mould) {
            getSession().update(getNamespace()+"updateInspectionMouldInfo", mould);
      }
}
