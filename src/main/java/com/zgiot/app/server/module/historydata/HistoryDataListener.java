package com.zgiot.app.server.module.historydata;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryDataListener implements DataListener {
    private static final Logger logger = LoggerFactory.getLogger(HistoryDataListener.class);

    @Autowired
    HistoryDataService historyDataService;

    @Override
    public void onDataChange(DataModel dm) {
        historyDataService.asyncSmartAddData(dm);
    }

    @Override
    public void onError(Throwable error) {
        logger.error(error.getMessage());
    }

}

