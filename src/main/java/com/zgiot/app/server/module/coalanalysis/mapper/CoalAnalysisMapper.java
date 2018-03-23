package com.zgiot.app.server.module.coalanalysis.mapper;

import com.zgiot.app.server.module.reportforms.pojo.DensityAndFlowSourceInfo;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DensityAndFlowInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CoalAnalysisMapper {
    Integer getExistRecordId(CoalAnalysisRecord record);

    void updateRecordWithOutDensityAndFlow(@Param("record") CoalAnalysisRecord record);

    void insertRecord(CoalAnalysisRecord record);

    void updateRecordDensityAndFlow(CoalAnalysisRecord record);
    List<CoalAnalysisRecord> getRecordsMatchCondition(FilterCondition filterCondition);
    List<DensityAndFlowInfo> getDensityAndFlowInfo(int recordId);
}
