package com.zgiot.app.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.app.server.service.SendTraceLogService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.app.server.service.dao.SendTraceLogMapper;
import com.zgiot.app.server.service.pojo.*;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ThingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SendTraceLogServiceImp implements SendTraceLogService {
    /**
     * key: `influencethingCode`  + `--` +  `influenceMetricCode`  + `--` +  `expectedValue`
     */
    private Map<String, SendTraceLog> sendTraceMapWaitForFeedback = new HashMap<>();
    /**
     * key : `sendthingcode` + `--` + `sendmetriccode`
     */
    private Map<String, List<RelSendTraceInfo>> sendTraceRelMap = new HashMap<>();

    /**
     * key : sendmetriccode
     */
    private Map<String, List<SendTypeShowContent>> sendTypeShowContentMap = new HashMap<>();

    @Autowired
    private DataService dataService;
    @Autowired
    private SendTraceLogMapper sendTraceLogMapper;
    @Autowired
    private MetricService metricService;
    @Autowired
    private ThingService thingService;
    public static final String SPLIT_CODE = "--";
    public static final Logger logger = LoggerFactory.getLogger(SendTraceLogServiceImp.class);


    public void init() {
        List<RelSendTraceInfo> relSendTraceInfoList = sendTraceLogMapper.getRelSendTraceInfo();
        for (RelSendTraceInfo relSendTraceInfo : relSendTraceInfoList) {
            String sendThingCode = relSendTraceInfo.getSendThingCode();
            String sendMetricCode = relSendTraceInfo.getSendMetricCode();
            String sendThingMetricKey = sendThingCode + SPLIT_CODE + sendMetricCode;
            List<RelSendTraceInfo> thingMetricRelSendTraceInfos;
            if (sendTraceRelMap.containsKey(sendThingMetricKey)) {
                thingMetricRelSendTraceInfos = sendTraceRelMap.get(sendThingMetricKey);
            } else {
                thingMetricRelSendTraceInfos = new ArrayList<>();
                sendTraceRelMap.put(sendThingMetricKey, thingMetricRelSendTraceInfos);
            }
            thingMetricRelSendTraceInfos.add(relSendTraceInfo);
        }

        List<SendTypeShowContent> sendTypeShowContentList = sendTraceLogMapper.getSendTypeShowContentList();
        for (SendTypeShowContent sendTypeShowContent : sendTypeShowContentList) {
            List<SendTypeShowContent> sendTypeShowContentMetricList;
            if (sendTypeShowContentMap.containsKey(sendTypeShowContent.getStateMetricCode())) {
                sendTypeShowContentMetricList = sendTypeShowContentMap.get(sendTypeShowContent.getStateMetricCode());
            } else {
                sendTypeShowContentMetricList = new ArrayList<>();
                sendTypeShowContentMap.put(sendTypeShowContent.getStateMetricCode(), sendTypeShowContentMetricList);
            }
            sendTypeShowContentMetricList.add(sendTypeShowContent);
        }
    }


    @Override
    public void recordSendTraceLog(SendInfo sendInfo) {
        String sendThingMetricKey = sendInfo.getThingCode() + SPLIT_CODE + sendInfo.getMetricCode();
        if (sendTraceRelMap.containsKey(sendThingMetricKey)) {
            List<RelSendTraceInfo> relSendTraceInfoList = sendTraceRelMap.get(sendThingMetricKey);
            for (RelSendTraceInfo sendTrace : relSendTraceInfoList) {
                if (sendTrace.getSendValue().equalsIgnoreCase(sendInfo.getSendValue())) {
                    String influenceMetricCode = sendTrace.getInfluenceMetricCode();
                    String influenceThingCode = sendTrace.getInfluenceThingCode();
                    String expectedValue = sendTrace.getExpectedValue();
                    SendTraceLog sendTraceLog = new SendTraceLog(sendInfo);
                    Optional<DataModelWrapper> data = dataService.getData(influenceThingCode, influenceMetricCode);
                    if (data.isPresent()) {
                        sendTraceLog.setPreValue(data.get().getValue());
                    }
                    sendTraceLog.setSendType(sendTrace.getSendType());
                    sendTraceLog.setInfluenceMetricCode(influenceMetricCode);
                    String influenceThingMetricKey = influenceThingCode + SPLIT_CODE + influenceMetricCode + SPLIT_CODE + expectedValue.toUpperCase();
                    sendTraceMapWaitForFeedback.put(influenceThingMetricKey, sendTraceLog);
                    logger.debug("下发信号,thingCode:{},metricCode:{},value:{},user:{},platform:{},该信号需要记录日志,跟踪信号点thingCode:{},metricCode:{},期望值:{}"
                            , sendInfo.getThingCode(), sendInfo.getMetricCode(), sendInfo.getSendValue(), sendInfo.getUserName(), sendInfo.getPlatform(), influenceThingCode, influenceMetricCode, expectedValue);
                }

            }
        } else {
            logger.debug("下发信号，thingCode:{},metricCode:{},value:{},user:{},platform:{},该信号不需要记录日志，被忽略", sendInfo.getThingCode(), sendInfo.getMetricCode(), sendInfo.getSendValue(), sendInfo.getUserName(), sendInfo.getPlatform());
        }
    }

    @Override
    public Map<String, SendTraceLog> getSendInfoWithoutFeedback() {
        return sendTraceMapWaitForFeedback;
    }

    @Override
    public void onDataChange(DataModel dataModel) {
        String influenceThingMetricKey = dataModel.getThingCode() + SPLIT_CODE + dataModel.getMetricCode() + SPLIT_CODE + dataModel.getValue().toUpperCase();
        if (sendTraceMapWaitForFeedback.containsKey(influenceThingMetricKey)) {
            logger.debug("收到信号，thingCode:{},metricCode:{},value:{},与用户:{}在平台:{}下发的信号thingCode:{},metricCode:{},value:{}相关联，记录日志");
            SendTraceLog sendTraceLog = sendTraceMapWaitForFeedback.get(influenceThingMetricKey);
            sendTraceLog.setCurrentValue(dataModel.getValue());
            sendTraceLog.setSendTime(dataModel.getDataTimeStamp());
            sendTraceLogMapper.insertSendTraceLog(sendTraceLog);
        }
    }

    @Override
    public void onError(Throwable error) {

    }

    @Override
    public PageInfo<SendTraceLog> getSendTraceLogList(SendTraceLog condition, Date startTime, Date endTime, Integer page, Integer count) {
        PageHelper.startPage(page, count, true, false, null);
        List<SendTraceLog> sendTraceLogList = sendTraceLogMapper.getSendTraceLogList(condition, startTime, endTime);
        PageInfo<SendTraceLog> pageInfo = new PageInfo<>(sendTraceLogList);
        List<SendTraceLog> list = pageInfo.getList();
        for (SendTraceLog sendTraceLog : list) {
            String influenceMetricCode = sendTraceLog.getInfluenceMetricCode();
            getMetricShow(sendTraceLog);
            getThingShow(sendTraceLog);

            getStateShow(sendTraceLog, influenceMetricCode);
        }
        return pageInfo;
    }

    private void getThingShow(SendTraceLog sendTraceLog) {
        ThingModel thing = thingService.getThing(sendTraceLog.getSendThingCode());
        if (thing != null) {
            sendTraceLog.setSendThingName(thing.getThingName());
        }
    }

    private void getMetricShow(SendTraceLog sendTraceLog) {
        MetricModel metric = metricService.getMetric(sendTraceLog.getSendMetricCode());
        if (metric != null) {
            sendTraceLog.setSendMetricName(metric.getMetricName());
        }
    }

    private void getStateShow(SendTraceLog sendTraceLog, String influenceMetricCode) {
        if (sendTypeShowContentMap.containsKey(influenceMetricCode)) {
            List<SendTypeShowContent> sendTypeShowContents = sendTypeShowContentMap.get(influenceMetricCode);
            for (SendTypeShowContent sendTypeShowContent : sendTypeShowContents) {
                if (sendTypeShowContent.getStateValue().equalsIgnoreCase(sendTraceLog.getCurrentValue())) {
                    sendTraceLog.setCurrentValueShow(sendTypeShowContent.getShowContent());
                }
                if (sendTypeShowContent.getStateValue().equalsIgnoreCase(sendTraceLog.getPreValue())) {
                    sendTraceLog.setPreValueShow(sendTypeShowContent.getShowContent());
                }
            }
        }
    }

    @Override
    public List<SendType> getSendTypeList() {
        return sendTraceLogMapper.getSendTypeList();
    }
}
