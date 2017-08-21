package com.zgiot.app.server.common;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.dataprocessor.impl.CacheUpdater;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);
    @Autowired
    private CacheUpdater cacheUpdater;
    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor processor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        processor.connect().thenRun(() -> {
            logger.info("connected");
            processor.addListener(new DataListener() {
                @Override
                public void onDataChange(DataModel dataModel) {
                    cacheUpdater.onDataChange(dataModel);
                }

                @Override
                public void onError(Throwable error) {
                    logger.error("error", error);
                }
            });
        }).exceptionally(throwable -> {
            logger.error("error", throwable);
            return null;
        });


    }
}
