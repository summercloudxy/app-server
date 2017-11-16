package com.zgiot.app.server.module.bellows.compressor;

import com.zgiot.app.server.module.bellows.compressor.cache.CompressorCache;
import com.zgiot.app.server.module.bellows.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.pojo.CompressorState;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.CompressorMetricConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;

import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author wangwei
 */
@Component
public class CompressorManager {

    private static final Logger logger = LoggerFactory.getLogger(CompressorManager.class);

    private Object cacheLock = new Object();

    private Object pressureLock = new Object();

    /**
     * 脉冲指令清除时间
     */
    private static final int CLEAN_PERIOD = 500;

    /**
     * 智能压力控制等待时间
     */
    private static final int PRESSURE_WAIT_TIME = 60000;

    /**
     * 压力状态监测设备号
     */
    private static final String PRESSURE_THING_CODE = "SYS_SB";

    /**
     * 低压管道压力检测1
     */
    private static final String LOW_PRESSURE_ONE = "2333";
    /**
     * 低压管道压力检测2
     */
    private static final String LOW_PRESSURE_TWO = "2334";
    /**
     * 高压管道压力检测
     */
    private static final String HIGH_PRESSURE = "2335";




    @Autowired
    private CompressorCache compressorCache;
    @Autowired
    private DataService dataService;
    @Autowired
    private BellowsMapper bellowsMapper;
    @Autowired
    private CmdControlService cmdControlService;

    /**
     * 低压空压机组
     */
    private CompressorGroup low;

    /**
     * 高压空压机组
     */
    private CompressorGroup high;


    /**
     * 低压空压机智能
     */
    private volatile boolean intelligent = false;

    /**
     * 低压空压机压力状态
     */
    private volatile double pressureState;

    /**
     * 智能压力控制timer
     */
    private volatile Timer pressureTimer;


    @PostConstruct
    public void init() {
        //初始化空压机缓存
        synchronized (cacheLock) {
            compressorCache.put("2510", new Compressor("2510", "2510", Compressor.TYPE_LOW, 0, this).initState(dataService, cmdControlService));
            compressorCache.put("2511", new Compressor("2511", "2511", Compressor.TYPE_LOW, 1, this).initState(dataService, cmdControlService));
            compressorCache.put("2512", new Compressor("2512", "2512", Compressor.TYPE_LOW, 2, this).initState(dataService, cmdControlService));
            compressorCache.put("2530", new Compressor("2530", "2530", Compressor.TYPE_HIGH, 0, this).initState(dataService, cmdControlService));
            compressorCache.put("2531", new Compressor("2531", "2531", Compressor.TYPE_HIGH, 1, this).initState(dataService, cmdControlService));
            compressorCache.put("2532", new Compressor("2532", "2532", Compressor.TYPE_HIGH, 2, this).initState(dataService, cmdControlService));

            high = new CompressorGroup(compressorCache.findByType(Compressor.TYPE_HIGH), Compressor.TYPE_HIGH, Arrays.asList(HIGH_PRESSURE), this);
            low = new CompressorGroup(compressorCache.findByType(Compressor.TYPE_LOW), Compressor.TYPE_LOW, Arrays.asList(LOW_PRESSURE_ONE, LOW_PRESSURE_TWO), this);
        }

        //初始化空压机智能
        boolean intelligent = (bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT) == 1);

        //初始化低压空压机压力状态
        initPressureState();

        setIntelligent(intelligent);
    }


    public void onDataSourceChange(DataModel data) {
        String requestId = RequestIdUtil.generateRequestId();
        if (logger.isTraceEnabled()) {
            logger.trace("Got data: {}.RequestId: {}.", data, requestId);
        }

        String metricCode = data.getMetricCode();
        Compressor compressor;

        switch (metricCode) {
            case CompressorMetricConstants.RUN_STATE:
                compressor = getCompressorFromCache(data.getThingCode());
                if (compressor == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} not found.RequestId: {}.", data.getThingCode(), requestId);
                    }
                    return;
                }
                compressor.onRunStateChange(data.getValue(), requestId);
                break;
            case CompressorMetricConstants.LOAD_STATE:
                compressor = getCompressorFromCache(data.getThingCode());
                if (compressor == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} not found.RequestId: {}.", data.getThingCode(), requestId);
                    }
                    return;
                }
                compressor.onLoadStateChange(data.getValue(), requestId);
                break;
            case CompressorMetricConstants.WARN:
            case CompressorMetricConstants.ERROR:
                compressor = getCompressorFromCache(data.getThingCode());
                if (compressor == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} not found.RequestId: {}.", data.getThingCode(), requestId);
                    }
                    return;
                }
                compressor.onErrorStateChange(data.getValue(), requestId);
                break;
            case CompressorMetricConstants.LOCAL:
                compressor = getCompressorFromCache(data.getThingCode());
                if (compressor == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} not found.RequestId: {}.", data.getThingCode(), requestId);
                    }
                    return;
                }
                compressor.onLocalChange(data.getValue(), requestId, intelligent);
                break;
            case BellowsConstants.METRIC_PRESSURE_STATE:
                if (!data.getThingCode().equals(PRESSURE_THING_CODE)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Pressure device code {} is wrong.RequestId: {}.", data.getThingCode(), requestId);
                    }
                    return;
                }
                setPressureState(Double.parseDouble(data.getValue()), requestId);
                break;
            default:
                logger.warn("Got wrong metric code: {}.RequestId: {}.", metricCode, requestId);
        }
    }




    /**
     * 设置低压空压机智能模式
     * @param intelligent
     * @param requestId
     */
    public synchronized void changeLowCompressorIntelligent(int intelligent, String requestId) {
        if (this.intelligent == (intelligent==Compressor.YES)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Low compressor intelligent is already {}. RequestId: {}", this.intelligent, requestId);
            }
            return;
        }

        setIntelligent(intelligent == Compressor.YES);

        bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT, (double)intelligent);
        logger.info("Low compressor intelligent has bean set {}.RequestId: {}", this.intelligent, requestId);
    }

    /**
     * 操作空压机
     * @param thingCode 设备号
     * @param operation 操作
     * @param operationType 手动/自动
     * @param requestId
     */
    public int operateCompressor(String thingCode, EnumCompressorOperation operation, String operationType, String requestId) {
        Compressor compressor = getCompressorFromCache(thingCode);
        if (compressor == null) {
            logger.warn("Compressor: {} not found.RequestId: {}", thingCode, requestId);
            throw new SysException("空压机" + thingCode + "不存在", SysException.EC_UNKNOWN);
        }

        if (compressor.isLocal()) {
            logger.warn("Compressor: {} is local, cannot be operated remotely.RequestId: {}", thingCode, requestId);
            String error = "空压机" + thingCode + "处于就地模式，无法进行远程操作";
            saveFullLog(compressor, operation, operationType, requestId, error);
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
            saveFullLog(compressor, operation, operationType, requestId, e.getMessage());
            throw e;
        }

        //日志记录
        saveLog(compressor, operation, operationType, requestId);

        return count;
    }

    /**
     * 保存日志，等待状态确认
     * @param compressor
     * @param operation
     * @param operationType
     * @param requestId
     */
    private void saveLog(Compressor compressor, EnumCompressorOperation operation, String operationType, String requestId) {
        CompressorLog compressorLog = new CompressorLog();
        compressorLog.setOperation(operation.toString());
        compressorLog.setOperateTime(new Date());
        compressorLog.setRequestId(requestId);
        compressorLog.setThingCode(compressor.getThingCode());
        compressorLog.setPreState(compressor.getState());
        compressorLog.setOperateType(operationType);
        bellowsMapper.saveCompressorLog(compressorLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} save operation log.RequestId: {}.LogId: {}.", compressor.getThingCode(), requestId, compressorLog.getId());
        }

        //添加日志确认timer
        compressor.addLogWaitTimer(compressorLog.getId(), requestId);
    }

    /**
     * 保存日志
     * @param compressor
     * @param operation
     * @param operationType
     * @param requestId
     */
    private void saveFullLog(Compressor compressor, EnumCompressorOperation operation, String operationType, String requestId, String memo) {
        //数据刷新
        compressor.refresh(dataService);

        CompressorLog compressorLog = new CompressorLog();
        compressorLog.setOperation(operation.toString());
        compressorLog.setOperateTime(new Date());
        compressorLog.setRequestId(requestId);
        compressorLog.setThingCode(compressor.getThingCode());
        compressorLog.setPreState(compressor.getState());
        compressorLog.setOperateType(operationType);
        compressorLog.setPostState(compressor.getState());
        compressorLog.setConfirmTime(new Date());
        compressorLog.setPressure(compressor.getPressure());

        bellowsMapper.saveCompressorLog(compressorLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} save full operation log.RequestId: {}.LogId: {}.", compressor.getThingCode(), requestId, compressorLog.getId());
        }
    }


    /**
     * 更新日志确认状态
     * @param logId
     * @param thingCode
     */
    public void updateLog(Long logId, String thingCode, String requestId) {
        //获取最新状态
        Compressor compressor = getCompressorFromCache(thingCode);
        if (compressor == null) {
            logger.warn("Compressor: {} not found.RequestId: {}", thingCode, requestId);
            throw new SysException("空压机" + thingCode + "不存在", SysException.EC_UNKNOWN);
        }

        //等待状态确认
        compressor.waitForStateConfirm();

        compressor.refresh(dataService);
        CompressorLog compressorLog = new CompressorLog();
        compressorLog.setId(logId);
        compressorLog.setConfirmTime(new Date());
        compressorLog.setPostState(compressor.getState());
        compressorLog.setPressure(compressor.getPressure());

        bellowsMapper.updateCompressorLog(compressorLog);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor {} update log {}.", thingCode, logId);
        }

        //清除日志timerTask
        compressor.removeLogWaitTimer(logId);
    }

    /**
     * 分页获取空压机日志
     * @param startTime
     * @param endTime
     * @param page
     * @param count
     * @return
     */
    public List<CompressorLog> getCompressorLog(Date startTime, Date endTime, Integer page, Integer count, String requestId) {
        Integer offset = null;
        if (page != null && count != null) {
            offset = page * count;
        }

        return bellowsMapper.getCompressorLog(startTime, endTime, offset, count);
    }

    /**
     * 保存空压机状态
     * @param thingCode 设备号
     * @param postState 修改后状态
     * @param preState  修改前状态
     */
    public void saveCompressorState(String thingCode, String postState, String preState) {
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
     * 分析低压空压机状态
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Map<String, Long>> analyseCompressorState(Date startTime, Date endTime, String requestId) {
        //保存结果
        Map<String, Map<String, Long>> result = new HashMap<>(3);

        //记录thingCode对应上一条state
        Map<String, CompressorState> thingCodeStateMap = new HashMap<>(3);

        List<String> thingCodes = new ArrayList<>(3);
        for (Compressor compressor : low.getCompressors()) {
            thingCodes.add(compressor.getThingCode());
            result.put(compressor.getThingCode(), new HashMap<>(4));
            thingCodeStateMap.put(compressor.getThingCode(), null);
        }

        //数据库查询
        List<CompressorState> list = bellowsMapper.getCompressorState(startTime, endTime, thingCodes, null, null);

        if (list != null && !list.isEmpty()) {
            //遍历compressorStateList
            for (CompressorState state : list) {
                String thingCode = state.getThingCode();
                CompressorState lastState = thingCodeStateMap.get(thingCode);
                Map<String, Long> stateMap = result.get(thingCode);
                long add;
                if (lastState == null) {
                    add = endTime.getTime() - state.getTime().getTime();
                } else {
                    add = lastState.getTime().getTime() - state.getTime().getTime();
                }
                Long time = stateMap.get(state.getPostState());
                if (time == null) {
                    time = add;
                } else {
                    time += add;
                }
                stateMap.put(state.getPostState(), time);

                thingCodeStateMap.put(thingCode, state);
            }
        }


        thingCodeStateMap.forEach((thingCode, state) -> {
            if (state != null) {
                long add = state.getTime().getTime() - startTime.getTime();
                Long time = result.get(thingCode).get(state.getPreState());
                if (time == null) {
                    time = add;
                } else {
                    time += add;
                }
                result.get(thingCode).put(state.getPreState(), time);
            } else {
                //未查询到该thingCode数据，再次查询上一条数据
                if (logger.isDebugEnabled()) {
                    logger.debug("Compressor {} cannot found state log in startTime {} , endTime{}.RequestId: {}.", thingCode, startTime, endTime, requestId);
                }

                List<CompressorState> lastState = bellowsMapper.getCompressorState(null, startTime, Arrays.asList(thingCode), 0, 1);
                if (lastState != null && !lastState.isEmpty()) {
                    result.get(thingCode).put(lastState.get(0).getPostState(), endTime.getTime() - startTime.getTime());
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} found no state log.RequestId: {}.", thingCode, requestId);
                    }
                }
            }
        });

        return result;
    }


    /**
     * 刷新空压机组
     * @param type  分组类型
     * @param requestId
     * @return
     */
    public CompressorGroup refreshCompressorGroup(String type, String requestId) {
        if (Compressor.TYPE_HIGH.equals(type)) {
            return high.refresh(dataService);
        } else if (Compressor.TYPE_LOW.equals(type)) {
            return low.refresh(dataService);
        } else {
            logger.warn("Compressor group type {} is wrong. RequestId: {}.", type, requestId);
            return null;
        }
    }

    /**
     * 按分类刷新空压机
     * @param type
     * @param requestId
     * @return
     */
    public List<Compressor> refreshCompressors(String type, String requestId) {
        List<Compressor> list;
        if (Compressor.TYPE_HIGH.equals(type)) {
            list = high.getCompressors();
        } else if (Compressor.TYPE_LOW.equals(type)) {
            list = low.getCompressors();
        } else {
            logger.warn("Compressor group type {} is wrong. RequestId: {}.", type, requestId);
            return new ArrayList<>();
        }

        for (Compressor compressor : list) {
            compressor.refresh(dataService);
        }
        return list;
    }


    public boolean isIntelligent() {
        return intelligent;
    }


    private void setIntelligent(boolean intelligent) {
        this.intelligent = intelligent;
        logger.info("Low compressor initial intelligent state: {}", intelligent);

        if (intelligent) {
            //智能模式下
            //开启低压空压机的卸载关闭定时
            turnOnStopTimers();

            //压力检测判断
            onPressureStateChange(intelligent);
        } else {
            //手动模式下
            //关闭低压空压机的卸载关闭定时
            turnOffStopTimers();

            //关闭压力检测计时器
            turnOffPressureTimer();
        }

    }


    private void setPressureState(double pressureState, String requestId) {
        double oldState = this.pressureState;
        this.pressureState = pressureState;

        if (logger.isDebugEnabled()) {
            logger.debug("Pressure state is now {}.RequestId: {}.", pressureState, requestId);
        }

        if (pressureState != oldState) {
            //如果有压力计时器，则关闭
            turnOffPressureTimer();

            onPressureStateChange(intelligent);
        }
    }

    /**
     * 低压空压机压力状态变化
     */
    public void onPressureStateChange(boolean intelligent) {
        if (!intelligent) {
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
     * 从缓存中获取空压机实例
     * @param thingCode
     * @return
     */
    private Compressor getCompressorFromCache(String thingCode) {
        synchronized (cacheLock) {
            //等待空压机缓存初始化
        }
        Compressor compressor = compressorCache.findByThingCode(thingCode);
        return compressor;
    }

    /**
     * 开启低压空压机的卸载关闭定时
     */
    private void turnOnStopTimers() {
        low.getCompressors().forEach(compressor -> {
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
        low.getCompressors().forEach(compressor -> {
            compressor.turnOffStopTimer();
        });
    }

    /**
     * 初始化低压空压机压力状态
     */
    private void initPressureState() {
        String requestId = RequestIdUtil.generateRequestId();

        Optional<DataModelWrapper> data = dataService.getData(PRESSURE_THING_CODE, BellowsConstants.METRIC_PRESSURE_STATE);
        if (data.isPresent()) {
            setPressureState(Double.parseDouble(data.get().getValue()), requestId);
        } else {
            DataModelWrapper wrapper = dataService.adhocLoadData(PRESSURE_THING_CODE, BellowsConstants.METRIC_PRESSURE_STATE);

            if (wrapper == null) {
                logger.error("Cannot connect data engine to get thingCode {}, metricCode {}.", PRESSURE_THING_CODE, BellowsConstants.METRIC_PRESSURE_STATE);
                setPressureState(BellowsConstants.PRESSURE_NORMAL, requestId);
            } else {
                setPressureState(Double.parseDouble(wrapper.getValue()), requestId);
            }
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
        Compressor compressor = chooseLowCompressorToTurnOff();
        if (compressor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No compressor can be unloaded");
            }
            return;
        }

        logger.info("Compressor {} will be unloaded because of high pressure.", compressor.getThingCode());
        operateCompressor(compressor.getThingCode(), EnumCompressorOperation.UNLOAD, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
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
        Compressor compressor = chooseLowCompressorToTurnOn();
        if (compressor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No compressor can be run");
            }
            return;
        }
        logger.info("Compressor {} will be running because of low pressure.", compressor.getThingCode());
        if (EnumCompressorState.STOPPED.getState().equals(compressor.getState())) {
            operateCompressor(compressor.getThingCode(), EnumCompressorOperation.START, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
        } else if (EnumCompressorState.UNLOAD.getState().equals(compressor.getState())) {
            operateCompressor(compressor.getThingCode(), EnumCompressorOperation.LOAD, BellowsConstants.TYPE_AUTO, RequestIdUtil.generateRequestId());
        }
    }

    /**
     * 选择需要关闭的低压空压机
     * @return  null表示没有可关闭的
     */
    private Compressor chooseLowCompressorToTurnOff() {
        List<Compressor> list = low.getCompressors();
        Compressor result = null;
        for (Compressor compressor : list) {
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
    private Compressor chooseLowCompressorToTurnOn() {
        List<Compressor> list = low.getCompressors();
        Compressor result = null;
        for (Compressor compressor : list) {
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
}
