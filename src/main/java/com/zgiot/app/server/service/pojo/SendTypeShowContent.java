package com.zgiot.app.server.service.pojo;

public class SendTypeShowContent {
    private Integer id;
    private String stateMetricCode;
    private String stateValue;
    private String showContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateMetricCode() {
        return stateMetricCode;
    }

    public void setStateMetricCode(String stateMetricCode) {
        this.stateMetricCode = stateMetricCode;
    }

    public String getStateValue() {
        return stateValue;
    }

    public void setStateValue(String stateValue) {
        this.stateValue = stateValue;
    }

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }
}
