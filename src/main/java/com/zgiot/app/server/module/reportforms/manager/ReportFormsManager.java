package com.zgiot.app.server.module.reportforms.manager;

import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ReportFormsRecord;

import java.util.List;

public interface ReportFormsManager {
    Integer getExistRecordId(ReportFormsRecord record);
    void updateRecord(ReportFormsRecord record);
    void insertRecord(ReportFormsRecord record);
    List<DataModel> getDataForCache(ReportFormsRecord record);
    ReportFormsRecord parseDataModelToRecord(DataModel dataModel);
    void disposeAvgRecord(ReportFormsRecord record);
}
