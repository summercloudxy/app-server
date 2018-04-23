package com.zgiot.app.server.module.reportforms.handler;

import com.zgiot.app.server.config.ModuleListConfig;
import com.zgiot.app.server.module.alert.AlertListener;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.module.reportforms.manager.ReportFormsManager;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DensityAndFlowInfo;
import com.zgiot.common.pojo.ReportFormsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonHandler implements ReportFormsHandler {
    @Autowired
    private ReportFormsUtils reportFormsUtils;
    @Autowired
    private AlertListener alertListener;

    @Override
    public List<DataModel> handle(ReportFormsManager manager, ReportFormsRecord record) {
        persistRecord(manager,record);
        List<DataModel> dataForCaches = manager.getDataForCache(record, false);
        for (DataModel dataForCache:dataForCaches){
            alertListener.onDataChange(dataForCache);
        }
        return dataForCaches;
    }

    public boolean isMatch(ReportFormsRecord record){
        return !record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD);
    }

    private synchronized void persistRecord(ReportFormsManager reportFormsManager, ReportFormsRecord record) {
        Integer existRecordId = reportFormsManager.getExistRecordId(record);
        if (existRecordId != null) {
            record.setId(existRecordId);
            reportFormsManager.updateRecordWithOutDensityAndFlow(record);
        } else {
            reportFormsManager.insertRecord(record);
            List<DensityAndFlowInfo> densityAndFlow = getDensityAndFlow(record);
            if (!densityAndFlow.isEmpty()) {
                reportFormsManager.insertDetailDensityAndFlow(record);
            }
        }
    }

    private List<DensityAndFlowInfo>  getDensityAndFlow(ReportFormsRecord record){
        List<DensityAndFlowInfo> densityAndFlowValues = reportFormsUtils.getDensityAndFlowValues(record);
        if (!densityAndFlowValues.isEmpty()) {
            record.setDensityAndFlowInfos(densityAndFlowValues);
            reportFormsUtils.countDensityAndFlowAvgValue(record);
        }
        return densityAndFlowValues;
    }
}
