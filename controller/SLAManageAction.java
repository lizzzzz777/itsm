package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.servicecatalog.Vo.SLAManageVoQuery;
import com.vlinksoft.ves.servicecatalog.api.ISlaManageApi;
import com.vlinksoft.ves.servicecatalog.bo.*;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import com.vlinksoft.ves.workflow.um.eventmgr.api.IEventmgrApi;
import com.vlinksoft.ves.workflow.um.eventmgr.bo.ItsmEventApplyUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/11/6.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/slaManage"})
public class SLAManageAction extends BaseAction{
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(SLAManageAction.class);
    private ISlaManageApi iSlaManageApi;

    @Resource
    private AuditLogApi auditLogApi;

    @Resource
    private IEventmgrApi iEventmgrApi;

    /*
    添加SLA服务
     */
    @RequestMapping({"addSLAService"})
    @ResponseBody
    public JSONObject addSLAService(@RequestBody SLAManageVo bo) {
        logger.info("SLAManageAction--新增SLA");
        //1.查询SLA名称不能重复
        List<SLAManageVo> slaList = iSlaManageApi.getSlaByName(bo.getSlaName());
        if(slaList.size()>0){
            return toSuccess("nameErr");
        }
        //1.新增SLA信息
        long returnValue = iSlaManageApi.addSla(bo);
        //2.新增关联关系
        List<Long> catalogIds = bo.getCatalogIds();
        for(long cataId:catalogIds){
            SlaShipVo ship = new SlaShipVo();
            ship.setSlaId(returnValue);
            ship.setServiceId(cataId);
            iSlaManageApi.addShip(ship);
        }

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(returnValue+"");
        al.setFunctionModleId("OMC");
        al.setOperationType("新建");
        al.setOperationContent("新建SLA管理:"+bo.getSlaName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return toSuccess(returnValue);
    }
    /*
    /sla表单
     */
    @RequestMapping({"slaList"})
    @ResponseBody
    public JSONObject slaList(String searchText,int pageNumber,int pageSize) {
        logger.info("DirectoryManageAction--slaList");
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        SLAManageVoQuery bo=new SLAManageVoQuery();
        if(searchText == null){
            bo.setUnfinished("%%");
        }else {
            bo.setUnfinished("%"+searchText+"%");
        }
        page.setCondition(bo);
        iSlaManageApi.slaList(page);
        List<SLAManageVo> manageVos = page.getDatas();
        JSONObject jsonObject = new JSONObject();
        if(manageVos!=null && manageVos.size()>0){
            List<Long> slaIds = new ArrayList<>(manageVos.size());
            for (SLAManageVo vo:
                    manageVos) {
                slaIds.add(vo.getSlaId());
            }
            List<SlaServiceListVo> serviceList = iSlaManageApi.getSlaServiceList(slaIds);
            jsonObject.put("serviceList",serviceList);
        }
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }
    /*
    sla行内停用
     */
    @RequestMapping({"stopSla"})
    @ResponseBody
    public JSONObject stopSla(String id) {
        logger.info("DirectoryManageAction--stopSla"+Long.valueOf(id));
        //1.查询是否有未完成的工单使用此SLA，如果有则不能停用，如果没有，则可以停用
        List<Long> sheetIds = iSlaManageApi.getUnCompletedBySlaId(Long.valueOf(id));
        if(sheetIds != null && sheetIds.size()>0){
            return toSuccess("stopErr");
        }

        //审计日志统计  开始
        SLAManageVo sla= iSlaManageApi.getSlaById(Long.parseLong(id));
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(id);
        al.setFunctionModleId("OMC");
        al.setOperationType("停用");
        al.setOperationContent("停用SLA:"+sla.getSlaName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(this.iSlaManageApi.stopSla("'"+id+"'"));
    }


    /*
    /sla行内删除
     */
    @RequestMapping({"DelSlaById"})
    @ResponseBody
    public JSONObject DelSlaById(@RequestBody SLAManageVo vo){
        logger.info("DirectoryManageAction--DirectoryManageAction"+JSONObject.toJSON(vo.getSlaId()));
        //查询该sla下是否有工单信息，有则不允许删除
        List<Long> sheetIds = iSlaManageApi.getUnCompletedBySlaId(vo.getSlaId());
        if(sheetIds != null && sheetIds.size() > 0){
            return toSuccess("delErr");
        }
        //1.首先删除sla的关联关系
        int returnValue = this.iSlaManageApi.delShip(vo.getSlaId());
        //2.根据sla的ID查看是否还有关联关系存在，如果有则不可以删除，如果没有，则删除该SLA信息
        int count = iSlaManageApi.getSlaShip(vo.getSlaId());
        if(count <= 0){

            //审计日志统计  开始
            AuditLog al=new AuditLog();
            SLAManageVo sl= iSlaManageApi.getSlaById(vo.getSlaId());
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(vo.getSlaId()+"");
            al.setFunctionModleId("OMC");
            al.setOperationType("删除");
            al.setOperationContent("删除SLA:"+sl.getSlaName());
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束

            this.iSlaManageApi.delSla(vo.getSlaId()+"");
        }
        return  toSuccess(returnValue);
    }
    /*/
      sla行内启用
     */
    @RequestMapping({"startSla"})
    @ResponseBody
    public JSONObject startSla(String id){
        logger.info("DirectoryManageAction--startSla"+JSONObject.toJSON(id));

        //审计日志统计  开始
        SLAManageVo s= iSlaManageApi.getSlaById(Long.parseLong(id));
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(id);
        al.setFunctionModleId("OMC");
        al.setOperationType("启用");
        al.setOperationContent("启用SLA:"+s.getSlaName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return  toSuccess(this.iSlaManageApi.startSla("'"+id+"'"));
    }
    /*
    sla菜单停用
     */
    @RequestMapping({"stopSlaByIds"})
    @ResponseBody
    public JSONObject stopSlaByIds(@RequestBody List<Long> listId){
        logger.info("DirectoryManageAction--stopSlaByIds"+JSONObject.toJSON(listId));
        List<Long> sheetIds = iSlaManageApi.getUnCompletedBySlaIds(listId);
        if(sheetIds != null && sheetIds.size()>0){
            return toSuccess("stopErr");
        }
        StringBuffer ids=new StringBuffer();
        for(int i=0;i<listId.size();i++){
            ids.append("'").append(listId.get(i)).append("'").append(",");
        }
        String str=ids.substring(0,ids.length()-1);

        //审计日志统计  开始
        List<String>list=new ArrayList<>();
        for(int i=0;i<listId.size();i++){
            SLAManageVo s= iSlaManageApi.getSlaById(listId.get(i));
            list.add(s.getSlaName());
        }
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(listId.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("停用");
        al.setOperationContent("停用SLA:"+StringUtils.join(list.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(this.iSlaManageApi.stopSlas(str));
    }
    /*
    sla菜单启用
     */
    @RequestMapping({"startSlaByIds"})
    @ResponseBody
    public JSONObject startSlaByIds(@RequestBody List<String> listList){
        logger.info("DirectoryManageAction--startSlaByIds"+JSONObject.toJSON(listList));
        StringBuffer ids=new StringBuffer();
        for(int i=0;i<listList.size();i++){
            ids.append("'").append(listList.get(i)).append("'").append(",");
        };
        String str=ids.substring(0,ids.length()-1);

        //审计日志统计  开始
        List<String>list=new ArrayList<>();
        for(int i=0;i<listList.size();i++){
            SLAManageVo s= iSlaManageApi.getSlaById(Long.parseLong(listList.get(i)));
            list.add(s.getSlaName());
        }
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(listList.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("启用");
        al.setOperationContent("启用SLA:"+StringUtils.join(list.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(this.iSlaManageApi.startSlas(str));
    }
    /*
    sla菜单删除
     */
    @RequestMapping({"DelSlaByIdS"})
    @ResponseBody
    public JSONObject DelSlaByIdS(@RequestBody List<SLAManageVo> manageVoList){
        logger.info("DirectoryManageAction--DelSlaByIdS"+JSONObject.toJSON(manageVoList));
        List<Long> slaIds = new ArrayList<>();
        for(SLAManageVo slaManageVo:manageVoList){
            slaIds.add(slaManageVo.getSlaId());
        }
        List<Long> sheetIds = iSlaManageApi.getUnCompletedBySlaIds(slaIds);
        //首先查看该sla下是否还有工单关系，有则不允许删除
        if(sheetIds != null && sheetIds.size()>0){
            return toSuccess("delErr");
        }
        for(SLAManageVo slaManageVo:manageVoList){
            //1.首先删除sla的关联关系
            iSlaManageApi.delShip(slaManageVo.getSlaId());
            //2.根据sla的ID查看是否还有关联关系存在，如果有则不可以删除，如果没有，则删除该SLA信息
            if(iSlaManageApi.getSlaShip(slaManageVo.getSlaId()) <= 0){

                //审计日志统计  开始
                AuditLog al=new AuditLog();
                SLAManageVo sl= iSlaManageApi.getSlaById(slaManageVo.getSlaId());
//                AuditLAction auditLogAction= AuditLAction.getInstance();
//                AuditLogAction auditLogAction=new AuditLogAction();
                HttpServletRequest req=getHttpServletRequest();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date());
                al.setUserId(VesApp.getLoginUser(req).getId());
                al.setOperationObjId(slaManageVo.getSlaId()+"");
                al.setFunctionModleId("OMC");
                al.setOperationType("删除");
                al.setOperationContent("删除SLA:"+sl.getSlaName());
                try {
                    al.setOperationTime(sdf.parse(date));
                }catch (Exception e){
                    e.printStackTrace();
                }
                int i=auditLogApi.insert(al);
                //审计日志统计  结束

                iSlaManageApi.delSla(slaManageVo.getSlaId()+"");
            }
        }

        return toSuccess("success");
    }

    /*
    sla行内编辑时更新并启用
     */
    @RequestMapping({"updateAndStartSla"})
    @ResponseBody
    public JSONObject updateAndStartSla(@RequestBody SLAManageVo object){
        List<SLAManageVo> slaManageVoList = iSlaManageApi.getSlaByName(object.getSlaName());
        for(SLAManageVo vo:slaManageVoList){
            if(!object.getSlaId().equals(vo.getSlaId())){
                return toSuccess("nameErr");
            }
        }
        SLAManageVo slaManageVo = iSlaManageApi.getSlaById(object.getSlaId());

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(slaManageVo.getSlaId()+"");
        al.setFunctionModleId("OMC");
        al.setOperationType("编辑");
        al.setOperationContent("编辑SLA:"+slaManageVo.getSlaName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        //更新基本属性
        iSlaManageApi.updateAndStartSla(object);
        List<Long> ids = object.getCatalogIds();
        int returnValue = 0;
        iSlaManageApi.delShipByServiceId(ids,object.getSlaId());//先删除旧的关系
        if(ids != null && ids.size()>0){
            for(Long serviceId:ids){
                //1.根据serviceId和slaId从数据库中查询数据，如果没有查询到则新增该记录
                List<SlaShipVo> slaList = iSlaManageApi.getSlaByIdAndService(serviceId,object.getSlaId());
                if(slaList == null || slaList.size() ==0){
                    SlaShipVo shipVo = new SlaShipVo();
                    shipVo.setServiceId(serviceId);
                    shipVo.setSlaId(object.getSlaId());
                    iSlaManageApi.addShip(shipVo);
                }
            }
        }
        return toSuccess(returnValue);
    }

    /**
     * 根据SLA的ID获取服务分类列表
     */
    @RequestMapping({"serviceListBySID"})
    @ResponseBody
    public JSONObject serviceListBySID(Long slaId){
        List<DirectoryManageVo> serviceList = iSlaManageApi.getServiceListBySID(slaId);
        return toSuccess(serviceList);
    }

    /**
     * 级别设置保存
     * */
    @RequestMapping({"saveLevelManager"})
    @ResponseBody
    public JSONObject saveLevelManager(String number,String importantArray,String urgencyArray){
        JSONObject resultJSON = new JSONObject();
        if (importantArray != null && !importantArray.equals("") && urgencyArray != null && !urgencyArray.equals("") && number != null && !number.equals("")){
            String[] importantArrays = importantArray.split(",");
            String[] urgencyArrays = urgencyArray.split(",");
            int levelValue = Integer.parseInt(number) - 4;
            iSlaManageApi.updateLevelManagerNumber(String.valueOf(levelValue));
            List<LevelManagerVo> managerVos = new ArrayList<>(Integer.valueOf(number));
            for (int i = 0; i < Integer.valueOf(number); i++) {
                LevelManagerVo levelManagerVo = new LevelManagerVo();
                levelManagerVo.setLevelValue(String.valueOf(levelValue));
                levelManagerVo.setServiceLevelValue(String.valueOf(i+1));
                levelManagerVo.setImportantValue(importantArrays[i]);
                levelManagerVo.setUrgencyValue(urgencyArrays[i]);
                managerVos.add(levelManagerVo);
            }
            iSlaManageApi.updateServiceLevelTable(managerVos);
            resultJSON.put("code", 200);
            resultJSON.put("date", "修改成功！");
        }else {
            resultJSON.put("code", 700);
            resultJSON.put("date", "请求修改参数有问题！");
        }
        return resultJSON;
    }

    /**
     * 获取设置的事件级别值
     * */
    @RequestMapping({"getLevelManager"})
    @ResponseBody
    public JSONObject getLevelManager(){
        JSONObject resultJSON = new JSONObject();
        String number = iSlaManageApi.getLevelManagerNumber();
        resultJSON.put("code", "200");
        resultJSON.put("date", number);
        return resultJSON;
    }

    /**
     * 获取级别设置的数据表信息
     * */
    @RequestMapping({"getServiceLevelTableByLevelNum"})
    @ResponseBody
    public JSONObject getServiceLevelTableByLevelNum(String levelValue){
        JSONObject resultJSON = new JSONObject();
        List<LevelManagerVo> serviceLevelTable = iSlaManageApi.getServiceLevelTableByLevelNum(levelValue);
        if (serviceLevelTable != null && serviceLevelTable.size() > 0){
            int size = serviceLevelTable.size();
            StringBuffer importantArray = new StringBuffer();
            StringBuffer urgencyArray = new StringBuffer();
            for (int i = 0; i < size; i++) {
                LevelManagerVo levelManagerVo = serviceLevelTable.get(i);
                if (i==size-1){
                    importantArray.append(levelManagerVo.getImportantValue());
                    urgencyArray.append(levelManagerVo.getUrgencyValue());
                }else {
                    importantArray.append(levelManagerVo.getImportantValue()+",");
                    urgencyArray.append(levelManagerVo.getUrgencyValue()+",");
                }
            }
            resultJSON.put("code", 200);
            resultJSON.put("number", size);
            resultJSON.put("importantArray", importantArray.toString());
            resultJSON.put("urgencyArray", urgencyArray.toString());
        }else {
            resultJSON.put("code", 700);
            resultJSON.put("date", "数据库查询出错！");
        }
        return resultJSON;
    }

    public SLAManageAction(){
        super();
        iSlaManageApi= WebItsmBeanUtil.getSlaManage();
    }

    /**
     * 获取所有服务等级信息
     * */
    @RequestMapping({"getAllSLAServiceInfo"})
    @ResponseBody
    public JSONObject getAllSLAServiceInfo(){
        JSONObject resultJSON = new JSONObject();
        List<LevelManagerVo> slaServiceInfo = iSlaManageApi.getAllSLAServiceInfo();
        resultJSON.put("code", "200");
        resultJSON.put("total", slaServiceInfo.size());
        resultJSON.put("date", slaServiceInfo);
        return resultJSON;
    }

    /**
     * 设置服务级别响应时限、解决时限、描述 数据
     * */
    @RequestMapping({"setServiceLevelList"})
    @ResponseBody
    public JSONObject setServiceLevelList(@RequestBody List<LevelManagerVo> serviceLevelList){
        JSONObject resultJSON = new JSONObject();
        iSlaManageApi.setServiceLevelList(serviceLevelList);
        resultJSON.put("code", "200");
        resultJSON.put("date", "设置成功！");
        return resultJSON;
    }

    /**
     * 根据参数：角色信息，事件级别  获取服务级别信息，返回：服务级别、紧急程度、重要程度、响应时限、解决时限
     * */
    @RequestMapping({"getServiceInfoByRoleAndEvent"})
    @ResponseBody
    public JSONObject getServiceInfoByRoleAndEvent(String applyUserId,String eventLevel,String type){
        ItsmEventApplyUser applyUser = iEventmgrApi.findApplyUserById(Long.valueOf(applyUserId));
        JSONObject resultJSON = new JSONObject();
        int roleValue = 0;
        if (eventLevel == null){
            resultJSON.put("code", "500");
            resultJSON.put("date", "传入参数为空！");
            return resultJSON;
        }
        if (applyUser == null){
            roleValue = 2;
        }else{
            long applyRank = applyUser.getCustomRankLong();
            long userType = applyUser.getType();
            if (applyRank == 1){
                roleValue = 1;
            }else if (userType == 1 || userType == 4){
                roleValue = 4;
            }else {
                roleValue = 3;
            }
        }
        int eventValue = Integer.parseInt(eventLevel);
        int serviceLevelValue = eventValue + roleValue - 1;//得到服务等级
        if (type.equals("request")){
            String levelManagerNumber = iSlaManageApi.getLevelManagerNumber();
            eventValue = Integer.parseInt(levelManagerNumber);
            serviceLevelValue = eventValue + roleValue;
        }
        LevelManagerVo levelManagerVo = iSlaManageApi.getlevelManagerInfoByServiceLevel(serviceLevelValue);
        resultJSON.put("code", "200");
        resultJSON.put("data", levelManagerVo);
        return resultJSON;
    }

}
