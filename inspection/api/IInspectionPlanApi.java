package com.vlinksoft.ves.inspection.api;

import com.vlinksoft.ves.inspection.bo.InspectionCycleVo;
import com.vlinksoft.ves.inspection.bo.InspectionPlanQuery;
import com.vlinksoft.ves.inspection.bo.InspectionPlanVo;
import com.vlinksoft.ves.inspection.bo.InspectionUsersVo;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IInspectionPlanApi {

      //创建计划
      long insertInspectionPlan(InspectionPlanVo inspectionPlan);

      //保存修改的计划
      long saveInspectionPlan(InspectionPlanVo inspectionPlan);

      //批量删除计划-----真删
      int delInspectionPlan(List planIds);

      //批量删除计划-----假删
      int delNoInspectionPlan(List<String> planIds);

      //批量修改计划状态----->转为失效
      int updateInspectionPlanStateLose(List planIds);

      //批量修改计划状态----->转为生效
      int updateInspectionPlanStateYes(List planIds);

      //查询巡检计划，不管生效还是失效
      List<InspectionPlanVo> selectAllInspePlan(Page page);

      //查询根据巡检计划Id查询出巡检计划所有内容
      InspectionPlanVo selectInspecPlanById(long id);

      //获取所有的生效中的计划编号
      List<String> getAllPlanSheetIdByStaYes();

      //计划周期表中插入计划的变更信息
      int insertInspePlanCycle(InspectionCycleVo inspectionCycle);

      //更新计划周期表中时间根据计划Id
      int updateInspePlanCycle(InspectionCycleVo inspectionCycle);

      //获取计划周期表中的计划周期信息根据计划Id
      InspectionCycleVo selectInspeCycleByPlanId(long planId);

      //删除计划周期表中的计划周期信息根据计划Id
      int delInspeCycleByPlanId(long planId);

      //根据巡检计划类型，计划状态返回有效的计划id
      List<Long> selectPlanIdByTypeState(int type,int state);

      //根据当前时间获取几天需要执行的计划Id集合
      List<Long> selectPlanIdByNowTime(InspectionPlanQuery query);

      //根据计划Id获取巡检人巡检时间的具体信息
      List<InspectionUsersVo> selectInspePlanUser(long planId);

      List<String> selectPlanByMouldId(long mouldId);

      List<InspectionPlanVo> selectInspectionPlanByMouldId(long mouldId);

}
