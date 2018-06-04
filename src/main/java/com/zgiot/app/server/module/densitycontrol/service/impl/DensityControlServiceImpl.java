package com.zgiot.app.server.module.densitycontrol.service.impl;

import com.zgiot.app.server.module.densitycontrol.dto.DensityControlDTO;
import com.zgiot.app.server.module.densitycontrol.mapper.DensityControlConfigMapper;
import com.zgiot.app.server.module.densitycontrol.pojo.DensityControlConfig;
import com.zgiot.app.server.module.densitycontrol.service.DensityControlService;
import com.zgiot.app.server.module.densitycontrol.service.ParamCache;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zgiot.app.server.module.densitycontrol.constants.DensitycontrolConstants.*;
import static com.zgiot.common.constants.MetricCodes.*;

@Service
public class DensityControlServiceImpl implements DensityControlService {

    private static final Logger logger = LoggerFactory.getLogger(DensityControlServiceImpl.class);

    @Autowired
    private DensityControlConfigMapper densityControlConfigMapper;
    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ParamCache paramCache;

    private static String CMD_FAILED_LOG = "下发信号失败，失败原因：";
    private static Map<String, String> baseParamMap = new HashMap<>();

    static {
        baseParamMap.put("LE_H_SET", SETTED_HIGH_LEVEL);
        baseParamMap.put("PRE_STOP_DENSITY_HIGH_SET", PRE_STOP_SETTED_HIGH_LEVEL);
        baseParamMap.put("LE_PJ_SET", LE_PJ_SET);
        baseParamMap.put("LE_L_SET", SETTED_LOW_LEVEL);
        baseParamMap.put("DELAY_TEST", DELAY_TEST);
        baseParamMap.put("DELAY_TEST_FL", DELAY_TEST_FL);
        baseParamMap.put("DELAY_TEST_BS", DELAY_TEST_BS);
        baseParamMap.put("TIME_FL", TIME_FL);
        baseParamMap.put("TIME_BS", TIME_BS);
        baseParamMap.put("TIME_BS_LOW", TIME_BS_LOW);
        baseParamMap.put("TIME_FL_HIGH", TIME_FL_HIGH);
        baseParamMap.put("TIME_EXECUTE_HIGH", TIME_EXECUTE_HIGH);
        baseParamMap.put("TIME_STOP", TIME_STOP);
    }

    @Override
    public List<DensityControlConfig> getAllDensityControlConfig() {
        return densityControlConfigMapper.getAllDensityControlConfig();
    }

    @Override
    public void updateDensityControlConfig(DensityControlDTO densityControlDTO) {
        // TODO 整理数据,存入MySql和模块缓存
        DensityControlConfig densityControlConfig = new DensityControlConfig();
        densityControlConfigMapper.updateDensityControlConfig(densityControlConfig);
    }

    @Override
    public void switchDensityControl(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        DataModelWrapper dataModelWrapper = dataService.getData(thingCode, metricCode).orElse(null);
        if (dataModelWrapper != null) {
            if (!dataModel.getValue().equals(dataModelWrapper.getValue())) {
                if (Boolean.TRUE.toString().equals(dataModel.getValue())) {
                    // 初始化模块缓存和KepServer数据
                    paramCache.init();

                    // 改点状态写入KepServer
                    CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, "beginDensityControl");
                    if (cmdSendResponseData.getOkCount() <= 0) {
                        logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
                    }
                } else if (Boolean.FALSE.toString().equals(dataModel.getValue())) {
                    // 改点状态写入KepServer
                    CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(dataModel, "endDensityControl");
                    if (cmdSendResponseData.getOkCount() <= 0) {
                        logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
                    }

                    // 清空模块缓存
                    paramCache.clear();
                }
            }
        }
//        else {
//            // TODO 从dataengine获取
//        }
    }

    @Override
    public void setBaseParam(DensityControlConfig densityControlConfig) {
        if (baseParamMap.containsKey(densityControlConfig.getMetricCode())) {
            CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                    densityControlConfig.getThingCode(), null, densityControlConfig.getMetricCode(),
                    densityControlConfig.getMetricValue(), new Date()), "setDensityControlBaseParam");
            if (cmdSendResponseData.getOkCount() <= 0) {
                logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
            }
        }
    }

}
