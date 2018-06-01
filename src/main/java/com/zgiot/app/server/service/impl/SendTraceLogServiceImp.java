package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.dataprocessor.DataListener;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
                }
            }
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
    public List<SendTraceLog> getSendTraceLogList(SendTraceLog condition, Date startTime, Date endTime) {
        List<SendTraceLog> sendTraceLogList = sendTraceLogMapper.getSendTraceLogList(condition, startTime, endTime);
        for (SendTraceLog sendTraceLog : sendTraceLogList) {
            String influenceMetricCode = sendTraceLog.getInfluenceMetricCode();
            getMetricShow(sendTraceLog);
            getThingShow(sendTraceLog);

            getStateShow(sendTraceLog, influenceMetricCode);
        }
        return sendTraceLogList;
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
