package com.zgiot.app.server.module.coalanalysis.mapper;

import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowInfo;
import com.zgiot.app.server.module.coalanalysis.pojo.DensityAndFlowValue;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoalAnalysisMapper {
    Integer getExistRecordId(CoalAnalysisRecord record);
    void updateRecord(@Param("record") CoalAnalysisRecord record, @Param("id") Integer id);
    void insertRecord(CoalAnalysisRecord record);
    void updateRecordDensityAndFlow(CoalAnalysisRecord record);
    void insertDensityAndFlowValues(List<DensityAndFlowValue> densityAndFlowValues);
    List<DensityAndFlowInfo> getDensityAndFlowInfo();
}
