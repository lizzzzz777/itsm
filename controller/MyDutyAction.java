package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUserQuery;
import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.api.IDutyMyApi;
import com.vlinksoft.ves.dutymanage.bo.DutyLog;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserQo;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyMy;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2017/11/24.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/myDuty"})
public class MyDutyAction extends BaseAction {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(MyDutyAction.class);
    private IDutyMyApi iDutyMyApi;

    @Resource
    private IDutyManageApi iDutyManageApi;

    @Resource
    private AuditLogApi auditLogApi;

    private final Integer DUTY_ING_STATE = 1;
    private final Integer OFF_DUTY_STATE = 2;

    @RequestMapping({"getDutyRecordByUID"})
    @ResponseBody
    public JSONObject getDutyRecordByUID(long uid,long beginTime,long endTime,int pageNumber,int pageSize){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject jsonObject = new JSONObject();
        Page page = new Page();
        DutyMainUserVo dutyMainUserVo = new DutyMainUserVo();

        dutyMainUserVo.setBigenDate(new Date(beginTime));
        dutyMainUserVo.setEndDate(new Date(endTime));

        dutyMainUserVo.setTypeUserName(uid + "");
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        page.setCondition(dutyMainUserVo);

        iDutyManageApi.getDutyRecordByUID(page);

        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());

        return jsonObject;
    }

    /**
     * 值班人员统计分析
     * */
    @RequestMapping({"getDutyStatistics"})
    @ResponseBody
    public JSONObject getDutyStatistics(long beginTime,long endTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        DutyMonthUserQuery dutyMonthUserQuery = new DutyMonthUserQuery();
        dutyMonthUserQuery.setStartDate(new Date(beginTime));
        dutyMonthUserQuery.setEndDate(new Date(endTime));

        return toSuccess(iDutyManageApi.getDutyStatistics(dutyMonthUserQuery));
    }

    /*
    根据域进行值班人员统计分析
     */
    @RequestMapping({"getDutyStatisticsByDomainId"})
    @ResponseBody
    public JSONObject getDutyStatistics(long beginTime,long endTime,String domainId){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date(beginTime);
        Date endDate = new Date(endTime);
        return toSuccess(iDutyManageApi.getDutyStatistics(startDate,endDate,domainId));
    }

    /*
    *值日日期表中列表 */
    @RequestMapping({"list"})
    @ResponseBody
    public JSONObject list() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            if (i % 3 == 0) {
                map.put("type", 0);   /*0表示未值1表示已值2表示请假*/
            } else if (i % 3 == 1) {
                map.put("type", 1);
            } else {
                map.put("type", 2);
            }
            map.put("start", "2017-11-0" + i);
            list.add(map);
        }
        return toSuccess(list);
    }

    /*
    * 添加我要申请--换班*/
    @RequestMapping({"getShift"})
    @ResponseBody
    public JSONObject getShift(@RequestBody DutyMy obj, HttpSession session, HttpServletRequest request) {
        ILoginUser loginUser = VesApp.getLoginUser(request);//获取登录人信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long dutyTypeId = Long.valueOf(obj.getDutyClass());   //申请换班的值班班次id
        long changeTypeId =Long.valueOf(obj.getShiftClass());   //换班人员班次id
        Date dutyDate = obj.getDutyDate();  //值班日期
        Date shiftDate = obj.getShiftDate();  //换班人员-换班日期
        long dutyMainId = obj.getDutyMainId();
        long shiftMainId = obj.getShiftMainId();
        //根据申请人id、申请换班日期、班次类型查询当前是否有值班
        DutyMainUserVo flogDuty = iDutyMyApi.checkFlogDuty(loginUser.getId(), dutyDate, dutyTypeId);
        //根据换班人id和换班日期，班次类型查看是否有排班
        DutyMainUserVo shiftDuty = iDutyMyApi.checkFlogDuty(Long.valueOf(obj.getShiftPeople()), shiftDate, changeTypeId);
        //查看申请人 换班日期、班次 是否有值班
        DutyMainUserVo flogDutyChange = iDutyMyApi.checkFlogDuty(loginUser.getId(), shiftDate, changeTypeId);
        //查看换班人 申请日期、班次 是否有值班
        DutyMainUserVo shiftDutyChange = iDutyMyApi.checkFlogDuty(Long.valueOf(obj.getShiftPeople()), dutyDate, dutyTypeId);

       /* if (flogDuty==null||shiftDuty == null) {
            return toSuccess("err"); //时间选择有误，当前时间申请人没有值班
        } else if (flogDutyChange != null) {   //值班人在换班日期已有值班
            return toSuccess("changeErr");
        } else if (shiftDutyChange != null) {//换班人在申请日期已有值班
            return toSuccess("changeErr");
        } else if (shiftDuty == null) {
            return toSuccess("peopleErr");   //换班人员没有值班，不能换班
        } else {
            if (flogDuty.getDutyState() == 0) { //正常状态
                obj.setDutyPeopleId(loginUser.getId());
                obj.setDutyMainId(flogDuty.getDutyId());
                obj.setShiftMainId(shiftDuty.getDutyId());
                int shift = this.iDutyMyApi.insertShift(obj);
                if (shift > 0) {
                    //修改数据当前值班人员的dutyState值班状态
                    iDutyMyApi.changeState(flogDuty.getId(), 1);  //换班审批中
                    return toSuccess(shift);
                } else {
                    return toSuccess("InsertErr");   //申请失败
                }
            } else {
                return toSuccess("has");  //当前申请人已经申请不可重复申请
            }
        }*/
        obj.setDutyPeopleId(loginUser.getId()); //换班、请假人id
        obj.setDutyDate(dutyDate);  //调班日期
        obj.setApproveDate(new Date()); //申请日期
        obj.setDutyMainId(dutyMainId);
        obj.setShiftMainId(shiftMainId);
        obj.setDutyClassId(dutyTypeId); //换班，请假班次id
        obj.setShiftClassId(changeTypeId); //换班班次id
        obj.setShiftDate(shiftDate);//值班日期
        obj.setShiftPeopleId(Long.valueOf(obj.getShiftPeople()));
        if (flogDutyChange != null){
            return toSuccess("approveHasDuty"); //申请人在换班班次中有值班
        }else if(shiftDutyChange!=null) {
            return toSuccess("shiftHasDuty"); //换班人在申请人申请的班次和时间内有值班
        }else if (flogDuty.getDutyState() == 0){
            Map<String,Long>map = this.iDutyMyApi.insertShift(obj);
            if (map.get("shiftValue") > 0) {
                //修改数据当前值班人员的dutyState值班状态
                iDutyMyApi.changeState(flogDuty.getId(), 1);  //换班审批中

                //审计日志统计  开始
                AuditLog al=new AuditLog();
//                AuditLAction auditLogAction= AuditLAction.getInstance();
//                AuditLogAction auditLogAction=new AuditLogAction();
                SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdfs.format(new Date());
                al.setUserId(VesApp.getLoginUser(request).getId());
                al.setOperationObjId(map.get("sid")+"");
                al.setFunctionModleId("SVR");
                al.setOperationType("换班");
                al.setOperationContent("我的换班:换班");
                try {
                    al.setOperationTime(sdfs.parse(date));
                }catch (Exception e){
                    e.printStackTrace();
                }
                int i=auditLogApi.insert(al);
                //审计日志统计  结束
                return toSuccess(map.get("shiftValue"));
            } else {
                return toSuccess("InsertErr");   //申请失败
            }
        }else  {
            return toSuccess("hasApprove"); //已经申请过
        }

    }



    /*
    查看值班日志
     */

    @RequestMapping({"getDutyLogBydutyTime"})
    @ResponseBody
    public JSONObject getDutyLogBydutyMainId(String DutyLogdutyTime,HttpServletRequest request) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        ILoginUser loginUser = VesApp.getLoginUser(request);
        List<DutyLog> dutyLogs = iDutyManageApi.getDutyLogBydutyTime(DutyLogdutyTime,loginUser.getId());
        jsonObject.put("code",200);
        jsonObject.put("data",dutyLogs);
        return jsonObject;
    }

    /*查看日志*/
    @RequestMapping("findDutyLog")
    @ResponseBody
    public JSONObject findDutyLog(Long dutyMainId,Long dutyTypeId,Long subDutyUserId){
//        List<DutyLog> dutyLogs = iDutyManageApi.findDutyLog(dutyMainId,subDutyUserId);
        return toSuccess( iDutyManageApi.findDutyLog(dutyMainId,dutyTypeId,subDutyUserId));
    };

    /*查看日志*/
    @RequestMapping("findAdminDutyLog")
    @ResponseBody
    public JSONObject findAdminDutyLog(Long dutyMainId,Long dutyTypeId){
        return toSuccess(iDutyManageApi.findAdminDutyLog(dutyMainId,dutyTypeId));
    };



    /*
    删除值班日志
     */
    @RequestMapping({"delDutyLogById"})
    @ResponseBody
    public JSONObject delDutyLogById(Long id){
        JSONObject jsonObject = new JSONObject();
        try{
            iDutyManageApi.delDutyLogById(id);
            jsonObject.put("code",200);
            jsonObject.put("mes","删除成功");
        }catch (Exception e){
            jsonObject.put("code",199);
            jsonObject.put("mes","删除失败");
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*
    更新值班日志
     */
    @RequestMapping({"updateDutyLog"})
    @ResponseBody
    public JSONObject updateDutyLog(@RequestBody DutyLog dutyLog){
        JSONObject jsonObject = new JSONObject();
        try{
            iDutyManageApi.updateDutyLog(dutyLog);
            jsonObject.put("code",200);
            jsonObject.put("mes",dutyLog.getDutyId());
        }catch (Exception e){
            jsonObject.put("code",199);
            jsonObject.put("mes","更新失败");
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*
    * 添加我要申请--请假*/
    @RequestMapping({"getLevel"})
    @ResponseBody
    public JSONObject getLevel(@RequestBody DutyMy obj, HttpServletRequest request) {
        ILoginUser loginUser = VesApp.getLoginUser(request);
        //1.根据申请人id、请假日期、班次类型查询当前是否有值班
        Date dutyDate = obj.getDutyDate();//请假日期
        long dutyTypeId = obj.getDutyClassId();//班次类型

        DutyMainUserVo flogDuty = iDutyMyApi.checkFlogDuty(loginUser.getId(), dutyDate, dutyTypeId);
        if (flogDuty == null) {
            return toSuccess("err"); //时间选择有误，当前时间申请人没有值班
        } else {
            obj.setDutyMainId(flogDuty.getDutyId());
            if (flogDuty.getDutyState()==0) {//未申请状态
                obj.setDutyPeopleId(loginUser.getId());
                obj.setDutyDate(obj.getDutyDate());
                int shift = this.iDutyMyApi.insertLevel(obj);
                if (shift > 0) {
                    //修改数据当前值班人员的dutyState值班状态
                    iDutyMyApi.changeState(flogDuty.getId(), 4);//请假审批中


                    //审计日志统计  开始
                    AuditLog al=new AuditLog();
//                    AuditLAction auditLogAction= AuditLAction.getInstance();
//                    AuditLogAction auditLogAction=new AuditLogAction();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sdf.format(new Date());
                    al.setUserId(VesApp.getLoginUser(request).getId());
                    al.setOperationObjId(flogDuty.getId()+"");
                    al.setFunctionModleId("SVR");
                    al.setOperationType("请假");
                    al.setOperationContent("我的请假:请假");
                    try {
                        al.setOperationTime(sdf.parse(date));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    int i=auditLogApi.insert(al);
                    //审计日志统计  结束

                    return toSuccess(shift);
                } else {
                    return toSuccess("InsertErr");   //没有申请成功
                }
            } else {
                return toSuccess("has");  //当前申请人已经申请不可重复申请
            }
        }
    }
    @RequestMapping({"getMyDutyPage"})
    @ResponseBody
    public JSONObject getMyDutyPage(int pageNumber,int pageSize,long startTime,long endTime,long domainId,HttpServletRequest request){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ILoginUser loginUser = VesApp.getLoginUser(request);
        Page<DutyMainUserVo, DutyMainUserQo> page = new Page<>();
        int startRow=(pageNumber-1)*pageSize;
        int endRow = startRow + pageSize;
        page.setStartRow(startRow);
        page.setRowCount(endRow);
        DutyMainUserQo dutyMainUserQo = new DutyMainUserQo();
        dutyMainUserQo.setDomainId(domainId);
        dutyMainUserQo.setStartTime(new Date(startTime));
        dutyMainUserQo.setEndTime(new Date(endTime));
        dutyMainUserQo.setUserId(loginUser.getId());
        page.setCondition(dutyMainUserQo);
        iDutyManageApi.getMyDutyPage(page);
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        return jsonObject;
    }

    @RequestMapping({"updateDutyStatus"})
    @ResponseBody
    public JSONObject updateDutyStatus(@RequestBody DutyMainUserVo dutyMainUserVo,HttpServletRequest request){
        ILoginUser loginUser = VesApp.getLoginUser(request);
        Date currentDate = new Date();
        if (dutyMainUserVo.getDutyStatus().equals(OFF_DUTY_STATE)){
            dutyMainUserVo.setOffDutyDate(currentDate);
        }else if (dutyMainUserVo.getDutyStatus().equals(DUTY_ING_STATE)){
            dutyMainUserVo.setOnDutyDate(currentDate);
        }
        iDutyManageApi.updateDutyUserStatus(dutyMainUserVo);
        JSONObject jsonObject= new JSONObject();
        return jsonObject;
    }

    /*
     添加值班日志
  */
    @RequestMapping({"insertDutyLog"})
    @ResponseBody
    public JSONObject insertDutyLog(@RequestBody JSONObject object, HttpServletRequest request) throws ParseException {
        ILoginUser loginUser = VesApp.getLoginUser(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        JSONArray addLogs = object.getJSONArray("dutyLogList");
        for(int i = 0 ;i<addLogs.size();i++){
            JSONObject jsonObject = addLogs.getJSONObject(i);
            DutyLog dutyLog = new DutyLog();
            Long id = jsonObject.getLong("id");
            dutyLog.setDutyLog(jsonObject.getString("dutyLog"));
            dutyLog.setDutyId(jsonObject.getLong("dutyId"));
            dutyLog.setDutyTypeId(jsonObject.getLong("dutyTypeId"));
            dutyLog.setLogStatus(jsonObject.getInteger("logStatus"));
            dutyLog.setSubDutyUserId(loginUser.getId());
            dutyLog.setSubDutyLogTime(sdf.parse(jsonObject.getString("subDutyLogTime")));
            if(id != null && id != 0){
                dutyLog.setId(id);
                iDutyManageApi.updateDutyLog(dutyLog);
            }else
                iDutyManageApi.addDutyLog(dutyLog);
        }
        return toSuccess("ok");
    }

//查询日志
    @RequestMapping({"getLogListByDutyIdAndTypeId"})
    @ResponseBody
    public JSONObject getLogListByDutyIdAndTypeId(long dutyId,long typeId,HttpServletRequest request) throws ParseException {
        ILoginUser loginUser = VesApp.getLoginUser(request);
        return toSuccess(iDutyManageApi.getLogListByDutyIdAndTypeId(dutyId,typeId,loginUser.getId()));
    }




    public MyDutyAction() {
        super();
        iDutyMyApi = WebItsmBeanUtil.getDutyMyService();
    }
}
