package com.zgiot.app.server.config;

import com.zgiot.app.server.dataprocessor.CompleterDataListener;
import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.dataprocessor.impl.CacheUpdater;
import com.zgiot.app.server.module.alert.AlertFaultJob;
import com.zgiot.app.server.module.alert.AlertHistoryJob;
import com.zgiot.app.server.module.alert.AlertListener;
import com.zgiot.app.server.module.alert.AlertParamJob;
import com.zgiot.app.server.module.alert.handler.AlertFaultHandler;
import com.zgiot.app.server.module.alert.handler.AlertParamHandler;
import com.zgiot.app.server.module.bellows.BellowsDataListener;
import com.zgiot.app.server.module.bellows.compressor.CompressorManager;
import com.zgiot.app.server.module.bellows.valve.ValveIntelligentJob;
import com.zgiot.app.server.module.bellows.valve.ValveManager;
import com.zgiot.app.server.module.coalanalysis.listener.CoalAnalysisListener;
import com.zgiot.app.server.module.demo.DemoBusiness;
import com.zgiot.app.server.module.demo.DemoDataCompleter;
import com.zgiot.app.server.module.densitycontrol.DensityControlListener;
import com.zgiot.app.server.module.filterpress.FilterPressDataListener;
import com.zgiot.app.server.service.impl.HistoryDataPersistDaemon;
import com.zgiot.app.server.service.impl.QuartzManager;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);
    private static ApplicationContext applicationContext = null;
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
    private AlertListener alertListener;
    @Autowired
    private BellowsDataListener bellowsDataListener;
    @Autowired
    private ValveManager valveManager;
    @Autowired
    private CompressorManager compressorManager;
    @Autowired
    private ModuleListConfig moduleListConfig;
    @Autowired
    private AlertParamHandler alertParamHandler;
    @Autowired
    private AlertFaultHandler alertFaultHandler;
    @Autowired
    private HistoryDataPersistDaemon historyDataPersistDaemon;
    @Autowired
    private DensityControlListener densityControlListener;
    @Autowired
    private CoalAnalysisListener coalAnalysisListener;

    private static final int FAULT_SCAN_RATE = 20;



    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (applicationContext == null) {
            applicationContext = contextRefreshedEvent.getApplicationContext();
        }
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
            this.historyDataPersistDaemon.start();
        }

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_FILTERPRESS)) {
            processor.addListener(filterPressListener);
        }

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_DENSITY_CONTROL)) {
            processor.addListener(densityControlListener);
        }

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_ALERT)) {
            processor.addListener(alertListener);
            QuartzManager.addJob("checkParam", ModuleListConfig.MODULE_ALERT, "checkParam",
                    ModuleListConfig.MODULE_ALERT, AlertParamJob.class, "0/10 * * * * ?", new JobDataMap() {
                        {
                            put("handler", alertParamHandler);
                        }
                    });
            QuartzManager.addJob("clearHistory", ModuleListConfig.MODULE_ALERT, "clearHistory",
                    ModuleListConfig.MODULE_ALERT, AlertHistoryJob.class, "0 0 0 * * ?");
            QuartzManager.addJobWithInterval("checkFault", ModuleListConfig.MODULE_ALERT, "checkFault",
                    ModuleListConfig.MODULE_ALERT, AlertFaultJob.class, FAULT_SCAN_RATE, new JobDataMap() {
                        {
                            put("handler", alertFaultHandler);
                        }
                    });
        }

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_COAL_ANALYSIS)) {
            processor.addListener(coalAnalysisListener);
        }

        if (moduleListConfig.containModule(ModuleListConfig.MODULE_ALL)
                || moduleListConfig.containModule(ModuleListConfig.MODULE_BELLOWS)) {
            valveManager.init();
            compressorManager.init();
            processor.addListener(bellowsDataListener);
            QuartzManager.addJob("checkBlow", ModuleListConfig.MODULE_BELLOWS, "checkBlow",
                    ModuleListConfig.MODULE_BELLOWS, ValveIntelligentJob.class, "2 * * * * ?");
        }


        if (false) {
            processor.addListener(demoBusiness);
            completerDataListener.addCompleter(new DemoDataCompleter());
        }

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
