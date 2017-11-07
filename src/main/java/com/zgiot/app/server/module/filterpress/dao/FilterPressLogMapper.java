package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lulansong on 2017/10/31.
 */
@Mapper
public interface FilterPressLogMapper {
    /**
     * 插入压板信息（压滤日志数据来源）
     * @param filterPressLogBean
     */
    void insertFilterPressLog(FilterPressLogBean filterPressLogBean);

    /**
     * 根据日期查询压滤日志
     * @param queryDate
     * @return
     */
    List<FilterPressLogBean> queryLogByDate(String queryDate);

    /**
     * 查询各压滤机压板信息
     * @param isDayShift
     * @param currentDay
     * @param nextDay
     * @return
     */
    List<FilterPressSinglePlateCountBean> queryPlateInfos(@Param("isDayShift") boolean isDayShift, @Param("currentDay") String currentDay, @Param("nextDay") String nextDay);

    /**
     * 查询各压滤机压板总数信息
     * @param isDayShift
     * @param currentDay
     * @param nextDay
     * @return
     */
    List<FilterPressPlateAndTimeBean> queryTotalPlateInfos(@Param("isDayShift") boolean isDayShift,@Param("currentDay") String currentDay,@Param("nextDay") String nextDay);

    /**
     * 查询上一班次历史压板信息
     * @param isDayShift
     * @param currentDay
     * @param nextDay
     * @return
     */
    List<FilterPressTcAndMaxPlateCount> queryHisPlateCount(@Param("isDayShift") boolean isDayShift, @Param("currentDay") String currentDay, @Param("nextDay") String nextDay);

    /**
     * 人工清零前查询上一班次所属对组
     * @param isDayShift
     * @param priorShiftDay
     * @return
     */
    int getPriorTeam(@Param("isDayShift") boolean isDayShift, @Param("priorShiftDay") String priorShiftDay);
}
