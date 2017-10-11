package com.zgiot.app.server.dataprocessor.impl;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CacheUpdater implements DataListener {
    private static final Logger logger = LoggerFactory.getLogger(CacheUpdater.class);
    @Autowired
    private DataService dataService;

    @Override
    public void onDataChange(DataModel newData) {
        Optional<DataModelWrapper> old = dataService.getData(newData.getThingCode(), newData.getMetricCode());
        boolean toUpdate = false;

        String oldValue = null;
        if (!old.isPresent()) {
            toUpdate = true;
        } else {
            DataModelWrapper oldW = old.get();
            oldValue = oldW.getValue();
            if (newData.getDataTimeStamp().getTime() > oldW.getDataTimeStamp().getTime()) {
                toUpdate = true;
            }
        }

        // only update later one
        if (toUpdate) {
            newData.setPreValue(oldValue);
            dataService.updateCache(newData);
        }

    }

    @Override
    public void onError(Throwable error) {
        logger.error("总线数据异常：", error);
    }
}
