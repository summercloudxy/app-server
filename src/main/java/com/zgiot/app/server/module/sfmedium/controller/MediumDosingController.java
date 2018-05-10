package com.zgiot.app.server.module.sfmedium.controller;


import com.google.common.collect.Lists;
import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfmedium.constants.SfMediumConstants;
import com.zgiot.app.server.module.sfmedium.entity.po.MediumDosingConfigDO;
import com.zgiot.app.server.module.sfmedium.entity.vo.*;
import com.zgiot.app.server.module.sfmedium.service.MediumDosingService;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.pojo.ThingPropertyModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 加介功能
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfmedium", produces = MediaType.APPLICATION_JSON_VALUE)
public class MediumDosingController {
    private static final Logger logger = LoggerFactory.getLogger(MediumDosingController.class);
    private static final String MESSAGE_URI = "/topic/mediumdosing/message";

    @Autowired
    private MediumDosingService mediumDosingService;
    @Autowired
    private DataService dataService;
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private TMLMapper tmlMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private CmdControlService cmdControlService;

    @RequestMapping(value = "/getMediumDosingSystem", method = RequestMethod.GET)
    public ResponseEntity<String> getMediumDosingSystem() {
        List<MediumDosingConfigDO> allMediumDosing = mediumDosingService.getMediumDosingGroup();
        List<MediumDosingSystemVO> mediumDosingSystemList = new ArrayList<>();
        for (MediumDosingConfigDO mediumDosingConfig : allMediumDosing) {
            MediumDosingSystemVO mediumDosingSystem = new MediumDosingSystemVO();
            mediumDosingSystem.setMediumDosingSystemName(mediumDosingConfig.getMediumdosingsystemName());
            mediumDosingSystem.setMediumDosingSystemId(mediumDosingConfig.getMediumdosingsystemId());

            //分选系统
            getSeparatingSystemList(mediumDosingConfig, mediumDosingSystem);
            //补水阀
            getWaterSupplementValve(mediumDosingConfig, mediumDosingSystem);
            //鼓风阀
            getBlowerValve(mediumDosingConfig, mediumDosingSystem);

            //介质池
            getMediumPool(mediumDosingConfig, mediumDosingSystem);

            //加介泵
            getMediumDosingPump(mediumDosingConfig, mediumDosingSystem);

            mediumDosingSystemList.add(mediumDosingSystem);
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(mediumDosingSystemList), HttpStatus.OK);
    }

    /**
     * 鼓风阀
     *
     * @param mediumDosingConfig
     * @param mediumDosingSystem
     */
    private void getBlowerValve(MediumDosingConfigDO mediumDosingConfig, MediumDosingSystemVO mediumDosingSystem) {
        MediumDosingSystemVO.BlowerValve blowerValve = mediumDosingSystem.new BlowerValve();
        blowerValve.setThingName(SfMediumConstants.BLOWERVALVE_THINGNAME);
        blowerValve.setThingCode(mediumDosingConfig.getBlowervalveCode());
        blowerValve.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
        blowerValve.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
        blowerValve.setTapOpenMetricValue(getMetricDataValue(mediumDosingConfig.getBlowervalveCode(), MetricCodes.TAP_OPEN));
        blowerValve.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
        blowerValve.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
        blowerValve.setTapCloseMetricValue(getMetricDataValue(mediumDosingConfig.getBlowervalveCode(), MetricCodes.TAP_CLOSE));
        mediumDosingSystem.setBlowerValve(blowerValve);
    }

    /**
     * 加介泵
     *
     * @param mediumDosingConfig
     * @param mediumDosingSystem
     */
    private void getMediumDosingPump(MediumDosingConfigDO mediumDosingConfig, MediumDosingSystemVO mediumDosingSystem) {
        MediumDosingSystemVO.MediumDosingPump mediumDosingPump = mediumDosingSystem.new MediumDosingPump();
        mediumDosingPump.setThingName(mediumDosingConfig.getMediumdosingpumpCode() + SfMediumConstants.MEDIUM_DOSING_PUMP_NAME);
        mediumDosingPump.setThingCode(mediumDosingConfig.getMediumdosingpumpCode());
        mediumDosingPump.setThingImageName(getThingImageName(mediumDosingConfig.getMediumdosingpumpCode()));
        mediumDosingPump.setRunState(getMetricDataValue(mediumDosingConfig.getMediumdosingpumpCode(), MetricCodes.STATE));

        //泵告警等级
        Map<String, Short> seriousAlertLevel = alertManager.getSeriousAlertLevel(Lists.newArrayList(mediumDosingConfig.getMediumdosingpumpCode()));
        if (MapUtils.isNotEmpty(seriousAlertLevel)) {
            Short alertLevel = seriousAlertLevel.get(mediumDosingConfig.getMediumdosingpumpCode());
            mediumDosingPump.setAlertLevel(String.valueOf(alertLevel));
        } else {
            mediumDosingPump.setAlertLevel("");
        }
        //判断电流 智能 集控 就地模式
        getPatternMetric(mediumDosingConfig, mediumDosingPump);
        mediumDosingSystem.setMediumDosingPump(mediumDosingPump);
    }

    /**
     * 判断智能 集控 就地模式
     *
     * @param mediumDosingConfig
     * @param mediumDosingPump
     */
    private void getPatternMetric(MediumDosingConfigDO mediumDosingConfig, MediumDosingSystemVO.MediumDosingPump mediumDosingPump) {
        mediumDosingPump.setCurrentMetricCode(MetricCodes.CURRENT);
        mediumDosingPump.setCurrentMetricName(SfMediumConstants.CURRENT_NAME);
        mediumDosingPump.setCurrentMetricValue(getMetricDataValue(mediumDosingConfig.getMediumdosingpumpCode(), MetricCodes.CURRENT));
        mediumDosingPump.setIntelligentMetricCode(MetricCodes.INTELLIGENT_CONTROL);
        mediumDosingPump.setIntelligentMetricName(SfMediumConstants.INTELLIGENT);
        mediumDosingPump.setIntelligentMetricValue(getMetricDataValue(mediumDosingConfig.getMediumdosingpumpCode(), MetricCodes.INTELLIGENT_CONTROL));
        mediumDosingPump.setLocalMetricCode(MetricCodes.LOCAL);
        mediumDosingPump.setLocalMetricName(SfMediumConstants.LOCAL_METRIC_NAME);
        mediumDosingPump.setLocalMetricValue(getMetricDataValue(mediumDosingConfig.getMediumdosingpumpCode(), MetricCodes.LOCAL));
    }

    /**
     * 介质池
     *
     * @param mediumDosingConfig
     * @param mediumDosingSystem
     */
    private void getMediumPool(MediumDosingConfigDO mediumDosingConfig, MediumDosingSystemVO mediumDosingSystem) {
        MediumDosingSystemVO.MediumPool mediumPool = mediumDosingSystem.new MediumPool();
        if (SfMediumConstants.MEDIUM_POOL_1.equals(mediumDosingConfig.getMediumdosingsystemId())) {
            mediumPool.setThingName(SfMediumConstants.MEDIUMPOOL_1);
        } else if (SfMediumConstants.MEDIUM_POOL_2.equals(mediumDosingConfig.getMediumdosingsystemId())) {
            mediumPool.setThingName(SfMediumConstants.MEDIUMPOOL_2);
        } else if (SfMediumConstants.MEDIUM_POOL_3.equals(mediumDosingConfig.getMediumdosingsystemId())) {
            mediumPool.setThingName(SfMediumConstants.MEDIUMPOOL_3);
        }
        mediumPool.setThingCode(mediumDosingConfig.getMediumpoolCode());
        mediumPool.setThingImageName(getThingImageName(mediumDosingConfig.getMediumpoolCode()));
        List<MediumDosingSystemVO.MetricData> metricDataList = new ArrayList<>();
        MediumDosingSystemVO.MetricData metricData1 = mediumDosingSystem.new MetricData();
        metricData1.setMetricCode(MetricCodes.CURRENT_LEVEL_M);
        metricData1.setMetricName(SfMediumConstants.LEVEL_METRIC_NAME);
        String metricData = getMetricDataValue(mediumDosingConfig.getMediumpoolCode(), MetricCodes.CURRENT_LEVEL_M);
        metricData1.setMetricValue(metricData);
        if (StringUtils.isNotEmpty(metricData)) {
            BigDecimal levelPrecent = new BigDecimal(metricData).divide(BigDecimal.valueOf(SfMediumConstants.MEDIUM_POOL_HIGH), 3, BigDecimal.ROUND_HALF_UP);
            mediumPool.setLevelPercent(String.valueOf((levelPrecent).multiply(new BigDecimal(100)).setScale(1)));
        } else {
            mediumPool.setLevelPercent("");
        }


        MediumDosingSystemVO.MetricData metricData2 = mediumDosingSystem.new MetricData();
        metricData2.setMetricCode(MetricCodes.STATE);
        metricData2.setMetricName(SfMediumConstants.MEDIUM_COMPOUNDING_STATE);
        String metricDataValue = getMetricDataValue(mediumDosingConfig.getMediumpoolCode(), MetricCodes.STATE);
        metricData2.setMetricValue(metricDataValue);
        if (StringUtils.isNotEmpty(metricDataValue)) {
            if ("1".equals(metricDataValue)) {
                metricData2.setMetricValueName(SfMediumConstants.MEDIUM_COMPOUNDING_STATE_1);
            } else if ("0".equals(metricDataValue)) {
                metricData2.setMetricValueName(SfMediumConstants.MEDIUM_COMPOUNDING_STATE_0);
            } else if ("2".equals(metricDataValue)) {
                metricData2.setMetricValueName(SfMediumConstants.MEDIUM_COMPOUNDING_STATE_2);
            }
        } else {
            metricData2.setMetricValueName("");
        }
        metricDataList.add(metricData1);
        metricDataList.add(metricData2);
        mediumPool.setMetricDataList(metricDataList);
        mediumDosingSystem.setMediumPool(mediumPool);


    }

    /**
     * 补水阀
     *
     * @param mediumDosingConfig
     * @param mediumDosingSystem
     */
    private void getWaterSupplementValve(MediumDosingConfigDO mediumDosingConfig, MediumDosingSystemVO mediumDosingSystem) {
        MediumDosingSystemVO.WaterSupplementValve waterSupplementValve = mediumDosingSystem.new WaterSupplementValve();
        waterSupplementValve.setThingCode(mediumDosingConfig.getWatersupplementvalveCode());
        waterSupplementValve.setThingName(SfMediumConstants.WATERSUPPLEMENTVALVE_THINGNAME);
        waterSupplementValve.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
        waterSupplementValve.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
        waterSupplementValve.setTapOpenMetricValue(getMetricDataValue(mediumDosingConfig.getWatersupplementvalveCode(), MetricCodes.TAP_OPEN));
        waterSupplementValve.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
        waterSupplementValve.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
        waterSupplementValve.setTapCloseMetricValue(getMetricDataValue(mediumDosingConfig.getWatersupplementvalveCode(), MetricCodes.TAP_CLOSE));
        mediumDosingSystem.setWaterSupplementValve(waterSupplementValve);
    }

    /**
     * 查询分选系统
     *
     * @param mediumDosingConfig
     * @param mediumDosingSystem
     */
    private void getSeparatingSystemList(MediumDosingConfigDO mediumDosingConfig, MediumDosingSystemVO mediumDosingSystem) {
        List<MediumDosingSystemVO.SeparatingSystem> separatingSystemList = new ArrayList<>();
        List<MediumDosingConfigDO> separatingSystems = mediumDosingService.getSeparatingSystemById(mediumDosingConfig.getMediumdosingsystemId());
        for (MediumDosingConfigDO separatingSystem : separatingSystems) {
            MediumDosingSystemVO.SeparatingSystem sep = mediumDosingSystem.new SeparatingSystem();
            String separatingSystemMediumDosingState = getSeparatingSystemMediumDosingState(mediumDosingConfig);
            if (StringUtils.isNotEmpty(separatingSystemMediumDosingState)) {
                sep.setThingCode(separatingSystem.getCombinedbucketCode());
                sep.setThingName(separatingSystem.getSeparatingsystemName());
                String stateShow = null;
                if (separatingSystemMediumDosingState.equals("1")) {
                    stateShow = SfMediumConstants.MEDIUM_DOSING_STATE_1;
                } else if (separatingSystemMediumDosingState.equals("0")) {
                    stateShow = SfMediumConstants.MEDIUM_DOSING_STATE_0;
                }
                sep.setMetricValueName(stateShow);
                separatingSystemList.add(sep);
            }
            mediumDosingSystem.setSeparatingSystemList(separatingSystemList);
        }
    }

    /**
     * 快捷加介选择系统
     *
     * @return
     */
    @RequestMapping(value = "/getMediumDosingSystemGroup", method = RequestMethod.GET)
    public ResponseEntity<String> getMediumDosingSystemGroup() {
        List<MediumDosingSystemGroupVO> mediumDosingSystemGroupVOList = new ArrayList<>();
        List<MediumDosingConfigDO> mediumDosingGroup = mediumDosingService.getMediumDosingGroup();
        for (MediumDosingConfigDO mediumDosingConfigDO : mediumDosingGroup) {
            MediumDosingSystemGroupVO mediumDosingSystemGroupVO = new MediumDosingSystemGroupVO();
            if (mediumDosingConfigDO.getMediumdosingsystemId().equals(SfMediumConstants.MEDIUM_POOL_1)) {
                mediumDosingSystemGroupVO.setMediumDosingSystemName(SfMediumConstants.MEDIUM_DOSING_SYSTEM_1);
            } else if (mediumDosingConfigDO.getMediumdosingsystemId().equals(SfMediumConstants.MEDIUM_POOL_2)) {
                mediumDosingSystemGroupVO.setMediumDosingSystemName(SfMediumConstants.MEDIUM_DOSING_SYSTEM_2);
            } else if (mediumDosingConfigDO.getMediumdosingsystemId().equals(SfMediumConstants.MEDIUM_POOL_3)) {
                mediumDosingSystemGroupVO.setMediumDosingSystemName(SfMediumConstants.MEDIUM_DOSING_SYSTEM_3);
            }
            List<MediumDosingSystemGroupVO.SeparatingSystem> separatingSystems = new ArrayList<>();
            List<MediumDosingConfigDO> mediumDosingSystems = mediumDosingService.getChangeValue(mediumDosingConfigDO.getMediumdosingpumpCode());
            for (MediumDosingConfigDO subMediumDosingSystem : mediumDosingSystems) {
                MediumDosingSystemGroupVO.SeparatingSystem separatingSystem = mediumDosingSystemGroupVO.new SeparatingSystem();
                separatingSystem.setSeparatingSystemName(subMediumDosingSystem.getSystemName());
                List<MediumDosingConfigDO> subMediumDosingSystems = mediumDosingService.getMediumdosingvalueAndCombinedbucketCode(subMediumDosingSystem.getMediumdosingpumpCode(), subMediumDosingSystem.getChangevalueCode());
                List<String> combinedbucketCodes = new ArrayList<>();
                for (MediumDosingConfigDO combinedbucketCode : subMediumDosingSystems) {
                    combinedbucketCodes.add(combinedbucketCode.getCombinedbucketCode());
                }
                separatingSystem.setMediumDosingSystem(combinedbucketCodes);
                separatingSystems.add(separatingSystem);
            }
            mediumDosingSystemGroupVO.setSeparatingSystems(separatingSystems);
            mediumDosingSystemGroupVOList.add(mediumDosingSystemGroupVO);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(mediumDosingSystemGroupVOList), HttpStatus.OK);
    }


    /**
     * 介质池配介控制
     *
     * @param mediumPoolThingCode
     * @return
     */
    @RequestMapping(value = "/getMediumCompounding", method = RequestMethod.GET)
    public ResponseEntity<String> getMediumCompounding(@RequestParam("mediumPoolThingCode") String mediumPoolThingCode) {
        MediumCompoundingVO mediumCompoundingVO = new MediumCompoundingVO();
        //配介液位  最低液位
        //TODO 配介液位由设备新增的信号点决定 plc提供 不从表中查询
        List<MediumDosingConfigDO> mediumDosingConfigByMediumPoolCode = mediumDosingService.getMediumDosingConfigByMediumPoolCode(mediumPoolThingCode);
        if (CollectionUtils.isNotEmpty(mediumDosingConfigByMediumPoolCode)) {
            mediumCompoundingVO.setMediumCompoundingLevel(getMetricDataValue(mediumDosingConfigByMediumPoolCode.get(0).getWatersupplementvalveCode(), MetricCodes.LE_PJ_SET));
            mediumCompoundingVO.setLowLevel(getMetricDataValue(mediumDosingConfigByMediumPoolCode.get(0).getWatersupplementvalveCode(), MetricCodes.LE_ZD_SET));
            //加介状态
            mediumCompoundingVO.setMediumCompoundingState(getMetricDataValue(mediumPoolThingCode, MetricCodes.STATE));
            List<MediumDosingConfigDO> mediumDosingConfigs = mediumDosingService.getMediumDosingConfigByMediumPoolCode(mediumPoolThingCode);
            if (CollectionUtils.isNotEmpty(mediumDosingConfigs)) {
                //补水阀门
                getWaterSupplementValve(mediumCompoundingVO, mediumDosingConfigs);
                //鼓风阀门
                getBlowerValve(mediumCompoundingVO, mediumDosingConfigs);
            }
            mediumCompoundingVO.setMediumPoolThingCode(mediumPoolThingCode);
            mediumCompoundingVO.setMediumCompoundingMetricCode(MetricCodes.STATE);
            return new ResponseEntity<>(ServerResponse.buildOkJson(mediumCompoundingVO), HttpStatus.OK);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 鼓风阀门
     *
     * @param mediumCompoundingVO
     * @param mediumDosingConfigs
     */
    private void getBlowerValve(MediumCompoundingVO mediumCompoundingVO, List<MediumDosingConfigDO> mediumDosingConfigs) {
        MediumCompoundingVO.BlowerValve blowerValve = mediumCompoundingVO.new BlowerValve();
        blowerValve.setThingName(SfMediumConstants.BLOWERVALVE_THINGNAME);
        blowerValve.setThingCode(mediumDosingConfigs.get(0).getBlowervalveCode());
        blowerValve.setThingImageName(getThingImageName(mediumDosingConfigs.get(0).getBlowervalveCode()));
        blowerValve.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
        blowerValve.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
        blowerValve.setTapOpenMetricValue(getMetricDataValue(mediumDosingConfigs.get(0).getBlowervalveCode(), MetricCodes.TAP_OPEN));
        blowerValve.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
        blowerValve.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
        blowerValve.setTapCloseMeticValue(getMetricDataValue(mediumDosingConfigs.get(0).getBlowervalveCode(), MetricCodes.TAP_CLOSE));
        mediumCompoundingVO.setBlowerValve(blowerValve);
    }

    /**
     * 补水阀门
     *
     * @param mediumCompoundingVO
     * @param mediumDosingConfigs
     */
    private void getWaterSupplementValve(MediumCompoundingVO mediumCompoundingVO, List<MediumDosingConfigDO> mediumDosingConfigs) {
        MediumCompoundingVO.WaterSupplementValve waterSupplementValve = mediumCompoundingVO.new WaterSupplementValve();
        waterSupplementValve.setThingName(SfMediumConstants.WATERSUPPLEMENTVALVE_THINGNAME);
        String watersupplementValve = mediumDosingConfigs.get(0).getWatersupplementvalveCode();
        waterSupplementValve.setThingCode(watersupplementValve);
        waterSupplementValve.setStartMetricCode(MetricCodes.START_CMD);
        waterSupplementValve.setStopMetricCode(MetricCodes.STOP_CMD);
        waterSupplementValve.setThingImageName(getThingImageName(watersupplementValve));
        waterSupplementValve.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
        waterSupplementValve.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
        waterSupplementValve.setTapOpenMetricValue(getMetricDataValue(watersupplementValve, MetricCodes.TAP_OPEN));
        waterSupplementValve.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
        waterSupplementValve.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
        waterSupplementValve.setTapCloseMeticValue(getMetricDataValue(watersupplementValve, MetricCodes.TAP_CLOSE));
        waterSupplementValve.setLevelLockMetricCode(MetricCodes.LEVEL_LOCK);
        waterSupplementValve.setLevelLockMetricName(SfMediumConstants.LEVEL_LOCK_METRIC_NAME);
        waterSupplementValve.setLevelLockMetricValue(getMetricDataValue(watersupplementValve, MetricCodes.LEVEL_LOCK));
        mediumCompoundingVO.setWaterSupplementValve(waterSupplementValve);
    }

    /**
     * 快速配介
     *
     * @return
     */
    @RequestMapping(value = "/quickMediumCompounding", method = RequestMethod.POST)
    public ResponseEntity<String> quickMediumCompounding(@RequestParam("mediumPoolThingCode") String mediumPoolThingCode) {
        logger.info("开始对{} 介质池进行快捷配介", mediumPoolThingCode);
        List<MediumDosingConfigDO> mediumDosingConfigDOList = mediumDosingService.getMediumDosingConfigByMediumPoolCode(mediumPoolThingCode);
        String metricDataValue = getMetricDataValue(mediumDosingConfigDOList.get(0).getMediumdosingpumpCode(), MetricCodes.STATE);
        if (StringUtils.isNotEmpty(metricDataValue)) {
            if (metricDataValue.equals("2")) {
                simpMessagingTemplate.convertAndSend(MESSAGE_URI, mediumDosingConfigDOList.get(0).getMediumdosingpumpCode() + " 加介泵，当前正在运行中，请稍后");
            } else if (metricDataValue.equals("4")) {
                simpMessagingTemplate.convertAndSend(MESSAGE_URI, mediumDosingConfigDOList.get(0).getMediumdosingpumpCode() + " 加介泵，发生故障，无法配介");
            } else if (metricDataValue.equals("1")) {
                DataModel dataModel = new DataModel();
                dataModel.setThingCode(mediumDosingConfigDOList.get(0).getMediumpoolCode());
                dataModel.setMetricCode(MetricCodes.QUICKMIX);
                dataModel.setValue("1");
                logger.info("下发信号 thingCode：{},metricCode：{},value：1 ", mediumDosingConfigDOList.get(0).getMediumpoolCode(), MetricCodes.QUICKMIX);
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, SessionContext.getCurrentUser().getRequestId());
                if (cmdSendResponseData.getOkCount() == 0) {
                    logger.error(cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
                    simpMessagingTemplate.convertAndSend(MESSAGE_URI, "配介命令下发失败");
                } else {
                    simpMessagingTemplate.convertAndSend(MESSAGE_URI, "配介命令下发成功");
                }

            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 快速加介
     *
     * @param combinedbucketCode
     * @return
     */
    @RequestMapping(value = "/quickMediumDosing", method = RequestMethod.POST)
    public ResponseEntity<String> quickMediumDosing(@RequestParam("combinedbucketCode") String combinedbucketCode) {
        logger.info("开始对{} 合介桶进行快捷加介", combinedbucketCode);
        MediumDosingConfigDO mediumDosingConfigDO = mediumDosingService.getMediumDosingConfigDO(combinedbucketCode);
        String metricDataValue = getMetricDataValue(mediumDosingConfigDO.getMediumdosingpumpCode(), MetricCodes.STATE);
        if (StringUtils.isNotEmpty(metricDataValue)) {
            if (metricDataValue.equals("2")) {
                simpMessagingTemplate.convertAndSend(MESSAGE_URI, mediumDosingConfigDO.getMediumdosingpumpCode() + " 加介泵，当前正在运行中，请稍后");
            } else if (metricDataValue.equals("4")) {
                simpMessagingTemplate.convertAndSend(MESSAGE_URI, mediumDosingConfigDO.getMediumdosingpumpCode() + " 加介泵，发生故障，无法加介");
            } else if (metricDataValue.equals("1")) {
                DataModel dataModel = new DataModel();
                dataModel.setThingCode(mediumDosingConfigDO.getMediumdosingpumpCode());
                dataModel.setMetricCode(MetricCodes.JJ_ADD);
                dataModel.setValue("1");
                logger.info("下发信号 thingCode：{},metricCode：{},value：1 ", mediumDosingConfigDO.getMediumdosingpumpCode(), MetricCodes.JJ_ADD);
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, SessionContext.getCurrentUser().getRequestId());
                if (cmdSendResponseData.getOkCount() == 0) {
                    logger.error(cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
                    simpMessagingTemplate.convertAndSend(MESSAGE_URI, "加介命令下发失败");
                } else {
                    simpMessagingTemplate.convertAndSend(MESSAGE_URI, "加介命令下发成功");
                }

            }
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 加介控制
     *
     * @param thingCode
     * @return
     */
    @RequestMapping(value = "/getMediumDosing", method = RequestMethod.GET)
    public ResponseEntity<String> mediumDosing(@RequestParam("mediumdosingvalueCode") String thingCode) {
        MediumDosingControlVO mediumDosingControlVO = new MediumDosingControlVO();
        //加介泵
        getMediumDosingPump(thingCode, mediumDosingControlVO);
        //阀门状态
        List<MediumDosingControlVO.ValueControl> valueControls = new ArrayList<>();
        List<MediumDosingConfigDO> changeValues = mediumDosingService.getChangeValue(thingCode);
        for (MediumDosingConfigDO mediumDosingConfigDO : changeValues) {
            MediumDosingControlVO.ValueControl valueControl = mediumDosingControlVO.new ValueControl();
            //切换阀
            getChangeValue(mediumDosingControlVO, mediumDosingConfigDO, valueControl);
            valueControls.add(valueControl);
        }
        for (MediumDosingConfigDO mediumDosingConfigDO : changeValues) {
            MediumDosingControlVO.ValueControl valueControl = mediumDosingControlVO.new ValueControl();
            //1号加介池 特殊处理title2 显示一期块煤加介桶 / 二期块煤加介桶
            if (mediumDosingConfigDO.getSystem().equals(GlobalConstants.SYSTEM_01)) {
                if (mediumDosingConfigDO.getMediumdosingsystemId().equals(SfMediumConstants.MEDIUM_POOL_1)) {
                    valueControl.setTitle(mediumDosingConfigDO.getSystemName() + SfMediumConstants.COMBINEDBUCKET_THINGNAME);
                } else {
                    valueControl.setTitle(GlobalConstants.SYSTEM_ONE + SfMediumConstants.MEDIUMDOSINGVALUE_THINGNAME);
                }
            } else if (mediumDosingConfigDO.getSystem().equals(GlobalConstants.SYSTEM_02)) {
                if (mediumDosingConfigDO.getMediumdosingsystemId().equals(SfMediumConstants.MEDIUM_POOL_1)) {
                    valueControl.setTitle(mediumDosingConfigDO.getSystemName() + SfMediumConstants.COMBINEDBUCKET_THINGNAME);
                } else {
                    valueControl.setTitle(GlobalConstants.SYSTEM_TWO + SfMediumConstants.MEDIUMDOSINGVALUE_THINGNAME);
                }
            }
            //加介阀  合介桶
            getMediumDosingValuesAndCombinedBuckets(thingCode, mediumDosingControlVO, mediumDosingConfigDO, valueControl);
            valueControls.add(valueControl);
        }

        mediumDosingControlVO.setValueControls(valueControls);
        return new ResponseEntity<>(ServerResponse.buildOkJson(mediumDosingControlVO), HttpStatus.OK);
    }

    /**
     * 加介阀  合介桶
     *
     * @param thingCode
     * @param mediumDosingControlVO
     * @param mediumDosingConfigDO
     * @param valueControl
     */
    private void getMediumDosingValuesAndCombinedBuckets(String thingCode, MediumDosingControlVO mediumDosingControlVO, MediumDosingConfigDO mediumDosingConfigDO, MediumDosingControlVO.ValueControl valueControl) {
        List<MediumDosingControlVO.MediumDosingValue> mediumDosingValues = new ArrayList<>();
        List<MediumDosingControlVO.CombinedBucket> combinedBuckets = new ArrayList<>();
        List<MediumDosingConfigDO> mediumDosingConfigLists = mediumDosingService.getMediumdosingvalueAndCombinedbucketCode(thingCode, mediumDosingConfigDO.getChangevalueCode());
        for (MediumDosingConfigDO mediumDosingConfig : mediumDosingConfigLists) {
            if (!mediumDosingConfig.getMediumdosingsystemId().equals(SfMediumConstants.MEDIUM_POOL_1)) {
                MediumDosingControlVO.MediumDosingValue mediumDosingValue = mediumDosingControlVO.new MediumDosingValue();
                mediumDosingValue.setThingCode(mediumDosingConfig.getMediumdosingvalueCode());
                mediumDosingValue.setThingName(mediumDosingConfig.getSeparatingsystemName() + SfMediumConstants.MEDIUMDOSINGVALUE_THINGNAME);
                mediumDosingValue.setThingImageName(getThingImageName(mediumDosingConfig.getMediumdosingvalueCode()));
                mediumDosingValue.setStartMetricCode(MetricCodes.START_CMD);
                mediumDosingValue.setStopMetricCode(MetricCodes.STOP_CMD);

                mediumDosingValue.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
                mediumDosingValue.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
                mediumDosingValue.setTapOpenMetricValue(getMetricDataValue(mediumDosingConfig.getMediumdosingvalueCode(), MetricCodes.TAP_OPEN));
                mediumDosingValue.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
                mediumDosingValue.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
                mediumDosingValue.setTapCloseMetricValue(getMetricDataValue(mediumDosingConfig.getMediumdosingvalueCode(), MetricCodes.TAP_CLOSE));
                mediumDosingValues.add(mediumDosingValue);
            }
            valueControl.setMediumDosingValues(mediumDosingValues);


            MediumDosingControlVO.CombinedBucket combinedBucket = mediumDosingControlVO.new CombinedBucket();
            combinedBucket.setThingCode(mediumDosingConfig.getCombinedbucketCode());
            combinedBucket.setThingName(mediumDosingConfig.getSeparatingsystemName() + SfMediumConstants.COMBINEDBUCKET_THINGNAME);
            combinedBucket.setDensityMetricCode(MetricCodes.CURRENT_DENSITY);
            combinedBucket.setDensityMetricName(SfMediumConstants.CURRENT_DENSITY_METRIC_NAME);
            combinedBucket.setDensityMetricValue(getMetricDataValue(mediumDosingConfig.getCombinedbucketCode(), MetricCodes.CURRENT_DENSITY));

            combinedBucket.setLevelMetricCode(MetricCodes.CURRENT_LEVEL_M);
            combinedBucket.setLevelMetricName(SfMediumConstants.LEVEL_METRIC_NAME);
            combinedBucket.setLevelMetricValue(getMetricDataValue(mediumDosingConfig.getCombinedbucketCode(), MetricCodes.CURRENT_LEVEL_M));
            combinedBuckets.add(combinedBucket);
            valueControl.setCombinedBuckets(combinedBuckets);
        }
    }

    /**
     * 切换阀
     *
     * @param mediumDosingControlVO
     * @param mediumDosingConfigDO
     * @param valueControl
     */
    private void getChangeValue(MediumDosingControlVO mediumDosingControlVO, MediumDosingConfigDO mediumDosingConfigDO, MediumDosingControlVO.ValueControl valueControl) {
        MediumDosingControlVO.MediumDosingValue changeValue = mediumDosingControlVO.new MediumDosingValue();
        changeValue.setThingCode(mediumDosingConfigDO.getChangevalueCode());
        changeValue.setThingName(SfMediumConstants.CHANGEVALUE_THINGNAME);
        changeValue.setStartMetricCode(MetricCodes.START_CMD);
        changeValue.setStopMetricCode(MetricCodes.STOP_CMD);
        if (mediumDosingConfigDO.getSystem().equals(GlobalConstants.SYSTEM_01)) {
            valueControl.setTitle(GlobalConstants.SYSTEM_ONE + SfMediumConstants.CHANGEVALUE_THINGNAME);
        } else if (mediumDosingConfigDO.getSystem().equals(GlobalConstants.SYSTEM_02)) {
            valueControl.setTitle(GlobalConstants.SYSTEM_TWO + SfMediumConstants.CHANGEVALUE_THINGNAME);
        }
        changeValue.setThingImageName(getThingImageName(mediumDosingConfigDO.getChangevalueCode()));
        changeValue.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
        changeValue.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
        changeValue.setTapOpenMetricValue(getMetricDataValue(mediumDosingConfigDO.getChangevalueCode(), MetricCodes.TAP_OPEN));
        changeValue.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
        changeValue.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
        changeValue.setTapCloseMetricValue(getMetricDataValue(mediumDosingConfigDO.getChangevalueCode(), MetricCodes.TAP_CLOSE));

        valueControl.setMediumDosingValues(Lists.newArrayList(changeValue));
    }

    /**
     * 加介控制中加介泵状态
     *
     * @param mediumPoolThingCode
     * @param mediumDosingControlVO
     */
    private void getMediumDosingPump(String mediumPoolThingCode, MediumDosingControlVO mediumDosingControlVO) {
        MediumDosingControlVO.MediumDosingPump mediumDosingPump = mediumDosingControlVO.new MediumDosingPump();
        mediumDosingPump.setThingCode(mediumPoolThingCode);
        mediumDosingPump.setThingName(SfMediumConstants.MEDIUM_DOSING_PUMP_NAME);
        //运行状态
        mediumDosingPump.setRunState(getMetricDataValue(mediumPoolThingCode, MetricCodes.STATE));
        //启动停止
        mediumDosingPump.setStartCmdMetricCode(MetricCodes.START_CMD);
        mediumDosingPump.setStartCmdMetricName(SfMediumConstants.START_CMD_METRIC_NAME);
        mediumDosingPump.setStopCmdMetricCode(MetricCodes.STOP_CMD);
        mediumDosingPump.setStopCmdMetricName(SfMediumConstants.STOP_CMD_METRIC_NAME);
        //智能集控就地
        mediumDosingPump.setIntelligentMetricCode(MetricCodes.INTELLIGENT_CONTROL);
        mediumDosingPump.setIntelligentMetricName(SfMediumConstants.INTELLIGENT);
        mediumDosingPump.setIntelligentMetricValue(getMetricDataValue(mediumPoolThingCode, MetricCodes.INTELLIGENT_CONTROL));
        mediumDosingPump.setLocalMetricCode(MetricCodes.LOCAL);
        mediumDosingPump.setLocalMetricName(SfMediumConstants.LOCAL_METRIC_NAME);
        mediumDosingPump.setLocalMetricValue(getMetricDataValue(mediumPoolThingCode, MetricCodes.LOCAL));
        //最高级别的报警信息
        List<AlertData> seriousAlertLevelInList = alertManager.getSeriousAlertLevelInList(Lists.newArrayList(mediumPoolThingCode), 1);
        if (CollectionUtils.isNotEmpty(seriousAlertLevelInList)) {
            mediumDosingPump.setAlertInfo(seriousAlertLevelInList.get(0).getAlertInfo());
        } else {
            mediumDosingPump.setAlertInfo("");
        }
        mediumDosingControlVO.setMediumDosingPump(mediumDosingPump);
    }

    /**
     * 加介进程监控
     *
     * @param mediumdosingsystemId
     * @return
     */
    @RequestMapping(value = "/getSeparatingSystemList", method = RequestMethod.GET)
    public ResponseEntity<String> getSeparatingSystemList(@RequestParam("mediumdosingsystemId") String mediumdosingsystemId) {
        SeparatingSystemVO separatingSystemVO = new SeparatingSystemVO();
        List<MediumDosingConfigDO> separatingSystems = mediumDosingService.getSeparatingSystemById(mediumdosingsystemId);
        SeparatingSystemVO.MediumPool mediumPool = separatingSystemVO.new MediumPool();

        mediumPool.setThingCode(separatingSystems.get(0).getMediumpoolCode());
        if (SfMediumConstants.MEDIUM_POOL_1.equals(mediumdosingsystemId)) {
            mediumPool.setThingName(SfMediumConstants.MEDIUMPOOL_1);
        } else if (SfMediumConstants.MEDIUM_POOL_2.equals(mediumdosingsystemId)) {
            mediumPool.setThingName(SfMediumConstants.MEDIUMPOOL_2);
        } else if (SfMediumConstants.MEDIUM_POOL_3.equals(mediumdosingsystemId)) {
            mediumPool.setThingName(SfMediumConstants.MEDIUMPOOL_3);
        }
        //配介状态
        getMediumCompoundingMetricValue(separatingSystems, mediumPool);

        //介质池液位
        mediumPool.setLevel(getMetricDataValue(separatingSystems.get(0).getMediumpoolCode(), MetricCodes.CURRENT_LEVEL_M));

        //补水阀
        getWterSupplementValue(separatingSystemVO, separatingSystems, mediumPool);
        //鼓风阀
        getBlowerValve(separatingSystemVO, separatingSystems, mediumPool);

        //加介泵
        getMediumDosingPump(separatingSystemVO, separatingSystems, mediumPool);
        //加介分选系统
        List<SeparatingSystemVO.SeparatingSystem> separatingSystemList = getSeparatingSystems(separatingSystemVO, separatingSystems);
        separatingSystemVO.setSeparatingSystemList(separatingSystemList);
        return new ResponseEntity<>(ServerResponse.buildOkJson(separatingSystemVO), HttpStatus.OK);
    }

    /**
     * 加介分选系统
     *
     * @param separatingSystemVO
     * @param separatingSystems
     * @return
     */
    private List<SeparatingSystemVO.SeparatingSystem> getSeparatingSystems(SeparatingSystemVO separatingSystemVO, List<MediumDosingConfigDO> separatingSystems) {
        List<SeparatingSystemVO.SeparatingSystem> separatingSystemList = new ArrayList<>();
        for (MediumDosingConfigDO mediumDosingConfigDO : separatingSystems) {
            SeparatingSystemVO.SeparatingSystem separatingSystem = separatingSystemVO.new SeparatingSystem();
            //名称
            if (mediumDosingConfigDO.getSystem().equals(GlobalConstants.SYSTEM_01)) {
                separatingSystem.setSystemName(GlobalConstants.SYSTEM_ONE + mediumDosingConfigDO.getSeparatingsystemName());
            } else if (mediumDosingConfigDO.getSystem().equals(GlobalConstants.SYSTEM_02)) {
                separatingSystem.setSystemName(GlobalConstants.SYSTEM_TWO + mediumDosingConfigDO.getSeparatingsystemName());
            }
            //液位
            separatingSystem.setCombinedBucketLevel(getMetricDataValue(mediumDosingConfigDO.getCombinedbucketCode(), MetricCodes.CURRENT_LEVEL_M));

            //密度
            separatingSystem.setCombinedBucketDensity(getMetricDataValue(mediumDosingConfigDO.getCombinedbucketCode(), MetricCodes.CURRENT_DENSITY));
            //已加介时间
            separatingSystem.setElapsedTime(getMetricDataValue(mediumDosingConfigDO.getMediumpoolCode(), MetricCodes.JJ_TIME));
            //加介剩余时间
            separatingSystem.setRemainingTime(getMetricDataValue(mediumDosingConfigDO.getMediumpoolCode(), MetricCodes.JJ_RE_TIME));
            //加介状态
            getMediumDosingState(mediumDosingConfigDO, separatingSystem);
            separatingSystemList.add(separatingSystem);
        }
        return separatingSystemList;
    }

    /**
     * 加介状态
     *
     * @param mediumDosingConfigDO
     * @param separatingSystem
     */
    private void getMediumDosingState(MediumDosingConfigDO mediumDosingConfigDO, SeparatingSystemVO.SeparatingSystem separatingSystem) {
        //关联泵 阀门 介质池配介状态
        String mediumDosingState = getSeparatingSystemMediumDosingState(mediumDosingConfigDO);
        separatingSystem.setMediumDosingState(mediumDosingState);
        if (StringUtils.isNotEmpty(mediumDosingState)) {
            if (mediumDosingState.equals("1")) {
                separatingSystem.setMediumDosingStateShow(SfMediumConstants.MEDIUM_DOSING_STATE_1);
            } else if (mediumDosingState.equals("0")) {
                separatingSystem.setMediumDosingStateShow(SfMediumConstants.MEDIUM_DOSING_STATE_0);
            } else {
                separatingSystem.setMediumDosingStateShow("");
            }
        }
    }

    /**
     * 分选系统（合介桶）加介状态
     *
     * @param mediumDosingConfigDO
     * @return
     */
    private String getSeparatingSystemMediumDosingState(MediumDosingConfigDO mediumDosingConfigDO) {
        String mediumDosingState = "";
        String addingMetricDataValue = getMetricDataValue(mediumDosingConfigDO.getCombinedbucketCode(), MetricCodes.ADDING);
        String mixingMetricDataValue = getMetricDataValue(mediumDosingConfigDO.getCombinedbucketCode(), MetricCodes.MIXING);
        if (StringUtils.isNotEmpty(addingMetricDataValue) && "1".equals(addingMetricDataValue)) {
            mediumDosingState = "1";
        }
        if (StringUtils.isNotEmpty(mixingMetricDataValue) && "1".equals(mixingMetricDataValue)) {
            mediumDosingState = "0";
        }
        return mediumDosingState;
    }

    /**
     * 加介泵
     *
     * @param separatingSystemVO
     * @param separatingSystems
     * @param mediumPool
     */
    private void getMediumDosingPump(SeparatingSystemVO separatingSystemVO, List<MediumDosingConfigDO> separatingSystems, SeparatingSystemVO.MediumPool mediumPool) {
        SeparatingSystemVO.MediumDosingPump mediumDosingPump = separatingSystemVO.new MediumDosingPump();

        mediumDosingPump.setThingCode(separatingSystems.get(0).getMediumdosingpumpCode());
        mediumDosingPump.setThingName(SfMediumConstants.MEDIUM_DOSING_PUMP_NAME);
        mediumDosingPump.setRunState(getMetricDataValue(separatingSystems.get(0).getMediumdosingpumpCode(), MetricCodes.STATE));

        mediumDosingPump.setIntelligentMetricCode(MetricCodes.INTELLIGENT_CONTROL);
        mediumDosingPump.setIntelligentMetricName(SfMediumConstants.INTELLIGENT);
        mediumDosingPump.setIntelligentMetricValue(getMetricDataValue(separatingSystems.get(0).getMediumdosingpumpCode(), MetricCodes.INTELLIGENT_CONTROL));
        mediumDosingPump.setLocalMetricCode(MetricCodes.LOCAL);
        mediumDosingPump.setLocalMetricName(SfMediumConstants.LOCAL_METRIC_NAME);
        mediumDosingPump.setLocalMetricValue(getMetricDataValue(separatingSystems.get(0).getMediumdosingpumpCode(), MetricCodes.LOCAL));
        mediumPool.setMediumDosingPump(mediumDosingPump);
        separatingSystemVO.setMediumPool(mediumPool);
    }

    /**
     * 鼓风阀门
     *
     * @param separatingSystemVO
     * @param separatingSystems
     * @param mediumPool
     */
    private void getBlowerValve(SeparatingSystemVO separatingSystemVO, List<MediumDosingConfigDO> separatingSystems, SeparatingSystemVO.MediumPool mediumPool) {
        SeparatingSystemVO.BlowerValve blowerValve = separatingSystemVO.new BlowerValve();

        blowerValve.setThingCode(separatingSystems.get(0).getBlowervalveCode());
        blowerValve.setThingName(SfMediumConstants.BLOWERVALVE_THINGNAME);


        blowerValve.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
        blowerValve.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
        blowerValve.setTapOpenMetricValue(getMetricDataValue(separatingSystems.get(0).getBlowervalveCode(), MetricCodes.TAP_OPEN));
        blowerValve.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
        blowerValve.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
        blowerValve.setTapCloseMetricValue(getMetricDataValue(separatingSystems.get(0).getBlowervalveCode(), MetricCodes.TAP_CLOSE));
        mediumPool.setBlowerValve(blowerValve);
    }

    /**
     * 补水阀门
     *
     * @param separatingSystemVO
     * @param separatingSystems
     * @param mediumPool
     */
    private void getWterSupplementValue(SeparatingSystemVO separatingSystemVO, List<MediumDosingConfigDO> separatingSystems, SeparatingSystemVO.MediumPool mediumPool) {
        SeparatingSystemVO.WterSupplementValue wterSupplementValue = separatingSystemVO.new WterSupplementValue();

        wterSupplementValue.setThingCode(separatingSystems.get(0).getWatersupplementvalveCode());
        wterSupplementValue.setThingName(SfMediumConstants.WATERSUPPLEMENTVALVE_THINGNAME);
        wterSupplementValue.setTapOpenMetricCode(MetricCodes.TAP_OPEN);
        wterSupplementValue.setTapOpenMetricName(SfMediumConstants.TAP_OPEN_TRUE);
        wterSupplementValue.setTapOpenMetricValue(getMetricDataValue(separatingSystems.get(0).getWatersupplementvalveCode(), MetricCodes.TAP_OPEN));
        wterSupplementValue.setTapCloseMetricCode(MetricCodes.TAP_CLOSE);
        wterSupplementValue.setTapCloseMetricName(SfMediumConstants.TAP_CLOSE_TRUE);
        wterSupplementValue.setTapCloseMeticValue(getMetricDataValue(separatingSystems.get(0).getWatersupplementvalveCode(), MetricCodes.TAP_CLOSE));
        mediumPool.setWaterSupplementValue(wterSupplementValue);
    }

    /**
     * 配介状态
     *
     * @param separatingSystems
     * @param mediumPool
     */
    private void getMediumCompoundingMetricValue(List<MediumDosingConfigDO> separatingSystems, SeparatingSystemVO.MediumPool mediumPool) {
        //配介状态
        String metricDataValue = getMetricDataValue(separatingSystems.get(0).getMediumpoolCode(), MetricCodes.STATE);
        mediumPool.setMediumCompoundingMetricValue(metricDataValue);
        if (StringUtils.isNotEmpty(metricDataValue)) {
            if ("1".equals(metricDataValue)) {
                mediumPool.setMediumCompoundingMetricValueShow(SfMediumConstants.MEDIUM_COMPOUNDING_STATE_1);
                mediumPool.setMediumCompoundingState("");
            } else if ("0".equals(metricDataValue)) {
                mediumPool.setMediumCompoundingMetricValueShow(SfMediumConstants.MEDIUM_COMPOUNDING_STATE_0);
                mediumPool.setMediumCompoundingState("");
            } else if ("2".equals(metricDataValue)) {
                mediumPool.setMediumCompoundingMetricValueShow(SfMediumConstants.MEDIUM_COMPOUNDING_STATE_2);
                mediumPool.setMediumCompoundingState(SfMediumConstants.MEDIUM_COMPOUNDING_STATE_2_SHOW);
            }
        } else {
            mediumPool.setMediumCompoundingMetricValueShow("");
            mediumPool.setMediumCompoundingState("");
        }
    }

    /**
     * 查询指标数据封装
     *
     * @param thingCode
     * @param metricCode
     * @return
     */
    private String getMetricDataValue(String thingCode, String metricCode) {
        DataModelWrapper dataModelWrapper = dataService.getData(thingCode, metricCode).orElse(null);
        if (dataModelWrapper != null) {
            return dataModelWrapper.getValue();
        } else {
            return "";
        }
    }

    /**
     * 查询设备的照片
     *
     * @param thingCode
     * @return
     */
    private String getThingImageName(String thingCode) {
        ThingPropertyModel thingProperties = tmlMapper.findThingProperties(thingCode, SfMediumConstants.IMAGE_NAME);
        if (thingProperties != null) {
            return thingProperties.getPropValue();
        } else {
            return "";
        }
    }

}
                                                  