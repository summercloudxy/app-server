package com.zgiot.app.server.module.bellows.compressor;

import com.zgiot.app.server.module.bellows.compressor.cache.CompressorCache;
import com.zgiot.app.server.module.bellows.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.pojo.CompressorState;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
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

        if (!CollectionUtils.isEmpty(list)) {
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
                if (!CollectionUtils.isEmpty(lastState)) {
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
     * 获取空压机错误列表
     * @param requestId
     * @return
     */
    public List<String> getErrors(String requestId) {
        List<String> res = new ArrayList<>();
        high.refresh(dataService, requestId);
        if (!CollectionUtils.isEmpty(high.getErrors())) {
            res.addAll(high.getErrors());
        }

        low.refresh(dataService, requestId);
        if (!CollectionUtils.isEmpty(low.getErrors())) {
            res.addAll(low.getErrors());
        }
        return res;
    }

    /**
     * 获取空压机压力
     * @param requestId
     * @return
     */
    public Map<String, Double> getPressure(String requestId) {
        Map<String, Double> res = new HashMap<>();
        res.put(BellowsConstants.CP_TYPE_LOW, pressureManager.refreshPressure(BellowsConstants.CP_TYPE_LOW, dataService, requestId));
        res.put(BellowsConstants.CP_TYPE_HIGH, pressureManager.refreshPressure(BellowsConstants.CP_TYPE_HIGH, dataService, requestId));

        return res;
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
