package com.zgiot.app.server.module.bellows.compressor;

import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorState;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.CompressorMetricConstants;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final List<Compressor> compressors;

    /**
     * 压力检测设备列表
     */
    private final List<String> deviceThingCodes;


    private final CompressorManager manager;

    /**
     * 类型
     */
    private final String type;

    /**
     * 管道压力
     */
    private volatile double pressure;

    /**
     * 运行中数量
     */
    private int runningCount;

    /**
     * 总数量
     */
    private int totalCount;

    /**
     * 故障数量
     */
    private int errorCount;

    public CompressorGroup(List<Compressor> compressors, String type, List<String> deviceThingCodes, CompressorManager manager) {
        this.compressors = compressors;
        this.type = type;
        this.deviceThingCodes = deviceThingCodes;
        this.manager = manager;
    }

    /**
     * 数据刷新
     * @param dataService
     * @return
     */
    public CompressorGroup refresh(DataService dataService) {
        //数量刷新
        totalCount = compressors.size();
        int runningCount = 0;
        int errorCount = 0;

        for (Compressor compressor : compressors) {
            if (EnumCompressorState.ERROR.getState().equals(compressor.getState())) {
                errorCount++;
            } else if (EnumCompressorState.RUNNING.getState().equals(compressor.getState())) {
                runningCount++;
            }
        }

        this.runningCount = runningCount;
        this.errorCount = errorCount;

        //压力刷新
        double totalPressure = 0.0;
        int deviceCount = deviceThingCodes.size();
        for (int i=0;i<deviceCount;i++) {
            Optional<DataModelWrapper> data = dataService.getData(deviceThingCodes.get(i), CompressorMetricConstants.PRESSURE);
            if (!data.isPresent()) {
                logger.warn("Pressure device {} return no data.", deviceThingCodes.get(i));
                return this;
            }

            totalPressure += Double.parseDouble(data.get().getValue());
        }
        pressure = totalPressure/deviceCount;

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
}
