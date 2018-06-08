package com.zgiot.app.server.module.sfstop.util;

import java.util.Date;

public class DateTimeUtils {

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param timeFormat 日期格式 yyyy-MM-dd HH:mm:ss
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static Long getDifferenceTime(Date strTime1, Date strTime2, String timeFormat) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long time1 = strTime1.getTime();
        long time2 = strTime2.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return diff;
    }
}
