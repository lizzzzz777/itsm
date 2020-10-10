package com.vlinksoft.ves.dutymanage.job;

import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.bo.DutyTypeJobVo;
import com.vlinksoft.ves.util.SpringBeanUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InitDutyJob implements ApplicationContextAware {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InitDutyJob.class);
    private static ApplicationContext appCtx;
    public static SchedulerFactoryBean schedulerFactoryBean = null;


    //    InitQuartzJob 是程序初始化定时任务
//    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        if (this.appCtx == null) {
            this.appCtx = applicationContext;
        }
    }
    public static String createCronExpretion(Date dutyDate, Date currentDate, int endHour, Integer acrossDay){
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dutyDate);
        calendar.set(Calendar.HOUR_OF_DAY, endHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY,1);
        if (acrossDay.equals(1)){//跨天
            calendar.add(Calendar.DAY_OF_MONTH, 1);// 增加一天
        }else{
            if (currentCal.get(Calendar.HOUR_OF_DAY)>endHour){
                return null;
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//小时
        return "0 0 "+hour+" "+day+" "+month+" ? "+year;
    }

    public static void init() throws SchedulerException, ParseException {
        IDutyManageApi iDutyManageApi = SpringBeanUtil.getBean(IDutyManageApi.class);
        schedulerFactoryBean = (SchedulerFactoryBean) appCtx.getBean(SchedulerFactoryBean.class);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            logger.info(scheduler.getSchedulerName());
        } catch (SchedulerException e1) {
//             TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DutyTypeJobVo> typeList = iDutyManageApi.findDutyMainTypeListByCDate(sdf.format(currentDate));
        for(DutyTypeJobVo dutyTypeJobVo:typeList){
            int endHour = Integer.parseInt(dutyTypeJobVo.getEndTime());
            String cron = createCronExpretion(dutyTypeJobVo.getDutyDate(),currentDate,endHour,dutyTypeJobVo.getAcrossDay());
            if (StringUtils.isBlank(cron)){
                continue;
            }
            dutyTypeJobVo.setCron(cron);
            addDutyJob(dutyTypeJobVo);
        }
    }


    public static void addDutyJob(DutyTypeJobVo dutyTypeJobVo) throws SchedulerException{
        if (dutyTypeJobVo==null){
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        String typeId =dutyTypeJobVo.getTypeId()+"";
        String dutyId = dutyTypeJobVo.getDutyId()+"";
        TriggerKey triggerKey = TriggerKey.triggerKey(typeId,dutyId);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 如果不存在则创建
        if (trigger==null) {
            logger.info("start add dutyManage----------------Job");
            JobDetail jobDetail = JobBuilder.newJob(DutyTypeJob.class).withIdentity(typeId, dutyId).usingJobData("data",dutyId+"-"+typeId).build();
            jobDetail.getJobDataMap().put("DutyTypeJobVo", dutyTypeJobVo);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(dutyTypeJobVo.getCron());
            trigger = TriggerBuilder.newTrigger().withDescription(dutyId+"-"+typeId)
                    .withIdentity(typeId, dutyId).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            logger.info("start update dutyManage----------------Job");
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(dutyTypeJobVo.getCron());
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).usingJobData("data", dutyId+"-"+typeId)
                    .withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }

    }


}
