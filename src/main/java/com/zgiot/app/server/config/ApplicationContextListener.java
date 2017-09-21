package com.zgiot.app.server.config;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.dataprocessor.impl.CacheUpdater;
import com.zgiot.app.server.module.filterpress.FilterPressDataListener;
import com.zgiot.app.server.module.demo.DemoBusiness;
import com.zgiot.app.server.service.cache.DataCache;
import com.zgiot.app.server.service.impl.DataEngineTemplate;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);
    @Autowired
    private CacheUpdater cacheUpdater;
    @Autowired
    DemoBusiness demoBusiness;
    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor processor;
    @Autowired
    private FilterPressDataListener filterPressListener;
    @Autowired
    private DataCache dataCache;
    @Autowired
    private DataEngineTemplate dataEngineTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String initString = dataEngineTemplate.getForObject(DataEngineTemplate.URI_DATA_ALL, String.class);
        DataModel[] initValue = JSON.parseObject(initString, DataModel[].class);
        dataCache.initCache(Arrays.asList(initValue));
        processor.connect().thenRun(() -> {
            logger.info("connected");
            processor.addListener(cacheUpdater);
            processor.addListener(filterPressListener);
            //processor.addListener(demoBusiness);
        }).exceptionally(throwable -> {
            logger.error("error", throwable);
            return null;
        });

    }
}
