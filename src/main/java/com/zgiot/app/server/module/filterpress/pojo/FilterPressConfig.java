package com.zgiot.app.server.module.filterpress.pojo;

public class FilterPressConfig {
    private String thingCode;
    private String paramName;
    private String paramValue;

    public String getThingCode() {
        return thingCode;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
