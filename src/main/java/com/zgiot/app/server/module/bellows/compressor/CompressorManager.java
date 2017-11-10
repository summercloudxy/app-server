package com.zgiot.app.server.module.bellows.compressor;

import com.zgiot.app.server.module.bellows.compressor.cache.CompressorCache;
import com.zgiot.app.server.module.bellows.compressor.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
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

import javax.annotation.PostConstruct;
import java.util.*;

import static com.zgiot.app.server.module.bellows.compressor.Compressor.YES;

/**
 * @author wangwei
 */
@Component
public class CompressorManager {

    private static final Logger logger = LoggerFactory.getLogger(CompressorManager.class);

    private Object cacheLock = new Object();

    /**
     * 脉冲指令清除时间
     */
    private static final int CLEAN_PERIOD = 500;




    @Autowired
    private CompressorCache compressorCache;
    @Autowired
    private DataService dataService;
    @Autowired
    private BellowsMapper bellowsMapper;
    @Autowired
    private CmdControlService cmdControlService;


    /**
     * 低压空压机智能
     */
    private volatile boolean intelligent;


    @PostConstruct
    public void init() {
        //初始化空压机缓存
        synchronized (cacheLock) {
            compressorCache.put("2510", new Compressor("2510", "2510", Compressor.TYPE_LOW, 0, this));
            compressorCache.put("2511", new Compressor("2511", "2511", Compressor.TYPE_LOW, 1, this));
            compressorCache.put("2512", new Compressor("2512", "2512", Compressor.TYPE_LOW, 2, this));
            compressorCache.put("2530", new Compressor("2530", "2530", Compressor.TYPE_HIGH, 0, this));
            compressorCache.put("2531", new Compressor("2531", "2531", Compressor.TYPE_HIGH, 1, this));
            compressorCache.put("2532", new Compressor("2532", "2532", Compressor.TYPE_HIGH, 2, this));
        }

        //初始化空压机智能
        intelligent = (bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT) == 1);
        logger.info("Low compressor initial intelligent state: {}", intelligent);
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
                compressor = getCompressorFromCache(data.getThingCode(), requestId);
                if (compressor == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} not found.", data.getThingCode());
                    }
                    return;
                }
                compressor.onRunStateChange(data);
                break;
            case CompressorMetricConstants.LOAD_STATE:
                compressor = getCompressorFromCache(data.getThingCode(), requestId);
                if (compressor == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} not found.", data.getThingCode());
                    }
                    return;
                }
                compressor.onLoadStateChange(data);
                break;
            case CompressorMetricConstants.REMOTE:
                compressor = getCompressorFromCache(data.getThingCode(), requestId);
                if (compressor == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Compressor {} not found.", data.getThingCode());
                    }
                    return;
                }
                compressor.onRemoteChange(data);
            case CompressorMetricConstants.PRESSURE_STATE:
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
        if (this.intelligent == (intelligent==YES)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Low compressor intelligent is already {}. RequestId: {}", this.intelligent, requestId);
            }
        }

        this.intelligent = (intelligent == YES);
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
        Compressor compressor = getCompressorFromCache(thingCode, requestId);
        if (compressor == null) {
            logger.warn("Compressor: {} not found.RequestId: {}", thingCode, requestId);
            throw new SysException("空压机" + thingCode + "不存在", SysException.EC_UNKNOWN);
        }

        //判断是否是远程状态
        if (compressor.getRemote() == Compressor.YES) {
            logger.warn("Compressor: {} is local, cannot be operated remotely.RequestId: {}", thingCode, requestId);
            throw new SysException("空压机" + thingCode + "处于就地模式，无法进行远程操作", SysException.EC_UNKNOWN);
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
        int count = cmdControlService.sendPulseCmdBoolByShort(signal, null, null, requestId, operation.getPosition(), CLEAN_PERIOD, holding);

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
    public void saveLog(Compressor compressor, EnumCompressorOperation operation, String operationType, String requestId) {
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
     * 更新日志确认状态
     * @param logId
     * @param thingCode
     */
    public void updateLog(Long logId, String thingCode, String requestId) {
        //获取最新状态
        Compressor compressor = getCompressorFromCache(thingCode, requestId);
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
     * 开启卸载关闭定时
     * @param compressor
     */
    public void turnOnStopTimer(Compressor compressor) {
        if (intelligent) {
            compressor.turnOnStopTimer();
        }
    }

    /**
     * 从缓存中获取空压机实例
     * @param thingCode
     * @param requestId
     * @return
     */
    private Compressor getCompressorFromCache(String thingCode, String requestId) {
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
        if (intelligent == false) {
            if (logger.isDebugEnabled()) {
                logger.debug("Low press compressor intelligent is false, cannot turn on stop timers.");
            }
            compressorCache.findByType(Compressor.TYPE_LOW).forEach(compressor -> {
                compressor.turnOnStopTimer();
            });
        }
    }

}
