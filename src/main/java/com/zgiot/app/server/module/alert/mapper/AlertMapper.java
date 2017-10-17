package com.zgiot.app.server.module.alert.mapper;

import com.zgiot.app.server.module.alert.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by xiayun on 2017/9/26.
 */
@Mapper
public interface AlertMapper {
    void createAlertDate(AlertData alertData);

    void updateAlertDate(AlertData alertData);

    List<AlertRule> getAlertRuleList(@Param("alertType") int alertType, @Param("assetType") Integer assetType,
            @Param("category") String category, @Param("system") String system, @Param("metricType") String metricType,
            @Param("thingCode") String thingCode, @Param("enable") Boolean enable, @Param("level") Integer level,
            @Param("metricCode") String metricCode, @Param("buildingId") Integer buildingId);

    List<AlertRule> getWholeAlertRuleList(@Param("alertType") int alertType);

    List<AlertData> getCurrentAlertData();

    List<MetricAlertType> getMetricAlertType();

    void saveAlertMessage(AlertMessage alertMessage);

    List<AlertMessage> getAlertMessage(int alertId);

    void setRead(int messageId);

    void saveAlertShield(List<AlertMask> alertMasks);

    List<AlertRecord> getAlertDataListGroupByThing(@Param("stage") String stage, @Param("levels") List<Integer> levels,
            @Param("types") List<Short> types, @Param("buildingIds") List<Integer> buildingIds,
            @Param("floors") List<Integer> floors, @Param("systems") List<Integer> systems,
            @Param("assetType") String assetType, @Param("category") String category,
            @Param("sortType") Integer sortType, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("thingCode") String thingCode, @Param("offset") Integer offset, @Param("count") Integer count);

    List<AlertData> getAlertDataList(@Param("stage") String stage, @Param("level") Integer level,
            @Param("type") Short type, @Param("system") Integer system,
            @Param("assetType") String assetType, @Param("category") String category,
            @Param("sortType") Integer sortType, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("thingCode") String thingCode, @Param("offset") Integer offset, @Param("count") Integer count);

    List<AlertMask> getAlertShieldInfo(String thingCode, String metricCode, Date startTime, Date endTime);

    void insertAlertRule(List<AlertRule> alertRules);

    void updateAlertRule(List<AlertRule> alertRules);

    void deleteAlertRule(List<AlertRule> alertRules);

    /**
     * 获取报警数量统计信息
     * @param type  0：统计全部报警数量   1：统计每个设备的报警数量
     * @param alertStage
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlertStatisticsNum> getStatisticsInfo(@Param("type") int type, @Param("alertStage") String alertStage,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 获取不同类型报警的数量统计信息
     * @param alertType  0.故障 1.参数 2.保护 3.人工
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlertStatisticsNum> getTypeStatisticsInfo(@Param("alertType")Short alertType,@Param("startTime")Date startTime ,@Param("endTime")Date endTime);

    List<AlertRepairStatistics> getRepairStatisticsInfo(@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("alertLevel")Short alertLevel);
}
