package com.zgiot.app.server.module.alert.handler;

import com.sun.org.apache.regexp.internal.RE;
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
import sun.awt.windows.WToolkit;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private static final String STATE_FAULT = "4";
    private Map<String, List<DataModel>> preFaultAlertCache = new ConcurrentHashMap<>();
    private static final int WAIT_TIME = 1000;

    @Override
    public void check(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if (ENABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData == null) {
            Short level = getLevelWithOutState(thingCode);
            if (level == null) {
                level = getAlertLevelWithState(dataModel, true);
            }
            if (level == null) {
                putCache(dataModel);
            } else {
                generateFaultAlert(dataModel, level);
            }
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

    public void checkCache(DataModel stateModel) {
        if (preFaultAlertCache.containsKey(stateModel.getThingCode())) {
            List<DataModel> dataModels = preFaultAlertCache.get(stateModel.getThingCode());
            if (dataModels != null && dataModels.size() != 0) {
                for (DataModel dataModel : dataModels) {
                    Short alertLevel = getAlertLevel(stateModel.getPreValue());
                    generateFaultAlert(dataModel, alertLevel);
                }
                dataModels.clear();
            }
        }

    }

    public void updateCache() {
        for (Map.Entry<String, List<DataModel>> entry : preFaultAlertCache.entrySet()) {
            List<DataModel> faultModels = entry.getValue();
            if (faultModels != null && faultModels.size() != 0) {
                for (DataModel dataModel : faultModels) {
                    if (Math.abs(System.currentTimeMillis() - dataModel.getDataTimeStamp().getTime()) >= WAIT_TIME) {
                        faultModels.remove(dataModel);
                        Short alertLevel = getAlertLevelWithState(dataModel, false);
                        if (alertLevel == null) {
                            alertLevel = AlertConstants.LEVEL_30;
                        }
                        generateFaultAlert(dataModel, alertLevel);
                    }
                }
            }
        }
    }

    private void generateFaultAlert(DataModel dataModel, Short level) {
        AlertData alertData = new AlertData(dataModel, AlertConstants.TYPE_FAULT, level,
                metricService.getMetric(dataModel.getMetricCode()).getMetricName(), AlertConstants.SOURCE_SYSTEM,
                AlertConstants.REPORTER_SYSTEM);
        alertManager.generateAlert(alertData);
        logger.debug("生成一条故障类报警，thing:{},metric:{}", dataModel.getThingCode(), dataModel.getMetricCode());
    }

    public void putCache(DataModel dataModel) {
        List<DataModel> faultModels;
        if (preFaultAlertCache.containsKey(dataModel.getThingCode())) {
            faultModels = preFaultAlertCache.get(dataModel.getThingCode());
        } else {
            faultModels = new CopyOnWriteArrayList<>();
            preFaultAlertCache.put(dataModel.getThingCode(), faultModels);
        }
        faultModels.add(dataModel);
    }

    private Short getLevelWithOutState(String thingCode) {
        Set<String> metricCodeSet = thingService.findMetricsOfThing(thingCode);
        if (!metricCodeSet.contains(MetricCodes.STATE)) {
            return AlertConstants.LEVEL_10;
        }
        return null;
    }

    private Short getAlertLevelWithState(DataModel dataModel, Boolean checkInterval) {
        if (dataService.getData(dataModel.getThingCode(), MetricCodes.STATE).isPresent()) {
            DataModelWrapper dataModelWrapper = dataService.getData(dataModel.getThingCode(), MetricCodes.STATE).get();
            String preState = dataModelWrapper.getPreValue();
            if (checkInterval) {
                long stateTime = dataModelWrapper.getDataTimeStamp().getTime();
                long faultTime = dataModel.getDataTimeStamp().getTime();
                if (Math.abs(faultTime - stateTime) <= WAIT_TIME) {
                    return getAlertLevel(preState);
                }
            } else {
                return getAlertLevel(preState);
            }
        }
        return null;

    }

    private Short getAlertLevel(String preState) {
        Short level;
        if (STATE_RUN.equals(preState)) {
            level = AlertConstants.LEVEL_30;
        } else if (STATE_STOP.equals(preState)) {
            level = AlertConstants.LEVEL_20;
        } else {
            level = AlertConstants.LEVEL_30;
        }
        return level;
    }

}
