package com.vlinksoft.ves.inspection.job;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.inspection.api.IInspectTaskApi;
import com.vlinksoft.ves.inspection.bo.InspectionTask;
import com.vlinksoft.ves.inspection.service.InspectTaskApiImpl;
import com.vlinksoft.ves.util.SpringBeanUtil;
import com.vlinksoft.ves.workflow.um.enumUtil.ItsmEventMessageType;
import com.vlinksoft.ves.workflow.um.eventmgr.api.IEventMessageApi;
import com.vlinksoft.ves.workflow.um.eventmgr.bo.ItsmEventMessage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author liushy <1522903818@qq.com>
 * @Version ves6.x
 * @Since 1.0
 * @Date 2018/11/29 0029
 */
public class InspectNotifyJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(InspectNotifyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        IEventMessageApi eventMessageApi = getEventMessageApi();
        if(eventMessageApi == null){
            if(logger.isErrorEnabled()){
                logger.error("cannt find eventMessageApi");
            }
            return;
        }
        long inspectTaskId = Long.parseLong(jobExecutionContext.getJobDetail().getKey().getName());
        IInspectTaskApi inspectTaskApi = SpringBeanUtil.getBean(IInspectTaskApi.class);
        InspectionTask task = inspectTaskApi.getTaskById(inspectTaskId);
        if(task == null){
            if(logger.isWarnEnabled()){
                logger.warn("inspacttask is not exist id={}",inspectTaskId);
            }
            return;
        }
        if(logger.isInfoEnabled()){
            logger.info("InspectNotify your task is going to done.id={}",task.getId());
        }
        sendNotifyMessage(getEventMessageApi(),task);
    }

    public static void sendNotifyMessage(IEventMessageApi messageApi,InspectionTask task){
        ItsmEventMessage message = new ItsmEventMessage();
        message.setCreateDate(new Date());
        message.setType(ItsmEventMessageType.inspect_remind.ordinal());
        message.setReceiveUser(task.getUserId());
        message.setCreateDate(new Date());
        JSONObject eventMessage = new JSONObject();
        eventMessage.put("taskId",task.getId());
        eventMessage.put("title",task.getInspectionReport());
        eventMessage.put("taskNumber",task.getTaskSheetId());
        Date toExecuteDate = null;
//        try {
//            toExecuteDate = new SimpleDateFormat(InspectTaskApiImpl.TASK_TIME_FORMAT).parse(task.getInspectionStartTime().toString());
//        } catch (ParseException e) {
//            if(logger.isErrorEnabled()){
//                logger.error("createTaskNotifyJob InspectionTime format error。id="+task.getId()+" task.getInspectionTime()"+task.getInspectionStartTime(),e);
//            }
//            return;
//        }
        //jwt 报告开始时间是整分的Date格式，不需要转换
        toExecuteDate = task.getInspectionStartTime();
        eventMessage.put("overTime",((toExecuteDate.getTime()-System.currentTimeMillis())/60000));
        message.setMessage(eventMessage.toJSONString());
        messageApi.insertMessage(message);
        //TODO:send email and sms
    }

    private IEventMessageApi getEventMessageApi(){
        IEventMessageApi eventMessageApi = SpringBeanUtil.getBean(IEventMessageApi.class);
        if(eventMessageApi == null){
            if(logger.isWarnEnabled()){
                logger.warn("eventMessageApi is null.system is not started ok.wait one minute.");
            }
            for (int i = 0; i < 60; i++) {
                eventMessageApi = SpringBeanUtil.getBean(IEventMessageApi.class);
                if(eventMessageApi != null){
                    break;
                }
                synchronized (this){
                    try {
                        this.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return eventMessageApi;
    }
}