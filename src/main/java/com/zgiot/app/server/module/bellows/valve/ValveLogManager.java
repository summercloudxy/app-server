package com.zgiot.app.server.module.bellows.valve;

import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumValveOperation;
import com.zgiot.app.server.module.bellows.pojo.ValveLog;
import com.zgiot.app.server.module.bellows.pressure.PressureManager;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.BellowsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangwei
 */
public class ValveLogManager {

    private static final Logger logger = LoggerFactory.getLogger(ValveLogManager.class);

    private final BellowsMapper bellowsMapper;
    private final DataService dataService;
    private final PressureManager pressureManager;

    private Timer logTimer = new Timer();


    /**
     * 日志等待timer
     */
    private Map<Long, LogTimerTask> logTaskMap = new ConcurrentHashMap<>();

    public ValveLogManager(BellowsMapper bellowsMapper, DataService dataService, PressureManager pressureManager) {
        this.bellowsMapper = bellowsMapper;
        this.dataService = dataService;
        this.pressureManager = pressureManager;
    }

    /**
     * 保存日志，等待状态确认
     * @param valve
     * @param operation
     * @param operationType
     * @param timeout 日志确认时间（毫秒），0为不需要确认
     * @param requestId
     * @return logId
     */
    public Long saveLog(Valve valve, EnumValveOperation operation, String operationType, int timeout, String memo, String requestId) {
        ValveLog valveLog = new ValveLog();
        valveLog.setOperation(operation.toString());
        valveLog.setOperateTime(new Date());
        valveLog.setRequestId(requestId);
        valveLog.setThingCode(valve.getThingCode());
        valveLog.setPreState(valve.getState());
        valveLog.setOperateType(operationType);
        valveLog.setPostState(valve.getState());
        valveLog.setConfirmTime(new Date());
        valveLog.setHighPressure(pressureManager.refreshPressure(BellowsConstants.CP_TYPE_HIGH, dataService, requestId));
        valveLog.setLowPressure(pressureManager.refreshPressure(BellowsConstants.CP_TYPE_LOW, dataService, requestId));
        valveLog.setMemo(memo);
        valveLog.setTeamId(valve.getTeamId());
        valveLog.setName(valve.getName());

        bellowsMapper.saveValveLog(valveLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Valve {} save operation log.RequestId: {}.LogId: {}.", valve.getThingCode(), requestId, valveLog.getId());
        }

        //添加日志确认timer
        if (timeout > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve log start timer. RequestId: {}.", requestId);
            }

            LogTimerTask task = new LogTimerTask(valveLog.getId(), valve, requestId);
            logTimer.schedule(task, timeout);
            logTaskMap.put(valveLog.getId(), task);
        }

        return valveLog.getId();
    }


    /**
     * 更新日志确认状态
     * @param logId
     * @param valve
     * @param requestId
     */
    public void updateLog(Long logId, Valve valve, String requestId) {
        valve.refresh(dataService);

        ValveLog valveLog = new ValveLog();
        valveLog.setId(logId);
        valveLog.setConfirmTime(new Date());
        valveLog.setPostState(valve.getState());
        valveLog.setHighPressure(pressureManager.refreshPressure(BellowsConstants.CP_TYPE_HIGH, dataService, requestId));
        valveLog.setLowPressure(pressureManager.refreshPressure(BellowsConstants.CP_TYPE_LOW, dataService, requestId));

        bellowsMapper.updateValveLog(valveLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} update log {}.RequestId: {}.", valve.getThingCode(), logId, requestId);
        }

        //清除日志timerTask
        LogTimerTask task = logTaskMap.get(logId);
        if (task != null) {
            task.cancel();
            logTaskMap.remove(logId);
        }
    }

    /**
     * 获取阀门操作日志
     * @param startTime
     * @param endTime
     * @param lastId    上次查询的最后一个id（null为首次查询）
     * @param count
     * @param requestId
     * @return
     */
    public List<ValveLog> getValveLog(Date startTime, Date endTime, Long lastId, Integer count, String requestId) {
        return bellowsMapper.getValveLog(startTime, endTime, lastId, count);
    }


    /**
     * 日志确认定时任务
     */
    private class LogTimerTask extends TimerTask {
        Long id;    //日志id
        Valve valve;    //阀门
        String requestId;   //请求id

        public LogTimerTask(Long id, Valve valve, String requestId) {
            this.id = id;
            this.valve = valve;
            this.requestId = requestId;
        }

        @Override
        public void run() {
            if (logger.isDebugEnabled()) {
                logger.debug("In valve log timer.LogId: {}. Valve: {}. RequestId: {}.", id, valve.getThingCode(), requestId);
            }
            updateLog(id, valve, requestId);
        }
    }
}
