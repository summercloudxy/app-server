package com.zgiot.app.server.dataprocessor.impl;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.dataprocessor.ProcessorUtil;
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
        Optional<DataModelWrapper> old = this.dataService.getData(newData.getThingCode(), newData.getMetricCode());
        boolean toUpdate = false;

        if (!old.isPresent()) {
            toUpdate = true;
        } else {
            try {
                if (old.get().getDataTimeStamp() == null || newData.getDataTimeStamp() == null) {
                    toUpdate = false;
                } else if (newData.getDataTimeStamp().getTime() > old.get().getDataTimeStamp().getTime()) {
                    toUpdate = true;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (toUpdate) {
            dataService.saveData(newData);
        } else {
            ReturnData rtn = new ReturnData();
            rtn.stopHere = true;
            ProcessorUtil.dataContext.set(rtn);
        }

    }

    @Override
    public void onError(Throwable error) {
        logger.error("总线数据异常：", error);
    }
}
