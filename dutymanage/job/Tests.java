package com.vlinksoft.ves.dutymanage.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author:   nierf
 * Date:     2020/4/22 14:52
 */
public class Tests {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentDate);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse("2020-04-20"));
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY,1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//小时
        System.out.println(simpleDateFormat.format(calendar.getTime()));
    }
}
