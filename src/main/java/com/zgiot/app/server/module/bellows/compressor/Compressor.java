package com.zgiot.app.server.module.bellows.compressor;

import com.alibaba.fastjson.annotation.JSONField;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
import com.zgiot.app.server.module.bellows.enumeration.EnumHighCompressorFault;
import com.zgiot.app.server.module.bellows.enumeration.EnumLowCompressorFault;
import com.zgiot.app.server.module.bellows.util.BellowsUtil;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.CompressorMetricConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangwei
 */
public class Compressor {

    private static final Logger logger = LoggerFactory.getLogger(Compressor.class);

    public static final String TYPE_LOW = "low";
    public static final String TYPE_HIGH = "high";

    public static final int YES = 1;
    public static final int NO = 0;

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


    @JSONField(serialize = false)
    private final CompressorManager manager;

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



    public Compressor(String name, String thingCode, String type, int sort, CompressorManager manager) {
        this.name = name;
        this.thingCode = thingCode;
        this.type = type;
        this.sort = sort;
        this.manager = manager;
    }

    /**
     * 状态初始化
     * @return
     */
    public Compressor initState(DataService dataService, CmdControlService cmdControlService) {
        //故障状态
        Optional<String> warnData = BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.WARN);
        boolean warn = Boolean.parseBoolean(warnData.orElse(BellowsConstants.FALSE));

        Optional<String> errorData = BellowsUtil.getDataModelValue(dataService, thingCode, CompressorMetricConstants.ERROR);
        boolean error = Boolean.parseBoolean(warnData.orElse(BellowsConstants.FALSE));

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

        confirmState();

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
            if (TYPE_HIGH.equals(type)) {
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
    public void onRunStateChange(String value, String requestId) {
        runState = Boolean.parseBoolean(value);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get run state signal: {}. RequestId: {}.", thingCode, runState, requestId);
        }

        confirmState();
    }

    /**
     * 加载状态变化
     * @param value
     */
    public void onLoadStateChange(String value, String requestId) {
        loadState = Boolean.parseBoolean(value);

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
                        confirmState();
                    }
                }, STATE_WAIT_TIME);
            }
        } else {
            confirmState();
        }
    }

    /**
     * 故障状态变化
     * @param value
     * @param requestId
     */
    public void onErrorStateChange(String value, String requestId) {
        errorState = Boolean.parseBoolean(value);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get error state signal: {}.RequestId: {}", thingCode, runState, requestId);
        }

        confirmState();
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
    public void onLocalChange(String value, String requestId, boolean intelligent) {
        local = Boolean.parseBoolean(value);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get local signal: {}.RequestId: {}.", thingCode, local, requestId);
        }

        if (local) {
            turnOffStopTimer();
        } else if (intelligent) {
            turnOnStopTimer();

            //检查压力变化
            if (type.equals(TYPE_LOW)) {
                manager.onPressureStateChange(manager.isIntelligent());
            }
        }
    }

    /**
     * 开启stopTimer
     */
    public void turnOnStopTimer() {

        if (TYPE_HIGH.equals(type)) {
            if (logger.isDebugEnabled()) {
                logger.debug("High press compressor {} cannot turn on stop timer.", thingCode);
            }
            return;
        }

        if (local) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} is local, cannot turn on stopTimer.", thingCode);
            }
            return;
        }

        if (!EnumCompressorState.UNLOAD.getState().equals(state)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} is not unload, cannot turn on stopTimer.", thingCode);
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

            stopTimer = new Timer();
            stopTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Compressor {} in stopTimer.", thingCode);
                    }
                    waitForStateConfirm();
                    if (EnumCompressorState.UNLOAD.getState().equals(state)) {
                        manager.operateCompressor(thingCode, EnumCompressorOperation.STOP, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
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
    private synchronized void confirmState() {
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
            onStateChange(state, oldState, manager.isIntelligent());
        }


        //清除stateTimer
        removeStateTimer();

        //日志确认更新
        updateLogs();
    }

    /**
     * 状态变化
     */
    private void onStateChange(String postState, String preState, boolean intelligent) {
        if (EnumCompressorState.UNLOAD.getState().equals(state)) {
            //卸载状态下开启stopTimer
            if (!intelligent) {
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

        //保存状态日志
        manager.saveCompressorState(thingCode, postState, preState);
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
                    manager.updateLog(logId, thingCode, logTimerTask.requestId);
                });
            }
        }
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

    public CompressorManager getManager() {
        return manager;
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
            manager.updateLog(id, thingCode, requestId);
        }
    }
}
