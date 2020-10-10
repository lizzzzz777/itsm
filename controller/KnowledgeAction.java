package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.knowledgelogue.Vo.KnowledgeMouldVo;
import com.vlinksoft.ves.knowledgelogue.Vo.KnowledgeQuery;
import com.vlinksoft.ves.knowledgelogue.Vo.KnowledgeTreeVo;
import com.vlinksoft.ves.knowledgelogue.api.ISubKnowTypeApi;
import com.vlinksoft.ves.knowledgelogue.api.ISubKnowledgeApi;
import com.vlinksoft.ves.knowledgelogue.bo.*;
import com.vlinksoft.ves.platform.file.bean.FileGroupEnum;
import com.vlinksoft.ves.platform.file.service.IFileClientApi;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.util.WebItsmBeanUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.olap4j.impl.ArrayMap;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2018/1/8.
 */
@Controller
@RequestMapping({"/vesapp/omCenter/subKnowledge"})
public class KnowledgeAction extends BaseAction {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(KnowledgeAction.class);
    private ISubKnowledgeApi iSubKnowledgeApi;
    private static final String currentLoginPath = "/static/dist/img/documentKnowledge/";
    private com.vlinksoft.ves.platform.file.service.IFileClientApi fileClient;

    @Resource
    private AuditLogApi auditLogApi;
    @Resource
    private IFileClientApi iFileClient;
    @Resource
    private ISubKnowTypeApi subKnowTypeApi;
    /*
    提交知识
     */
    @RequestMapping({"subKnowledge"})
    @ResponseBody
    public JSONObject subKnowledge(HttpServletRequest request,@RequestParam("info") String info, HttpSession session) {
        ILoginUser loginUser = VesApp.getLoginUser(request);
        Knowledge knowledge=new Knowledge();
        knowledge.setCreateMan(loginUser.getId());
        knowledge.setCreateTime(new Date());
        JSONObject obj=JSONObject.parseObject(info);
        String title = obj.getString("knowledgeTitle");
        List<Knowledge> knowledgeList = iSubKnowledgeApi.getKnowledgeByTitle(title);
        if(knowledgeList.size()>0){
            return toSuccess("titleErr");
        }
        knowledge.setKnowledgeTitle(title);
        String[] keyWords = obj.getString("knowledgeKey").split("，");
        StringBuffer sb = new StringBuffer();
        sb.append("，");
        for(String s:keyWords){
            sb.append(s);
            sb.append("，");
        }
        knowledge.setKnowledgeKey(sb.toString());
        knowledge.setUseMember(obj.getInteger("useMember"));
        knowledge.setKnowledgeType(obj.getInteger("knowledgeType"));
        knowledge.setNarrative((obj.getString("narrative")).replaceAll("&nbsp;"," "));
        knowledge.setKnowledgeSource(obj.getInteger("knowledgeSource"));
        knowledge.setKnowledgeState(obj.getInteger("knowledgeState"));
        knowledge.setMethodsStep(obj.getString("methodsStep").replaceAll("&nbsp;"," "));
        knowledge.setRequestType(obj.getString("requestType"));
        long knowledgeId=this.iSubKnowledgeApi.subKnowledge(knowledge);
        MultipartHttpServletRequest files = (MultipartHttpServletRequest) request;
        List<MultipartFile> fileList = files.getFiles("files");
        this.insertDocument(fileList,knowledgeId,request,loginUser);

        //审计日志统计  开始
        if(knowledge.getKnowledgeState()==3){
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(loginUser.getId());
            al.setOperationObjId(knowledgeId+"");
            al.setFunctionModleId("SVR");
            al.setOperationType("新建");
            al.setOperationContent("新建知识:"+knowledge.getKnowledgeTitle());
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
        }
        if(knowledge.getKnowledgeState()==4){
            AuditLog al=new AuditLog();
//            AuditLAction auditLogAction= AuditLAction.getInstance();
//            AuditLogAction auditLogAction=new AuditLogAction();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            al.setUserId(loginUser.getId());
            al.setOperationObjId(knowledgeId+"");
            al.setFunctionModleId("SVR");
            al.setOperationType("保存");
            al.setOperationContent("保存知识:"+knowledge.getKnowledgeTitle());
            try {
                al.setOperationTime(sdf.parse(date));
            }catch (Exception e){
                e.printStackTrace();
            }
            int i=auditLogApi.insert(al);
        }
        //审计日志统计  结束
        return toSuccess(knowledgeId);
    }



    public int insertDocument(List<MultipartFile> fileList,long knowledgeId,HttpServletRequest request,ILoginUser loginUser){
        int m = 0;
        if (fileList.size() > 0) {
            MultipartFile[] fileImgArr = new MultipartFile[fileList.size()];
            for (int i = 0; i < fileList.size(); i++) {
                fileImgArr[i] = fileList.get(i);
            }
            for (int i = 0; i < fileImgArr.length; i++) {
                try {
                    CommonsMultipartFile file = (CommonsMultipartFile) fileImgArr[i];
                    if ((file != null) && (file.getSize() > 0L)) {
                        KnowledgeDocument annex = new KnowledgeDocument();
                        String fileName = file.getFileItem().getName();
                        long fileId = this.fileClient.upLoadFile(FileGroupEnum.VES_SYSTEM, file).longValue();
                        boolean fileImg = true;
                        fileImg = this.fileClient.isExist(fileId);
                        if (fileImg) {
                            File file2 = fileClient.getSystemFileByID(fileId, request.getSession().getServletContext().getRealPath("/") + currentLoginPath);
                            annex.setDocumentPath(currentLoginPath + "/" + file2.getName());
                        }
                        annex.setDocumentId(fileId);
                        annex.setDocumentName(fileName);
                        annex.setUploadPerson(loginUser.getName());
                        annex.setUploadTime(new Date());
                        annex.setKnowledgeId(knowledgeId);
                        m = this.iSubKnowledgeApi.insertDocument(annex);
                    }

                } catch (Exception e) {
                    this.logger.error("imgInsert:" + e.getMessage(), e);
                }
            }
        }
        return m;
    }

    //我提交的知识列表
    @RequestMapping({"subKnowledgeList"})
    @ResponseBody
    public JSONObject subKnowledgeList(String type, HttpServletRequest request, int pageSize, int pageNumber){
        ILoginUser loginUser=VesApp.getLoginUser(request);
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);

        KnowledgeQuery knowledge=new KnowledgeQuery();
        if(type.equals("all")){
            knowledge.setKnowledgeState(0);
        }else {
            knowledge.setKnowledgeState(Integer.valueOf(type));
        }
        knowledge.setCreateMan(loginUser.getId());
        page.setCondition(knowledge);
        iSubKnowledgeApi.knowledgeList(page);
        List<Knowledge> knowledgeList = page.getDatas();
        for(Knowledge k:knowledgeList){
            k.setAttachmentList(iSubKnowledgeApi.getDocumentListByKID(k.getId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }
    //获得列表中不同状态下知识的数量
    @RequestMapping({"getNumberByState"})
    @ResponseBody
    public JSONObject getNumberByState(HttpSession session,HttpServletRequest request){
        ILoginUser loginUser=VesApp.getLoginUser(request);
        long createUserId = loginUser.getId();
        Map<String,Object> map=new ArrayMap<>();
        map.put("all",this.iSubKnowledgeApi.getKnowledgeNumber(createUserId));
        map.put("posted",this.iSubKnowledgeApi.getPostedKnowledgeNum(createUserId));
        map.put("noExamine",this.iSubKnowledgeApi.getNoExamineKnowledgeNum(createUserId));
        map.put("inDraft",this.iSubKnowledgeApi.getInDraftKnowledgeNum(createUserId));
        map.put("noPass",this.iSubKnowledgeApi.getNoPassKnowledgeNum(createUserId));
        map.put("disable",this.iSubKnowledgeApi.getDisableKnowledgeNum(createUserId));
        return toSuccess(map);
    }
    //我的收藏
    @RequestMapping({"myCollect"})
    @ResponseBody
    public JSONObject myCollect(String searchText, HttpSession session, int pageSize, int pageNumber, HttpServletRequest request){
        ILoginUser loginUser=VesApp.getLoginUser(request);
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        KnowledgeQuery condition=new KnowledgeQuery();
        if(searchText==null){
            condition.setUnfinished("%%");
        }else {
            condition.setUnfinished("%"+searchText+"%");
        }
        condition.setCollectUserId(loginUser.getId());
        page.setCondition(condition);
        this.iSubKnowledgeApi.myCollect(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
        //return toSuccess(returnList);
    }

    //取消收藏
    @RequestMapping({"cancelCollectById"})
    @ResponseBody
    public JSONObject cancelCollectById(@RequestBody List<Long> id,HttpServletRequest request,HttpSession session) {
        ILoginUser iLoginUser = VesApp.getLoginUser(request);
        iSubKnowledgeApi.removeCollectName(iLoginUser.getId(),id);
        return toSuccess(id);
    }
    //我的知识--我提交的知识-删除
    @RequestMapping({"delMyKnowledgeById"})
    @ResponseBody
    public int delMyKnowledgeById(@RequestBody List<String> listId){
        logger.info("delMyKnowledgeById"+JSONObject.toJSON(listId));
        StringBuffer ids=new StringBuffer();
        for(int i=0;i<listId.size();i++){
            ids.append("'").append(listId.get(i)).append("'").append(",");
        }
        String str=ids.substring(0,ids.length()-1);
        //1.删除知识收藏关联关系
        iSubKnowledgeApi.delMyDocKnow(str);
        //2.删除知识附件关联关系
        iSubKnowledgeApi.delMyCollectKnow(str);

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        List<Knowledge> kds=iSubKnowledgeApi.selectAll(str);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(listId.toArray(), ","));
        al.setFunctionModleId("SVR");
        al.setOperationType("删除");
        StringBuffer stri=new StringBuffer();
        stri.append("删除知识:");
        for(Knowledge kd:kds){
            stri.append(kd.getKnowledgeTitle()).append(",");
        }
        String strin=stri.substring(0,stri.length()-1);
        al.setOperationContent(strin);
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        //3.删除知识
        int returnValue = this.iSubKnowledgeApi.delMyKnowledge(str);
        return returnValue;
    }
    /*
    所有知识列表
     */
    @RequestMapping({"list"})
    @ResponseBody
    public JSONObject list(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        ILoginUser loginUser=VesApp.getLoginUser(request);
        Knowledge knowledge=new Knowledge();
        String type = jsonObject.getString("type");
        String searchText = jsonObject.getString("searchText");
        knowledge.setKnowledgeApprove(type);
        if(searchText==null||searchText.equals("undefined")){
            knowledge.setKnowledgeTitle("%%");
        }else {
            knowledge.setKnowledgeTitle("%"+searchText+"%");
        }
        knowledge.setUserId(loginUser.getId());
        List<Knowledge> knowledgeListAll=this.iSubKnowledgeApi.list(knowledge);
        List<Knowledge> knowledgeList = new ArrayList<>();
        //如果选择最新和最热，返回前10个数据
        if (!type.equals("all")){
            if (knowledgeListAll.size()>10){
                for (int i = 0; i < 10; i++) {
                    knowledgeList.add(knowledgeListAll.get(i));
                }
            }else {
                knowledgeList=knowledgeListAll;
            }
        }else {
            knowledgeList=knowledgeListAll;
        }
        for(Knowledge know:knowledgeList){
            String name=know.getCollectMan();
            if(name!=null&&name!=""){
                String[] coll=name.split(",");
                for (String str:coll){
                    if(str!=null&&str.equals(loginUser.getName())){
                        know.setCollectMan("0");
                    }
                }
            }
        }
        return toSuccess(knowledgeList);
    }

    /*
        分页所有知识列表
     */
    @RequestMapping({"pageList"})
    @ResponseBody
    public JSONObject pageList(HttpServletRequest request,int pageSize,int pageNumber,String type,String searchText){
        ILoginUser loginUser=VesApp.getLoginUser(request);
        JSONObject jsonObject = new JSONObject();
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);

        Knowledge knowledge=new Knowledge();
        knowledge.setKnowledgeApprove(type);
        if(searchText==null||searchText.equals("undefined")){
            knowledge.setKnowledgeTitle("%%");
        }else {
            knowledge.setKnowledgeTitle("%"+searchText+"%");
        }
        knowledge.setUserId(loginUser.getId());
        page.setCondition(knowledge);
        List<Knowledge> knowledgeListAll=this.iSubKnowledgeApi.pageList(page);
        List<Knowledge> knowledgeList = new ArrayList<>();
        //如果选择最新和最热，返回前10个数据
        if (!type.equals("all")){
            if (knowledgeListAll.size()>10){
                for (int i = 0; i < 10; i++) {
                    knowledgeList.add(knowledgeListAll.get(i));
                }
            }else {
                knowledgeList=knowledgeListAll;
            }
        }else {
            knowledgeList=knowledgeListAll;
        }
        for(Knowledge know:knowledgeList){
            String name=know.getCollectMan();
            if(name!=null&&name!=""){
                String[] coll=name.split(",");
                for (String str:coll){
                    if(str!=null&&str.equals(loginUser.getName())){
                        know.setCollectMan("0");
                    }
                }
            }

            know.setAttachmentList(iSubKnowledgeApi.getDocumentListByKID(know.getId()));

        }
        jsonObject.put("rows",page.getDatas());
        jsonObject.put("total",page.getTotalRecord());
        return jsonObject;
    }

    /**
     *判断当前用户是否收藏了该知识
     */
    @RequestMapping({"whetherCollect"})
    @ResponseBody
    public JSONObject whetherCollect(long knowledgeId,HttpServletRequest request){
        int returnValue = 0;
        List<KnowledgeCollect> knowledgeCollects = iSubKnowledgeApi.whetherCollect(knowledgeId,VesApp.getLoginUser(request).getId());
        if(knowledgeCollects != null && knowledgeCollects.size()>0){
            returnValue = 1;
        }
        return toSuccess(returnValue);
    }
    /*
     知识审批列表查询所有数据knowledgeApprove
     */
    @RequestMapping({"knowledgeApprove"})
    @ResponseBody
    public JSONObject knowledgeApprove(int pageSize, int pageNumber){
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        iSubKnowledgeApi.knowledgeApprove(page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    /*
     * 知识审批列表查询待审核数据knowledgeApprove-no
     * */
    @RequestMapping({"knowledgeApproveNo"})
    @ResponseBody
    public JSONObject knowledgeApproveNo(int pageSize, int pageNumber){
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        List<Knowledge> knowledges = iSubKnowledgeApi.knowledgeApproveNo(page);
        for(Knowledge know:knowledges){

            know.setAttachmentList(iSubKnowledgeApi.getDocumentListByKID(know.getId()));

        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", knowledges);
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    /*
     * 知识审批列表查询已审核数据knowledgeApproveEd
     * */
    @RequestMapping({"knowledgeApproveEd"})
    @ResponseBody
    public JSONObject knowledgeApproveEd(int pageSize, int pageNumber){
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        List<Knowledge> knowledges = iSubKnowledgeApi.knowledgeApproveEd(page);
        for(Knowledge know:knowledges){

            know.setAttachmentList(iSubKnowledgeApi.getDocumentListByKID(know.getId()));

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", knowledges);
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    /*
    知识贡献列表--根据发布数
     */
    @RequestMapping ({"contributeList"})
    @ResponseBody
    public JSONObject contributeList(){
        List<Map<String, String>> mapList = this.iSubKnowledgeApi.contributeList();
        List<Map<String, String>> resultMapList = new ArrayList<>();
        if (mapList.size()>10){
            for (int i = 0; i < 10; i++) {
                resultMapList.add(mapList.get(i));
            }
            return toSuccess(resultMapList);
        }else {
            return toSuccess(mapList);
        }
    }
    /*
    点击引用
     */
    @RequestMapping ({"upQuoteNumById"})
    @ResponseBody
    public JSONObject upQuoteNumById(String id){
        int quoteNum = iSubKnowledgeApi.getKnowledgeById(Integer.getInteger(id));
        this.iSubKnowledgeApi.upQuoteNum(Long.valueOf(id),quoteNum);
        return toSuccess(this.iSubKnowledgeApi.selectQuoteNum(Long.valueOf(id)));
    }
    /*
      点击收藏--只可以点击一次
     */
    @RequestMapping ({"upCollectNumById"})
    @ResponseBody
    public JSONObject upCollectNumById(String id,HttpServletRequest request,HttpSession session){
        ILoginUser iLoginUser = VesApp.getLoginUser(request);
        KnowledgeCollect knowledgeCollect = new KnowledgeCollect();
        knowledgeCollect.setKnowledgeId(Long.valueOf(id));
        knowledgeCollect.setUserId(iLoginUser.getId());
        iSubKnowledgeApi.upCollectNum(knowledgeCollect);
        return toSuccess(1);
    }
    /*
    知识审批中行内删除知识
     */
    @RequestMapping ({"delKnowledge"})
    @ResponseBody
    public JSONObject delKnowledge(String id){
        //1.删除知识的收藏关系
        this.iSubKnowledgeApi.delKnowledgeCollectByKnowId(id);
        //2.删除知识和附件的关联关系
        this.iSubKnowledgeApi.delKnowledgeAttrByKnowId(id);
        //3.删除知识

        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        Knowledge kd=iSubKnowledgeApi.browseInfoById(id);   //根据id查询知识信息;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(id);
        al.setFunctionModleId("OMC");
        al.setOperationType("删除");
        StringBuffer stri=new StringBuffer();
        al.setOperationContent("删除知识:"+kd.getKnowledgeTitle());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        this.iSubKnowledgeApi.delKnowledgeById(id);
        return toSuccess("success");
    }
    /*
    enableKnowledge 行内启用按钮
     */
    @RequestMapping ({"enableKnowledge"})
    @ResponseBody
    public JSONObject enableKnowledge(String id){
        //审计日志统计  开始
        Knowledge k= iSubKnowledgeApi.browseInfoById(id);
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
        al.setOperationContent("知识启用:"+k.getKnowledgeTitle());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return toSuccess(this.iSubKnowledgeApi.enableKnowledgeById(id));
    }
    /*
    行内停用按钮
     */
    @RequestMapping ({"disableKnowledge"})
    @ResponseBody
    public JSONObject disableKnowledge(String id){

        //审计日志统计  开始
        Knowledge k= iSubKnowledgeApi.browseInfoById(id);
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
        al.setOperationContent("知识停用:"+k.getKnowledgeTitle());
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return toSuccess(this.iSubKnowledgeApi.disableKnowledgeById(id));
    }
    /*
    行内拟稿中-待审核disableKnowledge
     */
    @RequestMapping({"subToApprove"})
    @ResponseBody
    public JSONObject subToApprove(String id){
        return toSuccess(this.iSubKnowledgeApi.subToApproveById(id));
    }
    /*
    浏览详情根据id browseInfoById
     */
    @RequestMapping({"browseInfoById"})
    @ResponseBody
    public JSONObject browseInfoById(String id,HttpServletRequest request){
        ILoginUser loginUser=VesApp.getLoginUser(request);
        Knowledge knowledge=this.iSubKnowledgeApi.browseInfoById(id);
        Knowledge know=new Knowledge();
        know.setKnowledgeBrowse(knowledge.getKnowledgeBrowse()+1);
        know.setId(Long.valueOf(id));
        iSubKnowledgeApi.browseNumberAdd(know);
        knowledge.setKnowledgeBrowse(knowledge.getKnowledgeBrowse()+1);
        List<KnowledgeCollect> knowledgeCollects = iSubKnowledgeApi.whetherCollect(know.getId(), loginUser.getId());
        if (knowledgeCollects.size()<=0||knowledgeCollects.equals("")){
            knowledge.setWhetherCollect(false);
        }else {
            knowledge.setWhetherCollect(true);
        }
        return toSuccess(knowledge);
    }
    /*/
    审批页面痛通过approvePass
     */
    @RequestMapping({"approvePass"})
    @ResponseBody
    public JSONObject approvePass(@RequestBody List<Long> id){
        //审计日志统计  开始
        List<String>list= iSubKnowledgeApi.selectListAll(id);
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(id.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("知识审核");
        al.setOperationContent("知识审核通过:"+StringUtils.join(list.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return toSuccess(this.iSubKnowledgeApi.approvePass(id));
    }
    /*
    审核页面不通过approveDisPass
     */
    @RequestMapping({"approveDisPass"})
    @ResponseBody
    public JSONObject approveDisPass(@RequestBody List<Long> id){
        List<String>list= iSubKnowledgeApi.selectListAll(id);
        //审计日志统计  开始
        AuditLog al=new AuditLog();
//        AuditLAction auditLogAction= AuditLAction.getInstance();
//        AuditLogAction auditLogAction=new AuditLogAction();
        HttpServletRequest req=getHttpServletRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        al.setUserId(VesApp.getLoginUser(req).getId());
        al.setOperationObjId(StringUtils.join(id.toArray(), ","));
        al.setFunctionModleId("OMC");
        al.setOperationType("知识审核");
        al.setOperationContent("知识审核不通过:"+StringUtils.join(list.toArray(), ","));
        try {
            al.setOperationTime(sdf.parse(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        int i=auditLogApi.insert(al);
        //审计日志统计  结束
        return  toSuccess(this.iSubKnowledgeApi.approveDisPass(id));
    }

    @RequestMapping({"editKnowledge"})
    @ResponseBody
    public JSONObject editKnowledge(@RequestParam("info") String info,HttpServletRequest request){
        Knowledge knowledge = new Knowledge();
        JSONObject obj = JSONObject.parseObject(info);
        knowledge.setId(obj.getLong("knowledgeId"));
        knowledge.setKnowledgeTitle(obj.getString("knowledgeTitle"));
        String[] keyWords = obj.getString("knowledgeKey").split("，");
        StringBuffer sb = new StringBuffer();
        sb.append("，");
        for(String s:keyWords){
            sb.append(s);
            sb.append("，");
        }
        knowledge.setKnowledgeKey(sb.toString());
        knowledge.setUseMember(obj.getInteger("useMember"));
        knowledge.setKnowledgeType(obj.getInteger("knowledgeType"));
        knowledge.setRequestType(obj.getString("requestType"));
        knowledge.setCreateTime(new Date());
        knowledge.setMethodsStep(obj.getString("methodsStep"));
        knowledge.setNarrative(obj.getString("narrative"));
        knowledge.setKnowledgeState(obj.getInteger("knowledgeState"));
        JSONArray jsonArray = obj.getJSONArray("existFiles");
        List<Long> documentIds = new ArrayList<>();
        if(jsonArray.size()>0){
            for(int i=0;i<jsonArray.size();i++){
                documentIds.add(jsonArray.getLong(i));
            }

        }
        int returnValue = iSubKnowledgeApi.updateKnowledge(knowledge);
        MultipartHttpServletRequest files = (MultipartHttpServletRequest) request;
        List<MultipartFile> fileList = files.getFiles("files");
        ILoginUser loginUser = VesApp.getLoginUser(request);
        //1.删除知识关联的所有的附件
        iSubKnowledgeApi.deleteDocumentByKID(knowledge.getId(),documentIds);
        this.insertDocument(fileList,knowledge.getId(),request,loginUser);
        return toSuccess(returnValue);
    }


    public KnowledgeAction() {
        super();
        iSubKnowledgeApi = WebItsmBeanUtil.getSubKnowledgeService();
        this.fileClient = WebItsmBeanUtil.getFileClient();
    }

    /*
     * 运维中心知识库查询--->根据主题或类型一二级分类查询
     * */
    @RequestMapping({"selectAllKnowByLike"})
    @ResponseBody
    public JSONObject selectAllKnowByLike(String selectText,String selectValue,int pageSize, int pageNumber){
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        iSubKnowledgeApi.selectAllKnowByLike(selectText, selectValue, page);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", page.getDatas());
        jsonObject.put("total", page.getTotalRecord());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    /*
     * 运维中心知识库查询--->左上角下拉树返回值
     * */
    @RequestMapping({"selectTreeKnowChild"})
    @ResponseBody
    public JSONObject selectTreeKnowChild(){
        KnowledgeTreeVo knowledgeTreeVo = new KnowledgeTreeVo();
        knowledgeTreeVo.setId(632499);
        knowledgeTreeVo.setName("所有目录");
        knowledgeTreeVo.setpId(0);
        knowledgeTreeVo.setOpen(true);
        knowledgeTreeVo.setLevel(0);
        List<KnowledgeTreeVo> childrenSecond = iSubKnowledgeApi.selectSecondTree(knowledgeTreeVo.getId());
        int size = childrenSecond.size();
        for (int i = 0; i < size; i++) {
            childrenSecond.get(i).setOpen(true);
            childrenSecond.get(i).setLevel(0);
            List<KnowledgeTreeVo> childrenThrid = iSubKnowledgeApi.selectThirdTree(childrenSecond.get(i).getId());
            childrenSecond.get(i).setChildren(childrenThrid);
        }
        knowledgeTreeVo.setChildren(childrenSecond);
        JSONObject resultJSON = new JSONObject();
        resultJSON.put("code", 200);
        resultJSON.put("data", knowledgeTreeVo);
        return resultJSON;
    }

    /*
     * 运维中心知识库查询--->批量删除知识
     * */
    @RequestMapping({"delectKnowledgesByIds"})
    @ResponseBody
    public JSONObject delectKnowledgesByIds(@RequestBody List<String> ids){

//        //审计日志统计  开始
//        List<Long>idss=new ArrayList<>();
//        for(int i=0;i<ids.size();i++){
//            idss.add(Long.parseLong(ids.get(i)));
//        }
//        List<String>list= iSubKnowledgeApi.selectListAll(idss);
//        AuditLog al=new AuditLog();
////        AuditLAction auditLogAction= AuditLAction.getInstance();
////        AuditLogAction auditLogAction=new AuditLogAction();
//        HttpServletRequest req=getHttpServletRequest();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = sdf.format(new Date());
//        al.setUserId(VesApp.getLoginUser(req).getId());
//        al.setOperationObjId(StringUtils.join(ids.toArray(), ","));
//        al.setFunctionModleId("OMC");
//        al.setOperationType("删除");
//        al.setOperationContent("删除知识:"+StringUtils.join(list.toArray(), ","));
//        try {
//            al.setOperationTime(sdf.parse(date));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        int i=auditLogApi.insert(al);
//        //审计日志统计  结束
        for (String id : ids) {
            this.delKnowledge(id);
        }
        return toSuccess(200);
    }

    //上传添加知识模板
    @RequestMapping({"uploadFileMould"})
    @ResponseBody
    public JSONObject uploadFileMould(HttpServletRequest httpRequest){
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
        MultipartFile file = request.getFile("file");
        String mouldName = request.getParameter("mouldName");
        String mouldId = request.getParameter("mouldId");
        Long typeId = Long.parseLong(request.getParameter("typeId"));
        String typeName = request.getParameter("typeName");
        KnowledgeMouldVo mould = new KnowledgeMouldVo();

        ILoginUser loginUser=VesApp.getLoginUser(request);

        if (file != null ) {
            CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) file;
            try {
                String fileName = commonsMultipartFile.getFileItem().getName();
                //上传文件
                long fileId = iFileClient.upLoadFile(FileGroupEnum.VES_SYSTEM, commonsMultipartFile).longValue();
                mould.setCreateTime(new Date());
                mould.setMouldName(mouldName);
                mould.setMouldFileId(fileId);
                mould.setMouldId(mouldId);
                mould.setMouldFileName(fileName);
                mould.setCreateMan(loginUser.getId());
                mould.setTypeId(typeId);
                mould.setTypeName(typeName);
                iSubKnowledgeApi.insertKnowledgeMould(mould);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }else {
            return toSuccess("添加模板失败！");
        }
        return toSuccess("添加模板成功！");
    }

    //分页获取模板信息
    @RequestMapping({"getKnowledgeMouldByPage"})
    @ResponseBody
    public JSONObject getKnowledgeMouldByPage(KnowledgeMouldVo vo, int pageNumber, int pageSize,String mouldId,Long typeId){
        Page page = new Page<>();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        JSONObject jsonObject = new JSONObject();
        //判断前台是否传来模板名称
        if(StringUtils.isNotBlank(mouldId)){
            Map<String, Object> condition = new HashMap();
            condition.put("mouldId",mouldId);
            page.setCondition(condition);

            iSubKnowledgeApi.selectMouldIdByMouldIdPage(page);

            jsonObject.put("rows", page.getDatas());
            jsonObject.put("total", page.getTotalRecord());
            jsonObject.put("page", page.getStartRow());

        }else{
            if(typeId != null && typeId != 0)
                vo.setTypeId(typeId);
            page.setCondition(vo);
            iSubKnowledgeApi.getKnowledgeMouldByPage(page);

            jsonObject.put("rows", page.getDatas());
            jsonObject.put("total", page.getTotalRecord());
            jsonObject.put("page", page.getStartRow());

        }

        return jsonObject;
    }

    /*
    根据知识id获取知识附件信息
     */
    @RequestMapping({"findFilesByKnowledgeId"})
    @ResponseBody
    public JSONObject findFilesByKnowledgeId(String mouldId){
        List<KnowledgeMouldVo> knowledges = iSubKnowledgeApi.selectMouldIdByMouldId(mouldId);
        return toSuccess(knowledges);
    }
    /*
    根据上传文件id删除
     */
    @RequestMapping({"deleteMouldByMouldFileId"})
    @ResponseBody
    public JSONObject deleteMouldByMouldFileId(@RequestBody List<Integer> idList){
        if (idList != null && idList.size()>0){
            for (int i = 0; i < idList.size(); i++) {
                iSubKnowledgeApi.deleteMouldById(idList.get(i));
            }
        }
        return toSuccess("删除成功！");
    }
    /*
    根据模板id删除
     */
    @RequestMapping({"deleteMouldByMouldId"})
    @ResponseBody
    public JSONObject deleteMouldByMouldId(@RequestBody List<String> idList){

        try {

            if (idList != null && idList.size()>0){
                for (int i = 0; i < idList.size(); i++) {
                    iSubKnowledgeApi.deleteMouldByMouldId(idList.get(i));
                }
            }

            return toSuccess("删除成功！");

        }catch (Exception e){

            return toSuccess("删除失败...");

        }

    }

    //下载文件
    @RequestMapping({"downLoadFilesByKnowledgeId"})
    @ResponseBody
    public JSONObject downloadFile(HttpServletResponse response, Long fileId){
        try {
            File var3 = this.iFileClient.getFileByID(fileId);
            this.downloadIO(var3, response);
            logger.error("下载附件成功");
            return toSuccess("下载附件成功");
        } catch (Exception var4) {
            logger.error("下载附件失败");
            return toSuccess("下载附件失败");
        }

    }

    //下载IO流
    private void downloadIO(File var1, HttpServletResponse var2) throws IOException {
        FileInputStream var3 = null;
        BufferedInputStream var4 = null;
        BufferedOutputStream var5 = null;

        try {
            var3 = new FileInputStream(var1);
            String var6 = URLEncoder.encode(var1.getName(), "utf-8");
            var2.setHeader("Content-Disposition", "attachment; filename=" + var6);
            var2.setContentType("application/octet-stream");
            int var7 = var3.available();
            var2.setContentLength(var7);
            var4 = new BufferedInputStream(var3);
            var5 = new BufferedOutputStream(var2.getOutputStream());
            byte[] var8 = new byte[2048];

            int var9;
            while(-1 != (var9 = var4.read(var8, 0, var8.length))) {
                var5.write(var8, 0, var9);
            }

            var5.flush();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if(var4 != null) {
                var4.close();
            }

            if(var5 != null) {
                var5.close();
            }

            if(var3 != null) {
                var3.close();
            }

        }

    }

    /**
     * jwt 创建知识模板类型
     * @param name
     * @return
     */
    @RequestMapping({"/addKnowType"})
    @ResponseBody
    public JSONObject addKnowType(String name){
        try {
            this.subKnowTypeApi.batchAddKnowType(new ArrayList<KnowTypeBo>(){{add(new KnowTypeBo(name));}});
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return toFailForGroupNameExsit("创建失败");
        }
        return toSuccess("创建成功");
    }


    /**
     * jwt 删除知识模板类型
     * @param ids
     * @return
     */
    @RequestMapping({"/delKnowType"})
    @ResponseBody
    public JSONObject delKnowType(String ids){
        try {
            List<Long> list = Arrays.asList((Long[])ConvertUtils.convert(ids.split(","), Long.class));
            Page<KnowledgeMouldVo,KnowledgeMouldVo> page = new Page<>();
            page.setStartRow(0);
            page.setRowCount(99999);
            KnowledgeMouldVo vo = new KnowledgeMouldVo();
            for (Long aLong : list) {
                vo.setTypeId(aLong);
                page.setCondition(vo);
                iSubKnowledgeApi.getKnowledgeMouldByPage(page);
                if(page.getTotalRecord() > 0)
                    return toFailForGroupNameExsit("有模板绑定该类型,不允许删除!");
            }
            this.subKnowTypeApi.delKnowType(list);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return toFailForGroupNameExsit("删除失败");
        }
        return toSuccess("删除成功");
    }

    /**
     * jwt 更新知识模板类型
     * @param bo
     * @return
     */
    @RequestMapping({"/updateKnowType"})
    @ResponseBody
    public JSONObject updateKnowType(@RequestBody KnowTypeBo bo){
        try {
            this.subKnowTypeApi.updateKnowType(bo);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return toFailForGroupNameExsit("更新失败");
        }
        return toSuccess("更新成功");
    }

    /**
     * jwt 分页获取知识模板类型
     * @param page
     * @return
     */
    @RequestMapping({"/queryKnowType"})
    @ResponseBody
    public JSONObject queryKnowType(@RequestBody Page<KnowTypeBo,KnowTypeBo> page){
        try {
            this.subKnowTypeApi.queryKnowType(page);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return toFailForGroupNameExsit("查询失败");
        }
        return toSuccess(page);
    }

}
