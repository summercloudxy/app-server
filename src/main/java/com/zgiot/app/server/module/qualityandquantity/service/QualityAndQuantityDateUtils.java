package com.zgiot.app.server.module.qualityandquantity.service;

import com.zgiot.app.server.module.qualityandquantity.pojo.DutyInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class QualityAndQuantityDateUtils {
    @Value("${shift.time.day}")
    private int shiftTimeDay;
    @Value("${shift.time.night}")
    private int shiftTimeNight;
    public static final String DUTY_TYPE_DAY = "day";
    public static final String DUTY_TYPE_NIGHT = "night";

    public DutyInfo getCurrentDutyInfo(Date currentTime) {
        LocalDateTime currentLocalDateTime = toLocalDateTime(currentTime);
        int hour = currentLocalDateTime.getHour();
        if (hour < shiftTimeDay) {
            Date startTime = toDate(currentLocalDateTime.plusDays(-1).withHour(shiftTimeNight));
            Date endTime = toDate(currentLocalDateTime.withHour(shiftTimeDay));
            return new DutyInfo(startTime, endTime, DUTY_TYPE_NIGHT);
        } else if (hour < shiftTimeNight) {
            Date startTime = toDate(currentLocalDateTime.withHour(shiftTimeDay));
            Date endTime = toDate(currentLocalDateTime.withHour(shiftTimeNight));
            return new DutyInfo(startTime, endTime, DUTY_TYPE_DAY);
        } else {
            Date startTime = toDate(currentLocalDateTime.withHour(shiftTimeNight));
            Date endTime = toDate(currentLocalDateTime.plusDays(1).withHour(shiftTimeDay));
            return new DutyInfo(startTime, endTime, DUTY_TYPE_NIGHT);
        }
    }

    private Date toDate(LocalDateTime ldt) {
        return new Date(ldt.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli());
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date.getTime()),
                ZoneId.systemDefault());
    }
}
