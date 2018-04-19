package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.service.BusinessService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class BusinessServiceImpl implements BusinessService {


    @Autowired
    private DataService dataService;

    private String SYSTEM01_ROWCOAL = "Quit_SYS_1";

    private String SYSTEM02_ROWCOAL = "Quit_SYS_2";

    @Override
    public Map<String, String> getRowCoalCapPercent() {
        Map<String, String> map = new HashMap<>();
        //一期配煤比
        String instantMetric1 = getInstantMetric(SYSTEM01_ROWCOAL, MetricCodes.COAL_8_DEVICE, MetricCodes.COAL_CAP);
        String instantMetric2 = getInstantMetric(SYSTEM01_ROWCOAL, MetricCodes.COAL_13_DEVICE, MetricCodes.COAL_CAP);
        map.put(GlobalConstants.SYSTEM_ONE, getInstantMetricPercent(instantMetric1, instantMetric2));
        //一期配煤比
        String instantMetric3 = getInstantMetric(SYSTEM02_ROWCOAL, MetricCodes.COAL_8_DEVICE, MetricCodes.COAL_CAP);
        String instantMetric4 = getInstantMetric(SYSTEM02_ROWCOAL, MetricCodes.COAL_13_DEVICE, MetricCodes.COAL_CAP);
        map.put(GlobalConstants.SYSTEM_TWO, getInstantMetricPercent(instantMetric3, instantMetric4));
        return map;
    }

    @Override
    public String getLevelByThingCode(String thingCode) {
        DataModelWrapper levelHighData = dataService.getData(thingCode, MetricCodes.SETTED_HIGH_LEVEL).orElse(null);
        DataModelWrapper levelLowData = dataService.getData(thingCode, MetricCodes.SETTED_HIGH_LEVEL).orElse(null);
        String level =GlobalConstants.LEVEL_UNKNOWN;
        // (高液位在前)00：低液位 01：正常 11：高液位
        if(levelHighData != null && levelLowData != null){
            if ("1".equals(levelHighData.getValue()) && "1".equals(levelLowData.getValue())){
                return GlobalConstants.LEVEL_HIGH;
            }else if ("0".equals(levelHighData.getValue()) && "0".equals(levelLowData.getValue())){
                return GlobalConstants.LEVEL_LOW;
            }else if ("0".equals(levelHighData.getValue()) && "1".equals(levelLowData.getValue())){
                return GlobalConstants.LEVEL_NORMAL;
            }
        }
        return level;
    }

    /**
     * 计算皮带的设备号以及瞬时带煤量
     *
     * @param systemThing
     * @param metricCode1
     * @param metricCode2
     * @return
     */
    private String getInstantMetric(String systemThing, String metricCode1, String metricCode2) {
        DataModelWrapper thingData = dataService.getData(systemThing, metricCode1).orElse(null);
        if (thingData != null && (!"-1".equals(thingData.getValue())) && (!"0".equals(thingData.getValue()))) {
            DataModelWrapper dataModelWrapper = dataService.getData(thingData.getValue(), metricCode2).orElse(null);
            if (dataModelWrapper != null) {
                return dataModelWrapper.getValue();
            }
        }
        return "";
    }

    /**
     * 计算比例
     *
     * @param instantMetric1
     * @param instantMetric2
     * @return
     */
    private String getInstantMetricPercent(String instantMetric1, String instantMetric2) {
        if (StringUtils.isNotEmpty(instantMetric1) && StringUtils.isNotEmpty(instantMetric2)) {
            BigDecimal instant3 = new BigDecimal(instantMetric1);
            BigDecimal instant4 = new BigDecimal(instantMetric2);
            if (instant3.compareTo(instant4) > 0) {
                BigDecimal percent = instant3.divide(instant4, 2, BigDecimal.ROUND_HALF_UP);
                return percent + ":1";
            } else if (instant3.compareTo(instant4) < 0) {
                BigDecimal percent = instant4.divide(instant3, 2, BigDecimal.ROUND_HALF_UP);
                return "1:" + percent;
            } else {
                return "1:1";
            }
        } else {
            return "";
        }

    }


}
