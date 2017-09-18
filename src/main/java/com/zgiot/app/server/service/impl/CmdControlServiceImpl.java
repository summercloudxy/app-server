package com.zgiot.app.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.exception.CmdPulseFirstSendException;
import com.zgiot.app.server.exception.CmdPulseSecondSendException;
import com.zgiot.app.server.exception.CmdSendException;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CmdControlServiceImpl implements CmdControlService {
    private static final Logger logger = LoggerFactory.getLogger(CmdControlService.class);
    private static final int DEFAULT_RETRY_COUNT = 5;
    private static final int RETURN_CODE_SUCCESS = 1;
    private static final int DEFAULT_SEND_DELAY_TIME = 0;
    private static final int SEND_PERIOD = 1000;
    @Autowired
    private DataEngineTemplate dataEngineTemplate;

    @Override
    public int sendCmd(DataModel dataModel, String requestId) {
        return sendCmd(Collections.singletonList(dataModel), requestId);
    }

    @Override
    public int sendCmd(List<DataModel> dataModelList, String requestId) {
        if (StringUtils.isEmpty(requestId)) {
            return SysException.EC_CMD_FAILED;
            // throw new CmdSendException(new NullPointerException("request id cannot be
            // null"));
        }
        String data = JSON.toJSONString(dataModelList);
        ServerResponse response;
        try {
            response = dataEngineTemplate.postForObject(DataEngineTemplate.CMD_URI, data, ServerResponse.class);
            logger.trace("received:{}", response);
        } catch (RestClientException e) {
            // throw new CmdSendException(e);
            return SysException.EC_CMD_FAILED;
        }
        return Integer.valueOf(response.getMessage());
    }

    public int sendPulseCmd(DataModel dataModel, Integer delayTime, Integer retryCount, String requestId) {
        final Integer realRetryCount = retryCount == null ? DEFAULT_RETRY_COUNT : retryCount;
        if (delayTime == null) {
            delayTime = DEFAULT_SEND_DELAY_TIME;
        }
        String value = dataModel.getValue();
        Boolean boolValue;
        if (Boolean.TRUE.toString().equalsIgnoreCase(value) || Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            boolValue = Boolean.valueOf(value);
        } else {
            return SysException.EC_CMD_FAILED;
            // throw new CmdSendException("data type error", SysException.EC_UNKOWN);
        }
        if (sendCmd(dataModel, requestId) < SysException.EC_SUCCESS) {
            return SysException.EC_CMD_PULSE_FIRST_FAILED;
        }
        // try {
        // sendCmd(dataModel, requestId);
        // } catch (Exception e) {
        // throw new CmdPulseFirstSendException("failed send first pulse",
        // SysException.EC_UNKOWN);
        // }
        dataModel.setValue(Boolean.toString(!boolValue));
        Timer timer = new Timer();
        AtomicBoolean sendState = new AtomicBoolean(false);
        timer.schedule(new TimerTask() {
            private int count = 0;

            @Override
            public void run() {
                count++;
                if (count == realRetryCount) {
                    cancel();
                }
                if (sendCmd(Collections.singletonList(dataModel), requestId) >= SysException.EC_SUCCESS) {
                    sendState.set(true);
                    cancel();
                }
                // try {
                // sendCmd(Collections.singletonList(dataModel), requestId);
                // sendState.set(true);
                // cancel();
                // } catch (Exception e) {
                // logger.error("failed send second pulse,retry number: {}", count);
                // }
            }
        }, delayTime, SEND_PERIOD);
        if (!sendState.get()) {
            return SysException.EC_CMD_PULSE_SECOND_FAILED;
            // throw new CmdPulseSecondSendException("failed send second pulse",
            // SysException.EC_UNKOWN);
        }
        return RETURN_CODE_SUCCESS;
    }

}
