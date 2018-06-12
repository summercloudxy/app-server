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
    public static AtomicBoolean isIControl = new AtomicBoolean(false);
    public static AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * 判断当前逻辑是否运行完
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning.get();
    }

    /**
     * 判断当前是否为智能控制状态
     *
     * @return
     */
    public boolean isIControl() {
        DataModelWrapper data = dataService.getData(TERM_TWO_SYSTEM, INTELLIGENT_CONTROL).orElse(null);
        if (data != null && Boolean.TRUE.toString().equals(data.getValue())) {
            isIControl.set(true);
        }
        return isIControl.get();
    }

    /**
     * 初始化模块缓存
     */
    public void initParamCache() {
        paramCache.init();
    }

    /**
     * 液位变化处理
     *
     * @param dataModel
     */
    public void handleLevel(DataModel dataModel) {
        isRunning.set(true);

        try {
            // 当前液位数据
            String thingCode = dataModel.getThingCode();
            Double value = Double.valueOf(dataModel.getValue());

            // 获取设定高/低液位
            String levelSetHigh = paramCache.getValue(thingCode, SETTED_HIGH_LEVEL).getValue();
            String levelSetLow = paramCache.getValue(thingCode, SETTED_LOW_LEVEL).getValue();

            // 记录密控系统状态
            DensityControlStatus densityControlStatus = new DensityControlStatus();
            densityControlStatus.setMainThingCode(thingCode);
            densityControlStatus.setThingCodeA(TERM_TWO_SYSTEM_A_THING_CODE);
            densityControlStatus.setThingCodeB(TERM_TWO_SYSTEM_B_THING_CODE);
            densityControlStatus.setFLThingCodeA(TERM_TWO_SYSTEM_A_FL_THING_CODE);
            densityControlStatus.setFLThingCodeB(TERM_TWO_SYSTEM_B_FL_THING_CODE);
            densityControlStatus.setLevel(value);

            // 获取当前系统运行状态
            getSystemStatus(densityControlStatus);
            if (densityControlStatus.getSystemStatus() == null || SYSTEM_STATUS_0.equals(densityControlStatus.getSystemStatus())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("当前智能密控无系统设备运行,结束此次调整!");
                }
                return;
            }

            // 获取当前主控密度计密度
            if (StringUtils.isBlank(densityControlStatus.getDensityThingCode())) {
                return;
            }
            DataModelWrapper data = dataService.getData(densityControlStatus.getDensityThingCode(), CURRENT_DENSITY).orElse(null);
            if (data != null) {
                densityControlStatus.setDensity(StringUtils.isBlank(data.getValue()) ? 0.0 : Double.parseDouble(data.getValue()));
            }

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
        } catch (Exception e) {
            if (logger.isTraceEnabled()) {
                logger.trace("智能密控出现异常", e);
            }
        } finally {
            isRunning.set(false);
        }

    }

    /**
     * 获取当前系统运行状态
     *
     * @return
     */
    private void getSystemStatus(DensityControlStatus dcStatus) {
        DataModelWrapper dataA = dataService.getData(dcStatus.getThingCodeA(), STATE).orElse(null);
        DataModelWrapper dataB = dataService.getData(dcStatus.getThingCodeB(), STATE).orElse(null);

        if (dataA == null && dataB == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("无智能密控系统设备信号!");
            }
            return;
        } else if (dataA != null && dataB == null) {
            if (RUN_STATE_2.equals(dataA.getValue())) {
                dcStatus.setSystemStatus(SYSTEM_STATUS_1);
                dcStatus.setSystemThingCode1(dataA.getThingCode());

                // A系统在运行,检查主控状态
                checkSingleSystemControl(dcStatus, dcStatus.getFLThingCodeA(), CONTROL_MODE, dcStatus.getThingCodeA(),
                        DENSITY_CONTROL_MODE, dcStatus.getFLThingCodeB(), dcStatus.getThingCodeB());
            }
        } else if (dataB != null && dataA == null) {
            if (RUN_STATE_2.equals(dataB.getValue())) {
                dcStatus.setSystemStatus(SYSTEM_STATUS_1);
                dcStatus.setSystemThingCode1(dataB.getThingCode());

                // B系统在运行,检查主控状态
                checkSingleSystemControl(dcStatus, dcStatus.getFLThingCodeB(), CONTROL_MODE, dcStatus.getThingCodeB(),
                        DENSITY_CONTROL_MODE, dcStatus.getFLThingCodeA(), dcStatus.getThingCodeA());
            }
        } else {
            // 两个系统均有信号
            if (RUN_STATE_2.equals(dataA.getValue()) && !RUN_STATE_2.equals(dataB.getValue())) {
                // A运行,切为A主控
                dcStatus.setSystemStatus(SYSTEM_STATUS_1);
                dcStatus.setSystemThingCode1(dataA.getThingCode());

                checkSingleSystemControl(dcStatus, dcStatus.getFLThingCodeA(), CONTROL_MODE, dcStatus.getThingCodeA(),
                        DENSITY_CONTROL_MODE, dcStatus.getFLThingCodeB(), dcStatus.getThingCodeB());
            } else if (RUN_STATE_2.equals(dataB.getValue()) && !RUN_STATE_2.equals(dataA.getValue())) {
                // B运行,切为B主控
                dcStatus.setSystemStatus(SYSTEM_STATUS_1);
                dcStatus.setSystemThingCode1(dataB.getThingCode());

                checkSingleSystemControl(dcStatus, dcStatus.getFLThingCodeB(), CONTROL_MODE, dcStatus.getThingCodeB(),
                        DENSITY_CONTROL_MODE, dcStatus.getFLThingCodeA(), dcStatus.getThingCodeA());
            } else if (RUN_STATE_2.equals(dataA.getValue()) && RUN_STATE_2.equals(dataB.getValue())) {
                // 都在运行
                dcStatus.setSystemStatus(SYSTEM_STATUS_2);
                dcStatus.setSystemThingCode1(dataA.getThingCode());
                dcStatus.setSystemThingCode2(dataB.getThingCode());

                // 检查双系统主控状态
                checkDoubleSystemControl(dcStatus, dataA, dataB);
            } else {
                // 都不在运行
                dcStatus.setSystemStatus(SYSTEM_STATUS_0);
            }
        }
    }

    /**
     * 检查单系统主控状态
     *
     * @param densityControlStatus
     * @param thingCodeFL
     * @param metricCodeFL
     * @param thingCodeMD
     * @param metricCodeMD
     * @param thingCodeFLOther
     * @param thingCodeMDOther
     */
    private void checkSingleSystemControl(DensityControlStatus densityControlStatus, String thingCodeFL, String metricCodeFL,
                                          String thingCodeMD, String metricCodeMD, String thingCodeFLOther, String thingCodeMDOther) {
        DataModelWrapper dataFL = dataService.getData(thingCodeFL, metricCodeFL).orElse(null);
        if (dataFL != null && Boolean.FALSE.toString().equals(dataFL.getValue())) {
            // 状态不符,切换分流阀为主控
            changeFL(dataFL.getThingCode(), thingCodeFLOther, dataFL.getMetricCode());
        }

        DataModelWrapper dataMD = dataService.getData(thingCodeMD, metricCodeMD).orElse(null);
        if (dataMD != null && Boolean.FALSE.toString().equals(dataMD.getValue())) {
            densityControlStatus.setDensityThingCode(dataMD.getThingCode());
            // 状态不符,切换密度计为主控
            changeMD(dataMD.getThingCode(), thingCodeMDOther, dataMD.getMetricCode());
        }
    }

    /**
     * 切换分流阀
     *
     * @param thingCodeFL
     * @param thingCodeFLOther
     * @param metricCode
     */
    private void changeFL(String thingCodeFL, String thingCodeFLOther, String metricCode) {
        CmdControlService.CmdSendResponseData data1 = cmdControlService.sendCmd(new DataModel(null,
                thingCodeFL, null, metricCode, Boolean.TRUE.toString(), new Date()), "sendFLControl");
        if (data1.getOkCount() <= 0) {
            logger.error(CMD_FAILED_LOG + data1.getErrorMessage(), SysException.EC_CMD_FAILED);
        }
        //  另一设备切位非主控
        CmdControlService.CmdSendResponseData data2 = cmdControlService.sendCmd(new DataModel(null,
                thingCodeFLOther, null, metricCode, Boolean.FALSE.toString(), new Date()), "sendFLNotControl");
        if (data2.getOkCount() <= 0) {
            logger.error(CMD_FAILED_LOG + data2.getErrorMessage(), SysException.EC_CMD_FAILED);
        }
    }

    /**
     * 切换密度阀
     *
     * @param thingCodeMD
     * @param thingCodeMDOther
     * @param metricCode
     */
    private void changeMD(String thingCodeMD, String thingCodeMDOther, String metricCode) {
        CmdControlService.CmdSendResponseData data1 = cmdControlService.sendCmd(new DataModel(null,
                thingCodeMD, null, metricCode, Boolean.TRUE.toString(), new Date()), "sendMDControl");
        if (data1.getOkCount() <= 0) {
            logger.error(CMD_FAILED_LOG + data1.getErrorMessage(), SysException.EC_CMD_FAILED);
        }
        // 另一设备切位非主控
        CmdControlService.CmdSendResponseData data2 = cmdControlService.sendCmd(new DataModel(null,
                thingCodeMDOther, null, metricCode, Boolean.FALSE.toString(), new Date()), "sendMDNotControl");
        if (data2.getOkCount() <= 0) {
            logger.error(CMD_FAILED_LOG + data2.getErrorMessage(), SysException.EC_CMD_FAILED);
        }
    }

    /**
     * 检查双系统主控状态
     *
     * @param dataA
     * @param dataB
     */
    private void checkDoubleSystemControl(DensityControlStatus scStatus, DataModelWrapper dataA, DataModelWrapper dataB) {
        DataModelWrapper dataFLA = dataService.getData(scStatus.getFLThingCodeA(), CONTROL_MODE).orElse(null);
        DataModelWrapper dataFLB = dataService.getData(scStatus.getFLThingCodeB(), CONTROL_MODE).orElse(null);
        if (dataFLA == null && dataFLB == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("两个分流阀均无信号!");
            }
        } else if (dataFLA != null && dataFLB == null) {
            if (Boolean.FALSE.toString().equals(dataFLA.getValue())) {
                changeFL(dataFLA.getThingCode(), scStatus.getFLThingCodeB(), dataFLA.getMetricCode());
            }
        } else if (dataFLA == null && dataFLB != null) {
            if (Boolean.FALSE.toString().equals(dataFLB.getValue())) {
                changeFL(dataFLB.getThingCode(), scStatus.getFLThingCodeA(), dataFLB.getMetricCode());
            }
        } else {
            if ((Boolean.TRUE.toString().equals(dataFLA.getValue()) && Boolean.TRUE.toString().equals(dataFLB.getValue()))
                    || (Boolean.FALSE.toString().equals(dataFLA.getValue()) && Boolean.FALSE.toString().equals(dataFLB.getValue()))) {
                // 两个均为主控或均为非主控状态,设置A为主控,B为非主控
                changeFL(dataFLA.getThingCode(), dataFLB.getThingCode(), dataFLA.getMetricCode());
            }
        }

        DataModelWrapper dataMDA = dataService.getData(dataA.getThingCode(), DENSITY_CONTROL_MODE).orElse(null);
        DataModelWrapper dataMDB = dataService.getData(dataB.getThingCode(), DENSITY_CONTROL_MODE).orElse(null);
        if (dataMDA == null && dataMDB == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("两个密度计均无信号!");
            }
        } else if (dataMDA != null && dataMDB == null) {
            scStatus.setDensityThingCode(dataMDA.getThingCode());
            if (Boolean.FALSE.toString().equals(dataMDA.getValue())) {
                changeMD(dataMDA.getThingCode(), dataB.getThingCode(), dataMDA.getMetricCode());
            }
        } else if (dataMDA == null && dataMDB != null) {
            scStatus.setDensityThingCode(dataMDB.getThingCode());
            if (Boolean.FALSE.toString().equals(dataMDB.getValue())) {
                changeMD(dataMDB.getThingCode(), dataA.getThingCode(), dataMDB.getMetricCode());
            }
        } else {
            scStatus.setDensityThingCode(dataMDA.getThingCode());
            if ((Boolean.TRUE.toString().equals(dataMDA.getValue()) && Boolean.TRUE.toString().equals(dataMDB.getValue()))
                    || (Boolean.FALSE.toString().equals(dataMDA.getValue()) && Boolean.FALSE.toString().equals(dataMDB.getValue()))) {
                // 两个均为主控或均为非主控状态,设置A为主控,B为非主控
                changeMD(dataMDA.getThingCode(), dataMDB.getThingCode(), dataMDA.getMetricCode());
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

}
