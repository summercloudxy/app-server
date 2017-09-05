package com.zgiot.app.server.dataprocessor.impl;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheUpdater implements DataListener {
    private static final Logger logger = LoggerFactory.getLogger(CacheUpdater.class);
    @Autowired
    private DataService dataService;

    @Override
    public void onDataChange(DataModel dataModel) {
        DataModelWrapper old = dataService.getData(dataModel.getThingCode(), dataModel.getMetricCode());
        // only update later one
        if (old == null || dataModel.getDataTimeStamp().getTime() > old.getDataTimeStamp().getTime() ){
            dataService.updateCache(dataModel);
        }
    }

    @Override
    public void onError(Throwable error) {
        logger.error("总线数据异常：", error);
    }
}
