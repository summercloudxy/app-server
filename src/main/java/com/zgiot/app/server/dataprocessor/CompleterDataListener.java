package com.zgiot.app.server.dataprocessor;

import com.zgiot.app.server.common.ThreadPoolManager;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class CompleterDataListener implements DataListener {
    private static final Logger logger = LoggerFactory.getLogger(CompleterDataListener.class);
    private List<DataCompleter> completers = new ArrayList<>();

    @Autowired
    DataService dataService;
    @Autowired
    HistoryDataService historyDataService;

    ExecutorService es = ThreadPoolManager.getThreadPool(ThreadPoolManager.COMMON_POOL);


    @Override
    public void onDataChange(DataModel dataModel) {
        try {
            for (DataCompleter completer : completers) {
                es.submit(() -> {
                    try {
                        List<DataModel> list = completer.onComplete(dataModel);
                        if (list != null) {
                            for (DataModel dm : list) {
                                dataService.saveData(dm);
                            }
                        }

                    } catch (Throwable e) {
                        completer.onError(dataModel, e);
                    }
                });
            }

        } catch (Throwable error) {
            logger.error("Unknown data completer exception! ", error);
        }
    }


    public void addCompleter(DataCompleter c) {
        this.completers.add(c);
    }

    @Override
    public void onError(Throwable error) {
        logger.error("Unknown data completer error! ", error);
    }
}
