package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.alert.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xiayun on 2017/9/26.
 */
@Mapper
public interface AlertMapper {
    void createAlertDate(AlertData alertData);

    void updateAlertDate(AlertData alertData);

    List<AlertRule> getAlertRuleList(@Param("alertType") int alertType);

    List<AlertData> getCurrentAlertData();

    List<MetricAlertType> getMetricAlertType();

    void saveAlertMessage(AlertMessage alertMessage);

    List<AlertMessage> getAlertMessage(int alertId);

    void setRead(int messageId);

    List<AlertRecord> getAlertDataList(@Param("stage") String stage, @Param("levels") List<Integer> levels,
            @Param("types") List<Short> types, @Param("buildingIds") List<Integer> buildingIds,
            @Param("floors") List<Integer> floors, @Param("systems") List<Integer> systems,
            @Param("sortType") Integer sortType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
