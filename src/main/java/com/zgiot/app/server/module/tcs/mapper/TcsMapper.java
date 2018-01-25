package com.zgiot.app.server.module.tcs.mapper;

import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DensityAndFlowInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TcsMapper {
    List<CoalAnalysisRecord> getRecordsMatchCondition(FilterCondition filterCondition);
    List<DensityAndFlowInfo> getDensityAndFlowInfo(int recordId);
}
                                                  