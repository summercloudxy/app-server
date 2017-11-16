package com.zgiot.app.server.module.bellows.compressor;

import com.alibaba.fastjson.annotation.JSONField;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
import com.zgiot.app.server.module.bellows.pressure.PressureManager;
import com.zgiot.app.server.module.bellows.util.BellowsUtil;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.BellowsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wangwei
 */
public class CompressorGroup {

    private static final Logger logger = LoggerFactory.getLogger(CompressorGroup.class);

    /**
     * 空压机列表
     */
    @JSONField(serialize = false)
    private final List<Compressor> compressors;


    /**
     * 类型
     */
    @JSONField(serialize = false)
    private final String type;

    @JSONField(serialize = false)
    private final PressureManager pressureManager;

    /**
     * 管道压力
     */
    private volatile double pressure;

    /**
     * 运行中数量
     */
    private volatile int runningCount;

    /**
     * 总数量
     */
    private volatile int totalCount;

    /**
     * 故障数量
     */
    private volatile int errorCount;

    /**
     * 错误列表
     */
    private volatile List<String> errors;

    public CompressorGroup(List<Compressor> compressors, String type, PressureManager pressureManager) {
        this.compressors = compressors;
        this.type = type;
        this.pressureManager = pressureManager;
    }

    /**
     * 数据刷新
     * @param dataService
     * @return
     */
    public synchronized CompressorGroup refresh(DataService dataService, String requestId) {
        //数量刷新
        totalCount = compressors.size();
        errors = new ArrayList<>();
        int runningCount = 0;
        int errorCount = 0;

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor group {} refresh. RequestId: {}.", type, requestId);
        }

        for (Compressor compressor : compressors) {
            if (EnumCompressorState.RUNNING.getState().equals(compressor.getState())) {
                runningCount++;
            } else if (EnumCompressorState.ERROR.getState().equals(compressor.getState())) {
                errorCount++;
                //错误信息获取
                compressor.refresh(dataService);
                for (String error : compressor.getErrors()) {
                    errors.add(compressor.getThingCode() + error);
                }
            }
        }

        this.runningCount = runningCount;
        this.errorCount = errorCount;

        //管道压力刷新
        refreshPressure(dataService, requestId);

        return this;
    }

    /**
     * 管道压力刷新
     * @param dataService
     * @param requestId
     * @return
     */
    public CompressorGroup refreshPressure(DataService dataService, String requestId) {
        //压力刷新
        pressure = pressureManager.refreshPressure(type, dataService, requestId);

        if (logger.isDebugEnabled()) {
            logger.debug("Compressor group {} refresh pressure {}. RequestId: {}.", type, pressure, requestId);
        }

        return this;
    }



    public List<Compressor> getCompressors() {
        return compressors;
    }

    public String getType() {
        return type;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(int runningCount) {
        this.runningCount = runningCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
