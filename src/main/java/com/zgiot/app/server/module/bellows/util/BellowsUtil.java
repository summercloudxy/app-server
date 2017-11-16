package com.zgiot.app.server.module.bellows.util;

import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author wangwei
 */
public class BellowsUtil {

    private static final Logger logger = LoggerFactory.getLogger(BellowsUtil.class);

    /**
     * 拉取dataModel的value值
     * @param dataService
     * @param thingCode
     * @return
     */
    public static Optional<String> getDataModelValue(DataService dataService, String thingCode, String metricCode) {
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

        DataModelWrapper wrapper = dataService.adhocLoadData(thingCode, metricCode);
        if (wrapper == null || wrapper.getValue() == null) {
            logger.error("Cannot connect data engine to get thingCode {}, metricCode {}.", thingCode, metricCode);
            return Optional.empty();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Got data value : {} for thingCode {}, metricCode {} from data engine.", wrapper.getValue(), thingCode, metricCode);
            }

            return Optional.of(wrapper.getValue());
        }
    }
}
