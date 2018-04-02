package com.zgiot.app.server.module.reportforms.manager;

import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ReportFormsRecord;

import java.util.Date;
import java.util.List;

public interface ReportFormsManager {
    Integer getExistRecordId(ReportFormsRecord record);
    ReportFormsRecord getExistRecord(ReportFormsRecord record);

    /**
     * 获取该记录的上一条记录（同种化验项目）
     * @param record
     * @return
     */
    ReportFormsRecord getRecentRecord(ReportFormsRecord record);

    /**
     *  获取该班的最后一条记录  record.getTime 为dutyStartTime
     * @param record
     * @param dutyEndTime
     * @return
     */
    ReportFormsRecord getLastRecordOnDuty(ReportFormsRecord record,Date dutyEndTime);
    void updateRecordWithOutDensityAndFlow(ReportFormsRecord record);
    void updateRecordDensityAndFlow(ReportFormsRecord record);
    void insertRecord(ReportFormsRecord record);
    void insertDetailDensityAndFlow(ReportFormsRecord record);
    List<DataModel> getDataForCache(ReportFormsRecord record,boolean avgFlag);
    ReportFormsRecord parseDataModelToRecord(DataModel dataModel);

    /**
     * 读取到平均数据时，先确定该班所有记录已经读取到，然后取所有记录的密度平均值，否则返回，等下一次读取再做处理
     * @param record
     * @return
     */
    boolean hasAllRecordsBeforeAvgRecord(ReportFormsRecord record);



}
