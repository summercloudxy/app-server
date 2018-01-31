package com.zgiot.app.server.module.alert.mapper;

import com.zgiot.app.server.module.alert.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

/**
 * Created by xiayun on 2017/9/26.
 */
@Mapper
public interface AlertMapper {
    void createAlertData(AlertData alertData);

    void createAlertDataBackup(AlertData alertData);

    void updateAlertData(AlertData alertData);

    void updateAlertDataBackup(AlertData alertData);

    void releaseAlertData(AlertData alertData);

    void releaseAlertDataBackup(AlertData alertData);

    List<ThingAlertRule> getProtAlertRuleList(@Param("filter") FilterCondition filterCondition);

    Integer getProtAlertRuleCount(@Param("filter") FilterCondition filterCondition);

    List<ThingAlertRule> getParamAlertConfList(@Param("filter") FilterCondition filterCondition);

    List<AlertRule> getAlertRuleList(@Param("thingCode") String thingCode, @Param("metricCode") String metricCode, @Param("filter") FilterCondition filterCondition);

    Integer getParamAlertConfSize(@Param("filter") FilterCondition filterCondition);

    List<AlertRule> getWholeAlertRuleList(@Param("alertType") int alertType);

    List<AlertData> getCurrentAlertData();

    List<MetricAlertType> getMetricAlertType();

    void saveAlertMessage(AlertMessage alertMessage);

    List<AlertMessage> getAlertMessage(int alertId);

    void setRead(@Param("messageIds") List<Integer> messageIds, @Param("state") int state);

    void saveAlertShield(List<AlertMask> alertMasks);

    List<AlertRecord> getAlertDataListGroupByThing(@Param("stage") String stage, @Param("excluStage") String excluStage,
                                                   @Param("levels") List<Integer> levels, @Param("types") List<Short> types,
                                                   @Param("buildingIds") List<Integer> buildingIds, @Param("floors") List<Integer> floors,
                                                   @Param("systems") List<Integer> systems, @Param("assetType") String assetType,
                                                   @Param("category") String category, @Param("sortType") Integer sortType, @Param("startTime") Date startTime,
                                                   @Param("endTime") Date endTime, @Param("thingCode") String thingCode, @Param("offset") Integer offset,
                                                   @Param("count") Integer count);

    Integer getAlertDataListCount(FilterCondition filterCondition);

    //    List<AlertData> getAlertDataList(@Param("stage") String stage, @Param("excluStage") String excluStage,
////            @Param("level") Integer level, @Param("type") Short type, @Param("system") Integer system,
////            @Param("assetType") String assetType, @Param("category") String category,
////            @Param("sortType") Integer sortType, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
////            @Param("thingCode") String thingCode, @Param("offset") Integer offset, @Param("count") Integer count);
    List<AlertData> getAlertDataList(FilterCondition filterCondition);

    List<AlertMaskInfo> getAlertMaskInfo(FilterCondition filterCondition);

    List<AlertMaskStatistics> getMaskStatisticsInfo(FilterCondition filterCondition);

    Integer getMaskStatisticsInfoCount(FilterCondition filterCondition);

    void insertAlertRules(List<AlertRule> alertRules);

    void updateAlertRules(List<AlertRule> alertRules);

    void deleteAlertRules(List<Long> alertRules);

    /**
     * 获取报警数量统计信息
     *
     * @param type       0：统计全部报警数量 1：统计每个设备的报警数量
     * @param alertStage
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlertStatisticsNum> getStatisticsInfo(@Param("type") int type, @Param("alertStage") String alertStage,
                                               @Param("excluStage") String excluStage, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<AlertLevelNum> getLevelStatisticsInfo(@Param("type") int type, @Param("alertStage") String alertStage,
                                               @Param("excluStage") String excluStage, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 获取不同类型报警的数量统计信息
     *
     * @param alertType 0.故障 1.参数 2.保护 3.人工
     * @param startTime
     * @param endTime
     * @return
     */
    List<AlertStatisticsNum> getTypeStatisticsInfo(@Param("alertType") Short alertType,
                                                   @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<AlertRepairStatistics> getRepairStatisticsInfo(@Param("startTime") Date startTime,
                                                        @Param("endTime") Date endTime, @Param("alertLevel") Short alertLevel);

    void clearAlertDateHistory(@Param("timeStamp") Date timeStamp, @Param("stage") String stage);

    void updateAlertRule(@Param("alertRule") AlertRule alertRule);

    void insertAlertRule(@Param("alertRule") AlertRule alertRule);

    void setParamConfigurationList(@Param("list") List<AlertRule> list);

    List<AlertRule> getParamConfigurationList(@Param("metricCode")String metricCode);

    AlertRule getParamThreshold(@Param("thingCode") String thingCode, @Param("metricCode") String metricCode);

    void insertParamThreshold(AlertRule alertRule);

    void setParamThreshold(AlertRule alertRule);
}
