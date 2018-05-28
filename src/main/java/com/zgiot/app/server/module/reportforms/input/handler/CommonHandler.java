package com.zgiot.app.server.module.reportforms.input.handler;

import com.zgiot.app.server.module.alert.AlertListener;
import com.zgiot.app.server.module.reportforms.input.ReportFormsUtils;
import com.zgiot.app.server.module.reportforms.input.manager.ReportFormsManager;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DensityAndFlowInfo;
import com.zgiot.common.pojo.ReportFormsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonHandler implements ReportFormsHandler {
    @Autowired
    private ReportFormsUtils reportFormsUtils;
    @Autowired
    private AlertListener alertListener;

    public static final Logger logger = LoggerFactory.getLogger(CommonHandler.class);

    @Override
    public List<DataModel> handle(ReportFormsManager manager, ReportFormsRecord record) {
        logger.debug("收到一条普通报表数据，内容为:{}",record);
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
        logger.debug("存储普通报表数据，内容为:{}",record);
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
