package com.zgiot.app.server.module.filterpress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilterPressLogUtil {
    public static final String CURRENT_DAY = "currentDay";
    public static final String NEXT_DAY = "nextDay";

    public static synchronized boolean isDayShift(int start,int end){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, start);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startHour = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, end);
        long endHour = calendar.getTimeInMillis();
        if(System.currentTimeMillis() > startHour && System.currentTimeMillis() < endHour){
            return true;
        }
        return false;
    }

    public static Map<String,String> getCurrentDayAndNext(){
        Map<String,String> dayMap = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String currentDay = simpleDateFormat.format(date);
        dayMap.put(CURRENT_DAY,currentDay);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
        date = calendar.getTime();
        String nextDay = simpleDateFormat.format(date);
        dayMap.put(NEXT_DAY,nextDay);
        return dayMap;
    }
}
