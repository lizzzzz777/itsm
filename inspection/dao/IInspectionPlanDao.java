package com.vlinksoft.ves.inspection.dao;

import com.vlinksoft.ves.inspection.bo.*;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInspectionPlanDao {

      /*
       * itsm_inspe_plan 表操作
       * */
      //添加巡检计划
      long insertInspectionPlan(InspectionPlanVo inspectionPlan);
      //批量删除巡检计划-----真删
      long deleteInspectionPlans(List ids);
      //修改巡检计划状态------->失效
      int updateInspectionPlanStateLose(List planIds);
      //修改巡检计划状态-------->生效
      int updateInspectionPlanStateYes(List planIds);
      //保存修改的巡检计划
      long saveInspectionPlan(InspectionPlanVo inspectionPlan);
      //获取最大的计划编号值
      String findMaxNumPlanSheetId(String nowValue);
      //获取所有的生效的就计划编号
      List<String> getAllPlanSheetIdByStaYes();
      //批量删除巡检计划------假删
      int deleteNoInspectionPlans(List ids);


      /*
       * itsm_plan_rule 表操作
       * */
      //添加计划规则信息
      long insertInspectionPlanRule(InspectionPlanVo inspectionPlan);
      //批量删除巡检计划规则信息
      long deleteInspectionPlanRules(List planIds);

      /*
       * itsm_inspe_peotime 表操作
       * */
      //添加巡检人巡检时间信息表
      long insertInspectionPlanPeoTime(InspectionPlanVo inspectionPlan);
      //批量删除巡检人巡检时间信息表
      long deleteInspectionPlanPeoTimes(List planIds);
      //根据Id获取巡检计划中巡检人巡检时间方面的信息
      List<InspectionUsersVo> selectInspePlanUser(long planId);


      /*
       * itsm_plan_inform 表操作
       * */
      //添加计划中信息
      long insertInspectionPlanInform(InspectionPlanVo inspectionPlan);
      //批量删除巡检计划中的信息
      long deleteInspectionPlanInforms(List planIds);


      /*
       * itsm_inspe_asset  表操作
       * */
      //添加巡检范围
      long insertInspectionPlanAsset(InspectionPlanVo inspectionPlan);
      //批量删除巡检计划中的巡检范围信息
      long deleteInspectionPlanAssets(List planIds);
      //根据Id获取巡检计划中资产方面的信息
      List<InspectionAssetVo> selectInspecPlanAsset(long planId);


      /*
       * itsm_inspe_cycle  表操作
       * */
      //添加计划周期信息
      int insertInspePlanCycle(InspectionCycleVo inspectionCycle);
      //更新计划周期信息
      int updateInspePlanCycle(InspectionCycleVo inspectionCycle);
      //获取计划周期表中的计划周期信息根据计划Id
      InspectionCycleVo selectInspeCycleByPlanId(long planId);
      //删除计划周期表中的计划周期信息根据计划Id
      int delInspeCycleByPlanId(long planId);




      /*
       * 多表联合查询
       * */
      //查询所有巡检计划
      List<InspectionPlanVo> selectAllInspePlan(Page page);
      //根据Id获取巡检计划所有的信息
      InspectionPlanVo selectInspePlanById(long Id);
      //根据巡检计划类型，计划状态返回有效的计划id
      List<Long> selectPlanIdByTypeState(int type,int state);
      //根据当前时间获取几天需要执行的计划Id集合
      List<Long> selectPlanIdByNowTime(InspectionPlanQuery query);

      List<String> selectPlanByMouldId(long mouldId);

      List<InspectionPlanVo> selectInspectionPlanByMouldId(long mouldId);
}
