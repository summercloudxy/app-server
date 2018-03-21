package com.zgiot.app.server.module.reportforms.listener;

import com.zgiot.app.server.dataprocessor.DataCompleter;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.module.reportforms.manager.CoalAnalysisManager;
import com.zgiot.app.server.module.reportforms.manager.ProductionInspectManager;
import com.zgiot.app.server.module.reportforms.manager.ReportFormsManager;
import com.zgiot.common.constants.CoalAnalysisConstants;
import com.zgiot.common.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReportFormsCompleter implements DataCompleter {
    private final Logger logger = LoggerFactory.getLogger(ReportFormsCompleter.class);
    @Autowired
    private CoalAnalysisManager coalAnalysisManager;
    @Autowired
    private ProductionInspectManager productionInspectManager;

    private List<DataModel> getParamDataToCache(ReportFormsRecord record,ReportFormsManager reportFormsManager) {
        return reportFormsManager.getDataForCache(record);
    }


    @Override
    public List<DataModel> onComplete(DataModel dataModel) {
        ReportFormsManager reportFormsManager;
        if (CoalAnalysisConstants.COAL_ANALYSIS.equals(dataModel.getThingCode())) {
            reportFormsManager = coalAnalysisManager;
        } else if (CoalAnalysisConstants.PRODUCTION_INSPECT.equals(dataModel.getThingCode())) {
            reportFormsManager = productionInspectManager;
        } else {
            return Collections.emptyList();
        }
        ReportFormsRecord record = reportFormsManager.parseDataModelToRecord(dataModel);
        if (record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)) {
            reportFormsManager.disposeAvgRecord(record);
        }
        Integer existRecordId = reportFormsManager.getExistRecordId(record);
        if (existRecordId != null) {
            record.setId(existRecordId);
            reportFormsManager.updateRecord(record);
        } else {
            reportFormsManager.insertRecord(record);
        }
        return getParamDataToCache(record, reportFormsManager);

    }

    @Override
    public void onError(DataModel dm, Throwable e) {
        logger.error("data invalid", e);
    }
}
