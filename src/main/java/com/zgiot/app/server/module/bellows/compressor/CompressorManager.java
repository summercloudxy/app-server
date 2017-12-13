package com.zgiot.app.server.module.bellows.compressor;

import com.zgiot.app.server.module.bellows.compressor.cache.CompressorCache;
import com.zgiot.app.server.module.bellows.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.pojo.CompressorState;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.pressure.PressureManager;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.CompressorMetricConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author wangwei
 */
@Component
public class CompressorManager {

    private static final Logger logger = LoggerFactory.getLogger(CompressorManager.class);

    private Object cacheLock = new Object();

    private volatile boolean initial = false;


    @Autowired
    private CompressorCache compressorCache;
    @Autowired
    private DataService dataService;
    @Autowired
    private BellowsMapper bellowsMapper;
    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private PressureManager pressureManager;

    /**
     * 低压空压机组
     */
    private CompressorGroup low;

    /**
     * 高压空压机组
     */
    private CompressorGroup high;



    @PostConstruct
    public void init() {
        //初始化空压机缓存
        synchronized (cacheLock) {
            if (initial) {
                return;
            }

            compressorCache.put("2510",
                    new Compressor("2510", "2510", BellowsConstants.CP_TYPE_LOW, 0, dataService, pressureManager, cmdControlService, bellowsMapper)
                            .initState());
            compressorCache.put("2511",
                    new Compressor("2511", "2511", BellowsConstants.CP_TYPE_LOW, 1, dataService, pressureManager, cmdControlService, bellowsMapper)
                            .initState());
            compressorCache.put("2512",
                    new Compressor("2512", "2512", BellowsConstants.CP_TYPE_LOW, 2, dataService, pressureManager, cmdControlService, bellowsMapper)
                            .initState());
            compressorCache.put("2530",
                    new Compressor("2530", "2530", BellowsConstants.CP_TYPE_HIGH, 0, dataService, pressureManager, cmdControlService, bellowsMapper)
                            .initState());
            compressorCache.put("2531",
                    new Compressor("2531", "2531", BellowsConstants.CP_TYPE_HIGH, 1, dataService, pressureManager, cmdControlService, bellowsMapper)
                            .initState());
            compressorCache.put("2532",
                    new Compressor("2532", "2532", BellowsConstants.CP_TYPE_HIGH, 2, dataService, pressureManager, cmdControlService, bellowsMapper)
                            .initState());

            high = new CompressorGroup(compressorCache.findByType(BellowsConstants.CP_TYPE_HIGH), BellowsConstants.CP_TYPE_HIGH, pressureManager, dataService, bellowsMapper).init();
            low = new CompressorGroup(compressorCache.findByType(BellowsConstants.CP_TYPE_LOW), BellowsConstants.CP_TYPE_LOW, pressureManager, dataService, bellowsMapper).init();

            initial = true;
        }
    }

    /**
     * 根据参数获取空压机分组
     * @param type
     * @return
     */
    private CompressorGroup getGroupByType(String type) {
        if (BellowsConstants.CP_TYPE_LOW.equals(type)) {
            return low;
        } else {
            return high;
        }
    }


    public void onDataSourceChange(DataModel data) {
        String requestId = RequestIdUtil.generateRequestId();
        if (logger.isTraceEnabled()) {
            logger.trace("Got data: {}.RequestId: {}.", data, requestId);
        }

        String metricCode = data.getMetricCode();
        Compressor compressor = getCompressorFromCache(data.getThingCode());
        CompressorGroup group = getGroupByType(compressor.getType());

        switch (metricCode) {
            case CompressorMetricConstants.RUN_STATE:
                group.onRunStateChange(compressor, data.getValue(), requestId);
                break;
            case CompressorMetricConstants.LOAD_STATE:
                group.onLoadStateChange(compressor, data.getValue(), requestId);
                break;
            case CompressorMetricConstants.WARN:
                group.onWarnStateChange(compressor, data.getValue(), requestId);
                break;
            case CompressorMetricConstants.ERROR:
                group.onErrorStateChange(compressor, data.getValue(), requestId);
                break;
            case CompressorMetricConstants.LOCAL:
                group.onLocalChange(compressor, data.getValue(), requestId);
                break;
            default:
                logger.warn("Got wrong metric code: {}.RequestId: {}.", metricCode, requestId);
        }
    }

    /**
     * 压力状态变化
     * @param data
     */
    public void onPressureStateChange(DataModel data) {
        String requestId = RequestIdUtil.generateRequestId();
        if (logger.isTraceEnabled()) {
            logger.trace("Got data: {}.RequestId: {}.", data, requestId);
        }

        low.setPressureState(Double.parseDouble(data.getValue()), requestId);
    }



    /**
     * 设置低压空压机智能模式
     * @param type  high/low
     * @param intelligent   0:否 1:是
     * @param requestId
     */
    public synchronized void changeGroupIntelligent(String type, boolean intelligent, String requestId) {
        if (BellowsConstants.CP_TYPE_LOW.equals(type)) {
            low.setIntelligent(intelligent, requestId);
        } else {
            high.setIntelligent(intelligent, requestId);
        }
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

        int count = compressor.operate(operation, operationType, requestId);
        return count;
    }


    /**
     * 分页获取空压机日志
     * @param startTime
     * @param endTime
     * @param lastId 上次查询的最后一个id（null为首次查询）
     * @param count
     * @return
     */
    public List<CompressorLog> getCompressorLog(Date startTime, Date endTime, Long lastId, Integer count, String requestId) {
        return bellowsMapper.getCompressorLog(startTime, endTime, lastId, count);
    }


    /**
     * 获取时间范围内空压机状态列表（如果不存在，再去寻找之后一条或之前一条）
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param thingCodes    thingCode列表
     * @param requestId
     * @return
     */
    private Map<String, List<CompressorState>> getCompressorState(Date startTime, Date endTime, List<String> thingCodes, String requestId) {
        Map<String, List<CompressorState>> result = new HashMap<>(thingCodes.size());
        for (String thingCode : thingCodes) {
            result.put(thingCode, new ArrayList<>());
        }

        //获取所有时间段内状态
        List<CompressorState> allList = bellowsMapper.getCompressorState(startTime, endTime, thingCodes, true,null, null);

        if (!CollectionUtils.isEmpty(allList)) {
            for (CompressorState state : allList) {
                List<CompressorState> list = result.get(state.getThingCode());
                list.add(state);
            }
        }

        result.forEach((thingCode, list) -> {
            if (list.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Compressor {} cannot get state in startTime {}, endTime {}.RequestId: {}.", thingCode, startTime, endTime, requestId);
                }

                //查找后一条记录
                List<CompressorState> postState = bellowsMapper.getCompressorState(endTime, null, Arrays.asList(thingCode), true, 0 ,1);
                if (!CollectionUtils.isEmpty(postState)) {
                    list.add(postState.get(0));
                } else {
                    //查找前一条记录
                    List<CompressorState> preState = bellowsMapper.getCompressorState(null, startTime, Arrays.asList(thingCode), false, 0, 1);
                    if (!CollectionUtils.isEmpty(preState)) {
                        list.add(preState.get(0));
                    }
                }
            }
        });

        return result;
    }



    /**
     * 分析低压空压机状态
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Map<String, Long>> analyseCompressorState(Date startTime, Date endTime, String requestId) {
        //保存结果
        Map<String, Map<String, Long>> result = new HashMap<>(low.getCompressors().size());

        //记录thingCode对应上一条state
        Map<String, CompressorState> lastStateMap = new HashMap<>(low.getCompressors().size());

        List<String> thingCodes = new ArrayList<>(low.getCompressors().size());
        for (Compressor compressor : low.getCompressors()) {
            thingCodes.add(compressor.getThingCode());
            result.put(compressor.getThingCode(), new HashMap<>());
            lastStateMap.put(compressor.getThingCode(), null);
        }

        //数据库查询
        Map<String, List<CompressorState>> map = getCompressorState(startTime, endTime, thingCodes, requestId);

        map.forEach((thingCode, list)-> {
            if (list.isEmpty()) {
                return;
            }

            Map<String, Long> stateMap = result.get(thingCode);
            //遍历state
            for (int i = 0, length = list.size(); i < length; i++) {
                CompressorState state = list.get(i);

                if (state.getTime().before(startTime)) {
                    //状态在时间段之前，时间段内没有值
                    stateMap.put(state.getPostState(), endTime.getTime() - startTime.getTime());
                } else if (state.getTime().after(endTime)) {
                    //状态在时间段之后，时间段内没有值
                    stateMap.put(state.getPreState(), endTime.getTime() - startTime.getTime());
                } else {
                    //保存的上次状态
                    CompressorState lastState = lastStateMap.get(thingCode);
                    //计算状态时长
                    long duration;
                    if (lastState == null) {
                        duration = state.getTime().getTime() - startTime.getTime();
                    } else {
                        duration = state.getTime().getTime() - lastState.getTime().getTime();
                    }

                    //保存该状态总时长
                    Long time = stateMap.get(state.getPreState());
                    if (time == null) {
                        time = duration;
                    } else {
                        time += duration;
                    }
                    stateMap.put(state.getPreState(), time);

                    if (i == length - 1) {
                        //最后一条记录，保存postState时长
                        time = stateMap.get(state.getPostState());
                        duration = endTime.getTime() - state.getTime().getTime();
                        if (time == null) {
                            time = duration;
                        } else {
                            time += duration;
                        }
                        stateMap.put(state.getPostState(), time);
                    } else {
                        //保存此次状态
                        lastStateMap.put(thingCode, state);
                    }
                }
            }
        });

        return result;
    }

    /**
     * 获取空压机状态时间线
     * @param startTime
     * @param endTime
     * @param requestId
     * @return
     */
    public Map<String, List<Map<String, Long>>> getCompressorTimeline(Date startTime, Date endTime, String requestId) {
        //保存结果
        Map<String, List<Map<String, Long>>> result = new HashMap<>(low.getCompressors().size());

        //记录thingCode对应上一条state
        Map<String, CompressorState> lastStateMap = new HashMap<>(low.getCompressors().size());

        List<String> thingCodes = new ArrayList<>(low.getCompressors().size());
        for (Compressor compressor : low.getCompressors()) {
            thingCodes.add(compressor.getThingCode());
            result.put(compressor.getThingCode(), new ArrayList<>());
            lastStateMap.put(compressor.getThingCode(), null);
        }

        //数据库查询
        Map<String, List<CompressorState>> map = getCompressorState(startTime, endTime, thingCodes, requestId);

        map.forEach((thingCode, list)-> {
            if (list.isEmpty()) {
                return;
            }

            List<Map<String, Long>> timelineList = result.get(thingCode);
            //遍历state
            for (int i = 0, length = list.size(); i < length; i++) {
                CompressorState state = list.get(i);

                if (state.getTime().before(startTime)) {
                    //状态在时间段之前，时间段内没有值
                    Map<String, Long> timeline = new HashMap<>(1);
                    timeline.put(state.getPostState(), endTime.getTime() - startTime.getTime());
                    timelineList.add(timeline);
                } else if (state.getTime().after(endTime)) {
                    //状态在时间段之后，时间段内没有值
                    Map<String, Long> timeline = new HashMap<>(1);
                    timeline.put(state.getPreState(), endTime.getTime() - startTime.getTime());
                    timelineList.add(timeline);
                } else {
                    //保存的上次状态
                    CompressorState lastState = lastStateMap.get(thingCode);
                    //计算状态时长
                    long duration;
                    if (lastState == null) {
                        duration = state.getTime().getTime() - startTime.getTime();
                    } else {
                        duration = state.getTime().getTime() - lastState.getTime().getTime();
                    }

                    //保存该状态总时长
                    Map<String, Long> timeline = new HashMap<>(1);
                    timeline.put(state.getPreState(), duration);
                    timelineList.add(timeline);

                    if (i == length - 1) {
                        //最后一条记录，保存postState时长
                        timeline = new HashMap<>(1);
                        timeline.put(state.getPostState(), endTime.getTime() - state.getTime().getTime());
                        timelineList.add(timeline);
                    } else {
                        //保存此次状态
                        lastStateMap.put(thingCode, state);
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
    public CompressorGroup refreshGroup(String type, String requestId) {
        if (BellowsConstants.CP_TYPE_HIGH.equals(type)) {
            return high.refresh(dataService, requestId);
        } else if (BellowsConstants.CP_TYPE_LOW.equals(type)) {
            return low.refresh(dataService, requestId);
        } else {
            logger.warn("Compressor group type {} is wrong. RequestId: {}.", type, requestId);
            return null;
        }
    }


    /**
     * 获取空压机压力
     * @param type 类型(low/high)
     * @param requestId
     * @return
     */
    public Double refreshPressure(String type, String requestId) {
        return pressureManager.refreshPressure(type, dataService, requestId);
    }


    /**
     * 从缓存中获取空压机实例
     * @param thingCode
     * @return
     */
    private Compressor getCompressorFromCache(String thingCode) {
        checkInit();

        Compressor compressor = compressorCache.findByThingCode(thingCode);
        return compressor;
    }

    /**
     * 检测thingCode是否存在
     * @param thingCode
     * @return
     */
    public boolean containCompressor(String thingCode) {
        checkInit();

        if (compressorCache.findByThingCode(thingCode) == null) {
            return false;
        }
        return true;
    }

    private void checkInit() {
        synchronized (cacheLock) {
            //等待空压机缓存初始化
            if (!initial) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Compressor manager is not initial, start to init.");
                }
                init();
            }
        }
    }
}
