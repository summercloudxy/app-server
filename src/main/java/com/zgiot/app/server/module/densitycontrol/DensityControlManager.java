package com.zgiot.app.server.module.densitycontrol;

import com.zgiot.app.server.module.densitycontrol.handle.*;
import com.zgiot.app.server.module.densitycontrol.mapper.DensityControlMapper;
import com.zgiot.app.server.module.densitycontrol.pojo.MonitoringParam;
import com.zgiot.app.server.module.densitycontrol.pojo.NotifyThingMetricInfo;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.QuartzManager;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DensityControlManager {
    @Autowired
    private AddingMediumHandler addingMediumHandler;
    @Autowired
    private BackFlowHandler backFlowHandler;
    @Autowired
    private NotifyAlertHandler notifyAlertHandler;
    @Autowired
    private CancelNotifyHandler cancelNotifyHandler;
    /**
     * key:智能控制阶段 - 密度阶段+开度阶段+液位阶段拼接
     * value:该阶段对应操作
     */
    private Map<String, DensityControlHandler> stageDisposeMap = new HashMap<String, DensityControlHandler>() {
        {
            put("101", addingMediumHandler);
            put("021", addingMediumHandler);
            put("033", backFlowHandler);
            put("101", addingMediumHandler);
            put("201", addingMediumHandler);
            put("303", notifyAlertHandler);

        }
    };
    /**
     * key:thingCode
     * value:当前状态及参数值
     */
    private Map<String, MonitoringParam> monitoringParamMap = new HashMap<>();
    /**
     * key:thingCode
     * value:当前所属智能控制阶段
     */
    private Map<String, String> currentStageMap = new HashMap<>();
    private static final int STAGE_LESS_LOWER = 1;
    private static final int STAGE_DURING_LOWER_HIGHER = 2;
    private static final int STAGE_GREATER_HIGHER = 3;
    private static final int STAGE_DEFAULT = 0;
    private static final String PARAM_LEVEL = "LEVEL";
    private static final String PARAM_VALVE_OPENING = "VALVEOPENING";
    private static final String PARAM_DENSITY = "DENSITY";
    private static final String PARAM_STATE = "STATE";
    public static final String NOTIFY_TYPE_ADDING_MEDIUM = "ADDING_MEDIUM";
    public static final String NOTIFY_TYPE_BACK_FLOW = "BACK_FLOW";
    public static final String NOTIFY_TYPE_ALERT = "NOTIFY_ALERT";

    private Map<String, String> paramTypeMap = new HashMap<String, String>() {
        {
            put(MetricCodes.CURRENT_DENSITY, PARAM_DENSITY);
            put(MetricCodes.SETTED_DENSITY, PARAM_DENSITY);
            put(MetricCodes.FLUCTUANT_DENSITY, PARAM_DENSITY);
            put(MetricCodes.PROPORTIONAL_VALVE_OPENING, PARAM_VALVE_OPENING);
            put(MetricCodes.PROPORTIONAL_SETTED_LOW_VALVE_OPENING, PARAM_VALVE_OPENING);
            put(MetricCodes.PROPORTIONAL_SETTED_HIGH_VALVE_OPENING, PARAM_VALVE_OPENING);
            put(MetricCodes.PRE_STOP_PROPORTIONAL_SETTED_LOW_VALVE_OPENING, PARAM_VALVE_OPENING);
            put(MetricCodes.CURRENT_LEVEL_M, PARAM_LEVEL);
            put(MetricCodes.SETTED_LOW_LEVEL, PARAM_LEVEL);
            put(MetricCodes.SETTED_HIGH_LEVEL, PARAM_LEVEL);
            put(MetricCodes.PRE_STOP_SETTED_HIGH_LEVEL, PARAM_LEVEL);
            put(MetricCodes.MODE_CHOOSE, PARAM_STATE);
            put(MetricCodes.INTELLIGENT_CONTROL, PARAM_STATE);
        }
    };

    @Autowired
    private DensityControlMapper densityControlMapper;
    @Autowired
    private DataService dataService;
    private Map<String, NotifyThingMetricInfo> notifyThingMetricInfoMap;

    @PostConstruct
    public void init() {
        notifyThingMetricInfoMap = densityControlMapper.getNotifyInfo();
    }

    public MonitoringParam getMonitoringParam(String thingCode) {
        NotifyThingMetricInfo notifyThingMetricInfo = notifyThingMetricInfoMap.get(thingCode);
        MonitoringParam monitoringParam = new MonitoringParam();
        monitoringParam.setThingCode(thingCode);
        if (notifyThingMetricInfo.getValveOpeningThingCode() != null) {
            if (dataService
                    .getData(notifyThingMetricInfo.getValveOpeningThingCode(), MetricCodes.PROPORTIONAL_VALVE_OPENING)
                    .isPresent()) {
                DataModelWrapper valveOpening =
                        dataService.getData(notifyThingMetricInfo.getValveOpeningThingCode(), MetricCodes.PROPORTIONAL_VALVE_OPENING).get();
                monitoringParam.setCurrentValveOpening(Double.valueOf(valveOpening.getValue()));
            }
        }
        if (notifyThingMetricInfo.getDensityThingCode() != null) {
            if (dataService.getData(notifyThingMetricInfo.getDensityThingCode(), MetricCodes.CURRENT_DENSITY)
                    .isPresent()) {
                DataModelWrapper density =
                        dataService.getData(notifyThingMetricInfo.getDensityThingCode(), MetricCodes.CURRENT_DENSITY).get();
                monitoringParam.setCurrentDensity(Double.valueOf(density.getValue()));
            }
        }
        if (notifyThingMetricInfo.getLevelThingCode() != null) {
            if (dataService.getData(notifyThingMetricInfo.getLevelThingCode(), MetricCodes.CURRENT_LEVEL_M)
                    .isPresent()) {
                DataModelWrapper level =
                        dataService.getData(notifyThingMetricInfo.getLevelThingCode(), MetricCodes.CURRENT_LEVEL_M).get();
                monitoringParam.setCurrentFuelLevel(Double.valueOf(level.getValue()));
            }
        }
        return monitoringParam;
    }

    public String getControlStage(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        int densityStage = 0;
        int levelStage = 0;
        int valveOpeningStage = 0;
        if (paramTypeMap.containsKey(metricCode)) {
            String type = paramTypeMap.get(metricCode);
            switch (type) {
                case PARAM_DENSITY:
                    densityStage = disposeDensity(dataModel);
                    break;
                case PARAM_LEVEL:
                    levelStage = disposeLevel(dataModel);
                    break;
                case PARAM_VALVE_OPENING:
                    valveOpeningStage = disposeValveOpening(dataModel);
                    break;
                case PARAM_STATE:
                    disposeState(dataModel);
                    break;
                default:
                    break;
            }
        }

        String stage = String.valueOf(densityStage) + levelStage + valveOpeningStage;
        return stage;
    }

    /**
     * 处理控制逻辑中的状态点
     * 
     * @param dataModel
     */
    private void disposeState(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Boolean value = Boolean.valueOf(dataModel.getValue());
        MonitoringParam monitoringParam = getMonitoringParamByThing(thingCode);
        switch (metricCode) {
            case MetricCodes.MODE_CHOOSE:
                monitoringParam.setPreStopState(value);
                break;
            case MetricCodes.INTELLIGENT_CONTROL:
                monitoringParam.setIntelligentState(value);
                if (value) {
                    if (currentStageMap.containsKey(thingCode)) {
                        checkStage(thingCode, currentStageMap.get(thingCode));
                    }
                } else {
                    cancelNotifyHandler.dispose(monitoringParam);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理密度
     * 
     * @param dataModel
     * @return
     */
    private int disposeDensity(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Double value = Double.valueOf(dataModel.getValue());
        MonitoringParam monitoringParam = getMonitoringParamByThing(thingCode);
        switch (metricCode) {
            case MetricCodes.CURRENT_DENSITY:
                monitoringParam.setCurrentDensity(value);
                break;
            case MetricCodes.SETTED_DENSITY:
                monitoringParam.setSettedDensity(value);
                break;
            case MetricCodes.FLUCTUANT_DENSITY:
                monitoringParam.setFluctuantDensity(value);
            default:
                break;
        }
        return getDensityStage(monitoringParam);

    }

    /**
     * 计算当前密度所处阶段
     * 
     * @param monitoringParam
     * @return
     */
    private int getDensityStage(MonitoringParam monitoringParam) {
        int densityStage = STAGE_DEFAULT;
        Double currentDensity = monitoringParam.getCurrentDensity();
        Double fluctuantDensity = monitoringParam.getFluctuantDensity();
        if (currentDensity != null && fluctuantDensity != null) {
            double lowDensity = currentDensity - fluctuantDensity;
            double highDensity = currentDensity + fluctuantDensity;
            if (currentDensity < lowDensity) {
                densityStage = STAGE_LESS_LOWER;
                monitoringParam.setDensityStage(STAGE_LESS_LOWER);
            } else if (currentDensity <= highDensity) {
                densityStage = STAGE_DURING_LOWER_HIGHER;
                monitoringParam.setDensityStage(STAGE_DURING_LOWER_HIGHER);
            } else {
                densityStage = STAGE_GREATER_HIGHER;
                monitoringParam.setDensityStage(STAGE_GREATER_HIGHER);
            }
        }
        return densityStage;
    }

    /**
     * 处理开度
     * 
     * @param dataModel
     * @return
     */
    private int disposeValveOpening(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Double value = Double.valueOf(dataModel.getValue());
        MonitoringParam monitoringParam = getMonitoringParamByThing(thingCode);
        switch (metricCode) {
            case MetricCodes.PROPORTIONAL_VALVE_OPENING:
                monitoringParam.setCurrentValveOpening(value);
                break;
            case MetricCodes.PROPORTIONAL_SETTED_LOW_VALVE_OPENING:
                monitoringParam.setSettedLowValveOpening(value);
                break;
            case MetricCodes.PROPORTIONAL_SETTED_HIGH_VALVE_OPENING:
                monitoringParam.setSettedHighValveOpening(value);
                break;
            case MetricCodes.PRE_STOP_PROPORTIONAL_SETTED_LOW_VALVE_OPENING:
                monitoringParam.setPreStopSettedLowValveOpening(value);
                break;
            default:
                break;
        }
        return getValveOpeningStage(monitoringParam);
    }

    /**
     * 计算当前开度所处阶段
     * 
     * @param monitoringParam
     * @return
     */
    private int getValveOpeningStage(MonitoringParam monitoringParam) {
        int valveStage = STAGE_DEFAULT;
        Double lowValveOpen;
        if (monitoringParam.getPreStopState()) {
            lowValveOpen = monitoringParam.getPreStopSettedLowValveOpening();
        } else {
            lowValveOpen = monitoringParam.getSettedLowValveOpening();
        }
        Double highValveOpen = monitoringParam.getSettedHighValveOpening();
        Double currentValueOpen = monitoringParam.getCurrentValveOpening();
        if (lowValveOpen != null && highValveOpen != null && currentValueOpen != null) {
            if (currentValueOpen <= lowValveOpen) {
                valveStage = STAGE_LESS_LOWER;
                monitoringParam.setValveOpeningStage(STAGE_LESS_LOWER);
            } else if (currentValueOpen < highValveOpen) {
                valveStage = STAGE_DURING_LOWER_HIGHER;
                monitoringParam.setValveOpeningStage(STAGE_DURING_LOWER_HIGHER);
            } else {
                valveStage = STAGE_GREATER_HIGHER;
                monitoringParam.setValveOpeningStage(STAGE_GREATER_HIGHER);
            }
        }
        return valveStage;
    }

    /**
     * 处理液位
     * 
     * @param dataModel
     * @return
     */
    private int disposeLevel(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Double value = Double.valueOf(dataModel.getValue());
        MonitoringParam monitoringParam = getMonitoringParamByThing(thingCode);
        switch (metricCode) {
            case MetricCodes.CURRENT_LEVEL_M:
                monitoringParam.setCurrentFuelLevel(value);
                break;
            case MetricCodes.SETTED_LOW_LEVEL:
                monitoringParam.setSettedLowLevel(value);
                break;
            case MetricCodes.SETTED_HIGH_LEVEL:
                monitoringParam.setSettedHighLevel(value);
                break;
            case MetricCodes.PRE_STOP_SETTED_HIGH_LEVEL:
                monitoringParam.setPreStopSettedHighLevel(value);
                break;
            default:
                break;
        }
        return getLevelStage(monitoringParam);
    }

    /**
     * 获取一个设备的智能控制相关状态及参数值
     * 
     * @param thingCode
     * @return
     */
    public MonitoringParam getMonitoringParamByThing(String thingCode) {
        MonitoringParam monitoringParam;
        if (monitoringParamMap.containsKey(thingCode)) {
            monitoringParam = monitoringParamMap.get(thingCode);
        } else {
            monitoringParam = new MonitoringParam();
            monitoringParam.setThingCode(thingCode);
            monitoringParamMap.put(thingCode, monitoringParam);
        }
        return monitoringParam;
    }

    /**
     * 计算当前液位所处阶段
     * 
     * @param monitoringParam
     * @return
     */
    private int getLevelStage(MonitoringParam monitoringParam) {
        int levelStage = STAGE_DEFAULT;
        Double settedHighLevel;
        if (monitoringParam.getPreStopState()) {
            settedHighLevel = monitoringParam.getPreStopSettedHighLevel();
        } else {
            settedHighLevel = monitoringParam.getSettedHighLevel();
        }
        Double settedLowLevel = monitoringParam.getSettedLowLevel();
        Double currentLevel = monitoringParam.getCurrentFuelLevel();
        if (currentLevel != null && settedHighLevel != null && settedLowLevel != null) {
            if (currentLevel <= settedLowLevel) {
                levelStage = STAGE_LESS_LOWER;
            } else if (currentLevel < settedHighLevel) {
                levelStage = STAGE_DURING_LOWER_HIGHER;
            } else {
                levelStage = STAGE_GREATER_HIGHER;
            }
        }
        return levelStage;
    }

    /**
     * 所处的智能控制状态是否发生改变
     * 
     * @param thingCode
     * @param stage
     * @return
     */
    public boolean isStageChange(String thingCode, String stage) {
        if (currentStageMap.containsKey(thingCode)) {
            String preStage = currentStageMap.get(thingCode);
            if (stage.equals(preStage)) {
                return false;
            } else {
                currentStageMap.put(thingCode, stage);
                return true;
            }
        } else {
            currentStageMap.put(thingCode, stage);
            return true;
        }
    }

    /**
     * 目前的状态应该进行的智能操作
     * 
     * @param thingCode
     * @param stage
     */
    public void checkStage(String thingCode, String stage) {
        MonitoringParam monitoringParam = monitoringParamMap.get(thingCode);
        if (monitoringParam.getIntelligentState() && stageDisposeMap.containsKey(stage)) {
            stageDisposeMap.get(stage).dispose(monitoringParam);
        } else {
            cancelNotifyHandler.dispose(monitoringParam);
        }
    }

    /**
     * 屏蔽掉无用的状态点
     * 
     * @param currentStage
     * @param maskIndex
     * @return
     */
    public String convertToRealStage(String currentStage, int maskIndex) {
        StringBuilder stringBuilder = new StringBuilder(currentStage);
        stringBuilder.setCharAt(maskIndex, '0');
        return stringBuilder.toString();
    }

    public List<MonitoringParam> getNotifyInfo(String thingCode) {
        List<MonitoringParam> monitoringParamList = new ArrayList<>();
        checkNotifyExist(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,thingCode,monitoringParamList);
        checkNotifyExist(DensityControlManager.NOTIFY_TYPE_ALERT,thingCode,monitoringParamList);
        checkNotifyExist(DensityControlManager.NOTIFY_TYPE_BACK_FLOW,thingCode,monitoringParamList);
        return monitoringParamList;
    }

    private void checkNotifyExist(String module,String thingCode,List<MonitoringParam> monitoringParamList){
        if (QuartzManager.checkExists(module, thingCode)) {
            MonitoringParam monitoringParam = getMonitoringParam(thingCode);
            monitoringParam.setCurrentStage(module);
            monitoringParamList.add(monitoringParam);
        }
    }

}
