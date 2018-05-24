package com.zgiot.app.server.module.sfstart.pojo;


public class StartRequirement {

    // 比较设备id
    private String compareDeviceId;

    // 比较设备code
    private String compareDeviceCode;

    // 比较设备name
    private String compareDeviceName;

    // 设备读取name
    private Integer compareName;

    // 读取name中文释义
    private String compareNameExplain;

    // PLC标签
    private String label;

    //比较符(greater：大于,less:小于,equal:等于,greaterAndEqual:大于等于,lessAndEqual:小于等于,notEqual:不等于)
    private String operator;

    // 比较值
    private Float compareValue;

    private Integer isAbnormal;//比较结果是否正常(0:正常，1:异常)

    public String getCompareDeviceId() {
        return compareDeviceId;
    }

    public void setCompareDeviceId(String compareDeviceId) {
        this.compareDeviceId = compareDeviceId;
    }

    public String getCompareDeviceCode() {
        return compareDeviceCode;
    }

    public void setCompareDeviceCode(String compareDeviceCode) {
        this.compareDeviceCode = compareDeviceCode;
    }

    public String getCompareDeviceName() {
        return compareDeviceName;
    }

    public void setCompareDeviceName(String compareDeviceName) {
        this.compareDeviceName = compareDeviceName;
    }

    public Integer getCompareName() {
        return compareName;
    }

    public void setCompareName(Integer compareName) {
        this.compareName = compareName;
    }

    public String getCompareNameExplain() {
        return compareNameExplain;
    }

    public void setCompareNameExplain(String compareNameExplain) {
        this.compareNameExplain = compareNameExplain;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Float getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(Float compareValue) {
        this.compareValue = compareValue;
    }

    public Integer getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Integer isAbnormal) {
        this.isAbnormal = isAbnormal;
    }
}

