package com.vlinksoft.ves.dutymanage.job;

import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.bo.DutyTypeJobVo;
import org.quartz.SchedulerException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author:   nierf
 * Date:     2020/4/22 15:27
 */
@Component("dutyJob")
public class DutyJobEvery {

    @Scheduled(cron = "0 0 0 * * ?")
    public void dutyJobEveryTask() throws ParseException, SchedulerException {
        InitDutyJob.init();
    }
}
