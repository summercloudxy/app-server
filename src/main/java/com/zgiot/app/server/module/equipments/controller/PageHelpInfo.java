package com.zgiot.app.server.module.equipments.controller;

import java.util.List;

public class PageHelpInfo<T> {

    List<T> list;// 数据
    Long sum;// 总数

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
