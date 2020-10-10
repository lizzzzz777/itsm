package com.vlinksoft.ves.omcenter.controller;



import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.form.api.IVesFormRuntime;
import com.vlinksoft.ves.form.object.ItemCountResult;
import com.vlinksoft.ves.knowledgelogue.api.ISubKnowledgeApi;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.servicecatalog.Vo.DirectoryManageVoQuery;
import com.vlinksoft.ves.servicecatalog.api.IDirectoryManageApi;
import com.vlinksoft.ves.servicecatalog.api.ISlaManageApi;
import com.vlinksoft.ves.servicecatalog.bo.DirectoryManageVo;
import com.vlinksoft.ves.servicecatalog.bo.ServiceTreeVo;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import com.vlinksoft.ves.workflow.um.eventmgr.api.IEventmgrApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2017/11/6.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/directory"})
public class DirectoryManageAction extends BaseAction {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DirectoryManageAction.class);
    private IDirectoryManageApi IdirectoryManageApi;
    private IEventmgrApi iEventmgrApi;
    @Resource
    private IVesFormRuntime formRuntimeApi;
    private ISlaManageApi iSlaManageApi;
    @Resource
    private ISubKnowledgeApi iSubKnowledgeApi;
    @Resource
    private AuditLogApi auditLogApi;

    @RequestMapping({"list"})
    @ResponseBody
    public JSONObject list(String searchText,long catalogId,int pageNumber,int pageSize) {
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DirectoryManageVoQuery condition=new DirectoryManageVoQuery();
        if(searchText == null){
            condition.setServiceName("%%");
        }else {
            condition.setServiceName("%"+searchText+"%");
        }
        ServiceTreeVo catalog = IdirectoryManageApi.getCatalogById(catalogId);
        if(catalog != null && catalog.getpId().equals("0")){
            condition.setCatalogId(0L);
        }else{
            condition.setCatalogId(catalogId);
        }
        page.setCondition(condition);
        IdirectoryManageApi.selectList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
        // return toSuccess(returnList);
    }
    /*
    添加新服务目录
     */
    @RequestMapping({"add"})
    @ResponseBody
    public JSONObject add(@RequestBody DirectoryManageVo bo) {
        bo.setDirectSubTime(new Date());
        logger.info("add11111111111111"+JSONObject.toJSON(bo));
        Map<String,Long>map=IdirectoryManageApi.addDir(bo);

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(map.get("id")+"");
        al.setFunctionModleId("OMC");
        al.setOperationType("新建");
        al.setOperationContent("新建服务:"+bo.getServiceName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return toSuccess(bo);
    }
    /*查询目录下的服务数量*/

    //删除目录
    @RequestMapping({"TreeNumberById"})
    @ResponseBody
    public JSONObject TreeNumberById(long id) {
        int DelNumber=IdirectoryManageApi.ServiceNumberById(id);
        return toSuccess(DelNumber);
    }
    //删除目录
    @RequestMapping({"delTreeById"})
    @ResponseBody
    public JSONObject delTreeById(int id) {
        //审计日志统计  开始
        ServiceTreeVo sv= IdirectoryManageApi.getCatalogById(id);
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(id+"");
        al.setFunctionModleId("OMC");
        al.setOperationType("删除");
        al.setOperationContent("删除目录:"+sv.getName());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束

        int flogDel=IdirectoryManageApi.delServiceById(id);
        return toSuccess(flogDel);
    }
    /*
     * 目录管理点击启用*/
    @RequestMapping({"startUseByIds"})
    @ResponseBody
    public JSONObject startUseByIds(@RequestBody List<String> idsList){
        logger.info("DirectoryManageAction--startUseByIds"+JSONObject.toJSON(idsList));
        StringBuffer ids=new StringBuffer();
        for(int i=0;i<idsList.size();i++){
            ids.append("'").append(idsList.get(i)).append("'").append(",");
        }
        String str=ids.substring(0,ids.length()-1);

        //审计日志统计  开始
        List<String> names= IdirectoryManageApi.selectListName(str);
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(idsList.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("启用");
        al.setOperationContent("启用服务:"+StringUtils.join(names.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return  toSuccess(this.IdirectoryManageApi.startUses(str));
    }


    /*
     * 行内启用*/
    @RequestMapping({"startUseById"})
    @ResponseBody
    public JSONObject startUseById(String id){
        logger.info("DirectoryManageAction--startUseById"+JSONObject.toJSON(id));

        //审计日志统计  开始
        List<String> names= IdirectoryManageApi.selectListName(id);
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
        al.setOperationContent("启用服务:"+StringUtils.join(names.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return  toSuccess(this.IdirectoryManageApi.startUse("'"+id+"'"));
    }



    /*
     * 目录管理点击停用*/
    @RequestMapping({"disUseByIds"})
    @ResponseBody
    public JSONObject disUseByIds(@RequestBody List<String> idsList){
        logger.info("DirectoryManageAction--disUseByIds"+JSONObject.toJSON(idsList));
        StringBuffer ids=new StringBuffer();
        for(int i=0;i<idsList.size();i++){
            int num = IdirectoryManageApi.getUncompletedNumByCID(idsList.get(i));
            if(num > 0){
                return toSuccess("disUseErr");
            }
            ids.append("'").append(idsList.get(i)).append("'").append(",");
        }
        String str=ids.substring(0,ids.length()-1);

        //审计日志统计  开始
        List<String> names= IdirectoryManageApi.selectListName(str);
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(idsList.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("停用");
        al.setOperationContent("停用服务:"+StringUtils.join(names.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return  toSuccess(this.IdirectoryManageApi.disUses(str));
    }

    /*
     * 行内停用*/
    @RequestMapping({"disUseById"})
    @ResponseBody
    public JSONObject disUseById(String id){
        logger.info("DirectoryManageAction--disUseById"+JSONObject.toJSON(id));
        int num = IdirectoryManageApi.getUncompletedNumByCID(id);
        if(num > 0){
            return toSuccess("disUseErr");
        }

        //审计日志统计  开始
        List<String> names= IdirectoryManageApi.selectListName(id);
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
        al.setOperationContent("停用服务:"+StringUtils.join(names.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return  toSuccess(this.IdirectoryManageApi.disUse("'"+id+"'"));
    }



    @RequestMapping({"getEventByIds"})
    @ResponseBody
    public JSONObject getEventByIds(@RequestBody List<Integer> list){
        return toSuccess(iEventmgrApi.getEventsByServiceIds(list));
    }

    //
    @RequestMapping({"checkWorkRelationService"})
    @ResponseBody
    public JSONObject checkWorkRelationService(@RequestBody List<Integer> list){
        int workData = 0;
        List<ItemCountResult> itsmResults = formRuntimeApi.countVesFormByItem("serviceCatalogId");
        if(CollectionUtils.isNotEmpty(itsmResults)){
            workData = itsmResults.stream().filter(e -> list.contains(Integer.valueOf(e.getItemValue()))).mapToInt(ItemCountResult::getTotal).sum();
        }
        Long konwData = iSubKnowledgeApi.getKnowRelationService(list);
        JSONObject json = new JSONObject();
        json.put("workData",workData);
        json.put("knowData",konwData);
        return json;
    }

    /*
     * 目录管理菜单点击删除*/
    @RequestMapping({"delByIds"})
    @ResponseBody
    public JSONObject delByIds(@RequestBody List<String> idsList) {
        logger.info("delByIds---delByIds" + JSONObject.toJSON(idsList));
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < idsList.size(); i++) {
            ids.append(idsList.get(i)).append(",");
            String delIds = ids.substring(0, ids.length() - 1);
            String[] strings = delIds.split(",");
            for (int j=0;j<strings.length;j++){
                String delId=strings[j];
                //获取该目录下的SLA，如果有，则不允许删除
                int slaSum = iSlaManageApi.getSlaByServiceId(delId);
                if (slaSum > 0) {
                    return toSuccess("error");
                }
            }
        }
        String str = ids.substring(0, ids.length() - 1);

        //审计日志统计  开始
        List<String> names = IdirectoryManageApi.selectListName(str);
        AuditLog al = new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req = getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(idsList.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("删除");
        al.setOperationContent("删除服务:" + StringUtils.join(names.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = auditLogApi.insert(al);
        //审计日志统计  结束
        return toSuccess(this.IdirectoryManageApi.delDirs(str));
    }


    /**
     * 如果该目录下有工单信息，则不允许删除
     * @param id
     * @return
     */
    @RequestMapping({"getEventById"})
    @ResponseBody
    public JSONObject getEventById(String id){
        return toSuccess(iEventmgrApi.getEventsByServiceId(id));
    }

    /*
        如果该目录下有工单信息，则不允许删除
     */
    @RequestMapping({"checkServiceId"})
    @ResponseBody
    public JSONObject checkServiceId(String id){
        JSONObject json = new JSONObject();
        int workData = 0;
        List<ItemCountResult> itsmResults = formRuntimeApi.countVesFormByItem("serviceCatalogId");
        if(CollectionUtils.isNotEmpty(itsmResults)){
            workData = itsmResults.stream().filter(e -> (e.getItemValue().equals(id))).mapToInt(ItemCountResult::getTotal).sum();
        }
        Long knowleData = iSubKnowledgeApi.checkHasServiceId(id);
        json.put("workData",workData);
        json.put("knowleData",knowleData);
        return json;

    }

    /*
     * 目录管理菜单点击删除*/
    @RequestMapping({"delById"})
    @ResponseBody
    public JSONObject delById(String id){
        logger.info("delByIds---delById"+JSONObject.toJSON(id));
        //获取该目录下的SLA，如果有，则不允许删除
        int slaSum = iSlaManageApi.getSlaByServiceId(id);
        if(slaSum > 0){
            return toSuccess("error");
        }else {

            //审计日志统计  开始
            List<String> names= IdirectoryManageApi.selectListName(id);
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(id);
            al.setFunctionModleId("OMC");
            al.setOperationType("删除");
            al.setOperationContent("删除服务:"+StringUtils.join(names.toArray(), ","));
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束
            return toSuccess(this.IdirectoryManageApi.delDir("'"+id+"'"));
        }

    }
    /*
     停用服務查詢
     */
    @RequestMapping({"disableList"})
    @ResponseBody
    public JSONObject disableList(String searchText,int pageNumber,int pageSize) {
        logger.info("DirectoryManageAction--disableList");
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        DirectoryManageVoQuery no=new DirectoryManageVoQuery();
        if(searchText == null){
            no.setServiceName("%%");
        }else {
            no.setServiceName("%"+searchText+"%");
        }
        page.setCondition(no);
        List<DirectoryManageVo> returnList = IdirectoryManageApi.disableList(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
        //return toSuccess(returnList);
    }

    /*/
     文件树结构
     */
    public List<ServiceTreeVo> createTreeData(List<ServiceTreeVo> list){

        List<ServiceTreeVo> _list = new ArrayList<ServiceTreeVo>();
        for(ServiceTreeVo vo2 : list){
            if(vo2.getId().toString()==vo2.getpId().toString()){
                _list.add(vo2);
                vo2.setChildren(findChild(vo2,list));
            }
        };

        return  _list;
    };
    public static  List<ServiceTreeVo> findChild(ServiceTreeVo _vo, List<ServiceTreeVo> list ){
        List<ServiceTreeVo> childList = new ArrayList<ServiceTreeVo>();
        for(ServiceTreeVo vo1:list){
            if(_vo.getId()==vo1.getId()){
                continue;
            }
            if(_vo.getId().toString()==vo1.getpId().toString()){
                childList.add(vo1);
                vo1.setChildren(findChild(vo1,list));
            }
        }
        return  childList;
    };





    /* 获取SLA服务目录文件树*/
    @RequestMapping({"serviceSLATreeList"})
    @ResponseBody
    public JSONObject serviceSLATreeList() {
        return toSuccess(IdirectoryManageApi.selectServiceList());
    }


    /*
     * 生成文件树*/
    @RequestMapping({"serviceTreeList"})
    @ResponseBody
    public JSONObject serviceTreeList() {
        List<ServiceTreeVo> returnList=IdirectoryManageApi.treeList_();
        for (ServiceTreeVo tree:returnList){
            if(tree.getpId().equals("0")){
                tree.setOpen(true);
            }
        }
        returnList.get(0).setOpen(true);
        return toSuccess(returnList);
    }
    /*增加文件树节点*/
    @RequestMapping({"addTreeNode"})
    @ResponseBody
    public JSONObject addTreeNode(String name,String pid) {
        logger.info("name"+name);
        logger.info("id111111"+pid);
        //根据名称，检查是否有重复名称
        int flogName=IdirectoryManageApi.checkFlogTreeName(name);
        if(flogName>0){
            return  toSuccess("hasName");
        }else {
            ServiceTreeVo bo=new ServiceTreeVo();
            bo.setName(name);
            bo.setpId(pid);
            bo.setOpen(false);
            logger.info("添加家电"+JSONObject.toJSON(bo));
            this.IdirectoryManageApi.addTreeNode(bo);

            //审计日志统计  开始
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(bo.getId()+"");
            al.setFunctionModleId("OMC");
            al.setOperationType("新建");
            al.setOperationContent("新建目录:"+name);
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束
            return toSuccess(bo.getId());
        }
    }
    /*修改文件树节点*/
    @RequestMapping({"editTreeNode"})
    @ResponseBody
    public JSONObject editTreeNode(String name,String id) {
        logger.info("sdf"+name);
        ServiceTreeVo bo=new ServiceTreeVo();
        bo.setName(name);
        bo.setpId(id);
        //根据名称，检查是否有重复名称
        int flogName=IdirectoryManageApi.checkFlogTreeName(name);
        if(flogName>0){
            return  toSuccess("hasName");
        }else {
            this.IdirectoryManageApi.editTreeNode(bo);

            //审计日志统计  开始
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            HttpServletRequest req=getHttpServletRequest();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(VesApp.getLoginUser(req).getId());
            al.setOperationObjId(id);
            al.setFunctionModleId("OMC");
            al.setOperationType("编辑");
            al.setOperationContent("编辑目录:"+name);
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
            //审计日志统计  结束
            return toSuccess(bo);
        }

    }

    /*
    根据选中的文件树id查找相关服务serviceList
     */
    @RequestMapping({"serviceList"})
    @ResponseBody
    public JSONObject serviceList(String id) {

        return toSuccess(this.IdirectoryManageApi.serviceList(Long.valueOf(id)));
    }

    public DirectoryManageAction(){
        super();
        IdirectoryManageApi = WebItsmBeanUtil.getDirectoryService();
        iEventmgrApi = WebItsmBeanUtil.getEventmgrService();
        iSlaManageApi = WebItsmBeanUtil.getSlaManage();
    }
}
