package com.zgiot.app.server.module.densitycontrol;

import com.zgiot.app.server.module.densitycontrol.dto.DensityControlStatus;
import com.zgiot.app.server.module.densitycontrol.service.ParamCache;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.zgiot.app.server.module.densitycontrol.constants.DensitycontrolConstants.*;
import static com.zgiot.common.constants.MetricCodes.*;

@Component
public class DensityControlManager2 {

    private static final Logger logger = LoggerFactory.getLogger(DensityControlManager2.class);

    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ParamCache paramCache;

    private static String CMD_FAILED_LOG = "下发信号失败，失败原因：";
    private AtomicLong timeExecuteHigh = new AtomicLong(0);
    public static AtomicBoolean isRunning = new AtomicBoolean(false);

    public boolean isRunning() {
        return isRunning.get();
    }

    /**
     * 液位变化处理
     *
     * @param dataModel
     */
    public void handleLevel(DataModel dataModel) {
        isRunning.set(true);

        String thingCode = dataModel.getThingCode();
        Double value = Double.valueOf(dataModel.getValue());

        String levelSetHigh = paramCache.getValue(thingCode, SETTED_HIGH_LEVEL).getValue();
        String levelSetLow = paramCache.getValue(thingCode, SETTED_LOW_LEVEL).getValue();

        // 获取当前系统运行状态
        DensityControlStatus densityControlStatus = new DensityControlStatus();
        densityControlStatus.setLevel(value);
        getSystemStatus(densityControlStatus);
        if (SYSTEM_STATUS_0.equals(densityControlStatus.getSystemStatus())) {
            return;
        }

        // 获取当前主控密度计密度
        getDensityData(densityControlStatus);

        String timeSet = paramCache.getValue(thingCode, LE_H_EXECUTE_TIME).getValue();
        if (new Date().getTime() > (timeExecuteHigh.get() + Long.parseLong(timeSet))) {
            if (value > Double.parseDouble(levelSetHigh)) {
                // 高液位处理
                handleHighLevel();
            } else if (value < Double.parseDouble(levelSetLow)) {
                // 低液位处理
                handleLowLevel(densityControlStatus);
            } else {
                // 正常液位处理
                handleNormalLevel(densityControlStatus);
            }
        }

        isRunning.set(false);
    }

    /**
     * 获取当前系统运行状态
     *
     * @return
     */
    private void getSystemStatus(DensityControlStatus densityControlStatus) {
        DataModelWrapper dataA = dataService.getData(TERM_TWO_SYSTEM_A_THING_CODE, RUN_STATE).orElse(null);
        String runsA = dataA == null ? "0" : dataA.getValue();
        String thingCodeA = dataA == null ? "" : dataA.getThingCode();

        DataModelWrapper dataB = dataService.getData(TERM_TWO_SYSTEM_B_THING_CODE, RUN_STATE).orElse(null);
        String runsB = dataB == null ? "0" : dataB.getValue();
        String thingCodeB = dataB == null ? "" : dataB.getThingCode();

        if (RUN_STATE_1.equals(runsA) && !RUN_STATE_1.equals(runsB)) {
            densityControlStatus.setSystemStatus(SYSTEM_STATUS_1);
            densityControlStatus.setSystemThingCode1(thingCodeA);
        } else if (RUN_STATE_1.equals(runsB) && !RUN_STATE_1.equals(runsA)) {
            densityControlStatus.setSystemStatus(SYSTEM_STATUS_1);
            densityControlStatus.setSystemThingCode1(thingCodeB);
        } else if (RUN_STATE_1.equals(runsA) && RUN_STATE_1.equals(runsB)) {
            densityControlStatus.setSystemStatus(SYSTEM_STATUS_2);
            densityControlStatus.setSystemThingCode1(thingCodeA);
            densityControlStatus.setSystemThingCode2(thingCodeB);
        } else {
            densityControlStatus.setSystemStatus(SYSTEM_STATUS_0);
        }
    }

    /**
     * 获取当前主控密度计密度
     *
     * @param densityControlStatus
     */
    private void getDensityData(DensityControlStatus densityControlStatus) {
        DataModelWrapper dataA = dataService.getData(TERM_TWO_SYSTEM_A_THING_CODE, DENSITY_CONTROL_MODE).orElse(null);
        String runsA = dataA == null ? "" : dataA.getValue();
        String thingCodeA = dataA == null ? "" : dataA.getThingCode();

        DataModelWrapper dataB = dataService.getData(TERM_TWO_SYSTEM_B_THING_CODE, DENSITY_CONTROL_MODE).orElse(null);
        String thingCodeB = dataB == null ? "" : dataB.getThingCode();

        if (Boolean.TRUE.toString().equals(runsA)) {
            densityControlStatus.setDensityThingCode(thingCodeA);
            DataModelWrapper data = dataService.getData(thingCodeA, CURRENT_DENSITY).orElse(null);
            if (data != null) {
                densityControlStatus.setDensity(StringUtils.isBlank(data.getValue()) ? 0.0 : Double.parseDouble(data.getValue()));
            }
        } else {
            densityControlStatus.setDensityThingCode(thingCodeB);
            DataModelWrapper data = dataService.getData(thingCodeB, CURRENT_DENSITY).orElse(null);
            if (data != null) {
                densityControlStatus.setDensity(StringUtils.isBlank(data.getValue()) ? 0.0 : Double.parseDouble(data.getValue()));
            }
        }
    }

    /**
     * 高液位处理
     */
    private void handleHighLevel() {
        CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                TERM_TWO_THING_CODE, null, LEVEL_MODE, HIGH_MODE, new Date()), "beginHandleHighLevel");
        if (cmdSendResponseData.getOkCount() <= 0) {
            logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
        }
        timeExecuteHigh = new AtomicLong(new Date().getTime());
    }

    /**
     * 低液位处理
     *
     * @param densityControlStatus
     */
    private void handleLowLevel(DensityControlStatus densityControlStatus) {
        CmdControlService.CmdSendResponseData csrd = cmdControlService.sendCmd(new DataModel(null,
                TERM_TWO_THING_CODE, null, LEVEL_MODE, LOW_MODE, new Date()), "beginHandleLowLevel");
        if (csrd.getOkCount() <= 0) {
            logger.error(CMD_FAILED_LOG + csrd.getErrorMessage(), SysException.EC_CMD_FAILED);
        }

        // 获取当前密度
        Double density = densityControlStatus.getDensity();

        // 获取设定密度
        DataModelWrapper dataSet = paramCache.getValue(TERM_TWO_THING_CODE, SETTED_DENSITY);
        Double densitySet = 0.0;
        if (dataSet != null) {
            densitySet = new BigDecimal(density).multiply(new BigDecimal(dataSet.getValue())).doubleValue();
        }

        // 计算设定密度最小值
        DataModelWrapper dataMin = paramCache.getValue(TERM_TWO_THING_CODE, LE_L_DENSITY_RANGE_MIN);
        Double densityMin = 0.0;
        if (dataMin != null) {
            densityMin = new BigDecimal(densitySet).multiply(new BigDecimal(1).divide(new BigDecimal(dataMin.getValue()))).doubleValue();
        }

        if (density > densityMin) {
            // 执行正常液位逻辑
            sendNormalLevelMode();
        } else {
            // 检测合介桶液位
            DataModelWrapper levelData = dataService.getData(TERM_TWO_THING_CODE, CURRENT_LEVEL_M).orElse(null);
            Double level = levelData == null ? 0.0 : Double.parseDouble(levelData.getValue());

            // 获取设定低液位
            DataModelWrapper levelSetData = paramCache.getValue(TERM_TWO_THING_CODE, SETTED_LOW_LEVEL);
            String levelSet = levelSetData == null ? "" : levelSetData.getValue();

            // 获取低液位最大和最小范围
            String levelRangeMax = paramCache.getValue(TERM_TWO_THING_CODE, LE_L_LEVEL_RANGE_MAX).getValue();
            Double levelMax = new BigDecimal(levelSet).multiply(new BigDecimal(1).add(new BigDecimal(levelRangeMax))).doubleValue();
            String levelRangeMin = paramCache.getValue(TERM_TWO_THING_CODE, LE_L_LEVEL_RANGE_MIN).getValue();
            Double levelMin = new BigDecimal(levelSet).multiply(new BigDecimal(1).add(new BigDecimal(levelRangeMin))).doubleValue();

            if (level < levelMin) {
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                        TERM_TWO_THING_CODE, null, LOW_LEVEL_FLU,
                        LOW_MODE, new Date()), "sendLowLowLevelMode");
                if (cmdSendResponseData.getOkCount() <= 0) {
                    logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
                }
            } else if (level > levelMax) {
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                        TERM_TWO_THING_CODE, null, LOW_LEVEL_FLU,
                        HIGH_MODE, new Date()), "sendLowHighLevelMode");
                if (cmdSendResponseData.getOkCount() <= 0) {
                    logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
                }
            } else {
                CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                        TERM_TWO_THING_CODE, null, LOW_LEVEL_FLU,
                        NORMAL_MODE, new Date()), "sendLowNormalLevelMode");
                if (cmdSendResponseData.getOkCount() <= 0) {
                    logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
                }
            }
        }
    }

    /**
     * 正常液位处理
     *
     * @param densityControlStatus
     */
    private void handleNormalLevel(DensityControlStatus densityControlStatus) {
        sendNormalLevelMode();

        // 当前密度
        Double density = densityControlStatus.getDensity();

        // 设定密度
        DataModelWrapper dataSet = paramCache.getValue(TERM_TWO_THING_CODE, SETTED_DENSITY);
        Double densitySet = 0.0;
        if (dataSet != null) {
            densitySet = new BigDecimal(density).multiply(new BigDecimal(dataSet.getValue())).doubleValue();
        }

        // 设定密度最大值
        DataModelWrapper dataMax = paramCache.getValue(TERM_TWO_THING_CODE, LE_N_DENSITY_RANGE_MAX);
        Double densityMax = 0.0;
        if (dataMax != null) {
            densityMax = new BigDecimal(densitySet).multiply(new BigDecimal(1).add(new BigDecimal(dataMax.getValue()))).doubleValue();
        }

        // 设定密度最小值
        DataModelWrapper dataMin = paramCache.getValue(TERM_TWO_THING_CODE, LE_N_DENSITY_RANGE_MIN);
        Double densityMin = 0.0;
        if (dataMin != null) {
            densityMin = new BigDecimal(densitySet).multiply(new BigDecimal(1).divide(new BigDecimal(dataMin.getValue()))).doubleValue();
        }

        if (density > densityMax) {
            // 高密度
            CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                    densityControlStatus.getSystemThingCode1(), null, DENSITY_FLU2,
                    HIGH_MODE, new Date()), "sendHighDensityMode");
            if (cmdSendResponseData.getOkCount() <= 0) {
                logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
            }
        } else if (density < densityMin) {
            // 低密度
            CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                    densityControlStatus.getSystemThingCode1(), null, DENSITY_FLU2,
                    LOW_MODE, new Date()), "sendLowDensityMode");
            if (cmdSendResponseData.getOkCount() <= 0) {
                logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
            }
        } else {
            // 正常密度
            CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                    densityControlStatus.getSystemThingCode1(), null, DENSITY_FLU2,
                    NORMAL_MODE, new Date()), "sendNormalDensityMode");
            if (cmdSendResponseData.getOkCount() <= 0) {
                logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
            }
        }
    }

    /**
     * 发送指令:正常液位模式
     */
    private void sendNormalLevelMode() {
        CmdControlService.CmdSendResponseData cmdSendResponseData = cmdControlService.sendCmd(new DataModel(null,
                TERM_TWO_THING_CODE, null, LEVEL_MODE,
                NORMAL_MODE, new Date()), "sendNormalLevelMode");
        if (cmdSendResponseData.getOkCount() <= 0) {
            logger.error(CMD_FAILED_LOG + cmdSendResponseData.getErrorMessage(), SysException.EC_CMD_FAILED);
        }
    }

    public void checkSystemRunState() {
        // 判断是否为智能状态
        DataModelWrapper data = dataService.getData(TERM_TWO_THING_CODE, RUN_STATE).orElse(null);
        if (data == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("无智能密控信号!");
            }
            return;
        } else if (!RUN_STATE_1.equals(data.getValue())) {
            if (logger.isDebugEnabled()) {
                logger.debug("智能密控不在运行状态!");
            }
            return;
        }

        // 判断系统状态
        DataModelWrapper dataA = dataService.getData(TERM_TWO_SYSTEM_A_THING_CODE, RUN_STATE).orElse(null);
        DataModelWrapper dataB = dataService.getData(TERM_TWO_SYSTEM_B_THING_CODE, RUN_STATE).orElse(null);
        if (dataA == null && dataB == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("无智能密控系统设备信号!");
            }
            return;
        } else if (dataA != null && dataB == null) {
            if (RUN_STATE_1.equals(dataA.getValue())) {
                // A系统在运行,检查主控状态
                DataModelWrapper dataAFL = dataService.getData(TERM_TWO_SYSTEM_A_FL_THING_CODE, CONTROL_MODE).orElse(null);
                DataModelWrapper dataAMD = dataService.getData(TERM_TWO_SYSTEM_A_THING_CODE, DENSITY_CONTROL_MODE).orElse(null);

            }
        }

    }

}
