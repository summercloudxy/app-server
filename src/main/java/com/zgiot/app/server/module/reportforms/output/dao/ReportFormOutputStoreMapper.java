package com.zgiot.app.server.module.reportforms.output.dao;

import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormOutputStoreRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ReportFormOutputStoreMapper {
    void updateRecord(ReportFormOutputStoreRecord record);

    void insertRecord(ReportFormOutputStoreRecord record);

    ReportFormOutputStoreRecord getOutputStoreRecord(@Param("type") Integer type, @Param("dutyStartTime") Date dutyStartTime);

    List<ReportFormOutputStoreRecord> getOutputStoreRecordsInDuration(@Param("type") Integer type, @Param("startTime") Date startTime,@Param("endTime") Date endTime);
}
