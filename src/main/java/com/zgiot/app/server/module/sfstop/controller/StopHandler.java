package com.zgiot.app.server.module.sfstop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstart.pojo.StartMessage;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.*;
import com.zgiot.app.server.module.sfstop.exception.StopException;
import com.zgiot.app.server.module.sfstop.service.*;
import com.zgiot.app.server.module.sfstop.util.DateTimeUtils;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.SessionContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 停车辅助
 */
@Component
public class StopHandler {
    private static final Logger logger = LoggerFactory.getLogger(StopHandler.class);
    @Autowired
    private StopService stopService;
    @Autowired
    private StopDeviceAreaService stopDeviceAreaService;

    @Autowired
    private StopManualInterventionService stopManualInterventionService;
    @Autowired
    private StopLineService stopLineService;
    @Autowired
    private StopDeviceBagService stopDeviceBagService;
    @Autowired
    private StopInformationService stopInformationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private StopTypeSetDelayService stopTypeSetDelayService;

    @Autowired
    private StopTypeSetPararmeterService stopTypeSetPararmeterService;

    @Autowired
    private DataService dataService;

    @Autowired
    private CmdControlService cmdControlService;
    // 一期系统暂停状态
    private static Boolean system1PauseState;
    // 二期系统暂停状态
    private static Boolean system2PauseState;

    private static BlockingQueue blockingQueue;


    public static Boolean startListenerFlag;

    public static Logger getLogger() {
        return logger;
    }

    public StopService getStopService() {
        return stopService;
    }

    public void setStopService(StopService stopService) {
        this.stopService = stopService;
    }

    public StopDeviceAreaService getStopDeviceAreaService() {
        return stopDeviceAreaService;
    }

    public void setStopDeviceAreaService(StopDeviceAreaService stopDeviceAreaService) {
        this.stopDeviceAreaService = stopDeviceAreaService;
    }

    public StopLineService getStopLineService() {
        return stopLineService;
    }

    public void setStopLineService(StopLineService stopLineService) {
        this.stopLineService = stopLineService;
    }

    public StopDeviceBagService getStopDeviceBagService() {
        return stopDeviceBagService;
    }

    public void setStopDeviceBagService(StopDeviceBagService stopDeviceBagService) {
        this.stopDeviceBagService = stopDeviceBagService;
    }

    public StopInformationService getStopInformationService() {
        return stopInformationService;
    }

    public void setStopInformationService(StopInformationService stopInformationService) {
        this.stopInformationService = stopInformationService;
    }


    public static Boolean getSystem2PauseState() {
        return system2PauseState;
    }

    public static void setSystem2PauseState(Boolean system2PauseState) {
        StopHandler.system2PauseState = system2PauseState;
    }

    public static Boolean getSystem1PauseState() {
        return system1PauseState;
    }

    public static void setSystem1PauseState(Boolean system1PauseState) {
        StopHandler.system1PauseState = system1PauseState;
    }


    public static Boolean getStartListenerFlag() {
        return startListenerFlag;
    }

    public static void setStartListenerFlag(Boolean startListenerFlag) {
        StopHandler.startListenerFlag = startListenerFlag;
    }

    /**
     * 判断当前启车状态
     *
     * @return
     */
    public StopOperationRecord getStopState(int systemId) {
        List<StopOperationRecord> querystartOperationRecord = stopService.findUnfinishStopOperate(systemId, null, StopConstants.STOP_FINISH_STATE);
        if (CollectionUtils.isEmpty(querystartOperationRecord)) {
            StopOperationRecord startOperationRecord = new StopOperationRecord();
            startOperationRecord.setOperateState(StopConstants.STOP_NO_STATE);
            return startOperationRecord;
        } else if (querystartOperationRecord.size() > 1) {
            throw new StopException("当前起车数据异常，存在多条停车有效数据，请检查。");
        }
        return querystartOperationRecord.get(0);
    }

    /**
     * 判断当前停车状态
     *
     * @param startState
     * @param finishState
     * @return
     */
    public boolean judgeStopingState(Integer systemId, Integer startState, Integer finishState) {
        List<StopOperationRecord> stopOperationRecords = stopService.findUnfinishStopOperate(systemId, startState, finishState);
        if (CollectionUtils.isNotEmpty(stopOperationRecords)) {
            // 已经存在启车任务
            return true;
        }
        return false;
    }

    /**
     * 根据停车线查询异常设备的信息
     *
     * @return
     * @throws Exception
     */
    public List<StopInformation> getStopInformations(String lineId) {
        List<StopInformation> stopInformationList = new ArrayList<>();
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(lineId));
        for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
            //查询停车包下的设备
            List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
            for (StopInformation stopInformation : stopInformations) {
                String state = getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);
                if (StringUtils.isNotEmpty(state) && StopConstants.RUNSTATE_4.equals(state)) {
                    stopInformationList.add(stopInformation);
                }
            }
        }
        return stopInformationList;
    }

    /**
     * 查询某个停车线下的人工干预记录
     *
     * @return
     * @throws Exception
     */
    public List<StopManualInterventionVO> getStopManualInterventionVOS(String lineId) {
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
        return stopManualInterventionList;
    }

    /**
     * TCS系统检修模式设置
     *
     * @return
     */
    public Map<String, List<TcsBucketVO>> getStringListMap(String system) {
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
        return map;
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
     * 查询设备详情
     *
     * @return
     * @throws Exception
     */
    public StopThingDetail getStopThingDetail(String thingCode, String system) {
        StopThingDetail stopThingDetail = new StopThingDetail();
        StopInformation stopInformation = stopInformationService.getStopInformation(thingCode);
        if (stopInformation != null) {
            stopThingDetail.setThingCode(thingCode);
            stopThingDetail.setThingName(stopInformation.getThingName());
            //设备条件
            getConditionList(thingCode, stopThingDetail);
            //参数条件
            getPararmeterConditionList(thingCode, stopThingDetail);

            //查询人工干预
            getManualIntervention(thingCode, system, stopThingDetail);


        }
        return stopThingDetail;
    }

    /**
     * 查询设备的人工干预记录
     *
     * @param thingCode
     * @param system
     * @param stopThingDetail
     */
    private void getManualIntervention(String thingCode, String system, StopThingDetail stopThingDetail) {
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

    /**
     * 设备参数条件
     *
     * @param thingCode
     * @param stopThingDetail
     */
    private void getPararmeterConditionList(String thingCode, StopThingDetail stopThingDetail) {
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
            String realMetricValue = getMetricValue(stopPararmeter.getThingCode(), metricCode);

            pararmeterCondition.setIsUnusual(getIsUnusual(metricValue, operator, realMetricValue));

            pararmeterConditionList.add(pararmeterCondition);

        }
        stopThingDetail.setPararmeterConditionList(pararmeterConditionList);
    }

    /**
     * 查询设备条件
     *
     * @param thingCode
     * @param stopThingDetail
     */
    private void getConditionList(String thingCode, StopThingDetail stopThingDetail) {
        List<StopThingDetail.ThingCondition> thingConditionList = new ArrayList<>();
        List<StopThing> parentStopTypeSetDelay = stopTypeSetDelayService.getParentStopTypeSetDelay(thingCode);
        for (StopThing stopThing : parentStopTypeSetDelay) {
            StopThingDetail.ThingCondition thingCondition = stopThingDetail.new ThingCondition();
            thingCondition.setThingCode(stopThing.getThingCode());
            thingCondition.setThingName(stopThing.getThingName());
            thingCondition.setRunState(getMetricValue(stopThing.getThingCode(), MetricCodes.STATE));
            thingCondition.setAlertInfo(getThingAlertInfo(stopThing.getThingCode()));
            thingConditionList.add(thingCondition);
        }
        stopThingDetail.setThingConditionList(thingConditionList);
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
     * 查询停车线的详情
     *
     * @return
     */
    public List<StopLineDetailVO> getStopLineDetailVOS(String lineId) {
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
                String metricValue = getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);
                thingAlertInfo.setRunState(metricValue);
                thingAlertInfo.setAlertInfo(getThingAlertInfo(stopInformation.getThingCode()));
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
        return stopLineDetailVOList;
    }


    /**
     * 获取停车中系统和设备关系
     *
     * @return
     * @throws Exception
     */
    public StopingIndexVO getStopingIndexVO(String system) {
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
                getStopingLine(stopingIndexVO, stopingLines, stopLine);
            }
            stopingArea.setStopingLines(stopingLines);
            stopingAreas.add(stopingArea);

        }
        stopingIndexVO.setStopingAreas(stopingAreas);
        return stopingIndexVO;
    }

    /**
     * 查询停车线内的设备
     *
     * @param stopingIndexVO
     * @param stopingLines
     * @param stopLine
     */
    private void getStopingLine(StopingIndexVO stopingIndexVO, List<StopingIndexVO.StopingLine> stopingLines, StopLine stopLine) {
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
                String metricValue = getMetricValue(stopInformation.getThingCode(), MetricCodes.STATE);
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

    /**
     * 获取自检中系统和设备关系
     *
     * @return
     * @throws Exception
     */
    public StopExamineIndexVO getStopExamineIndexVO(String system) {
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
                lineRunCount = getStopExamineLine(operateId, stopExamineIndexVO, lineRunCount, stopExamineLines, stopLine);
            }
            stopExamineArea.setStopExamineLines(stopExamineLines);
            stopExamineAreas.add(stopExamineArea);

        }
        stopExamineIndexVO.setStopExamineAreas(stopExamineAreas);
        stopExamineIndexVO.setThingRunCount(String.valueOf(lineRunCount));
        return stopExamineIndexVO;
    }

    /**
     * 自检停车线
     *
     * @param operateId
     * @param stopExamineIndexVO
     * @param lineRunCount
     * @param stopExamineLines
     * @param stopLine
     * @return
     */
    private int getStopExamineLine(Integer operateId, StopExamineIndexVO stopExamineIndexVO, int lineRunCount, List<StopExamineIndexVO.StopExamineLine> stopExamineLines, StopLine stopLine) {

        StopExamineIndexVO.StopExamineLine stopExamineLine = stopExamineIndexVO.new StopExamineLine();
        stopExamineLine.setLineId(String.valueOf(stopLine.getId()));
        stopExamineLine.setLineName(stopLine.getLineName());
        //停车线下的停车包
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(stopLine.getId());
        //待机
        Boolean lineRunState_1 = true;
        //运行
        Boolean lineRunState_2 = true;
        //故障
        Boolean lineRunState_4 = true;
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
        lineRunCount = getLineRunState(lineRunCount, lineRunState_1, lineRunState_2, lineRunState_4, stopExamineLine);
        stopExamineLines.add(stopExamineLine);
        return lineRunCount;
    }

    /**
     * 自检停车线的状态
     *
     * @param lineRunCount
     * @param lineRunState_1
     * @param lineRunState_2
     * @param lineRunState_4
     * @param stopExamineLine
     * @return
     */
    private int getLineRunState(int lineRunCount, Boolean lineRunState_1, Boolean lineRunState_2, Boolean lineRunState_4, StopExamineIndexVO.StopExamineLine stopExamineLine) {
        if (lineRunState_1) {
            stopExamineLine.setLineRunState(StopConstants.RUNSTATE_1);
        } else if (lineRunState_2) {
            lineRunCount++;
            stopExamineLine.setLineRunState(StopConstants.RUNSTATE_2);
        } else if (lineRunState_4) {
            stopExamineLine.setLineRunState(StopConstants.RUNSTATE_4);
        }
        return lineRunCount;
    }


    /**
     * 停车方案设置
     *
     * @param system
     * @return
     */
    public List<StopChoiceVO> getStopChoiceVOS(String system) {
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
            coal8Device = getMetricValue(StopConstants.Quit_SYS_1, MetricCodes.COAL_8_DEVICE);
            coal13Device = getMetricValue(StopConstants.Quit_SYS_1, MetricCodes.COAL_13_DEVICE);

        } else if (StopConstants.SYSTEM_2.equals(system)) {
            coal8Device = getMetricValue(StopConstants.Quit_SYS_2, MetricCodes.COAL_8_DEVICE);
            coal13Device = getMetricValue(StopConstants.Quit_SYS_2, MetricCodes.COAL_13_DEVICE);
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
        return stopChoiceVOList;
    }

    /**
     * 查询二期所有停车线
     *
     * @param system
     * @return
     */
    public List<String> getStopLinesBySystem(String system) {
        List<String> stopLineList = new ArrayList<>();
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(system));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            for (StopLine stopLine : stopLines) {
                stopLineList.add(String.valueOf(stopLine.getId()));
            }
        }
        return stopLineList;
    }


    /**
     * 获取所有停车设备id
     *
     * @param lineIds
     * @return
     */
    public Set<String> getStopDeviceIds(List<String> lineIds) {
        Set<String> startDeviceIds = new HashSet<>();
        for (String line : lineIds) {
            List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(Long.valueOf(line));
            for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
                List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
                for (StopInformation stopInformation : stopInformations) {
                    startDeviceIds.add(stopInformation.getThingCode());
                }
            }
        }
        return startDeviceIds;
    }

    /**
     * 发送json格式信息
     *
     * @param urlTopic
     * @param message
     */
    public void sendMessageTemplateByJson(String urlTopic, String message) {
        String jsonPause = "{\"message\":\"" + message + "\"}";
        JSONObject jsonObject = JSON.parseObject(jsonPause);
        sendMessagingTemplate(urlTopic, jsonObject);
    }

    /**
     * 存储订阅消息
     *
     * @param label
     * @param object
     */
    public void sendMessagingTemplate(String label, Object object) {
        if (blockingQueue == null) {
            // 已经首次建立消息发送任务
            blockingQueue = new LinkedBlockingQueue();
            // 首次建立消息发送任务
            createMessagingTemplateTimer();
        }
        StartMessage startMessage = new StartMessage();
        startMessage.setLabel(label);
        startMessage.setObject(object);
        try {
            blockingQueue.put(startMessage);
        } catch (Exception e) {
            logger.error("下发信号存储错误");
        }
    }

    protected void createMessagingTemplateTimer() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!blockingQueue.isEmpty()) {
                    try {
                        StopMessage stopMessage = (StopMessage) blockingQueue.take();
                        messagingTemplate.convertAndSend(stopMessage.getLabel(), stopMessage.getObject());
                    } catch (Exception e) {
                        logger.error("停车发送任务失败");
                    }
                }
            }
        };
        timer.schedule(timerTask, StopConstants.SEND_MESSAGE_CIRCLE_TIME, StopConstants.SEND_MESSAGE_WAIT_TIME);
    }

    /**
     * 停车自检
     *
     * @param stopExamineRules
     */
    public void autoExamineStarting(List<StopExamineRule> stopExamineRules) {
        List<StopExamineThing> examinethings = new ArrayList<>();
        for (StopExamineRule rule : stopExamineRules) {
            try {
                logger.info("停车自检,检查设备:{},检查内容:{}", rule.getExamineThingCode(), rule.getExamineMetricCode());
                String metricValue = getMetricValue(rule.getExamineThingCode(), rule.getExamineMetricCode());


                if (StringUtils.isNotEmpty(metricValue)) {

                    // updateExamineRecord(rule, metricValue);

                }
            } catch (Exception e) {
                logger.error("检查建立失败,错误内容{}", e);
                throw new StopException("自动检查任务失败，请稍后重新提交");
            }
            StopExamineThing stopExamineThing = new StopExamineThing();
            stopExamineThing.setMetricCode(rule.getExamineMetricCode());
        }
        //    startExamineListener.setStartExamineLabels(examineLabel);
     

    }


    /**
     * 查询指标数据
     *
     * @param thingCode
     * @param metricCode
     * @return
     */
    public String getMetricValue(String thingCode, String metricCode) {
        DataModelWrapper dataModelWrapper = dataService.getData(thingCode, metricCode).orElse(null);
        if (dataModelWrapper != null) {
            return dataModelWrapper.getValue();
        }
        return "";
    }

    /**
     * 查询设备的告警信息
     *
     * @param thingCode
     * @return
     */
    public String getThingAlertInfo(String thingCode) {

        AlertData maxLevelAlertData = stopService.getMaxLevelAlertData(thingCode);
        if (maxLevelAlertData != null) {
            return maxLevelAlertData.getAlertInfo();
        }
        return "";
    }

    /**
     * 解除人工干预
     *
     * @param system
     * @param thingCode
     * @return
     */
    public String relieveManualIntervention(String system, String thingCode) {
        String information;
        if (stopService.judgeStopingState(system, null, StopConstants.STOP_FINISH_STATE)) {
            Integer operteId = stopService.getStopOperateId(Integer.valueOf(system));
            stopService.updateStopManualInterventionRecord(thingCode, operteId, StopConstants.MANUAL_INTERVENTION_REMOVE, null);
            // 通知前端人工干预变化
            sendMessageTemplateByJson(StopConstants.URI_MANUAL_INTERVENTION, StopConstants.URI_MANUAL_INTERVENTION_MESSAGE_MANUAL_INTERVENTION_CHANGE);
            information = "success";
        } else {
            information = "noStarting";
        }
        return information;
    }

    /**
     * 完成停车操作
     */
    public void finishStopState(String system) {

        // 关闭停车
        stopService.updateStopOperate(system, StopConstants.STOP_FINISH_STATE);
    }

    /**
     * 更新停车自检记录
     *
     * @param thingCode
     * @param metricCode
     * @param value
     */
    public void updateExamineRecordByRule(String thingCode, String metricCode, String value) {

    }

    /**
     * 停车中操作(包括启车结束检查)
     */
    public void operateStoping(String system, String operate) {
        switch (operate) {
            case "pause":
                // 暂停停车
                if (StopConstants.SYSTEM_1.equals(system)) {
                    system1PauseState = true;
                } else if (StopConstants.SYSTEM_2.equals(system)) {
                    system2PauseState = true;
                }
                stopService.updateStopOperate(system, StopConstants.STOP_PAUSE_STATE);
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_PAUSE);
                break;
            case "continueStop":
                // 恢复停车
                if (StopConstants.SYSTEM_1.equals(system)) {
                    system1PauseState = false;
                } else if (StopConstants.SYSTEM_2.equals(system)) {
                    system2PauseState = false;
                }
                stopService.updateStopOperate(system, StopConstants.STOP_STOPING_STATE);
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_CONTINUE_STOP);
                break;
            case "closeStop":
                // 结束停车
                stopService.closeStopOperate(system);
                if (StopConstants.SYSTEM_1.equals(system)) {
                    system1PauseState = false;
                } else if (StopConstants.SYSTEM_2.equals(system)) {
                    system2PauseState = false;
                }
                sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_CLOSE_STOP);
                break;
            default:
                break;
        }

    }

    /**
     * 停车设备线总览
     *
     * @param system
     * @return
     */
    public StopIndexVO getStopIndex(String system) {
        StopIndexVO stopIndexVO = new StopIndexVO();
        Long runTime = null;
        //最后的启车的记录
        StartOperationRecord startOperationRecord = stopService.getStartOperationRecord(StopConstants.START_FINISH_STATE);
        if (startOperationRecord != null) {
            Date updateTime = startOperationRecord.getUpdateTime();
            runTime = DateTimeUtils.getDifferenceTime(updateTime, new Date(), StopConstants.TIMEFORMAT);
        }
        stopIndexVO.setThingRunTime(runTime);
        List<StopIndexVO.StopThingArea> stopThingAreas = new ArrayList<>();
        //查询大区下区域
        int lineRunCount = 0;
        List<StopDeviceArea> stopDeviceAreas = stopDeviceAreaService.getStopDeviceArea(StopConstants.REGION_1, Integer.valueOf(system));
        for (StopDeviceArea stopDeviceArea : stopDeviceAreas) {
            StopIndexVO.StopThingArea stopThingArea = stopIndexVO.new StopThingArea();
            stopThingArea.setAreaId(String.valueOf(stopDeviceArea.getId()));
            stopThingArea.setAreaName(stopDeviceArea.getAreaName());
            //区域下的停车线
            List<StopLine> stopLines = stopLineService.getStopLineByAreaId(stopDeviceArea.getId());
            stopThingArea.setLineCnt(String.valueOf(stopLines.size()));
            List<StopIndexVO.StopThingLine> stopThingLines = new ArrayList<>();
            for (StopLine stopLine : stopLines) {
                lineRunCount = getLineRunCount(stopIndexVO, lineRunCount, stopThingLines, stopLine);
            }
            stopThingArea.setStopThingLines(stopThingLines);
            stopThingAreas.add(stopThingArea);

        }
        stopIndexVO.setStopThingAreas(stopThingAreas);
        stopIndexVO.setThingRunCount(String.valueOf(lineRunCount));
        return stopIndexVO;
    }

    /**
     * @param stopIndexVO
     * @param lineRunCount
     * @param stopThingLines
     * @param stopLine
     * @return
     */
    private int getLineRunCount(StopIndexVO stopIndexVO, int lineRunCount, List<StopIndexVO.StopThingLine> stopThingLines, StopLine stopLine) {
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
        return lineRunCount;
    }

    /**
     * 停车自检修复
     *
     * @param ruleIds
     * @param tapOperate
     */
    public void handleExamine(List<Integer> ruleIds, String tapOperate) {
        for (Integer ruleId : ruleIds) {
            StopExamineRule stopExamineRule = stopService.getStopExamineRuleByRuleId(ruleId);

            if (stopExamineRule != null) {


                switch (stopExamineRule.getExamineType()) {
                    // 控制类型
                    case StopConstants.REMOTE_ERROR_TYPE:
                        cmdControl(stopExamineRule.getExamineThingCode(), stopExamineRule.getExamineMetricCode(), stopExamineRule.getCompareValue());
                        break;
                    // 液位检查
                    case StopConstants.LEVEL_ERROR_TYPE:
                        // 不做处理
                        //TODO 更新自检记录
                        break;
                    default:
                        break;

                }
            }
        }

    }

    /**
     * 查询停车的人工干预记录
     *
     * @return
     * @throws Exception
     */
    public List<StopManualInterventionAreaVO> getStopManualInterventionAreaVOS(String system) {
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
                getStopThingLine(stopManualInterventionAreaVO, stopThingLines, stopLine);
            }
            stopManualInterventionAreaVO.setStopThingLines(stopThingLines);
            manualInterventionAreaVOList.add(stopManualInterventionAreaVO);
        }
        return manualInterventionAreaVOList;
    }

    /**
     * @param stopManualInterventionAreaVO
     * @param stopThingLines
     * @param stopLine
     */
    private void getStopThingLine(StopManualInterventionAreaVO stopManualInterventionAreaVO, List<StopManualInterventionAreaVO.StopThingLine> stopThingLines, StopLine stopLine) {
        StopManualInterventionAreaVO.StopThingLine stopThingLine = stopManualInterventionAreaVO.new StopThingLine();
        int manualInterventionThing = 0;
        List<StopDeviceBag> stopDeviceBags = stopDeviceBagService.getStopDeviceBagByStartLineId(stopLine.getId());
        for (StopDeviceBag stopDeviceBag : stopDeviceBags) {
            List<StopInformation> stopInformations = stopInformationService.getStopInformationByBagId(stopDeviceBag.getId());
            for (StopInformation stopInformation : stopInformations) {
                StopManualIntervention stopManualInterventionByThingCode = stopManualInterventionService.getStopManualInterventionByThingCode(stopInformation.getThingCode());
                if (stopManualInterventionByThingCode != null && 1 == stopManualInterventionByThingCode.getState()) {
                    manualInterventionThing++;
                }
            }
        }
        stopThingLine.setLineId(String.valueOf(stopLine.getId()));
        stopThingLine.setLineName(stopLine.getLineName());
        stopThingLine.setManualCnt(String.valueOf(manualInterventionThing));

        stopThingLines.add(stopThingLine);
    }

    /**
     * 发送停车自检修复
     *
     * @param thingCode
     * @param metricCode
     * @param value
     */
    public void cmdControl(String thingCode, String metricCode, String value) {

        DataModel dataModel = new DataModel();
        dataModel.setThingCode(thingCode);
        dataModel.setMetricCode(metricCode);
        dataModel.setValue(value);
        logger.info("ThingCode{},MetricCode{},Value{}", thingCode, metricCode, value);
        CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, SessionContext.getCurrentUser().getRequestId());
        if (cmdSendResponseData.getOkCount() == 0) {
            logger.error("下发信号失败，失败原因：" + cmdSendResponseData.getErrorMessage());
        }

    }
}
