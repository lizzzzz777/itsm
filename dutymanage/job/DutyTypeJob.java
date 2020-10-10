package com.vlinksoft.ves.dutymanage.job;

import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.bo.DutyLog;
import com.vlinksoft.ves.dutymanage.bo.DutyMainUserVo;
import com.vlinksoft.ves.dutymanage.bo.DutyTypeJobVo;
import com.vlinksoft.ves.util.SpringBeanUtil;
import org.quartz.*;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.vlinksoft.ves.util.SpringBeanUtil.*;

@Component
public class DutyTypeJob implements Job{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DutyTypeJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        DutyTypeJobVo dutyTypeJobVo = (DutyTypeJobVo) context.getMergedJobDataMap().get("DutyTypeJobVo");
        invokMethod(dutyTypeJobVo);
    }

    public static void invokMethod(DutyTypeJobVo dutyTypeJobVo) {
            SchedulerFactoryBean schedulerFactoryBean = SpringBeanUtil.getBean(SchedulerFactoryBean.class);
            IDutyManageApi iDutyManageApi = SpringBeanUtil.getBean(IDutyManageApi.class);
            logger.info("dutyTypeJobVo success----------------------------");
            System.out.println(dutyTypeJobVo.toString());
            Date currentDate = new Date();
//            查询上班中的用户
            List<DutyMainUserVo> list  = iDutyManageApi.findUserListByDutyIdAndTypeId(dutyTypeJobVo.getDutyId(),dutyTypeJobVo.getTypeId(),1);
            List<Long> dutyMainUserIds = list.stream().map(DutyMainUserVo::getId).collect(Collectors.toList());
            if (!dutyMainUserIds.isEmpty()){
                iDutyManageApi.batchOffDutyList(dutyMainUserIds,currentDate,2);//批量下班和下班时间
            }
            for(DutyMainUserVo item:list){
                DutyLog dutyLog = new DutyLog();
                dutyLog.setDutyId(item.getDutyId());
                dutyLog.setDutyTypeId(item.getDutyTypeId());
                dutyLog.setSubDutyUserId(item.getTypeUserId());
                dutyLog.setLogStatus(1);
                iDutyManageApi.updateDutyLogByDutyTypeUser(dutyLog);
            }
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(dutyTypeJobVo.getTypeId()+"", dutyTypeJobVo.getDutyId()+"");
            try {
                scheduler.deleteJob(jobKey);
            } catch (SchedulerException e1) {
                e1.printStackTrace();
            }
//            threadPoolTaskExecutor.execute(new Thread(String.valueOf(id)){
//                public void run() {
//
//                }
//            });
    }

}
