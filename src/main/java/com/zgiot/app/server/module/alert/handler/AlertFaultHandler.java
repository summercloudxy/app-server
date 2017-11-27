package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by xiayun on 2017/9/26.
 */
@Component
public class AlertFaultHandler implements AlertHandler {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private DataService dataService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private ThingService thingService;
    private Logger logger = LoggerFactory.getLogger(AlertFaultHandler.class);
    private static final String ENABLE_VALUE = Boolean.TRUE.toString();
    private static final String DISABLE_VALUE = Boolean.FALSE.toString();
    private static final String STATE_RUN = "2";
    private static final String STATE_STOP = "1";

    @Override
    public void check(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if (ENABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData == null) {
            Set<String> metricCodeSet = thingService.findMetricsOfThing(thingCode);
            Short level = getAlertLevel(thingCode, metricCodeSet);
            alertData = new AlertData(dataModel, AlertConstants.TYPE_FAULT, level,
                    metricService.getMetric(dataModel.getMetricCode()).getMetricName(), AlertConstants.SOURCE_SYSTEM,
                    AlertConstants.REPORTER_SYSTEM);
            alertManager.generateAlert(alertData);
            logger.debug("生成一条故障类报警，thing:{},metric:{}", thingCode, metricCode);
        } else if (DISABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData != null) {
            alertData.setRecovery(true);
            logger.debug("报警恢复，thing:{},metric:{}", thingCode, metricCode);
            if (!alertData.isManualIntervention()) {
                alertManager.releaseAlert(alertData);
            } else {
                alertManager.updateAlert(alertData);
            }
        }
    }

    private Short getAlertLevel(String thingCode, Set<String> metricCodeSet) {
        Short level;
        if (metricCodeSet.contains(MetricCodes.STATE)) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                logger.debug("thread is interrupted");
            }
            if (dataService.getData(thingCode, MetricCodes.STATE).isPresent()) {
                DataModelWrapper dataModelWrapper = dataService.getData(thingCode, MetricCodes.STATE).get();
                String preState = dataModelWrapper.getPreValue();
                if (STATE_RUN.equals(preState)) {
                    level = AlertConstants.LEVEL_30;
                } else if (STATE_STOP.equals(preState)) {
                    level = AlertConstants.LEVEL_20;
                } else {
                    level = AlertConstants.LEVEL_30;
                }
            } else {
                level = AlertConstants.LEVEL_30;
            }
        } else {
            level = AlertConstants.LEVEL_10;
        }
        return level;
    }

}
