package com.zgiot.app.server.module.coalanalysis.mapper;

import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowInfo;
import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowValue;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CoalAnalysisMapper {
    Integer getExistRecordId(CoalAnalysisRecord record);
    void updateRecord(@Param("record") CoalAnalysisRecord record, @Param("id") Integer id);
    void insertRecord(CoalAnalysisRecord record);
    void updateRecordDensityAndFlow(CoalAnalysisRecord record);
    void insertDensityAndFlowValues(List<DensityAndFlowValue> densityAndFlowValues);
    List<DensityAndFlowInfo> getDensityAndFlowInfo();

    /**
     * 查询某个设备的最新的两条化验数据
     *
     * @param sample
     * @param target
     * @return
     */
    @Select("select * from tb_coal_analysis where sample =#{sample} and  target =#{target} ORDER BY time DESC  LIMIT 0,2")
    List<CoalAnalysisRecord> getTop2CoalAnalysisRecord(@Param("sample") String sample, @Param("target") String target);
}
