package com.zgiot.app.server.dataprocessor;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.enums.MetricDataTypeEnum;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class ProcessorUtil {
    public static void handleMessage(DataModel dataModel, ExecutorService executor,
                                     List<DataListener> listeners, Logger logger) {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("received: {}", dataModel);
            }

            // exclude ERR data
            if (MetricDataTypeEnum.METRIC_DATA_TYPE_ERROR.getName().equals(dataModel.getMetricDataType())) {
                if (HistoryDataService.fulldataLogger.isDebugEnabled()) {
                    HistoryDataService.fulldataLogger.debug(JSON.toJSONString(dataModel));
                }
                logger.warn("Got error data `{}`. ", dataModel.toString());
                return;
            }

            // each data per thread, but listeners are sync for ensuring logic dependencies
            executor.submit(() -> {
                for (DataListener listener : listeners) {
                    try {
                        listener.onDataChange(dataModel);
                    } catch (Throwable e) {
                        listener.onError(e);
                    }
                }
            });

        } catch (Throwable error) {
            logger.error("Unknown data processor exception! ", error);
        }
    }
}
