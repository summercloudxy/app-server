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
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date time = calendar.getTime();
        if(date.before(time)){
            return true;
        }

        Date dateMonth = DateUtils.addMonths(time, 1);
        Date lastMonth = DateUtils.addHours(dateMonth, -12);
        if(!date.before(lastMonth)){
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

        if(calendar.get(Calendar.MONTH)==0){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.HOUR_OF_DAY,8);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(date.before(calendar.getTime())){
                return true;
            }
        }

        if(calendar.get(Calendar.MONTH)==11){
            calendar.set(Calendar.DAY_OF_MONTH,31);
            calendar.set(Calendar.HOUR_OF_DAY,20);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(!date.before(calendar.getTime())){
                return true;
            }
        }
        return false;

    }

    /**
     * 判断两个时间是否在同一年
     * @param beforeDate 这里必须是之前数据
     * @param afterDate
     * @return
     */
    public static Boolean isYearSame(Date beforeDate,Date afterDate){
        Calendar beforeCal=Calendar.getInstance();
        beforeCal.setTime(beforeDate);
        Calendar afterCal=Calendar.getInstance();
        afterCal.setTime(afterDate);
        if(beforeCal.get(Calendar.YEAR)==afterCal.get(Calendar.YEAR)){
            return true;
        }
        Date date = DateUtils.addHours(beforeDate, 4);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(calendar.get(Calendar.YEAR)==afterCal.get(Calendar.YEAR)){
            return true;
        }
        return false;
    }

    /**
     * 判断两个时间是否在用一年的同一月
     * @param beforeDate 这里必须是之前的数据
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

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(DateUtils.addHours(beforeDate, 4));
        if(calendar.get(Calendar.YEAR)==afterCal.get(Calendar.YEAR) && calendar.get(Calendar.MONTH)==afterCal.get(Calendar.MONTH)){
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


    /**
     * 获取给定日期的产品开始时间,如果刚好16:00返回给定时间
     *
     * @param date
     * @return
     */
    public static Date getProductStartTime(Date date) {
        //获取给定日期16点
        Calendar daytimeCal = Calendar.getInstance();
        daytimeCal.setTime(date);
        daytimeCal.set(Calendar.HOUR_OF_DAY, 16);
        daytimeCal.set(Calendar.SECOND, 0);
        daytimeCal.set(Calendar.MINUTE, 0);
        daytimeCal.set(Calendar.MILLISECOND, 0);
        Date daytimeDate = daytimeCal.getTime();

        if (daytimeDate.after(date)) {
            return DateUtils.addDays(daytimeDate,-1);
        } else {
            return daytimeDate;
        }
    }

    /**
     * 获取给定日期的产品结束时间,如果如果刚好16:00则返回下一天的16:00
     *
     * @param date
     * @return
     */
    public static Date getProductEndTime(Date date) {
        //获取给定日期的早上8点
        Calendar daytimeCal = Calendar.getInstance();
        daytimeCal.setTime(date);
        daytimeCal.set(Calendar.HOUR_OF_DAY, 16);
        daytimeCal.set(Calendar.SECOND, 0);
        daytimeCal.set(Calendar.MINUTE, 0);
        daytimeCal.set(Calendar.MILLISECOND, 0);
        Date daytimeDate = daytimeCal.getTime();

        if (daytimeDate.after(date)) {
            return daytimeDate;
        } else {
            return DateUtils.addDays(daytimeDate,1);
        }
    }

    /**
     * 获取给定日期和当前日期的产品当班开始时间是否相等
     *
     * @param date
     * @return
     */
    public static Boolean isProductCurrentDuty(Date date) {
        Date productStartTime = getProductStartTime(date);
        Date nowProductStartTime = getProductStartTime(new Date());
        return nowProductStartTime.equals(productStartTime);
    }

    public static int getDaysOfMonth(Date date) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 判断当前班次开始是否是当月第一班产品
     * @param date
     * @return
     */
    public static Boolean isProductMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,16);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date time = calendar.getTime();
        if(date.before(time)){
            return true;
        }

        Date dateMonth = DateUtils.addMonths(time, 1);
        Date lastMonth = DateUtils.addHours(dateMonth, -24);
        if(!date.before(lastMonth)){
            return true;
        }
        return false;
    }


    /**
     * 判断当前班次开始是否是当年第一班产品
     *
     * @param date
     * @return
     */
    public static Boolean isProductYearFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if(calendar.get(Calendar.MONTH)==0){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.HOUR_OF_DAY,16);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(date.before(calendar.getTime())){
                return true;
            }
        }

        if(calendar.get(Calendar.MONTH)==11){
            calendar.set(Calendar.DAY_OF_MONTH,31);
            calendar.set(Calendar.HOUR_OF_DAY,16);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(!date.before(calendar.getTime())){
                return true;
            }
        }
        return false;

    }


    /**
     * 获取当前班次开始的当月第一班产品
     *
     * @param date
     * @return
     */
    public static Date getProductMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,16);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date time = calendar.getTime();

        Date dateMonth = DateUtils.addMonths(time, 1);
        Date lastMonth = DateUtils.addHours(dateMonth, -24);
        if(!date.before(lastMonth)){
            return lastMonth;
        }

        return DateUtils.addHours(time,-24);
    }


    /**
     * 获取当前班次开始的当年第一班产品
     *
     * @param date
     * @return
     */
    public static Date getProductYearFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if(calendar.get(Calendar.MONTH)==11){
            calendar.set(Calendar.DAY_OF_MONTH,31);
            calendar.set(Calendar.HOUR_OF_DAY,16);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(!date.before(calendar.getTime())){
                return calendar.getTime();
            }
        }

        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,16);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return DateUtils.addHours(calendar.getTime(),-24);

    }


}
