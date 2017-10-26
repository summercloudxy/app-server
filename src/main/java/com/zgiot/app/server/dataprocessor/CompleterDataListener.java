package com.zgiot.app.server.dataprocessor;

import com.zgiot.app.server.common.ThreadPoolManager;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class CompleterDataListener implements DataListener {
    private static final Logger logger = LoggerFactory.getLogger(CompleterDataListener.class);
    private List<DataCompleter> completers = new ArrayList<>();

    ExecutorService es = ThreadPoolManager.getThreadPool(ThreadPoolManager.COMMON_POOL);

    @Override
    public void onDataChange(DataModel dataModel) {
        try {
            for (DataCompleter completer : completers) {
                es.submit(() -> {
                    try {
                        completer.onComplete(dataModel);
                    } catch (Throwable e) {
                        completer.onError(dataModel, e);
                    }
                });
            }

        } catch (Throwable error) {
            logger.error("Unknown data completer exception! ", error);
        }
    }

    @Override
    public void onError(Throwable error) {
        logger.error("Unknown data completer error! ", error);
    }
}
