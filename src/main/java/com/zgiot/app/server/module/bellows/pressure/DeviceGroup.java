package com.zgiot.app.server.module.bellows.pressure;

import com.zgiot.app.server.module.bellows.util.BellowsUtil;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.BellowsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * @author wangwei
 */
public class DeviceGroup {

    private static final Logger logger = LoggerFactory.getLogger(DeviceGroup.class);

    private final List<String> thingCodes;

    private volatile double pressure;

    public DeviceGroup(List<String> thingCodes) {
        this.thingCodes = thingCodes;
    }

    /**
     * 刷新获取分组最新压力值
     * @param dataService
     * @param requestId
     * @return
     */
    public synchronized double refresh(DataService dataService, String requestId) {
        //压力刷新
        double totalPressure = 0.0;
        int deviceCount = thingCodes.size();
        for (int i=0;i<deviceCount;i++) {
            String thingCode = thingCodes.get(i);

            Optional<String> data = BellowsUtil.getDataModelValue(dataService, thingCode, BellowsConstants.METRIC_PRESSURE);
            if (data.isPresent()) {
                totalPressure += Double.parseDouble(data.get());
            } else {
                pressure = 0;
                return pressure;
            }
        }
        pressure = totalPressure/deviceCount;

        if (logger.isDebugEnabled()) {
            logger.debug("Pressure group refresh pressure {}. RequestId: {}.", pressure, requestId);
        }

        return pressure;
    }
}
