package com.vlinksoft.ves.inspection.dao;

import com.vlinksoft.ves.inspection.bo.FilePathVo;
import com.vlinksoft.ves.inspection.bo.InspectionTaksFile;
import com.vlinksoft.ves.inspection.bo.InspectionTask;
import com.vlinksoft.ves.inspection.bo.ReportNumQuery;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface InspectTaskDao {

    int addTask(InspectionTask jsonObj);

    List<InspectionTask> findAllTask(Page page);

    int publishTask(Long id);

    void delTask(Long[] ids);

    InspectionTask getTaskById(Long id);

    void updateTaskById(InspectionTask inspectionTask);

    List<InspectionTask> findAllMyTaskByUserId(Page page);

    List<InspectionTask> findAfterAllMyTaskByUserId(Page page);

    List<InspectionTask> findAfterAllMyTaskByAdminId(Page page);

    void toReportById(InspectionTask inspectionTask);

    List<InspectionTask> findAllInspection(Page page);

    String getMaxNumer(String now);

    int autoCreateTask(InspectionTask inspectionTask);

    void updateInspectionType(Long id);

    void updateTimeOutState(Long id);

    void insertInspectionFile(InspectionTaksFile inspectionTaksFile);

    List<InspectionTask> checkRepeat(Long planId, Long userId, String inspectionStartTime,String inspectionEndTime);

    List<InspectionTaksFile> findFilesByUserId(Long userId,String taskNumber);

    List<InspectionTask> findAllTaskByDay(String strDate,String endDate);

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
