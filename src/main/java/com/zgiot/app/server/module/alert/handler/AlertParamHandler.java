package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.MetricModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiayun on 2017/9/26.
 */
@Component
public class AlertParamHandler implements AlertHandler {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private MetricService metricService;
    private Map<String, Map<String, AlertData>> alertDataCache;
    @Value("${alert.param.period}")
    private Long paramAlertUpdatePeriod;
    private final Logger logger = LoggerFactory.getLogger(AlertParamHandler.class);

    @Override
    public void check(DataModel dataModel) {
        alertDataCache = alertManager.getAlertParamDataMap();
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Double value = Double.parseDouble(dataModel.getValue());
        MetricModel metricModel = metricService.getMetric(metricCode);
        BigDecimal valueStr = new BigDecimal(dataModel.getValue());
        valueStr = valueStr.setScale(2, BigDecimal.ROUND_HALF_UP);
        String alertInfo = metricModel.getMetricName() + "-" + valueStr + metricModel.getValueUnit();
        AlertRule alertRule = getAlertLevel(thingCode, metricCode, value);
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if (alertData == null && alertRule != null) {
            alertData = new AlertData(dataModel, AlertConstants.TYPE_PARAM, alertRule.getAlertLevel(), alertInfo,
                    AlertConstants.SOURCE_SYSTEM, AlertConstants.REPORTER_SYSTEM);
            alertData.setParamValue(value);
            alertData.setParamLower(alertRule.getLowerLimit());
            alertData.setParamUpper(alertRule.getUpperLimit());
            alertData.setLastUpdateTime(new Date());
            alertManager.generateAlert(alertData);
            Map<String, AlertData> metricAlertDataCache;
            if (alertDataCache.containsKey(thingCode)) {
                metricAlertDataCache = alertDataCache.get(thingCode);
            } else {
                metricAlertDataCache = new ConcurrentHashMap<>();
                alertDataCache.put(thingCode, metricAlertDataCache);
            }
            metricAlertDataCache.put(metricCode, alertData);
            logger.debug("生成一条参数类报警，thing:{},metric:{}", thingCode, metricCode);
        } else if (alertData != null) {
            alertData.setParamValue(value);
        }

    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void updateAlertLevel() {
        alertDataCache = alertManager.getAlertParamDataMap();
        for (Map.Entry<String, Map<String, AlertData>> entry : alertDataCache.entrySet()) {
            String thingCode = entry.getKey();
            Map<String, AlertData> metricAlertDataMap = entry.getValue();
            for (Map.Entry<String, AlertData> dataEntry : metricAlertDataMap.entrySet()) {
                String metricCode = dataEntry.getKey();
                AlertData alertData = dataEntry.getValue();
                if (System.currentTimeMillis() - alertData.getLastUpdateTime().getTime() > paramAlertUpdatePeriod) {
                    disposeTimeOutParamAlertData(thingCode, metricAlertDataMap, metricCode, alertData);
                }
            }
        }
    }

    private void disposeTimeOutParamAlertData(String thingCode, Map<String, AlertData> metricAlertDataMap, String metricCode, AlertData alertData) {
        AlertRule alertRule = getAlertLevel(thingCode, metricCode, alertData.getParamValue());
        if (alertRule == null) {
            alertData.setRecovery(true);
            if (!alertData.isManualIntervention()) {
                alertManager.releaseAlert(alertData);
                metricAlertDataMap.remove(metricCode);
            }
        } else if (!alertRule.getAlertLevel().equals(alertData.getAlertLevel())) {
            alertData.setAlertLevel(alertRule.getAlertLevel());
            alertData.setParamUpper(alertRule.getUpperLimit());
            alertData.setParamLower(alertRule.getLowerLimit());
            MetricModel metricModel = metricService.getMetric(metricCode);
            String alertInfo = metricModel.getMetricName() + "-" + alertData.getParamValue()
                    + metricModel.getValueUnit();
            alertData.setAlertInfo(alertInfo);
            alertData.setLastUpdateTime(new Date());
            alertManager.updateAlert(alertData);
            logger.debug("调整报警等级，thingCode {}，metricCode {}，当前等级为{}", thingCode, metricCode,
                    alertRule.getAlertLevel());
        }
    }

    private AlertRule getAlertLevel(String thingCode, String metricCode, double value) {
        Map<String, Map<String, List<AlertRule>>> alertRuleMap = alertManager.getParamRuleMap();
        if (alertRuleMap.containsKey(thingCode) && alertRuleMap.get(thingCode).containsKey(metricCode)) {
            List<AlertRule> alertRuleList = alertRuleMap.get(thingCode).get(metricCode);
            for (AlertRule alertRule : alertRuleList) {
                if (value < alertRule.getUpperLimit() && value >= alertRule.getLowerLimit()) {
                    return alertRule;
                }
            }

        }
        return null;
    }

}
