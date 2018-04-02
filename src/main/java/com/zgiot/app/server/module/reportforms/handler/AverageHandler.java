package com.zgiot.app.server.module.reportforms.handler;

import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.module.reportforms.manager.ReportFormsManager;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ReportFormsRecord;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AverageHandler implements ReportFormsHandler {
    @Override
    public List<DataModel> handle(ReportFormsManager manager, ReportFormsRecord record) {
        if (!manager.hasAllRecordsBeforeAvgRecord(record)){
            return Collections.emptyList();
        }
        persistRecord(manager,record);
        return manager.getDataForCache(record,true);

    }

    private synchronized void persistRecord(ReportFormsManager reportFormsManager, ReportFormsRecord record) {
        Integer existRecordId = reportFormsManager.getExistRecordId(record);
        if (existRecordId != null) {
            record.setId(existRecordId);
            reportFormsManager.updateRecordWithOutDensityAndFlow(record);
            reportFormsManager.updateRecordDensityAndFlow(record);
        } else {
            reportFormsManager.insertRecord(record);
        }
    }

    @Override
    public boolean isMatch(ReportFormsRecord record) {
        return record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD);
    }
}
