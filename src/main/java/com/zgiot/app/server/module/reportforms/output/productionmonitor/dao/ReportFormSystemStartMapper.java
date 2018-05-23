package com.zgiot.app.server.module.reportforms.output.productionmonitor.dao;


import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.StateThreshold;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ReportFormSystemStartMapper {
    ReportFormSystemStartRecord getTheLastOneRecord(@Param("date") Date date, @Param("term") Integer term);

    void insertRecord(ReportFormSystemStartRecord record);

    void updateRecord(ReportFormSystemStartRecord record);

    void deleteRecord(ReportFormSystemStartRecord record);

    List<ReportFormSystemStartRecord> getRecordsOnDuty(@Param("dutyStartTime") Date date, @Param("term") Integer term);

    List<StateThreshold> getAllModuleStateThreshold();

}
