package com.zgiot.app.server.dataprocessor.impl;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
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
        dataService.updateCache(dataModel);
    }

    @Override
    public void onError(Throwable error) {
        logger.error("总线数据异常：", error);
    }
}
