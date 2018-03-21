package com.zgiot.app.server.module.reportforms.pojo;

import java.util.List;

public class DensityAndFlowSourceInfo {
    private String coalSample;
    private Integer system;
    private List<String> thingCodes;
    private String densityCode;
    private String flowCode;
    private Double runDensityThreshold;
    private Double runFlowThreshold;


    public Double getRunFlowThreshold() {
        return runFlowThreshold;
    }

    public void setRunFlowThreshold(Double runFlowThreshold) {
        this.runFlowThreshold = runFlowThreshold;
    }

    public Double getRunDensityThreshold() {
        return runDensityThreshold;
    }

    public void setRunDensityThreshold(Double runDensityThreshold) {
        this.runDensityThreshold = runDensityThreshold;
    }


    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public List<String> getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(List<String> thingCodes) {
        this.thingCodes = thingCodes;
    }

    public String getDensityCode() {
        return densityCode;
    }

    public void setDensityCode(String densityCode) {
        this.densityCode = densityCode;
    }

    public String getFlowCode() {
        return flowCode;
    }

    public void setFlowCode(String flowCode) {
        this.flowCode = flowCode;
    }

    public String getCoalSample() {
        return coalSample;
    }

    public void setCoalSample(String coalSample) {
        this.coalSample = coalSample;
    }
}
