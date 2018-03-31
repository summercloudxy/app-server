package com.zgiot.app.server.module.bellows.util;

import com.zgiot.app.server.common.ThreadPoolManager;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * @author wangwei
 */
public class BellowsUtil {

    private static final Logger logger = LoggerFactory.getLogger(BellowsUtil.class);

    private BellowsUtil() {

    }

    /**
     * 拉取dataModel的value值
     * @param dataService
     * @param thingCode
     * @return
     */
    public static Optional<String> getDataModelValue(DataService dataService, String thingCode, String metricCode) {
        return getDataModelValue(dataService, thingCode, metricCode, false);
    }

    /**
     * 拉取dataModel的value值
     * @param dataService
     * @param thingCode
     * @param adhocLoad 是否异步调用adhocLoad接口
     * @return
     */
    public static Optional<String> getDataModelValue(DataService dataService, String thingCode, String metricCode, boolean adhocLoad) {
        Optional<DataModelWrapper> data = dataService.getData(thingCode, metricCode);
        if (data.isPresent()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Got data value : {} for thingCode {}, metricCode {} from cache.", data.get().getValue(), thingCode, metricCode);
            }
            return Optional.of(data.get().getValue());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Cannot find data for thingCode {}, metricCode {} in cache.", thingCode, metricCode);
        }

        if (adhocLoad) {
            ExecutorService es = ThreadPoolManager.getThreadPool(ThreadPoolManager.COMMON_POOL);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Load value for thingCode {}, metricCode {} from dataEngine.", thingCode, metricCode);
                    }
                    dataService.adhocLoadData(thingCode, metricCode);
                }
            };
            es.submit(runnable);
        }

        return Optional.empty();
    }
}
