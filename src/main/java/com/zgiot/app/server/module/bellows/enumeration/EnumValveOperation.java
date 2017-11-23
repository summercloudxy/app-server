package com.zgiot.app.server.module.bellows.enumeration;

import com.zgiot.common.constants.ValveMetricConstants;

/**
 * @author wangwei
 */
public enum EnumValveOperation {

    OPEN(ValveMetricConstants.CONTROL_OPEN),

    CLOSE(ValveMetricConstants.CONTROL_CLOSE);

    private String metricCode;

    EnumValveOperation(String metricCode) {
        this.metricCode = metricCode;
    }

    public String getMetricCode() {
        return metricCode;
    }
}
