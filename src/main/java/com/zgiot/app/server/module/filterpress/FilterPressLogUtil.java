package com.zgiot.app.server.module.filterpress;

import com.zgiot.common.constants.FilterPressLogConstants;
import com.zgiot.common.exceptions.SysException;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilterPressLogUtil {


    public static synchronized boolean isDayShift(int start,int end){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, start);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, end);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long endTime = calendar.getTimeInMillis();
        if(System.currentTimeMillis() > startTime && System.currentTimeMillis() < endTime){
            return true;
        }
        return false;
    }

    public static Map<String,String> getCurrentDayAndNextOrPriorDay(int currentOffset,int nextOrPriorOffset){
        Map<String,String> dayMap = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, currentOffset);
        date = calendar.getTime();
        String currentDay = simpleDateFormat.format(date);
        dayMap.put(FilterPressLogConstants.CURRENT_DAY,currentDay);
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, nextOrPriorOffset);
        date = calendar.getTime();
        String nextOrPrior = simpleDateFormat.format(date);
        dayMap.put(FilterPressLogConstants.NEXT_OR_PRIOR_DAY,nextOrPrior);
        return dayMap;
    }

    public static boolean isPriorPartNightShift(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String day = simpleDateFormat.format(date);
        String nightShiftline = day + FilterPressLogConstants.NIGHT_SHIFT_LINE;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean isPriorOrNextPartNightShift = false;
        try{
            Date nightShiftTime = simpleDateFormat.parse(nightShiftline);
            if(date.getTime() < nightShiftTime.getTime()){
                isPriorOrNextPartNightShift = false;
            }else{
                isPriorOrNextPartNightShift = true;
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
        return isPriorOrNextPartNightShift;
    }

    public static Date getDayOrNightShiftRateStartTime(String attchTime,int currentOrPrior){
        if(StringUtils.isBlank(attchTime)){
            throw new SysException("ratedStartTimeOffset is null",SysException.EC_UNKNOWN);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String day = simpleDateFormat.format(date);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nightTimeLine = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOrNightShiftAttach = null;
        try{
            nightTimeLine = simpleDateFormat.parse(day + FilterPressLogConstants.NIGHT_SHIFT_LINE);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(date.getTime() < nightTimeLine.getTime() || currentOrPrior == 1){
                calendar.add(Calendar.DAY_OF_MONTH, FilterPressLogConstants.DAY_DEC_ONE);
            }
            dayOrNightShiftAttach = attchTime;
            String currentDay = simpleDateFormat.format(calendar.getTime()) + " " + dayOrNightShiftAttach;
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(currentDay);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isFirstLooseEveryDay(long priorLooseTime,long currentLooseTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDayStartTime = simpleDateFormat.format(currentLooseTime) + FilterPressLogConstants.FILTERPRESS_2492_RATE_TIME;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentDayStartTimeMills = 0;
        try{
            currentDayStartTimeMills = simpleDateFormat.parse(currentDayStartTime).getTime();
        }catch(ParseException e){
            throw new SysException("simpleDateFormat parse exception",SysException.EC_UNKNOWN);
        }
        if(priorLooseTime < currentDayStartTimeMills){
            return true;
        }
        return false;
    }
}
