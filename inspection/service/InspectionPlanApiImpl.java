package com.vlinksoft.ves.inspection.service;

import com.vlinksoft.ves.inspection.api.IInspectTaskApi;
import com.vlinksoft.ves.inspection.api.IInspectionPlanApi;
import com.vlinksoft.ves.inspection.bo.*;
import com.vlinksoft.ves.inspection.dao.IInspectionPlanDao;
import com.vlinksoft.ves.platform.file.service.IFileClientApi;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InspectionPlanApiImpl implements IInspectionPlanApi {

      @Resource
      private IInspectionPlanDao inspectionPlanDao;
      @Resource
      private IInspectTaskApi inspectTaskApi;
      @Resource
      private IFileClientApi iFileClientApi;

      @Transactional
      @Override
      public long insertInspectionPlan(InspectionPlanVo inspectionPlan) {
            inspectionPlan.setPlanSheetId(this.getMaxNumber());
            inspectionPlanDao.insertInspectionPlan(inspectionPlan);//添加巡检计划
            inspectionPlan.setPlanId(inspectionPlan.getId());
            inspectionPlanDao.insertInspectionPlanPeoTime(inspectionPlan);//巡检信息详情
            inspectionPlanDao.insertInspectionPlanRule(inspectionPlan);//规则
            inspectionPlanDao.insertInspectionPlanInform(inspectionPlan);//计划
//            inspectionPlanDao.insertInspectionPlanAsset(inspectionPlan);
            return inspectionPlan.getId();
      }

      public String getMaxNumber(){
            SimpleDateFormat sim = new SimpleDateFormat("YYMMdd");
            String now = sim.format(new Date());
            String maxNum = inspectionPlanDao.findMaxNumPlanSheetId(now);
            if (maxNum != null&& !maxNum.equals("")){
                  return "PL"+now+String.format("%04d", Integer.valueOf(maxNum)+1);
            }else {
                  return "PL"+now+"0001";
            }
      }

      @Override
      public long saveInspectionPlan(InspectionPlanVo inspectionPlan) {
            inspectionPlanDao.saveInspectionPlan(inspectionPlan);
            ArrayList<Long> planIds = new ArrayList<>();
            planIds.add(inspectionPlan.getPlanId());
            inspectionPlanDao.deleteInspectionPlanPeoTimes(planIds);
            // jwt 过滤今天之前的计划
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            List<InspectionUsersVo> collect = inspectionPlan.getUserTimeList().stream().filter(e -> e.getInspectDate().getTime() > cal.getTimeInMillis()).collect(Collectors.toList());
            inspectionPlan.setUserTimeList(collect);

            inspectionPlanDao.insertInspectionPlanPeoTime(inspectionPlan);
            inspectionPlanDao.deleteInspectionPlanRules(planIds);
            inspectionPlanDao.insertInspectionPlanRule(inspectionPlan);
            inspectionPlanDao.deleteInspectionPlanInforms(planIds);
            inspectionPlanDao.insertInspectionPlanInform(inspectionPlan);
//            inspectionPlanDao.deleteInspectionPlanAssets(idList);
//            inspectionPlanDao.insertInspectionPlanAsset(inspectionPlan);
            return inspectionPlan.getPlanId();
      }

      @Override
      public int delInspectionPlan(List planIds) {
            inspectionPlanDao.deleteInspectionPlans(planIds);
            inspectionPlanDao.deleteInspectionPlanPeoTimes(planIds);
            inspectionPlanDao.deleteInspectionPlanRules(planIds);
            inspectionPlanDao.deleteInspectionPlanInforms(planIds);
//            inspectionPlanDao.deleteInspectionPlanAssets(planIds);
            return 200;
      }

      @Override
      public int delNoInspectionPlan(List<String> planIds) {
            for (int i = 0; i < planIds.size(); i++) {
                  Long planId = Long.valueOf(planIds.get(i));
                  List<InspectionTask> inspectionTasks = inspectTaskApi.selectInspectionTaskByPlanId(planId);
                  List<Long> fileId = new ArrayList<>();
                  for (int j = 0; j < inspectionTasks.size(); j++) {
                        InspectionTask inspectionTask = inspectionTasks.get(j);
                        if (inspectionTask.getFileId() != 0){
                              fileId.add((long)inspectionTask.getFileId());
                        }
                  }
                  try {
                        if (fileId.size()>0){
                              iFileClientApi.deleteFiles(fileId);
                        }
                  } catch (IOException e) {
                        e.printStackTrace();
                  }
                  inspectTaskApi.deleteInspectionReportByPlanId(planId);
            }
            return inspectionPlanDao.deleteNoInspectionPlans(planIds);
      }

      @Override
      public int updateInspectionPlanStateLose(List planIds) {
            return inspectionPlanDao.updateInspectionPlanStateLose(planIds);
      }

      @Override
      public int updateInspectionPlanStateYes(List planIds) {
            return inspectionPlanDao.updateInspectionPlanStateYes(planIds);
      }

      @Override
      public List<InspectionPlanVo> selectAllInspePlan(Page page) {
            List<InspectionPlanVo> inspectionPlanList = inspectionPlanDao.selectAllInspePlan(page);
            for (InspectionPlanVo inspectionPlanVo : inspectionPlanList) {
                  List<InspectionUsersVo> inspectionUsersList = inspectionPlanDao.selectInspePlanUser(inspectionPlanVo.getPlanId());
                  inspectionPlanVo.setUserTimeList(inspectionUsersList);
            }
            return inspectionPlanList;
      }

      @Override
      public InspectionPlanVo selectInspecPlanById(long id) {
            InspectionPlanVo inspectionPlanVo = inspectionPlanDao.selectInspePlanById(id);
            List<InspectionUsersVo> usersVoList = inspectionPlanDao.selectInspePlanUser(id);
//            List<InspectionAssetVo> assetVos = inspectionPlanDao.selectInspecPlanAsset(id);
            inspectionPlanVo.setUserTimeList(usersVoList);
//            inspectionPlanVo.setAssetList(assetVos);
            return inspectionPlanVo;
      }

      @Override
      public List<String> getAllPlanSheetIdByStaYes() {
            return inspectionPlanDao.getAllPlanSheetIdByStaYes();
      }

      @Override
      public int insertInspePlanCycle(InspectionCycleVo inspectionCycle) {
            return inspectionPlanDao.insertInspePlanCycle(inspectionCycle);
      }

      @Override
      public int updateInspePlanCycle(InspectionCycleVo inspectionCycle) {
            return inspectionPlanDao.updateInspePlanCycle(inspectionCycle);
      }

      @Override
      public InspectionCycleVo selectInspeCycleByPlanId(long planId) {
            return inspectionPlanDao.selectInspeCycleByPlanId(planId);
      }

      @Override
      public int delInspeCycleByPlanId(long planId) {
            return 0;
      }

      @Override
      public List<Long> selectPlanIdByTypeState(int type, int state) {
            return null;
      }

      @Override
      public List<Long> selectPlanIdByNowTime(InspectionPlanQuery query) {
            return inspectionPlanDao.selectPlanIdByNowTime(query);
      }

      @Override
      public List<InspectionUsersVo> selectInspePlanUser(long planId) {
            return inspectionPlanDao.selectInspePlanUser(planId);
      }

      @Override
      public List<String> selectPlanByMouldId(long mouldId) {
            return inspectionPlanDao.selectPlanByMouldId(mouldId);
      }

      @Override
      public List<InspectionPlanVo> selectInspectionPlanByMouldId(long mouldId) {
            return inspectionPlanDao.selectInspectionPlanByMouldId(mouldId);
      }
}
