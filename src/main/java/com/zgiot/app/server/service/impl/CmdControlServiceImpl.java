package com.zgiot.app.server.service.impl;

import com.alibaba.fastjson.JSON;
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
    public CmdSendResponseData sendCmd(DataModel dataModel, String requestId) {
        return sendCmd(Collections.singletonList(dataModel), requestId);
    }

    @Override
    public CmdSendResponseData sendCmd(List<DataModel> dataModelList, String requestId) {
        CmdSendResponseData cmdSendResponseData = new CmdSendResponseData();
        if (StringUtils.isEmpty(requestId)) {
            throw new SysException("request id cannot be null", SysException.EC_CMD_FAILED);
        }
        String data = JSON.toJSONString(dataModelList);
        ServerResponse response = null;
        try { 
            String resStr = dataEngineTemplate.postForObject(DataEngineTemplate.URI_CMD, data, String.class);
            logger.debug("dataengine response: `{}`", resStr);
            response = JSON.parseObject(resStr,ServerResponse.class);

            cmdSendResponseData.setErrorMessage(response.getErrorMsg());
            cmdSendResponseData.setOkCount((Integer) response.getObj());
        } catch (RestClientException e) {
            throw new SysException(response.getErrorMsg(), SysException.EC_CMD_FAILED);
        }

        return cmdSendResponseData;
    }

    public int sendPulseCmd(DataModel dataModel, Integer retryPeriod, Integer retryCount, String requestId) {
        final Integer realRetryCount = retryCount == null ? DEFAULT_RETRY_COUNT : retryCount;
        if (retryPeriod == null) {
            retryPeriod = SEND_PERIOD;
        }
        String value = dataModel.getValue();
        Boolean boolValue;
        if (Boolean.TRUE.toString().equalsIgnoreCase(value) || Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            boolValue = Boolean.valueOf(value);
        } else {
            throw new SysException("data type error", SysException.EC_CMD_FAILED);
        }
        try {
            sendCmd(dataModel, requestId);
        } catch (Exception e) {
            throw new SysException("failed send first pulse", SysException.EC_CMD_PULSE_FIRST_FAILED);
        }
        dataModel.setValue(Boolean.toString(!boolValue));
        Boolean state = false;
        for (int i = 1; i <= realRetryCount; i++) {
            try {
                sendCmd(Collections.singletonList(dataModel), requestId);
                state = true;
                break;
            } catch (Exception e) {
                logger.error("failed send second pulse,retry number: {}", i);
            }
            try {
                Thread.sleep(retryPeriod);
            } catch (InterruptedException e) {
                logger.error("thread is interrupted");
            }
        }
        if (!state) {
            throw new SysException("failed send second pulse", SysException.EC_CMD_PULSE_SECOND_FAILED);
        }
        return RETURN_CODE_SUCCESS;
    }

}
