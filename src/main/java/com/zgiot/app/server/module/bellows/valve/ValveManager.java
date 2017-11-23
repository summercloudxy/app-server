package com.zgiot.app.server.module.bellows.valve;

import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumValveOperation;
import com.zgiot.app.server.module.bellows.enumeration.EnumValveState;
import com.zgiot.app.server.module.bellows.pojo.ValveLog;
import com.zgiot.app.server.module.bellows.pojo.ValveTeam;
import com.zgiot.app.server.module.bellows.pojo.ValveTeamResult;
import com.zgiot.app.server.module.bellows.pressure.PressureManager;
import com.zgiot.app.server.module.bellows.util.BellowsUtil;
import com.zgiot.app.server.module.bellows.valve.cache.ValveCache;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.ValveMetricConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import org.apache.commons.lang.time.DateUtils;
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
public class ValveManager {

    private static final Logger logger = LoggerFactory.getLogger(ValveManager.class);

    @Autowired
    private ValveCache valveCache;

    @Autowired
    private DataService dataService;

    @Autowired
    private CmdControlService cmdControlService;

    @Autowired
    private BellowsMapper bellowsMapper;

    @Autowired
    private PressureManager pressureManager;

    private ValveLogManager valveLogManager;

    private Object cacheLock = new Object();

    /**
     * 泵频率限制
     */
    private double speedLimit;


    /**
     * 智能鼓风阶段
     */
    private volatile String stage;

    /**
     * 下轮鼓风预计时间
     */
    private volatile Long nextBlowTime;

    /**
     * 每组最大个数
     */
    private volatile int maxCount;

    /**
     * 鼓风等待时间（分）
     */
    private volatile int waitTime;

    /**
     * 每组鼓风时长（分）
     */
    private volatile int runTime;

    /**
     * 下轮需要重新分组
     */
    private volatile boolean changed;

    /**
     * 最大分组id
     */
    private volatile Long maxTeamId;

    /**
     * 块煤分组数
     */
    private volatile int lumpTeamCount;

    /**
     * 末煤分组数
     */
    private volatile int slackTeamCount;



    @PostConstruct
    public void init() {
        if (logger.isTraceEnabled()) {
            logger.trace("Valve cache start to init.");
        }
        //缓存初始化
        synchronized (cacheLock) {
            int sort = 0;

            valveCache.put("2321.GF-1",
                    new Valve("2321.GF-1", "块合2321.GF1", BellowsConstants.VALVE_TYPE_LUMP, sort++, "2321", "2322").init(bellowsMapper));
            valveCache.put("2321.GF-2",
                    new Valve("2321.GF-2", "块合2321.GF2", BellowsConstants.VALVE_TYPE_LUMP, sort++, "2321", "2323").init(bellowsMapper));

            valveCache.put("2333.GF-1",
                    new Valve("2333.GF-1", "主混2333.GF1", BellowsConstants.VALVE_TYPE_MAIN_WASH, sort++, "2333", "2336").init(bellowsMapper));
            valveCache.put("2358.GF-1",
                    new Valve("2358.GF-1", "主合2358.GF1", BellowsConstants.VALVE_TYPE_MAIN_WASH, sort++, "2358", "2361").init(bellowsMapper));
            valveCache.put("2334.GF-1",
                    new Valve("2334.GF-1", "主混2334.GF1", BellowsConstants.VALVE_TYPE_MAIN_WASH, sort++, "2334", "2337").init(bellowsMapper));
            valveCache.put("2359.GF-1",
                    new Valve("2359.GF-1", "主合2359.GF1", BellowsConstants.VALVE_TYPE_MAIN_WASH, sort++, "2359", "2362").init(bellowsMapper));
            valveCache.put("2335.GF-1",
                    new Valve("2335.GF-1", "主混2335.GF1", BellowsConstants.VALVE_TYPE_MAIN_WASH, sort++, "2335", "2338").init(bellowsMapper));
            valveCache.put("2360.GF-1",
                    new Valve("2360.GF-1", "主合2360.GF1", BellowsConstants.VALVE_TYPE_MAIN_WASH, sort++, "2360", "2360").init(bellowsMapper));

            valveCache.put("2380.GF-1",
                    new Valve("2380.GF-1", "再混2380.GF1", BellowsConstants.VALVE_TYPE_REWASH, sort++, "2380", "2383").init(bellowsMapper));
            valveCache.put("2412.GF-1",
                    new Valve("2412.GF-1", "再合2412.GF1", BellowsConstants.VALVE_TYPE_REWASH, sort++, "2412", "2415").init(bellowsMapper));
            valveCache.put("2381.GF-1",
                    new Valve("2381.GF-1", "再混2381.GF1", BellowsConstants.VALVE_TYPE_REWASH, sort++, "2381", "2384").init(bellowsMapper));
            valveCache.put("2413.GF-1",
                    new Valve("2413.GF-1", "再合2413.GF1", BellowsConstants.VALVE_TYPE_REWASH, sort++, "2413", "2416").init(bellowsMapper));
            valveCache.put("2382.GF-1",
                    new Valve("2382.GF-1", "再混2382.GF1", BellowsConstants.VALVE_TYPE_REWASH, sort++, "2382", "2385").init(bellowsMapper));
            valveCache.put("2414.GF-1",
                    new Valve("2414.GF-1", "再合2414.GF1", BellowsConstants.VALVE_TYPE_REWASH, sort++, "2414", "2417").init(bellowsMapper));
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Valve cache end init.");
        }

        //初始化valveLogManager
        valveLogManager = new ValveLogManager(bellowsMapper, dataService, pressureManager);

        //初始化鼓风参数
        initValveParam();

        //初始化下次鼓风时间和鼓风状态
        initNextBlowTimeAndStage();
    }

    /**
     * 初始化鼓风参数
     */
    private void initValveParam() {
        //泵频率限制
        speedLimit = bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_SPEED_LIMIT);

        //计算分组数量
        lumpTeamCount = bellowsMapper.countValveTeamByType(BellowsConstants.VALVE_TEAM_LUMP).intValue();
        if (logger.isTraceEnabled()) {
            logger.trace("Valve lump team count is {}.", lumpTeamCount);
        }
        slackTeamCount = bellowsMapper.countValveTeamByType(BellowsConstants.VALVE_TEAM_SLACK).intValue();
        if (logger.isTraceEnabled()) {
            logger.trace("Valve slack team count is {}.", slackTeamCount);
        }

        maxTeamId = bellowsMapper.getMaxTeamId();
        if (maxTeamId == null) {
            maxTeamId = 0L;
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Valve max team id is {}.", maxTeamId);
        }

        maxCount = bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_MAX_COUNT).intValue();
        if (logger.isTraceEnabled()) {
            logger.trace("Valve max count is {}.", maxCount);
        }

        runTime = bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_RUN_MINUTE).intValue();
        if (logger.isTraceEnabled()) {
            logger.trace("Valve runTime is {} minutes.", runTime);
        }

        waitTime = bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_WAIT_MINUTE).intValue();
        if (logger.isTraceEnabled()) {
            logger.trace("Valve waitTime is {} minutes.", waitTime);
        }

        changed = (bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_CHANGED).intValue() == BellowsConstants.YES);
        if (logger.isTraceEnabled()) {
            logger.trace("Valve team changed is {}.", changed);
        }
    }


    /**
     * 初始化下次鼓风时间
     * @return
     */
    private void initNextBlowTimeAndStage() {
        List<ValveTeam> runningTeams = bellowsMapper.getValveTeamByStatus(BellowsConstants.VALVE_STATUS_RUNNING);
        List<ValveTeam> waitTeams = bellowsMapper.getValveTeamByStatus(BellowsConstants.VALVE_STATUS_WAIT);
        if (runningTeams == null || runningTeams.isEmpty()) {
            //没有正在鼓风的
            if (waitTeams == null || waitTeams.isEmpty()) {
                //不处于智能鼓风状态
                stage = BellowsConstants.BLOW_STAGE_NONE;
                nextBlowTime = null;
            } else {
                //处于鼓风前等待阶段
                stage = BellowsConstants.BLOW_STAGE_WAIT;

                //判断将要鼓风时间是否超时
                Date execTime = checkExecOutTime(waitTeams.get(0).getExecTime());
                nextBlowTime = execTime.getTime();

                //设置每组阀门的状态
                for (int i=0,length=waitTeams.size();i<length;i++) {
                    if (i == 0) {
                        setValveStageAndExecTime(waitTeams.get(i).getId(), BellowsConstants.BLOW_STAGE_WAIT, execTime);
                    } else {
                        setValveStageAndExecTime(waitTeams.get(i).getId(), BellowsConstants.BLOW_STAGE_WAIT, null);
                    }
                }
            }
        } else {
            //正在鼓风阶段
            stage = BellowsConstants.BLOW_STAGE_RUN;

            ValveTeam runningTeam = runningTeams.get(0);
            //判断当前正在鼓风组是否超时
            Date execTime = checkExecOutTime(runningTeam.getExecTime());

            //设置每组阀门的状态
            setValveStageAndExecTime(runningTeam.getId(), BellowsConstants.BLOW_STAGE_RUN, null);
            for (int i=0,length=waitTeams.size();i<length;i++) {
                if (i == 0) {
                    setValveStageAndExecTime(waitTeams.get(i).getId(), BellowsConstants.BLOW_STAGE_WAIT, execTime);
                } else {
                    setValveStageAndExecTime(waitTeams.get(i).getId(), BellowsConstants.BLOW_STAGE_WAIT, null);
                }
            }

            if (runTime == 0 || maxCount == 0) {
                //不需要下次鼓风
                nextBlowTime = null;
            } else {
                //判断是否有阀门处于智能状态
                boolean needNextBlow = false;
                List<Valve> valves = valveCache.findAll();
                for (Valve valve : valves) {
                    if (valve.isIntelligent()) {
                        needNextBlow = true;
                        break;
                    }
                }
                if (!needNextBlow) {
                    nextBlowTime = null;
                } else {
                    //计算鼓风前等待时间
                    long nextBlowTime = waitTime*DateUtils.MILLIS_PER_MINUTE;
                    //判断当前正在鼓风组是否超时
                    nextBlowTime += execTime.getTime();

                    //计算等待组的总时间
                    if (waitTeams != null) {
                        nextBlowTime += runningTeam.getDuration()*waitTeams.size()*DateUtils.MILLIS_PER_MINUTE;
                    }

                    this.nextBlowTime = nextBlowTime;
                }
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Valve team next blow time is {}.", nextBlowTime);
            logger.trace("Valve blow stage is {}.", stage);
        }
    }


    /**
     * 刷新阀门信息
     * @return
     */
    public List<Valve> refreshValves() {
        List<Valve> valves = valveCache.findAll();
        for (Valve valve : valves) {
            valve.refresh(dataService);
        }

        return valves;
    }

    /**
     * 设置阀门智能鼓风阶段和执行时间
     * @param teamId
     * @param stage
     * @param execTime
     */
    private void setValveStageAndExecTime(Long teamId, String stage, Date execTime) {
        List<Valve> valves = valveCache.findByTeam(teamId);
        for (Valve valve : valves) {
            valve.setStage(stage);
            valve.setExecTime(execTime);
        }
    }

    /**
     * 判断执行时间是否已超过
     * @param execTime
     * @return
     */
    private Date checkExecOutTime(Date execTime) {
        Date now = new Date();
        if (execTime.after(now)) {
            return execTime;
        } else {
            return DateUtils.ceiling(now, Calendar.MINUTE);
        }
    }


    /**
     * 设置阀门智能状态
     * @param thingCode
     * @param intelligent
     * @param requestId
     */
    private void setValveIntelligent(String thingCode, boolean intelligent, String requestId) {
        Valve valve = getValveFromCache(thingCode);
        if (valve == null) {
            logger.warn("Valve: {} not found. RequestId: {}", thingCode, requestId);
            throw new SysException("阀门" + thingCode + "不存在", SysException.EC_UNKNOWN);
        }
        valve.setIntelligent(intelligent, bellowsMapper, requestId);
    }


    /**
     * 批量设置阀门智能状态
     * @param thingCodes    智能状态thingCode
     * @param requestId
     */
    public synchronized void setValveIntelligentBatch(String[] thingCodes, String requestId) {
        List<String> intelligentCodes;
        if (thingCodes == null || thingCodes.length == 0) {
            //所有阀门都是手动模式
            intelligentCodes = new ArrayList<>();
            //下次智能鼓风时间为null
            setNextBlowTime(null, requestId);
        } else {
            intelligentCodes = Arrays.asList(thingCodes);
        }

        Set<String> all = valveCache.findAllThingCode();
        all.forEach(thingCode -> {
            if (intelligentCodes.contains(thingCode)) {
                setValveIntelligent(thingCode,true, requestId);
            } else {
                setValveIntelligent(thingCode,false, requestId);
            }
        });

        onChanged(true, requestId);
    }


    /**
     * 修改阀门智能参数
     * @param maxCount  每组最大个数
     * @param runTime   每组运行时间
     * @param waitTime  等待时间
     * @param requestId
     */
    public synchronized void setValveParam(int maxCount, int runTime, int waitTime, String requestId) {
        boolean maxCountChanged = setMaxCount(maxCount, requestId);
        boolean runTimeChanged = setRunTime(runTime, requestId);
        boolean waitTimeChanged = setWaitTime(waitTime, requestId);

        if (logger.isDebugEnabled()) {
            logger.debug("Valve param set maxCount {}, runTime {}, waitTime {}. RequestId: {}.", maxCount, runTime, waitTime, requestId);
        }

        if (maxCountChanged || runTimeChanged || waitTimeChanged) {
            //有一项更新了，触发onChanged
            onChanged(true, requestId);
        }
    }

    /**
     * 设置每组最大个数
     * @param maxCount
     * @param requestId
     * @return
     */
    private boolean setMaxCount(int maxCount, String requestId) {
        int old = this.maxCount;
        if (maxCount == old) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve max count is already {}. RequestId: {}.", maxCount, requestId);
            }
            return false;
        }

        this.maxCount = maxCount;
        bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_MAX_COUNT, (long)maxCount);
        if (logger.isDebugEnabled()) {
            logger.debug("Valve max count is set to {}. RequestId: {}.", maxCount, requestId);
        }

        if (maxCount == 0) {
            setNextBlowTime(null, requestId);
        }
        return true;
    }

    /**
     * 设置等待时间
     * @param waitTime
     * @param requestId
     * @return
     */
    private boolean setWaitTime(int waitTime, String requestId) {
        int old = this.waitTime;
        if (waitTime == old) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve wait time is already {}. RequestId: {}.", waitTime, requestId);
            }
            return false;
        }

        this.waitTime = waitTime;
        bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_WAIT_MINUTE, (long)waitTime);
        if (logger.isDebugEnabled()) {
            logger.debug("Valve wait time is set to {}. RequestId: {}.", waitTime, requestId);
        }

        if (BellowsConstants.BLOW_STAGE_RUN.equals(stage)) {
            //处在鼓风中状态，需要修改下次鼓风时间
            int interval = waitTime - old;
            setNextBlowTime(nextBlowTime + interval * DateUtils.MILLIS_PER_MINUTE, requestId);
        }

        return true;
    }

    /**
     * 设置每组运行时间
     * @param runTime
     * @param requestId
     * @return  true为修改，false为不需要修改
     */
    private boolean setRunTime(int runTime, String requestId) {
        int old = this.runTime;
        if (runTime == old) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve run time is already {}. RequestId: {}.", runTime, requestId);
            }
            return false;
        }

        this.runTime = runTime;
        bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_RUN_MINUTE, (long)runTime);
        if (logger.isDebugEnabled()) {
            logger.debug("Valve run time is set to {}. RequestId: {}.", runTime, requestId);
        }

        if (runTime == 0) {
            setNextBlowTime(null, requestId);
        }
        return true;
    }


    /**
     * 操作阀门
     * @param thingCode
     * @param operation
     * @param operationType
     * @param requestId
     * @return
     */
    public int operateValve(String thingCode, EnumValveOperation operation, String operationType, String requestId) {
        Valve valve = getValveFromCache(thingCode);
        if (valve == null) {
            logger.warn("Valve: {} not found. RequestId: {}", thingCode, requestId);
            throw new SysException("阀门" + thingCode + "不存在", SysException.EC_UNKNOWN);
        }

        if (valve.isIntelligent() && BellowsConstants.TYPE_MANUAL.equals(operationType)) {
            logger.info("Valve {} in intelligent mode, cannot be operated manually.", thingCode, requestId);
            String error = "智能模式下无法手动操作";
            valveLogManager.saveFullLog(valve, operation, operationType, requestId, error);
            throw new SysException(error, SysException.EC_UNKNOWN);
        }

        //刷新状态
        valve.refresh(dataService);

        //阀门状态判断
        if (!validateValveState(valve, operation, dataService)) {
            logger.info("Valve {} is already {}. RequestId: {}.", thingCode, operation.toString(), requestId);
            String error;
            if (operation.equals(EnumValveOperation.OPEN)) {
                error = valve.getName() + "已处于开启状态";
            } else {
                error = valve.getName() + "已处于关闭状态";
            }
            valveLogManager.saveFullLog(valve, operation, operationType, requestId, error);
            throw new SysException(error, SysException.EC_UNKNOWN);
        }

        //介质桶状态判断
        if (!validateBucket(valve, dataService, speedLimit)) {
            logger.info("Valve {} bucket state is open. RequestId: {}.", thingCode, requestId);
            String error = valve.getName() + "介质桶处于开启状态，无法操作";
            valveLogManager.saveFullLog(valve, operation, operationType, requestId, error);
            throw new SysException(error, SysException.EC_UNKNOWN);
        }


        //组装发送信号
        DataModel signal = new DataModel();
        signal.setThingCode(thingCode);
        signal.setMetricCode(operation.getMetricCode());
        signal.setValue(BellowsConstants.TRUE);

        if (logger.isDebugEnabled()) {
            logger.debug("RequestId: {} send command: {} to valve: {}.Operation type is {}", requestId, operation, thingCode, operationType);
        }
        CmdControlService.CmdSendResponseData responseData = null;
        try {
            responseData = cmdControlService.sendCmd(signal, requestId);
            if (responseData.getOkCount() == 0) {
                throw new SysException(responseData.getErrorMessage(), SysException.EC_CMD_FAILED);
            }
        } catch (SysException e) {
            logger.warn(e.getMessage());
            valveLogManager.saveFullLog(valve, operation, operationType, requestId, e.getMessage());
            throw e;
        }

        //保存日志
        valveLogManager.saveLog(valve, operation, operationType, requestId);

        return responseData.getOkCount();
    }

    /**
     * 验证阀门状态
     * @param valve
     * @param operation
     * @param dataService
     * @return
     */
    private boolean validateValveState(Valve valve, EnumValveOperation operation, DataService dataService) {
        valve.refresh(dataService);

        //异或操作，不同返回true
        if (EnumValveOperation.OPEN.equals(operation) && EnumValveState.OPEN.getState().equals(valve.getState())) {
            return false;
        } else if (EnumValveOperation.CLOSE.equals(operation) && EnumValveState.CLOSE.getState().equals(valve.getState())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证介质桶状态
     * @return
     */
    private boolean validateBucket(Valve valve, DataService dataService, double speedLimit) {
        String bucketThingCode = valve.getBucketThingCode();
        boolean bucketOpen = Boolean.parseBoolean(BellowsUtil.getDataModelValue(dataService, bucketThingCode, ValveMetricConstants.BUCKET_STATE).orElse(BellowsConstants.FALSE));
        if (bucketOpen) {
            if (BellowsConstants.VALVE_TYPE_LUMP.equals(valve.getType())) {
                //块煤系统，只判断介质桶开关
                return false;
            } else {
                //末煤系统，判断泵频率
                String pumpThingCode = valve.getPumpThingCode();
                double pumpSpeed = Double.parseDouble(BellowsUtil.getDataModelValue(dataService, pumpThingCode, ValveMetricConstants.PUMP_SPEED).orElse("0"));
                if (pumpSpeed > speedLimit) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 批量操作阀门
     * @param thingCodes
     * @param operation
     * @param operationType
     * @param requestId
     * @return
     */
    public synchronized int operateValveBatch(List<String> thingCodes, EnumValveOperation operation, String operationType, String requestId) {
        if (thingCodes == null || thingCodes.isEmpty()) {
            logger.warn("Valve thing code list is empty. RequestId: {}.", requestId);
            return 0;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("RequestId: {} send command: {} to valve: {}.Operation type is {}", requestId, operation, thingCodes, operationType);
        }

        int okCount = 0;
        for (String thingCode : thingCodes) {
            int count = 0;
            try {
                count = operateValve(thingCode, operation, operationType, requestId);
            } catch (SysException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Valve {} operate fail. RequestId: {}.", thingCode, requestId);
                }
            }
            okCount += count;
        }
        return okCount;
    }


    /**
     * 操作所有阀门
     * @param operation
     * @param operationType
     * @param requestId
     * @return
     */
    public synchronized int operateValveAll(EnumValveOperation operation, String operationType, String requestId) {
        if (logger.isDebugEnabled()) {
            logger.debug("RequestId: {} send command: {} to all valves.Operation type is {}", requestId, operation, operationType);
        }

        int okCount = 0;
        List<Valve> valves = valveCache.findAll();
        for (Valve valve : valves) {
            if (!validateValveState(valve, operation, dataService)) {
                //操作与阀门状态相同，不进行操作
                continue;
            }
            int count = 0;
            try {
                count = operateValve(valve.getThingCode(), operation, operationType, requestId);
            } catch (SysException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Valve {} operate fail. RequestId: {}.", valve.getThingCode(), requestId);
                }
            }
            okCount += count;
        }
        return okCount;
    }


    /**
     * 开始循环
     * @param changed   是否需要重新分组
     * @param requestId
     */
    private void startLoop(boolean changed, String requestId) {
        if (changed) {
            execNewLoop(requestId);
        } else {
            execOldLoopAgain(requestId);
        }
    }

    /**
     * 执行新分组策略
     * @param requestId
     */
    private void execNewLoop(String requestId) {
        onChanged(false, requestId);

        //将之前的分组删除
        bellowsMapper.deleteValveTeamByStatus(BellowsConstants.VALVE_STATUS_EXECUTED);

        //检查参数
        if (runTime == 0 || maxCount == 0) {
            lumpTeamCount = 0;
            slackTeamCount = 0;
            stage = BellowsConstants.BLOW_STAGE_NONE;
            setNextBlowTime(null, requestId);

            //清除阀门分组状态
            List<Valve> valves = valveCache.findAll();
            for (Valve valve : valves) {
                valve.setTeamId(null, bellowsMapper, requestId);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Valve intelligent runtime or maxItemCount is 0, cannot start loop. RequestId: {}.", requestId);
            }
            return;
        }

        //分组
        ValveTeamResult teamResult = generateTeam(valveCache.findAll(), maxCount, waitTime, runTime, maxTeamId);
        lumpTeamCount = teamResult.getLumpTeamCount();
        slackTeamCount = teamResult.getSlackTeamCount();
        maxTeamId = teamResult.getMaxTeamId();

        List<ValveTeam> teams = teamResult.getTeams();
        if (teams == null || teams.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve team list is empty, cannot start loop. RequestId: {}.", requestId);
            }
            stage = BellowsConstants.BLOW_STAGE_NONE;
            setNextBlowTime(null, requestId);
            return;
        }
        stage = BellowsConstants.BLOW_STAGE_WAIT;

        //设置执行时间
        for (int i=0,length=teams.size();i<length;i++) {
            ValveTeam team = teams.get(i);
            if (i == 0) {
                Date execTime = DateUtils.truncate(new Date(new Date().getTime() + waitTime*DateUtils.MILLIS_PER_MINUTE), Calendar.MINUTE);
                team.setExecTime(execTime);
                setNextBlowTime(execTime.getTime(), requestId);

                setValveStageAndExecTime(team.getId(), BellowsConstants.BLOW_STAGE_WAIT, execTime);
            } else {
                setValveStageAndExecTime(team.getId(), BellowsConstants.BLOW_STAGE_WAIT, null);
            }
        }


        bellowsMapper.insertBatchValveTeam(teamResult.getTeams());

        //更新阀门teamId
        for (ValveTeam team : teams) {
            List<String> thingCodes = team.getValveThingCodes();
            for (String thingCode : thingCodes) {
                Valve valve = getValveFromCache(thingCode);
                valve.setTeamId(team.getId(), bellowsMapper, requestId);
            }
        }
    }

    /**
     * 再次执行旧分组策略
     * @param requestId
     */
    private void execOldLoopAgain(String requestId) {
        stage = BellowsConstants.BLOW_STAGE_WAIT;

        //将之前的分组设置为等待执行
        bellowsMapper.updateValveTeamByStatus(BellowsConstants.VALVE_STATUS_WAIT, BellowsConstants.VALVE_STATUS_EXECUTED);

        //修改执行时间
        List<ValveTeam> teams = bellowsMapper.getValveTeamByStatus(BellowsConstants.VALVE_STATUS_WAIT);
        if (teams == null || teams.isEmpty()) {
            logger.warn("Old valve loop is empty. RequestId: {}.", requestId);
            return;
        }

        for (int i=0,length=teams.size();i<length;i++) {
            ValveTeam team = teams.get(i);
            if (i == 0) {
                Date execTime = DateUtils.truncate(new Date(new Date().getTime() + waitTime*DateUtils.MILLIS_PER_MINUTE), Calendar.MINUTE);
                team.setExecTime(execTime);
                setNextBlowTime(execTime.getTime(),requestId);
                bellowsMapper.updateValveTeam(team);

                setValveStageAndExecTime(team.getId(), BellowsConstants.BLOW_STAGE_WAIT, execTime);
            } else {
                setValveStageAndExecTime(team.getId(), BellowsConstants.BLOW_STAGE_WAIT, null);
            }
        }

    }


    /**
     * 检查循环（定时任务）
     */
    public synchronized void checkLoop() {
        String requestId = RequestIdUtil.generateRequestId();
        if (logger.isDebugEnabled()) {
            logger.debug("Check valve loop. RequestId: {}.", requestId);
        }

        ValveTeam team = bellowsMapper.getValveTeamToExec(new Date());
        if (team == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No valve team is found. RequestId: {}.", requestId);
            }
            return;
        }

        if (BellowsConstants.VALVE_STATUS_EXECUTED.equals(team.getStatus())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve team {} status is {}, need not to execute again. RequestId: {}.", team.getId(), team.getStatus(), requestId);
            }
            return;
        }

        if (BellowsConstants.VALVE_STATUS_RUNNING.equals(team.getStatus())) {
            //运行中，需要关闭
            closeValveTeam(team, requestId);
        } else if (BellowsConstants.VALVE_STATUS_WAIT.equals(team.getStatus())) {
            //等待中，需要开启
            runValveTeam(team, requestId);
        } else {
            logger.warn("Valve team {} has wrong status {}. RequestId: {}.", team.getId(), team.getStatus(), requestId);
        }
    }

    /**
     * 打开等待中的分组
     * @param team
     * @param requestId
     */
    private void runValveTeam(ValveTeam team, String requestId) {
        stage = BellowsConstants.BLOW_STAGE_RUN;

        autoOpenValveTeam(team.getId(), team.getDuration(), requestId);

        //修改下次鼓风时间
        int teamCount = lumpTeamCount + slackTeamCount;
        long nextTimeStamp = DateUtils.truncate(new Date(), Calendar.MINUTE).getTime() + teamCount * runTime * DateUtils.MILLIS_PER_MINUTE + waitTime * DateUtils.MILLIS_PER_MINUTE;
        setNextBlowTime(nextTimeStamp, requestId);
    }

    /**
     * 关闭运行的分组
     * @param team
     * @param requestId
     */
    private void closeValveTeam(ValveTeam team, String requestId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Valve team {} status is {}, will be closed. RequestId: {}.", team.getId(), team.getStatus(), requestId);
        }
        List<String> runningValves = valveCache.findThingCodeByTeam(team.getId());
        operateValveBatch(runningValves, EnumValveOperation.CLOSE, BellowsConstants.TYPE_AUTO, requestId);

        //更新当前组
        team.setStatus(BellowsConstants.VALVE_STATUS_EXECUTED);
        team.setExecTime(null);
        bellowsMapper.updateValveTeam(team);

        setValveStageAndExecTime(team.getId(), BellowsConstants.BLOW_STAGE_NONE, null);

        if (team.getNextId() == null) {
            //没有下一组，重新开始计算
            startLoop(changed, requestId);
        } else {
            //有下一组，执行
            autoOpenValveTeam(team.getNextId(), team.getDuration(), requestId);
        }
    }

    /**
     * 执行分组开启
     * @param teamId
     * @param duration
     * @param requestId
     */
    private void autoOpenValveTeam(Long teamId, int duration, String requestId) {
        List<String> thingCodes = valveCache.findThingCodeByTeam(teamId);
        operateValveBatch(thingCodes, EnumValveOperation.OPEN, BellowsConstants.TYPE_AUTO, requestId);

        //更新数据库
        ValveTeam team = bellowsMapper.getValveTeamById(teamId);
        team.setExecTime(DateUtils.truncate(new Date(new Date().getTime() + duration*DateUtils.MILLIS_PER_MINUTE), Calendar.MINUTE));
        team.setStatus(BellowsConstants.VALVE_STATUS_RUNNING);
        bellowsMapper.updateValveTeam(team);

        //更新阀门状态和执行时间
        setValveStageAndExecTime(teamId, BellowsConstants.BLOW_STAGE_RUN, null);
        if (team.getNextId() != null) {
            setValveStageAndExecTime(team.getNextId(), BellowsConstants.BLOW_STAGE_WAIT, team.getExecTime());
        }
    }


    /**
     * 修改保存下次鼓风时间
     * @param time
     */
    private void setNextBlowTime(Long time, String requestId) {
        nextBlowTime = time;

        if (logger.isDebugEnabled()) {
            logger.debug("Valve next blow time is set to {}.RequestId: {}.", nextBlowTime, requestId);
        }
    }


    /**
     * 修改changed
     * @param changed
     * @param requestId
     */
    private void onChanged(boolean changed, String requestId) {
        if (changed == this.changed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve changed is already {}. RequestId: {}.", changed, requestId);
            }
            return;
        }

        //当前没有智能鼓风，立即开始智能鼓风
        if (changed && BellowsConstants.BLOW_STAGE_NONE.equals(stage)) {
            startLoop(true, requestId);
            return;
        }


        this.changed = changed;
        long value = (long)BellowsConstants.NO;
        if (changed) {
            value = (long)BellowsConstants.YES;
        }
        bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.VALVE_CHANGED, value);
        if (logger.isDebugEnabled()) {
            logger.debug("Valve changed is set {}. RequestId: {}.", changed, requestId);
        }
    }


    /**
     * 生成智能鼓风分组
     * @param valves
     * @param maxCount
     * @param waitTime
     * @param runTime
     * @param maxTeamId
     * @return
     */
    private ValveTeamResult generateTeam(List<Valve> valves, int maxCount, int waitTime, int runTime, long maxTeamId) {
        //块煤分组数
        int lumpTeamCount = 0;
        //末煤分组数
        int slackTeamCount = 0;

        //分组阀门thingCode
        List<String> thingCodes = new ArrayList<>(maxCount);

        boolean lumpTeam = true;

        //分组列表
        List<ValveTeam> teams = new ArrayList<>();

        for (Valve valve : valves) {
            if (!valve.isIntelligent()) {
                continue;
            }

            if (valve.getType().equals(BellowsConstants.VALVE_TYPE_LUMP)) {
                //块煤
                //分组已满，新建一个分组
                if (thingCodes.size() == maxCount) {
                    maxTeamId++;
                    ValveTeam team = new ValveTeam(maxTeamId, maxTeamId+1, BellowsConstants.VALVE_STATUS_WAIT, null, runTime, BellowsConstants.VALVE_TEAM_LUMP, thingCodes);
                    teams.add(team);
                    thingCodes = new ArrayList<>(maxCount);
                    lumpTeamCount++;
                }
                //添加thingCode
                thingCodes.add(valve.getThingCode());
            } else {
                //末煤
                if (lumpTeam && !thingCodes.isEmpty()) {
                    //上一组是块煤
                    maxTeamId++;
                    ValveTeam team = new ValveTeam(maxTeamId, maxTeamId+1, BellowsConstants.VALVE_STATUS_WAIT, null, runTime, BellowsConstants.VALVE_TEAM_LUMP, thingCodes);
                    teams.add(team);
                    thingCodes = new ArrayList<>(maxCount);
                    lumpTeamCount++;
                }
                lumpTeam = false;
                //分组已满，新建一个分组
                if (thingCodes.size() == maxCount) {
                    maxTeamId++;
                    ValveTeam team = new ValveTeam(maxTeamId, maxTeamId+1, BellowsConstants.VALVE_STATUS_WAIT, null, runTime, BellowsConstants.VALVE_TEAM_SLACK, thingCodes);
                    teams.add(team);
                    thingCodes = new ArrayList<>(maxCount);
                    slackTeamCount++;
                }
                //添加thingCode
                thingCodes.add(valve.getThingCode());
            }
        }

        //添加最后一组
        if (!thingCodes.isEmpty()) {
            maxTeamId++;
            if (lumpTeam) {
                //块煤
                ValveTeam team = new ValveTeam(maxTeamId, null, BellowsConstants.VALVE_STATUS_WAIT, null, runTime, BellowsConstants.VALVE_TEAM_LUMP, thingCodes);
                teams.add(team);
                lumpTeamCount++;
            } else {
                //末煤
                ValveTeam team = new ValveTeam(maxTeamId, null, BellowsConstants.VALVE_STATUS_WAIT, null, runTime, BellowsConstants.VALVE_TEAM_SLACK, thingCodes);
                teams.add(team);
                slackTeamCount++;
            }
        }

        ValveTeamResult result = new ValveTeamResult();
        result.setLumpTeamCount(lumpTeamCount);
        result.setSlackTeamCount(slackTeamCount);
        result.setMaxTeamId(maxTeamId);
        result.setTeams(teams);
        return result;
    }


    /**
     * 从缓存中获取空压机实例
     * @param thingCode
     * @return
     */
    private Valve getValveFromCache(String thingCode) {
        synchronized (cacheLock) {
            //等待空压机缓存初始化
        }
        Valve valve = valveCache.findByThingCode(thingCode);
        return valve;
    }

    /**
     * 获取阀门日志
     * @param startTime
     * @param endTime
     * @param page
     * @param count
     * @param requestId
     * @return
     */
    public List<ValveLog> getValveLog(Date startTime, Date endTime, Integer page, Integer count, String requestId) {
        return valveLogManager.getValveLog(startTime, endTime, page, count, requestId);
    }

    public Long getNextBlowTime() {
        return nextBlowTime;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getRunTime() {
        return runTime;
    }

    public boolean isChanged() {
        return changed;
    }

    public Long getMaxTeamId() {
        return maxTeamId;
    }

    public void setMaxTeamId(Long maxTeamId) {
        this.maxTeamId = maxTeamId;
    }

    public int getLumpTeamCount() {
        return lumpTeamCount;
    }

    public void setLumpTeamCount(int lumpTeamCount) {
        this.lumpTeamCount = lumpTeamCount;
    }

    public int getSlackTeamCount() {
        return slackTeamCount;
    }

    public void setSlackTeamCount(int slackTeamCount) {
        this.slackTeamCount = slackTeamCount;
    }
}
