package com.zgiot.app.server.module.qualityandquantity.service;

import com.zgiot.app.server.module.qualityandquantity.pojo.*;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;

import java.util.Date;
import java.util.List;

public interface QualityAndQuantityService {
    List<DutyInfoInOneDay> getMaxValueOnDuty(String thingCode, String metricCode, Date startTime, Date endTime);
    List<CoalAnalysisData> getCoalAnalysisHistoryData(FilterCondition filterCondition);
    List<ProductionInspectData> getProductionInspectData(FilterCondition filterCondition);
    List<AreaInfo> getAreaInfos(List<Integer> areaIds);

}
