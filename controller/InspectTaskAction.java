package com.vlinksoft.ves.inspection.controller;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.audit.api.AuditLogApi;
import com.vlinksoft.ves.audit.bo.AuditLog;
import com.vlinksoft.ves.inspection.api.IInspectTaskApi;
import com.vlinksoft.ves.inspection.api.IInspectionMouldApi;
import com.vlinksoft.ves.inspection.api.IInspectionPlanApi;
import com.vlinksoft.ves.inspection.bo.*;
import com.vlinksoft.ves.platform.file.bean.FileGroupEnum;
import com.vlinksoft.ves.platform.file.bean.FileModel;
import com.vlinksoft.ves.platform.file.service.IFileClientApi;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;

import com.vlinksoft.ves.platform.web.vo.IRole;
import com.vlinksoft.ves.util.FileUtil;
import com.vlinksoft.ves.util.InspectionFileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.taskdefs.condition.IsTrue;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/*
巡检任务
 */
@Controller
@RequestMapping({"/vesapp/inspection/inspectTask"})
public class InspectTaskAction extends BaseAction {
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(InspectTaskAction.class);

    private static final String currentLoginPath = "/static/dist/img/documentKnowledge/";

    @Resource
    private IInspectTaskApi inspectTaskApi;

    @Resource
    private IFileClientApi iFileClient;

    @Resource
    private IInspectionMouldApi iInspectionMouldApi;

    @Resource
    private IInspectionPlanApi iInspectionPlanApi;

    //获取任务详情
    @RequestMapping({"getTaskById"})
    @ResponseBody
    public JSONObject getTaskById(Long id) {
        logger.info("查看任务详情");
        JSONObject json = new JSONObject();
        try {
            InspectionTask inspectionTask = inspectTaskApi.getTaskById(id);
            json.put("data", inspectionTask);
            json.put("code", "200");
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            json.put("err", "查看失败");
            return json;
        }
    }

    //查询待办巡检
    @RequestMapping({"findAllMyTaskByUserId"})
    @ResponseBody
    public JSONObject findAllMyTaskByUserId(int pageNumber, int pageSize, HttpServletRequest request, String inspectionReport) throws ParseException {
        logger.info("查询待办巡检");
        ILoginUser loginUser = VesApp.getLoginUser(request);
        Long loginId = loginUser.getId();
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        InspectionTaskQuery query = new InspectionTaskQuery();
        query.setUserId(loginId);
        if (inspectionReport != null && !inspectionReport.equals("")) {
            query.setInspectionReport("%" + inspectionReport + "%");
        }
        //添加域Id
        String domainId = request.getParameter("domainId");
        query.setDomainId(domainId);
        page.setCondition(query);
        List<InspectionTask> inspectionTaskList = inspectTaskApi.findAllMyTaskByUserId(page);
        //现在的时间
//        Long currDate = new Date().getTime();
//        for (InspectionTask inspectionTask : inspectionTaskList) {
//            Date endDate = inspectionTask.getInspectionEndDate();
//            InspectionTaskState taskState = inspectionTask.getTaskState();
//            if (endDate.getTime() < currDate) {
//                if (taskState.equals(InspectionTaskState.wait_writer)) {
//                    inspectionTask.setTaskState(InspectionTaskState.un_complete);
//                    inspectTaskApi.updateTimeOutState(inspectionTask.getId());
//                }
//            }
//        }
//        //如果是管理员则查询全部
//        if (loginId == 1) {
//            inspectionTaskList = inspectTaskApi.findAfterAllMyTaskByAdminId(page);
//        } else {
//            inspectionTaskList = inspectTaskApi.findAfterAllMyTaskByUserId(page);
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", inspectionTaskList);
        jsonObject.put("total", inspectionTaskList.size());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    //提交报告
    @RequestMapping({"toReportById"})
    @ResponseBody
    public JSONObject toReportById(@RequestBody InspectionTask inspectionTask) {
        logger.info("提交报告");
        System.err.println(inspectionTask);
        JSONObject json = new JSONObject();
        try {
            inspectionTask.setReportTime(new Date());
            inspectionTask.setTaskState(InspectionTaskState.completed);
            inspectTaskApi.toReportById(inspectionTask);
            json.put("code", "200");
        } catch (Exception e) {
            e.printStackTrace();
            json.put("err", "修改失败");
        } finally {
            return json;
        }
    }

    //上传文件
    @RequestMapping({"uploadFile"})
    @ResponseBody
    public JSONObject uploadFile(HttpServletRequest httpRequest) {
        logger.info("上传附件");
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
        List<MultipartFile> files = request.getFiles("file");
        Long userId = Long.valueOf(request.getParameter("userId"));
        String taskNumber = request.getParameter("taskNumber");
        if (files != null && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                InspectionTaksFile inspectionTaksFile = new InspectionTaksFile();
                try {
                    CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartFile;
                    String fileName = commonsMultipartFile.getFileItem().getName();
                    //上传文件
                    long fileId = iFileClient.upLoadFile(FileGroupEnum.VES_SYSTEM, commonsMultipartFile).longValue();
                    boolean fileImg = true;
                    fileImg = this.iFileClient.isExist(fileId);
                    FilePathVo filePathVo = new FilePathVo();
                    if (fileImg) {
                        File file2 = iFileClient.getSystemFileByID(fileId, request.getSession().getServletContext().getRealPath("/") + currentLoginPath);
                        filePathVo.setFilePath(currentLoginPath + "/" + file2.getName());
                    }
                    filePathVo.setFileId(fileId);
                    filePathVo.setAppendixType(1);
                    filePathVo.setAnnexId(taskNumber);
                    inspectTaskApi.insertFilePath(filePathVo);
                    inspectionTaksFile.setFileName(fileName);
                    inspectionTaksFile.setFileId(fileId);
                    inspectionTaksFile.setUploadTime(new Date());
                    inspectionTaksFile.setUploadUserId(userId);
                    inspectionTaksFile.setTaskNumber(taskNumber);
                    inspectTaskApi.insertInspectionFile(inspectionTaksFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return toSuccess("ok");
    }

    //根据用户id获取上传文件
    @RequestMapping({"findFilesByUserId"})
    @ResponseBody
    public JSONObject findFilesByUserId(Long userId, String taskNumber) {
        List<InspectionTaksFile> inspectionTaksFileList = inspectTaskApi.findFilesByUserId(userId, taskNumber);
        return toSuccess(inspectionTaksFileList);
    }

    //下载文件
    @RequestMapping({"downloadFile"})
    @ResponseBody
    public String downloadFile(HttpServletResponse response, String fileId) {
        try {
            File var3 = this.iFileClient.getFileByID(Long.parseLong(fileId));
            this.downloadIO(var3, response);
            logger.error("下载附件成功");
        } catch (Exception var4) {
            logger.error("下载附件失败");
        }
        return null;
    }

    //下载IO流
    private void downloadIO(File var1, HttpServletResponse var2) throws IOException {
        FileInputStream var3 = null;
        BufferedInputStream var4 = null;
        BufferedOutputStream var5 = null;

        try {
            var3 = new FileInputStream(var1);
            String var6 = URLEncoder.encode(var1.getName(), "UTF-8");
            var2.setHeader("Content-Disposition", "attachment; filename=" + var6);
            var2.setContentType("application/octet-stream");
            int var7 = var3.available();
            var2.setContentLength(var7);
            var4 = new BufferedInputStream(var3);
            var5 = new BufferedOutputStream(var2.getOutputStream());
            byte[] var8 = new byte[2048];

            int var9;
            while (-1 != (var9 = var4.read(var8, 0, var8.length))) {
                var5.write(var8, 0, var9);
            }

            var5.flush();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (var4 != null) {
                var4.close();
            }

            if (var5 != null) {
                var5.close();
            }

            if (var3 != null) {
                var3.close();
            }

        }

    }

    /*
    查询附件路径
     */
    @RequestMapping({"getFilePathById"})
    @ResponseBody
    public JSONObject getFilePathById(String id) {
        List<FilePathVo> filePathVo = inspectTaskApi.getFilePathById(id);
        return toSuccess(filePathVo);
    }

    @RequestMapping({"getResportFileByFileId"})
    @ResponseBody
    public JSONObject getResportFileByFileId(long fileId, long mouldId) {
        JSONObject jsonObject = new JSONObject();
        long fileValueId = 0l;
        try {
            if (fileId != 0) {
                fileValueId = Long.valueOf(fileId);
            } else {
                Long id = Long.valueOf(mouldId);
                InspectionMouldVo mouldVo = iInspectionMouldApi.getInspectionMouldById(id);
                fileValueId = mouldVo.getMouldFileId();
            }
            File file = this.iFileClient.getFileByID(fileValueId);
//            File file = this.iFileClient.getSystemFileByID(fileValueId,FileGroupEnum.VES_SYSTEM.toString());
            String excelHtml = InspectionFileUtils.readExcelToHtml(file, false);
            jsonObject.put("data", excelHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @RequestMapping({"saveInspectionReportTable"})
    @ResponseBody
    public JSONObject saveInspectionReportTable(@RequestBody List<List<String>> tableDate, long inspectionReportId) {
        InspectionTask inspectionTask = inspectTaskApi.getTaskById(inspectionReportId);
        int mouldId = inspectionTask.getMouldId();
        int fileId = inspectionTask.getFileId();
        if (fileId != 0) {
            try {
                this.reSetExcelFile(fileId, tableDate, inspectionTask);
                iFileClient.deleteFile(fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            InspectionMouldVo mould = iInspectionMouldApi.getInspectionMouldById(mouldId);
            this.reSetExcelFile((int) mould.getMouldFileId(), tableDate, inspectionTask);
        }
        if (inspectionTask.getTaskState().equals(InspectionTaskState.wait_writer)) {
            inspectionTask.setTaskState(InspectionTaskState.writering);
        }
        inspectionTask.setReportTime(new Date());
        System.out.println(inspectionTask.toString());
        int i = inspectTaskApi.updateInspectionInfo(inspectionTask);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", "保存成功！");
        jsonObject.put("value", i);
        return jsonObject;
    }

    @RequestMapping({"updateInspectionReportTable"})
    @ResponseBody
    public JSONObject updateInspectionReportTable(@RequestBody List<List<String>> tableDate, long inspectionReportId) {
        InspectionTask inspectionTask = inspectTaskApi.getTaskById(inspectionReportId);
        int mouldId = inspectionTask.getMouldId();
        int fileId = inspectionTask.getFileId();
        if (fileId != 0) {
            try {
                this.reSetExcelFile(fileId, tableDate, inspectionTask);
                iFileClient.deleteFile(fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            InspectionMouldVo mould = iInspectionMouldApi.getInspectionMouldById(mouldId);
            this.reSetExcelFile((int) mould.getMouldFileId(), tableDate, inspectionTask);
        }
        if (inspectionTask.getTaskState().equals(InspectionTaskState.overtime_wait_writer)) {
            inspectionTask.setTaskState(InspectionTaskState.overtime_completed);
        } else {
            inspectionTask.setTaskState(InspectionTaskState.completed);
        }
        inspectionTask.setReportTime(new Date());
        int i = inspectTaskApi.updateInspectionInfo(inspectionTask);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", "提交成功！");
        jsonObject.put("value", i);
        return jsonObject;
    }

    private void reSetExcelFile(int oldFileId, List<List<String>> tableDate, InspectionTask inspectionTask) {
        try {
            File oldFile = iFileClient.getFileByID(oldFileId);
            long newFileId = iFileClient.upLoadFile(FileGroupEnum.VES_SYSTEM, oldFile).longValue();
//            File newFile = iFileClient.getFileByID(newFileId);
            FileModel fileModel = iFileClient.getFileModelByID(newFileId);
            File newFile = new File(fileModel.getFilePath());
            FileUtil.copyFile(oldFile, newFile);
            InspectionFileUtils.reSetExcelInfo(tableDate, newFile);
            inspectionTask.setFileId((int) newFileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传巡检报告文件
    @RequestMapping({"uploadReportFile"})
    @ResponseBody
    public JSONObject uploadFileMould(HttpServletRequest httpRequest) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpRequest;
        MultipartFile file = request.getFile("file");
        String reportMouldId = request.getParameter("reportMouldId");
        String inspectionReportId = request.getParameter("inspectionReportId");
        String domainId = request.getParameter("domainId");
        JSONObject jsonObject = new JSONObject();
        if (file != null) {
            CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) file;
            try {
                //上传文件
                long fileId = iFileClient.upLoadFile(FileGroupEnum.VES_SYSTEM, commonsMultipartFile).longValue();
                FileModel reportModeFile = iFileClient.getFileModelByID(fileId);
                InspectionMouldVo mouldVo = iInspectionMouldApi.getInspectionMouldById(Long.valueOf(reportMouldId));
                FileModel mouldModeFile = iFileClient.getFileModelByID(mouldVo.getMouldFileId());
                File mouldFile = new File(mouldModeFile.getFilePath());
                File reportFile = new File(reportModeFile.getFilePath());
                boolean verifyExcelFile = InspectionFileUtils.VerifyExcelFile(reportFile, mouldFile);
                if (verifyExcelFile) {
                    jsonObject.put("isTrue", true);
                    jsonObject.put("data", "验证格式正确，上传成功！");
                    InspectionTask inspectionTask = inspectTaskApi.getTaskById(Long.valueOf(inspectionReportId));
                    if (inspectionTask.getTaskState().equals(InspectionTaskState.overtime_wait_writer)) {
                        inspectionTask.setTaskState(InspectionTaskState.overtime_completed);
                    } else {
                        inspectionTask.setTaskState(InspectionTaskState.completed);
                    }
                    inspectionTask.setFileId((int) fileId);
                    inspectionTask.setReportTime(new Date());
                    inspectionTask.setDomainId(domainId);
                    inspectTaskApi.updateInspectionInfo(inspectionTask);
                } else {
                    jsonObject.put("isTrue", false);
                    jsonObject.put("data", "验证格式错误，上传失败！");
                    iFileClient.deleteFile(fileId);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return jsonObject;
    }

    //巡检统计查询巡检报告
    @RequestMapping({"selectInspectionReportByUser"})
    @ResponseBody
    public JSONObject selectInspectionReportByUser(int pageNumber, int pageSize, HttpServletRequest request, String inspectionReport, String domainId,Long userId,Long startTime,Long endTime) {
//        boolean isLookAll = false;
//        ILoginUser loginUser = VesApp.getLoginUser(request);
//        if (loginUser.getId() == 1 || loginUser.getUserType() >= 3) {
//            isLookAll = true;
//        } else {
//            List<IRole> roles = loginUser.getRoles();
//            if (roles != null && roles.size() > 0) {
//                for (int i = 0; i < roles.size(); i++) {
//                    IRole role = roles.get(i);
//                    if (role.getId() == 499500) {
//                        isLookAll = true;
//                        break;
//                    }
//                }
//            }
//        }
        Page page = new Page();
        int startNumber = (pageNumber - 1) * pageSize;
        page.setStartRow(startNumber);
        page.setRowCount(pageSize);
        InspectionTaskQuery query = new InspectionTaskQuery();
//        query.setUserId(loginUser.getId());
        if (inspectionReport != null && !inspectionReport.equals("")) {
            query.setInspectionReport("%" + inspectionReport + "%");
        }
        //添加所属域Id
        query.setDomainId(domainId);
        query.setUserId(userId);
        query.setStartTime(startTime == null ? null : new Date(startTime));
        query.setEndTime(endTime == null ? null : new Date(endTime));
        page.setCondition(query);
        List<InspectionTask> taskList = null;
//        if (isLookAll) {
            taskList = inspectTaskApi.findAllTask(page);
//        } else {
//            taskList = inspectTaskApi.findAllInspection(page);
//        }
        Long currDate = new Date().getTime();
        for (InspectionTask inspectionTask : taskList) {
            Date endDate = inspectionTask.getInspectionEndDate();
            InspectionTaskState taskState = inspectionTask.getTaskState();
            if (endDate.getTime() < currDate) {
                if (taskState.equals(InspectionTaskState.wait_writer)) {
                    inspectionTask.setTaskState(InspectionTaskState.un_complete);
                    inspectTaskApi.updateTimeOutState(inspectionTask.getId());
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", taskList);
        jsonObject.put("total", taskList.size());
        jsonObject.put("page", page.getStartRow());
        return jsonObject;
    }

    //提供的接口
    @RequestMapping({"getInspectionReportNumByTime"})
    @ResponseBody
    public JSONObject getInspectionReportNumByTime(long startTime, long endTime, int userId, String state, int serviceId) {
        JSONObject json = new JSONObject();
        ReportNumQuery query = new ReportNumQuery();
        if (startTime != 0 && endTime != 0) {
            Date startDate = new Date(startTime);
            Date endDate = new Date(endTime);
            query.setEndTime(endDate);
            query.setStartTime(startDate);
        }
        if (state != null && !state.equals("")) {
            try {
                query.setState(InspectionTaskState.valueOf(state));
            } catch (Exception e) {
                json.put("data", "状态参数传入出错！");
                return json;
            }
        }
        query.setUserId(userId);
        query.setServiceId(serviceId);
        int taskNum = inspectTaskApi.selectReportNumByQuery(query);
        json.put("data", taskNum);
        return json;
    }
}