package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.app.server.module.alert.pojo.NoPowerThing;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by xiayun on 2017/9/25.
 */
@Component
public class AlertProtectHandler implements AlertHandler {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private MetricService metricService;
    @Autowired
    private DataService dataService;
    private static final String ENABLE_VALUE = Boolean.TRUE.toString();
    private static final String DISABLE_VALUE = Boolean.FALSE.toString();
    public static final String STATE_RUN = "2";
    private static final String STATE_STOP = "1";
    private static final int WAIT_TIME = 1000;
    private final Logger logger = LoggerFactory.getLogger(AlertProtectHandler.class);

    @Override
    public void check(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Map<String, Map<String, AlertRule>> alertRuleMap = alertManager.getProtectRuleMap();
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        AlertRule alertRule = null;
        if (alertRuleMap.containsKey(thingCode) && alertRuleMap.get(thingCode).containsKey(metricCode)) {
            alertRule = alertRuleMap.get(thingCode).get(metricCode);
        }
        if (alertRule == null) {
            return;
        }
        if (ENABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData == null && STATE_RUN.equals(getState(dataModel))) {
            alertData = new AlertData(dataModel, AlertConstants.TYPE_PROTECT, alertRule.getAlertLevel(),
                    metricService.getMetric(metricCode).getMetricName(), AlertConstants.SOURCE_SYSTEM,
                    AlertConstants.REPORTER_SYSTEM);
            alertManager.generateAlert(alertData);
            logger.debug("生成一条保护类报警，thing:{},metric:{}", thingCode, metricCode);
        } else if (DISABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData != null) {
            alertData.setRecovery(true);
            if (alertData.isManualIntervention()) {
                alertManager.updateAlert(alertData);
            } else {
                alertManager.releaseAlert(alertData);
            }
        }
    }


    protected String getState(DataModel dataModel) {
        List<NoPowerThing> noPowerThings = alertManager.getNoPowerThingByThingCode(dataModel.getThingCode());
        String state;
        if (CollectionUtils.isNotEmpty(noPowerThings)) {
            state = getNoPowerThingState(noPowerThings);
        } else {
            state = getPowerThingState(dataModel);
        }
        return state;
    }

    private String getPowerThingState(DataModel dataModel) {
        Optional<DataModelWrapper> stateData = dataService.getData(dataModel.getThingCode(), MetricCodes.STATE);
        if (stateData.isPresent()) {
            DataModelWrapper dataModelWrapper = stateData.get();
            String preState = dataModelWrapper.getPreValue();
            String currentState = dataModelWrapper.getValue();
            long stateTime = dataModelWrapper.getDataTimeStamp().getTime();
            long faultTime = dataModel.getDataTimeStamp().getTime();
            if (Math.abs(faultTime - stateTime) <= WAIT_TIME) {
                return preState;
            }
            return currentState;

        }
        return null;
    }

    private String getNoPowerThingState(List<NoPowerThing> noPowerThings) {
        for (NoPowerThing noPowerThing : noPowerThings) {
            DataModelWrapper stateData = dataService.getData(noPowerThing.getSubThingCode(), MetricCodes.STATE).orElse(null);
            if (stateData != null && STATE_RUN.equals(stateData.getValue())) {
                return STATE_RUN;
            }
        }
        return STATE_STOP;
    }


}
