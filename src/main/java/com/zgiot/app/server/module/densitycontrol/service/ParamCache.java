package com.zgiot.app.server.module.densitycontrol.service;

import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;

public interface ParamCache {

    DataModelWrapper getValue(String thingCode, String metricCode);

    void updateValue(DataModel dataModel);

    void updateValue(DataModelWrapper dataModelWrapper);

    void init();

    void clear();

}
