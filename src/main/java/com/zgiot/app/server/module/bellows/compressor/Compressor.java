package com.zgiot.app.server.module.bellows.compressor;

import com.alibaba.fastjson.annotation.JSONField;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
import com.zgiot.app.server.module.bellows.enumeration.EnumHighCompressorFault;
import com.zgiot.app.server.module.bellows.enumeration.EnumLowCompressorFault;
import com.zgiot.app.server.module.bellows.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.pojo.CompressorState;
import com.zgiot.app.server.module.bellows.pressure.PressureManager;
import com.zgiot.app.server.module.bellows.util.BellowsUtil;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.CompressorMetricConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangwei
 */
public class Compressor {

    private static final Logger logger = LoggerFactory.getLogger(Compressor.class);

    /**
     * 脉冲指令清除时间
     */
    private static final int CLEAN_PERIOD = 500;

    /**
     * 加载状态判断等待时间
     */
    private static final int STATE_WAIT_TIME = 8000;

    /**
     * 日志确认等待时间
     */
    private static final int LOG_WAIT_TIME = 8000;

    /**
     * 卸载状态停止等待时间
     */
    private static final int STOP_WAIT_TIME = 30 * 60 * 1000;

    /**
     * 加载状态timer
     */
    private volatile Timer stateTimer;

    /**
     * 卸载变停止timer
     */
    private volatile Timer stopTimer;

    /**
     * 日志确认timer
     */
    private final Timer logTimer = new Timer();

    private Object logLock = new Object();
    private Object stateLock = new Object();
    private Object stopLock = new Object();


    private final DataService dataService;

    private final PressureManager pressureManager;

    private final CmdControlService cmdControlService;

    private final BellowsMapper bellowsMapper;


    /**
     * 名称
     */
    private final String name;

    /**
     * 设备号
     */
    private final String thingCode;

    /**
     * 类型
     */
    private final String type;

    /**
     * 排序
     */
    @JSONField(serialize = false)
    private final int sort;



    /**
     * 是否就地，0集控，1就地
     */
    private volatile boolean local;

    /**
     * 加载状态
     */
    @JSONField(serialize = false)
    private volatile boolean loadState;

    /**
     * 运行状态
     */
    @JSONField(serialize = false)
    private volatile boolean runState;

    /**
     * 故障状态
     */
    @JSONField(serialize = false)
    private volatile boolean errorState;

    /**
     * 排气压力
     */
    private volatile double pressure;

    /**
     * 排气温度
     */
    private volatile int temperature;

    /**
     * 当前电流
     */
    private volatile int current;

    /**
     * 油气桶温度
     */
    private volatile int oilTemperature;

    /**
     * 油气桶压力
     */
    private volatile double oilPressure;

    /**
     * 运行时间
     */
    private volatile int runTime;

    /**
     * 加载时间
     */
    private volatile int loadTime;

    /**
     * 状态（根据runState和loadState判断）
     */
    private volatile String state;

    /**
     * 错误信息
     */
    private volatile List<String> errors;

    /**
     * 日志等待timer
     */
    private Map<Long, LogTimerTask> logTimerMap = new ConcurrentHashMap<>();



    public Compressor(String name, String thingCode, String type, int sort, DataService dataService, PressureManager pressureManager, CmdControlService cmdControlService, BellowsMapper bellowsMapper) {
        this.name = name;
        this.thingCode = thingCode;
        this.type = type;
        this.sort = sort;
        this.dataService = dataService;
        this.pressureManager = pressureManager;
        this.cmdControlService = cmdControlService;
        this.bellowsMapper = bellowsMapper;
    }

    /**
     * 状态初始化
     * @return
     */
    public Compressor initState() {
        //故障状态
        Optional<String> warnData = BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.WARN);
        boolean warn = Boolean.parseBoolean(warnData.orElse(BellowsConstants.FALSE));

        Optional<String> errorData = BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.ERROR);
        boolean error = Boolean.parseBoolean(errorData.orElse(BellowsConstants.FALSE));

        if (warn || error) {
            errorState = true;
        } else {
            errorState = false;
        }


        //运行状态
        Optional<String> runData = BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.RUN_STATE);
        runState = Boolean.parseBoolean(runData.orElse(BellowsConstants.FALSE));


        //加载状态
        Optional<String> loadData = BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.LOAD_STATE);
        loadState = Boolean.parseBoolean(loadData.orElse(BellowsConstants.FALSE));


        //远程/就地状态
        Optional<String> localData = BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.LOCAL);
        local = Boolean.parseBoolean(localData.orElse(BellowsConstants.TRUE));

        confirmState(false);

        return this;
    }

    /**
     * 数据刷新
     * @param dataService   数据源
     * @return
     */
    public synchronized Compressor refresh(DataService dataService) {
        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} refresh.", thingCode);
        }
        errors = new ArrayList<>();

        //组装属性（远程状态、故障状态、启动状态和加载状态使用监听者，不在这里组装）
        this.setCurrent(Integer.parseInt(BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.CURRENT).orElse("0")));
        this.setPressure(Double.parseDouble(BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.CURRENT).orElse("0"))/100);
        this.setTemperature(Integer.parseInt(BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.TEMPERATURE).orElse("0")));
        this.setRunTime(Integer.parseInt(BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.RUN_TIME).orElse("0")));
        this.setLoadTime(Integer.parseInt(BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.LOAD_TIME).orElse("0")));
        this.setOilPressure(Double.parseDouble(BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.OIL_PRESSURE).orElse("0"))/100);
        this.setOilTemperature(Integer.parseInt(BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.OIL_TEMPERATURE).orElse("0")));

        //错误信息组装
        if (EnumCompressorState.ERROR.getState().equals(state)) {
            if (BellowsConstants.CP_TYPE_HIGH.equals(type)) {
                for (EnumHighCompressorFault fault : EnumHighCompressorFault.values()) {
                    boolean value = Boolean.parseBoolean(BellowsUtil.getDataModelValue(dataService, thingCode, fault.getMetricCode()).orElse(BellowsConstants.FALSE));
                    if (value) {
                        errors.add(fault.getInfo());
                    }
                }
            } else {
                for (EnumLowCompressorFault fault : EnumLowCompressorFault.values()) {
                    boolean value = Boolean.parseBoolean(BellowsUtil.getDataModelValue(dataService, thingCode, fault.getMetricCode()).orElse(BellowsConstants.FALSE));
                    if (value) {
                        errors.add(fault.getInfo());
                    }
                }
            }
        }

        return this;
    }


    /**
     * 添加日志timer
     * @param logId
     * @param requestId
     */
    public void addLogWaitTimer(Long logId, String requestId) {
        synchronized (logLock) {
            if (logger.isTraceEnabled()) {
                logger.trace("Compressor {} add logWaitTimer.Log id: {}.RequestId: {}.", thingCode, logId, requestId);
            }
            LogTimerTask timerTask = new LogTimerTask(logId, requestId);
            logTimer.schedule(timerTask, LOG_WAIT_TIME);
            logTimerMap.put(logId, timerTask);
        }
    }

    /**
     * 清除日志timer
     * @param id
     */
    public void removeLogWaitTimer(Long id) {
        synchronized (logLock) {
            LogTimerTask timerTask = logTimerMap.get(id);
            if (timerTask != null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Compressor {} clear logWaitTimer.", thingCode);
                }

                timerTask.cancel();
                timerTask = null;
                logTimerMap.remove(id);
            }
        }
    }

    /**
     * 运行状态变化
     * @param value
     */
    public void onRunStateChange(boolean value, String requestId, Boolean intelligent) {
        runState = value;

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get run state signal: {}. RequestId: {}.", thingCode, runState, requestId);
        }

        confirmState(intelligent);
    }

    /**
     * 加载状态变化
     * @param value
     */
    public void onLoadStateChange(boolean value, String requestId, Boolean intelligent) {
        loadState = value;

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get load state signal: {}.RequestId: {}", thingCode, runState, requestId);
        }

        if (!errorState && runState && loadState) {
            //收到加载信号，启动定时器，判断是否是保护模式
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} check protect mode.RequestId: {}.", thingCode, requestId);
            }

            synchronized (stateLock) {
                if (stateTimer != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor: {} state timer exist.RequestId: {}.", thingCode, requestId);
                    }
                    return;
                }
                stateTimer = new Timer();
                stateTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (logger.isTraceEnabled()) {
                            logger.trace("Compressor: {} in state timer.", thingCode);
                        }
                        confirmState(intelligent);
                    }
                }, STATE_WAIT_TIME);
            }
        } else {
            confirmState(intelligent);
        }
    }

    /**
     * 故障状态变化
     * @param value
     * @param requestId
     */
    public void onErrorStateChange(boolean value, String requestId, Boolean intelligent) {
        errorState = value;

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get error state signal: {}.RequestId: {}", thingCode, runState, requestId);
        }

        confirmState(intelligent);
    }

    /**
     * 等待状态确认
     */
    public void waitForStateConfirm() {
        synchronized (stateLock) {
            if (stateTimer != null) {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} is waiting for state confirm.", thingCode);
                    }
                    stateLock.wait();
                } catch (InterruptedException e) {
                    logger.warn("Compressor {} state lock is interrupted.", thingCode);
                }
            }
        }
    }

    /**
     * 远程、就地状态变化
     * @param value
     */
    public void onLocalChange(boolean value, String requestId, Boolean intelligent) {
        local = value;

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get local signal: {}.RequestId: {}.", thingCode, local, requestId);
        }

        if (local) {
            turnOffStopTimer();
        } else if (Boolean.TRUE.equals(intelligent)) {
            turnOnStopTimer();
        }
    }

    /**
     * 开启stopTimer
     */
    public void turnOnStopTimer() {
        if (BellowsConstants.CP_TYPE_HIGH.equals(type)) {
            if (logger.isDebugEnabled()) {
                logger.debug("High press compressor {} cannot turn on stop timer.", thingCode);
            }
            return;
        }

        if (!EnumCompressorState.UNLOAD.getState().equals(state)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} is not unload, cannot turn on stopTimer.", thingCode);
            }
            return;
        }

        if (local) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} is local, cannot turn on stopTimer.", thingCode);
            }
            return;
        }

        synchronized (stopLock) {
            if (stopTimer != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Compressor {} stopTimer exists.", thingCode);
                }
                return;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} turn on stopTimer.", thingCode);
            }

            stopTimer = new Timer();
            stopTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Compressor {} in stopTimer.", thingCode);
                    }
                    waitForStateConfirm();
                    if (EnumCompressorState.UNLOAD.getState().equals(state)) {
                        logger.info("Compressor {} unload overtime, starting stop.", thingCode);
                        try {
                            operate(EnumCompressorOperation.STOP, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
                        } catch (SysException e) {
                            logger.warn(e.getMessage());
                        }
                    }
                    turnOffStopTimer();
                }
            }, STOP_WAIT_TIME);
        }
    }

    /**
     * 关闭stopTimer
     */
    public void turnOffStopTimer() {
        synchronized (stopLock) {
            if (stopTimer != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Compressor: {} clear stopTimer.", thingCode);
                }
                stopTimer.cancel();
                stopTimer = null;
            }
        }
    }


    /**
     * 确认空压机状态
     */
    private synchronized void confirmState(Boolean intelligent) {
        String oldState = state;

        if (errorState) {
            state = EnumCompressorState.ERROR.getState();
        } else if (runState) {
            if (loadState) {
                state = EnumCompressorState.RUNNING.getState();
            } else {
                state = EnumCompressorState.UNLOAD.getState();
            }
        } else {
            state = EnumCompressorState.STOPPED.getState();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} state now is: {}.", thingCode, state);
        }


        if (!state.equals(oldState)) {
            onStateChange(state, oldState, intelligent);
        }


        //清除stateTimer
        removeStateTimer();

        //日志确认更新
        updateLogs();
    }

    /**
     * 状态变化
     */
    private void onStateChange(String postState, String preState, Boolean intelligent) {
        //保存状态日志
        saveCompressorState(postState, preState);

        if (EnumCompressorState.UNLOAD.getState().equals(state) && BellowsConstants.CP_TYPE_LOW.equals(type)) {
            //卸载状态下开启stopTimer
            if (!Boolean.TRUE.equals(intelligent)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Low press intelligent is {},  cannot turn on stop timer.", intelligent);
                }
                return;
            }
            turnOnStopTimer();
        } else {
            //其他状态下关闭stopTimer
            turnOffStopTimer();
        }
    }

    /**
     * 清除stateTimer
     */
    private void removeStateTimer() {
        synchronized (stateLock) {
            if (stateTimer != null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Compressor {} clear state lock.", thingCode);
                }

                stateTimer.cancel();
                stateTimer = null;
            }
            stateLock.notifyAll();
        }
    }

    /**
     * 日志确认更新
     */
    private void updateLogs() {
        synchronized (logLock) {
            if (!logTimerMap.isEmpty()) {
                logTimerMap.forEach((logId, logTimerTask) ->{
                    updateLog(logId, logTimerTask.requestId);
                });
            }
        }
    }


    /**
     * 操作空压机
     * @param operation 操作
     * @param operationType 手动/自动
     * @param requestId
     */
    public int operate(EnumCompressorOperation operation, String operationType, String requestId) {
        if (local) {
            logger.warn("Compressor: {} is local, cannot be operated remotely.RequestId: {}", thingCode, requestId);
            String error = "空压机" + thingCode + "处于就地模式，无法进行远程操作";
            saveFullLog(operation, operationType, requestId, error);
            throw new SysException(error, SysException.EC_UNKNOWN);
        }

        //组装发送信号
        DataModel signal = new DataModel();
        signal.setThingCode(thingCode);
        signal.setMetricCode(CompressorMetricConstants.CONTROL);
        signal.setValue(operation.getValue());

        boolean holding = operation.isHolding();

        if (logger.isDebugEnabled()) {
            logger.debug("RequestId: {} send command: {} to compressor: {}.Operation type is {}", requestId, operation, thingCode, operationType);
        }

        int count = 0;
        try {
            count = cmdControlService.sendPulseCmdBoolByShort(signal, null, null, requestId, operation.getPosition(), CLEAN_PERIOD, holding);
        } catch (SysException e) {
            logger.warn(e.getMessage());
            saveFullLog(operation, operationType, requestId, e.getMessage());
            throw e;
        }

        //日志记录
        saveLog(operation, operationType, requestId);

        return count;
    }


    /**
     * 保存空压机状态
     * @param postState 修改后状态
     * @param preState  修改前状态
     */
    public void saveCompressorState(String postState, String preState) {
        CompressorState state = new CompressorState();
        state.setPostState(postState);
        if (preState == null) {
            //没有修改前状态，从数据库里查询
            List<CompressorState> list = bellowsMapper.getCompressorState(null, new Date(), Arrays.asList(thingCode),0, 1);
            if (list == null || list.isEmpty()) {
                preState = postState;
            } else if (list.get(0).getPostState().equals(postState)){
                if (logger.isDebugEnabled()) {
                    logger.debug("Compressor {} state {} is not changed.", thingCode, postState);
                }
                return;
            } else {
                preState = list.get(0).getPostState();
            }
        }
        state.setPreState(preState);
        state.setThingCode(thingCode);
        state.setTime(new Date());

        bellowsMapper.saveCompressorState(state);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} save postState {}, preState {}, id {}.", thingCode, postState, preState, state.getId());
        }
    }


    /**
     * 保存日志，等待状态确认
     * @param operation
     * @param operationType
     * @param requestId
     */
    private void saveLog(EnumCompressorOperation operation, String operationType, String requestId) {
        CompressorLog compressorLog = new CompressorLog();
        compressorLog.setOperation(operation.toString());
        compressorLog.setOperateTime(new Date());
        compressorLog.setRequestId(requestId);
        compressorLog.setThingCode(thingCode);
        compressorLog.setPreState(state);
        compressorLog.setOperateType(operationType);
        bellowsMapper.saveCompressorLog(compressorLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} save operation log.RequestId: {}.LogId: {}.", thingCode, requestId, compressorLog.getId());
        }

        //添加日志确认timer
        addLogWaitTimer(compressorLog.getId(), requestId);
    }


    /**
     * 保存完整日志
     * @param operation
     * @param operationType
     * @param requestId
     * @param memo
     */
    private void saveFullLog(EnumCompressorOperation operation, String operationType, String requestId, String memo) {
        //数据刷新
        refresh(dataService);

        CompressorLog compressorLog = new CompressorLog();
        compressorLog.setOperation(operation.toString());
        compressorLog.setOperateTime(new Date());
        compressorLog.setRequestId(requestId);
        compressorLog.setThingCode(thingCode);
        compressorLog.setPreState(state);
        compressorLog.setOperateType(operationType);
        compressorLog.setPostState(state);
        compressorLog.setConfirmTime(new Date());
        compressorLog.setPressure(pressure);
        compressorLog.setMemo(memo);


        bellowsMapper.saveCompressorLog(compressorLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} save full operation log.RequestId: {}.LogId: {}.", thingCode, requestId, compressorLog.getId());
        }
    }


    /**
     * 更新日志确认状态
     * @param logId
     * @param requestId
     */
    public void updateLog(Long logId, String requestId) {
        //等待状态确认
        waitForStateConfirm();

        refresh(dataService);
        CompressorLog compressorLog = new CompressorLog();
        compressorLog.setId(logId);
        compressorLog.setConfirmTime(new Date());
        compressorLog.setPostState(state);
        compressorLog.setPressure(pressure);

        bellowsMapper.updateCompressorLog(compressorLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} update log {}.RequestId: {}.", thingCode, logId, requestId);
        }

        //清除日志timerTask
        removeLogWaitTimer(logId);
    }


    public String getName() {
        return name;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getType() {
        return type;
    }

    public int getSort() {
        return sort;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public boolean isLoadState() {
        return loadState;
    }

    public void setLoadState(boolean loadState) {
        this.loadState = loadState;
    }

    public boolean isRunState() {
        return runState;
    }

    public void setRunState(boolean runState) {
        this.runState = runState;
    }

    public boolean isErrorState() {
        return errorState;
    }

    public void setErrorState(boolean errorState) {
        this.errorState = errorState;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getOilTemperature() {
        return oilTemperature;
    }

    public void setOilTemperature(int oilTemperature) {
        this.oilTemperature = oilTemperature;
    }

    public double getOilPressure() {
        return oilPressure;
    }

    public void setOilPressure(double oilPressure) {
        this.oilPressure = oilPressure;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(int loadTime) {
        this.loadTime = loadTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * 日志确认定时任务
     */
    private class LogTimerTask extends TimerTask {
        Long id;    //日志id
        String requestId;   //请求id

        public LogTimerTask(Long id, String requestId) {
            this.id = id;
            this.requestId = requestId;
        }

        @Override
        public void run() {
            updateLog(id, requestId);
        }
    }
}
