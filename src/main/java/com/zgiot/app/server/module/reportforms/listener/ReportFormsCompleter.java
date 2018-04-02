package com.zgiot.app.server.module.reportforms.listener;

import com.zgiot.app.server.config.ApplicationContextListener;
import com.zgiot.app.server.dataprocessor.DataCompleter;
import com.zgiot.app.server.module.reportforms.handler.ReportFormsHandler;
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
    private List<ReportFormsHandler> handlers = new ArrayList<>(0);


    private void initHandlers(){
        Map<String, ReportFormsHandler> beansOfType = ApplicationContextListener.getApplicationContext().getBeansOfType(ReportFormsHandler.class);
        handlers = new ArrayList<>(beansOfType.values());
    }


    @Override
    public List<DataModel> onComplete(DataModel dataModel) {
        ReportFormsManager reportFormsManager = getManager(dataModel.getThingCode());
        if (reportFormsManager == null){
            return Collections.emptyList();
        }
        ReportFormsRecord record = reportFormsManager.parseDataModelToRecord(dataModel);
        if (handlers.isEmpty()){
            initHandlers();
        }
        List<DataModel> result = new ArrayList<>();
        for (ReportFormsHandler handler:handlers){

            if (handler.isMatch(record)){
                List<DataModel> handle = handler.handle(reportFormsManager, record);
                result.addAll(handle);
            }
        }
        return result;
    }


    private ReportFormsManager getManager(String thingCode){
        ReportFormsManager reportFormsManager = null;
        if (CoalAnalysisConstants.COAL_ANALYSIS.equals(thingCode)) {
            reportFormsManager = coalAnalysisManager;
        } else if (CoalAnalysisConstants.PRODUCTION_INSPECT.equals(thingCode)) {
            reportFormsManager = productionInspectManager;
        }
        return reportFormsManager;
    }

    @Override
    public void onError(DataModel dm, Throwable e) {
        logger.error("data invalid", e);
    }
}
