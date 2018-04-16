package com.zgiot.app.server.module.produtioninspect.mapper;

import com.zgiot.app.server.module.reportforms.pojo.DensityAndFlowSourceInfo;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.common.pojo.DensityAndFlowInfo;
import com.zgiot.common.pojo.ProductionInspectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProductionInspectMapper {
    Integer getExistRecordId(ProductionInspectRecord record);
    ProductionInspectRecord getExistRecord(ProductionInspectRecord record);
    ProductionInspectRecord getRecentRecord(ProductionInspectRecord record);

    /**
     * 获取该班的最后一条记录  record.getTime 为dutyStartTime
     * @param record
     * @param dutyEndTime
     * @return
     */
    ProductionInspectRecord getLastRecordOnDuty(@Param("record")ProductionInspectRecord record,@Param("endTime")  Date dutyEndTime);
    void updateRecordWithOutDensityAndFlow(@Param("record") ProductionInspectRecord record);
    void insertRecord(ProductionInspectRecord record);
    void updateRecordDensityAndFlow(ProductionInspectRecord record);
    void insertDetailDensityAndFlowValues(@Param("list") List<DensityAndFlowInfo> densityAndFlowValues,@Param("inspectId")Integer id);
    List<DensityAndFlowSourceInfo> getDensityAndFlowSourceInfo();
    List<ProductionInspectRecord> getRecordsMatchCondition(FilterCondition filterCondition);
}
