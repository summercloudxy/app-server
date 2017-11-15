package com.zgiot.app.server.module.bellows.compressor;

import com.alibaba.fastjson.annotation.JSONField;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
import com.zgiot.app.server.module.bellows.enumeration.EnumHighCompressorFault;
import com.zgiot.app.server.module.bellows.enumeration.EnumLowCompressorFault;
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
    private final int sort;


    @JSONField(serialize = false)
    private final CompressorManager manager;

    /**
     * 是否就地，1集控，0就地
     */
    private volatile boolean remote;

    /**
     * 加载状态
     */
    private volatile boolean loadState;

    /**
     * 运行状态
     */
    private volatile boolean runState;

    /**
     * 故障状态
     */
    private volatile boolean errorState;

    /**
     * 排气压力
     */
    private volatile double pressure;

    /**
     * 排气温度
     */
    private volatile double temperature;

    /**
     * 当前电流
     */
    private volatile double current;

    /**
     * 额定电流
     */
    private volatile double rateCurrent;

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
    private Map<Long, LogTimerTask> logTimerMap = new HashMap<>();



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
        Optional<DataModelWrapper> warnData = dataService.getData(thingCode, CompressorMetricConstants.WARN);
        if (!warnData.isPresent()) {
            //TODO: 调用cmdService接口取数据
            return this;
        }
        boolean warn = Boolean.parseBoolean(warnData.get().getValue());

        Optional<DataModelWrapper> errorData = dataService.getData(thingCode, CompressorMetricConstants.ERROR);
        if (!errorData.isPresent()) {
            //TODO: 调用cmdService接口取数据
            return this;
        }
        boolean error = Boolean.parseBoolean(errorData.get().getValue());
        if (warn == true || error == true) {
            errorState = true;
        }


        //运行状态
        Optional<DataModelWrapper> runData = dataService.getData(thingCode, CompressorMetricConstants.RUN_STATE);
        if (runData.isPresent()) {
            runState = Boolean.parseBoolean(runData.get().getValue());
        } else {
            //TODO: 调用cmdService接口取数据
        }

        //加载状态
        Optional<DataModelWrapper> loadData = dataService.getData(thingCode, CompressorMetricConstants.LOAD_STATE);
        if (loadData.isPresent()) {
            loadState = Boolean.parseBoolean(loadData.get().getValue());
        } else {
            //TODO: 调用cmdService接口取数据
        }

        //远程/就地状态
        Optional<DataModelWrapper> remoteData = dataService.getData(thingCode, CompressorMetricConstants.REMOTE);
        if (remoteData.isPresent()) {
            remote = Boolean.parseBoolean(remoteData.get().getValue());
        } else {
            //TODO: 调用cmdService接口取数据
        }

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
        List<DataModelWrapper> dataList = dataService.findDataByThing(thingCode);
        if (dataList == null || dataList.isEmpty()) {
            logger.info("Compressor: {} refresh failed.Because data list is empty.");
            return this;
        }

        dataList.forEach((data) -> {
            String metricCode = data.getMetricCode();
            String value = data.getValue();
            //组装属性（远程状态、故障状态、启动状态和加载状态使用监听者，不在这里组装）
            switch (metricCode) {
                case CompressorMetricConstants.CURRENT:
                    this.setCurrent(Double.parseDouble(value));
                    break;
                case CompressorMetricConstants.RATE_CURRENT:
                    this.setRateCurrent(Double.parseDouble(value));
                    break;
                case CompressorMetricConstants.PRESSURE:
                    this.setPressure(Double.parseDouble(value));
                    break;
                case CompressorMetricConstants.TEMPERATURE:
                    this.setTemperature(Double.parseDouble(value));
                    break;
                case CompressorMetricConstants.RUN_TIME:
                    this.setRunTime(Integer.parseInt(value));
                    break;
                case CompressorMetricConstants.LOAD_TIME:
                    this.setLoadTime(Integer.parseInt(value));
                    break;
                default:
                    if (EnumCompressorState.ERROR.getState().equals(state)) {
                        if (TYPE_HIGH.equals(type) && EnumHighCompressorFault.metricCodes().contains(metricCode)) {
                            EnumHighCompressorFault fault = EnumHighCompressorFault.getByMetricCode(metricCode);
                            errors.add(fault.getInfo());
                        } else if (TYPE_LOW.equals(type) && EnumLowCompressorFault.metricCodes().contains(metricCode)) {
                            EnumLowCompressorFault fault = EnumLowCompressorFault.getByMetricCode(metricCode);
                            errors.add(fault.getInfo());
                        }
                    }
                    break;
            }
        });
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
                logger.trace("Compressor {} add logWaitTimer.Log id: {}.", thingCode, logId);
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

        if (errorState == false && runState == true && loadState == true) {
            //收到加载信号，启动定时器，判断是否是保护模式
            synchronized (stateLock) {
                if (stateTimer != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor: {} state timer exist.", thingCode);
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
    public void onRemoteChange(String value, String requestId) {
        remote = Boolean.parseBoolean(value);
        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get remote signal: {}.RequestId: {}.", thingCode, remote, requestId);
        }

        if (remote == false) {
            //就地状态删除stopTimer
            turnOffStopTimer();
        } else if (manager.isIntelligent()){
            //远程状态下设置stopTimer
            turnOnStopTimer();
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

        if (remote == false) {
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

        if (errorState == true) {
            state = EnumCompressorState.ERROR.getState();
        } else if (runState == true) {
            if (loadState == true) {
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
            onStateChange(state, oldState);
        }


        //清除stateTimer
        removeStateTimer();

        //日志确认更新
        updateLogs();
    }

    /**
     * 状态变化
     */
    private void onStateChange(String postState, String preState) {
        if (EnumCompressorState.UNLOAD.getState().equals(state)) {
            //卸载状态下开启stopTimer
            if (!manager.isIntelligent()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Low press intelligent is {},  cannot turn on stop timer.", manager.isIntelligent());
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

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
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

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getRateCurrent() {
        return rateCurrent;
    }

    public void setRateCurrent(double rateCurrent) {
        this.rateCurrent = rateCurrent;
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
