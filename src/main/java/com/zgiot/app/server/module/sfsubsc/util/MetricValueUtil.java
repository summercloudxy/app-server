package com.zgiot.app.server.module.sfsubsc.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * 格式化指标数据保留的小数位
 */
public class MetricValueUtil {

    private static int places = 1;
    private static int placesPoint2 = 2;

    public static String formart(String metricValue) {
        return String.valueOf(new BigDecimal(metricValue).setScale(places, BigDecimal.ROUND_HALF_UP));

    }

    public static String formart(BigDecimal metricValue) {
        return String.valueOf(metricValue.setScale(places, BigDecimal.ROUND_HALF_UP));
    }

    public static String formartPoint2(String metricValue) {
        return String.valueOf(new BigDecimal(StringUtils.isBlank(metricValue) ? "0" : metricValue)
                .setScale(placesPoint2, BigDecimal.ROUND_HALF_UP));

    }

    public static String formartPoint2(BigDecimal metricValue) {
        return formartPoint2(Double.toString((metricValue == null ? BigDecimal.ZERO : metricValue).doubleValue()));
    }

    public static String formartPoint2(Double metricValue) {
        return formartPoint2((metricValue == null ? "0" : metricValue).toString());
    }

}
