package com.zgiot.app.server.module.filterpress.pojo;

import java.util.List;

public class FilterPressHisPlateCountWrapper {
    String hisDate;
    List<FilterPressHisPlateCountBean>  filterPressHisPlateCountBeanList;

    public String getHisDate() {
        return hisDate;
    }

    public List<FilterPressHisPlateCountBean> getFilterPressHisPlateCountBeanList() {
        return filterPressHisPlateCountBeanList;
    }

    public void setHisDate(String hisDate) {
        this.hisDate = hisDate;
    }

    public void setFilterPressHisPlateCountBeanList(List<FilterPressHisPlateCountBean> filterPressHisPlateCountBeanList) {
        this.filterPressHisPlateCountBeanList = filterPressHisPlateCountBeanList;
    }
}
