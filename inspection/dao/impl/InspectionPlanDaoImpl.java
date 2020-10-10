package com.vlinksoft.ves.inspection.dao.impl;

import com.vlinksoft.ves.inspection.bo.*;
import com.vlinksoft.ves.inspection.dao.IInspectionPlanDao;
import com.vlinksoft.ves.platform.dao.BaseDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InspectionPlanDaoImpl extends BaseDao<InspectionPlanVo> implements IInspectionPlanDao {

      @Autowired
      public InspectionPlanDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
            super(session, IInspectionPlanDao.class.getName());
      }

      @Override
      public long insertInspectionPlan(InspectionPlanVo inspectionPlan) {
            return getSession().insert("insertInspectionPlan",inspectionPlan);
      }

      @Override
      public long deleteInspectionPlans(List ids) {
            return getSession().delete("deleteInspectionPlan", ids);
      }

      @Override
      public int updateInspectionPlanStateLose(List planIds) {
            return getSession().update("updateInspePlanStateLose", planIds);
      }

      @Override
      public int updateInspectionPlanStateYes(List planIds) {
            return getSession().update("updateInspePlanStateYes", planIds);
      }

      @Override
      public long saveInspectionPlan(InspectionPlanVo inspectionPlan) {
            return getSession().update("updateSaveInspePlan",inspectionPlan);
      }

      @Override
      public String findMaxNumPlanSheetId(String nowValue) {
            String maxNum = getSession().selectOne("findMaxNumPlanSheetId", nowValue);
            if (maxNum != null&&maxNum.length() >0){
                  return maxNum.substring(9);
            }else {
                  return null;
            }
      }

      @Override
      public List<String> getAllPlanSheetIdByStaYes() {
            return getSession().selectList("getAllPlanSheetIdByStaYes");
      }

      @Override
      public int deleteNoInspectionPlans(List ids) {
            return getSession().update("deleteNoInspectionPlans", ids);
      }

      @Override
      public long insertInspectionPlanRule(InspectionPlanVo inspectionPlan) {
            return getSession().insert("insertInspectionPlanRule",inspectionPlan);
      }

      @Override
      public long deleteInspectionPlanRules(List planIds) {
            return getSession().delete("deleteInspectionPlanRules",planIds);
      }

      @Override
      public long deleteInspectionPlanInforms(List planIds) {
            return getSession().delete("deleteInspectionPlanInforms",planIds);
      }

      @Override
      public long deleteInspectionPlanAssets(List planIds) {
            return getSession().delete("deleteInspectionPlanAssets",planIds);
      }

      @Override
      public List<InspectionAssetVo> selectInspecPlanAsset(long planId) {
            return getSession().selectList("selectInspecPlanAsset", planId);
      }

      @Override
      public int insertInspePlanCycle(InspectionCycleVo inspectionCycle) {
            return getSession().insert("insertInspePlanCycle", inspectionCycle);
      }

      @Override
      public int updateInspePlanCycle(InspectionCycleVo inspectionCycle) {
            return getSession().update("updateInspePlanCycle", inspectionCycle);
      }

      @Override
      public InspectionCycleVo selectInspeCycleByPlanId(long planId) {
            return getSession().selectOne("selectInspeCycleByPlanId", planId);
      }

      @Override
      public int delInspeCycleByPlanId(long planId) {
            return getSession().delete("delInspeCycleByPlanId", planId);
      }

      @Override
      public List<InspectionPlanVo> selectAllInspePlan(Page page) {
            return getSession().selectList("selectAllInspePlan",page);
      }

      @Override
      public InspectionPlanVo selectInspePlanById(long Id) {
            return getSession().selectOne("selectInspePlanById", Id);
      }

      @Override
      public List<Long> selectPlanIdByTypeState(int type, int state) {
            Map<String,Object> map = new HashMap<>();
            map.put("type", type);
            map.put("state", state);
            map.put("nowTime", new Date().getTime());
            return getSession().selectList("selectPlanIdByTypeState", map);
      }

      @Override
      public List<Long> selectPlanIdByNowTime(InspectionPlanQuery query) {
            return getSession().selectList("selectPlanIdByNowTime",query);
      }

      @Override
      public long deleteInspectionPlanPeoTimes(List planIds) {
            return getSession().delete("deleteInspectionPlanPeoTimes",planIds);
      }

      @Override
      public List<InspectionUsersVo> selectInspePlanUser(long planId) {
            return getSession().selectList("selectInspePlanUser", planId);
      }

      @Override
      public long insertInspectionPlanPeoTime(InspectionPlanVo inspectionPlan) {
            return getSession().insert("insertInspectionPlanPeoTime",inspectionPlan);
      }

      @Override
      public long insertInspectionPlanInform(InspectionPlanVo inspectionPlan) {
            return getSession().insert("insertInspectionPlanInform", inspectionPlan);
      }

      @Override
      public long insertInspectionPlanAsset(InspectionPlanVo inspectionPlan) {
            return getSession().insert("insertInspectionPlanAsset",inspectionPlan);
      }

      @Override
      public List<String> selectPlanByMouldId(long mouldId) {
            return getSession().selectList("selectPlanByMouldId", mouldId);
      }

      @Override
      public List<InspectionPlanVo> selectInspectionPlanByMouldId(long mouldId) {
            return getSession().selectList("selectInspectionPlanByMouldId", mouldId);
      }
}
