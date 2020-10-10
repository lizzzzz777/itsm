package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.cmdb.api.ICMDBItemApi;
import com.vlinksoft.ves.cmdb.object.CMDBItem;
import com.vlinksoft.ves.form.api.IVesFormRuntime;
import com.vlinksoft.ves.form.object.VesAssertElement;
import com.vlinksoft.ves.form.object.VesAssertItem;
import com.vlinksoft.ves.form.object.VesForm;
import com.vlinksoft.ves.form.object.VesItemConstant;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.platform.web.vo.IRole;
import com.vlinksoft.ves.servicecatalog.api.IDirectoryManageApi;
import com.vlinksoft.ves.servicecatalog.api.ISlaManageApi;
import com.vlinksoft.ves.servicecatalog.bo.DirectoryManageVo;
import com.vlinksoft.ves.servicecatalog.bo.SLAManageVo;
import com.vlinksoft.ves.system.um.user.api.IUserApi;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import com.vlinksoft.ves.vo.HttpRequestBody;
import com.vlinksoft.ves.workflow.um.applicant.api.IApplicantApi;
import com.vlinksoft.ves.workflow.um.enumUtil.AccordingToType;
import com.vlinksoft.ves.workflow.um.enumUtil.StateOfApproval;
import com.vlinksoft.ves.workflow.um.eventmgr.api.*;
import com.vlinksoft.ves.workflow.um.eventmgr.bo.*;
import com.vlinksoft.ves.workflow.um.eventmgr.vo.ItsmEventQueryVo;
import com.vlinksoft.ves.workflow.um.workflow.api.IProcessInstanceApi;
import com.vlinksoft.ves.workflow.um.workflow.bo.ProcessUserTask;
import com.vlinksoft.ves.workflow.um.workflow.bo.ProcessUserTaskPageQuery;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * Created by admin on 2017/11/6.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/sheetManage"})

public class MySheetAction extends BaseAction{
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(MySheetAction.class);
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private ProcessEngineConfiguration processEngineConfiguration;
    @Resource
    private ProcessEngine processEngine;
    @Resource
    private IVesFormRuntime vesFormRuntime;
    @Resource
    private IProcessInstanceApi processInstanceApi;

    private IEventmgrApi iEventmgrApi;
    private IApplicantApi iApplicantApi;
    private IPriorityApi iPriorityApi;
    private IUserApi iUserApi;
    private IDirectoryManageApi iDirectoryManageApi;
    private ISlaManageApi iSlaManageApi;
    private ICMDBItemApi icmdbItemApi;
    private ITraceProcessApi iTraceProcessApi;
    private IProcessUserApi iProcessUserApi;
    @Resource
    private IBaseProcessApi iBaseProcessApi;
    private IEventMessageApi iEventMessageApi;

    @Resource
    private IVesFormRuntime iVesFormRuntime;


    @RequestMapping({"getSatisfyStatistics"})
    @ResponseBody
    public JSONObject getSatisfyStatistics(@RequestParam("beginTime") long beginTime,@RequestParam("endTime")long endTime){
        return toSuccess(iEventmgrApi.getSatisfyStatistics(new Date(beginTime),new Date(endTime)));
    }

    //废弃----被TaskController中的getNumberStatistics方法取代
    @RequestMapping({"getNumberStatistics"})
    @ResponseBody
    public JSONObject getNumberStatistics(long beginTime,long endTime){
        return toSuccess(iEventmgrApi.getNumberStatistics(new Date(beginTime),new Date(endTime)));
    }



    @RequestMapping({"getOverTime"})
    @ResponseBody
    public JSONObject getOverTime(String searchText, int pageNumber, int pageSize) {
        Page<ItsmEventmgr,ItsmEventQueryVo> page = new Page<>();

        List<ItsmEventmgr> itsmEventmgrs1 = iEventmgrApi.getOverTime("%"+searchText+"%");
        List<ItsmEventmgr> itsmEventmgrs = new ArrayList<ItsmEventmgr>();

        for(ItsmEventmgr itsmEventmgr:itsmEventmgrs1){
            //超时时间
            if(itsmEventmgr.getSlaId()!=0){
                List<SLAManageVo> slaManageVos = iSlaManageApi.getSlaByServiceAndPriority(itsmEventmgr.getServiceCatalogId() + "",itsmEventmgr.getPriorityId()+ "");
                SLAManageVo slaManageVo = slaManageVos!=null && slaManageVos.size() >0 ?slaManageVos.get(0):null;


                if(slaManageVo != null && slaManageVo.getSlaSalveTime() != null && !slaManageVo.getSlaSalveTime().trim().equals("")){
                    //查看是否有冻结记录，如果有，则需减去冻结的时间段
                    List<ItsmEventFreeze> itsmEventFreezeList = iEventmgrApi.findFreezeByEventId(itsmEventmgr.getId()+"");
                    Date createTime = itsmEventmgr.getAcceptTime() != null ?itsmEventmgr.getAcceptTime():itsmEventmgr.getCreateTime();

                    long startTime = createTime.getTime()+(Long.valueOf(slaManageVo.getSlaSalveTime())*60*60*1000);
                    if(itsmEventFreezeList != null && itsmEventFreezeList.size()>0){
                        for(ItsmEventFreeze itsmEventFreeze:itsmEventFreezeList){
                            Date ThawTime = itsmEventFreeze.getThawTime();
                            if (ThawTime == null){
                                ThawTime = new Date();
                            }

                            startTime = startTime+(ThawTime.getTime()-itsmEventFreeze.getFreezeTime().getTime());
                        }
                        itsmEventmgr.setTimeoutSituation(this.getTimeLag(new Date(),new Date(startTime)));
                    }else{
                        itsmEventmgr.setTimeoutSituation(this.getTimeLag(new Date(),new Date(startTime)));
                    }

                    if (itsmEventmgr.getTimeoutSituation().indexOf("超") != -1){
                        itsmEventmgrs.add(itsmEventmgr);
                    }
                }else{
                    itsmEventmgr.setTimeoutSituation("无SLA");
                }
            }else{
                itsmEventmgr.setTimeoutSituation("无SLA");
            }
        }

        long start,end;
        start = (pageNumber - 1) * pageSize;
        end =  start +  pageSize;
        List<ItsmEventmgr> itsmEventmgrList = new ArrayList<ItsmEventmgr>();


        for (long i = (start > itsmEventmgrs.size()?itsmEventmgrs.size():start ); i < (end > itsmEventmgrs.size()?itsmEventmgrs.size():end); i++) {
            itsmEventmgrList.add(itsmEventmgrs.get(Integer.parseInt(i+"")));
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",itsmEventmgrList);
        jsonObject.put("page",page.getStartRow());
        jsonObject.put("total",itsmEventmgrs.size());


        return jsonObject;
    }

    @RequestMapping({"getResponseStatistics"})
    @ResponseBody
    public JSONObject getResponseStatistics(long beginTime,long endTime){
        return toSuccess(iEventmgrApi.getResponseStatistics(new Date(beginTime),new Date(endTime)));
    }

    @RequestMapping({"selectServiceStatistics"})
    @ResponseBody
    public JSONObject selectServiceStatistics(){
        return toSuccess(iEventmgrApi.selectServiceStatistics());
    }


    @RequestMapping({"getLoginUserId"})
    @ResponseBody
    public JSONObject getLoginUserId(HttpServletRequest request,HttpSession httpSession){
        return toSuccess(VesApp.getLoginUser(request).getId());
    }

    @RequestMapping({"getEventmgrById"})
    @ResponseBody
    public JSONObject getEventmgrById(String piid){
        ItsmEventmgr itsmEventmgr = iEventmgrApi.findByProcessInst(piid);
        return  toSuccess(itsmEventmgr);
    }

    @RequestMapping({"getEventmgrByIdsNew"})
    @ResponseBody
    public JSONObject getEventmgrByIdsNew(@RequestParam String id){
        String[] strings = id.split(",");
        String[] prioritys = {"紧急","高","中","低"};

        List<String> ids = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (!strings[i].equals(""))
                ids.add(strings[i]);
        }
        List<ItsmEventmgr> itsmEventmgrList = new ArrayList<>();
        for (String idLong : ids) {
            ItsmEventmgr itsmEventmgr = new ItsmEventmgr();
            VesForm vesForm = vesFormRuntime.findFormById(idLong);
            itsmEventmgr.setId(Long.valueOf(vesForm.getId()));
            itsmEventmgr.setPriorityId((int)vesForm.findVesItemByName(VesItemConstant.PRIORITY_ID).getItemValue());
            itsmEventmgr.setWorksheetId(vesForm.getWorksheetId());
            itsmEventmgr.setRequest_type((int)vesForm.findVesItemByName(VesItemConstant.REQUEST_TYPE).getItemValue());
            itsmEventmgr.setUrgency((int) vesForm.findVesItemByName(VesItemConstant.URGENCY).getItemValue());
            itsmEventmgr.setPriorityName(prioritys[itsmEventmgr.getPriorityId()]);
            itsmEventmgr.setSlaId(0);//暂时没办法处理
            itsmEventmgr.setTheme((String)vesForm.findVesItemByName(VesItemConstant.THEME).getItemValue());
            itsmEventmgr.setKeyWord((String)vesForm.findVesItemByName(VesItemConstant.KEY_WORD).getItemValue());
            itsmEventmgr.setCreateUser(Long.valueOf(vesForm.getCreateUser()));
            itsmEventmgr.setCreateTime(vesForm.getCreateDate());
            itsmEventmgr.setProcessKey(vesForm.getProcessDefKey());
            itsmEventmgr.setLocation("");//暂时没办法处理
            itsmEventmgr.setServiceCatalogId(0);//暂时没办法处理
            itsmEventmgr.setSource(null);//暂时没办法处理
//            itsmEventmgr.setStatus(vesForm.getFromStatus());//暂时没办法处理
            itsmEventmgr.setDescription((String) vesForm.findVesItemByName(VesItemConstant.DESCRIPTION).getItemValue());
            itsmEventmgr.setProcess_instanceId(vesForm.getProcessInsId());
            itsmEventmgr.setAppointmentTime(null);//暂时没办法处理
            itsmEventmgr.setApplyUser((String) vesForm.findVesItemByName(VesItemConstant.APPLY_USER).getItemValue());
            itsmEventmgr.setAccept(0);//暂时没办法处理
            ItsmEventSatisfied itsmEventSatisfied = iEventmgrApi.findApraiseByEventId(itsmEventmgr.getId()+"");
            if (itsmEventSatisfied != null){
                itsmEventmgr.setIsAppraise(0);
            }else{
                itsmEventmgr.setIsAppraise(1);
            }

        }
        return null;
    }

    @RequestMapping({"getEventmgrByIds"})
    @ResponseBody
    public JSONObject getEventmgr(@RequestParam String id){

        String[] strings = id.split(",");

        List<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < strings.length; i++) {
            if (!strings[i].equals(""))
                ids.add(Long.parseLong(strings[i]));
        }
        List<ItsmEventmgr> itsmEventmgrs = iEventmgrApi.getEventmgrByIds(ids);
        for (int i = 0; i < itsmEventmgrs.size(); i++) {
            ItsmEventmgr itsmEventmgr = itsmEventmgrs.get(i);


        }

        for(ItsmEventmgr itsmEventmgr:itsmEventmgrs){
            //超时时间
            if(itsmEventmgr.getSlaId()!=0){
                List<SLAManageVo> slaManageVos = iSlaManageApi.getSlaByServiceAndPriority(itsmEventmgr.getServiceCatalogId() + "",itsmEventmgr.getPriorityId()+ "");
                SLAManageVo slaManageVo = slaManageVos!=null && slaManageVos.size() >0 ?slaManageVos.get(0):null;

                if(slaManageVo != null && slaManageVo.getSlaSalveTime() != null && !slaManageVo.getSlaSalveTime().trim().equals("")){
                    //查看是否有冻结记录，如果有，则需减去冻结的时间段
                    List<ItsmEventFreeze> itsmEventFreezeList = iEventmgrApi.findFreezeByEventId(itsmEventmgr.getId()+"");
                    Date createTime = itsmEventmgr.getAcceptTime() != null ?itsmEventmgr.getAcceptTime():itsmEventmgr.getCreateTime();


                    long startTime = createTime.getTime()+(Long.valueOf(slaManageVo.getSlaSalveTime())*60*60*1000);
                    if(itsmEventFreezeList != null && itsmEventFreezeList.size()>0){
                        for(ItsmEventFreeze itsmEventFreeze:itsmEventFreezeList){
                            Date ThawTime = itsmEventFreeze.getThawTime();
                            if (ThawTime == null){
                                ThawTime = new Date();
                            }

                            startTime = startTime+(ThawTime.getTime()-itsmEventFreeze.getFreezeTime().getTime());
                        }

                        itsmEventmgr.setTimeoutSituation(this.getTimeLag(new Date(),new Date(startTime)));
                    }else{
                        itsmEventmgr.setTimeoutSituation(this.getTimeLag(new Date(),new Date(startTime)));
                    }

                }else{
                    itsmEventmgr.setTimeoutSituation("无SLA");
                }
            }else{
                itsmEventmgr.setTimeoutSituation("无SLA");
            }

        }

        return toSuccess(itsmEventmgrs);
    }

    @RequestMapping({"selectFreezeEvent"})
    @ResponseBody
    public JSONObject selectFreezeEvent(HttpServletRequest request ,String searchText,int pageSize,int pageNumber) {
        JSONObject jsonObject = new JSONObject();
        //type是工单状态
        ILoginUser loginUser =  VesApp.getLoginUser(request);

        Long userId =loginUser.getId();//取到用户id
        String account = loginUser.getAccount();//取得用戶名

        //分页
        ItsmEventQueryVo condition = new ItsmEventQueryVo();
        if(searchText!=null&&!searchText.equals("")){
            condition.setSearchText("%"+searchText+"%");
        }else{
            condition.setSearchText("%%");
        }
        Page page = new Page();
        int startNumber = (pageNumber-1)*pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        page.setCondition(condition);

        if(userId == 1){
            condition.setIsAdmin(1);
        }
        condition.setCreateUserId(userId);
        condition.setUserAccount(account);
        condition.setLoginUserId(userId);
        page.setCondition(condition);
        iEventmgrApi.getFreezeEvent(page);

        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        jsonObject.put("page",page.getStartRow());

        return jsonObject;
    }

    /*
    * 新接口----暂时没有使用，等待service应用
    * */
    @RequestMapping({"selectFreezeEventStatisticsNew"})
    @ResponseBody
    public JSONObject selectFreezeEventStatisticsNew(String searchText,int pageSize,int pageNumber) {
        JSONObject jsonObject = new JSONObject();
        ProcessUserTaskPageQuery query = new ProcessUserTaskPageQuery();
        query.setPageSize(pageSize);
        query.setPageNumber(pageNumber);
        query.setSearchText(searchText);
        List<ProcessUserTask> processUserTaskList = processInstanceApi.selectAllFreeze(query);
        for (ProcessUserTask processUserTask : processUserTaskList) {
            VesForm vesForm = vesFormRuntime.findFormById(processUserTask.getFormId());
        /*
        * 进行填充值操作。
        * */

        }
        jsonObject.put("rows","");
        jsonObject.put("total",query.getSunCount());
        jsonObject.put("page",query.getPageSize());
        return jsonObject;
    }
    /*
    * 旧接口----不用了
    * */
    @RequestMapping({"selectFreezeEventStatistics"})
    @ResponseBody
    public JSONObject selectFreezeEventStatistics(HttpServletRequest request ,String searchText,int pageSize,int pageNumber) {
        JSONObject jsonObject = new JSONObject();

        //分页
        ItsmEventQueryVo condition = new ItsmEventQueryVo();
        if(searchText!=null&&!searchText.equals("")){
            condition.setSearchText("%"+searchText+"%");
        }else{
            condition.setSearchText("%%");
        }
        Page page = new Page();
        int startNumber = (pageNumber-1)*pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        page.setCondition(condition);
        iEventmgrApi.getFreezeEventStatistics(page);

        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        jsonObject.put("page",page.getStartRow());

        return jsonObject;
    }

    /**
     * 根据前台传入的type值进行判断查询
     *
     * @return
     */
    @RequestMapping({"AccordingToType"})
    @ResponseBody
    public JSONObject AccordingToType(HttpServletRequest request, String type ,String searchText,int pageSize,int pageNumber) {
        JSONObject jsonObject = new JSONObject();
        //type是工单状态
        Long userId = VesApp.getLoginUser(request).getId();//取到用户id
        String account = VesApp.getLoginUser(request).getAccount();//取得用戶名

        //分页
        ItsmEventQueryVo condition = new ItsmEventQueryVo();
        if(searchText!=null&&!searchText.equals("")){
            condition.setSearchText("%"+searchText+"%");
        }else{
            condition.setSearchText("%%");
        }
        Page page = new Page();
        int startNumber = (pageNumber-1)*pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        page.setCondition(condition);
        List<IRole> roles = VesApp.getLoginUser(request).getRoles();
        if(roles != null && roles.size() >0){
            for (IRole role:roles){
                if(role.getId() == 424500){
                    condition.setIsService(1);
                }
                if(role.getId() == 424501){
                    condition.setIsFirstLine(1);
                }
                if(role.getId() == 424502){
                    condition.setIsSecondLine(1);
                }
                if(role.getId() == 499500){
                    condition.setIsServiceManager(1);
                }
            }
        }
        if(userId == 1){
            condition.setIsAdmin(1);
        }

        if (type.equals(AccordingToType.Freezed.getIndex())) {//已冻结工单
            condition.setCreateUserId(userId);
            condition.setUserAccount(account);
            condition.setLoginUserId(userId);
            page.setCondition(condition);
            iEventmgrApi.getFreezeEvent(page);
        } else if (type.equals(AccordingToType.Unappraise.getIndex())) {//待评价工单--包括已处理但是未完成的工单
            condition.setCreateUserId(userId);
            page.setCondition(condition);
            iEventmgrApi.getUnAppraise(page);
        }

        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        jsonObject.put("page",page.getStartRow());

        return jsonObject;
    }

    public Map<String,Object> getRoles(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        ILoginUser iLoginUser = VesApp.getLoginUser(request);
        List<IRole> roles = VesApp.getLoginUser(request).getRoles();
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
            }
        }
        if(iLoginUser.getId() == 1){
            map.put("isAdmin",1);
        }
        map.put("userAccount",iLoginUser.getAccount());
        return map;
    }

    /**
     * 根据主键查看详情
     */
    @RequestMapping({"lookById"})
    @ResponseBody
    public JSONObject lookById(String worksheetId,String taskId,HttpServletRequest request) {
        ItsmEventmgr eventmgr = new ItsmEventmgr();
        eventmgr.setWorksheetId(worksheetId);
        eventmgr= iEventmgrApi.findByEventId(eventmgr);
        List<IRole> roles = VesApp.getLoginUser(request).getRoles();
        if(roles != null && roles.size() >0){
            for (IRole role:roles){
                if(role.getId() == 424500){
                    eventmgr.setIsService(1);
                }
                if(role.getId() == 424501){
                    eventmgr.setIsFirstLine(1);
                }
                if(role.getId() == 424502){
                    eventmgr.setIsSecondLine(1);
                }
                if(role.getId() == 499500){
                    eventmgr.setIsServiceManager(1);
                }
            }
        }
        ItsmEventFreeze freeze = iEventmgrApi.findLastFreezeByEventId(eventmgr.getId());
        if(freeze != null){
            eventmgr.setFreezeUserId(freeze.getFreezeUserId());
        }
        if(VesApp.getLoginUser(request).getAccount() .equals("admin")){
            eventmgr.setIsAdmin(1);
        }
        eventmgr.setLoginUserId(VesApp.getLoginUser(request).getId());
        if (taskId != null){
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if(task != null){
                eventmgr.setNodeFlow(task.getName());
                eventmgr.setAssignee(task.getAssignee());
            }
        }else{
            eventmgr.setNodeFlow("冻结");
            eventmgr.setAssignee(VesApp.getLoginUser(request).getName());
        }
        return toSuccess(eventmgr);
    }

    /**
     * 根据分类ID获取工单所属服务分类
     * @param catalogId
     * @return
     */
    @RequestMapping({"getServiceType"})
    @ResponseBody
    public JSONObject getServiceType(long catalogId){
        List<DirectoryManageVo> directoryManageVos = iDirectoryManageApi.serviceListById(catalogId);
        return toSuccess(directoryManageVos);
    }


    /**
     * 工单解决
     */
    @RequestMapping({"doneTheTask"})
    @ResponseBody
    public JSONObject doneTheTask(@RequestBody HttpRequestBody httpRequestBody,HttpServletRequest request) {
        int returnValue = 0;
        boolean flag = false;
        String preAssignee = "";
        String nowAssignee = "";
        ItsmEventmgr eventmgr = new ItsmEventmgr();
        ItsmReview review = new ItsmReview();
        review.setReviewDate(new Date());
        review.setReviewAccount(VesApp.getLoginUser(request).getAccount());
        review.setReviewName(VesApp.getLoginUser(request).getName());
        eventmgr.setWorksheetId(httpRequestBody.getWorksheetId());
        eventmgr = iEventmgrApi.findByEventId(eventmgr);

        if(httpRequestBody.getItemIds() != null && httpRequestBody.getItemIds().size() > 0){
            ItsmEventAssetShip itsmEventAssetShip = new ItsmEventAssetShip();
            for(Long itemId:httpRequestBody.getItemIds()){
                int num = iEventmgrApi.getShipByEventIdAndAssetId(eventmgr.getId(),itemId);
                if(num > 0){
                    continue;
                }
                itsmEventAssetShip.setEventId(eventmgr.getId());
                itsmEventAssetShip.setItemId(itemId);
                iEventmgrApi.addEventAssetShip(itsmEventAssetShip);
            }
        }

        review.setProcId(eventmgr.getProcess_instanceId());
        eventmgr.setStatus(5);//状态修改为"已处理"
        Task task = taskService.createTaskQuery().processInstanceId(eventmgr.getProcess_instanceId()).singleResult();
        review.setNodeName(task.getName());
        String next = "";

        if(!task.getAssignee().equals(VesApp.getLoginUser(request).getAccount())){
            flag = true;
            preAssignee = task.getAssignee();
            nowAssignee = VesApp.getLoginUser(request).getAccount();
            task.setAssignee(nowAssignee);
            taskService.saveTask(task);
        }

        if(task != null && task.getName().equals("一线工程师")){
            next = "firstLineSolve";
            if(flag){
                review.setNodeDesc("【"+iUserApi.getByAccount(nowAssignee).getName()+"】"+"代替一线工程师"+"【"+iUserApi.getByAccount(preAssignee).getName()+"】"+"解决工单");
            }else{
                review.setNodeDesc("一线工程师"+"【"+review.getReviewName()+"】"+"解决工单");
            }
            review.setProcessMode(4);
            returnValue = 3;
        }else if(task != null && task.getName().equals("服务台受理")){
            next = "serviceSolve";
            if(flag){
                review.setNodeDesc("【"+iUserApi.getByAccount(nowAssignee).getName()+"】"+"代替服务台"+"【"+iUserApi.getByAccount(preAssignee).getName()+"】"+"直接解决工单");
            }else{
                review.setNodeDesc("服务台"+"【"+review.getReviewName()+"】"+"直接解决工单");
            }

            review.setProcessMode(4);
            returnValue = 2;
        }else if(task != null && task.getName().equals("二线工程师")){
            if(flag){
                review.setNodeDesc("【"+iUserApi.getByAccount(nowAssignee).getName()+"】"+"代替二线工程师"+"【"+iUserApi.getByAccount(preAssignee).getName()+"】"+"解决工单");
            }else{
                review.setNodeDesc("二线工程师"+"【"+review.getReviewName()+"】"+"解决工单");
            }
            review.setProcessMode(4);
            returnValue = 4;
        }
        review.setTaskId(task.getId());
        String nextUser = "";
        if(eventmgr.getCreateUser() == 0){
            nextUser = iEventmgrApi.findApplyUserById(Long.valueOf(eventmgr.getApplyUserId())).getMobile();
        }else{
            nextUser = iUserApi.get((long)eventmgr.getCreateUser()).getAccount();
        }

        iBaseProcessApi.completeTask(eventmgr.getProcess_instanceId(),next,nextUser,httpRequestBody.getComment());
        review.setReviewDesc(httpRequestBody.getComment());
        review.setNext("等待提单人评价工单");
        iEventmgrApi.saveReview(review);
        eventmgr.setSolveTime(new Date());//解决时间
        iEventmgrApi.updateEvent(eventmgr);

        return toSuccess(returnValue);
    }

    /**
     * 服务台经理审核工单
     * @param result
     * @param procId
     * @return
     */
    @RequestMapping({"checkEvent"})
    @ResponseBody
    public JSONObject checkEvent(String result,String solution,String procId,HttpServletRequest request) throws Exception{
        Task task = taskService.createTaskQuery().processInstanceId(procId).singleResult();

        //流程轨迹对象
        ItsmReview itsmReview = new ItsmReview();
        itsmReview.setProcId(procId);
        itsmReview.setTaskId(task.getId());
        itsmReview.setReviewDate(new Date());
        itsmReview.setReviewAccount(VesApp.getLoginUser(request).getAccount());
        itsmReview.setReviewName(VesApp.getLoginUser(request).getName());
        itsmReview.setNodeName(task.getName());

        //工单对象
        ItsmEventmgr itsmEventmgr = iEventmgrApi.findByProcessInst(procId);
        String next = "";
        String user = "";

        if(result != null && result.equals("1")){//通过
            iBaseProcessApi.completeTask(procId,"pass",null,solution);
            itsmReview.setReviewDesc("审核通过");
            itsmReview.setNodeDesc("服务台经理"+"【"+itsmReview.getReviewName()+"】"+"审核通过");
            itsmReview.setProcessMode(7);
            itsmEventmgr.setStatus(1);
            itsmReview.setNext("服务台经理审核通过，流程结束");
        }else if(result != null && result.equals("2")){//审核不通过
            //根据历史进行判断，有二线则驳回给二线，没有二线驳回给一线，否则驳回给服务台
            List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(procId).orderByTaskCreateTime().asc().list();
            if(historicTaskInstanceList.size()>=3){
                HistoricTaskInstance historicTaskInstance = historicTaskInstanceList.get(historicTaskInstanceList.size()-3);
                if(historicTaskInstance.getTaskDefinitionKey().equals("secondLine")){
                    next = "noSecondPass";
                    itsmEventmgr.setStatus(4);
                }else if(historicTaskInstance.getTaskDefinitionKey().equals("firstLine")){
                    next = "noFirstPass";
                    itsmEventmgr.setStatus(4);
                }else if(historicTaskInstance.getTaskDefinitionKey().equals("sAccept")){
                    next = "noServicePass";
                    itsmEventmgr.setStatus(3);
                }
                user = historicTaskInstance.getAssignee();
                iBaseProcessApi.completeTask(procId,next,user,solution);
                itsmReview.setReviewDesc("审核不通过，驳回");
                itsmReview.setNodeDesc("服务台经理"+"【"+itsmReview.getReviewName()+"】"+"驳回");
                itsmReview.setProcessMode(8);
            }else if(historicTaskInstanceList.size()==2){//提交工单时直接解决的，转服务台
                next = "noServicePassAnd";
                user = "";
                itsmEventmgr.setStatus(2);
                iBaseProcessApi.completeTask(procId,next,user,solution);
                itsmReview.setReviewDesc("审核不通过,驳回");
                itsmReview.setNodeDesc("服务台经理"+"【"+itsmReview.getReviewName()+"】"+"驳回");
                itsmReview.setProcessMode(8);
            }else{
                throw new Exception("流程错误，请联系管理员");
            }
            itsmReview.setNext("服务台经理审核不通过，已驳回，等待重新处理");
        }

        iEventmgrApi.updateEvent(itsmEventmgr);
        iEventmgrApi.saveReview(itsmReview);


        return toSuccess(null);
    }

    /**
     * 分派任务
     */
    @RequestMapping({"directDistribution"})
    @ResponseBody
    public JSONObject directDistribution(String worksheetId, String userFirstName,
                                         String comment,String tid,String taskAnssigee,HttpServletRequest request) {
        boolean flag = false;
        String preAssignee = "";
        String nowAssignee = "";
        ItsmEventmgr eventmgr = new ItsmEventmgr();
        eventmgr.setWorksheetId(worksheetId);
        eventmgr = iEventmgrApi.findByEventId(eventmgr);

        ItsmReview review = new ItsmReview();
        review.setTaskId(tid);

        if(!taskAnssigee.equals(VesApp.getLoginUser(request).getAccount())){
            flag = true;
            preAssignee = taskAnssigee;
            nowAssignee = VesApp.getLoginUser(request).getAccount();

        }
        if(userFirstName==null || userFirstName.equals("")){
            userFirstName = VesApp.getLoginUser(request).getAccount();
        }


        //更新工单状态
        ItsmEventmgr updateEventmgr = eventmgr;
        updateEventmgr.setStatus(StateOfApproval.Processing.getIndex());
        updateEventmgr.setAcceptTime(new Date());//受理时间
        updateEventmgr.setIsDraft(1);
        int returnValue = iEventmgrApi.updateEvent(updateEventmgr);

        review.setProcId(eventmgr.getProcess_instanceId());

        review.setReviewDate(new Date());
        review.setReviewAccount(VesApp.getLoginUser(request).getAccount());
        review.setReviewName(VesApp.getLoginUser(request).getName());
        if(flag && !preAssignee.equals("")){
            review.setNodeDesc("【"+iUserApi.getByAccount(nowAssignee).getName()+"】"+"代替"+"【"+iUserApi.getByAccount(preAssignee).getName()+"】"
                    +"分派给"+"【"+iUserApi.getUserByAccount(userFirstName).getName()+"】");
        }else{
            review.setNodeDesc("【"+VesApp.getLoginUser(request).getName()+"】"+"分派给"+"【"+iUserApi.getUserByAccount(userFirstName).getName()+"】");
        }

        review.setProcessMode(2);
        review.setNext("等待"+"【"+iUserApi.getUserByAccount(userFirstName).getName()+"】"+"处理工单");
        iEventmgrApi.saveReview(review);

        /*ItsmEventMessage message = new ItsmEventMessage();
        message.setEventId(eventmgr.getId());
        message.setWorksheetId(eventmgr.getWorksheetId());
        message.setIfRead(0);
        message.setOverTime(null);
        message.setReminder(null);
        message.setCreateDate(new Date());
        message.setType(1);
        message.setIsDeleted(0);
        message.setReceiveUser(iUserApi.getUserByAccount(userFirstName).getId());
        iEventMessageApi.insertMessage(message);*/


        return toSuccess(returnValue);
    }

    /**
     * 一线转派给一线人员
     * @return
     */
    @RequestMapping({"redirectFirst"})
    @ResponseBody
    public  JSONObject redirectFirst(@RequestBody HttpRequestBody requestBody,HttpServletRequest request){
        boolean flag = false;
        String preAssignee = "";
        String nextAssignee = "";
        ItsmEventmgr itsmEventmgr = new ItsmEventmgr();
        itsmEventmgr.setWorksheetId(requestBody.getWorksheetId());
        itsmEventmgr = iEventmgrApi.findByEventId(itsmEventmgr);
        if(requestBody.getItemIds()!=null && requestBody.getItemIds().size()>0){
            ItsmEventAssetShip itsmEventAssetShip = new ItsmEventAssetShip();
            for(Long itemId:requestBody.getItemIds()){
                itsmEventAssetShip.setEventId(itsmEventmgr.getId());
                itsmEventAssetShip.setItemId(itemId);
                iEventmgrApi.addEventAssetShip(itsmEventAssetShip);
            }
        }

        //在转派表中保存数据
        ItsmEventRedeploy itsmEventRedeploy = new ItsmEventRedeploy();
        itsmEventRedeploy.setProcId(Long.valueOf(itsmEventmgr.getProcess_instanceId()));
        itsmEventRedeploy.setComment(requestBody.getComment());
        itsmEventRedeploy.setTaskName("一线转派");
        itsmEventRedeploy.setOperator(VesApp.getLoginUser(request).getAccount());
        itsmEventRedeploy.setCreateTime(new Date());


        Task task = taskService.createTaskQuery().processInstanceId(itsmEventmgr.getProcess_instanceId()).singleResult();
        if(!task.getAssignee().equals(VesApp.getLoginUser(request).getAccount())){
            flag = true;
            preAssignee = task.getAssignee();
            nextAssignee = VesApp.getLoginUser(request).getAccount();
        }

        itsmEventRedeploy.setTaskId(Long.valueOf(task.getId()));
        itsmEventRedeploy.setPreUserAccount(task.getAssignee());

        task.setAssignee(requestBody.getUserAccount());

        itsmEventRedeploy.setNextUserAccount(task.getAssignee());


        taskService.saveTask(task);


        //在转派表中保存数据
        iEventmgrApi.saveRedeploy(itsmEventRedeploy);

        //在历史表中保存信息
        ItsmReview review = new ItsmReview();
        review.setProcId(itsmEventmgr.getProcess_instanceId());
        review.setTaskId(task.getId());
        review.setReviewDate(new Date());
        review.setReviewAccount(VesApp.getLoginUser(request).getAccount());
        review.setReviewName(VesApp.getLoginUser(request).getName());
        if(flag){
            review.setNodeDesc("【"+iUserApi.getUserByAccount(nextAssignee).getName()+"】"+"代替"+"【"+iUserApi.getUserByAccount(preAssignee).getName()+"】"+"转派给一线工程师" +
                    "【"+iUserApi.getUserByAccount(requestBody.getUserAccount()).getName()+"】");
        }else{
            review.setNodeDesc("【"+review.getReviewName()+"】"+"转派给一线工程师"+"【"+iUserApi.getUserByAccount(requestBody.getUserAccount()).getName()+"】");
        }

        review.setIsDispatch(1);
        review.setNext("等待一线工程师处理工单");
        iEventmgrApi.saveReview(review);
        return toSuccess(itsmEventmgr);
    }

    /**
     * 提交二线处理
     * @return
     */
    @RequestMapping({"submitSecondLine"})
    @ResponseBody
    public JSONObject submitSecondLine(@RequestBody HttpRequestBody httpRequestBody,HttpServletRequest request){
        boolean flag = false;
        String preAssignee = "";
        String nowAssignee = "";
        ItsmEventmgr itsmEventmgr = new ItsmEventmgr();
        itsmEventmgr.setWorksheetId(httpRequestBody.getWorksheetId());
        itsmEventmgr = iEventmgrApi.findByEventId(itsmEventmgr);

        if(httpRequestBody.getItemIds() != null && httpRequestBody.getItemIds().size() > 0){
            ItsmEventAssetShip itsmEventAssetShip = new ItsmEventAssetShip();
            for(Long itemId:httpRequestBody.getItemIds()){
                int num = iEventmgrApi.getShipByEventIdAndAssetId(itsmEventmgr.getId(),itemId);
                if(num > 0){
                    continue;
                }
                itsmEventAssetShip.setEventId(itsmEventmgr.getId());
                itsmEventAssetShip.setItemId(itemId);
                iEventmgrApi.addEventAssetShip(itsmEventAssetShip);
            }
        }


        ItsmReview review = new ItsmReview();
        review.setProcId(itsmEventmgr.getProcess_instanceId());
        review.setTaskId(taskService.createTaskQuery().processInstanceId(itsmEventmgr.getProcess_instanceId()).singleResult().getId());

        Task task = taskService.createTaskQuery().processInstanceId(itsmEventmgr.getProcess_instanceId()).singleResult();
        if(!task.getAssignee().equals(VesApp.getLoginUser(request).getAccount())){
            flag = true;
            preAssignee = task.getAssignee();
            nowAssignee = VesApp.getLoginUser(request).getAccount();
            task.setAssignee(nowAssignee);
            taskService.saveTask(task);
        }

        iBaseProcessApi.completeTask(itsmEventmgr.getProcess_instanceId(),"toSecondLine",httpRequestBody.getUserAccount(),httpRequestBody.getComment());

        review.setReviewDate(new Date());
        review.setReviewAccount(VesApp.getLoginUser(request).getAccount());
        review.setReviewName(VesApp.getLoginUser(request).getName());
        if(flag){
            review.setNodeDesc("【"+iUserApi.getUserByAccount(nowAssignee).getName()+"】"+"代替一线工程师"+"【"+iUserApi.getUserByAccount(preAssignee).getName()+"】"+
                    "提交给二线工程师"+"【"+iUserApi.getUserByAccount(httpRequestBody.getUserAccount()).getName()+"】");
        }else{
            review.setNodeDesc("【"+review.getReviewName()+"】"+"提交给二线工程师"+"【"+iUserApi.getUserByAccount(httpRequestBody.getUserAccount()).getName()+"】");
        }
        review.setProcessMode(3);
        review.setNext("等待二线工程师处理工单");
        iEventmgrApi.saveReview(review);
        ItsmEventMessage message = new ItsmEventMessage();
//        message.setEventId(itsmEventmgr.getId());
//        message.setWorksheetId(itsmEventmgr.getWorksheetId());
//        message.setType(1);
//        message.setCreateDate(new Date());
//        message.setReminder(null);
//        message.setOverTime(null);
//        message.setIfRead(0);
//        message.setIsDeleted(0);
        message.setReceiveUser(iUserApi.getUserByAccount(httpRequestBody.getUserAccount()).getId());
        iEventMessageApi.insertMessage(message);
        return toSuccess(null);
    }

    @RequestMapping({"reDispacthSecondLine"})
    @ResponseBody
    public JSONObject reDispacthSecondLine(@RequestBody HttpRequestBody httpRequestBody,HttpServletRequest request){
        boolean flag = false;
        String preAssignee = "";
        String nowAssignee = "";
        ItsmEventmgr itsmEventmgr = new ItsmEventmgr();
        itsmEventmgr.setWorksheetId(httpRequestBody.getWorksheetId());
        itsmEventmgr = iEventmgrApi.findByEventId(itsmEventmgr);

        if(httpRequestBody.getItemIds() != null && httpRequestBody.getItemIds().size() > 0){
            ItsmEventAssetShip itsmEventAssetShip = new ItsmEventAssetShip();
            for(Long itemId:httpRequestBody.getItemIds()){
                int num = iEventmgrApi.getShipByEventIdAndAssetId(itsmEventmgr.getId(),itemId);
                if(num > 0){
                    continue;
                }
                itsmEventAssetShip.setEventId(itsmEventmgr.getId());
                itsmEventAssetShip.setItemId(itemId);
                iEventmgrApi.addEventAssetShip(itsmEventAssetShip);
            }
        }

        //在转派表中保存数据
        ItsmEventRedeploy itsmEventRedeploy = new ItsmEventRedeploy();
        itsmEventRedeploy.setProcId(Long.valueOf(itsmEventmgr.getProcess_instanceId()));
        itsmEventRedeploy.setComment(httpRequestBody.getComment());
        itsmEventRedeploy.setTaskName("二线转派");
        itsmEventRedeploy.setOperator(VesApp.getLoginUser(request).getAccount());
        itsmEventRedeploy.setCreateTime(new Date());

        Task task = taskService.createTaskQuery().processInstanceId(itsmEventmgr.getProcess_instanceId()).singleResult();
        if(!task.getAssignee().equals(VesApp.getLoginUser(request).getAccount())){
            flag = true;
        }
        preAssignee = task.getAssignee();
        nowAssignee = VesApp.getLoginUser(request).getAccount();

        //------------
        itsmEventRedeploy.setTaskId(Long.valueOf(task.getId()));
        itsmEventRedeploy.setPreUserAccount(task.getAssignee());
        itsmEventRedeploy.setNextUserAccount(httpRequestBody.getUserAccount());

        task.setAssignee(httpRequestBody.getUserAccount());
        taskService.saveTask(task);

        //保存转派数据
        iEventmgrApi.saveRedeploy(itsmEventRedeploy);

        ItsmReview review = new ItsmReview();
        review.setProcId(itsmEventmgr.getProcess_instanceId());
        review.setReviewDate(new Date());
        review.setReviewAccount(VesApp.getLoginUser(request).getAccount());
        review.setReviewName(VesApp.getLoginUser(request).getName());
        if(flag){
            review.setNodeDesc("【"+iUserApi.getUserByAccount(nowAssignee).getName()+"】"+"代替二线工程师"+"【"+iUserApi.getUserByAccount(preAssignee).getName()+"】"+
                                "转派给二线"+"【"+iUserApi.getUserByAccount(httpRequestBody.getUserAccount()).getName()+"】");
        }else{
            review.setNodeDesc("二线工程师"+"【"+iUserApi.getUserByAccount(preAssignee).getName()+"】"+"转派给二线"+"【"+iUserApi.getUserByAccount(httpRequestBody.getUserAccount()).getName()+"】");
        }
        review.setIsDispatch(1);
        review.setNext("等待二线工程师处理工单");
        iEventmgrApi.saveReview(review);

        return toSuccess(null);
    }

    /**
     * 个人的本月统计工单数量
     */
    @RequestMapping({"monthStatistics"})
    @ResponseBody
    public JSONObject monthStatistics(HttpServletRequest request) {
        Map<String,Object> roles = this.getRoles(request);
        long loginUser=VesApp.getLoginUser(request).getId();
        List<Map<String,Object>> returnMap= new ArrayList<>();
        int unHandle = iEventmgrApi.monthStatistics(roles,1);//未处理
        Map<String,Object> map = new HashMap<>();
        map.put("name","未处理工单");
        map.put("value",unHandle);
        returnMap.add(map);
        int unDispatch = iEventmgrApi.monthStatistics(roles,2);//未分派的工单
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","未分派工单");
        map1.put("value",unDispatch);
        returnMap.add(map1);
        int freezed = iEventmgrApi.monthStatistics(roles,3);//未分派的工单
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","已冻结工单");
        map2.put("value",freezed);
        returnMap.add(map2);
        int submit = iEventmgrApi.monthStatistics(roles,4);//未分派的工单
        Map<String,Object> map3 = new HashMap<>();
        map3.put("name","已提交工单");
        map3.put("value",submit);
        returnMap.add(map3);
        int handled = iEventmgrApi.monthStatistics(roles,5);//未分派的工单
        Map<String,Object> map4 = new HashMap<>();
        map4.put("name","已处理工单");
        map4.put("value",handled);
        returnMap.add(map4);

        return toSuccess(returnMap);
    }


    /**
     * 显示流程图
     * @param pid
     * @return
     */
    @RequestMapping({"showProcessImg"})
    @ResponseBody
    public byte[] showProcessImg(String pid, HttpServletResponse response){

        //我写的方法
        //获取流程图片
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pid).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

        //获取历史流程实例
//        HistoricActivityInstance processInstance = historyService.createHistoricActivityInstanceQuery().processInstanceId(pid).singleResult();

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(historicProcessInstance.getProcessDefinitionId());


        List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getProcessDefinitionId()).list();

        /*//高亮环节ID集合
        List<String> highLightedActivitis = new ArrayList<>();
        //高亮线路ID集合
        List<String> highLightedFlows = new ArrayList<>();

        //获取需要的高亮的线
        for (int i = 0; i < highLightedActivitList.size() - 1; i++) {
            ActivityImpl activityImp1 = definitionEntity.findActivity(highLightedActivitList.get(i).getActivityId());
            //用以保存后需开始时间相同的节点
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();
            ActivityImpl activityImp11 = definitionEntity.findActivity(highLightedActivitList.get(i + 1).getActivityId());
            //将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(activityImp11);
            for (int j = i+1; j <highLightedActivitList.size()-1; j++) {
                //后续第一个节点
                HistoricActivityInstance activityImpl1 = highLightedActivitList.get(j);
                //后续第二个节点
                HistoricActivityInstance activityImpl2 = highLightedActivitList.get(j + 1);
            }
        }*/
        //高亮环节ID集合
        List<String> highLightedActivities = new ArrayList<>();
        //高亮线路ID集合
        List<String> highLightedFlows = iBaseProcessApi.getHighLightedFlows(definitionEntity,highLightedActivitList);

        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityId = tempActivity.getActivityId();
            highLightedActivities.add(activityId);
        }
        //得到图片输入流(这样加可以防止生成的流程图片乱码)
        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel,"png",highLightedActivities,highLightedFlows,
                processEngineConfiguration.getActivityFontName(),processEngineConfiguration.getLabelFontName(),
                null,1.0);
        //生成本地图片
        try {
            File file = new File(System.getProperty("user.dir")+"/process.png");
            FileUtils.copyInputStreamToFile(inputStream,file);

            OutputStream os = response.getOutputStream();
            //BufferedImage image_ = ImageIO.read(inputStream);
            BufferedImage image = ImageIO.read(file);

            response.setContentType("image/png");
            ImageIO.write(image,"png",os);

            logger.info("生成流程图成功");
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("生成流程图失败");
        }finally{
            IOUtils.closeQuietly(inputStream);
        }



        /*//1.获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pid).singleResult();

        //任务节点
        Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
        //获取流程图片
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());

        //正在活动节点
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(task.getExecutionId());
        ProcessDiagramGenerator pdg = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();

        //获取流程定义节点
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(pid).list();

        //高亮环节ID集合
        List<String> highLightedActivities = new ArrayList<>();
        //高亮线路ID集合
        List<String> highLightedFlows = iBaseProcessApi.getHighLightedFlows(definitionEntity,historicActivityInstanceList);

        for(HistoricActivityInstance tempActivity : historicActivityInstanceList){
            String activityId = tempActivity.getActivityId();
            highLightedActivities.add(activityId);
        }

        //得到图片输入流(这样加可以防止生成的流程图片乱码)
        InputStream inputStream = pdg.generateDiagram(bpmnModel,"png",highLightedActivities,highLightedFlows,
                processEngineConfiguration.getActivityFontName(),processEngineConfiguration.getLabelFontName(),
                null,1.0);
        //生成本地图片
        try {
            File file = new File(System.getProperty("user.dir")+"/process.png");
            FileUtils.copyInputStreamToFile(inputStream,file);

            OutputStream os = response.getOutputStream();
            //BufferedImage image_ = ImageIO.read(inputStream);
            BufferedImage image = ImageIO.read(file);

            response.setContentType("image/png");
            ImageIO.write(image,"png",os);

            logger.info("生成流程图成功");
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("生成流程图失败");
        }finally{
            IOUtils.closeQuietly(inputStream);
        }*/
    }

    /**
     * 流程追踪
     */
    public String showProcessImgNew(String procId) throws Exception {
        List<Map<String,Object>> activityInfos = iTraceProcessApi.traceProcess(procId); //各种节点信息
        Map<String,List<ProcNodeUser>> processUsers = iProcessUserApi.getProcessUsersByInstanceId(procId);
        int maxY=0;
        int maxX=0;
        int minX=99999;
        int minY=99999;
        if(activityInfos!=null && activityInfos.size()>0){
            for(Map<String,Object> list:activityInfos){
                if(list==null){
                    continue;
                }
                Object oy = list.get("x");
                Object ox = list.get("y");

                if(oy!=null && ox!=null){
                    Integer intY = (Integer)oy;
                    Integer intX = (Integer)ox;
                    if(intY>maxY){
                        maxY = intY;
                    }
                    if(intX>maxX){
                        maxX = intX;
                    }
                    if(intX<minX){
                        minX = intX;
                    }
                    if(intY<minY){
                        minY = intY;
                    }
                }

            }
        }
        minY = minY - 2;
        minX = minX - 2;
        return "success";
    }



    /**
     * 根据流程ID获取流程历史信息
     * @return
     */
    @RequestMapping({"getReview"})
    @ResponseBody
    public JSONObject getReview(String processInst){
        List<ItsmReview> reviewList = iEventmgrApi.findReviewByProcId(processInst);
        return toSuccess(reviewList);
    }

    /**
     * 获取所有的优先级列表
     * @return
     */
    @RequestMapping({"vesapp/system/login/getPriority"})
    @ResponseBody
    public JSONObject getPriority(){
        return toSuccess(iPriorityApi.getAll());
    }

    /**
     * 根据优先级ID获取优先级名称
     * @return
     */
    @RequestMapping({"getPriorityById"})
    @ResponseBody
    public JSONObject getPriorityById(Long priorityId){
        ItsmPriority priority = iPriorityApi.getById(priorityId);
        return toSuccess(priority);
    }

    /**
     * 根据用户ID获取用户名
     * @return
     */
    @RequestMapping({"getUserById"})
    @ResponseBody
    public JSONObject getUserById(Long userId){
        com.vlinksoft.ves.system.um.user.bo.User user = iUserApi.get(userId);
        return toSuccess(user);
    }

    /**
     * 根据工单ID获取资产列表信息
     */
    @RequestMapping({"getAssetByEventId"})
    @ResponseBody
    public JSONObject getAssetByEventId(String eventId){
        List<CMDBItem> returnValue = new ArrayList<>();

        VesForm vesForm = iVesFormRuntime.findFormById(eventId);
        VesAssertItem vesAssertItem = (VesAssertItem)vesForm.findVesItemByName("itemIds");
        List<VesAssertElement> vesAssertElements =  vesAssertItem.getItemValue();
        if (vesAssertElements != null) {
            for (int i = 0; i < vesAssertElements.size(); i++) {
                VesAssertElement vesAssertElement = vesAssertElements.get(i);
                CMDBItem cmdbItem = icmdbItemApi.findById(vesAssertElement.getItemId());
                returnValue.add(cmdbItem);
            }
        }
        return toSuccess(returnValue);
    }

    /**
     * 根据工单ID获取申请客户信息
     */
    @RequestMapping({"getApplyUserByEventId"})
    @ResponseBody
    public JSONObject getApplyUserByEventId(Long userId){
        ItsmEventApplyUser itsmEventApplyUser = iEventmgrApi.findApplyUserById(userId);
        return toSuccess(itsmEventApplyUser);
    }

    /**
     * 根据工单ID获取附件信息
     */
    @RequestMapping({"getAttachmentByEventId"})
    @ResponseBody
    public JSONObject getAttachmentByEventId(String eventId){
        List<ItsmEventAttachment> itsmEventAttachmentList = iEventmgrApi.findAttachmentByEventId(eventId);
        return toSuccess(itsmEventAttachmentList);
    }


    public MySheetAction() {
        super();
        iEventmgrApi = WebItsmBeanUtil.getEventmgrService();
        iApplicantApi = WebItsmBeanUtil.getApplicantService();
        iPriorityApi = WebItsmBeanUtil.getIPriority();
        iUserApi = WebItsmBeanUtil.getIUserApi();
        iDirectoryManageApi = WebItsmBeanUtil.getDirectoryService();
        iSlaManageApi = WebItsmBeanUtil.getSlaManage();
        icmdbItemApi = WebItsmBeanUtil.getICMDBItemApi();
        iEventMessageApi = WebItsmBeanUtil.getIEventMessageApi();
    }

    /**
     * 获取时间差
     * @param startTime 现在的时间点
     * @param endTime SLA到期的时间点
     * @return
     */
    public String getTimeLag(Date startTime,Date endTime){
        String returnValue = "";
        long start = startTime.getTime();
        long end = endTime.getTime();
        int hours = (int)(end-start)/(1000*60*60);
        int minute = (int)((end-start)%(1000*60*60))/(1000*60);
        if(hours <0 || minute<0){
            returnValue += "超时";
        }
        return (returnValue + Math.abs(hours)+"小时"+Math.abs(minute)+"分钟");
    }
}
