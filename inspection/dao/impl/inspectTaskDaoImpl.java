package com.vlinksoft.ves.inspection.dao.impl;

import com.vlinksoft.ves.inspection.bo.FilePathVo;
import com.vlinksoft.ves.inspection.bo.InspectionTaksFile;
import com.vlinksoft.ves.inspection.bo.InspectionTask;
import com.vlinksoft.ves.inspection.bo.ReportNumQuery;
import com.vlinksoft.ves.inspection.dao.InspectTaskDao;
import com.vlinksoft.ves.platform.dao.BaseDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class inspectTaskDaoImpl extends BaseDao<InspectionTask> implements InspectTaskDao {

    @Autowired    //构造函数，写在最下面
    public inspectTaskDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
        super(session, InspectTaskDao.class.getName());
    }
    //添加任务
    @Override
    public int addTask(InspectionTask jsonObj) {
        return getSession().insert("saveTask",jsonObj);
    }
    //查询创建任务
    @Override
    public List<InspectionTask> findAllTask(Page page) {
        return getSession().selectList("findAllTask",page);
    }

    @Override
    public int publishTask(Long id) {
        return getSession().update("publishTask",id);
    }
    //删除任务
    @Override
    public void delTask(Long[] ids) {
        for(Long id : ids){
            getSession().delete("delTask",id);
        }

    }
    //查询任务详情
    @Override
    public InspectionTask getTaskById(Long id) {
        return getSession().selectOne("getTaskByIds",id);
    }
    //修改任务
    @Override
    public void updateTaskById(InspectionTask inspectionTask) {
        getSession().update("updateTaskById",inspectionTask);
    }
    //查询我的待办任务
    @Override
    public List<InspectionTask> findAllMyTaskByUserId(Page page) {
        return getSession().selectList("findAllMyTaskByUserId",page);
    }
    //更新后
    @Override
    public List<InspectionTask> findAfterAllMyTaskByUserId(Page page) {
        return getSession().selectList("findAfterAllMyTaskByUserId",page);
    }

    @Override
    public List<InspectionTask> findAfterAllMyTaskByAdminId(Page page) {
        return getSession().selectList("findAfterAllMyTaskByAdminId",page);
    }

    @Override
    public void toReportById(InspectionTask inspectionTask) {
        getSession().update("toReportById",inspectionTask);
    }

    @Override
    public List<InspectionTask> findAllInspection(Page page) {
        return getSession().selectList("findAllInspection",page);
    }

    @Override
    public List<InspectionTask> findAllInspections(Page page) {
        return getSession().selectList("findAllInspections",page);
    }

    @Override
    public int findAgenda(Long loginUser) {
        return getSession().selectOne("findAgenda",loginUser);
    }

    @Override
    public int updateInspectionInfo(InspectionTask inspectionTask) {
        return getSession().update("updateInspectionInfo", inspectionTask);
    }

    @Override
    public String getMaxNumer(String now) {
        String maxNum =  getSession().selectOne("getMaxNumer",now);
        if(maxNum != null && maxNum.length()>0){
            return maxNum.substring(9);
        }else{
            return null;
        }

    }
    //自动生成任务，
    @Override
    public int autoCreateTask(InspectionTask inspectionTask) {
        return getSession().insert("autoCreateTask",inspectionTask);
    }

    @Override
    public void updateInspectionType(Long id) {
        getSession().update("updateInspectionType",id);
    }

    @Override
    public void updateTimeOutState(Long id) {
        getSession().update("updateTimeOutState",id);
    }

    @Override
    public void insertInspectionFile(InspectionTaksFile inspectionTaksFile) {
        getSession().insert("insertInspectionFile",inspectionTaksFile);
    }

    @Override
    public List<InspectionTask> checkRepeat(Long planId, Long userId, String inspectionStartTime,String inspectionEndTime) {
        Map map = new HashMap();
        map.put("planId",planId);
        map.put("userId",userId);
        map.put("inspectionStartTime",inspectionStartTime);
        map.put("inspectionEndTime",inspectionEndTime);
        return getSession().selectList("checkRepeat",map);
    }

    @Override
    public List<InspectionTaksFile> findFilesByUserId(Long userId,String taskNumber) {
        Map map = new HashMap();
        map.put("userId",userId);
        map.put("taskNumber",taskNumber);
        return getSession().selectList("findFilesByUserId",map);
    }

    @Override
    public List<InspectionTask> findAllTaskByDay(String strDate,String endDate) {
        Map map = new HashMap();
        map.put("strDate",strDate);
        map.put("endDate",endDate);
        return getSession().selectList("findAllTaskByDay",map);
    }

    @Override
    public void insertFilePath(FilePathVo filePathVo) {
        getSession().insert("insertFilePath",filePathVo);
    }

    @Override
    public List<FilePathVo> getFilePathById(String id) {
        return getSession().selectList("getFilePathById",id);
    }

    @Override
    public int findByTimeType(Long planId) {
        return getSession().selectOne("findByTimeType",planId);
    }

    @Override
    public List<InspectionTask> selectInspectionTaskByPlanId(long planId) {
        return getSession().selectList("selectInspectionTaskByPlanId", planId);
    }

    @Override
    public void deleteInspectionReportByPlanId(Long planId) {
        getSession().delete("deleteInspectionReportByPlanId", planId);
    }

    @Override
    public int selectReportNumByQuery(ReportNumQuery query) {
        return getSession().selectOne("selectReportNumByQuery", query);
    }

    @Override
    public long selectCompleteNum(Date begin, Date end,String status) {
        Map<String,Object> param = new HashMap<>();
        param.put("begin",begin);
        param.put("end",end);
        param.put("status",status);
        return getSession().selectOne("selectCompleteNum",param);
    }

    @Override
    public List<Map<String, Object>> selectCompleteNumByUser(Date begin,Date end) {
        Map<String,Object> param = new HashMap<>();
        param.put("begin",begin);
        param.put("end",end);
        return getSession().selectList("selectCompleteNumByUser",param);
    }
}
