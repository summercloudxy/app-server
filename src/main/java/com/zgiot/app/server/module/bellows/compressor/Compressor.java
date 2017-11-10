package com.zgiot.app.server.module.bellows.compressor;

import com.alibaba.fastjson.annotation.JSONField;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
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
    private volatile int remote;

    /**
     * 加载状态
     */
    private volatile int loadState;

    /**
     * 运行状态
     */
    private volatile int runState;

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
     * 数据刷新
     * @param dataService   数据源
     * @return
     */
    public Compressor refresh(DataService dataService) {
        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} refresh.", thingCode);
        }
        List<DataModelWrapper> dataList = dataService.findDataByThing(thingCode);
        if (dataList == null || dataList.isEmpty()) {
            logger.info("Compressor: {} refresh failed.Because data list is empty.");
            return this;
        }

        dataList.forEach((data) -> {
            String metricCode = data.getMetricCode();
            String value = data.getValue();
            //组装属性（远程状态、启动状态和加载状态使用监听者，不在这里组装）
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
                    if (logger.isDebugEnabled()) {
                        logger.debug("MetricCode: {} is unnecessary for {}.", metricCode, thingCode);
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
     * @param dataModel
     */
    public void onRunStateChange(DataModel dataModel) {
        runState = Integer.parseInt(dataModel.getValue());
        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get run state signal: {}.", thingCode, runState);
        }

        confirmState();
    }

    /**
     * 加载状态变化
     * @param dataModel
     */
    public void onLoadStateChange(DataModel dataModel) {
        loadState = Integer.parseInt(dataModel.getValue());
        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get load state signal: {}.", thingCode, runState);
        }

        if (runState == YES && loadState == YES) {
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
     * @param dataModel
     */
    public void onRemoteChange(DataModel dataModel) {
        remote = Integer.parseInt(dataModel.getValue());
        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} get remote signal: {}.", thingCode, remote);
        }

        //就地状态删除stopTimer
        turnOffStopTimer();
    }

    /**
     * 开启stopTimer
     */
    public void turnOnStopTimer() {
        if (TYPE_HIGH.equals(type)) {
            if (logger.isDebugEnabled()) {
                logger.debug("High press compressor {} need not turn on stop timer.", thingCode);
            }
            return;
        }

        if (remote == NO) {
            if (logger.isDebugEnabled()) {
                logger.debug("Compressor {} is local, cannot turn on stopTimer.", thingCode);
            }
            return;
        }

        if (!BellowsConstants.STATE_UNLOAD.equals(state)) {
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
                    manager.operateCompressor(thingCode, EnumCompressorOperation.STOP, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
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
    private void confirmState() {
        String oldState = state;

        if (runState == YES) {
            if (loadState == YES) {
                state = BellowsConstants.STATE_RUNNING;
            } else {
                state = BellowsConstants.STATE_UNLOAD;
            }
        } else {
            state = BellowsConstants.STATE_STOPPED;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor: {} state now is: {}.", thingCode, state);
        }


        if (!oldState.equals(state)) {
            onStateChange();
        }


        //清除stateTimer
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

        //保存日志确认
        synchronized (logLock) {
            if (!logTimerMap.isEmpty()) {
                logTimerMap.forEach((logId, logTimerTask) ->{
                    manager.updateLog(logId, thingCode, logTimerTask.requestId);
                });
            }
        }
    }

    /**
     * 状态变化
     */
    private void onStateChange() {
        if (BellowsConstants.STATE_UNLOAD.equals(state)) {
            //卸载状态下开启stopTimer
            manager.turnOnStopTimer(this);
        } else {
            //其他状态下关闭stopTimer
            turnOffStopTimer();
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

    public int getRemote() {
        return remote;
    }

    public void setRemote(int remote) {
        this.remote = remote;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }

    public int getRunState() {
        return runState;
    }

    public void setRunState(int runState) {
        this.runState = runState;
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
