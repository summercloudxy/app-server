package com.zgiot.app.server.module.sfstop.controller;

import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.exception.StopException;
import com.zgiot.app.server.module.sfstop.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private StopLineService stopLineService;
    @Autowired
    private StopDeviceBagService stopDeviceBagService;
    @Autowired
    private StopInformationService stopInformationService;


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
}
