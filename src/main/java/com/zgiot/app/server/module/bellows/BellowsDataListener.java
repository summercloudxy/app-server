package com.zgiot.app.server.module.bellows;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.bellows.compressor.CompressorManager;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.CompressorMetricConstants;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 */
@Component
public class BellowsDataListener implements DataListener {

    private static final Logger logger = LoggerFactory.getLogger(BellowsDataListener.class);

    private static Set<String> compressorMetricCode = new HashSet<>();
    private static Set<String> valveMetricCode = new HashSet<>();

    @Autowired
    private CompressorManager compressorManager;

    static {
        compressorMetricCode.add(CompressorMetricConstants.WARN);
        compressorMetricCode.add(CompressorMetricConstants.ERROR);
        compressorMetricCode.add(CompressorMetricConstants.RUN_STATE);
        compressorMetricCode.add(CompressorMetricConstants.LOAD_STATE);
        compressorMetricCode.add(CompressorMetricConstants.LOCAL);
    }

    @Override
    public void onDataChange(DataModel dataModel) {
        if (logger.isTraceEnabled()) {
            logger.trace("received data from data engine: {}", dataModel);
        }
        //检查thingCode是否存在
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        if (BellowsConstants.PRESSURE_THING_CODE.equals(thingCode) && CompressorMetricConstants.PRESSURE_STATE.equals(metricCode)) {
            //压力状态变化
            handleCompressorPressureState(dataModel);
        } else if (compressorManager.containCompressor(thingCode) && compressorMetricCode.contains(metricCode)) {
            //空压机状态变化
            handleCompressorData(dataModel);
        }
    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }


    private void handleCompressorData(DataModel dataModel) {
        compressorManager.onDataSourceChange(dataModel);
    }

    private void handleCompressorPressureState(DataModel dataModel) {
        compressorManager.onPressureStateChange(dataModel);
    }
}
