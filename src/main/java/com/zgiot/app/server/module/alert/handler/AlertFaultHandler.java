package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.alert.pojo.NoPowerThing;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.app.server.service.ThingService;
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
    @Autowired
    private AlertProtectHandler protectHandler;
    private Logger logger = LoggerFactory.getLogger(AlertFaultHandler.class);
    private static final String ENABLE_VALUE = Boolean.TRUE.toString();
    private static final String DISABLE_VALUE = Boolean.FALSE.toString();
    private static final String STATE_RUN = "2";
    private static final String STATE_STOP = "1";
    private Map<String, List<DataModel>> preFaultAlertCache = new ConcurrentHashMap<>();
    private static final int WAIT_TIME = 1000;

    @Override
    public void check(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if (ENABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData == null) {
            String state = protectHandler.getState(dataModel);
            Short level = getAlertLevel(state);
            if (STATE_RUN.equals(state)) {
                generateFaultAlert(dataModel, level);
            }
        } else if (DISABLE_VALUE.equalsIgnoreCase(dataModel.getValue()) && alertData != null) {
            alertData.setRecovery(true);
            logger.debug("报警恢复，thing:{},metric:{}", thingCode, metricCode);
            if (alertData.isManualIntervention()) {
                alertManager.updateAlert(alertData);
            } else {
                alertManager.releaseAlert(alertData);
            }
        }
    }

    public void checkCache(DataModel stateModel) {
        if (preFaultAlertCache.containsKey(stateModel.getThingCode())) {
            List<DataModel> dataModels = preFaultAlertCache.get(stateModel.getThingCode());
            if (dataModels != null && !dataModels.isEmpty()) {
                for (DataModel dataModel : dataModels) {
                    Short alertLevel = getAlertLevel(stateModel.getPreValue());
                    logger.debug("设备{}生成{}等级为{}的报警，状态点于故障点之后返回，设备状态当前值为：{}，上一状态值为：{},状态点时间戳为{},故障点时间戳为{}",
                            dataModel.getThingCode(), dataModel.getMetricCode(), alertLevel, stateModel.getValue(),
                            stateModel.getPreValue(), stateModel.getDataTimeStamp().getTime(), dataModel.getDataTimeStamp().getTime());
                    generateFaultAlert(dataModel, alertLevel);
                }
                dataModels.clear();
            }
        }
    }

    public void updateCache() {
        for (Map.Entry<String, List<DataModel>> entry : preFaultAlertCache.entrySet()) {
            List<DataModel> faultModels = entry.getValue();
            if (faultModels != null && !faultModels.isEmpty()) {
                for (DataModel dataModel : faultModels) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (Math.abs(currentTimeMillis - dataModel.getDataTimeStamp().getTime()) >= WAIT_TIME) {
                        disposeTimeOutFaultInCache(faultModels, dataModel, currentTimeMillis);
                    }
                }
            }
        }
    }

    private void disposeTimeOutFaultInCache(List<DataModel> faultModels, DataModel dataModel, long currentTimeMillis) {
        faultModels.remove(dataModel);
        Short alertLevel = getAlertLevelWithState(dataModel, false);
        if (alertLevel == null) {
            alertLevel = AlertConstants.LEVEL_30;
        }
        logger.debug("设备{}生成{}等级为{}报警，超过等待时间未获取到设备状态信号点，当前时间戳为{}，故障点时间戳为{}", dataModel.getThingCode(),
                dataModel.getMetricCode(), alertLevel, currentTimeMillis,
                dataModel.getDataTimeStamp().getTime());
        generateFaultAlert(dataModel, alertLevel);
    }

    private void generateFaultAlert(DataModel dataModel, Short level) {
        AlertData alertData = new AlertData(dataModel, AlertConstants.TYPE_FAULT, level,
                metricService.getMetric(dataModel.getMetricCode()).getMetricName(), AlertConstants.SOURCE_SYSTEM,
                AlertConstants.REPORTER_SYSTEM);
        //根据thingCode和metricCode查询出报警原因并放入AlertData中
        alertDataSetCause(alertData);

        alertManager.generateAlert(alertData);
        logger.debug("生成一条故障类报警，thing:{},metric:{},等级：{}", dataModel.getThingCode(), dataModel.getMetricCode(), level);
    }

    private void alertDataSetCause(AlertData alertData) {
        if(alertData!=null){
            String cause=alertManager.getAlertDataCauseByTCAndMC(alertData);
            alertData.setAlertCause(cause);
        }
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

    private Short getLevelWithOutState(List<NoPowerThing> noPowerThings) {
        for (NoPowerThing noPowerThing : noPowerThings) {
            DataModelWrapper stateData = dataService.getData(noPowerThing.getSubThingCode(), MetricCodes.STATE).orElse(null);
            if (stateData != null && STATE_RUN.equals(stateData.getValue())) {
                return AlertConstants.LEVEL_30;
            }
        }
        return AlertConstants.LEVEL_20;
    }

    private Short getAlertLevelWithState(DataModel dataModel, Boolean checkInterval) {
        Optional<DataModelWrapper> stateData = dataService.getData(dataModel.getThingCode(), MetricCodes.STATE);
        if (stateData.isPresent()) {
            DataModelWrapper dataModelWrapper = stateData.get();
            String preState = dataModelWrapper.getPreValue();
            if (checkInterval) {
                long stateTime = dataModelWrapper.getDataTimeStamp().getTime();
                long faultTime = dataModel.getDataTimeStamp().getTime();
                if (Math.abs(faultTime - stateTime) <= WAIT_TIME) {
                    logger.debug("设备{}状态点于故障点之前返回，计算设备报警等级，设备状态当前值为{}，上一状态值为{}，设备状态时间戳为{}，故障点时间戳为{}",
                            dataModel.getThingCode(), dataModelWrapper.getValue(), dataModelWrapper.getPreValue(),
                            stateTime, faultTime);
                    return getAlertLevel(preState);
                }
            } else {
                logger.debug("超过等待时间未获取到设备{}状态信号点，计算设备报警等级，设备状态当前值为{}，上一状态值为{},设备状态时间戳为{}", dataModel.getThingCode(),
                        dataModelWrapper.getValue(), dataModelWrapper.getPreValue(),
                        dataModelWrapper.getDataTimeStamp().getTime());
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
