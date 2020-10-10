package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.dutymanage.api.IDutyListApi;
import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.bo.*;
import com.vlinksoft.ves.dutymanage.job.InitDutyJob;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.platform.web.vo.IRole;
import com.vlinksoft.ves.system.um.role.api.IRoleApi;
import com.vlinksoft.ves.system.um.user.api.IUserApi;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import com.vlinksoft.ves.workflow.um.workflow.vo.ProcessDeployVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by admin on 2017/11/27.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/dutyView"})
public class DutyViewAction extends BaseAction {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DutyViewAction.class);
    private IDutyManageApi iDutyManageApi;
    private IUserApi iUserApi;
    private IRoleApi iRoleApi;

    @Resource
    private IDutyListApi iDutyListApi;

    @Resource
    private AuditLogApi auditLogApi;

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;


    /**
     * 取消顺延
     * @param id
     * @return
     */
    @RequestMapping({"updateDutyList"})
    @ResponseBody
    public JSONObject updateDutyList(Long id){
        DutyListVo dutyListVo = iDutyListApi.findAllDutyListById(id);
        dutyListVo.setState(0);
        iDutyListApi.updateDutyList(dutyListVo);
        DutyTypeVo dutyTypeVo = new DutyTypeVo();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(dutyListVo.getAfterStartTime());
        String endTime = sdf.format(dutyListVo.getAfterEndTime());
        dutyTypeVo.setStartTime(startTime);
        dutyTypeVo.setEndTime(endTime);
        dutyTypeVo.setDomainId(dutyListVo.getDomainId());
        List<DutyMainUserVo> dutyMains1 = iDutyManageApi.findDutyMainUserListByDate(startTime,endTime,id);
        List<Long> list = new ArrayList<>();
        for (int i = 0;i<dutyMains1.size();i++){
            DutyMainUserVo dutyMain =  dutyMains1.get(i);
            list.add(dutyMain.getId());
            iDutyManageApi.deleteRecordByDutyId(dutyMain.getDutyId());
        }
        iDutyManageApi.delLineUser(list);
        iDutyManageApi.deleteDutyMainByDate(startTime,endTime,dutyListVo.getId());
        return toSuccess("ok");
    }

    @RequestMapping({"delDutyListByIds"})
    @ResponseBody
    public JSONObject delDutyListByIds(String ids){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] idList = ids.split(",");
        for(String id:idList){
            Long listId= Long.valueOf(id);
            DutyListVo dutyListVo = iDutyListApi.findAllDutyListById(listId);
            iDutyListApi.deleteDutyList(listId);
            List<DutyMain> dutyMains = iDutyManageApi.findDutyMainByListId(listId);
            for(DutyMain dutyMain:dutyMains){
                List<DutyMainUserVo> dutyMainUserVos = iDutyManageApi.findDutyMainUserListByDutyId(dutyMain.getDutyId());
                List<Long> userIds = new ArrayList<>();
                for(DutyMainUserVo dutyMainUserVo:dutyMainUserVos){
                    userIds.add(dutyMainUserVo.getId());
                }
                iDutyManageApi.delLineUser(userIds);
                iDutyManageApi.deleteRecordByDutyId(dutyMain.getDutyId());
            }
            iDutyManageApi.deleteDutyMainByListId(listId);

            //删除定时任务
            Date currentDate = new Date();
            List<DutyTypeJobVo> typeList = iDutyManageApi.findDutyMainTypeListByCDate(sdf.format(currentDate));
            for(DutyTypeJobVo dutyTypeJobVo:typeList){
                if (id.equals(dutyTypeJobVo.getTypeId()+"")){
                    continue;
                }
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                JobKey jobKey = JobKey.jobKey(dutyTypeJobVo.getTypeId()+"", dutyTypeJobVo.getDutyId()+"");
                try {
                    scheduler.deleteJob(jobKey);
                } catch (SchedulerException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return toSuccess("ok");
    }



        /*
    *向main的两个表中插入数据
     */
    @RequestMapping({"insertMains"})
    @ResponseBody
    public JSONObject insertMains(String obj,Long domainId,String afterStartTime,String afterEndTime) throws ParseException, SchedulerException {
        logger.info("新增值班");
        JSONArray ja = JSON.parseArray(obj);
        Date maxDate = null;
        Date maxAfterDate = null;
        Date minDate = null;
        Date minAfterDate = null;
        DutyListVo dutyListVo = new DutyListVo();
        for(int i = 0;i<ja.size();i++){
            JSONObject jsonObject = ja.getJSONObject(i);
            Date data = jsonObject.getDate("data");
            String state = jsonObject.getString("state");
            if(StringUtils.isNotBlank(state)){
                dutyListVo.setState(Integer.valueOf(jsonObject.getString("state")));
                if (maxAfterDate == null){
                    maxAfterDate = data;
                }else{
                    if(maxAfterDate.before(data)){
                        maxAfterDate = data;
                    };
                }
                if (minAfterDate == null){
                    minAfterDate = data;
                }else{
                    if(minAfterDate.after(data)){
                        minAfterDate = data;
                    }
                }
            }else{
                if (maxDate == null){
                    maxDate = data;
                }else{
                    if(maxDate.before(data)){
                        maxDate = data;
                    };
                }
                if (minDate == null){
                    minDate = data;
                }else{
                    if(minDate.after(data)){
                        minDate = data;
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(afterStartTime)&&StringUtils.isNotBlank(afterEndTime)){
            dutyListVo.setAfterStartTime(minAfterDate);
            dutyListVo.setAfterEndTime(maxAfterDate);
        }
        dutyListVo.setStartTime(minDate);
        dutyListVo.setEndTime(maxDate);
        dutyListVo.setDomainId(domainId);
        iDutyListApi.insertDutyList(dutyListVo);
        Iterator<Object> it = ja.iterator();
        while (it.hasNext()) {
            JSONObject ob = (JSONObject) it.next();
            DutyMain main = new DutyMain();
            main.setDutyDate(ob.getDate("data"));
            main.setDutyStates(0);
            main.setListId(dutyListVo.getId());
            JSONArray dutyVo = ob.getJSONArray("classNames");
            long dutyId = this.iDutyManageApi.insertToMain(main);
            List<DutyMainUserVo> userList = new ArrayList<DutyMainUserVo>();
            //记录值班日期下存在的班次信息
//            List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAll();
            List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAllByDomainId(String.valueOf(domainId));
            for(DutyTypeVo typeVo:dutyTypeVoList){
                typeVo.setDutyId(dutyId);
            }
            iDutyManageApi.insertDutyTypeRecord(dutyTypeVoList);
            for (int i = 0; i < dutyVo.size(); i++) {
                JSONObject classNameList = dutyVo.getJSONObject(i);
                DutyMainUserVo dutyMainUserVo = new DutyMainUserVo();
                if (classNameList.getLong("userId") != null){
                    dutyMainUserVo.setTypeUserId(classNameList.getLong("userId"));
                }
                if (classNameList.getString("name") != null){
                    dutyMainUserVo.setTypeUserName(classNameList.getString("name"));
                }
                dutyMainUserVo.setDutyId(dutyId);
                dutyMainUserVo.setDutyTypeId(classNameList.getLong("typeId"));
                dutyMainUserVo.setStartTime(classNameList.getInteger("startTime"));
                dutyMainUserVo.setEndTime(classNameList.getInteger("endTime"));
                dutyMainUserVo.setTypeName(classNameList.getString("typeName"));
                if (classNameList.get("contactsId")!=null){
                    dutyMainUserVo.setContactsId(classNameList.getLong("contactsId"));
                }
                dutyMainUserVo.setDutyDate(ob.getDate("data"));
                if (classNameList.getString("dutyName") == null || "" == classNameList.getString("dutyName")){
                    dutyMainUserVo.setDutyName("");
                }else {
                    dutyMainUserVo.setDutyName(classNameList.getString("dutyName"));
                }
                userList.add(dutyMainUserVo);
            }
            if (dutyVo.size() > 0) {
                iDutyManageApi.insertToUser(userList);
            }
        }

        //删除今日定时任务 同时生成新的定时任务
        InitDutyJob.init();


        //审计日志统计  开始
        AuditLog al=new AuditLog();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId("0");
        al.setFunctionModleId("SVR");
        al.setOperationType("新建");
        al.setOperationContent("值班管理:新建值班");
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(obj);
    }


    /*
     * jwt-改 向main的两个表中插入数据
     */
    @RequestMapping({"newInsertMains"})
    @ResponseBody
    public JSONObject newInsertMains(String obj,Long domainId,String afterStartTime,String afterEndTime) throws ParseException, SchedulerException {
        logger.info("新增值班");
        JSONArray ja = JSON.parseArray(obj);
        Date maxDate = null;
        Date maxAfterDate = null;
        Date minDate = null;
        Date minAfterDate = null;
        DutyListVo dutyListVo = new DutyListVo();
        for(int i = 0;i<ja.size();i++){
            JSONObject jsonObject = ja.getJSONObject(i);
            Date data = jsonObject.getDate("data");
            String state = jsonObject.getString("state");
            if(StringUtils.isNotBlank(state)){
                dutyListVo.setState(Integer.valueOf(jsonObject.getString("state")));
                if (maxAfterDate == null){
                    maxAfterDate = data;
                }else{
                    if(maxAfterDate.before(data)){
                        maxAfterDate = data;
                    };
                }
                if (minAfterDate == null){
                    minAfterDate = data;
                }else{
                    if(minAfterDate.after(data)){
                        minAfterDate = data;
                    }
                }
            }else{
                if (maxDate == null){
                    maxDate = data;
                }else{
                    if(maxDate.before(data)){
                        maxDate = data;
                    };
                }
                if (minDate == null){
                    minDate = data;
                }else{
                    if(minDate.after(data)){
                        minDate = data;
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(afterStartTime)&&StringUtils.isNotBlank(afterEndTime)){
            dutyListVo.setAfterStartTime(minAfterDate);
            dutyListVo.setAfterEndTime(maxAfterDate);
        }
        dutyListVo.setStartTime(minDate);
        dutyListVo.setEndTime(maxDate);
        dutyListVo.setDomainId(domainId);
        iDutyListApi.insertDutyList(dutyListVo);
        Iterator<Object> it = ja.iterator();
        while (it.hasNext()) {
            JSONObject ob = (JSONObject) it.next();
            DutyMain main = new DutyMain();
            main.setDutyDate(ob.getDate("data"));
            main.setDutyStates(0);
            main.setListId(dutyListVo.getId());
            JSONArray dutyVo = ob.getJSONArray("classNames");
            long dutyId = this.iDutyManageApi.insertToMain(main);
            List<DutyMainUserVo> userList = new ArrayList<DutyMainUserVo>();
            Set typeIds = new HashSet();
            //记录值班日期下存在的班次信息
//            List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAll();
            for (int i = 0; i < dutyVo.size(); i++) {
                JSONObject classNameList = dutyVo.getJSONObject(i);
                typeIds.add(classNameList.getLong("typeId"));
                DutyMainUserVo dutyMainUserVo = new DutyMainUserVo();
                if (classNameList.getLong("userId") != null){
                    dutyMainUserVo.setTypeUserId(classNameList.getLong("userId"));
                }
                if (classNameList.getString("name") != null){
                    dutyMainUserVo.setTypeUserName(classNameList.getString("name"));
                }
                dutyMainUserVo.setDutyId(dutyId);
                dutyMainUserVo.setDutyTypeId(classNameList.getLong("typeId"));
                dutyMainUserVo.setStartTime(classNameList.getInteger("startTime"));
                dutyMainUserVo.setEndTime(classNameList.getInteger("endTime"));
                dutyMainUserVo.setTypeName(classNameList.getString("typeName"));
                if (classNameList.get("contactsId")!=null){
                    dutyMainUserVo.setContactsId(classNameList.getLong("contactsId"));
                }
                dutyMainUserVo.setDutyDate(ob.getDate("data"));
                if (classNameList.getString("dutyName") == null || "" == classNameList.getString("dutyName")){
                    dutyMainUserVo.setDutyName("");
                }else {
                    dutyMainUserVo.setDutyName(classNameList.getString("dutyName"));
                }
                userList.add(dutyMainUserVo);
            }
            List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAll();
            List<DutyTypeVo> dutyTypeVos = new ArrayList<>();
            for(DutyTypeVo typeVo:dutyTypeVoList){
                if(typeIds.contains(typeVo.getTypeId())){
                    typeVo.setDutyId(dutyId);
                    dutyTypeVos.add(typeVo);
                }
            }
            iDutyManageApi.insertDutyTypeRecord(dutyTypeVos);

            if (dutyVo.size() > 0) {
                iDutyManageApi.insertToUser(userList);
            }
        }

        //删除今日定时任务 同时生成新的定时任务
        InitDutyJob.init();


        //审计日志统计  开始
        AuditLog al=new AuditLog();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId("0");
        al.setFunctionModleId("SVR");
        al.setOperationType("新建");
        al.setOperationContent("值班管理:新建值班");
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(obj);
    }


    @RequestMapping({"getDutyListPage"})
    @ResponseBody
    public JSONObject getDutyListPage(int pageNumber,int pageSize,String searchText,Long domainId){
        Page<DutyListVo,DutyListVo> page = new Page();
        page.setStartRow((pageNumber - 1)*pageSize);
        page.setRowCount(pageSize);
        DutyListVo condition = new DutyListVo();
        condition.setDomainId(domainId);
        page.setCondition(condition);
        iDutyListApi.findAllDutyPage(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        jsonObject.put("page",page.getStartRow());
        return jsonObject;
    }


    /*按周延续排班*/
    @RequestMapping({"addWeek"})
    @ResponseBody
    public JSONObject addWeek(String obj,String domainId) {
        JSONArray ja = JSON.parseArray(obj);
        Iterator<Object> it = ja.iterator();

        while (it.hasNext()) {
            JSONObject ob = (JSONObject) it.next();
            DutyMain main = new DutyMain();
            main.setDutyDate(ob.getDate("data"));
            main.setDutyStates(Integer.valueOf(ob.getString("dutyState")));
            long dutyId = this.iDutyManageApi.insertToMain(main);
//            List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAll();
            List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAllByDomainId(domainId);
            for(DutyTypeVo typeVo:dutyTypeVoList){
                typeVo.setDutyId(dutyId);
            }
            iDutyManageApi.insertDutyTypeRecord(dutyTypeVoList);
            JSONArray dutyVo = ob.getJSONArray("classNames");
            if(CollectionUtils.isNotEmpty(dutyVo)){
                List<DutyWeekUserVo> userList = new ArrayList<DutyWeekUserVo>();
                for (int i = 0; i < dutyVo.size(); i++) {
                    DutyWeekUserVo dutyWeekUserVo = new DutyWeekUserVo();
                    JSONObject classNameList = dutyVo.getJSONObject(i);
                    if (classNameList.getLong("userId") != null){
                        dutyWeekUserVo.setTypeUserId(classNameList.getLong("userId"));
                    }
                    if (classNameList.getString("name") != null){
                        dutyWeekUserVo.setTypeUserName(classNameList.getString("name"));
                    }
                    dutyWeekUserVo.setDutyTypeId(classNameList.getLong("typeId"));
                    dutyWeekUserVo.setStartTime(classNameList.getInteger("startTime"));
                    dutyWeekUserVo.setOverTime(classNameList.getInteger("endTime"));
                    dutyWeekUserVo.setTypeName(classNameList.getString("typeName"));
                    dutyWeekUserVo.setDutyId(dutyId);
                    dutyWeekUserVo.setDutyDate(ob.getDate("data"));
                    dutyWeekUserVo.setBigenDate(classNameList.getDate("bigenDate"));
                    dutyWeekUserVo.setEndDate(classNameList.getDate("endDate"));
                    dutyWeekUserVo.setArrangeState(1);
                    if (classNameList.getString("dutyName") == null || "" == classNameList.getString("dutyName")){
                        dutyWeekUserVo.setDutyName("");
                    }else {
                        dutyWeekUserVo.setDutyName(classNameList.getString("dutyName"));
                    }
                    userList.add(dutyWeekUserVo);
                }
                iDutyManageApi.addWeek(userList);
                }
            }
        return toSuccess(obj);
    }

    /**
     * 每周一凌晨零点执行一次；
     */
    //  0 00 14 * * ?       0 0 0 ? * MON
    @Scheduled(cron = "0 0 0 ? * MON")
    public void everyWeekPlanToTask(){
        List<DutyWeekUserVo> dutyWeekUserVos = iDutyManageApi.findWeek();
        List<Long> dutyIds = iDutyManageApi.findMinId();
        long dutyId = 0L;
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        Date tomorrow = new Date();
        if (CollectionUtils.isNotEmpty(dutyWeekUserVos)){
            if (CollectionUtils.isNotEmpty(dutyIds)){
                for (int j = 0;j<dutyIds.size();j++){
                    DutyMain dutyMain = new DutyMain();
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    tomorrow = c.getTime();
                    dutyMain.setDutyDate(tomorrow);
                    dutyMain.setDutyStates(0);
                    dutyId  = this.iDutyManageApi.insertToMain(dutyMain);
                    List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAll();
                    for(DutyTypeVo typeVo:dutyTypeVoList){
                        typeVo.setDutyId(dutyId);
                    }
                    iDutyManageApi.insertDutyTypeRecord(dutyTypeVoList);
                    List<DutyWeekUserVo> dutyWeekUserVoList = iDutyManageApi.findWeekDuty(dutyIds.get(j));
                    List<DutyMainUserVo> userList = new ArrayList<DutyMainUserVo>();
                    for (int i = 0; i <dutyWeekUserVoList.size() ; i++) {
                        DutyMainUserVo dutyMainUserVo = new DutyMainUserVo();

                        if(dutyWeekUserVos.get(i).getDutyId() == dutyIds.get(0)){
                            tomorrow = c.getTime();
                        }
                        if(dutyWeekUserVos.get(i).getDutyId() == dutyIds.get(1)){
                            c.add(Calendar.DAY_OF_MONTH, 1);
                            tomorrow = c.getTime();
                            c = Calendar.getInstance();
                        }
                        if(dutyWeekUserVos.get(i).getDutyId() == dutyIds.get(2)){
                            c.add(Calendar.DAY_OF_MONTH, 2);
                            tomorrow = c.getTime();
                            c = Calendar.getInstance();
                        }
                        if(dutyWeekUserVos.get(i).getDutyId() == dutyIds.get(3)){
                            c.add(Calendar.DAY_OF_MONTH, 3);
                            tomorrow = c.getTime();
                            c = Calendar.getInstance();
                        }
                        if(dutyWeekUserVos.get(i).getDutyId() == dutyIds.get(4)){
                            c.add(Calendar.DAY_OF_MONTH, 4);
                            tomorrow = c.getTime();
                            c = Calendar.getInstance();
                        }
                        if(dutyWeekUserVos.get(i).getDutyId() == dutyIds.get(5)){
                            c.add(Calendar.DAY_OF_MONTH, 5);
                            tomorrow = c.getTime();
                            c = Calendar.getInstance();
                        }
                        if(dutyWeekUserVos.get(i).getDutyId() == dutyIds.get(6)){
                            c.add(Calendar.DAY_OF_MONTH, 6);
                            tomorrow = c.getTime();
                            c = Calendar.getInstance();
                        }
                        dutyMainUserVo.setTypeUserId(dutyWeekUserVoList.get(i).getTypeUserId());
                        dutyMainUserVo.setTypeUserName(dutyWeekUserVoList.get(i).getTypeUserName());
                        dutyMainUserVo.setDutyTypeId(dutyWeekUserVoList.get(i).getDutyTypeId());
                        dutyMainUserVo.setStartTime(dutyWeekUserVoList.get(i).getStartTime());
                        dutyMainUserVo.setEndTime(dutyWeekUserVoList.get(i).getOverTime());
                        dutyMainUserVo.setTypeName(dutyWeekUserVoList.get(i).getTypeName());
                        dutyMainUserVo.setDutyDate(tomorrow);
                        dutyMainUserVo.setDutyId(dutyId);
                        dutyMainUserVo.setDutyName(dutyWeekUserVoList.get(i).getDutyName());
                        userList.add(dutyMainUserVo);
                    }
                    iDutyManageApi.insertToUser(userList);
                }
            }
        }
    }


    /**
     * 查询排班信息
     * @param listId
     * @return
     */
    @RequestMapping({"findDutyMainUserList"})
    @ResponseBody
    public JSONObject findDutyMainUserList(Long listId, String isNew) {
        DutyListVo dutyListVo = iDutyListApi.findAllDutyListById(listId);
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        List<DutyMain> dutyMains1 = iDutyManageApi.findDutyMainByListId(sdf.format(dutyListVo.getStartTime()),sdf.format(dutyListVo.getEndTime()),listId);
        if (CollectionUtils.isNotEmpty(dutyMains1)) {
            for (DutyMain lists : dutyMains1) {
                Long dutyId = lists.getDutyId();
                List<DutyMainUserVo> dutyWeekUserVoss = iDutyManageApi.findDutyMainUserByTypeId(dutyId);
                List<DutyTypeVo> dutyTypeVos = iDutyManageApi.findDutyTypeById(lists.getDutyId());
                if(isNew != null && Boolean.valueOf(isNew))
                    dutyTypeVos.stream().forEach(e -> {e.setStartTime(e.getTypeStartTime());e.setEndTime(e.getTypeEndTime());});
                lists.setDutyMainUser(dutyWeekUserVoss);
                lists.setDutyTypeVoList(dutyTypeVos);
            }
        }
        List<DutyMain> newDutyMins = dutyMains1.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DutyMain::getDutyId))), ArrayList::new));
        return toSuccess(newDutyMins);
    }

    /*查询按周顺延表*/
    @RequestMapping({"findWeek"})
    @ResponseBody
    public JSONObject findWeek() {
        List<DutyMain> dutyMains1 = new ArrayList<>();
        List<DutyWeekUserVo> dutyWeekUserVos = iDutyManageApi.findWeek();
            if (CollectionUtils.isNotEmpty(dutyWeekUserVos)) {
                for (DutyWeekUserVo lists : dutyWeekUserVos) {
                    DutyMain dutyMains = new DutyMain();
                    List<DutyWeekUserVo> dutyWeekUserVoss = iDutyManageApi.findWeekDuty(lists.getDutyId());
                    List<DutyTypeVo> dutyTypeVos = iDutyManageApi.findDutyTypeById(lists.getDutyId());
                    dutyMains.setDutyWeekUser(dutyWeekUserVoss);
                    dutyMains.setDutyTypeVoList(dutyTypeVos);
                    dutyMains.setDutyDate(lists.getDutyDate());
                    dutyMains.setDutyId(lists.getDutyId());
                    dutyMains1.add(dutyMains);
                }
            }
            List<DutyMain> newDutyMins = dutyMains1.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DutyMain::getDutyId))), ArrayList::new));
        return toSuccess(newDutyMins);
    }

    @RequestMapping({"delWeekUsers"})
    @ResponseBody
    public JSONObject delWeekUsers(){
        iDutyManageApi.delWeekUsers();
        return toSuccess("ok");
    }

    /*
    根据域查询值班列表
     */
    @RequestMapping({"findDutyDomainId"})
    @ResponseBody
    public JSONObject findDutyDomainId(String beginTime, String endTime,Long domainId) {
        DutyTypeVo vo = new DutyTypeVo();
        vo.setStartTime(beginTime+" 00:00:00");
        vo.setEndTime(endTime+" 23:59:59");
        vo.setDomainId(domainId);
//        List<DutyMain> dutyMains=this.iDutyManageApi.selectDutyList(vo);
//        List<DutyMain> dutyMains=this.iDutyManageApi.selectAllDutyList(vo);
        List<DutyMain> dutyMains=this.iDutyManageApi.findAllDutyList(vo);
        for (DutyMain dutyMain:dutyMains){
            //dutyTypeVoList  查询当前日期存在的值班类别
            //List<DutyTypeVo> dutyTypeVos= iDutyManageApi.selectDutyTypeById(dutyMain.getDutyId());
            List<DutyTypeVo> dutyTypeVos= iDutyManageApi.selectDutyTypeById(dutyMain.getDutyId(),domainId);
            dutyMain.setDutyTypeVoList(dutyTypeVos);
            //DutyMainUserVo
            List<DutyMainUserVo> dutyMainUserVos=iDutyManageApi.selectDutyManById(dutyMain.getDutyId(),domainId);
            dutyMain.setDutyMainUser(dutyMainUserVos);
        }
        return toSuccess(dutyMains);
    }

    /*
    值班列表
     */
    @RequestMapping({"dutyList"})
    @ResponseBody
    public JSONObject dutyList(String beginTime,String endTime) {
        DutyTypeVo vo = new DutyTypeVo();
        vo.setStartTime(beginTime+" 00:00:00");
        vo.setEndTime(endTime+" 23:59:59");
        List<DutyMain> dutyMains=this.iDutyManageApi.selectDutyList(vo);
        for (DutyMain dutyMain:dutyMains){
            //dutyTypeVoList  查询当前日期存在的值班类别
            List<DutyTypeVo> dutyTypeVos=iDutyManageApi.selectDutyTypeById(dutyMain.getDutyId());
            dutyMain.setDutyTypeVoList(dutyTypeVos);
            //DutyMainUserVo
            List<DutyMainUserVo> dutyMainUserVos=iDutyManageApi.selectDutyManById(dutyMain.getDutyId());
            dutyMain.setDutyMainUser(dutyMainUserVos);
        }

        return toSuccess(dutyMains);
    }

    /* 通过毫秒数计算两个时间间隔*/
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
    public static Date getDateAfter(Date d,int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }
    /*
    复制值班
     */
    @RequestMapping({"copyDuty"})
    @ResponseBody
    public JSONObject copyDuty(String copyBeginTime, String copyEndTime, String newBeginTime) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime = df.parse(copyBeginTime);//开始复制时间
        Date endTime = df.parse(copyEndTime);  //结束复制时间
        Date newBegin = df.parse(newBeginTime);  //复制开始时间
        int differentDay=differentDaysByMillisecond(beginTime,endTime); //计算开始复制时间到结束复制时间相差几天
        for (int i=0;i<differentDay;i++){
          Date serTime=getDateAfter(beginTime,i);
          int sss=0;
        }
        Date minTime = df.parse(this.iDutyManageApi.getMinDate());
        Date maxTime = this.iDutyManageApi.getMaxDate();
        if (beginTime.before(minTime)) {  //如果复制开始日期小于表中的最小值班日期
            return toSuccess(2);
        } else if (endTime.after(maxTime)) {
            return toSuccess(3);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(newBegin);
            DutyTypeVo vo = new DutyTypeVo();
            vo.setStartTime(copyBeginTime);
            vo.setEndTime(copyEndTime);
            List<DutyMain> list = this.iDutyManageApi.selectDutyList(vo);
            List<DutyMainUserVo> listVo = this.iDutyManageApi.getDutyInfoByDutyDate(vo);  //得到这段时间值班信息
            for (int i = 0; i < list.size(); i++) {
                Calendar b = Calendar.getInstance();
                DutyMain main = list.get(i);
                b.setTime(newBegin);
                b.add(Calendar.DATE,i);
                newBegin = b.getTime();
                DutyMain dutyLine = new DutyMain();
                dutyLine.setDutyDate(newBegin);
                //根据时间和值班班次类型获取信息
                List<DutyMain> dutyMainList = iDutyManageApi.getDutyMainByDate(df.format(newBegin));  //获得当前时间值班信息
                if(dutyMainList == null || dutyMainList.size() == 0){
                    long dutyId = this.iDutyManageApi.insertToMainLine(dutyLine);   //向itsm_duty_main中插入一行数据得到dutyId
                    List<DutyMainUserVo> dutyMainUser = main.getDutyMainUser();
                    for (int j = 0; j < dutyMainUser.size(); j++) {
                        dutyMainUser.get(j).setDutyId(dutyId);
                    }
                    this.iDutyManageApi.insertToMainList(dutyMainUser);
                }
            }

            return toSuccess("success");
        }

    }

    /*
    获得值班列表中最大日期
     */
    @RequestMapping({"getMaxDate"})
    @ResponseBody
    public JSONObject getMaxDate() {
        return toSuccess(this.iDutyManageApi.getMaxDate());
    }


    /*
 获得值班列表中最大日期根据domainId
  */
    @RequestMapping({"getMaxDateByDomainId"})
    @ResponseBody
    public JSONObject getMaxDateByDomainId(Long domainId) {
        return toSuccess(this.iDutyManageApi.getMaxDate(domainId));
    }

    /*
    更新值班状态
     */
    @RequestMapping({"updateDutyState"})
    @ResponseBody
    public JSONObject updateDutyState(String state, String id) {
        DutyMainUserVo vo = new DutyMainUserVo();
        vo.setId(Long.valueOf(id));
        vo.setDutyState(Long.valueOf(state));
        return toSuccess(this.iDutyManageApi.updateDutyState(vo));
    }
    /*
    添加值班人员是根据typeId查询人员
     */
    @RequestMapping({"getUsersByTypeId"})
    @ResponseBody
    public JSONObject getUsersByTypeId(String typeId,String searchText) {
        logger.info("DutyViewAction--getUsersByTypeId--" + typeId);
        return toSuccess(this.iDutyManageApi.getUsersByTypeId(typeId,searchText));
    }

    @RequestMapping({"findTypeUser"})
    @ResponseBody
    public JSONObject findTypeUser(Long typeId,String searchText) {
        return toSuccess(iDutyManageApi.findTypeUser(typeId,searchText));
    }



    @RequestMapping({"findUserByDomainId"})
    @ResponseBody
    public JSONObject findUserByDomainId(String typeId,String searchText,Long domainId) {
        logger.info("DutyViewAction--getUsersByTypeId--" + typeId);
            return toSuccess(this.iDutyManageApi.findUserByDomainId(typeId,searchText,domainId));

    }


    /*
    向人员数据表中添加信息
     */
    @RequestMapping({"setInUserInfo"})
    @ResponseBody
    public JSONObject setInUserInfo(String userId, String userName, String typeId) {
        logger.info("setInUserInfo--userID" + userId + "setInUserInfo--userName" + userName);
        DutyMainUserVo mainUserVo = new DutyMainUserVo();
        mainUserVo.setDutyId(Long.valueOf(userId));
        mainUserVo.setTypeUserName(userName);
        mainUserVo.setTypeUserId(Long.valueOf(typeId));
        return toSuccess(this.iDutyManageApi.setInUserInfo(mainUserVo));
    }

    /*
    删除新建时添加的人员
     */
    @RequestMapping({"setDelUser"})
    @ResponseBody
    public JSONObject setDelUser(String userId) {
        return toSuccess(this.iDutyManageApi.setDelUser(userId));
    }



    /*按周编辑保存*/
    @RequestMapping({"upDateDutyUserInfo"})
    @ResponseBody
    public JSONObject upDateDutyUserInfo(@RequestBody JSONObject obj){
        JSONArray delManss = obj.getJSONArray("delMans");
        JSONArray addManss = obj.getJSONArray("addMans");
        List<Long> delIds = new ArrayList<>();
        List<DutyMainUserVo> addMan = new ArrayList<>();
        if (delManss.size()!=0) {
            for (int i = 0; i < delManss.size(); i++) {
                delIds.add(delManss.getLong(i));
            }
            this.iDutyManageApi.delMainUserByIds(delIds);
        }
        if (addManss.size()!=0) {
            for (int j = 0; j < addManss.size(); j++) {
                DutyMainUserVo map = new DutyMainUserVo();
                map.setDutyId(addManss.getJSONObject(j).getLong("dutyId"));
                map.setTypeUserId(Long.valueOf(addManss.getJSONObject(j).getString("typeUserId")));
                map.setDutyTypeId(Long.valueOf(addManss.getJSONObject(j).getString("dutyTypeId")));
                map.setTypeUserName(addManss.getJSONObject(j).getString("typeUserName"));
                this.iDutyManageApi.updateDutyMainUser(map);
            }
        }
        return toSuccess(0);

    }

    /**
     *
     * @param object
     * @return
     */
    @RequestMapping({"upDateDutyUserContactsInfo"})
    @ResponseBody
    public JSONObject upDateDutyUserContactsInfo(@RequestBody JSONObject object){
        JSONArray jsonArray = object.getJSONArray("userContacts");
        for(int i = 0;i<jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            DutyMainUserVo dutyMainUserVo = new DutyMainUserVo();
            dutyMainUserVo.setDutyTypeId(jsonObject.getLong("dutyTypeId"));
            dutyMainUserVo.setDutyId(jsonObject.getLong("dutyId"));
            dutyMainUserVo.setContactsId(jsonObject.getLong("contactsId"));
            iDutyManageApi.upDateDutyUserContactsInfo(dutyMainUserVo);
        }
        return toSuccess(0);

    }

    /*按周编辑保存*/
    @RequestMapping({"upDateWeekDutyInfo"})
    @ResponseBody
    public JSONObject upDateWeekDutyInfo(@RequestBody JSONObject obj){
        JSONArray delManss = obj.getJSONArray("delMans");
        JSONArray addManss = obj.getJSONArray("addMans");
        List<Long> delIds = new ArrayList<>();
        List<DutyWeekUserVo> addMan = new ArrayList<>();
        if (delManss.size()!=0) {
            for (int i = 0; i < delManss.size(); i++) {
                delIds.add(delManss.getLong(i));
            }
            this.iDutyManageApi.delWeekUser(delIds);
        }
        if (addManss.size()!=0) {
            for (int j = 0; j < addManss.size(); j++) {
                DutyWeekUserVo map = new DutyWeekUserVo();
                map.setDutyId(addManss.getJSONObject(j).getLong("dutyId"));
                map.setTypeUserName(addManss.getJSONObject(j).getString("typeUserName"));
                map.setDutyTypeId(Long.valueOf(addManss.getJSONObject(j).getString("dutyTypeId")));
                map.setTypeName(addManss.getJSONObject(j).getString("typeUserName"));
                map.setTypeUserId(Long.valueOf(addManss.getJSONObject(j).getString("typeUserId")));
                map.setStartTime(Integer.parseInt(addManss.getJSONObject(j).getString("startTime")));
                map.setOverTime(Integer.parseInt(addManss.getJSONObject(j).getString("endTime")));
                map.setDutyDate(new Date(Long.valueOf(addManss.getJSONObject(j).getString("dutyDate"))));
                map.setBigenDate(new Date(Long.valueOf(addManss.getJSONObject(j).getString("dutyDate"))));
                map.setEndDate(new Date(Long.valueOf(addManss.getJSONObject(j).getString("dutyDate"))));
                map.setDutyDate(new Date(Long.valueOf(addManss.getJSONObject(j).getString("dutyDate"))));
                map.setArrangeState(Integer.parseInt(addManss.getJSONObject(j).getString("arrangeState")));
                addMan.add(map);
            }
            this.iDutyManageApi.addMans(addMan);
        }
        return toSuccess(0);

    }



    /*
    编辑后保存upDateDutyInfo
     */
    @RequestMapping({"upDateDutyInfo"})
    @ResponseBody
    public JSONObject upDateDutyInfo(@RequestBody JSONObject obj) throws ParseException {
        JSONArray delMans = obj.getJSONArray("delMans");
        JSONArray addMans = obj.getJSONArray("addMans");
        List<Long> delIds = new ArrayList<>();
        List<DutyMainUserVo> addMan = new ArrayList<>();
        if (delMans.size()!=0) {
            for (int i = 0; i < delMans.size(); i++) {
                delIds.add(delMans.getLong(i));
            }
            this.iDutyManageApi.delLineUser(delIds);
        }
        if (addMans.size()!=0) {
            for (int j = 0; j < addMans.size(); j++) {
                DutyMainUserVo map = new DutyMainUserVo();
                map.setDutyId(addMans.getJSONObject(j).getLong("dutyId"));
                map.setTypeUserName(addMans.getJSONObject(j).getString("typeUserName"));
                map.setDutyTypeId(Long.valueOf(addMans.getJSONObject(j).getString("dutyTypeId")));
                String typeUserName = addMans.getJSONObject(j).getString("typeUserName");
                String dutyTypeId = addMans.getJSONObject(j).getString("dutyTypeId");
                map.setStartTime(Integer.parseInt(addMans.getJSONObject(j).getString("startTime")));
                map.setEndTime(Integer.parseInt(addMans.getJSONObject(j).getString("endTime")));
                map.setTypeName(addMans.getJSONObject(j).getString("typeName"));
                map.setDutyName(addMans.getJSONObject(j).getString("dutyName"));
                map.setDutyDate(new Date(Long.valueOf(addMans.getJSONObject(j).getString("dutyDate"))));
                if(StringUtils.isNotBlank(typeUserName)){
                    int userId = iDutyManageApi.getMainUserInfo(typeUserName, dutyTypeId);
                    map.setTypeUserId(Long.valueOf(userId));
                }

                addMan.add(map);
            }
            this.iDutyManageApi.addMan(addMan);
        }

        //审计日志统计  开始
        AuditLog al=new AuditLog();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId("0");
        al.setFunctionModleId("SVR");
        al.setOperationType("编辑");
        al.setOperationContent("值班管理:编辑值班");
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(0);
    }

    @RequestMapping({"getUserRoles"})
    @ResponseBody
    public JSONObject getUserRoles(HttpServletRequest request){
        ILoginUser loginUser = VesApp.getLoginUser(request);
        Map<String,Object> map = new HashMap<>();
        List<IRole> roles = loginUser.getRoles();
        if(roles != null && roles.size() >0){
            for (IRole role:roles){
                if(role.getId() == 424500){
                    map.put("isService",1);
                }
                if(role.getId() == 424501){
                    map.put("isFirstLine",1);
                }
                if(role.getId() == 424502){
                    map.put("isSecondLine",1);
                }
                if(role.getId() == 499500){
                    map.put("isServiceManager",1);
                }
                if(role.getId()==424503){
                    map.put("leader",1);
                }
            }
        }
        if(loginUser.getId() == 1){
            map.put("isAdmin",1);
        }
        return toSuccess(map);
    }

    /*查询最大时间和最小时间*/
    @RequestMapping({"findTime"})
    @ResponseBody
    public JSONObject findTime(){
        List<Date> dates = new ArrayList<>();
        Date desc = iDutyManageApi.findDesc();

        Date asc = iDutyManageApi.findAsc();
        dates.add(desc);
        dates.add(asc);
        return toSuccess(dates);
    };



    public DutyViewAction() {
        super();
        iDutyManageApi = WebItsmBeanUtil.getDutyManageService();
        iUserApi = WebItsmBeanUtil.getIUserApi();
        iRoleApi = WebItsmBeanUtil.getRoleApi();
    }


    /**
     * jwt 通过排班id获取所绑定班次的用户
     * @param id
     * @return
     */
    @RequestMapping({"/getUserByDutyId"})
    @ResponseBody
    public JSONObject getUserByDutyId(Long id){
        try {
            List<DutyMainUserVo> list = this.iDutyManageApi.selectDutyMainUserListByListID(id);
            if (list != null && list.size() > 0) {
                DutyMainUserVo vo = list.get(0);
                List<DutyTypeUserVo> typeUser = this.iDutyManageApi.findTypeUser(vo.getDutyTypeId(), "");
                return toSuccess(typeUser);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return toFailForGroupNameExsit("查询失败");
    }

    /**
     * jwt 通过类型id获取所绑定班次的用户
     * @param id
     * @return
     */
    @RequestMapping({"/getUserByTypeId"})
    @ResponseBody
    public JSONObject getUserByTypeId(Long id){
        try {
            List<DutyTypeUserVo> typeUser = this.iDutyManageApi.findTypeUser(id, "");
            return toSuccess(typeUser);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return toFailForGroupNameExsit("查询失败");
    }


}
