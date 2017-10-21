package com.zgiot.app.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import springfox.documentation.spring.web.json.Json;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmdControlServiceImpl implements CmdControlService {
    private static final Logger logger = LoggerFactory.getLogger(CmdControlService.class);
    private static final int DEFAULT_RETRY_COUNT = 5;
    private static final int RETURN_CODE_SUCCESS = 1;
    private static final int DEFAULT_SEND_DELAY_TIME = 0;
    private static final int SEND_PERIOD = 1000;
    private static final int VALUE_TRUE = 1;
    private static final int VALUE_FALSE = 0;
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
        String value = dataModel.getValue();
        Boolean boolValue;
        if (Boolean.TRUE.toString().equalsIgnoreCase(value) || Boolean.FALSE.toString().equalsIgnoreCase(value)) {
            boolValue = Boolean.valueOf(value);
        } else {
            throw new SysException("data type error", SysException.EC_CMD_FAILED);
        }
        // 信号首次发送
        sendfirst(dataModel, requestId);
        dataModel.setValue(Boolean.toString(!boolValue));
        // 信号脉冲清除发送
        sendSecond(dataModel, retryPeriod, retryCount, requestId, null);
        return RETURN_CODE_SUCCESS;
    }

    /**
     * 根据信号位置下发信号
     * @param dataModel 信号发送类（包括信号地址和发送值，本接口无视发送值）
     * @param retryPeriod 重发等待时间
     * @param retryCount 重发次数
     * @param requestId 请求id
     * @param position 发送位置
     * @param cleanPeriod 清除等待时间
     * @param isHolding 是否保持
     * @return
     */
    public int sendPulseCmdBoolByShort(DataModel dataModel, Integer retryPeriod, Integer retryCount, String requestId
            , int position, int cleanPeriod, boolean isHolding){
        if(1 > position){
            throw new SysException("data type error", SysException.EC_CMD_FAILED);
        }
        String readValue = getDataSync(dataModel);
        int valueInt = Integer.parseInt(readValue);
        boolean dataModelOperate = Boolean.parseBoolean(dataModel.getValue());
        if(dataModelOperate && getBit(valueInt, position, VALUE_FALSE)) {
            valueInt += (int) Math.pow(2, (position - 1));
        }
        if(!dataModelOperate && getBit(valueInt, position, VALUE_TRUE)){
            valueInt -= (int) Math.pow(2, (position - 1));
        }
        if(0 > valueInt){
            throw new SysException("valueInt is less than zero", SysException.EC_CMD_FAILED);
        }
        // 信号首次发送
        dataModel.setMetricCategoryCode(MetricModel.CATEGORY_SIGNAL);
        dataModel.setValue(String.valueOf(valueInt));
        sendfirst(dataModel, requestId);
        // 信号脉冲清除发送
        if(!isHolding) {
            String readValueBeforeClean = getDataSync(dataModel);
            int cleanValueInt = Integer.parseInt(readValueBeforeClean);
            if(dataModelOperate && getBit(cleanValueInt, position, VALUE_TRUE)){
                cleanValueInt -= (int) Math.pow(2, (position - 1));
            }
            if(!dataModelOperate && getBit(cleanValueInt, position, VALUE_FALSE)) {
                cleanValueInt += (int) Math.pow(2, (position - 1));
            }
            dataModel.setValue(String.valueOf(cleanValueInt));
            sendSecond(dataModel, retryPeriod, retryCount, requestId, cleanPeriod);
        }
        return RETURN_CODE_SUCCESS;
    }

    /**
     * 信号首次发送
     * @param dataModel 信号发送类（包括信号地址和发送值）
     * @param requestId 请求id
     */
    private void sendfirst(DataModel dataModel, String requestId){
        try {
            sendCmd(dataModel, requestId);
        } catch (Exception e) {
            throw new SysException("failed send first pulse", SysException.EC_CMD_PULSE_FIRST_FAILED);
        }
    }

    /**
     * 信号重新发送清除
     * @param dataModel 信号发送类（包括信号地址和发送值）
     * @param retryPeriod 重发等待时间
     * @param retryCount 重发次数
     * @param requestId 请求id
     */
    private void sendSecond(DataModel dataModel, Integer retryPeriod, Integer retryCount, String requestId, Integer cleanPeriod){
        final Integer realRetryCount = retryCount == null ? DEFAULT_RETRY_COUNT : retryCount;
        if (retryPeriod == null) {
            retryPeriod = SEND_PERIOD;
        }
        Boolean state = false;
        for (int i = 1; i <= realRetryCount; i++) {
            try {
                if (null != cleanPeriod) {
                    Thread.sleep(cleanPeriod);
                }
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
    }

    /**
     * 判断二进制第几位是否为预期值
     * @param value
     * @param position
     * @return
     */
     boolean getBit(int value, int position, int compareValue){
        boolean flag = false;
        for(int i=1;i<= position;i++){
            if(value %2 == compareValue){
                flag = true;
            }else{
                flag = false;
            }
            value = value/2;
        }
        return flag;
    }

    /**
     * 获取/data/sync接口反馈数据值
     * @param dataModel
     * @return
     */
    public String getDataSync(DataModel dataModel){
        ServerResponse response = null;
        try {
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("thingCode", dataModel.getThingCode());
            uriVariables.put("metricCode", dataModel.getMetricCode());

            String readDataModelByString = dataEngineTemplate.getForObject(DataEngineTemplate.URI_DATA_SYNC + "/{thingCode}/{metricCode}"
                    , String.class, uriVariables);

            response = JSON.parseObject(readDataModelByString, ServerResponse.class );

        } catch (RestClientException e) {
            throw new SysException(response.getErrorMsg(), SysException.EC_CMD_FAILED);
        }
        String readValue = (String) response.getObj();
        return readValue;
    }
}
