package com.zgiot.app.server.module.reportforms.output.dao;

import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductOutput;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductQuality;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductStore;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportFormProductionMapper {

    @MapKey(value = "coalType")
    Map<Integer, ReportFormProductOutput> getReportFormProductOutputPlan(Date productStartTime);

    @MapKey(value = "coalType")
    Map<Integer, ReportFormProductOutput> getReportFormProductOutput(Date productStartTime);

    void insertReportFormProductOutputs(List<ReportFormProductOutput> reportFormProductOutputList);


    void insertReportFormProductOutputPlans(List<ReportFormProductOutput> reportFormProductOutputPlanList);

    @MapKey(value = "coalType")
    Map<Integer, ReportFormProductStore> getReportFormProductStore(Date productStartTime);

    void insertReportFormProductStores(List<ReportFormProductStore> productStores);

    @MapKey(value = "coalType")
    Map<Integer, ReportFormProductQuality> getReportFormProductQuality(Date productStartTime);

    void insertReportFormProductQualities(List<ReportFormProductQuality> reportFormProductQualityList);

    void insertReportFormProductStore(ReportFormProductStore productStore);


    void updateReportFormProductStore(ReportFormProductStore productStore);
}
