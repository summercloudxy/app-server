package com.zgiot.app.server.module.bellows.compressor;

import com.alibaba.fastjson.annotation.JSONField;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
import com.zgiot.app.server.module.bellows.pressure.PressureManager;
import com.zgiot.app.server.module.bellows.util.BellowsUtil;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.CompressorMetricConstants;
import com.zgiot.common.exceptions.SysException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wangwei
 */
public class CompressorGroup {

    private static final Logger logger = LoggerFactory.getLogger(CompressorGroup.class);

    private Object pressureLock = new Object();



    /**
     * 智能压力控制等待时间
     */
    private static final int PRESSURE_WAIT_TIME = 60000;


    /**
     * 空压机列表
     */
    private final List<Compressor> compressors;


    /**
     * 类型
     */
    @JSONField(serialize = false)
    private final String type;

    private final PressureManager pressureManager;

    private final DataService dataService;

    private final BellowsMapper bellowsMapper;

    /**
     * 管道压力
     */
    private volatile double pressure;

    /**
     * 运行中数量
     */
    private volatile int runningCount;

    /**
     * 总数量
     */
    private volatile int totalCount;

    /**
     * 故障数量
     */
    private volatile int errorCount;

    /**
     * 智能状态（低压空压机有效）
     */
    private volatile Boolean intelligent;

    /**
     * 空压机压力状态(低压空压机有效）
     */
    private volatile double pressureState = BellowsConstants.PRESSURE_NORMAL;


    /**
     * 智能压力控制timer（低压空压机有效）
     */
    private volatile Timer pressureTimer;

    /**
     * 错误列表
     */
    private volatile List<String> errors;

    public CompressorGroup(List<Compressor> compressors, String type, PressureManager pressureManager, DataService dataService, BellowsMapper bellowsMapper) {
        this.compressors = compressors;
        this.type = type;
        this.pressureManager = pressureManager;
        this.dataService = dataService;
        this.bellowsMapper = bellowsMapper;
    }

    /**
     * 初始化（低压空压机）
     * @return
     */
    public CompressorGroup init() {
        if (BellowsConstants.CP_TYPE_HIGH.equals(type)) {
            return this;
        }

        //初始化低压空压机压力状态和智能状态
        String requestId = RequestIdUtil.generateRequestId();
        setPressureState(Double.parseDouble(BellowsUtil.getDataModelValue(dataService, BellowsConstants.PRESSURE_THING_CODE, CompressorMetricConstants.PRESSURE_STATE).orElse(BellowsConstants.PRESSURE_NORMAL + "")), requestId);

        boolean intelligent = bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT) == BellowsConstants.YES;

        setIntelligent(intelligent, requestId);

        return this;
    }


    /**
     * 智能化设置
     * @param intelligent
     */
    public void setIntelligent(boolean intelligent, String requestId) {
        if (BellowsConstants.CP_TYPE_HIGH.equals(type)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Only low compressor can be set intelligent. RequestId: {}.", requestId);
            }
            return;
        }

        Boolean old = this.intelligent;
        this.intelligent = intelligent;
        if (this.intelligent.equals(old)) {
            logger.info("Low compressor intelligent is already {}. RequestId: {}", intelligent, requestId);
            return;
        }

        if (intelligent) {
            bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT, (long)BellowsConstants.YES);
        } else {
            bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT, (long)BellowsConstants.NO);
        }

        logger.info("Low compressor intelligent has bean set {}.RequestId: {}", intelligent, requestId);

        if (this.intelligent) {
            //开启低压空压机的卸载关闭定时
            turnOnStopTimers();

            //压力检测判断
            onPressureStateChange();
        } else {
            //关闭低压空压机的卸载关闭定时
            turnOffStopTimers();

            //关闭压力检测计时器
            turnOffPressureTimer();
        }
    }


    /**
     * 开启低压空压机的卸载关闭定时
     */
    private void turnOnStopTimers() {
        compressors.forEach(compressor -> {
            compressor.turnOnStopTimer();
        });
    }


    /**
     * 关闭低压空压机的卸载关闭定时
     */
    private void turnOffStopTimers() {
        if (logger.isDebugEnabled()) {
            logger.debug("Low press compressor intelligent is false, cannot turn on stop timers.");
        }
        compressors.forEach(compressor -> {
            compressor.turnOffStopTimer();
        });
    }

    /**
     * 运行状态变化
     * @param compressor
     * @param value
     * @param requestId
     */
    public void onRunStateChange(Compressor compressor, String value, String requestId) {
        boolean runState = Boolean.parseBoolean(value);
        compressor.onRunStateChange(runState, requestId, intelligent);
    }

    /**
     * 加载状态变化
     * @param compressor
     * @param value
     * @param requestId
     */
    public void onLoadStateChange(Compressor compressor, String value, String requestId) {
        boolean loadState = Boolean.parseBoolean(value);
        compressor.onLoadStateChange(loadState, requestId, intelligent);
    }

    /**
     * 错误状态变化
     * @param compressor
     * @param value
     * @param requestId
     */
    public void onErrorStateChange(Compressor compressor, String value, String requestId) {
        boolean errorState = Boolean.parseBoolean(value);
        compressor.onErrorStateChange(errorState, requestId, intelligent);
    }

    /**
     * 远程/就地状态变化
     * @param compressor
     * @param value
     * @param requestId
     */
    public void onLocalChange(Compressor compressor, String value, String requestId) {
        boolean local = Boolean.parseBoolean(value);
        compressor.onLocalChange(local, requestId, intelligent);

        if (!local && BellowsConstants.CP_TYPE_LOW.equals(type)) {
            //低压空压机开启远程状态，检测是否需要智能开/关
            onPressureStateChange();
        }
    }


    /**
     * 数据刷新
     * @param dataService
     * @return
     */
    public synchronized CompressorGroup refresh(DataService dataService, String requestId) {
        //数量刷新
        totalCount = compressors.size();
        errors = new ArrayList<>();
        int runningCount = 0;
        int errorCount = 0;

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor group {} refresh. RequestId: {}.", type, requestId);
        }

        for (Compressor compressor : compressors) {
            compressor.refresh(dataService);

            if (EnumCompressorState.RUNNING.getState().equals(compressor.getState())) {
                runningCount++;
            } else if (EnumCompressorState.ERROR.getState().equals(compressor.getState())) {
                errorCount++;
                //错误信息获取
                for (String error : compressor.getErrors()) {
                    errors.add(compressor.getThingCode() + error);
                }
            }
        }

        this.runningCount = runningCount;
        this.errorCount = errorCount;

        //管道压力刷新
        pressure = refreshPressure(dataService, requestId);

        return this;
    }

    /**
     * 管道压力刷新
     * @param dataService
     * @param requestId
     * @return
     */
    public double refreshPressure(DataService dataService, String requestId) {
        //压力刷新
        double pressure = pressureManager.refreshPressure(type, dataService, requestId);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor group {} refresh pressure {}. RequestId: {}.", type, pressure, requestId);
        }

        return pressure;
    }



    public void setPressureState(double pressureState, String requestId) {
        double oldState = this.pressureState;
        this.pressureState = pressureState;

        if (logger.isDebugEnabled()) {
            logger.debug("Pressure state is now {}.RequestId: {}.", pressureState, requestId);
        }

        if (pressureState != oldState) {
            //如果有压力计时器，则关闭
            turnOffPressureTimer();

            onPressureStateChange();
        }
    }


    /**
     * 低压空压机压力状态变化
     */
    public void onPressureStateChange() {
        if (!Boolean.TRUE.equals(intelligent)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Low pressure compressor intelligent is false, cannot handle pressure state change.");
            }
            return;
        }

        if (BellowsConstants.PRESSURE_NORMAL == pressureState) {
            //压力正常
            if (logger.isDebugEnabled()) {
                logger.debug("Pressure state is changed to normal.");
            }
        } else if (BellowsConstants.PRESSURE_HIGH == pressureState) {
            //压力过高
            if (logger.isDebugEnabled()) {
                logger.debug("Pressure state is changed to high.");
            }
            onPressureHigh();
        } else if (BellowsConstants.PRESSURE_LOW == pressureState) {
            //压力过低
            if (logger.isDebugEnabled()) {
                logger.debug("Pressure state is changed to low.");
            }
            onPressureLow();
        } else {
            logger.warn("Pressure state is wrong.Now pressure state is {}.", pressureState);
        }
    }


    /**
     * 关闭压力监测计时器
     */
    private void turnOffPressureTimer() {
        synchronized (pressureLock) {
            if (pressureTimer != null) {
                pressureTimer.cancel();
                pressureTimer = null;

                if (logger.isDebugEnabled()) {
                    logger.debug("Clear pressure timer.");
                }
            }
        }
    }

    /**
     * 压力过高
     */
    private void onPressureHigh() {
        //添加压力检测计时器
        synchronized (pressureLock) {
            if (pressureTimer != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Pressure timer already exists.");
                }
                return;
            }

            pressureTimer = new Timer();
            pressureTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("In pressure high timer");
                    }
                    turnOffPressureTimer();

                    onPressureHigh();
                }
            }, PRESSURE_WAIT_TIME);
        }

        //查找可关闭空压机
        Compressor compressor = chooseCompressorToTurnOff();
        if (compressor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No compressor can be unloaded");
            }
            return;
        }

        logger.info("Compressor {} will be unloaded because of high pressure.", compressor.getThingCode());
        try {
            compressor.operate(EnumCompressorOperation.UNLOAD, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
        } catch (SysException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * 压力过低
     */
    private void onPressureLow() {
        //添加压力检测计时器
        synchronized (pressureLock) {
            if (pressureTimer != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Pressure timer already exists.");
                }
                return;
            }

            pressureTimer = new Timer();
            pressureTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("In pressure low timer");
                    }
                    turnOffPressureTimer();

                    onPressureLow();
                }
            }, PRESSURE_WAIT_TIME);
        }

        //查找可开启空压机
        Compressor compressor = chooseCompressorToTurnOn();
        if (compressor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No compressor can be run");
            }
            return;
        }
        logger.info("Compressor {} will be running because of low pressure.", compressor.getThingCode());
        if (EnumCompressorState.STOPPED.getState().equals(compressor.getState())) {
            try {
                compressor.operate(EnumCompressorOperation.START, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
            } catch (SysException e) {
                logger.warn(e.getMessage());
            }
        } else if (EnumCompressorState.UNLOAD.getState().equals(compressor.getState())) {
            try {
                compressor.operate(EnumCompressorOperation.LOAD, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
            } catch (SysException e) {
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * 选择需要关闭的低压空压机
     * @return  null表示没有可关闭的
     */
    private Compressor chooseCompressorToTurnOff() {
        Compressor result = null;
        for (Compressor compressor : compressors) {
            if (result == null) {
                result = compressor;
            } else {
                result = compare(result, compressor, BellowsConstants.PRESSURE_HIGH);
            }
        }

        if (result.isLocal()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} is local.No compressor is chose.", result.getThingCode());
            }
            return null;
        }

        if (!EnumCompressorState.RUNNING.getState().equals(result.getState())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} state is {}.No compressor is chose.", result.getThingCode(), result.getState());
            }
            return null;
        }
        return result;
    }

    /**
     * 选择需要开启的低压空压机
     * @return  null表示没有可开启的
     */
    private Compressor chooseCompressorToTurnOn() {
        Compressor result = null;
        for (Compressor compressor : compressors) {
            if (result == null) {
                result = compressor;
            } else {
                result = compare(result, compressor, BellowsConstants.PRESSURE_LOW);
            }
        }

        if (result.isLocal()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} is local.No compressor is chose.", result.getThingCode());
            }
            return null;
        }

        if (EnumCompressorState.RUNNING.getState().equals(result.getState()) || EnumCompressorState.ERROR.getState().equals(result.getState())) {
            //选中的空压机是运行中或故障
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} state is {}.No compressor is chose.", result.getThingCode(), result.getState());
            }
            return null;
        }
        return result;
    }

    /**
     * 比较空压机
     * @param c1
     * @param c2
     * @param state 压力状态
     * @return 选中的空压机
     */
    private Compressor compare(Compressor c1, Compressor c2, double state) {
        if (c1.isLocal()) {
            return c2;
        }
        if (c2.isLocal()) {
            return c1;
        }


        //记录高压状态下是否选择第一个
        boolean highSelectFirst;

        int sort1 = EnumCompressorState.getByState(c1.getState()).getSort();
        int sort2 = EnumCompressorState.getByState(c2.getState()).getSort();

        if (sort1 > sort2) {
            highSelectFirst = true;
        } else if (sort2 > sort1) {
            highSelectFirst = false;
        } else if (c1.getLoadTime() > c2.getLoadTime()) {
            highSelectFirst = true;
        } else if (c1.getLoadTime() < c2.getLoadTime()) {
            highSelectFirst = false;
        } else if (c1.getRunTime() > c2.getRunTime()) {
            highSelectFirst = true;
        } else {
            highSelectFirst = false;
        }

        Compressor result;
        //异或操作，不同返回true
        if (highSelectFirst ^ (BellowsConstants.PRESSURE_HIGH == state)) {
            result = c2;
        } else {
            result = c1;
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Choose compressor {}, state {}.", result.getThingCode(), result.getState());
        }

        return result;
    }



    public List<Compressor> getCompressors() {
        return compressors;
    }

    public String getType() {
        return type;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(int runningCount) {
        this.runningCount = runningCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Boolean getIntelligent() {
        return intelligent;
    }

}
