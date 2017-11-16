package com.zgiot.app.server.module.bellows.pressure;

import com.zgiot.app.server.module.bellows.compressor.Compressor;
import com.zgiot.app.server.service.DataService;
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

    /**
     * 低压管道压力检测1
     */
    private static final String LOW_PRESSURE_ONE = "2333";
    /**
     * 低压管道压力检测2
     */
    private static final String LOW_PRESSURE_TWO = "2334";
    /**
     * 高压管道压力检测
     */
    private static final String HIGH_PRESSURE = "2335";


    private Map<String, DeviceGroup> map = new HashMap<>();


    @PostConstruct
    public void init() {
        synchronized (this) {
            map.put(Compressor.TYPE_HIGH, new DeviceGroup(Arrays.asList(HIGH_PRESSURE)));
            map.put(Compressor.TYPE_LOW, new DeviceGroup(Arrays.asList(LOW_PRESSURE_ONE, LOW_PRESSURE_TWO)));
        }
    }

    /**
     * 刷新获取分组最新压力值
     * @param type
     * @return
     */
    public double refreshPressure(String type, DataService dataService, String requestId) {
        synchronized (this) {

        }
        DeviceGroup group = map.get(type);
        if (type == null) {
            logger.error("Pressure group {} cannot find. RequestId: {}.", type, requestId);
            return 0;
        }

        return group.refresh(dataService, requestId);
    }
}
