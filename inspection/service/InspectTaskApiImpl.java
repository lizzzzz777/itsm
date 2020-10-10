package com.vlinksoft.ves.inspection.service;

import com.vlinksoft.ves.inspection.api.IInspectTaskApi;
import com.vlinksoft.ves.inspection.bo.FilePathVo;
import com.vlinksoft.ves.inspection.bo.InspectionTaksFile;
import com.vlinksoft.ves.inspection.bo.InspectionTask;
import com.vlinksoft.ves.inspection.bo.ReportNumQuery;
import com.vlinksoft.ves.inspection.dao.InspectTaskDao;
import com.vlinksoft.ves.inspection.job.InspectNotifyJob;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.sequence.service.ISequence;
import com.vlinksoft.ves.platform.sequence.service.SequenceFactory;
import com.vlinksoft.ves.workflow.um.eventmgr.api.IEventMessageApi;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;


@Service
public class InspectTaskApiImpl implements IInspectTaskApi{

    private static final Logger logger = LoggerFactory.getLogger(IInspectTaskApi.class);

    private static final String NOTIFY_CRON_PATTERN="0 m H d M ? YYYY";
    public static final String TASK_TIME_FORMAT="yyyy-MM-dd HH:mm";

    @Resource
    private InspectTaskDao inspectTaskDao;


    @Resource
    private Scheduler scheduler;

    private ISequence idSequence;

    @Resource
    private SequenceFactory sequenceFactory;

    @Resource
    private IEventMessageApi eventMessageApi;

    @PostConstruct
    public void start(){
        idSequence = sequenceFactory.getSeq("InspectTaskPrimaryKey");
    }

    //获取任务编号
    public String getMaxNumber(){
        SimpleDateFormat sd = new SimpleDateFormat("YYMMdd");
        String now  = sd.format(new Date());
        String maxNum = inspectTaskDao.getMaxNumer(now);
        if(maxNum != null && !"".equals(maxNum)){
            return "TK"+now+String.format("%04d",Integer.valueOf(maxNum)+1);
        }else {
            return "TK"+now+"0001";
        }
    }

    //新建任务
    @Override
    public Map<String,Long> addTask(InspectionTask jsonObj) {
        String taskNumber = getMaxNumber();
        jsonObj.setTaskSheetId(taskNumber);
        jsonObj.setId(idSequence.next());
        int success=  inspectTaskDao.addTask(jsonObj);
        /*if(success ==1 && jsonObj.getIsPublish() == 1){
            createTaskNotifyJob(jsonObj.getId());
        }*/

        Map<String,Long>map=new HashMap<>(2);
        map.put("id",jsonObj.getId());
        map.put("success",(long)success);

        return map;
    }

    private void createTaskNotifyJob(Long id){
        InspectionTask task = getTaskById(id);
        Date toExecuteDate = null;
//        try {
//            toExecuteDate = new SimpleDateFormat(TASK_TIME_FORMAT).parse(task.getInspectionStartTime().toString());
        //jwt 报告开始时间是整分的Date格式，不需要转换
            toExecuteDate = task.getInspectionStartTime();
//        } catch (ParseException e) {
//            if(logger.isErrorEnabled()){
//                logger.error("createTaskNotifyJob InspectionTime format error。id="+id+" task.getInspectionTime()"+task.getInspectionStartTime(),e);
//            }
//            return;
//        }
        long time = task.getRemindTime()*60000;
        Date notifyDate = new Date(toExecuteDate.getTime()-time);
        if(notifyDate.getTime()>System.currentTimeMillis()){
            JobKey key = new JobKey(String.valueOf(id),"InspectNotifyJob");
            try {
                JobDetail oldDetail = scheduler.getJobDetail(key);
                if(oldDetail!=null){
                    if(logger.isWarnEnabled()){
                        logger.warn("createTaskNotifyJob Job has exist.id={} title={}",task.getId(),task.getInspectionReport());
                    }
                    return;
                }
            } catch (Exception e) {
                if(logger.isErrorEnabled()){
                    logger.error("createTaskNotifyJob error",e);
                }
                return;
            }
            JobDetail jobDetail = JobBuilder.newJob(InspectNotifyJob.class).withIdentity(key).build();
            SimpleDateFormat dateFormat = new SimpleDateFormat(NOTIFY_CRON_PATTERN);
            String cron = dateFormat.format(notifyDate);
            Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            if(logger.isInfoEnabled()){
                logger.info("createTaskNotifyJob id={} title={} cron={}",task.getId(),task.getInspectionReport(),cron);
            }
            try {
                scheduler.scheduleJob(jobDetail,trigger);
            } catch (Exception e) {
                if(logger.isErrorEnabled()){
                    logger.error("createTaskNotifyJob error id="+id,e);
                }
            }
        }else{
            if(logger.isWarnEnabled()){
                logger.warn("job notify date has been outoftime.id={} title={}",task.getId(),task.getInspectionReport());
            }
            InspectNotifyJob.sendNotifyMessage(eventMessageApi,task);
        }
    }

    private void removeJob(Long id){
        JobKey key = new JobKey(String.valueOf(id),"InspectNotifyJob");
        try {
            scheduler.deleteJob(key);
        } catch (SchedulerException e) {
            if(logger.isErrorEnabled()){
                logger.error("removeJob error.id="+id,e);
            }
        }
    }
    private void removeJob(Long[] ids){
        if(ids!=null && ids.length>0){
            List<JobKey> keys = new ArrayList<>(ids.length);
            for (Long id:ids
            ) {
                JobKey key = new JobKey(String.valueOf(id),"InspectNotifyJob");
                keys.add(key);
            }
            try {
                scheduler.deleteJobs(keys);
            } catch (SchedulerException e) {
                if(logger.isErrorEnabled()){
                    logger.error("removeJob error.ids="+ Arrays.toString(ids),e);
                }
            }
        }
    }

    @Override
    public List<InspectionTask> findAllTask(Page page) {
        return inspectTaskDao.findAllTask(page);
    }
    //发布任务
    @Override
    public int publishTask(Long id) {
        int success= inspectTaskDao.publishTask(id);
        if(success ==1 ){
            createTaskNotifyJob(id);
        }
        return success;
    }
    //删除任务
    @Override
    public void delTask(Long[] ids) {
        inspectTaskDao.delTask(ids);
        removeJob(ids);
    }
    //获取任务详情
    @Override
    public InspectionTask getTaskById(Long id) {
        return inspectTaskDao.getTaskById(id);
    }
    //修改任务
    @Override
    public void updateTaskById(InspectionTask inspectionTask) {
        inspectTaskDao.updateTaskById(inspectionTask);
    }
    //查找我的所有任务
    @Override
    public List<InspectionTask> findAllMyTaskByUserId(Page page) {
        return inspectTaskDao.findAllMyTaskByUserId(page);
    }

    @Override
    public List<InspectionTask> findAfterAllMyTaskByUserId(Page page) {
        return inspectTaskDao.findAfterAllMyTaskByUserId(page);
    }

    @Override
    public List<InspectionTask> findAfterAllMyTaskByAdminId(Page page) {
        return inspectTaskDao.findAfterAllMyTaskByAdminId(page);
    }

    //提交报告
    @Override
    public void toReportById(InspectionTask inspectionTask) {
        inspectTaskDao.toReportById(inspectionTask);
    }

    @Override
    public List<InspectionTask> findAllInspection(Page page) {
        return inspectTaskDao.findAllInspection(page);
    }
    @Override
    public List<InspectionTask> findAllInspections(Page page) {
        return inspectTaskDao.findAllInspections(page);
    }

    @Override
    public int findAgenda(Long loginUser) {
        return inspectTaskDao.findAgenda(loginUser);
    }

    @Override
    public String getMaxNumer(String now) {
        return inspectTaskDao.getMaxNumer(now);
    }
    @Transactional
    @Override
    public int autoCreateTask(InspectionTask inspectionTask) {
        String taskNumber = getMaxNumber();
        inspectionTask.setTaskSheetId(taskNumber);
        inspectionTask.setId(idSequence.next());
        int success = inspectTaskDao.autoCreateTask(inspectionTask);
        if(success ==1 && inspectionTask.getIsInsider() == 1){
            createTaskNotifyJob(inspectionTask.getId());
        }
        return success;
    }

    @Override
    public void updateInspectionType(Long id) {
        inspectTaskDao.updateInspectionType(id);
        removeJob(id);
    }

    @Override
    public void updateTimeOutState(Long id) {
        inspectTaskDao.updateTimeOutState(id);
        removeJob(id);
    }

    @Override
    public void insertInspectionFile(InspectionTaksFile inspectionTaksFile) {
        inspectTaskDao.insertInspectionFile(inspectionTaksFile);
    }

    @Override
    public boolean checkRepeat(Long planId, Long userId, String inspectionStartTime,String inspectionEndTime) {
        List<InspectionTask> inspectionList = inspectTaskDao.checkRepeat(planId,userId,inspectionStartTime,inspectionEndTime);
        if(inspectionList.size() > 0){
            return true;
        }
        return false;

    }

    @Override
    public List<InspectionTaksFile>findFilesByUserId(Long userId,String taskNumber) {
        return inspectTaskDao.findFilesByUserId(userId,taskNumber);
    }


    //查询今天所有的任务
    public List<InspectionTask> findAllTaskByDay(Date today){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(today);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        Date startTime = startCalendar.getTime();
        String staDate = sd.format(startTime);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(today);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        Date endTime = endCalendar.getTime();
        String endDate = sd.format(endTime);
        return inspectTaskDao.findAllTaskByDay(staDate,endDate);
    }

    @Override
    public void insertFilePath(FilePathVo filePathVo) {
        filePathVo.setId(idSequence.next());
        inspectTaskDao.insertFilePath(filePathVo);
    }

    @Override
    public List<FilePathVo> getFilePathById(String id) {
        return inspectTaskDao.getFilePathById(id);
    }

    @Override
    public int findByTimeType(Long planId) {
        return inspectTaskDao.findByTimeType(planId);
    }

    @Override
    public int updateInspectionInfo(InspectionTask inspectionTask) {
        return inspectTaskDao.updateInspectionInfo(inspectionTask);
    }

    @Override
    public List<InspectionTask> selectInspectionTaskByPlanId(long planId) {
        return inspectTaskDao.selectInspectionTaskByPlanId(planId);
    }

    @Override
    public void deleteInspectionReportByPlanId(Long planId) {
        inspectTaskDao.deleteInspectionReportByPlanId(planId);
    }

    @Override
    public int selectReportNumByQuery(ReportNumQuery query) {
        return inspectTaskDao.selectReportNumByQuery(query);
    }

    @Override
    public long selectCompleteNum(Date begin,Date end,String status) {
        return inspectTaskDao.selectCompleteNum(begin,end,status);
    }

    @Override
    public List<Map<String, Object>> selectCompleteNumByUser(Date begin,Date end) {
        return inspectTaskDao.selectCompleteNumByUser(begin,end);
    }
}
