package com.zgiot.app.server.module.sfstop.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.*;
import com.zgiot.app.server.module.sfstop.service.*;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 智能停车
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfstop", produces = MediaType.APPLICATION_JSON_VALUE)
public class StopController {
    private static final Logger logger = LoggerFactory.getLogger(StopController.class);
    @Autowired
    private StopDeviceAreaService stopDeviceAreaService;
    @Autowired
    private StopDeviceRegionService stopDeviceRegionService;
    @Autowired
    private StopService stopService;
    @Autowired
    private StopLineService stopLineService;
    @Autowired
    private StopDeviceBagService stopDeviceBagService;

    @Autowired
    private StopInformationService stopInformationService;
    @Autowired
    private StopManualInterventionService stopManualInterventionService;
    @Autowired
    private StopHandler stopHandler;
    @Autowired
    private StopTypeSetDelayService stopTypeSetDelayService;

    @Autowired
    private StopTypeSetPararmeterService stopTypeSetPararmeterService;


    /**
     * 查询停车状态,根据停车状态判断跳转页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findStopState", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findStopState(@RequestParam("system") String system) throws Exception {
        StopOperationRecord stopState = stopHandler.getStopState(Integer.valueOf(system));
        logger.info("查询到的启车状态为{}", stopState);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopState), HttpStatus.OK);
    }


    /**
     * 查询所有的设备大区
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopIndex", method = RequestMethod.GET)
    public ResponseEntity<String> getStopIndex(@RequestParam("system") String system) {
        StopIndexVO stopIndexVO = stopHandler.getStopIndex(system);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopIndexVO), HttpStatus.OK);
    }

    /**
     * 根据停车线查询异常设备的信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopAbnormalDevices", method = RequestMethod.GET)
    public ResponseEntity<String> getStopAbnormalDevices(@RequestParam("lineId") String lineId) {
        List<StopInformation> stopInformationList = new ArrayList<>();
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(lineId));
        for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
            //查询停车包下的设备
            List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
            for (StopInformation stopInformation : stopInformations) {
                String state = stopHandler.getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);
                if (StringUtils.isNotEmpty(state) && StopConstants.RUNSTATE_4.equals(state)) {
                    stopInformationList.add(stopInformation);
                }
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopInformationList), HttpStatus.OK);
    }


    /**
     * 查询停车的人工干预记录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopManualIntervention", method = RequestMethod.GET)
    public ResponseEntity<String> getStopManualIntervention(@RequestParam("system") String system) {
        List<StopManualInterventionAreaVO> manualInterventionAreaVOList = new ArrayList<>();
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(system));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            StopManualInterventionAreaVO stopManualInterventionAreaVO = new StopManualInterventionAreaVO();
            stopManualInterventionAreaVO.setAreaId(String.valueOf(stopDeviceArea.getId()));
            stopManualInterventionAreaVO.setAreaName(stopDeviceArea.getAreaName());
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            stopManualInterventionAreaVO.setLineCnt(String.valueOf(stopLines.size()));
            List<StopManualInterventionAreaVO.StopThingLine> stopThingLines = new ArrayList<>();
            for (StopLine stopLine : stopLines) {
                StopManualInterventionAreaVO.StopThingLine stopThingLine = stopManualInterventionAreaVO.new StopThingLine();
                int manualInterventionThing = 0;
                List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(stopLine.getId());
                for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
                    List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
                    for (StopInformation stopInformation : stopInformations) {
                        StopManualIntervention stopManualInterventionByThingCode = stopManualInterventionService.getStopManualInterventionByThingCode(stopInformation.getThingCode());
                        if (stopManualInterventionByThingCode != null) {
                            if (1 == stopManualInterventionByThingCode.getState()) {
                                manualInterventionThing++;
                            }
                        }
                    }
                }
                stopThingLine.setLineId(String.valueOf(stopLine.getId()));
                stopThingLine.setLineName(stopLine.getLineName());
                stopThingLine.setManualCnt(String.valueOf(manualInterventionThing));

                stopThingLines.add(stopThingLine);
            }
            stopManualInterventionAreaVO.setStopThingLines(stopThingLines);
            manualInterventionAreaVOList.add(stopManualInterventionAreaVO);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(manualInterventionAreaVOList), HttpStatus.OK);
    }

    /**
     * 查询某个停车线下的人工干预记录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopLineManualIntervention", method = RequestMethod.GET)
    public ResponseEntity<String> getStopLineManualIntervention(@RequestParam("lineId") String lineId) {
        List<StopManualInterventionVO> stopManualInterventionList = new ArrayList<>();
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(lineId));
        for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
            List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
            for (StopInformation stopInformation : stopInformations) {
                StopManualInterventionVO stopManualInterventionVO = new StopManualInterventionVO();
                StopManualIntervention stopManualInterventionByThingCode = stopManualInterventionService.getStopManualInterventionByThingCode(stopInformation.getThingCode());
                if (stopManualInterventionByThingCode != null) {
                    stopManualInterventionVO.setThingCode(stopInformation.getThingCode());
                    stopManualInterventionVO.setThingName(stopInformation.getThingName());
                    stopManualInterventionVO.setIsManualIntervention(String.valueOf(stopManualInterventionByThingCode.getState()));
                    stopManualInterventionList.add(stopManualInterventionVO);
                }
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopManualInterventionList), HttpStatus.OK);
    }

    /**
     * 保存人工干预记录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveStopLineManualIntervention", method = RequestMethod.POST)
    public ResponseEntity<String> saveStopLineManualIntervention(@RequestBody List<StopManualInterventionVO> stopManualInterventionVOs) {
        for (StopManualInterventionVO stopManualInterventionVO : stopManualInterventionVOs) {
            StopManualIntervention stopManualIntervention = new StopManualIntervention();
            stopManualIntervention.setThingCode(stopManualInterventionVO.getThingCode());
            stopManualIntervention.setState(Integer.parseInt(stopManualInterventionVO.getIsManualIntervention()));
            stopManualInterventionService.updateStopManualInterventionByTC(stopManualIntervention);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 停车方案设置
     *
     * @param system
     * @return
     */
    @RequestMapping(value = "/getStopChoice", method = RequestMethod.GET)
    public ResponseEntity<String> setStopChoice(@RequestParam("system") String system) {
        List<StopChoiceVO> stopChoiceVOList = new ArrayList<>();

        StopChoiceVO rawStopSet = new StopChoiceVO();
        rawStopSet.setChoiceTitle("原煤与洗选停车模式");
        List<StopChoiceVO.ChiceVO> rawStopSets = new ArrayList<>();
        StopChoiceVO.ChiceVO rawStop1 = rawStopSet.new ChiceVO();
        rawStop1.setId("1");
        rawStop1.setName("先原煤准备后主洗停车");
        StopChoiceVO.ChiceVO rawStop2 = rawStopSet.new ChiceVO();
        rawStop2.setId("0");
        rawStop2.setName("原煤准备停车");
        StopChoiceVO.ChiceVO rawStop3 = rawStopSet.new ChiceVO();
        rawStop3.setId("0");
        rawStop3.setName("主洗停车");
        StopChoiceVO.ChiceVO rawStop4 = rawStopSet.new ChiceVO();
        rawStop4.setId("0");
        rawStop4.setName("同时停车");
        rawStopSets.add(rawStop1);
        rawStopSets.add(rawStop2);
        rawStopSets.add(rawStop3);
        rawStopSets.add(rawStop4);
        rawStopSet.setChoiceSets(rawStopSets);
        rawStopSet.setIsSingleChoice("1");
        stopChoiceVOList.add(rawStopSet);


        StopChoiceVO tcsStopSet = new StopChoiceVO();
        tcsStopSet.setChoiceTitle("TCS停车模式");
        List<StopChoiceVO.ChiceVO> tcsStopSets = new ArrayList<>();
        StopChoiceVO.ChiceVO tcsStop1 = tcsStopSet.new ChiceVO();
        tcsStop1.setId("1");
        tcsStop1.setName("TCS系统正常停车");
        StopChoiceVO.ChiceVO tcsStop2 = tcsStopSet.new ChiceVO();
        tcsStop2.setId("0");
        tcsStop2.setName("TCS系统检修停车");
        tcsStopSets.add(tcsStop1);
        tcsStopSets.add(tcsStop2);
        tcsStopSet.setChoiceSets(tcsStopSets);
        tcsStopSet.setIsSingleChoice("1");
        stopChoiceVOList.add(tcsStopSet);

        StopChoiceVO filterpressStopSet = new StopChoiceVO();
        filterpressStopSet.setChoiceTitle("压滤系统停车模式");
        List<StopChoiceVO.ChiceVO> filterpressStopSets = new ArrayList<>();
        StopChoiceVO.ChiceVO filterpressStop1 = filterpressStopSet.new ChiceVO();
        filterpressStop1.setId("1");
        filterpressStop1.setName("压滤与洗选同步停车");
        StopChoiceVO.ChiceVO filterpressStop2 = filterpressStopSet.new ChiceVO();
        filterpressStop2.setId("0");
        filterpressStop2.setName("压滤滞后洗选停车");
        filterpressStopSets.add(filterpressStop1);
        filterpressStopSets.add(filterpressStop2);
        filterpressStopSet.setChoiceSets(filterpressStopSets);
        filterpressStopSet.setIsSingleChoice("1");
        stopChoiceVOList.add(filterpressStopSet);


        StopChoiceVO beltRouteSet = new StopChoiceVO();
        beltRouteSet.setChoiceTitle("原煤仓下皮带停车选择");
        String coal8Device = null;
        String coal13Device = null;
        if (StopConstants.SYSTEM_1.equals(system)) {
            coal8Device = stopHandler.getMetricValue(StopConstants.Quit_SYS_1, MetricCodes.COAL_8_DEVICE);
            coal13Device = stopHandler.getMetricValue(StopConstants.Quit_SYS_1, MetricCodes.COAL_13_DEVICE);

        } else if (StopConstants.SYSTEM_2.equals(system)) {
            coal8Device = stopHandler.getMetricValue(StopConstants.Quit_SYS_2, MetricCodes.COAL_8_DEVICE);
            coal13Device = stopHandler.getMetricValue(StopConstants.Quit_SYS_2, MetricCodes.COAL_13_DEVICE);
        }
        List<StopChoiceVO.ChiceVO> beltRoutes = new ArrayList<>();
        StopChoiceVO.ChiceVO beltRoute1 = beltRouteSet.new ChiceVO();
        beltRoute1.setName("1143");
        if (StringUtils.isNotEmpty(coal8Device) && "1143".equals(coal8Device)) {
            beltRoute1.setId("1");
        } else {
            beltRoute1.setId("0");
        }
        StopChoiceVO.ChiceVO beltRoute2 = beltRouteSet.new ChiceVO();
        beltRoute2.setName("1144");
        if (StringUtils.isNotEmpty(coal8Device) && "1144".equals(coal8Device)) {
            beltRoute2.setId("1");
        } else {
            beltRoute2.setId("0");
        }

        StopChoiceVO.ChiceVO beltRoute3 = beltRouteSet.new ChiceVO();
        beltRoute3.setName("2143");
        if (StringUtils.isNotEmpty(coal8Device) && "2143".equals(coal13Device)) {
            beltRoute3.setId("1");
        } else {
            beltRoute3.setId("0");
        }

        StopChoiceVO.ChiceVO beltRoute4 = beltRouteSet.new ChiceVO();
        beltRoute4.setName("2144");
        if (StringUtils.isNotEmpty(coal8Device) && "2144".equals(coal13Device)) {
            beltRoute4.setId("1");
        } else {
            beltRoute4.setId("0");
        }

        beltRoutes.add(beltRoute1);
        beltRoutes.add(beltRoute2);
        beltRoutes.add(beltRoute3);
        beltRoutes.add(beltRoute4);
        beltRouteSet.setChoiceSets(beltRoutes);
        beltRouteSet.setIsSingleChoice("0");
        stopChoiceVOList.add(beltRouteSet);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopChoiceVOList), HttpStatus.OK);

    }

    /**
     * TCS系统检修模式设置
     *
     * @return
     */
    @RequestMapping(value = "/getTcsBucketCheck", method = RequestMethod.GET)
    public ResponseEntity<String> getTcsBucketCheck(@RequestParam("system") String system) {
        Map<String, List<TcsBucketVO>> map = new HashMap<>();
        if (StopConstants.SYSTEM_1.equals(system)) {
            List<TcsBucketVO> tcsBuckets1 = new ArrayList<>();
            TcsBucketVO tcsBucketVO1 = new TcsBucketVO();
            tcsBucketVO1.setThingCode("1857-1");
            tcsBucketVO1.setRunState(getBucketRunState(tcsBucketVO1.getThingCode()));
            tcsBuckets1.add(tcsBucketVO1);

            TcsBucketVO tcsBucketVO2 = new TcsBucketVO();
            tcsBucketVO2.setThingCode("1857-2");
            tcsBucketVO2.setRunState(getBucketRunState(tcsBucketVO2.getThingCode()));
            tcsBuckets1.add(tcsBucketVO2);

            TcsBucketVO tcsBucketVO3 = new TcsBucketVO();
            tcsBucketVO3.setThingCode("1857-3");
            tcsBucketVO3.setRunState(getBucketRunState(tcsBucketVO3.getThingCode()));
            tcsBuckets1.add(tcsBucketVO3);
            map.put("1857", tcsBuckets1);


            List<TcsBucketVO> tcsBuckets2 = new ArrayList<>();
            TcsBucketVO tcsBucketVO4 = new TcsBucketVO();
            tcsBucketVO4.setThingCode("1858-1");
            tcsBucketVO4.setRunState(getBucketRunState(tcsBucketVO4.getThingCode()));
            tcsBuckets2.add(tcsBucketVO4);

            TcsBucketVO tcsBucketVO5 = new TcsBucketVO();
            tcsBucketVO5.setThingCode("1858-2");
            tcsBucketVO5.setRunState(getBucketRunState(tcsBucketVO5.getThingCode()));
            tcsBuckets2.add(tcsBucketVO5);

            TcsBucketVO tcsBucketVO6 = new TcsBucketVO();
            tcsBucketVO6.setThingCode("1858-3");
            tcsBucketVO6.setRunState(getBucketRunState(tcsBucketVO6.getThingCode()));
            tcsBuckets2.add(tcsBucketVO6);
            map.put("1858", tcsBuckets2);


            List<TcsBucketVO> tcsBuckets3 = new ArrayList<>();
            TcsBucketVO tcsBucketVO7 = new TcsBucketVO();
            tcsBucketVO7.setThingCode("1859-1");
            tcsBucketVO7.setRunState(getBucketRunState(tcsBucketVO7.getThingCode()));
            tcsBuckets3.add(tcsBucketVO7);

            TcsBucketVO tcsBucketVO8 = new TcsBucketVO();
            tcsBucketVO8.setThingCode("1859-2");
            tcsBucketVO8.setRunState(getBucketRunState(tcsBucketVO8.getThingCode()));
            tcsBuckets3.add(tcsBucketVO8);

            TcsBucketVO tcsBucketVO9 = new TcsBucketVO();
            tcsBucketVO9.setThingCode("1859-3");
            tcsBucketVO9.setRunState(getBucketRunState(tcsBucketVO9.getThingCode()));
            tcsBuckets3.add(tcsBucketVO9);
            map.put("1859", tcsBuckets3);


        } else if (StopConstants.SYSTEM_2.equals(system)) {
            List<TcsBucketVO> tcsBuckets1 = new ArrayList<>();
            TcsBucketVO tcsBucketVO1 = new TcsBucketVO();
            tcsBucketVO1.setThingCode("2857-1");
            tcsBucketVO1.setRunState(getBucketRunState(tcsBucketVO1.getThingCode()));
            tcsBuckets1.add(tcsBucketVO1);

            TcsBucketVO tcsBucketVO2 = new TcsBucketVO();
            tcsBucketVO2.setThingCode("2857-2");
            tcsBucketVO2.setRunState(getBucketRunState(tcsBucketVO2.getThingCode()));
            tcsBuckets1.add(tcsBucketVO2);

            TcsBucketVO tcsBucketVO3 = new TcsBucketVO();
            tcsBucketVO3.setThingCode("2857-3");
            tcsBucketVO3.setRunState(getBucketRunState(tcsBucketVO3.getThingCode()));
            tcsBuckets1.add(tcsBucketVO3);
            map.put("2857", tcsBuckets1);


            List<TcsBucketVO> tcsBuckets2 = new ArrayList<>();
            TcsBucketVO tcsBucketVO4 = new TcsBucketVO();
            tcsBucketVO4.setThingCode("2858-1");
            tcsBucketVO4.setRunState(getBucketRunState(tcsBucketVO4.getThingCode()));
            tcsBuckets2.add(tcsBucketVO4);

            TcsBucketVO tcsBucketVO5 = new TcsBucketVO();
            tcsBucketVO5.setThingCode("2858-2");
            tcsBucketVO5.setRunState(getBucketRunState(tcsBucketVO5.getThingCode()));
            tcsBuckets2.add(tcsBucketVO5);

            TcsBucketVO tcsBucketVO6 = new TcsBucketVO();
            tcsBucketVO6.setThingCode("2858-3");
            tcsBucketVO6.setRunState(getBucketRunState(tcsBucketVO6.getThingCode()));
            tcsBuckets2.add(tcsBucketVO6);
            map.put("2858", tcsBuckets2);


            List<TcsBucketVO> tcsBuckets3 = new ArrayList<>();
            TcsBucketVO tcsBucketVO7 = new TcsBucketVO();
            tcsBucketVO7.setThingCode("2859-1");
            tcsBucketVO7.setRunState(getBucketRunState(tcsBucketVO7.getThingCode()));
            tcsBuckets3.add(tcsBucketVO7);

            TcsBucketVO tcsBucketVO8 = new TcsBucketVO();
            tcsBucketVO8.setThingCode("2859-2");
            tcsBucketVO8.setRunState(getBucketRunState(tcsBucketVO8.getThingCode()));
            tcsBuckets3.add(tcsBucketVO8);

            TcsBucketVO tcsBucketVO9 = new TcsBucketVO();
            tcsBucketVO9.setThingCode("2859-3");
            tcsBucketVO9.setRunState(getBucketRunState(tcsBucketVO9.getThingCode()));
            tcsBuckets3.add(tcsBucketVO9);
            map.put("2859", tcsBuckets3);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(map), HttpStatus.OK);

    }

    /**
     * 查询停车线的详情
     *
     * @return
     */
    @RequestMapping(value = "/getStopLineDetails", method = RequestMethod.GET)
    public ResponseEntity<String> getStopLineDetails(@RequestParam("lineId") String lineId) {
        List<StopLineDetailVO> stopLineDetailVOList = new ArrayList<>();

        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(lineId));
        for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
            StopLineDetailVO stopLineDetailVO = new StopLineDetailVO();
            stopLineDetailVO.setBagId(String.valueOf(stopDeviceBag.getId()));
            stopLineDetailVO.setBagName(stopDeviceBag.getBagName());
            List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
            boolean isFault = false;
            List<StopLineDetailVO.ThingAlertInfo> thingAlertInfoList = new ArrayList<>();
            for (StopInformation stopInformation : stopInformations) {
                StopLineDetailVO.ThingAlertInfo thingAlertInfo = stopLineDetailVO.new ThingAlertInfo();
                thingAlertInfo.setThingCode(stopInformation.getThingCode());
                thingAlertInfo.setThingName(stopInformation.getThingName());
                String metricValue = stopHandler.getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);
                thingAlertInfo.setRunState(metricValue);
                thingAlertInfo.setAlertInfo(stopHandler.getThingAlertInfo(stopInformation.getThingCode()));
                if (StringUtils.isNotEmpty(metricValue) && StopConstants.RUNSTATE_4.equals(metricValue)) {
                    isFault = true;
                    break;
                }
                thingAlertInfoList.add(thingAlertInfo);

            }
            if (isFault) {
                stopLineDetailVO.setIsFault("1");
            } else {
                stopLineDetailVO.setIsFault("0");
            }
            stopLineDetailVO.setThingAlertInfoList(thingAlertInfoList);
            stopLineDetailVOList.add(stopLineDetailVO);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopLineDetailVOList), HttpStatus.OK);
    }


    /**
     * 保存停车方案并自检
     *
     * @return
     */
    @RequestMapping(value = "/setUpAutoTest", method = RequestMethod.POST)
    public synchronized ResponseEntity<String> setUpAutoTest(@RequestBody StopChoiceSetVO stopChoiceSetVO) throws Exception {
        if (stopHandler.judgeStopingState(Integer.valueOf(stopChoiceSetVO.getSystem()), null, StopConstants.STOP_FINISH_STATE)) {
            // 通知前端已经存在停车任务
            return new ResponseEntity<>(ServerResponse.buildOkJson("stoping"), HttpStatus.OK);
        }

        //保存停车记录，保存停车方案选择记录
        StopOperationRecord stopOperationRecord = new StopOperationRecord();
        stopOperationRecord.setOperateState(StopConstants.STOP_CHOICE_SET);
        stopOperationRecord.setOperateSystem(JSON.toJSONString(stopChoiceSetVO.getLineIds()));
        stopOperationRecord.setSystem(Integer.valueOf(stopChoiceSetVO.getSystem()));
        stopService.saveStopOperationRecord(stopOperationRecord);


        StopChoiceSet stopChoiceSet = new StopChoiceSet();
        stopChoiceSet.setRawStopSet(Integer.valueOf(stopChoiceSetVO.getRawStopSet()));
        stopChoiceSet.setTcsStopSet(Integer.valueOf(stopChoiceSetVO.getTcsStopSet()));
        stopChoiceSet.setFilterpressStopSet(Integer.valueOf(stopChoiceSetVO.getFilterpressStopSet()));
        stopChoiceSet.setBeltRoute(JSON.toJSONString(stopChoiceSetVO.getBeltRouteSet()));
        stopChoiceSet.setOperateId(Long.valueOf(stopOperationRecord.getOperateId()));
        stopService.saveStopChoiceSet(stopChoiceSet);

        Set<String> stopDeviceIds = stopHandler.getStopDeviceIds(stopChoiceSetVO.getLineIds());

        stopService.setUpAutoExamine(stopDeviceIds, stopOperationRecord.getOperateId());

        autoExamine(stopDeviceIds, stopOperationRecord.getOperateId(), stopChoiceSetVO.getSystem());


        return new ResponseEntity<>(ServerResponse.buildOkJson(StopConstants.SUCCESS), HttpStatus.OK);
    }


    /**
     * 获取自检中系统和设备关系
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAutoExamineSystem", method = RequestMethod.GET)
    public ResponseEntity<String> getAutoExamineSystem(@RequestParam("system") String system) throws Exception {
        Integer operateId = null;
        List<StopOperationRecord> stopOperationRecords = stopService.findUnfinishStopOperate(Integer.valueOf(system), null, StopConstants.STOP_FINISH_STATE);
        if (CollectionUtils.isNotEmpty(stopOperationRecords)) {
            operateId = stopOperationRecords.get(0).getOperateId();
        }
        StopExamineIndexVO stopExamineIndexVO = new StopExamineIndexVO();
        //查询大区下区域
        int lineRunCount = 0;
        List<StopExamineIndexVO.StopExamineArea> stopExamineAreas = new ArrayList<>();
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(system));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            StopExamineIndexVO.StopExamineArea stopExamineArea = stopExamineIndexVO.new StopExamineArea();
            stopExamineArea.setAreaId(String.valueOf(stopDeviceArea.getId()));
            stopExamineArea.setAreaName(stopDeviceArea.getAreaName());
            //区域下的停车线
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            stopExamineArea.setLineCnt(String.valueOf(stopLines.size()));
            List<StopExamineIndexVO.StopExamineLine> stopExamineLines = new ArrayList<>();
            for (StopLine stopLine : stopLines) {
                //待机
                Boolean lineRunState_1 = true;
                //运行
                Boolean lineRunState_2 = true;
                //故障
                Boolean lineRunState_4 = true;
                StopExamineIndexVO.StopExamineLine stopExamineLine = stopExamineIndexVO.new StopExamineLine();
                stopExamineLine.setLineId(String.valueOf(stopLine.getId()));
                stopExamineLine.setLineName(stopLine.getLineName());
                //停车线下的停车包
                List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(stopLine.getId());
                for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
                    //查询停车包下的设备
                    List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
                    for (StopInformation stopInformation : stopInformations) {
                        String metricValue = stopHandler.getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);

                        if (!StopConstants.RUNSTATE_2.equals(metricValue)) {
                            lineRunState_2 = false;
                        }
                        if (!StopConstants.RUNSTATE_1.equals(metricValue)) {
                            lineRunState_1 = false;
                        }
                        if (!StopConstants.RUNSTATE_4.equals(metricValue)) {
                            lineRunState_4 = false;
                        }
                        if (operateId != null) {
                            StopExamineRecord stopExamineRecord = stopService.getStopExamineRecordByThingCode(operateId, stopInformation.getThingCode());
                            if (stopExamineRecord != null) {
                                stopExamineLine.setIsUnusual("1");
                            } else {
                                stopExamineLine.setIsUnusual("0");
                            }
                        }
                    }
                }
                stopExamineLine.setLineRunState("");
                if (lineRunState_1) {
                    stopExamineLine.setLineRunState(StopConstants.RUNSTATE_1);
                } else if (lineRunState_2) {
                    lineRunCount++;
                    stopExamineLine.setLineRunState(StopConstants.RUNSTATE_2);
                } else if (lineRunState_4) {
                    stopExamineLine.setLineRunState(StopConstants.RUNSTATE_4);
                }
                stopExamineLines.add(stopExamineLine);
            }
            stopExamineArea.setStopExamineLines(stopExamineLines);
            stopExamineAreas.add(stopExamineArea);

        }
        stopExamineIndexVO.setStopExamineAreas(stopExamineAreas);
        stopExamineIndexVO.setThingRunCount(String.valueOf(lineRunCount));
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopExamineIndexVO), HttpStatus.OK);
    }

    /**
     * 获取停车中系统和设备关系
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopingSystem", method = RequestMethod.GET)
    public ResponseEntity<String> getStopingSystem(@RequestParam("system") String system) throws Exception {
        StopingIndexVO stopingIndexVO = new StopingIndexVO();
        List<StopOperationRecord> stopOperationRecords = stopService.findUnfinishStopOperate(Integer.valueOf(system), null, StopConstants.STOP_FINISH_STATE);
        if (CollectionUtils.isNotEmpty(stopOperationRecords)) {
            stopingIndexVO.setStopElapsedTime(stopOperationRecords.get(0).getStopElapsedTime());
            stopingIndexVO.setStopPauseTime(stopOperationRecords.get(0).getStopPauseTime());
            stopingIndexVO.setOperateState(String.valueOf(stopOperationRecords.get(0).getOperateState()));
        }
        List<StopingIndexVO.StopingArea> stopingAreas = new ArrayList<>();
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(system));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            StopingIndexVO.StopingArea stopingArea = stopingIndexVO.new StopingArea();
            stopingArea.setAreaId(String.valueOf(stopDeviceArea.getId()));
            stopingArea.setAreaName(stopDeviceArea.getAreaName());
            //区域下的停车线
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            stopingArea.setLineCnt(String.valueOf(stopLines.size()));
            List<StopingIndexVO.StopingLine> stopingLines = new ArrayList<>();
            for (StopLine stopLine : stopLines) {
                //待机
                Boolean lineRunState_1 = true;
                //运行
                Boolean lineRunState_2 = true;
                //故障
                Boolean lineRunState_4 = true;
                StopingIndexVO.StopingLine stopingLine = stopingIndexVO.new StopingLine();
                stopingLine.setLineId(String.valueOf(stopLine.getId()));
                stopingLine.setLineName(stopLine.getLineName());
                //停车线下的停车包
                List<StopingIndexVO.StopingThing> stopingThings = new ArrayList<>();
                List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(stopLine.getId());
                for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
                    //查询停车包下的设备
                    List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
                    for (StopInformation stopInformation : stopInformations) {
                        StopingIndexVO.StopingThing stopingThing = stopingIndexVO.new StopingThing();
                        stopingThing.setThingCode(stopInformation.getThingCode());
                        stopingThing.setThingName(stopInformation.getThingName());
                        String metricValue = stopHandler.getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);
                        stopingThing.setRunState(metricValue);
                        stopingThings.add(stopingThing);
                        if (!StopConstants.RUNSTATE_2.equals(metricValue)) {
                            lineRunState_2 = false;
                        }
                        if (!StopConstants.RUNSTATE_1.equals(metricValue)) {
                            lineRunState_1 = false;
                        }
                        if (!StopConstants.RUNSTATE_4.equals(metricValue)) {
                            lineRunState_4 = false;
                        }
                    }
                }
                stopingLine.setStopingThings(stopingThings);
                stopingLine.setLineRunState("");
                if (lineRunState_1) {
                    stopingLine.setLineRunState(StopConstants.RUNSTATE_1);
                } else if (lineRunState_2) {
                    stopingLine.setLineRunState(StopConstants.RUNSTATE_2);
                } else if (lineRunState_4) {
                    stopingLine.setLineRunState(StopConstants.RUNSTATE_4);
                }
                stopingLines.add(stopingLine);
            }
            stopingArea.setStopingLines(stopingLines);
            stopingAreas.add(stopingArea);

        }
        stopingIndexVO.setStopingAreas(stopingAreas);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopingIndexVO), HttpStatus.OK);
    }
    /**
     * 自检开始
     *
     * @throws Exception
     */
    public void autoExamine(Set<String> startDeviceIds, Integer operateId, String system) throws Exception {
        startMultithreading(operateId, startDeviceIds, system);
    }

    /**
     * 建立多线程任务
     *
     * @param operateId     启车操作id
     * @param stopDeviceIds 涉及设备
     */
    public void startMultithreading(Integer operateId, Set<String> stopDeviceIds, String system) {
        StopMultithreading stopMultithreading = new StopMultithreading();
        stopMultithreading.setSystem(system);
        stopMultithreading.setStopService(stopService);
        stopMultithreading.setStopHandler(stopHandler);
        stopMultithreading.setStopDeviceIds(stopDeviceIds);
        stopMultithreading.setOperateId(operateId);
        new Thread(stopMultithreading).start();
    }


    /**
     * 查询人工干预解除区域
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getManualInterventionRecord", method = RequestMethod.GET)
    public ResponseEntity<String> getManualInterventionRecord(@RequestParam("lineId") String lineId) throws Exception {
        List<StopManualInterventionRecord> stopManualInterventionRecordByLineId = stopService.getStopManualInterventionRecordByLineId(Integer.valueOf(lineId));
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopManualInterventionRecordByLineId), HttpStatus.OK);
    }


    /**
     * 保存TCS系统检修模式设置 选择桶
     *
     * @return
     */
    @RequestMapping(value = "/saveTcsBucketCheck", method = RequestMethod.POST)
    public ResponseEntity<String> saveTcsBucketCheck(@RequestBody List<String> thingCodes) {
        for (String thingCode : thingCodes) {
            StopManualIntervention stopManualIntervention = new StopManualIntervention();
            stopManualIntervention.setThingCode(thingCode);
            stopManualIntervention.setState(StopConstants.MANUALINTERVENTION_1);
            stopManualInterventionService.updateStopManualInterventionByTC(stopManualIntervention);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson("保存成功"), HttpStatus.OK);
    }

    /**
     * 查询tcs桶的顶水流量 判断桶的状态
     *
     * @param thingCode
     * @return
     */
    private String getBucketRunState(String thingCode) {
        String dsflMetricValue = stopHandler.getMetricValue(thingCode, MetricCodes.DSFL);
        if (StringUtils.isNotEmpty(dsflMetricValue) && new BigDecimal(dsflMetricValue).compareTo(BigDecimal.valueOf(10)) > 0) {
            return "1";
        } else {
            return "0";
        }
    }


    /**
     * 修复检查
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/handleExamine", method = RequestMethod.POST)
    public ResponseEntity<String> handleExamine(@RequestBody List<Integer> ruleIds, @RequestParam("tapOperate") String tapOperate) throws Exception {
        stopHandler.handleExamine(ruleIds, tapOperate);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 查询设备详情
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getStopThingDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleExamine(@RequestParam("thingCode") String thingCode, @RequestParam("system") String system) throws Exception {
        StopThingDetail stopThingDetail = new StopThingDetail();
        StopInformation stopInformation = stopInformationService.getStopInformation(thingCode);
        if (stopInformation != null) {
            stopThingDetail.setThingCode(thingCode);
            stopThingDetail.setThingName(stopInformation.getThingName());
            List<StopThingDetail.ThingCondition> thingConditionList = new ArrayList<>();
            List<StopThing> parentStopTypeSetDelay = stopTypeSetDelayService.getParentStopTypeSetDelay(thingCode);
            for (StopThing stopThing : parentStopTypeSetDelay) {
                StopThingDetail.ThingCondition thingCondition = stopThingDetail.new ThingCondition();
                thingCondition.setThingCode(stopThing.getThingCode());
                thingCondition.setThingName(stopThing.getThingName());
                thingCondition.setRunState(stopHandler.getMetricValue(stopThing.getThingCode(), MetricCodes.STATE));
                thingCondition.setAlertInfo(stopHandler.getThingAlertInfo(stopThing.getThingCode()));
                thingConditionList.add(thingCondition);
            }
            stopThingDetail.setThingConditionList(thingConditionList);


            List<StopThingDetail.PararmeterCondition> pararmeterConditionList = new ArrayList<>();
            List<StopPararmeterVO> parentStopTypeSetPararmeters = stopTypeSetPararmeterService.getParentStopTypeSetPararmeter(thingCode);
            for (StopPararmeterVO stopPararmeter : parentStopTypeSetPararmeters) {
                StopThingDetail.PararmeterCondition pararmeterCondition = stopThingDetail.new PararmeterCondition();
                pararmeterCondition.setThingCode(stopPararmeter.getThingCode());
                pararmeterCondition.setThingName(stopPararmeter.getThingName());
                pararmeterCondition.setPararmeter(stopPararmeter.getMetricCode() + stopPararmeter.getOperator() + stopPararmeter.getMetricValue());

                String metricCode = stopPararmeter.getMetricCode();
                String operator = stopPararmeter.getOperator();
                String metricValue = stopPararmeter.getMetricValue();
                String realMetricValue = stopHandler.getMetricValue(stopPararmeter.getThingCode(), metricCode);

                pararmeterCondition.setIsUnusual(getIsUnusual(metricValue, operator, realMetricValue));

                pararmeterConditionList.add(pararmeterCondition);

            }
            stopThingDetail.setPararmeterConditionList(pararmeterConditionList);

            //查询人工干预
            StopManualIntervention stopManualInterventionByThingCode = stopManualInterventionService.getStopManualInterventionByThingCode(thingCode);
            if (stopManualInterventionByThingCode != null && stopManualInterventionByThingCode.getState() == 1) {
                stopThingDetail.setIsManualIntervention("1");
                List<StopOperationRecord> unfinishStopOperate = stopService.findUnfinishStopOperate(Integer.valueOf(system), StopConstants.STOP_STOPING_STATE, StopConstants.STOP_FINISH_STATE);
                if (CollectionUtils.isNotEmpty(unfinishStopOperate)) {
                    StopManualInterventionRecord stopManualInterventionRecord = stopService.getStopManualInterventionRecord(unfinishStopOperate.get(0).getOperateId(), thingCode);
                    if (stopManualInterventionRecord != null) {
                        stopThingDetail.setIsRelieve(String.valueOf(stopManualInterventionRecord.getInterventionState()));
                    }
                }
            } else {
                stopThingDetail.setIsManualIntervention("0");
                stopThingDetail.setIsRelieve("0");
            }


        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(stopThingDetail), HttpStatus.OK);
    }

    /**
     * 根据比较字符判断实时的指标值和设定的指标值
     *
     * @param setMetricValue
     * @param operator
     * @param realMetricValue
     * @return
     */
    public String getIsUnusual(String setMetricValue, String operator, String realMetricValue) {

        double setValue1 = Double.valueOf(setMetricValue);
        double realValue = Double.valueOf(realMetricValue);
        String isUnusual = "0";
        switch (operator) {
            case StopConstants.COMPARE_GREATER_THAN:
                if (realValue > setValue1) {
                    isUnusual = "1";
                }
                break;
            case StopConstants.COMPARE_LESS_THAN:
                if (realValue < setValue1) {
                    isUnusual = "1";
                }
                break;
            case StopConstants.COMPARE_EQUAL_TO:
                if (realValue == setValue1) {
                    isUnusual = "1";
                }
                break;
            case StopConstants.COMPARE_GREATER_THAN_AND_EQUAL_TO:
                if (realValue >= setValue1) {
                    isUnusual = "1";
                }
                break;
            case StopConstants.COMPARE_LESS_THAN_AND_EQUAL_TO:
                if (realValue <= setValue1) {
                    isUnusual = "1";
                }
                break;
            case StopConstants.COMPARE_NOT_EQUAL_TO:
                if (realValue != setValue1) {
                    isUnusual = "1";
                }
                break;
            default:
                break;

        }
        return isUnusual;

    }


    /**
     * 正式停车
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setUpStopTask", method = RequestMethod.POST)
    public synchronized ResponseEntity<String> setUpStartTask(@RequestBody List<String> lineIds) throws Exception {

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 停车完成
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setUpStopEnd", method = RequestMethod.GET)
    public ResponseEntity<String> setUpStopEnd(@RequestParam("system") String system) throws Exception {
        stopHandler.finishStopState(system);
        stopHandler.sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_STOP_FINISH);

        return new ResponseEntity<>(ServerResponse.buildOkJson(StopConstants.SUCCESS), HttpStatus.OK);
    }

    /**
     * 获得本次停车检查信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAutoExamineRecord", method = RequestMethod.GET)
    public ResponseEntity<String> getAutoExamineRecord(@RequestParam("system") String system) throws Exception {
        List<StopExamineResult> stopExamineResults = new ArrayList<>();
        List<StopOperationRecord> stopOperationRecords = stopService.findUnfinishStopOperate(Integer.valueOf(system), null, StopConstants.STOP_FINISH_STATE);
        if (CollectionUtils.isNotEmpty(stopOperationRecords)) {
            stopExamineResults = stopService.getStartExaminRecordByOperateId(stopOperationRecords.get(0).getOperateId());
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopExamineResults), HttpStatus.OK);
    }


    /**
     * 停车中操作
     *
     * @return
     */
    @RequestMapping(value = "/operateStoping", method = RequestMethod.GET)
    public ResponseEntity<String> operateStarting(@RequestParam("system") String system, @RequestParam("operate") String operate) throws Exception {

        stopHandler.operateStoping(system, operate);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 解除人工干预
     *
     * @param thingCode
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/relieveManualIntervention", method = RequestMethod.GET)
    public ResponseEntity<String> relieveManualIntervention(@RequestParam("system") String system, @RequestParam("thingCode") String thingCode) throws Exception {
        // 使用者id
        String information = stopHandler.relieveManualIntervention(system, thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(information), HttpStatus.OK);
    }


}
