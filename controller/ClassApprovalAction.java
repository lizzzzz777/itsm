package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUser;
import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUserQuery;
import com.vlinksoft.ves.dutymanage.Vo.DutyMyQuery;
import com.vlinksoft.ves.dutymanage.api.IDutyApproveApi;
import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.api.IDutyMyApi;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.system.um.user.api.IUserApi;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by admin on 2017/11/6.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/classApprove"})
public class ClassApprovalAction extends BaseAction {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(ClassApprovalAction.class);
    private IDutyApproveApi iDutyApproveApi;
    private IDutyMyApi iDutyMyApi;
    private IDutyManageApi iDutyManageApi;
    private IUserApi iUserApi;

    @Resource
    private AuditLogApi auditLogApi;

    @RequestMapping({"list"})
    @ResponseBody
    public JSONObject list(int pageNumber,int pageSize,String searchText) {
        Page page = new Page();
        int startNumber = (pageNumber-1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DutyMyQuery dutyMy = new DutyMyQuery();
        if (searchText == null) {
            dutyMy.setDutyPeople("%%");
        } else {
            dutyMy.setDutyPeople("%" + searchText + "%");
        }
        page.setCondition(dutyMy);
        this.iDutyApproveApi.list(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    @RequestMapping({"leaveList"})
    @ResponseBody
    public JSONObject leaveList(int pageNumber,int pageSize,String searchText) {
        Page page = new Page();
        int startNumber = (pageNumber-1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DutyMyQuery dutyMy = new DutyMyQuery();
        if (searchText == null) {
            dutyMy.setDutyPeople("%%");
        } else {
            dutyMy.setDutyPeople("%" + searchText + "%");
        }
        page.setCondition(dutyMy);
        this.iDutyApproveApi.leaveList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }
    @RequestMapping({"exchangeList"})
    @ResponseBody
    public JSONObject exchangeList(int pageNumber,int pageSize,String searchText) {
        Page page = new Page();
        int startNumber = (pageNumber-1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DutyMyQuery dutyMy = new DutyMyQuery();
        if (searchText == null) {
            dutyMy.setDutyPeople("%%");
        } else {
            dutyMy.setDutyPeople("%" + searchText + "%");
        }
        page.setCondition(dutyMy);
        this.iDutyApproveApi.exchangeList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    @RequestMapping({"leftList"})
    @ResponseBody
    public JSONObject leftList(int pageNumber,int pageSize,String searchText) {
        Page page = new Page();
        int startNumber = (pageNumber-1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DutyMyQuery dutyMy = new DutyMyQuery();
        if (searchText == null) {
            dutyMy.setDutyPeople("%%");
        } else {
            dutyMy.setDutyPeople("%" + searchText + "%");
        }
        page.setCondition(dutyMy);
        this.iDutyApproveApi.leftList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    @RequestMapping({"exchangedList"})
    @ResponseBody
    public JSONObject exchangedList(int pageNumber,int pageSize,String searchText) {
        Page page = new Page();
        int startNumber = (pageNumber-1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DutyMyQuery dutyMy = new DutyMyQuery();
        if (searchText == null) {
            dutyMy.setDutyPeople("%%");
        } else {
            dutyMy.setDutyPeople("%" + searchText + "%");
        }
        page.setCondition(dutyMy);
        this.iDutyApproveApi.exchangedList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }




    @RequestMapping({"myLeaveApply"})
    @ResponseBody
    public JSONObject myLeaveApplyList(int pageNumber, int pageSize, String dutyTime, HttpServletRequest request) {
        Page page = new Page();
        int startNumber = (pageNumber - 1) *pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        DutyMyQuery dutyMy = new DutyMyQuery();
        if (dutyTime != null && dutyTime != "" && !dutyTime.equals("undefined")) {
            dutyMy.setDutyDate(Date.valueOf(dutyTime));
        }
        dutyMy.setDutyPeopleId(VesApp.getLoginUser(request).getId());
        page.setCondition(dutyMy);
        this.iDutyApproveApi.myLeaveApplyList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    @RequestMapping({"myExchangeApply"})
    @ResponseBody
    public JSONObject myExchangeApplyList(int pageNumber, int pageSize, String dutyTime, HttpServletRequest request) {
        Page page = new Page();
        int startNumber = (pageNumber - 1) *pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        DutyMyQuery dutyMy = new DutyMyQuery();
        if (dutyTime != null && dutyTime != "" && !dutyTime.equals("undefined")) {
            dutyMy.setDutyDate(Date.valueOf(dutyTime));
        }
        dutyMy.setDutyPeopleId(VesApp.getLoginUser(request).getId());
        page.setCondition(dutyMy);
        this.iDutyApproveApi.myExchangeApplyList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }


    /*
    换班申请通过
     */
    @RequestMapping({"approveShift"})
    @ResponseBody
    public JSONObject approveShift(@RequestBody DutyMy obj) {
        int flog = this.iDutyApproveApi.passApproveShift(obj);
        DutyMy dutyMyApprove = iDutyApproveApi.findApproveById(obj.getId());
        String dutyUserName = iUserApi.get(dutyMyApprove.getDutyPeopleId()).getName();
        String shiftUserName = iUserApi.get(dutyMyApprove.getShiftPeopleId()).getName();
        if (flog > 0) {
            //修改值班人员状态
            iDutyApproveApi.changeDutyUserState(dutyMyApprove.getDutyPeopleId(), dutyMyApprove.getDutyMainId(),dutyMyApprove.getDutyClassId(), 0); //修改值班人员信息表
            //修改值班人员信息
            iDutyApproveApi.changeDutyUser(dutyMyApprove.getDutyMainId(),dutyMyApprove.getDutyClassId(),
                    dutyMyApprove.getDutyPeopleId(),dutyMyApprove.getShiftPeopleId(),shiftUserName);
            //修改换班人员信息
            iDutyApproveApi.changeDutyUser(dutyMyApprove.getShiftMainId(),dutyMyApprove.getShiftClassId(),
                    dutyMyApprove.getShiftPeopleId(),dutyMyApprove.getDutyPeopleId(),dutyUserName);

            //审计日志统计  开始
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new java.util.Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(obj.getId()+"");
            al.setFunctionModleId("OMC");
            al.setOperationType("换班审核");
            al.setOperationContent("换班审核:同意");
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束
            return toSuccess("success");

        } else {
            return toSuccess("error");
        }

    }

    /*
    换班申请不通过
     */
    @RequestMapping({"approveNoShift"})
    @ResponseBody
    public JSONObject approveNoShift(@RequestBody DutyMy obj) {
        int flog = this.iDutyApproveApi.passApproveNoShift(obj);
        DutyMy dutyMyApprove = iDutyApproveApi.findApproveById(obj.getId());
        if (flog > 0) {
            //修改值班人员状态
            iDutyApproveApi.changeDutyUserState(dutyMyApprove.getDutyPeopleId(),dutyMyApprove.getDutyMainId(),dutyMyApprove.getDutyClassId(),0);

            //审计日志统计  开始
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new java.util.Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(obj.getId()+"");
            al.setFunctionModleId("OMC");
            al.setOperationType("换班审核");
            al.setOperationContent("换班审核:不同意");
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束

            return toSuccess("success");
        } else {
            return toSuccess("error");
        }

    }

    /*
    请假同意
     */
    @RequestMapping({"approveLeave"})
    @ResponseBody
    public JSONObject approveLeave(@RequestBody DutyMy obj) {

        //首先判断顶班人员在该班次已有值班,顶班人 申请日期、班次 是否有值班
        DutyMainUserVo shiftDutyChange = iDutyMyApi.checkFlogDuty(obj.getShiftPeopleId(),obj.getDutyDate(),obj.getDutyClassId());
        if(shiftDutyChange != null){
            return toSuccess("has");
        }
        int flog = this.iDutyApproveApi.passApproveLevel(obj);
        DutyMy dutyMyApprove = iDutyApproveApi.findApproveById(obj.getId());
        String dutyUserName = iUserApi.get(dutyMyApprove.getDutyPeopleId()).getName();
        String shiftUserName = iUserApi.get(dutyMyApprove.getShiftPeopleId()).getName();
        if (flog > 0) {
            //修改请假人员状态
            iDutyApproveApi.changeDutyUserState(dutyMyApprove.getDutyPeopleId(), dutyMyApprove.getDutyMainId(),dutyMyApprove.getDutyClassId(), 0); //修改值班人员信息表
            //修改顶班人员信息
            iDutyApproveApi.changeDutyUser(dutyMyApprove.getDutyMainId(),dutyMyApprove.getDutyClassId(),
                    dutyMyApprove.getDutyPeopleId(),dutyMyApprove.getShiftPeopleId(),shiftUserName);
            //修改换班人员信息
            iDutyApproveApi.changeDutyUser(dutyMyApprove.getShiftMainId(),dutyMyApprove.getShiftClassId(),
                    dutyMyApprove.getShiftPeopleId(),dutyMyApprove.getDutyPeopleId(),dutyUserName);

            //审计日志统计  开始
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new java.util.Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(obj.getId()+"");
            al.setFunctionModleId("OMC");
            al.setOperationType("请假审核");
            al.setOperationContent("请假审核:同意");
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束
            return toSuccess("success");
        } else {
            return toSuccess("error");
        }
    }

    /*
   请假不同意
    */
    @RequestMapping({"approveNoLeave"})
    @ResponseBody
    public JSONObject approveNoLeave(@RequestBody DutyMy obj) {
        int flog = this.iDutyApproveApi.passApproveNoLevel(obj);
        if (flog > 0) {
            DutyMy dutyMyApprove = iDutyApproveApi.findApproveById(obj.getId());
            iDutyApproveApi.changeDutyUserState(dutyMyApprove.getDutyPeopleId(),dutyMyApprove.getDutyMainId(),dutyMyApprove.getDutyClassId(),0);

            //审计日志统计  开始
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new java.util.Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(obj.getId()+"");
            al.setFunctionModleId("OMC");
            al.setOperationType("请假审核");
            al.setOperationContent("请假审核:不同意");
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束
            return toSuccess("success");
        } else {
            return toSuccess("error");
        }
    }

    /**
     *本月值班人员详情
     */
    @RequestMapping({"allUserDutyInfo"})
    @ResponseBody
    public JSONObject allUserDutyInfo(int pageNumber,int pageSize){
        Page<DutyMonthUser,DutyMonthUserQuery> page = new Page<>();
        page.setStartRow((pageNumber - 1)*pageSize);
        page.setRowCount(pageSize);
        SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-01");
        SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-31");
        DutyMonthUserQuery condition = new DutyMonthUserQuery();
       /* condition.setStartDate(new java.util.Date());
        condition.setEndDate(new java.util.Date());*/
        page.setCondition(condition);
        List<DutyMonthUser> dutyMonthUserList = iDutyManageApi.getUserDutyInfoList(page);
        if(dutyMonthUserList != null && dutyMonthUserList.size() > 0){
            for(DutyMonthUser dutyMonthUser:dutyMonthUserList){
                //1.请假
                dutyMonthUser.setLeaveSum(iDutyManageApi.getTypeNumByUserId(dutyMonthUser.getUserId(),1));
                //2.换班
                dutyMonthUser.setChangeSum(iDutyManageApi.getTypeNumByUserId(dutyMonthUser.getUserId(),2));
                //3.已值
                dutyMonthUser.setHasDuty(iDutyManageApi.getHasDutyMonth(dutyMonthUser.getUserId(),1));
                //4.未值
                dutyMonthUser.setUnDuty(iDutyManageApi.getHasDutyMonth(dutyMonthUser.getUserId(),2));
            }
        }
        page.setDatas(dutyMonthUserList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        jsonObject.put("page",page.getStartRow());
        return jsonObject;
    }

    public ClassApprovalAction() {
        super();
        iDutyApproveApi = WebItsmBeanUtil.getDutyApproveService();
        iDutyMyApi = WebItsmBeanUtil.getDutyMyService();
        iDutyManageApi=WebItsmBeanUtil.getDutyManageService();
        iUserApi = WebItsmBeanUtil.getIUserApi();
    }
}
