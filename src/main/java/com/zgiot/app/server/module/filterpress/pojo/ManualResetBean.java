package com.zgiot.app.server.module.filterpress.pojo;

import com.zgiot.common.pojo.DataModel;

public class ManualResetBean {
    private int position;
    private DataModel dataModel;

    public int getPosition() {
        return position;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }
}
