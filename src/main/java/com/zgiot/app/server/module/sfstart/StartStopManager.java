package com.zgiot.app.server.module.sfstart;

import com.zgiot.app.server.module.sfstart.constants.StartStopConstants;
import com.zgiot.app.server.module.sfstart.controller.StartHandler;
import com.zgiot.app.server.module.sfstart.pojo.*;
import com.zgiot.app.server.module.sfstart.service.StartService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.zgiot.app.server.module.sfstart.constants.StartStopConstants.IS_REQUIREMENT;
import static com.zgiot.app.server.module.sfstart.controller.StartController.startDeviceRequirements;

/**
 * @author jys
 */
@Component
@Transactional
public class StartStopManager {
    @Autowired
    private StartService startService;
    @Autowired
    private DataService dataService;
    @Autowired
    private StartHandler startHandler;
    @Autowired
    private TMLMapper tmlMapper;

    private List<String> labelBydevices;

    public List<String> getLabelBydevices() {
        return labelBydevices;
    }

    public void setLabelBydevices(List<String> labelBydevices) {
        this.labelBydevices = labelBydevices;
    }

    private static final Logger logger = LoggerFactory.getLogger(StartStopManager.class);

    public void init() {

        List<String> deviceIds = startService.selectStartDeviceIdBySystemCategory(StartStopConstants.SYSTEM_CATEGORY_BROWSE_PAGE, null);
        HashSet<String> deviceIdSets = new HashSet<>(deviceIds);
        labelBydevices = startService.getLabelBydeviceIdsAndName(deviceIdSets, StartStopConstants.DEVICE_STATE);


    }

    /**
     * 前置条件判断
     */
    public void startDeviceByRequirement() {
        if (CollectionUtils.isNotEmpty(startDeviceRequirements)) {
            List<StartDeviceRequirement> findstartDeviceRequirements = new ArrayList<>(startDeviceRequirements);
            for (StartDeviceRequirement deviceRequirement : findstartDeviceRequirements) {
                logger.info("设备{}前置条件判断开始", deviceRequirement.getDeviceId());
                Double startPrecondition = null;
                StartSignal startSignal = startService.getStartSignalByDeviceId(deviceRequirement.getDeviceId());
                DataModelWrapper dataModelWrapper = dataService.getData(startSignal.getDeviceCode(), MetricCodes.Parameter_Cond).orElse(null);
                if (dataModelWrapper != null) {
                    startPrecondition = Double.valueOf(dataModelWrapper.getValue());
                }
                if (deviceRequirement.getStartRequirements() != null && deviceRequirement.getStartRequirements().size() > 0) {
                    judgeRequirements(deviceRequirement, startPrecondition);
                } else {
                    // 没有规则，则通知PLC启动设备
                    logger.info("deviceId:{}没有启车前置条件下发信号", deviceRequirement.getDeviceId());
                    startHandler.operateSignal(deviceRequirement.getDeviceId(), IS_REQUIREMENT, "没有启车前置条件", (float) StartStopConstants.VALUE_TRUE);
                    startDeviceRequirements.remove(deviceRequirement);
                }

            }

        }
    }

    /**
     * 判断启车前置条件
     *
     * @param deviceRequirement
     * @param startPrecondition
     */
    private void judgeRequirements(StartDeviceRequirement deviceRequirement, Double startPrecondition) {
        if (judgeRequirements(deviceRequirement.getStartRequirements()) && (float) StartStopConstants.VALUE_FALSE == startPrecondition) {
            // 规则满足通知PLC
            logger.info("deviceId:{}启车前置条件满足，下发信号", deviceRequirement.getDeviceId());
            startHandler.operateSignal(deviceRequirement.getDeviceId(), IS_REQUIREMENT, "启车前置条件满足", (float) StartStopConstants.VALUE_TRUE);
        }
        if (!judgeRequirements(deviceRequirement.getStartRequirements()) && (float) StartStopConstants.VALUE_TRUE == startPrecondition) {
            logger.info("deviceId:{}启车前置条件不满足，下发信号", deviceRequirement.getDeviceId());
            startHandler.operateSignal(deviceRequirement.getDeviceId(), IS_REQUIREMENT, "启车前置条件不满足", (float) StartStopConstants.VALUE_FALSE);
        }
    }

    /**
     * 判断启车前置条件是否满足
     *
     * @param requirements 启车前置条件
     * @return
     */
    public Boolean judgeRequirements(List<StartRequirement> requirements) {
        Boolean flag = true;
        try {
            for (StartRequirement requirement : requirements) {
                if (!startHandler.judgeSingleRequirement(requirement)) {
                    flag = false;
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            logger.error("判断启车前置条件判断错误" + e);
        }
        return flag;
    }

    /**
     * 发送总览页面带煤量给前端
     */
    public void sendCoalCapacity() {

        List<String> deviceIds = startService.selectStartDeviceIdBySystemCategory(StartStopConstants.SYSTEM_CATEGORY_BROWSE_PAGE, StartStopConstants.SYSTEM_TYPE_BROWSE_COAL);
        List<StartDevice> startDeviceCoalCapacitys = new ArrayList<>();
        for (String deviceId : deviceIds) {
            StartSignal startSignal = startService.getStartSignalByDeviceId(deviceId);
            StartDevice startDevice = new StartDevice();
            startDevice.setDeviceCode(startSignal.getDeviceCode());
            startDevice.setDeviceId(deviceId);
            DataModelWrapper dataModelWrapper = dataService.getData(startSignal.getDeviceCode(), MetricCodes.COAL_CAP).orElse(null);
            if (dataModelWrapper != null) {
                startDevice.setCoalCapacity(Double.valueOf(dataModelWrapper.getValue()));
                startDeviceCoalCapacitys.add(startDevice);
            }
        }
        // 发送带煤量集合
        if (CollectionUtils.isNotEmpty(startDeviceCoalCapacitys)) {
            logger.debug("发送带煤量信号数量{}", startDeviceCoalCapacitys.size());
            startHandler.sendMessagingTemplate(StartStopConstants.URI_START_BROWSE_COALCAPACITY, startDeviceCoalCapacitys);
        }
    }

    /**
     * 发送总览页面仓库库存给前端
     */
    public void sendCoalDeport() {
        List<String> deviceIds = startService.selectStartDeviceIdBySystemCategory(StartStopConstants.SYSTEM_CATEGORY_COAL_DEPOT_PAGE, null);
        List<StartBrowseCoalDevice> startBrowseCoalDevices = new ArrayList<>();
        for (String deviceId : deviceIds) {
            StartSignal startSignal = startService.getStartSignalByDeviceId(deviceId);
            StartDeviceSignal startDeviceSignal = startService.getStartDeviceSignalById(startSignal.getName());
            MetricModel metricModel = tmlMapper.findMetricByMetricName(startDeviceSignal.getName());
            StartBrowseCoalDevice startBrowseCoalDevice = new StartBrowseCoalDevice();
            startBrowseCoalDevice.setDeviceCode(startSignal.getDeviceCode());
            startBrowseCoalDevice.setDeviceId(deviceId);
            DataModelWrapper dataModelWrapper = dataService.getData(startSignal.getDeviceCode(), metricModel.getMetricCode()).orElse(null);
            if (dataModelWrapper != null) {
                if (StartStopConstants.DEPOT_ONE.equals(String.valueOf(startSignal.getName()))) {
                    startBrowseCoalDevice.setCoalDeportOne(Double.valueOf(dataModelWrapper.getValue()));

                } else if (StartStopConstants.DEPOT_TWO.equals(String.valueOf(startSignal.getName()))) {
                    startBrowseCoalDevice.setCoalDeportTwo(Double.valueOf(dataModelWrapper.getValue()));
                }
                startBrowseCoalDevices.add(startBrowseCoalDevice);
            } else {
                StartBrowseCoalDevice startBrowseCoalNull = new StartBrowseCoalDevice();
                startBrowseCoalNull.setDeviceId(deviceId);
                startBrowseCoalDevices.add(startBrowseCoalNull);
            }
        }
        logger.trace("发送仓库库存信号数量{}", startBrowseCoalDevices.size());
        startHandler.sendMessagingTemplate(StartStopConstants.URI_START_BROWSE_COALDEPORT, startBrowseCoalDevices);
    }
}
