package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.common.QueueManager;
import com.zgiot.app.server.common.ThreadPoolManager;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

@Component
public class HistoryDataPersistDaemon {
    private static final Logger logger = LoggerFactory.getLogger(HistoryDataPersistDaemon.class);

    @Autowired
    HistoryDataService historyDataService;

    ExecutorService es = ThreadPoolManager.getThreadPool(ThreadPoolManager.COMMON_POOL);

    private static final int BATCH_ITEMS = 10000; // items
    private static final int TIMEOUT = 3000; // ms
    private static final int TIME_INTERVAL = 1000; // ms

    public void start() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int timeout = 0;
                BlockingQueue<DataModel> dataBuffer = (BlockingQueue<DataModel>) QueueManager.getQueue(QueueManager.HIST_BUFFER);

                while (true) {
                    try {
                        Thread.sleep(TIME_INTERVAL);
                        if (dataBuffer.size() >= BATCH_ITEMS
                                || timeout > TIMEOUT) {
                            // check and insert
                            synchronized (dataBuffer) {
                                int size = dataBuffer.size();
                                List<DataModel> list = new ArrayList<>(size);

                                if (size > 0) {
                                    dataBuffer.drainTo(list);
                                    es.execute(() -> {
                                        try {
                                            historyDataService.insertBatch(list);
                                        } catch (Exception e) {
                                            logger.error("Batch insert histdata error (msg: `{}`), write back to buffer for {} items."
                                                    , e.getMessage(), list.size());
                                            dataBuffer.addAll(list);
                                        }
                                    });
                                }

                                // clear timeout counter
                                timeout = 0;
                            }

                        } else {
                            timeout += TIME_INTERVAL;
                        }

                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        });

        t.setName("HistDataDaemon");
        t.setDaemon(true);
        t.start();

        logger.info("History data flush daemon started. (batch size={}, timeoutMs={}, intervalMs={})"
                , BATCH_ITEMS, TIMEOUT, TIME_INTERVAL);
    }

}

