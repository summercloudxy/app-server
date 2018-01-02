package com.zgiot.app.server.module.bellows.enumeration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 */
public enum EnumHighCompressorFault {


    HIGH_EXHAUST_TEMPERATURE("CP_HET", "排气温度过高"),
    HIGH_PRESSURE_TANK_FAILURE("CP_HPTF", "高压柜故障"),
    MAIN_MOTOR_OVER_CURRENT("CP_MMOC", "主电机过流"),
    AIR_FILTER_BLOCKING("CP_AFB", "空滤堵塞"),
    FINE_OIL_BLOCKING("CP_FOB", "油细堵塞"),
    OIL_FILTER_BLOCKING("CP_OFB", "油滤堵塞"),
    FAN_OVER_CURRENT("CP_FOC", "冷却风机过电流"),
    STIMULATION_CHANNEL_FAILURE("CP_SCF", "模拟量通道故障"),
    LOW_SCREW_EXHAUST_PRESSURE("CP_LSEP", "螺杆排气压力低"),
    HIGH_PRESSURE_CONTACT_FAILURE("CP_HPCF", "高压柜触点反馈故障"),
    BLOWER_CONTACT_FAILURE("CP_BCF", "风机接触器触点故障"),
    ABNORMAL_EXHAUST_PRESSURE("CP_AEP", "排气压力异常"),
    AUTO_STOPPING("CP_AS", "自动运行停机");


    private String metricCode;

    private String info;

    EnumHighCompressorFault(String metricCode, String info) {
        this.metricCode = metricCode;
        this.info = info;
    }

    /**
     * 获取metricCode set
     * @return
     */
    public static Set<String> metricCodes() {
        Set<String> result = new HashSet<>(EnumHighCompressorFault.values().length);
        for (EnumHighCompressorFault e : EnumHighCompressorFault.values()) {
            result.add(e.getMetricCode());
        }
        return result;
    }

    public static EnumHighCompressorFault getByMetricCode(String metricCode) {
        for (EnumHighCompressorFault e : EnumHighCompressorFault.values()) {
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
