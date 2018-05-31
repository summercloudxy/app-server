package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstart.controller.StartController;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.StopChoiceVO;
import com.zgiot.app.server.module.sfstop.entity.vo.StopIndexVO;
import com.zgiot.app.server.module.sfstop.entity.vo.StopManualInterventionAreaVO;
import com.zgiot.app.server.module.sfstop.entity.vo.TcsBucketVO;
import com.zgiot.app.server.module.sfstop.service.*;
import com.zgiot.app.server.module.sfstop.util.DateTimeUtils;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.SessionContext;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    private StartOperationRecordService startOperationRecordService;
    @Autowired
    private StopLineService stopLineService;
    @Autowired
    private StopDeviceBagService stopDeviceBagService;
    @Autowired
    private DataService dataService;
    @Autowired
    private StopInformationService stopInformationService;
    @Autowired
    private StopManualInterventionService stopManualInterventionService;
    @Autowired
    private StopHandler stopHandler;

    // 当前启车操作号
    private static Integer operateId;

    public static Integer getOperateId() {
        return operateId;
    }

    public static void setOperateId(Integer operateId) {
        StopController.operateId = operateId;
    }


    /**
     * 查询停车状态,根据停车状态判断跳转页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findStopState", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findStopState() throws Exception {
        StopOperationRecord stopState = stopHandler.getStopState();
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
    public ResponseEntity<String> getStopIndex(@RequestParam("areaSystem") String areaSystem) {
        StopIndexVO stopIndexVO = new StopIndexVO();
        Long runTime =null;
        //最后的启车的记录
        StartOperationRecord startOperationRecord = startOperationRecordService.getStartOperationRecord(StopConstants.START_FINISH_STATE);
        if (startOperationRecord != null) {
            Date updateTime = startOperationRecord.getUpdateTime();
            runTime = DateTimeUtils.getDifferenceTime(updateTime, new Date(), StopConstants.TIMEFORMAT);
        }
        stopIndexVO.setThingRunTime(runTime);
        List<StopIndexVO.StopThingArea> stopThingAreas = new ArrayList<>();
        //查询大区下区域
        int lineRunCount = 0;
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(areaSystem));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            StopIndexVO.StopThingArea stopThingArea = stopIndexVO.new StopThingArea();
            stopThingArea.setAreaId(String.valueOf(stopDeviceArea.getId()));
            stopThingArea.setAreaName(stopDeviceArea.getAreaName());
            //区域下的停车线
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            stopThingArea.setLineCnt(String.valueOf(stopLines.size()));
            List<StopIndexVO.StopThingLine> stopThingLines = new ArrayList<>();
            for (StopLine stopLine : stopLines) {
                //待机
                Boolean lineRunState_1 = true;
                //运行
                Boolean lineRunState_2 = true;
                //故障
                Boolean lineRunState_4 = true;
                StopIndexVO.StopThingLine stopThingLine = stopIndexVO.new StopThingLine();
                stopThingLine.setLineId(String.valueOf(stopLine.getId()));
                stopThingLine.setLineName(stopLine.getLineName());
                //停车线下的停车包
                List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(stopLine.getId());
                for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
                    //查询停车包下的设备
                    List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
                    for (StopInformation stopInformation : stopInformations) {
                        String metricValue = getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);

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
                stopThingLine.setLineRunState("");
                if (lineRunState_1) {
                    stopThingLine.setLineRunState(StopConstants.RUNSTATE_1);
                } else if (lineRunState_2) {
                    lineRunCount++;
                    stopThingLine.setLineRunState(StopConstants.RUNSTATE_2);
                } else if (lineRunState_4) {
                    stopThingLine.setLineRunState(StopConstants.RUNSTATE_4);
                }
                stopThingLines.add(stopThingLine);
            }
            stopThingArea.setStopThingLines(stopThingLines);
            stopThingAreas.add(stopThingArea);

        }
        stopIndexVO.setStopThingAreas(stopThingAreas);
        stopIndexVO.setThingRunCount(String.valueOf(lineRunCount));
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
        List<StopInformation> stopInformationList=new ArrayList<>();
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(lineId));
        for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
            //查询停车包下的设备
            List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
            for (StopInformation stopInformation : stopInformations) {
                String state = getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);
                if(StringUtils.isNotEmpty(state)&&StopConstants.RUNSTATE_4.equals(state)){
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
    public ResponseEntity<String> getStopManualIntervention(@RequestParam("areaSystem") String areaSystem) {
        List<StopManualInterventionAreaVO> manualInterventionAreaVOList = new ArrayList<>();
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(areaSystem));
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
    public ResponseEntity<String> getStopLineManualIntervention(@RequestParam("lineId") String lineId, @RequestParam("state") String state) {
        List<StopManualIntervention> stopManualInterventionList = new ArrayList<>();
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(lineId));
        for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
            List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
            for (StopInformation stopInformation : stopInformations) {
                StopManualIntervention stopManualInterventionByThingCode = stopManualInterventionService.getStopManualInterventionByThingCode(stopInformation.getThingCode());
                if (stopManualInterventionByThingCode != null) {
                    if ("-1".equals(state)) {
                        stopManualInterventionList.add(stopManualInterventionByThingCode);
                    } else if (Integer.parseInt(state) == stopManualInterventionByThingCode.getState()) {
                        stopManualInterventionList.add(stopManualInterventionByThingCode);
                    }
                }
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopManualInterventionList), HttpStatus.OK);
    }

    /**
     * 停车方案设置
     *
     * @param areaSystem
     * @return
     */
    @RequestMapping(value = "/setStopChoice", method = RequestMethod.GET)
    public ResponseEntity<String> setStopChoice(@RequestParam("areaSystem") String areaSystem) {
        StopChoiceVO stopChoiceVO = new StopChoiceVO();
        List<StopChoiceVO.ChiceVO> rawStopSets = new ArrayList<>();
        StopChoiceVO.ChiceVO rawStop1 = stopChoiceVO.new ChiceVO();
        rawStop1.setId("1");
        rawStop1.setName("先原煤准备后主洗停车");
        StopChoiceVO.ChiceVO rawStop2 = stopChoiceVO.new ChiceVO();
        rawStop2.setId("0");
        rawStop2.setName("原煤准备停车");
        StopChoiceVO.ChiceVO rawStop3 = stopChoiceVO.new ChiceVO();
        rawStop3.setId("0");
        rawStop3.setName("主洗停车");
        StopChoiceVO.ChiceVO rawStop4 = stopChoiceVO.new ChiceVO();
        rawStop4.setId("0");
        rawStop4.setName("同时停车");
        rawStopSets.add(rawStop1);
        rawStopSets.add(rawStop2);
        rawStopSets.add(rawStop3);
        rawStopSets.add(rawStop4);
        stopChoiceVO.setRawStopSets(rawStopSets);

        List<StopChoiceVO.ChiceVO> tcsStopSets = new ArrayList<>();
        StopChoiceVO.ChiceVO tcsStop1 = stopChoiceVO.new ChiceVO();
        tcsStop1.setId("1");
        tcsStop1.setName("TCS系统正常停车");
        StopChoiceVO.ChiceVO tcsStop2 = stopChoiceVO.new ChiceVO();
        tcsStop2.setId("0");
        tcsStop2.setName("TCS系统检修停车");
        tcsStopSets.add(tcsStop1);
        tcsStopSets.add(tcsStop2);
        stopChoiceVO.setTcsStopSet(tcsStopSets);

        List<StopChoiceVO.ChiceVO> filterpressStopSets = new ArrayList<>();

        StopChoiceVO.ChiceVO filterpressStop1 = stopChoiceVO.new ChiceVO();
        filterpressStop1.setId("1");
        filterpressStop1.setName("压滤与洗选同步停车");
        StopChoiceVO.ChiceVO filterpressStop2 = stopChoiceVO.new ChiceVO();
        filterpressStop2.setId("0");
        filterpressStop2.setName("压滤滞后洗选停车");
        filterpressStopSets.add(filterpressStop1);
        filterpressStopSets.add(filterpressStop2);
        stopChoiceVO.setFilterpressStopSet(filterpressStopSets);

        String coal8Device = getMetricValue(StopConstants.Quit_SYS_2, MetricCodes.COAL_8_DEVICE);
        String coal13Device = getMetricValue(StopConstants.Quit_SYS_2, MetricCodes.COAL_13_DEVICE);

        List<StopChoiceVO.ChiceVO> beltRoutes = new ArrayList<>();
        StopChoiceVO.ChiceVO beltRoute1 = stopChoiceVO.new ChiceVO();
        beltRoute1.setName("1143");
        if (StringUtils.isNotEmpty(coal8Device) && "1143".equals(coal8Device)) {
            beltRoute1.setId("1");
        } else {
            beltRoute1.setId("0");
        }
        StopChoiceVO.ChiceVO beltRoute2 = stopChoiceVO.new ChiceVO();
        beltRoute2.setName("1144");
        if (StringUtils.isNotEmpty(coal8Device) && "1144".equals(coal8Device)) {
            beltRoute2.setId("1");
        } else {
            beltRoute2.setId("0");
        }

        StopChoiceVO.ChiceVO beltRoute3 = stopChoiceVO.new ChiceVO();
        beltRoute3.setName("2143");
        if (StringUtils.isNotEmpty(coal8Device) && "2143".equals(coal13Device)) {
            beltRoute3.setId("1");
        } else {
            beltRoute3.setId("0");
        }

        StopChoiceVO.ChiceVO beltRoute4 = stopChoiceVO.new ChiceVO();
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
        stopChoiceVO.setBeltRoute(beltRoutes);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopChoiceVO), HttpStatus.OK);
    }

    /**
     * TCS系统检修模式设置
     *
     * @return
     */
    @RequestMapping(value = "/getTcsBucketCheck", method = RequestMethod.GET)
    public ResponseEntity<String> getTcsBucketCheck() {
        Map<String, List<TcsBucketVO>> map = new HashMap<>();

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


        return new ResponseEntity<>(ServerResponse.buildOkJson(map), HttpStatus.OK);

    }

    /**
     * 查询停车线下的停车包
     *
     * @return
     */
    @RequestMapping(value = "/getStopDeviceBags", method = RequestMethod.GET)
    public ResponseEntity<String> getStopDeviceBags(@RequestParam("lineId") String lineId) {
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(lineId));
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopDeviceBags), HttpStatus.OK);
    }


    /**
     * 保存TCS系统检修模式设置 选择桶
     *
     * @return
     */
    @RequestMapping(value = "/saveTcsBucketCheck", method = RequestMethod.POST)
    public ResponseEntity<String> saveTcsBucketCheck(@RequestBody List<String> thingCodes) {
        for (String thingCode : thingCodes) {
            stopManualInterventionService.updateStopManualInterventionState(1, thingCode);
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
        String dsflMetricValue = getMetricValue(thingCode, MetricCodes.DSFL);
        if (StringUtils.isNotEmpty(dsflMetricValue) && new BigDecimal(dsflMetricValue).compareTo(BigDecimal.valueOf(10)) > 0) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 停车自检
     *
     * @param areaSystem
     * @return
     */
    @RequestMapping(value = "/setUpAutoTest", method = RequestMethod.GET)
    public ResponseEntity<String> setUpAutoTest(@RequestParam("areaSystem") String areaSystem) {
        if (stopHandler.judgeStopingState(null, StopConstants.STOP_FINISH_STATE)) {
            // 通知前端已经存在停车任务
            return new ResponseEntity<>(ServerResponse.buildOkJson("stoping"), HttpStatus.OK);
        }
        List<String> thingCodes=new ArrayList<>();
        List<String> lines = stopHandler.getStopLinesBySystem(areaSystem);


        // 保存启车检查操作
        StopOperationRecord saveStartOperationRecord = stopHandler.saveStopOperationRecord(lines, SessionContext.getCurrentUser().getUserUuid());

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 获得本次停车检查信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAutoExamineRecord", method = RequestMethod.GET)
    public ResponseEntity<String> getAutoExamineRecord() throws Exception {

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 修复检查
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/handleExamine", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleExamine(@RequestBody List<Integer> ruleIds, @RequestParam("tapOperate") String tapOperate) throws Exception {


        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     * 正式停车
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setUpStopTask", method = RequestMethod.POST)
    public synchronized ResponseEntity<String> setUpStartTask() throws Exception {

        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }


    /**
     * 查询指标数据
     *
     * @param thingCode
     * @param metricCode
     * @return
     */
    private String getMetricValue(String thingCode, String metricCode) {
        DataModelWrapper dataModelWrapper = dataService.getData(thingCode, metricCode).orElse(null);
        if (dataModelWrapper != null) {
            return dataModelWrapper.getValue();
        }
        return "";
    }


}
