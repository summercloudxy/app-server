package com.zgiot.app.server.module.produtioninspect.mapper;

import com.zgiot.app.server.module.reportforms.pojo.DensityAndFlowSourceInfo;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.pojo.DensityAndFlowInfo;
import com.zgiot.common.pojo.ProductionInspectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductionInspectMapper {
    Integer getExistRecordId(ProductionInspectRecord record);
    void updateRecord(@Param("record") ProductionInspectRecord record);
    void insertRecord(ProductionInspectRecord record);
    void updateRecordDensityAndFlow(ProductionInspectRecord record);
    void insertDensityAndFlowValues(List<DensityAndFlowInfo> densityAndFlowValues);
    List<DensityAndFlowSourceInfo> getDensityAndFlowSourceInfo();
    List<ProductionInspectRecord> getRecordsMatchCondition(FilterCondition filterCondition);
}
