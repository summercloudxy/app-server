package com.zgiot.app.server.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ModuleListConfig {
    private static final Logger logger = LoggerFactory.getLogger(ModuleListConfig.class);
    public static final String MODULE_DEMO = "demo";
    public static final String MODULE_FILTERPRESS = "filterpress";
    public static final String MODULE_ALERT = "alert";
    public static final String MODULE_HIST_PERSIST = "hist-persist";
    public static final String MODULE_BELLOWS = "bellows";
    public static final String MODULE_DENSITY_CONTROL = "density-control";
    public static final String MODULE_COAL_ANALYSIS = "coal-analysis";
    public static final String MODULE_SUBSCRIPTION = "subscription";

    @Value("${sysmodule.demo.enabled}")
    private boolean moduleDemoEnabled;
    @Value("${sysmodule.filterpress.enabled}")
    private boolean moduleFPEnabled;
    @Value("${sysmodule.alert.enabled}")
    private boolean moduleAlertEnabled;
    @Value("${sysmodule.hist-persist.enabled}")
    private boolean moduleHistPersistEnabled;
    @Value("${sysmodule.bellows.enabled}")
    private boolean moduleBellowsEnabled;
    @Value("${sysmodule.density-control.enabled}")
    private boolean moduleDensityCtlEnabled;
    @Value("${sysmodule.coal-analysis.enabled}")
    private boolean moduleCoalAnalyEnabled;
    @Value("${sysmodule.subscription.enabled}")
    private boolean moduleSubsEnabled;


    private static final int FAULT_SCAN_RATE = 20;

    private Map configedModuleMap = new ConcurrentHashMap(100);

    @Autowired
    private CacheUpdater cacheUpdater;
    @Autowired
    DemoBusiness demoBusiness;
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
    private AlertParamHandler alertParamHandler;
    @Autowired
    private AlertFaultHandler alertFaultHandler;
    @Autowired
    private HistoryDataPersistDaemon historyDataPersistDaemon;
    @Autowired
    private DensityControlListener densityControlListener;
    @Autowired
    private ReportFormsCompleter reportFormsCompleter;
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private FilterPressManager filterPressManager;

    @PostConstruct
    void init() {
        configedModuleMap.put(MODULE_DEMO, this.moduleDemoEnabled);
        configedModuleMap.put(MODULE_FILTERPRESS, this.moduleFPEnabled);
        configedModuleMap.put(MODULE_ALERT, this.moduleAlertEnabled);
        configedModuleMap.put(MODULE_HIST_PERSIST, this.moduleHistPersistEnabled);
        configedModuleMap.put(MODULE_BELLOWS, this.moduleBellowsEnabled);
        configedModuleMap.put(MODULE_DENSITY_CONTROL, this.moduleDensityCtlEnabled);
        configedModuleMap.put(MODULE_COAL_ANALYSIS, this.moduleCoalAnalyEnabled);
        configedModuleMap.put(MODULE_SUBSCRIPTION, this.moduleSubsEnabled);
        configedModuleMap.put(MODULE_REPORTFORM, this.moduleReportEnabled);
    }

    public boolean containModule(String moduleName) {
        Boolean rtn = (Boolean) configedModuleMap.get(moduleName);
        return (rtn == null) ? false : rtn.booleanValue();
    }

    void installModules(DataProcessor processor) {

        try {
            processor.addListener(completerDataListener);
            processor.addListener(cacheUpdater);

            if (containModule(ModuleListConfig.MODULE_COAL_ANALYSIS)) {
                completerDataListener.addCompleter(reportFormsCompleter);
                logIt(MODULE_COAL_ANALYSIS);
            }

            if (containModule(ModuleListConfig.MODULE_HIST_PERSIST)) {
                historyDataPersistDaemon.start();
                QuartzManager.addJob("historyMinData", ModuleListConfig.MODULE_SUBSCRIPTION, "historyMinData",
                        ModuleListConfig.MODULE_HIST_PERSIST, HistoryMinDataJob.class, "0 0/1 * * * ?");
                logIt(MODULE_HIST_PERSIST);
            }

            if (containModule(ModuleListConfig.MODULE_FILTERPRESS)) {
                filterPressManager.initFilterPress();
                processor.addListener(filterPressListener);
                logIt(MODULE_FILTERPRESS);
            }

            if (containModule(ModuleListConfig.MODULE_DENSITY_CONTROL)) {
                processor.addListener(densityControlListener);
                logIt(MODULE_DENSITY_CONTROL);
            }

            if (containModule(ModuleListConfig.MODULE_ALERT)) {
                alertManager.init();
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

                logIt(MODULE_ALERT);
            }

            if (containModule(ModuleListConfig.MODULE_BELLOWS)) {
                valveManager.init();
                compressorManager.init();
                processor.addListener(bellowsDataListener);
                QuartzManager.addJob("checkBlow", ModuleListConfig.MODULE_BELLOWS, "checkBlow",
                        ModuleListConfig.MODULE_BELLOWS, ValveIntelligentJob.class, "2 * * * * ?");
                logIt(MODULE_BELLOWS);
            }

            if (containModule(ModuleListConfig.MODULE_SUBSCRIPTION)) {
                QuartzManager.addJob("uploadsubscCardDatasOf5s", ModuleListConfig.MODULE_SUBSCRIPTION, "uploadsubscCardDatasOf5s",
                        ModuleListConfig.MODULE_SUBSCRIPTION, UploadSubscCardDatas.class, "0/5 * * * * ?");

                QuartzManager.addJob("uploadsubscCardDatasOf10s", ModuleListConfig.MODULE_SUBSCRIPTION, "uploadsubscCardDatasOf10s",
                        ModuleListConfig.MODULE_SUBSCRIPTION, UploadHistorySubscCardDatas.class, "0/10 * * * * ?");

            QuartzManager.addJob("uploadsubscCardDatasOf6oclock", ModuleListConfig.MODULE_SUBSCRIPTION, "uploadsubscCardDatasOf6oclock",
                    ModuleListConfig.MODULE_SUBSCRIPTION, UploadProductionSubscCardDatas.class, "0 0 6,18 * * ?");

            logIt(MODULE_SUBSCRIPTION);
        }

            if (this.containModule(MODULE_DEMO)) {
                completerDataListener.addCompleter(new DemoDataCompleter());
                processor.addListener(demoBusiness);
                logIt(MODULE_DEMO);
            }

            logger.info("Modules are all loaded successfully. ");

        } catch (Exception e) {
            logger.error("Sys Modules failed to load. Pls check exception and restart again! ",e );
        }

    }

    private void logIt(String moduleName) {
        logger.info("Module `{}` loaded. ", moduleName);
    }

}
