package com.zgiot.app.server.module.reportforms.input.handler;

import com.zgiot.app.server.module.reportforms.input.ReportFormsUtils;
import com.zgiot.app.server.module.reportforms.input.manager.ReportFormsManager;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ReportFormsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AverageHandler implements ReportFormsHandler {
    public static final Logger logger = LoggerFactory.getLogger(AverageHandler.class);
    @Override
    public List<DataModel> handle(ReportFormsManager manager, ReportFormsRecord record) {
        logger.debug("收到一条平均报表数据，内容为:{}",record);
        if (!manager.hasAllRecordsBeforeAvgRecord(record)){
            return Collections.emptyList();
        }
        persistRecord(manager,record);
        return manager.getDataForCache(record,true);

    }

    private synchronized void persistRecord(ReportFormsManager reportFormsManager, ReportFormsRecord record) {
        logger.debug("存储平均报表数据，内容为:{}",record);
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
