package com.vlinksoft.ves.inspection.job;

import com.vlinksoft.ves.inspection.api.IInspectTaskApi;
import com.vlinksoft.ves.inspection.api.IInspectionPlanApi;
import com.vlinksoft.ves.inspection.bo.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component("planJob")
public class InspectPlanToTask {

      @Resource
      private IInspectionPlanApi inspectionPlanApi;
      @Resource
      private IInspectTaskApi inspectTaskApi;

//      @Scheduled(cron = "0 * * * * ?")
//      public void test(){
//
//      }

      /*
       * 每天执行一次；时间为每天的凌晨零点执行
       * */
      @Scheduled(cron = "0 0 0 * * ?")
      public void everydayPlanToTask() throws ParseException {
            SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currentDate = Calendar.getInstance();
//            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            String format = simFormat.format(currentDate.getTime());
            InspectionPlanQuery query = new InspectionPlanQuery();
            query.setNowTime(currentDate.getTime());
            query.setIsStart(1);
            query.setType(0);
            List<Long> planIdList = inspectionPlanApi.selectPlanIdByNowTime(query);
            for (Long planId : planIdList) {
                  InspectionPlanVo inspectionPlan = inspectionPlanApi.selectInspecPlanById(planId);
                  List<InspectionUsersVo> usersVoList = inspectionPlan.getUserTimeList();
                  for (InspectionUsersVo usersVo : usersVoList) {
                        String inspectDate = simFormat.format(usersVo.getInspectDate());
                        if (!(inspectDate).equals(format)){
                              continue;
                        }
                        String inspectUserStartTime = inspectDate+" "+usersVo.getStartTime();
                        String inspectUserEndTime = inspectDate+" "+usersVo.getEndTime();
                        boolean isExist = inspectTaskApi.checkRepeat(planId, usersVo.getUserId(), inspectUserStartTime+":00",inspectUserEndTime+":00");
                        if (isExist){
                              continue;
                        }else {
                              boolean isSuccess = createTask(inspectionPlan, usersVo, inspectUserStartTime,inspectUserEndTime);
                              if (!isSuccess){
                                    continue;
                              }
                        }
                  }
            }
      }

      /*
       * 每周一凌晨零点执行一次；
       * */
//      @Scheduled(cron = "0 0 0 ? * MON")
//      public void everyWeekPlanToTask(){
//            SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar nextWeek = Calendar.getInstance();
//            Calendar nextWeek2 = Calendar.getInstance();
//            nextWeek2.add(Calendar.DAY_OF_MONTH, 13);
//            nextWeek.add(Calendar.DAY_OF_MONTH, 7);
//            InspectionPlanQuery query = new InspectionPlanQuery();
//            query.setNowTime(nextWeek.getTime());
//            query.setIsStart(1);
//            query.setType(1);
//            List<Long> planIdList = inspectionPlanApi.selectPlanIdByNowTime(query);
//            query.setNowTime(nextWeek2.getTime());
//            List<Long> planIdList2 = inspectionPlanApi.selectPlanIdByNowTime(query);
//            planIdList2.removeAll(planIdList);
//            planIdList.addAll(planIdList2);
//            for (Long planId : planIdList) {
//                  InspectionPlanVo inspectionPlan = inspectionPlanApi.selectInspecPlanById(planId);
//                  List<InspectionUsersVo> usersVoList = inspectionPlan.getUserTimeList();
//                  for (InspectionUsersVo usersVo : usersVoList) {
//                        String timeType = usersVo.getTimeType();
//                        String[] timeTypeArray = timeType.split(",");
//                        int weekValue = Integer.parseInt(timeTypeArray[1]);
//                        nextWeek.add(Calendar.DAY_OF_MONTH, weekValue-1);
//                        String inspectUserStartTime = simFormat.format(nextWeek.getTime())+" "+usersVo.getStartTime();
//                        String inspectUserEndTime = simFormat.format(nextWeek.getTime())+" "+usersVo.getEndTime();
//                        boolean isExist = inspectTaskApi.checkRepeat(planId, usersVo.getUserId(), inspectUserStartTime,inspectUserEndTime);
//                        Date planEndTime = inspectionPlan.getPlanEndTime();
//                        Date planStartTime = inspectionPlan.getPlanStartTime();
//                        if (isExist||planEndTime.getTime()<nextWeek.getTime().getTime()||planStartTime.getTime()>nextWeek.getTime().getTime()){
//                              continue;
//                        }else {
//                              boolean isSuccess = createTask(inspectionPlan, usersVo, inspectUserStartTime);
//                              if (!isSuccess){
//                                    continue;
//                              }
//                        }
//                  }
//            }
//      }

      private boolean createTask(InspectionPlanVo inspectionPlan,InspectionUsersVo usersVo,String inspectUserTime,String inspectUserEndTime){
            InspectionTask inspectionTask = new InspectionTask();
            inspectionTask.setPlanId(inspectionPlan.getId());
            inspectionTask.setInspectionReport(inspectionPlan.getTitle());
            inspectionTask.setUserName(usersVo.getUserName());
            inspectionTask.setUserId(usersVo.getUserId());
            inspectionTask.setTimeOver(Long.valueOf(usersVo.getTimeOver()));
            inspectionTask.setRemindTime((long)inspectionPlan.getRemindTime());
            inspectionTask.setIsEmail(inspectionPlan.getIsEmail());
            inspectionTask.setIsTelphone(inspectionPlan.getIsTelphone());
            inspectionTask.setIsInsider(inspectionPlan.getIsInsider());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                  Date date = sdf.parse(inspectUserTime);
                  inspectionTask.setInspectionStartTime(date);
//                  Calendar cal = Calendar.getInstance();
//                  cal.setTime(date);
//                  cal.add(Calendar.HOUR, 1);// 24小时制
//                  cal.add(Calendar.HOUR, Integer.parseInt(usersVo.getTimeOver()));// 24小时制
//                  Date endDate = cal.getTime();
                  inspectionTask.setInspectionEndDate(sdf.parse(inspectUserEndTime));
            } catch (ParseException e) {
                  return false;
            }
            inspectionTask.setTaskState(InspectionTaskState.wait_writer);
            inspectionTask.setMouldId(inspectionPlan.getMouldId());
            inspectionTask.setServiceId(inspectionPlan.getServiceId());
            inspectionTask.setPlanSheetId(inspectionPlan.getPlanSheetId());
            inspectionTask.setDomainId(inspectionPlan.getDomainId());
            inspectTaskApi.autoCreateTask(inspectionTask);
            return true;
      }
}
