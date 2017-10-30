package com.zgiot.app.server.config;

import com.zgiot.app.server.dataprocessor.CompleterDataListener;
import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.dataprocessor.impl.CacheUpdater;
import com.zgiot.app.server.module.alert.AlertListener;
import com.zgiot.app.server.module.demo.DemoBusiness;
import com.zgiot.app.server.module.filterpress.FilterPressDataListener;
import com.zgiot.app.server.module.historydata.HistoryDataListener;
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
    DemoBusiness demoBusiness;
    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor processor;
    @Autowired
    private FilterPressDataListener filterPressListener;
    @Autowired
    private CompleterDataListener completerDataListener;
    @Autowired
    private HistoryDataListener historyDataListener;
    @Autowired
    private AlertListener alertListener;
    @Autowired
    ModuleListConfig moduleListConfig;

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        processor.connect().thenRun(() -> {
            logger.info("Connected DataEngine. ");
            installModules(processor);
        }).exceptionally(throwable -> {
            logger.error("error", throwable);
            return null;
        });
    }

    void installModules(DataProcessor processor) {
        processor.addListener(cacheUpdater);
        processor.addListener(completerDataListener);

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_HIST_PERSIST)) {
            processor.addListener(historyDataListener);
        }

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_FILTERPRESS)) {
            processor.addListener(filterPressListener);
        }

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_ALERT)) {
            processor.addListener(alertListener);
        }

        //processor.addListener(demoBusiness);
    }

}
