package com.zgiot.app.server.module.bellows.pressure;

import com.zgiot.app.server.module.bellows.compressor.Compressor;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.BellowsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 */
@Component
public class PressureManager {

    private static final Logger logger = LoggerFactory.getLogger(PressureManager.class);


    private Map<String, DeviceGroup> map = new HashMap<>();


    private volatile boolean initial;


    @PostConstruct
    public void init() {
        synchronized (this) {
            if (initial) {
                return;
            }
            map.put(BellowsConstants.CP_TYPE_HIGH, new DeviceGroup(Arrays.asList("2334")));
            //低压2335已坏，只取2333的值
            map.put(BellowsConstants.CP_TYPE_LOW, new DeviceGroup(Arrays.asList("2333")));
            initial = true;
        }
    }

    /**
     * 刷新获取分组最新压力值
     * @param type
     * @return
     */
    public double refreshPressure(String type, DataService dataService, String requestId) {
        synchronized (this) {
            if (!initial) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Pressure manager is not initial, start to init.");
                }
                init();
            }
        }
        DeviceGroup group = map.get(type);
        if (type == null) {
            logger.error("Pressure group {} cannot find. RequestId: {}.", type, requestId);
            return 0;
        }

        return group.refresh(dataService, requestId);
    }
}
