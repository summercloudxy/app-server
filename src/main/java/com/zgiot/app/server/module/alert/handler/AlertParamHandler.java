package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.app.server.service.impl.MetricServiceImpl;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.MetricModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiayun on 2017/9/26.
 */
@Component
public class AlertParamHandler implements AlertHandler {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private MetricServiceImpl metricService;
    private Map<String, Map<String, List<AlertRule>>> alertRuleMap;
    private Map<String, Map<String, AlertData>> alertDataCache ;
    @Value("${alert.param.period}")
    private Long paramAlertUpdatePeriod;
    private static final Logger logger = LoggerFactory.getLogger(AlertParamHandler.class);

    @Override
    public void check(DataModel dataModel) {
        alertDataCache = alertManager.getAlertParamDataMap();
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Double value = Double.parseDouble(dataModel.getValue());
        MetricModel metricModel = metricService.getMetric(metricCode);
        String alertInfo = metricModel.getMetricName() + dataModel.getValue() + metricModel.getValueUnit();
        Short alertLevel = getAlertLevel(thingCode, metricCode, value);
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if (alertData == null && alertLevel != null) {
            alertData = new AlertData(dataModel, AlertConstants.TYPE_PARAM, alertLevel, alertInfo,
                    AlertConstants.SOURCE_SYSTEM, AlertConstants.REPORTER_SYSTEM);
            alertData.setParamValue(value);
            alertData.setLastUpdateTime(new Date());
            alertManager.generateAlert(alertData);
            Map<String, AlertData> metricAlertDataCache;
            if (alertDataCache.containsKey(thingCode)) {
                metricAlertDataCache = alertDataCache.get(thingCode);
            } else {
                metricAlertDataCache = new HashMap<>();
                alertDataCache.put(thingCode, metricAlertDataCache);
            }
            metricAlertDataCache.put(metricCode, alertData);
            logger.debug("生成一条参数类报警，thing:{},metric:{}", thingCode, metricCode);
        } else if (alertData != null) {
            alertData.setParamValue(value);
        }

    }

    @Scheduled(cron = "0/10 * * * * ?")
    private void updateAlertLevel() {
        alertDataCache = alertManager.getAlertParamDataMap();
        for (Map.Entry<String, Map<String, AlertData>> entry : alertDataCache.entrySet()) {
            String thingCode = entry.getKey();
            Map<String, AlertData> metricAlertDataMap = entry.getValue();
            for (Map.Entry<String, AlertData> dataEntry : metricAlertDataMap.entrySet()) {
                String metricCode = dataEntry.getKey();
                AlertData alertData = dataEntry.getValue();
                if (new Date().getTime() - alertData.getLastUpdateTime().getTime() > paramAlertUpdatePeriod) {
                    Short alertLevel = getAlertLevel(thingCode, metricCode, alertData.getParamValue());
                    if (alertLevel == null) {
                        alertData.setRecovery(true);
                        if (!alertData.isManualIntervention()) {
                            alertManager.releaseAlert(alertData);
                            metricAlertDataMap.remove(metricCode);
                        }
                    } else if (!alertLevel.equals(alertData.getAlertLevel())) {
                        alertData.setAlertLevel(alertLevel);
                        MetricModel metricModel = metricService.getMetric(metricCode);
                        String alertInfo =
                                metricModel.getMetricName() + alertData.getParamValue() + metricModel.getValueUnit();
                        alertData.setAlertInfo(alertInfo);
                        alertData.setLastUpdateTime(new Date());
                        alertManager.updateAlert(alertData);
                        logger.debug("调整报警等级，thingCode {}，metricCode {}，当前等级为{}", thingCode, metricCode, alertLevel);
                    }
                }
            }
        }
    }

    private Short getAlertLevel(String thingCode, String metricCode, double value) {
        alertRuleMap = alertManager.getParamRuleMap();
        List<AlertRule> alertRuleList = alertRuleMap.get(thingCode).get(metricCode);
        Short alertLevel = null;
        for (AlertRule alertRule : alertRuleList) {
            if (value < alertRule.getUpperLimit() && value >= alertRule.getLowerLimit()) {
                alertLevel = alertRule.getAlertLevel();
                break;
            }
        }
        return alertLevel;
    }

}
