package com.zgiot.app.server.module.bellows.enumeration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 */
public enum EnumLowCompressorFault {

    HIGH_EXHAUST_TEMPERATURE("CP_HET", "排气温度过高"),
    ROTATION_ERROR("CP_RE", "相序错误"),
    MAIN_MOTOR_OVER_CURRENT("CP_MMOC", "主电机过流"),
    AIR_FILTER_DIFFERENTIAL_PRESSURE("CP_AFDP", "空滤压差"),
    FINE_OIL_DIFFERENTIAL_PRESSURE("CP_FODP", "油细压差"),
    OIL_FILTER_DIFFERENTIAL_PRESSURE("CP_OFDP", "油滤压差"),
    FAN_OVER_CURRENT("CP_FOC", "风机过电流"),
    A_D_FAILURE("CP_ADF", "A/D故障"),
    LOW_OIL_PRESSURE("CP_LOP", "油压过低"),
    MAIN_CONTACTOR_FAILURE("CP_MCF", "主接触器故障"),
    FAN_CONTACTOR_FAILURE("CP_FCF", "风机接触器故障"),
    ABNORMAL_EXHAUST_PRESSURE("CP_AEP", "排气压力异常"),
    AUTO_STOPPING("CP_AS", "自动运行停机");

    private String metricCode;

    private String info;

    EnumLowCompressorFault(String metricCode, String info) {
        this.metricCode = metricCode;
        this.info = info;
    }

    /**
     * 获取metricCode set
     * @return
     */
    public static Set<String> metricCodes() {
        Set<String> result = new HashSet<>(13);
        for (EnumLowCompressorFault e : EnumLowCompressorFault.values()) {
            result.add(e.getMetricCode());
        }
        return result;
    }

    public static EnumLowCompressorFault getByMetricCode(String metricCode) {
        for (EnumLowCompressorFault e : EnumLowCompressorFault.values()) {
            if (e.getMetricCode().equals(metricCode)) {
                return e;
            }
        }
        return null;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
