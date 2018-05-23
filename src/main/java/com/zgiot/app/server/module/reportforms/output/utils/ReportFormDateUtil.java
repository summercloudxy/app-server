package com.zgiot.app.server.module.reportforms.output.utils;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class ReportFormDateUtil {


    /**
     * 获取给定日期的当班开始时间
     *
     * @param date
     * @return
     */
    public static Date getNowDutyStartTime(Date date) {
        //获取给定日期的早上8点
        Calendar daytimeCal = Calendar.getInstance();
        daytimeCal.setTime(date);
        daytimeCal.set(Calendar.HOUR_OF_DAY, 8);
        daytimeCal.set(Calendar.SECOND, 0);
        daytimeCal.set(Calendar.MINUTE, 0);
        daytimeCal.set(Calendar.MILLISECOND, 0);
        Date daytimeDate = daytimeCal.getTime();

        //获取给定日期的晚上8点
        Calendar nightCal = Calendar.getInstance();
        nightCal.setTime(date);
        nightCal.set(Calendar.HOUR_OF_DAY, 20);
        nightCal.set(Calendar.SECOND, 0);
        nightCal.set(Calendar.MINUTE, 0);
        nightCal.set(Calendar.MILLISECOND, 0);
        Date nightDate = nightCal.getTime();

        if (date.before(daytimeDate)) {
            //在当天日期8点之前
            return DateUtils.addDays(nightDate, -1);
        } else {
            //当天日期在8点之后
            int param = date.compareTo(nightDate);
            if (param < 0) {
                return daytimeDate;
            } else {
                return nightDate;
            }
        }
    }

    /**
     * 判断给定的当班开始时间是否为当前班次
     *
     * @param dutyStartTime
     * @return
     */
    public static boolean isCurrentDuty(Date dutyStartTime) {
        Date nowDutyStartTime = getNowDutyStartTime(new Date());
        return dutyStartTime.equals(nowDutyStartTime);
    }

    /**
     * 获取给定日期的前一个当班开始时间
     *
     * @param date
     * @return
     */
    public static Date getbeforeDutyStartTime(Date date) {
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(date);
        return DateUtils.addHours(nowDutyStartTime, -12);
    }

    /**
     * 判断当前班次开始是否是当月第一班
     *
     * @param date
     * @return
     */
    public static Boolean isMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1 && calendar.get(Calendar.HOUR_OF_DAY) == 8) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前班次开始是否是当年第一班
     *
     * @param date
     * @return
     */
    public static Boolean isYearFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1 && calendar.get(Calendar.DATE) == 1 && calendar.get(Calendar.HOUR_OF_DAY) == 8) {
            return true;
        }
        return false;
    }

    /**
     * 判断两个时间是否在同一年
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static Boolean isYearSame(Date beforeDate,Date afterDate){
        Calendar beforeCal=Calendar.getInstance();
        beforeCal.setTime(beforeDate);
        Calendar afterCal=Calendar.getInstance();
        afterCal.setTime(afterDate);
        return beforeCal.get(Calendar.YEAR)==afterCal.get(Calendar.YEAR);
    }

    /**
     * 判断两个时间是否在用一年的同一月
     * @param beforeDate
     * @param afterDate
     * @return
     */
    public static Boolean isMonthSame(Date beforeDate,Date afterDate){
        Calendar beforeCal=Calendar.getInstance();
        beforeCal.setTime(beforeDate);
        Calendar afterCal=Calendar.getInstance();
        afterCal.setTime(afterDate);
        if(beforeCal.get(Calendar.YEAR)==afterCal.get(Calendar.YEAR) && beforeCal.get(Calendar.MONTH)==afterCal.get(Calendar.MONTH)){
            return true;
        }
        return false;
    }


    /**
     * 判断当前班是否为白班
     *
     * @param dutyStartTime
     * @return
     */
    public static Boolean isDayShift(Date dutyStartTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dutyStartTime);
        return calendar.get(Calendar.HOUR_OF_DAY) == 8;
    }

    /**
     * 获取给定日期的当班结束时间
     * @param date
     * @return
     */
    public static Date getDutyEndStartTime(Date date){
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(date);
        return DateUtils.addHours(nowDutyStartTime,12);
    }

}
