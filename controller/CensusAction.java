package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.knowledgelogue.api.ISubKnowledgeApi;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.platform.web.vo.IRole;
import com.vlinksoft.ves.servicecatalog.api.IDirectoryManageApi;
import com.vlinksoft.ves.servicecatalog.api.ISlaManageApi;
import com.vlinksoft.ves.servicecatalog.bo.SLAManageVo;
import com.vlinksoft.ves.servicecatalog.bo.ServiceTreeVo;
import com.vlinksoft.ves.system.um.relation.bo.UserDomain;
import com.vlinksoft.ves.system.um.role.api.IRoleApi;
import com.vlinksoft.ves.system.um.user.api.IUserApi;
import com.vlinksoft.ves.system.um.user.bo.User;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import com.vlinksoft.ves.workflow.um.eventmgr.api.IEventmgrApi;
import com.vlinksoft.ves.workflow.um.eventmgr.api.IPriorityApi;
import com.vlinksoft.ves.workflow.um.eventmgr.bo.ItsmEventFreeze;
import com.vlinksoft.ves.workflow.um.eventmgr.bo.ItsmEventmgr;
import com.vlinksoft.ves.workflow.um.eventmgr.bo.ItsmPriority;
import com.vlinksoft.ves.workflow.um.eventmgr.vo.FreezeSheetVo;
import com.vlinksoft.ves.workflow.um.eventmgr.vo.ScreenEventPo;
import com.vlinksoft.ves.workflow.um.eventmgr.vo.ScreenEventQueryQo;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.eclipse.core.runtime.ILog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * Created by admin on 2017/11/6.
 */
@Controller
@RequestMapping({"/vesapp/omcenter/Census"})
public class CensusAction extends BaseAction {

    private final static Logger logger = LoggerFactory.getLogger(CensusAction.class);
    private IEventmgrApi iEventmgrApi;
    private ISlaManageApi iSlaManageApi;
    private IDirectoryManageApi iDirectoryManageApi;
    private IPriorityApi iPriorityApi;
    private IDutyManageApi iDutyManageApi;
    private IRoleApi iRoleApi;
    private ISubKnowledgeApi iSubKnowledgeApi;
    private IUserApi iUserApi;
    @Autowired
    private TaskService taskService;



    /**
     * 今日统计
     * @param type
     * @return
     */
    @RequestMapping({"servicework"})
    @ResponseBody
    public JSONObject list(String type) {
        List<Map<String,Object>> list = new ArrayList<>();
        return toSuccess(list);
    }

    /**
     *服务台今日工作量
     * @return
     */
    @RequestMapping({"servicenum"})
    @ResponseBody
    public JSONObject servicenum(HttpServletRequest request,int type_){//查询参数1当天2本周3本月
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> roles = this.getRoles(request);

        Map<String,Object> map = new HashMap();
        map.put("name","未完成工单");
        map.put("value",iEventmgrApi.getNumberOfSheet(roles,1,type_));//未完成工单
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","未分派工单");
        map.put("value",iEventmgrApi.getNumberOfSheet(roles,2,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已冻结工单");
        map.put("value",iEventmgrApi.getNumberOfSheet(roles,3,type_));//已冻结工单
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已提交工单");
        map.put("value",iEventmgrApi.getNumberOfSheet(roles,4,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已处理工单");
        map.put("value",iEventmgrApi.getNumberOfSheet(roles,5,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","待评价工单");
        map.put("value",iEventmgrApi.getNumberOfSheet(roles,6,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已完成工单");
        map.put("value",iEventmgrApi.getNumberOfSheet(roles,7,type_));
        returnValue.add(map);

        return toSuccess(returnValue);
    }

    /**
     * 工单管理-我的工单-工单统计
     */
    @RequestMapping({"mySheetNum"})
    @ResponseBody
    public JSONObject mySheetNum(int type_,HttpServletRequest request){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> roles = this.getRoles(request);

        Map<String,Object> map = new HashMap();
        map.put("name","未处理工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,1,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","未分派工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,2,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已冻结工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,3,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已提交工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,4,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已处理工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,5,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","待评价工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,6,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已完成工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,7,type_));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","待审核工单");
        map.put("value",iEventmgrApi.getMySheetNum(roles,8,type_));
        returnValue.add(map);

        return toSuccess(returnValue);
    }

    /**
     * 工单管理-我的工单-已处理统计
     */
    @RequestMapping({"handleLevelNum"})
    @ResponseBody
    public JSONObject handleLevelNum(int type_,HttpServletRequest request){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> roles = this.getRoles(request);
        Map<String,Object> map = new HashMap<>();
        map.put("urgency",iEventmgrApi.getMySheetLevelNum(roles,1,type_));
        map.put("high",iEventmgrApi.getMySheetLevelNum(roles,2,type_));
        map.put("middle",iEventmgrApi.getMySheetLevelNum(roles,3,type_));
        map.put("low",iEventmgrApi.getMySheetLevelNum(roles,4,type_));
        returnValue.add(map);
        return toSuccess(returnValue);
    }

    /**
     * 工单管理-我的工单-响应超时
     */
    @RequestMapping({"overTimeWithNum"})
    @ResponseBody
    public JSONObject overTimeWithNum(int type_, HttpServletRequest request){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> roles = this.getRoles(request);
        Map<String,Object> map = new HashMap<>();
        map.put("total",iEventmgrApi.overTimeWithNum(roles,6,type_));
        map.put("urgency",iEventmgrApi.overTimeWithNum(roles,1,type_));
        map.put("high",iEventmgrApi.overTimeWithNum(roles,2,type_));
        map.put("middle",iEventmgrApi.overTimeWithNum(roles,3,type_));
        map.put("low",iEventmgrApi.overTimeWithNum(roles,4,type_));
        map.put("overTime",iEventmgrApi.overTimeWithNum(roles,5,type_));
        returnValue.add(map);
        return toSuccess(returnValue);
    }

    /**
     * 工单管理-我的工单-解决超时
     */
    @RequestMapping({"overTimeHandleNum"})
    @ResponseBody
    public JSONObject overTimeHandleNum(int type_,HttpServletRequest request){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> roles = this.getRoles(request);
        Map<String,Object> map = new HashMap<>();
        map.put("total",iEventmgrApi.overTimeHandleNum(roles,6,type_));
        map.put("urgency",iEventmgrApi.overTimeHandleNum(roles,1,type_));
        map.put("high",iEventmgrApi.overTimeHandleNum(roles,2,type_));
        map.put("middle",iEventmgrApi.overTimeHandleNum(roles,3,type_));
        map.put("low",iEventmgrApi.overTimeHandleNum(roles,4,type_));
        map.put("overTime",iEventmgrApi.overTimeHandleNum(roles,5,type_));
        returnValue.add(map);
        return toSuccess(returnValue);
    }

    /**
     * 工单管理-我的工单-处理满意度
     */
    @RequestMapping({"handleSatisfiedNum"})
    @ResponseBody
    public JSONObject handleSatisfiedNum(HttpServletRequest request,int type_){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> roles = this.getRoles(request);
        Map<String,Object> map = new HashMap<>();
        map.put("total",iEventmgrApi.getHandleSatisfiedNum(roles,0,type_));
        map.put("five",iEventmgrApi.getHandleSatisfiedNum(roles,5,type_));
        map.put("four",iEventmgrApi.getHandleSatisfiedNum(roles,4,type_));
        map.put("three",iEventmgrApi.getHandleSatisfiedNum(roles,3,type_));
        map.put("two",iEventmgrApi.getHandleSatisfiedNum(roles,2,type_));
        map.put("one",iEventmgrApi.getHandleSatisfiedNum(roles,1,type_));
        returnValue.add(map);
        return toSuccess(returnValue);
    }

    /**
     * 工单管理-我的工单-冻结工单统计
     * @return
     */
    @RequestMapping({"freezeSheet"})
    @ResponseBody
    public JSONObject freezeSheet(HttpServletRequest request,int type_){
        Map<String,Object> roles = this.getRoles(request);
        List<FreezeSheetVo> returnValue = iEventmgrApi.freezeSheetList(roles,type_);
        return toSuccess(returnValue);
    }

    /**
     * 工单管理-我的工单-根据主键查看详情
     */
    @RequestMapping({"getSheetById"})
    @ResponseBody
    public JSONObject getSheetById(HttpServletRequest request,Long eventId){
        ItsmEventmgr eventmgr = iEventmgrApi.findByEventId(eventId);
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
        if(VesApp.getLoginUser(request).getAccount() .equals("admin")){
            eventmgr.setIsAdmin(1);
        }
        Task task = taskService.createTaskQuery().processInstanceId(eventmgr.getProcess_instanceId()).singleResult();
        if(task != null){
            eventmgr.setNodeFlow(task.getName());
            eventmgr.setAssignee(task.getAssignee());
        }
        return toSuccess(eventmgr);
    }

    /**
     * 工单管理-工单统计-今日统计/服务台工作量(今日)
     */
    @RequestMapping({"sheetCount"})
    @ResponseBody
    public JSONObject sheetCount(){
        List<Map<String,Object>> returnValue = new ArrayList<>();

        Map<String,Object> map_1 = new HashMap<>();
        map_1.put("name","未处理工单");
        map_1.put("value",iEventmgrApi.getSheetCount(1));
        returnValue.add(map_1);

        Map<String,Object> map_2 = new HashMap<>();
        map_2.put("name","未分派工单");
        map_2.put("value",iEventmgrApi.getSheetCount(2));
        returnValue.add(map_2);

        Map<String,Object> map_3 = new HashMap<>();
        map_3.put("name","已冻结工单");
        map_3.put("value",iEventmgrApi.getSheetCount(3));
        returnValue.add(map_3);

        Map<String,Object> map_4 = new HashMap<>();
        map_4.put("name","待评价工单");
        map_4.put("value",iEventmgrApi.getSheetCount(4));
        returnValue.add(map_4);

        Map<String,Object> map_5 = new HashMap<>();
        map_5.put("name","已超时工单");
        map_5.put("value",iEventmgrApi.getSheetCount(5));
        returnValue.add(map_5);

        Map<String,Object> map_6 = new HashMap<>();
        map_6.put("name","已完成工单");
        map_6.put("value",iEventmgrApi.getSheetCount(6));
        returnValue.add(map_6);

        return toSuccess(returnValue);
    }

    /**
     * 工单管理-工单统计-提单数统计(本周)
     * source1服务台2自助服务台
     * type1总工单2未处理3未分派4已完成
     */
    @RequestMapping({"sourceCount"})
    @ResponseBody
    public JSONObject sourceCount(){
        List<List<Integer>> subSheetNum = new ArrayList<>();
        List<Integer> autoSheetNum = new ArrayList<>();
        autoSheetNum.add(iEventmgrApi.getSubSheetNum(1,1));
        autoSheetNum.add(iEventmgrApi.getSubSheetNum(1,2));
        autoSheetNum.add(iEventmgrApi.getSubSheetNum(1,3));
        autoSheetNum.add(iEventmgrApi.getSubSheetNum(1,4));
        subSheetNum.add(autoSheetNum);
        List<Integer> workbenchSheetNum = new ArrayList<>();
        workbenchSheetNum.add(iEventmgrApi.getSubSheetNum(6,1));
        workbenchSheetNum.add(iEventmgrApi.getSubSheetNum(6,2));
        workbenchSheetNum.add(iEventmgrApi.getSubSheetNum(6,3));
        workbenchSheetNum.add(iEventmgrApi.getSubSheetNum(6,4));
        subSheetNum.add(workbenchSheetNum);

        return toSuccess(subSheetNum);
    }

    /**
     * 工单管理-工单统计-工程师工作量(最近30天)
     */
    @RequestMapping({"getEngineerAmount"})
    @ResponseBody
    public JSONObject getEngineerAmount(HttpServletRequest request){
        Map<String,List<String>> returnValue = new HashMap<>();
        List<String> nameList = new ArrayList<>();
        List<String> engineerList = iEventmgrApi.getTopEngineerList();
        for(String engineer:engineerList){
            nameList.add(iUserApi.getByAccount(engineer).getName());
        }
        returnValue.put("name",nameList);
        List<String> noHandList = new ArrayList<>();
        List<String> hasHandList = new ArrayList<>();
        List<String> freezeList = new ArrayList<>();
        for(String name:engineerList){
            noHandList.add(iEventmgrApi.getNoHandByName(name)+"");
            hasHandList.add(iEventmgrApi.getHasHandByName(name)+"");
            freezeList.add(iEventmgrApi.getFreezeByName(name)+"");
        }
        returnValue.put("noHand",noHandList);
        returnValue.put("hasHand",hasHandList);
        returnValue.put("freeze",freezeList);
        return toSuccess(returnValue);
    }
    /**
     * 工单统计-满意度排名TOP5
     */
    @RequestMapping({"satisfiedRank"})
    @ResponseBody
    public JSONObject satisfiedRank(){
        Map<String,Float> returnValue = new HashMap<>();
        //1.先获取所有已评价的工程师列表
        List<String> engineerList = iEventmgrApi.getSatisfiedEngineers();
        for(String engineer:engineerList){
            Float score = iEventmgrApi.getSatisfiedScoreByName(engineer);
            returnValue.put(iUserApi.getByAccount(engineer).getName(),score);
        }
        List<Map.Entry<String,Float>> list = new ArrayList<Map.Entry<String,Float>>(returnValue.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return -(o1.getValue().compareTo(o2.getValue()));
            }
        });
        List<Map<String,Object>> list_ = new ArrayList<>();
        for(Map.Entry<String,Float> mapping:list){
            Map<String,Object> map = new HashMap<>();
            map.put("name",mapping.getKey());
            map.put("value",mapping.getValue());
            list_.add(map);
        }
        return toSuccess(list);
    }

    /**
     * 工单统计-提单数统计(本周)
     * @return
     */
    @RequestMapping({"subsheet"})
    @ResponseBody
    public JSONObject subsheet(HttpServletRequest request, int type){//type表示统计分类1当天2本周3本月
        Map<Integer,List<Map<String,Object>>> returnValue = new HashMap<>();
        Map<String,Object> map = this.getRoles(request);
        List<Map<String,Object>> unHandle= iEventmgrApi.getNumberOfSheetType(map,1,type);
        returnValue.put(1,unHandle);
        List<Map<String,Object>> unDesignee= iEventmgrApi.getNumberOfSheetType(map,2,type);
        returnValue.put(2,unDesignee);
        List<Map<String,Object>> completed= iEventmgrApi.getNumberOfSheetType(map,3,type);
        returnValue.put(3,completed);
        return toSuccess(returnValue);
    }

    /**
     * 本周提单数统计(按服务分类统计)
     * @return
     */
    @RequestMapping({"subsheetByCatalogId"})
    @ResponseBody
    public JSONObject subsheetByCatalogId(int type,Long catalogId){//type表示统计分类1当天2本周3本月
        List<Integer> returnValue = new ArrayList<>();
        int unHandle= iEventmgrApi.getNumberByCatalogId(catalogId,type,3);
        returnValue.add(unHandle);
        int unDesignee= iEventmgrApi.getNumberByCatalogId(catalogId,type,2);
        returnValue.add(unDesignee);
        int completed= iEventmgrApi.getNumberByCatalogId(catalogId,type,1);
        returnValue.add(completed);
        return toSuccess(returnValue);
    }

    /**
     * 最近30天提单数统计
     * @return
     */
    @RequestMapping({"engineer"})
    @ResponseBody
    public JSONObject engineer(HttpServletRequest request){
        Map<Integer,List<Map<String,Object>>> returnValue = new HashMap<>();
        Map<String,Object> map = this.getRoles(request);
        List<Map<String,Object>> unHandle= iEventmgrApi.getNumberOfSheetMonth(map,1);
        returnValue.put(1,unHandle);
        List<Map<String,Object>> unDesignee= iEventmgrApi.getNumberOfSheetMonth(map,2);
        returnValue.put(2,unDesignee);
        List<Map<String,Object>> completed= iEventmgrApi.getNumberOfSheetMonth(map,3);
        returnValue.put(3,completed);
        return toSuccess(returnValue);
    }

    /**
     * 获取所有的服务分类
     * @return
     */
    @RequestMapping({"getAllService"})
    @ResponseBody
    public JSONObject getAllService(){
        List<ServiceTreeVo> serviceList = iDirectoryManageApi.treeList();
        return toSuccess(serviceList);
    }

    /**
     * 获取所有的优先级列表
     */
    @RequestMapping({"getPriorityList"})
    @ResponseBody
    public JSONObject getPriorityList(HttpSession httpSession){
        List<ItsmPriority> itsmPriorityList = iPriorityApi.getAll();
        return toSuccess(itsmPriorityList);
    }

    /**
     * 运维支撑能力
     * 生成工单 解决工单
     */
    @RequestMapping({"supportCapacity"})
    @ResponseBody
    public JSONObject supportCapacity(HttpServletRequest request){
        Map<Integer,List<Map<String,Object>>> returnValue = new HashMap<>();
        Map<String,Object> roles = this.getRoles(request);
        ILoginUser loginUser = VesApp.getLoginUser(request);
        returnValue.put(1,iEventmgrApi.getNumberByPriority(roles,1));
        returnValue.put(2,iEventmgrApi.getNumberByPriority(roles,2));
        return toSuccess(returnValue);
    }

    /**
     * 大屏展示值班人员信息
     * @return
     */
    @RequestMapping({"screenshow/info"})
    @ResponseBody
    public JSONObject info(HttpServletRequest request){
        Map<String,Object> returnValue = new HashMap<>();
        //查询当日值班人员信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DutyMainUserVo> userList = iDutyManageApi.getDutyUserByDate(sdf.format(new Date()));
        StringBuffer dutyMan = new StringBuffer();
        if(userList != null && userList.size() >0){
            for(DutyMainUserVo user:userList){
                dutyMan.append(user.getTypeUserName()+" ");
            }

        }
        returnValue.put("name",dutyMan);
        //查询系统所有一线人员信息
        List<UserDomain> firstLineList = iRoleApi.getUserDomainByRoleId(Long.valueOf(424501));
        StringBuffer firstLine = new StringBuffer();
        if(firstLineList != null && firstLineList.size() > 0){
            for(UserDomain userDomain:firstLineList){
                firstLine.append(userDomain.getUserName()+"、");
            }
        }
        returnValue.put("firstLine",firstLine);
        //获取截止到现在提交的未处理的工单数量
        Map<String,Object> map = this.getRoles(request);
        int unHandle = iEventmgrApi.getAllSheetNum(4);
        returnValue.put("unHandle",unHandle);
        //获取截止到现在提交的未分派的工单数量
        int unDispatch = iEventmgrApi.getAllSheetNum(2);
        returnValue.put("unDispatch",unDispatch);
        //获取截止到现在提交的在今日完成的工单数量
        int completed = iEventmgrApi.getTodayCompletedNum();
        returnValue.put("completed",completed);
        return toSuccess(returnValue);
    }

    /**
     * 大屏展示--工单列表
     * @return
     */
    @RequestMapping({"screenshow/list"})
    @ResponseBody
    public JSONObject list(HttpServletRequest request,int pageNumber,int pageSize){
        ILoginUser iLoginUser = VesApp.getLoginUser(request);
        Map<String,Object> map = this.getRoles(request);
        Page<ScreenEventPo,ScreenEventQueryQo> page = new Page();
        ScreenEventQueryQo queryQo = new ScreenEventQueryQo();
        queryQo.setIsAdmin((Integer) map.get("isAdmin"));
        queryQo.setIsServiceManager((Integer)map.get("isServiceManager"));
        queryQo.setIsService((Integer)map.get("isService"));
        queryQo.setId(iLoginUser.getId());
        queryQo.setAccount(iLoginUser.getAccount());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        queryQo.setDate(df.format(new Date()));
        page.setCondition(queryQo);
        int startNumber = (pageNumber - 1)*pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        iEventmgrApi.getEventListToday(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",page.getStartRow());
        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        //更新该值
        request.getSession().setAttribute("bigScreenFlag",false);
        return jsonObject;
    }
    /*
    public JSONObject list(HttpServletRequest request,int pageNumber,int pageSize,int timeout) throws InterruptedException {
        int i = 1;
        while(true){
            if(i > timeout){
                break;
            }
            Thread.sleep(300);
            Object obj = request.getSession().getAttribute("bigScreenFlag");
            if(obj!=null&&(boolean)obj){
                //今天提交的表单
                ILoginUser iLoginUser = VesApp.getLoginUser(request);
                Map<String,Object> map = this.getRoles(request);
                Page<ScreenEventPo,ScreenEventQueryQo> page = new Page();
                ScreenEventQueryQo queryQo = new ScreenEventQueryQo();
                queryQo.setIsAdmin((Integer) map.get("isAdmin"));
                queryQo.setIsServiceManager((Integer)map.get("isServiceManager"));
                queryQo.setIsService((Integer)map.get("isService"));
                queryQo.setId(iLoginUser.getId());
                queryQo.setAccount(iLoginUser.getAccount());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                queryQo.setDate(df.format(new Date()));
                page.setCondition(queryQo);
                int startNumber = (pageNumber - 1)*pageSize;
                page.setStartRow(startNumber);
                page.setRowCount(pageSize);
                iEventmgrApi.getEventListToday(page);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("page",page.getStartRow());
                jsonObject.put("rows",page.getDatas());
                jsonObject.put("total",page.getTotalRecord());
                //更新该值
                request.getSession().setAttribute("bigScreenFlag",false);
                return jsonObject;
            }else{
                Thread.sleep(1000);
                i++;
            }

        }
        return toSuccess(null);

    }*/

    /**
     * 大屏展示-提单数统计(今日)
     * 1自助服务台2系统用户
     * 1紧急2高3中4低
     */
    @RequestMapping({"getSubSheetNumByPriority"})
    @ResponseBody
    public JSONObject getSubSheetNumByPriority(){
      //  Map<String,List<Integer>> returnValue = new HashMap<>();
        Map<String,Object> returnValue = new HashMap<>();
        List<Integer> selfService = new ArrayList<>();
        selfService.add(iEventmgrApi.getSubSheetNumByPriority(6,1));
        selfService.add(iEventmgrApi.getSubSheetNumByPriority(6,2));
        selfService.add(iEventmgrApi.getSubSheetNumByPriority(6,3));
        selfService.add(iEventmgrApi.getSubSheetNumByPriority(6,4));
        List<Integer> workbench = new ArrayList<>();
        workbench.add(iEventmgrApi.getSubSheetNumByPriority(4,1));
        workbench.add(iEventmgrApi.getSubSheetNumByPriority(4,2));
        workbench.add(iEventmgrApi.getSubSheetNumByPriority(4,3));
        workbench.add(iEventmgrApi.getSubSheetNumByPriority(4,4));
        List<ItsmEventmgr> returnListOne=iEventmgrApi.getSubSheetNumByPriorityNoService();
        List<ItsmEventmgr> returnListTwo=iEventmgrApi.getSubSheetNumByPriorityService(6);
       /* returnValue.put("selfHelpDesk",selfService);
        returnValue.put("serviceDesk",workbench);*/
        returnValue.put("selfHelpDesk",returnListOne);
        returnValue.put("serviceDesk",returnListTwo);
        return toSuccess(returnValue);
    }

    /**
     * 大屏展示-处理满意度
     */
    @RequestMapping({"getSatisfiedNumber"})
    @ResponseBody
    public JSONObject getSatisfiedNumber(){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","满意");
        map.put("value",iEventmgrApi.getSatisfiedNumber(1)==null?0:iEventmgrApi.getSatisfiedNumber(1));
        returnValue.add(map);
        Map<String,Object> map_ = new HashMap<>();
        map_.put("name","不满意");
        map_.put("value",iEventmgrApi.getSatisfiedNumber(2)==null?0:iEventmgrApi.getSatisfiedNumber(2));
        returnValue.add(map_);
        return toSuccess(returnValue);
    }

    /**
     * 初始化大屏信息
     */
    @RequestMapping({"screenshow/initFlag"})
    @ResponseBody
    public JSONObject initFlag(HttpServletRequest request){
        request.getSession().setAttribute("bigScreenFlag",true);
        return toSuccess("success");
    }

    /**
     * 大屏展示-工单展示-服务台工作量(30天)
     */
    @RequestMapping({"monthWorkLoad"})
    @ResponseBody
    public JSONObject monthWorkLoad(){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        Map<String,Object> map = new HashMap();
        map.put("name","未处理工单");
        map.put("value",iEventmgrApi.monthWorkLoad(4));//未完成工单
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","未分派工单");
        map.put("value",iEventmgrApi.monthWorkLoad(2));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已冻结工单");
        map.put("value",iEventmgrApi.monthWorkLoad(3));//已冻结工单
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已提交工单");
        map.put("value",iEventmgrApi.monthWorkLoad(0));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已处理工单");
        map.put("value",iEventmgrApi.monthWorkLoad(6));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","待评价工单");
        map.put("value",iEventmgrApi.monthWorkLoad(5));
        returnValue.add(map);
        map = new HashMap<>();
        map.put("name","已完成工单");
        map.put("value",iEventmgrApi.monthWorkLoad(1));
        returnValue.add(map);

        return toSuccess(returnValue);
    }

    /**
     * 大屏展示-工单展示-提单数统计(30天)
     */
    @RequestMapping({"monthSourceSheet"})
    @ResponseBody
    public JSONObject monthSourceSheet(){
        List<List<Integer>> returnValue = new ArrayList<>();
        List<Integer> autoSheetNum = new ArrayList<>();
        autoSheetNum.add(iEventmgrApi.getMonthSubSheetNum(1,1));
        autoSheetNum.add(iEventmgrApi.getMonthSubSheetNum(1,2));
        autoSheetNum.add(iEventmgrApi.getMonthSubSheetNum(1,3));
        autoSheetNum.add(iEventmgrApi.getMonthSubSheetNum(1,4));
        returnValue.add(autoSheetNum);
        List<Integer> workbenchSheetNum = new ArrayList<>();
        workbenchSheetNum.add(iEventmgrApi.getMonthSubSheetNum(6,1));
        workbenchSheetNum.add(iEventmgrApi.getMonthSubSheetNum(6,2));
        workbenchSheetNum.add(iEventmgrApi.getMonthSubSheetNum(6,3));
        workbenchSheetNum.add(iEventmgrApi.getMonthSubSheetNum(6,4));
        returnValue.add(workbenchSheetNum);

        return toSuccess(returnValue);
    }

    /**
     * 大屏展示-工单展示-工单满意度统计(最近30天完成的)
     */
    @RequestMapping({"monthSatisfied"})
    @ResponseBody
    public JSONObject monthSatisfied(){
        List<Integer> returnValue = new ArrayList<>();
        for(int star=1;star<6;star++){
            returnValue.add(iEventmgrApi.monthSatisfied(star));
        }
        return toSuccess(returnValue);
    }




    /**
     * 判断当前登录用户的角色
     */
    public Map<String,Object> getRoles(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        ILoginUser loginUser = VesApp.getLoginUser(request);
        int isAdmin = 0;
        int isServiceManager = 0;
        int isService = 0;
        if(loginUser.getAccount().equals("admin")){
            isAdmin = 1;
        }
        if(loginUser.getAccount().equals("serviceManager")){
            isServiceManager = 1;
        }
        List<IRole> iRoles = VesApp.getLoginUser(request).getRoles();
        if(iRoles != null && iRoles.size() >0) {
            for (IRole role : iRoles) {
                if (role.getId() == 424500) {
                    isService = 1;
                }
            }
        }
        map.put("isAdmin",isAdmin);
        map.put("isServiceManager",isServiceManager);
        map.put("isService",isService);
        map.put("userAccount",loginUser.getAccount());
        map.put("createUserId",loginUser.getId());
        return map;
    }

    /**
     *获取工单评价星级列表
     * type 1最近7天 2最近30天3今日
     */
    @RequestMapping({"satisfied"})
    @ResponseBody
    public JSONObject satisfied(int type){
        List<Map<String,Object>> returnValue = new ArrayList<>();
        List<Map<String,Integer>> satisfied = iEventmgrApi.getSatisfied(type);

        for(int i=0;i<5;i++){
            Map<String,Object> mapValue = new HashMap<>();
            mapValue.put("value",0);
            switch(i){
                case 0:
                    mapValue.put("name","一星");
                    mapValue.put("value",0);
                    for(Map<String,Integer> map:satisfied) {
                        if(map.get("star")==1){
                            mapValue.put("value", map.get("number"));
                        }
                    }
                    break;
                case 1:
                    mapValue.put("name","二星");
                    for(Map<String,Integer> map:satisfied) {
                        if(map.get("star")==2){
                            mapValue.put("value", map.get("number"));
                        }
                    }
                    break;
                case 2:
                    mapValue.put("name","三星");
                    for(Map<String,Integer> map:satisfied) {
                        if(map.get("star")==3){
                            mapValue.put("value", map.get("number"));
                        }
                    }
                    break;
                case 3:
                    mapValue.put("name","四星");
                    for(Map<String,Integer> map:satisfied) {
                        if(map.get("star")==4){
                            mapValue.put("value", map.get("number"));
                        }
                    }
                    break;
                case 4:
                    mapValue.put("name","五星");
                    for(Map<String,Integer> map:satisfied) {
                        if(map.get("star")==5){
                            mapValue.put("value", map.get("number"));
                        }
                    }
                    break;
            }

            returnValue.add(mapValue);
        }


        return toSuccess(returnValue);
    }

    /**
     * 知识贡献排名，取前五名
     */
    @RequestMapping({"knowledgeRank"})
    @ResponseBody
    public JSONObject knowledgeRank(HttpSession httpSession){
        List<Map<String,Object>> knowledgeRankList = iSubKnowledgeApi.getKnowledgeRank();
        return toSuccess(knowledgeRankList);
    }


    public CensusAction(){
        this.iEventmgrApi = WebItsmBeanUtil.getEventmgrService();
        this.iSlaManageApi = WebItsmBeanUtil.getSlaManage();
        this.iDirectoryManageApi = WebItsmBeanUtil.getDirectoryService();
        this.iPriorityApi = WebItsmBeanUtil.getIPriority();
        this.iDutyManageApi = WebItsmBeanUtil.getDutyManageService();
        this.iRoleApi = WebItsmBeanUtil.getRoleApi();
        this.iSubKnowledgeApi = WebItsmBeanUtil.getSubKnowledgeService();
        this.iUserApi = WebItsmBeanUtil.getIUserApi();
    }
}
