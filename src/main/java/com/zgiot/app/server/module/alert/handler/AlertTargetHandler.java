package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertFaultJob;
import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class AlertTargetHandler implements AlertHandler {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private AlertParamHandler alertParamHandler;
    public static final Logger logger =  LoggerFactory.getLogger(AlertFaultJob.class);

    @Override
    public void check(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        Double value = Double.parseDouble(dataModel.getValue());
        Map<String, Map<String, List<AlertRule>>> targetRuleMap = alertManager.getTargetRuleMap();
        AlertRule alertRule = alertParamHandler.getAlertRule(targetRuleMap, thingCode, metricCode, value);
        //指标类报警  每次来新数据，都将原来的报警解除
            if (alertData != null) {
                alertManager.releaseAlert(alertData);
            }
            if (alertRule != null) {
                alertData = new AlertData(dataModel, AlertConstants.TYPE_TARGET, alertRule.getAlertLevel(), alertParamHandler.getAlertInfo(dataModel, metricCode),
                        AlertConstants.SOURCE_SYSTEM, AlertConstants.REPORTER_SYSTEM);
                alertData.setAlertDateTime(dataModel.getDataTimeStamp());
                alertData.setParamValue(value);
                alertData.setParamLower(alertRule.getLowerLimit());
                alertData.setParamUpper(alertRule.getUpperLimit());
                alertData.setLastUpdateTime(new Date());
                alertManager.generateAlert(alertData);
                logger.debug("生成一条保护类报警，thing:{},metric:{}", thingCode, metricCode);
        }

    }

}
