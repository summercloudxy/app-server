package com.zgiot.app.server.module.coalanalysis.mapper;

import com.zgiot.app.server.module.reportforms.input.pojo.DensityAndFlowSourceInfo;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DensityAndFlowInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface CoalAnalysisMapper {
    Integer getExistRecordId(CoalAnalysisRecord record);

    CoalAnalysisRecord getExistRecord(CoalAnalysisRecord record);

    CoalAnalysisRecord getRecentRecord(CoalAnalysisRecord record);

    CoalAnalysisRecord getLastRecordOnDuty(@Param("record") CoalAnalysisRecord record, @Param("endTime") Date dutyEndTime);

    void updateRecordWithOutDensityAndFlow(@Param("record") CoalAnalysisRecord record);

    void insertRecord(CoalAnalysisRecord record);

    void updateRecordDensityAndFlow(CoalAnalysisRecord record);

    void insertDetailDensityAndFlowValues(@Param("list") List<DensityAndFlowInfo> densityAndFlowValues, @Param("analysisId") Integer id);

    List<DensityAndFlowSourceInfo> getDensityAndFlowSourceInfo();

    List<CoalAnalysisRecord> getRecordsMatchCondition(FilterCondition filterCondition);

    List<DensityAndFlowInfo> getDetailDensityAndFlowInfo(int recordId);


    /**
     * 查询某个设备的最新的两条化验数据
     *
     * @param sample
     * @param target
     * @return
     */
    @Select("select * from tb_coal_analysis where sample =#{sample} and  target =#{target} ORDER BY time DESC  LIMIT 0,2")
    List<CoalAnalysisRecord> getTop2CoalAnalysisRecord(@Param("sample") String sample, @Param("target") String target);

    /**
     * 查询某个时间段内的所有时间
     *
     * @param target
     * @param timeBegin
     * @param timeEnd
     * @return
     */
    @Select("select time from tb_coal_analysis where target = #{target} and time >= #{timeBegin} and time <= #{timeEnd} ORDER BY time")
    List<Date> getTimeRangeTime(@Param("target") String target, @Param("timeBegin") Date timeBegin, @Param("timeEnd") Date timeEnd);

    /**
     * 查询某个时间段内记录的平均值
     *
     * @param system
     * @param target
     * @param timeBegin
     * @param timeEnd
     * @return
     */
    @Select("select AVG(aad) as aad,AVG(mt) as mt,AVG(stad) as stad,AVG(qnetar) as qnetar from tb_coal_analysis " +
            "where system = #{system} and target = #{target} and time >= #{timeBegin} and time <= #{timeEnd} ORDER BY time")
    CoalAnalysisRecord getTimeRangeCoalAnalysisRecordAVG(@Param("system") Integer system, @Param("target") String target,
                                                         @Param("timeBegin") Date timeBegin, @Param("timeEnd") Date timeEnd);

}
