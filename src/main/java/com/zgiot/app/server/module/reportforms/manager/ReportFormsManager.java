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

    /**
     * 读取到平均数据时，先确定该班所有记录已经读取到，然后取所有记录的密度平均值，否则返回，等下一次读取再做处理
     * @param record
     * @return
     */
    boolean hasAllRecordsBeforeAvgRecord(ReportFormsRecord record);
    void updateAvgRecord(ReportFormsRecord record);
    void insertAvgRecord(ReportFormsRecord record);

}
