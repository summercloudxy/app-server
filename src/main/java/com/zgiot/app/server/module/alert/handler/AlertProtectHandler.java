package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.app.server.service.impl.MetricServiceImpl;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by xiayun on 2017/9/25.
 */
@Component
public class AlertProtectHandler implements AlertHandler {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private MetricServiceImpl metricService;
    private static final String ENABLE_VALUE = Boolean.TRUE.toString();
    private static final String DISABLE_VALUE = Boolean.FALSE.toString();
    private static final Logger logger = LoggerFactory.getLogger(AlertProtectHandler.class);

    @Override
    public void check(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Map<String, Map<String, AlertRule>> alertRuleMap = alertManager.getProtectRuleMap();
        AlertData alertData =
                alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        AlertRule alertRule = alertRuleMap.get(thingCode).get(metricCode);
        if (ENABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData == null) {
            alertData = new AlertData(dataModel, AlertConstants.TYPE_PROTECT, alertRule.getAlertLevel(),
                    metricService.getMetric(metricCode).getMetricName(), AlertConstants.SOURCE_SYSTEM,
                    AlertConstants.REPORTER_SYSTEM);
            alertManager.generateAlert(alertData);
            logger.debug("生成一条保护类报警，thing:{},metric:{}", thingCode, metricCode);
        } else if (DISABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData != null) {
            alertData.setRecovery(true);
            if (!alertData.getManualIntervention()) {
                alertManager.releaseAlert(alertData);
            }
        }

    }

}
