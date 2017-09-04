package com.zgiot.app.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.exception.CmdSendException;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.util.RequestIdUtil;
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
    @Autowired
    private DataEngineTemplate dataEngineTemplate;

    @Override
    public int sendCmd(DataModel dataModel, String requestId) {
        return sendCmd(Collections.singletonList(dataModel), requestId);
    }

    @Override
    public int sendCmd(List<DataModel> dataModelList, String requestId) {
        if (StringUtils.isEmpty(requestId)) {
            throw new CmdSendException(new NullPointerException("request id cannot be null"));
        }
        String data = JSON.toJSONString(dataModelList);
        ServerResponse response;
        try {
            response = dataEngineTemplate.postForObject(DataEngineTemplate.CMD_URI, data, ServerResponse.class);
            logger.trace("received:{}", response);
        } catch (RestClientException e) {
            throw new CmdSendException(e);
        }
        return Integer.valueOf(response.getMessage());
    }

}
