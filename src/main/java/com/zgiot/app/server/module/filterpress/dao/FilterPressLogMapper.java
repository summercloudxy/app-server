package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

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
     * @param startTime
     * @param endTime
     * @return
     */
    List<FilterPressLogBean> queryLogByDate(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term);

    /**
     * 查询各压滤机压板信息
     * @param isDayShift
     * @param startTime
     * @param endTime
     * @return
     */
    List<FilterPressSinglePlateCountBean> queryPlateInfos(@Param("isDayShift") boolean isDayShift, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term);

    /**
     * 查询各压滤机压板总数信息
     * @param isDayShift
     * @param startTime
     * @param endTime
     * @return
     */
    List<FilterPressPlateAndTimeBean> queryTotalPlateInfos(@Param("isDayShift") boolean isDayShift, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term);

    /**
     * 查询上一班次历史压板信息
     * @param startTime
     * @param endTime
     * @return
     */
    List<FilterPressHisPlateCountBean> queryHisPlateCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term);

    /**
     * 人工清零前查询上一班次所属对组
     * @param isDayShift
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getPriorTeam(@Param("isDayShift") boolean isDayShift, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term);


    FilterPressRatedStartTimeBean getFilterPressRatedStartTime(@Param("thingCode") String thingCode, @Param("dayOrNightRatedTime") String dayOrNightRatedTime);

    void insertPlateStatistic(FilterPressPlateStatistic filterPressPlateStatistic);

    @Select("select * from tb_filterpress_plate_statistics where is_day_shift=#{isDayShift} and term=#{term}\n" +
            "        and datetime >str_to_date(#{startTime},'%Y-%m-%d %H:%i:%s')\n" +
            "        and datetime < str_to_date(#{endTime},'%Y-%m-%d %H:%i:%s')")
    List<FilterPressPlateStatistic> getPlateStatistic(@Param("isDayShift") boolean isDayShift, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term);

    int selectMaxPlate(@Param("isDayShift") boolean isDayShift, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term, @Param("team") int team);

    Integer selectTotalPlate(@Param("isDayShift") boolean isDayShift, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term, @Param("team") int team);

    List<FilterPressLogBean> queryLog(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("term") int term, @Param("isDayShift") boolean isDayShift);
}
