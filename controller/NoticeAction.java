package com.vlinksoft.ves.omcenter.controller;
import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.project.controller.ProjectTaskAction;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import com.vlinksoft.ves.workflow.um.noticeManage.api.INoticeManageApi;
import com.vlinksoft.ves.workflow.um.noticeManage.bo.NoticeManageClicks;
import com.vlinksoft.ves.workflow.um.noticeManage.bo.NoticeManageVo;
import com.vlinksoft.ves.workflow.um.noticeManage.bo.NoticeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2018/4/23.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/Notice"})
public class NoticeAction extends BaseAction {
    private INoticeManageApi iNoticeManageApi;
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProjectTaskAction.class);

    @Resource
    private AuditLogApi auditLogApi;


    public void viateTime(){
        List<NoticeManageVo> list = iNoticeManageApi.findNoticeListByState(1);
        Date nowDate = new Date();
        List<Long> ids = new ArrayList<>();
        list.forEach(i->{
            if (nowDate.after(i.getNotice_disEffect_time())) {
                ids.add(i.getNotice_id());
            }
        });
        if(ids.size()>0){
            iNoticeManageApi.updateNoticeStateList(ids);
        }
    }

    //通告列表
    @RequestMapping({"noticeList"})
    @ResponseBody
    public JSONObject noticeList(String state, int pageSize, int pageNumber,String domainId) {
        this.viateTime();
        Page<NoticeManageVo,NoticeVo> page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        NoticeVo noticeVo = new NoticeVo();
        if(StringUtils.isNotBlank(state)){
            if (state.equals("release")){
                noticeVo.setNotice_state(1);
            }else if(state.equals("overTime")){
                noticeVo.setNotice_state(0);
            }else if (state.equals("all")){
                noticeVo.setNotice_state(-1);
            }
        }else{
            noticeVo = null;
        }

        //填充所属域id
        noticeVo.setDomainId(domainId);

        page.setCondition(noticeVo);
        List<NoticeManageVo> noticeManageVoList = iNoticeManageApi.noticeList(page);


//        for (NoticeManageVo noticeManageVo : noticeManageVoList) {
//            if (nowDate.before(noticeManageVo.getNotice_disEffect_time())) {
//                noticeManageVo.setNotice_state(1);
//            }else {
//                noticeManageVo.setNotice_state(0);
//            }
//        }
//        Iterator<NoticeManageVo> iterator=noticeManageVoList.iterator();
//        if(state.equals("release")){
//             while (iterator.hasNext()){
//                 NoticeManageVo vo=iterator.next();
//                 if(vo.getNotice_state()==0){
//                     iterator.remove();
//                 }
//             }
//        }else if(state.equals("overTime")){
//            while (iterator.hasNext()){
//                NoticeManageVo vo=iterator.next();
//                if(vo.getNotice_state()==1){
//                    iterator.remove();
//                }
//            }
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    //搜索
    @RequestMapping({"likeSelectNoticeList"})
    @ResponseBody
    public JSONObject likeSelectNoticeList(String state, int pageSize, int pageNumber) {
        this.viateTime();
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        page.setCondition(state);
        List<NoticeManageVo> noticeManageVoList = iNoticeManageApi.likeSelectNoticeList(page);
        Date nowDate = new Date();
        for (NoticeManageVo noticeManageVo : noticeManageVoList) {
            if (nowDate.before(noticeManageVo.getNotice_disEffect_time())) {
                noticeManageVo.setNotice_state(1);
            }else {
                noticeManageVo.setNotice_state(0);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    //通告列表
    @RequestMapping({"addNotice"})
    @ResponseBody
    public JSONObject addNotice(HttpServletRequest request, HttpSession session) {
        String notice_title=request.getParameter("notice_title");
        String notice_content=request.getParameter("notice_content");
        String len=request.getParameter("notice_effect_length");
        //获取域id
        String domainId=request.getParameter("domainId");

        ILoginUser loginUser = VesApp.getLoginUser(request);
        Date disEffect = getDateAfter(new Date(), Integer.valueOf(len));
        NoticeManageVo noticeManageVo=new NoticeManageVo();
        noticeManageVo.setNotice_title(notice_title);
        noticeManageVo.setNotice_content(notice_content);
        noticeManageVo.setNotice_effect_length(Integer.valueOf(len));
        noticeManageVo.setNotice_disEffect_time(disEffect);
        noticeManageVo.setNotice_release_time(new Date());
        noticeManageVo.setNotice_subManId(loginUser.getId());
        noticeManageVo.setNotice_subManName(loginUser.getName());
        noticeManageVo.setDomainId(domainId);
        Map<String,Long>map=iNoticeManageApi.insertNotice(noticeManageVo);
        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(map.get("nid")+"");
        al.setFunctionModleId("OMC");
        al.setOperationType("新建");
        al.setOperationContent("新建通知:"+noticeManageVo.getNotice_title());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return toSuccess(map.get("i"));
    };

    //根据通告id进行编辑
    @RequestMapping({"editNoticeById"})
    @ResponseBody
    public JSONObject editNoticeById(@RequestBody  NoticeManageVo noticeManageVo){

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(noticeManageVo.getNotice_id()+"");
        al.setFunctionModleId("OMC");
        al.setOperationType("编辑");
        al.setOperationContent("编辑通告:"+noticeManageVo.getNotice_title());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(iNoticeManageApi.updateNoticeStateById(noticeManageVo));
    }
    //根据通告id进行删除
    @RequestMapping({"delNoticeByNoticeId"})
    @ResponseBody
    public JSONObject delNoticeByNoticeId(@RequestBody List<Integer> list){
        logger.info("delNoticeByNoticeId-delNoticeByNoticeId");

        //审计日志统计  开始
        List<NoticeManageVo>nmlist= iNoticeManageApi.selectListByIds(list);
        List<String>names=new ArrayList<>();
        for(int i=0;i<nmlist.size();i++){
            names.add(nmlist.get(i).getNotice_title());
        }
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(list.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("删除");
        al.setOperationContent("删除通告:"+StringUtils.join(names.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        return toSuccess(iNoticeManageApi.delNoticeById(list));
    }
    //根据通告id 查询通告信息

    @RequestMapping({"noticeInfoById"})
    @ResponseBody
    public JSONObject noticeInfoById(int id){
        logger.info("noticeInfoById-id",id);
        iNoticeManageApi.updateClicksById(id);
        NoticeManageClicks noticeManageClicks = iNoticeManageApi.getNoticeClicksByNoticeId(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("noticeInfo",iNoticeManageApi.getNoticeInfoById(id));
        jsonObject.put("clicksInfo",noticeManageClicks);
        return jsonObject;
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }
    public NoticeAction() {
        super();
        iNoticeManageApi = WebItsmBeanUtil.getINoticeService();
    }

    @RequestMapping({"findAllNotice"})
    @ResponseBody
    public JSONObject findAllNotice(String state, int pageSize, int pageNumber) {
        this.viateTime();
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        List<NoticeVo> noticeVoList = iNoticeManageApi.findAllNotice(page);
        Date nowDate = new Date();
        if (CollectionUtils.isNotEmpty(noticeVoList)){
            for (NoticeVo noticeVo : noticeVoList) {
                Long clicks = iNoticeManageApi.getClicks(noticeVo.getNotice_id());
                noticeVo.setClicks(clicks);
                if (nowDate.before(noticeVo.getNotice_disEffect_time())) {
                    noticeVo.setNotice_state(1);
                }else {
                    noticeVo.setNotice_state(0);
                }
            }
            Iterator<NoticeVo> iterator=noticeVoList.iterator();
            if(state.equals("release")){
                while (iterator.hasNext()){
                    NoticeVo vo=iterator.next();
                    if(vo.getNotice_state()==0){
                        iterator.remove();
                    }
                }
            }else if(state.equals("overTime")){
                while (iterator.hasNext()){
                    NoticeVo vo=iterator.next();
                    if(vo.getNotice_state()==1){
                        iterator.remove();
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

}
