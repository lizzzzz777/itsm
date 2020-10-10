package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.dutymanage.Vo.DutyTodayQuery;
import com.vlinksoft.ves.dutymanage.Vo.DutyTodayVo;
import com.vlinksoft.ves.dutymanage.Vo.DutyTypeVoQuery;
import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.api.IDutyUserApi;
import com.vlinksoft.ves.dutymanage.bo.*;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by admin on 2017/11/15.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/shiftManage"})
public class ShiftManageAction extends BaseAction {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(ShiftManageAction.class);
    private IDutyUserApi iDutyUserApi;
    private IDutyManageApi iDutyManageApi;

    @Resource
    private AuditLogApi auditLogApi;

    /*
    列表信息
     */
    @RequestMapping({"list"})
    @ResponseBody
    public JSONObject list(int pageNumber,int pageSize) {
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DutyTypeVoQuery dutyTypeVo = new DutyTypeVoQuery();
        dutyTypeVo.setName("%%");
        page.setCondition(dutyTypeVo);
        this.iDutyUserApi.list(page);
        for (DutyTypeVo duty : (List<DutyTypeVo>)page.getDatas()) {
            List<DutyUserVo> dutyUserList = iDutyUserApi.selectDutyUserByDutyId(duty.getTypeId());
            int flog=iDutyUserApi.checkFlogClass(duty.getTypeId()+"");
            if(flog>0){
                duty.setFlogClass(1);
            }else {
                duty.setFlogClass(0);
            }
            duty.setListMan(dutyUserList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    @RequestMapping({"findDutyTypeByDomainId"})
    @ResponseBody
    public JSONObject findDutyTypeByDomainId(int pageNumber,int pageSize,Long domainId){
        Page<DutyTypeVo,DutyTypeVoQuery> page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DutyTypeVoQuery dutyTypeVo = new DutyTypeVoQuery();
        dutyTypeVo.setDomainId(domainId);
        page.setCondition(dutyTypeVo);
        this.iDutyUserApi.findDutyTypeByDomainId(page);
        if(CollectionUtils.isNotEmpty(page.getDatas())){
            for (DutyTypeVo duty : (List<DutyTypeVo>)page.getDatas()) {
                List<DutyUserVo> dutyUserList = iDutyUserApi.selectDutyUserByDutyId(duty.getTypeId());
                int flog=iDutyUserApi.checkFlogClass(duty.getTypeId()+"");
                if(flog>0){
                    duty.setFlogClass(1);
                }else {
                    duty.setFlogClass(0);
                }
                duty.setListMan(dutyUserList);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    /**
     * 获取所有的班次信息
     */
    @RequestMapping({"listAll"})
    @ResponseBody
    public JSONObject listAll(){
        List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.listAll();
        return toSuccess(dutyTypeVoList);
    }


    /**
     * 获取所有的班次信息
     */
    @RequestMapping({"findAllDutyTypeByDomainId"})
    @ResponseBody
    public JSONObject findAllDutyTypeByDomainId(@Param(value="domainId")Long domainId){
        List<DutyTypeVo> dutyTypeVoList = iDutyManageApi.findAllDutyTypeByDomainId(domainId);
        return toSuccess(dutyTypeVoList);
    }

    /*
    删除值班班次
     */
    @RequestMapping({"delDutyClassByIds"})
    @ResponseBody
    public JSONObject delDutyClassByIds(@RequestBody List<String> listId) {
        logger.info("删除班次信息"+listId);
        int line = 0;
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < listId.size(); i++) {
            line = line + this.iDutyUserApi.checkFlogClass(listId.get(i));//检查是否排班
            ids.append("'").append(listId.get(i)).append("'").append(",");
        }
        if (line == 0) {
            String str = ids.substring(0, ids.length() - 1);

            //审计日志统计  开始
            List<Long>idss=new ArrayList<>();
            for(int i=0;i<listId.size();i++){
                idss.add(Long.parseLong(listId.get(i)));
            }
            List<DutyTypeVo> list= iDutyUserApi.selectByIds(idss);
            List<String>names=new ArrayList<>();
            for(int j=0;j<list.size();j++){
                names.add(list.get(j).getName());
            }
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(StringUtils.join(listId.toArray(), ","));
            al.setFunctionModleId("OMC");
            al.setOperationType("删除");
            al.setOperationContent("删除班次:"+StringUtils.join(names.toArray(), ","));
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束

            iDutyUserApi.delDutyClassUser(str);//删除班次人员信息
            return toSuccess(this.iDutyUserApi.delDutyClassType(str));//删除班次信息
        } else {
            return toSuccess("error");
        }

    }

    /*
   /添加值班班次
    */
    @RequestMapping({"addDutyClass"})
    @ResponseBody
    public JSONObject addDutyClass(@RequestBody JSONObject obj) {
        logger.info("增加值班班次"+obj);
        JSONObject classInfo = obj.getJSONObject("classInfo");
        DutyTypeVo dutyTypeVo = new DutyTypeVo();
        //检查班次名称是否重复
        //int nameLen = this.iDutyUserApi.checkClassName(classInfo.getString("name"));
        int nameLen = this.iDutyUserApi.checkClassName(classInfo.getString("name"),obj.getLong("domainId"));
        if (nameLen == 0) {
            return toSuccess("err");
        }
        int acrossDay=classInfo.getInteger("acrossDay");//是否跨天
        int startTime=classInfo.getIntValue("startTime");
        int endTime=classInfo.getIntValue("endTime");
        //检查值班时间是否合适
       /* if(this.whetherInline(startTime,endTime,acrossDay,0)>0){
            return  toSuccess("timeErr");
        }*/

//        if(this.checkDutyTimeIsRepeat(startTime,endTime,acrossDay,0,obj.getLong("domainId"))>0){
//            return  toSuccess("timeErr");
//        }

        dutyTypeVo.setName(classInfo.getString("name"));
        dutyTypeVo.setAcrossDay(classInfo.getInteger("acrossDay"));
        dutyTypeVo.setStartTime(startTime+"");
        dutyTypeVo.setEndTime(endTime+"");
        dutyTypeVo.setDomainId(obj.getLong("domainId"));
        dutyTypeVo.setPhone(classInfo.getString("phone"));
        long itd = this.iDutyUserApi.addDutyClass(dutyTypeVo);
        JSONArray mans = obj.getJSONArray("mans");
        for (int i = 0; i < mans.size(); i++) {
            String id = ((Map<String, String>) mans.get(i)).get("id");
            String name = ((Map<String, String>) mans.get(i)).get("name");
            DutyUserVo vo = new DutyUserVo();
            vo.setTypeId(itd);
            vo.setUserId(Long.valueOf(id));
            vo.setUserName(name);
            this.iDutyUserApi.insertShiftIdUser(vo);
        }

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(itd+"");
        al.setFunctionModleId("OMC");
        al.setOperationType("添加");
        al.setOperationContent("添加班次:"+dutyTypeVo.getName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(itd);
    }


    /*
    更新值班班次
     */
    @RequestMapping({"updateDutyClass"})
    @ResponseBody
    public JSONObject updateDutyClass(@RequestBody JSONObject obj) {
        logger.info("更新值班班次" + JSONObject.toJSON(obj));
        String tid = obj.getString("typeId");
        JSONObject classInfo = obj.getJSONObject("classInfo");
        int startTime = classInfo.getIntValue("startTime");
        int endTime = classInfo.getIntValue("endTime");
        int acrossDay = classInfo.getIntValue("acrossDay");
        JSONArray mans = obj.getJSONArray("mans");
        //班次人员是否有今天之后的排班
        if(checkTimeUser(tid,mans) > 0){
            return toSuccess("hasDuty");
        }
        //判断班次时间是否合规
//        if(this.whetherInline(startTime,endTime,acrossDay,Integer.valueOf(tid))>0){
//            return toSuccess("timeErr");
//        }
        DutyTypeVo dutyTypeVo = new DutyTypeVo();
        dutyTypeVo.setName(classInfo.getString("name"));
        dutyTypeVo.setAcrossDay(acrossDay);
        dutyTypeVo.setStartTime(startTime+"");
        dutyTypeVo.setEndTime(endTime+"");
        dutyTypeVo.setPhone(classInfo.getString("phone"));
        dutyTypeVo.setTypeId(Long.valueOf(tid));
        dutyTypeVo.setDomainId(obj.getLong("domainId"));
        //根据班次id删除班次下成员，再重新添加
        iDutyUserApi.delDutyClassUser(tid);

        for (int i = 0; i < mans.size(); i++) {
            String id = ((Map<String, String>) mans.get(i)).get("id");
            String name = ((Map<String, String>) mans.get(i)).get("name");
            DutyUserVo vo = new DutyUserVo();
            vo.setTypeId(Long.valueOf(tid));
            vo.setUserId(Long.valueOf(id));
            vo.setUserName(name);
            this.iDutyUserApi.insertShiftIdUser(vo);
        }

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(tid);
        al.setFunctionModleId("OMC");
        al.setOperationType("编辑");
        al.setOperationContent("编辑班次:"+dutyTypeVo.getName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(this.iDutyUserApi.updateDutyClassType(dutyTypeVo));
    }

    //判断排班班次是否合规
    public int whetherInline(int startTime,int endTime,int acrossDay,int dutyTypeId){
        //根据是否跨天获取系统中的班次时间段信息
        List<DutyTypeVo> unAcrossList = this.iDutyUserApi.dutyTypeList(0,dutyTypeId);  //不跨天的集合
        List<DutyTypeVo> acrossList = this.iDutyUserApi.dutyTypeList(1,dutyTypeId);    //跨天的集合
        int timeFlog=0;
        if(acrossDay == 0){//不跨天
            for (DutyTypeVo unAcross:unAcrossList){
                if(startTime>=Integer.valueOf(unAcross.getStartTime())&&startTime<Integer.valueOf(unAcross.getEndTime())){
                    timeFlog++;
                }
                if(endTime>Integer.valueOf(unAcross.getStartTime())&&endTime<=Integer.valueOf(unAcross.getEndTime())){
                    timeFlog++;
                }
            }
            for(DutyTypeVo across:acrossList){
                if(startTime<Integer.valueOf(across.getEndTime()) || startTime>=Integer.valueOf(across.getStartTime())){
                    timeFlog++;
                }
                if(endTime>Integer.valueOf(across.getStartTime())){
                    timeFlog++;
                }
            }
        }else if(acrossDay == 1){//跨天
            for(DutyTypeVo unAcross:unAcrossList){
                if(startTime < Integer.valueOf(unAcross.getEndTime())){
                    timeFlog++;
                }
                if(endTime > Integer.valueOf(unAcross.getStartTime())){
                    timeFlog++;
                }
            }
            if(acrossList.size()>0){//不能同时存在两个跨天的排班
                timeFlog++;
            }
        }
        return timeFlog;
    }

    //jwt 判断班次人员是否还有今天之后的排班
    public int checkTimeUser(String typeId,JSONArray mans){
        Long id = Long.valueOf(typeId);
        //获取old该班次的人员
        List<DutyUserVo> userVos = this.iDutyUserApi.selectDutyUserByDutyId(id);
        List<Long> userIds = userVos.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        //获取new该班次要保存的人员
        Set ids = mans.stream().map(e-> JSONObject.parseObject(e.toString()).getLong("id")).collect(Collectors.toSet());
        //获取已经要删除的人员
        List<Long> collect1 = userIds.stream().filter(e -> !ids.contains(e)).collect(Collectors.toList());
        if(collect1 != null && collect1.size() >0){
            Page page = new Page();
            DutyMainUserPageVo condition = new DutyMainUserPageVo();
            condition.setDutyTypeId(id);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            condition.setQueryStartTime(cal.getTime());
            page.setCondition(condition);
            //获取该班次的排班
            List<DutyMainUserVo> list = this.iDutyManageApi.queryDutyMainUser(page);
            //要删除的人员是否有排班
            List<DutyMainUserVo> collect = list.stream().filter(e -> collect1.contains(e.getTypeUserId())).collect(Collectors.toList());
            return collect == null ? 0 : collect.size();
        }
       return 0;
    }

    private int checkDutyTimeIsRepeat(int startTime,int endTime,int acrossDay,int dutyTypeId,Long domainId){
        List<DutyTypeVo> unAcrossList = this.iDutyUserApi.dutyTypeList(0,dutyTypeId,domainId);  //不跨天的集合
        List<DutyTypeVo> acrossList = this.iDutyUserApi.dutyTypeList(1,dutyTypeId,domainId);    //跨天的集合
        int timeFlog=0;
        if(acrossDay == 0){//不跨天
            for (DutyTypeVo unAcross:unAcrossList){
                if(startTime>=Integer.valueOf(unAcross.getStartTime())&&startTime<Integer.valueOf(unAcross.getEndTime())){
                    timeFlog++;
                }
                if(endTime>Integer.valueOf(unAcross.getStartTime())&&endTime<=Integer.valueOf(unAcross.getEndTime())){
                    timeFlog++;
                }
            }
            for(DutyTypeVo across:acrossList){
                if(startTime<Integer.valueOf(across.getEndTime()) || startTime>=Integer.valueOf(across.getStartTime())){
                    timeFlog++;
                }
                if(endTime>Integer.valueOf(across.getStartTime())){
                    timeFlog++;
                }
            }
        }else if(acrossDay == 1){//跨天
            for(DutyTypeVo unAcross:unAcrossList){
                if(startTime < Integer.valueOf(unAcross.getEndTime())){
                    timeFlog++;
                }
                if(endTime > Integer.valueOf(unAcross.getStartTime())){
                    timeFlog++;
                }
            }
            if(acrossList.size()>0){//不能同时存在两个跨天的排班
                timeFlog++;
            }
        }
        return timeFlog;
    }
    /*
    * 根据日期和值班人id查询值班班次*/
    @RequestMapping({"getDutyClassByTime"})
    @ResponseBody
    public JSONObject getDutyClassByTime(String shiftTime,HttpServletRequest request){
        ILoginUser loginUser = VesApp.getLoginUser(request);
        return  toSuccess(iDutyManageApi.getDutyClassByTime(shiftTime + "%",loginUser.getId()));
    }
    //根据日期查询有值班的班次
    @RequestMapping({"getShiftDutyClassByTime"})
    @ResponseBody
    public JSONObject getShiftDutyClassByTime(String time) throws ParseException {
        return  toSuccess(iDutyManageApi.getShiftDutyClassByTime(time));
    }
    //根据换班班次和日期查询值班人员
    @RequestMapping({"getShiftDutyClassByTimeAndDutyType"})
    @ResponseBody
    public JSONObject getShiftDutyClassByTimeAndDutyType(long shiftType,String time) throws ParseException {
        return  toSuccess(iDutyManageApi.getShiftDutyClassByTimeAndDutyType(shiftType,time));
    }
    /**
     * 当日值班信息
     */
    @RequestMapping({"getDutyToday"})
    @ResponseBody
    public JSONObject getDutyToday(){
        Page<DutyTodayVo,DutyTodayQuery> page = new Page<>();
        DutyTodayQuery condition = new DutyTodayQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        condition.setToday(sdf.format(new Date()));
        page.setCondition(condition);
        List<DutyTodayVo> getList=iDutyUserApi.selectDutyUserToday(page);
        for (DutyTodayVo vo:getList){
            String departmentName=iDutyUserApi.getDepartmentNameByUserId(vo.getUserId()); //根据用户id查询用户所属部门，没有所属部门的显示空
            vo.setDepartmentName(departmentName);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getDatas());
        jsonObject.put("page",page.getStartRow());
        jsonObject.put("total",page.getTotalRecord());
        return jsonObject;
    }


    /**
     * 全部值班日期
     * @return
     */
    @RequestMapping({"getDutyDateByDomainId"})
    @ResponseBody
    public JSONObject getDutyDateByDomainId(HttpServletRequest request,Long domainId,String startDate,String endDate){
        List<DutyMain> dutyMainList = iDutyManageApi.getAllDutyDateByDomainId(domainId,startDate,endDate);
        return toSuccess(dutyMainList);
    }



    /**
     * 我的值班日期
     * @return
     */
    @RequestMapping({"getDutyDate"})
    @ResponseBody
    public JSONObject getDutyDate(HttpServletRequest request,String startDate,String endDate){
        ILoginUser loginUser = VesApp.getLoginUser(request);
        List<DutyMain> dutyMainList = iDutyManageApi.getDutyDateByUserId(loginUser.getId(),startDate,endDate);
        return toSuccess(dutyMainList);
    }


    public ShiftManageAction() {
        super();
        iDutyUserApi = WebItsmBeanUtil.getShiftManageService();
        iDutyManageApi = WebItsmBeanUtil.getDutyManageService();
    }

}
