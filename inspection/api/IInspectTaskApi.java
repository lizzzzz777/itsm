package com.vlinksoft.ves.inspection.api;


import com.vlinksoft.ves.inspection.bo.FilePathVo;
import com.vlinksoft.ves.inspection.bo.InspectionTaksFile;
import com.vlinksoft.ves.inspection.bo.InspectionTask;
import com.vlinksoft.ves.inspection.bo.ReportNumQuery;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;


import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IInspectTaskApi {

    Map<String,Long> addTask(InspectionTask jsonObj);
    //查询全部巡检报告，不区分巡检人，报告状态
    List<InspectionTask> findAllTask(Page page);

    int publishTask(Long id);

    void delTask(Long[] ids);

    InspectionTask getTaskById(Long id);

    void updateTaskById(InspectionTask inspectionTask);

    List<InspectionTask> findAllMyTaskByUserId(Page page);

    List<InspectionTask> findAfterAllMyTaskByUserId(Page page);

    List<InspectionTask> findAfterAllMyTaskByAdminId(Page page);

    void toReportById(InspectionTask inspectionTask);

    //查询我的巡检报告，根据巡检人id，不区分报告状态
    List<InspectionTask> findAllInspection(Page page);

    String getMaxNumer(String now);
    //自动生成任务
    int autoCreateTask(InspectionTask inspectionTask);

    void updateInspectionType(Long id);

    void updateTimeOutState(Long id);

    void insertInspectionFile(InspectionTaksFile inspectionTaksFile);

    boolean  checkRepeat(Long planId,Long userId,String inspectionStartTime,String inspectionEndTime);

    List<InspectionTaksFile> findFilesByUserId(Long userId,String taskNumber);
    //查询今天所有的任务
    List<InspectionTask> findAllTaskByDay(Date today);

    void insertFilePath(FilePathVo filePathVo);

    List<FilePathVo> getFilePathById(String id);

    int findByTimeType(Long planId);

    List<InspectionTask> findAllInspections(Page page);

    int findAgenda(Long loginUser);

    int updateInspectionInfo(InspectionTask inspectionTask);

    List<InspectionTask> selectInspectionTaskByPlanId(long planId);

    void deleteInspectionReportByPlanId(Long planId);

    int selectReportNumByQuery(ReportNumQuery query);

    long selectCompleteNum(Date begin,Date end,String status);

    List<Map<String,Object>> selectCompleteNumByUser(Date begin,Date end);
}
