package com.zgiot.app.server.module.filterpress.pojo;

import java.util.List;

public class FilterPressSinglePlateCountBean {
    private String thingCode;
    FilterPressPlateAndTimeBean filterPressPlateAndTimeBean;

    public String getThingCode() {
        return thingCode;
    }

    public FilterPressPlateAndTimeBean getFilterPressPlateAndTimeBean() {
        return filterPressPlateAndTimeBean;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setFilterPressPlateAndTimeBean(FilterPressPlateAndTimeBean filterPressPlateAndTimeBean) {
        this.filterPressPlateAndTimeBean = filterPressPlateAndTimeBean;
    }
}
