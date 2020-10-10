package com.vlinksoft.ves.inspection.controller;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.inspection.api.IInspectTaskApi;
import com.vlinksoft.ves.inspection.api.IInspectionMouldApi;
import com.vlinksoft.ves.inspection.api.IInspectionPlanApi;
import com.vlinksoft.ves.inspection.bo.*;
import com.vlinksoft.ves.platform.file.bean.FileGroupEnum;
import com.vlinksoft.ves.platform.file.service.IFileClientApi;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.workflow.um.eventmgr.bo.ItsmEventAttachment;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping({"/ves/inspeCont/inspePlan"})
public class InspectionPlanAction extends BaseAction {
      protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(InspectionPlanAction.class);

      @Resource
      private IInspectionPlanApi inspectionPlanApi;
      @Resource
      private IInspectTaskApi inspectTaskApi;
      @Resource
      private IInspectionMouldApi inspectionMouldApi;

      @Resource
      private AuditLogApi auditLogApi;
      @Resource
      private IFileClientApi iFileClient;

      //创建巡检计划
      @RequestMapping({"subInspectPlan"})
      @ResponseBody
      public JSONObject subInspectPlan(@RequestBody InspectionPlanVo inspectionPlan){
            inspectionPlan.setIsStart(1);
            inspectionPlan.setState(1);//有效时间状态
            inspectionPlan.setType(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            long planId = inspectionPlanApi.insertInspectionPlan(inspectionPlan);
            if (inspectionPlan.getIsNowTask()==1) {
                  try {
                        List<InspectionUsersVo> inspeUsers = inspectionPlan.getUserTimeList();
                        for (InspectionUsersVo inspeUser : inspeUsers) {
                              if (format.format(inspeUser.getInspectDate()).equals(format.format(new Date()))) {
                                    InspectionTask inspectionTask = new InspectionTask();
                                    inspectionTask.setPlanId(inspectionPlan.getId());
                                    inspectionTask.setInspectionReport(inspectionPlan.getTitle());
                                    inspectionTask.setUserName(inspeUser.getUserName());
                                    inspectionTask.setUserId(inspeUser.getUserId());
                                    inspectionTask.setTimeOver(Long.valueOf(inspeUser.getTimeOver()));
                                    inspectionTask.setRemindTime((long) inspectionPlan.getRemindTime());
                                    inspectionTask.setIsEmail(inspectionPlan.getIsEmail());
                                    inspectionTask.setIsTelphone(inspectionPlan.getIsTelphone());
                                    inspectionTask.setIsInsider(inspectionPlan.getIsInsider());
                                    List<Date> dates = this.newInspectionTime(inspectionPlan, inspeUser);//任务开始时间
                                    if (dates == null) {
                                          continue;
                                    } else {
                                          inspectionTask.setInspectionStartTime(dates.get(0));
//                              Calendar cal = Calendar.getInstance();
//                              cal.setTime(date);
//                              cal.add(Calendar.HOUR, Integer.parseInt(inspeUser.getTimeOver()));// 24小时制
//                              Date endDate = cal.getTime();
                                          inspectionTask.setInspectionEndDate(dates.get(1));//结束时间
//                              if (currentDate.after(dates.get(1))){
//                                    continue;
//                              }
                                    }
                                    inspectionTask.setTaskState(InspectionTaskState.wait_writer);
                                    inspectionTask.setMouldId(inspectionPlan.getMouldId());
                                    inspectionTask.setServiceId(inspectionPlan.getServiceId());
                                    inspectionTask.setPlanSheetId(inspectionPlan.getPlanSheetId());
                                    inspectionTask.setDomainId(inspectionPlan.getDomainId());
                                    inspectTaskApi.autoCreateTask(inspectionTask);
                              }
                        }
                  } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.toString());
                  }
            }
                  //审计日志统计  开始
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(planId+"");
            al.setFunctionModleId("PM");
            al.setOperationType("新建");
            al.setOperationContent("新建巡检计划:"+inspectionPlan.getTitle());
            try {
                  al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                  e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束
            return toSuccess(planId);
      }

      private List<Date> newInspectionTime(InspectionPlanVo inspectionPlan,InspectionUsersVo inspectionUser){

            List inspectionTimeList=new ArrayList<>();

            Date date = new Date();
            Calendar nextCalDate = Calendar.getInstance();//第二天日期
            nextCalDate.setTime(date);
            nextCalDate.add(Calendar.DAY_OF_MONTH,1);
            nextCalDate.set(Calendar.HOUR_OF_DAY,0);
            nextCalDate.set(Calendar.MINUTE,0);
            nextCalDate.set(Calendar.SECOND,0);
            nextCalDate.set(Calendar.MILLISECOND, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inspectionUser.getInspectDate());
            String[] userTimeArray = inspectionUser.getStartTime().split(":");

            int hour = Integer.parseInt(userTimeArray[0]);
            int minute = Integer.parseInt(userTimeArray[1]);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

//            if (!(nextCalDate.getTime().after(calendar.getTime()))){
//                  return null;
//            }
            //添加开始时间
            inspectionTimeList.add(calendar.getTime());

            String[] userEndTime = inspectionUser.getEndTime().split(":");
            int hours = Integer.parseInt(userEndTime[0]);
            int minutes = Integer.parseInt(userEndTime[1]);
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            //添加结束时间
            inspectionTimeList.add(calendar.getTime());

            return inspectionTimeList;
      }

      //修改保存巡检计划
      @RequestMapping({"saveInspectPlan"})
      @ResponseBody
      public JSONObject saveInspectPlan(@RequestBody InspectionPlanVo inspectionPlan){
            inspectionPlan.setState(1);//有效时间状态
            inspectionPlan.setType(0);
            long planId = inspectionPlanApi.saveInspectionPlan(inspectionPlan);
            return toSuccess(planId);
      }

      //批量删除巡检计划----真删
      @RequestMapping({"delInspectPlans"})
      @ResponseBody
      public JSONObject delInspectPlans(@RequestBody List<String> idList){
            return toSuccess(inspectionPlanApi.delInspectionPlan(idList));
      }

      //批量删除巡检计划----假删
      @RequestMapping({"delNoInspectPlans"})
      @ResponseBody
      public JSONObject delNoInspectPlans(@RequestBody List<String> idList){
            //审计日志统计  开始
            List<String>names=new ArrayList<>();
            List<Long>ids=new ArrayList<>();
            for(int i=0;i<idList.size();i++){
//                  ids.add(Long.parseLong(idList.get(i)));
                  names.add(inspectionPlanApi.selectInspecPlanById(Long.parseLong(idList.get(i))).getTitle());
            }
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(StringUtils.join(idList.toArray(), ","));
            al.setFunctionModleId("PM");
            al.setOperationType("删除");
            al.setOperationContent("删除巡检计划:"+StringUtils.join(names.toArray(), ","));
            try {
                  al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                  e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束


            return toSuccess(inspectionPlanApi.delNoInspectionPlan(idList));
      }

      //批量修改巡检计划状态，失效
      @RequestMapping({"updateInspePlanLose"})
      @ResponseBody
      public JSONObject updateInspePlanLose(@RequestBody List<String> idList){
            return toSuccess(inspectionPlanApi.updateInspectionPlanStateLose(idList));
      }

      //批量修改巡检计划状态，生效
      @RequestMapping({"updateInspePlanYes"})
      @ResponseBody
      public JSONObject updateInspePlanYes(@RequestBody List<String> idList){
            return toSuccess(inspectionPlanApi.updateInspectionPlanStateYes(idList));
      }

      //查询所有巡检计划，不论生效与失效，可分页，可以模糊查询
      @RequestMapping({"getAllInspePlan"})
      @ResponseBody
      public JSONObject getAllInspePlan(int pageSize, int pageNumber,String searchText,String domainId){
            Page page = new Page();
            int startNumber = (pageNumber - 1) * pageSize;
            page.setStartRow(startNumber);
            page.setRowCount(pageSize);

            Map<String,Object> cond = new HashMap<>();

            cond.put("searchText",searchText);

            cond.put("domainId",domainId);

            page.setCondition(cond);

            List<InspectionPlanVo> inspectionPlanList = inspectionPlanApi.selectAllInspePlan(page);
            ArrayList<String> lostPlanIds = new ArrayList<>();
            for (int i = 0; i < inspectionPlanList.size(); i++) {
                  InspectionPlanVo inspectionPlanVo = inspectionPlanList.get(i);
                  int state = inspectionPlanVo.getState();
                  if (state==0){
                        continue;
                  }
                  Date planEndTime = inspectionPlanVo.getPlanEndTime();
                  Date date = new Date();
                  if (date.getTime() > planEndTime.getTime()){
                        inspectionPlanVo.setIsStart(0);
                        lostPlanIds.add(inspectionPlanVo.getId()+"");
                  }
            }
            if(CollectionUtils.isNotEmpty(lostPlanIds)){
                  inspectionPlanApi.updateInspectionPlanStateLose(lostPlanIds);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rows", inspectionPlanList);
            jsonObject.put("total", page.getTotalRecord());
            jsonObject.put("page", page.getStartRow());
            return jsonObject;
      }

      //根据计划Id查询巡检计划的详情
      @RequestMapping({"getInspePlanById"})
      @ResponseBody
      public JSONObject getInspePlanById(long Id){
            return toSuccess(inspectionPlanApi.selectInspecPlanById(Id));
      }

      //获取所有的生效的计划编号
      @RequestMapping({"getAllPlanSheetIdYes"})
      @ResponseBody
      public JSONObject getAllPlanSheetIdYes(){
            return toSuccess(inspectionPlanApi.getAllPlanSheetIdByStaYes());
      }

      //上传添加巡检模板
      @RequestMapping({"uploadFileMould"})
      @ResponseBody
      public JSONObject uploadFileMould(HttpServletRequest httpRequest){
            MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
            MultipartFile file = request.getFile("file");
            String mouldName = request.getParameter("mouldName");
            String domainId = request.getParameter("domainId");
            InspectionMouldVo mould = new InspectionMouldVo();
            if (file != null ) {
                  CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) file;
                  try {
                        String fileName = commonsMultipartFile.getFileItem().getName();
                        //上传文件
                        long fileId = iFileClient.upLoadFile(FileGroupEnum.VES_SYSTEM, commonsMultipartFile).longValue();
                        mould.setCreateTime(new Date());
                        mould.setMouldName(mouldName);
                        mould.setMouldFileId(fileId);
                        mould.setMouldFileName(fileName);
                        mould.setState(1);//默认开启
                        mould.setDomainId(domainId);
                        inspectionMouldApi.addInspectionMould(mould);
                  } catch (Exception e) {
                        logger.error(e.getMessage());
                  }
            }else {
                  return toSuccess("添加模板失败！");
            }
            return toSuccess("添加模板成功！");
      }

      //更改巡检模板
      @RequestMapping({"updateFileMould"})
      @ResponseBody
      public JSONObject updateFileMould(HttpServletRequest httpRequest){
            MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
            MultipartFile file = request.getFile("file");
            String mouldName = request.getParameter("mouldName");
            String mouldId = request.getParameter("mouldId");
            InspectionMouldVo inspectionMouldVo = inspectionMouldApi.getInspectionMouldById(Long.valueOf(mouldId));
            inspectionMouldVo.setMouldName(mouldName);
            if (file != null ) {
                  CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) file;
                  try {
                        String fileName = commonsMultipartFile.getFileItem().getName();
                        //上传文件
                        long fileId = iFileClient.upLoadFile(FileGroupEnum.VES_SYSTEM, commonsMultipartFile).longValue();
                        iFileClient.deleteFile(inspectionMouldVo.getMouldFileId());
                        inspectionMouldVo.setMouldFileName(fileName);
                        inspectionMouldVo.setMouldFileId(fileId);
                        inspectionMouldApi.updateInspectionMouldInfo(inspectionMouldVo);
                  } catch (Exception e) {
                        logger.error(e.getMessage());
                  }
            }else {
                  return toSuccess("更改模板失败！");
            }
            return toSuccess("更改模板成功！");
      }

      @RequestMapping({"getInspectionMouldByPage"})
      @ResponseBody
      public JSONObject getInspectionMouldByPage(InspectionMouldQuery mouldQuery, int pageNumber, int pageSize){
            Page page = new Page<>();
            page.setCondition(mouldQuery);
            int startNumber = (pageNumber - 1) * pageSize;
            page.setStartRow(startNumber);
            page.setRowCount(pageSize);
            inspectionMouldApi.getInspectionMouldByPage(page);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rows", page.getDatas());
            jsonObject.put("total", page.getTotalRecord());
            jsonObject.put("page", page.getStartRow());
            return jsonObject;
      }

      @RequestMapping({"startInspectionMouldByIds"})
      @ResponseBody
      public JSONObject startInspectionMouldByIds(@RequestBody List<Integer> idList){
            inspectionMouldApi.startInspectionMouldByIds(idList);
            return toSuccess("修改成功！");
      }

      @RequestMapping({"stopInspectionMouldByIds"})
      @ResponseBody
      public JSONObject stopInspectionMouldByIds(@RequestBody List<Integer> idList){
            boolean isStart = false;
            if (idList != null && idList.size() > 0){
                  for (int i = 0; i < idList.size(); i++) {
                        List<InspectionPlanVo> inspectionPlanVos = inspectionPlanApi.selectInspectionPlanByMouldId(idList.get(i));
                        if (inspectionPlanVos != null && inspectionPlanVos.size() > 0){
                              for (int j = 0; j < inspectionPlanVos.size(); j++) {
                                    InspectionPlanVo inspectionPlanVo = inspectionPlanVos.get(j);
                                    if (inspectionPlanVo.getIsStart() == 1){
                                          isStart = true;
                                          break;
                                    }
                              }
                        }
                        if (isStart){
                              break;
                        }
                  }
            }
            if (isStart){
                  return toSuccess("修改失败，有启用中的计划使用此模板！");
            }else {
                  inspectionMouldApi.stopInspectionMouldByIds(idList);
                  return toSuccess("修改成功！");
            }
      }

      @RequestMapping({"deleteMouldByMouldId"})
      @ResponseBody
      public JSONObject deleteMouldByMouldId(@RequestBody List<Integer> idList){
            if (idList != null && idList.size()>0){
                  for (int i = 0; i < idList.size(); i++) {
                        inspectionMouldApi.deleteMouldAndPlanTaskById(idList.get(i));
                  }
            }
            return toSuccess("修改成功！");
      }

}
