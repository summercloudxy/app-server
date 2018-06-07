package com.zgiot.app.server.module.sfstop.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.*;
import com.zgiot.app.server.module.sfstop.service.*;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        List<StopInformation> stopInformationList = stopHandler.getStopInformations(lineId);
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
        List<StopManualInterventionAreaVO> manualInterventionAreaVOList = stopHandler.getStopManualInterventionAreaVOS(system);
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
        List<StopManualInterventionVO> stopManualInterventionList = stopHandler.getStopManualInterventionVOS(lineId);
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
        List<StopChoiceVO> stopChoiceVOList = stopHandler.getStopChoiceVOS(system);
        return new ResponseEntity<>(ServerResponse.buildOkJson(stopChoiceVOList), HttpStatus.OK);

    }


    /**
     * TCS系统检修模式设置
     *
     * @return
     */
    @RequestMapping(value = "/getTcsBucketCheck", method = RequestMethod.GET)
    public ResponseEntity<String> getTcsBucketCheck(@RequestParam("system") String system) {
        Map<String, List<TcsBucketVO>> map = stopHandler.getStringListMap(system);
        return new ResponseEntity<>(ServerResponse.buildOkJson(map), HttpStatus.OK);

    }

    /**
     * 查询停车线的详情
     *
     * @return
     */
    @RequestMapping(value = "/getStopLineDetails", method = RequestMethod.GET)
    public ResponseEntity<String> getStopLineDetails(@RequestParam("lineId") String lineId) {
        List<StopLineDetailVO> stopLineDetailVOList = stopHandler.getStopLineDetailVOS(lineId);
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
        StopExamineIndexVO stopExamineIndexVO = stopHandler.getStopExamineIndexVO(system);
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
        StopingIndexVO stopingIndexVO = stopHandler.getStopingIndexVO(system);
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
        StopThingDetail stopThingDetail = stopHandler.getStopThingDetail(thingCode, system);

        return new ResponseEntity<>(ServerResponse.buildOkJson(stopThingDetail), HttpStatus.OK);
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
