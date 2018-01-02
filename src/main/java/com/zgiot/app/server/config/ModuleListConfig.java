package com.zgiot.app.server.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class ModuleListConfig {
    private static final Logger logger = LoggerFactory.getLogger(ModuleListConfig.class);
    public static final String MODULE_ALL = "ALL";
    public static final String MODULE_FILTERPRESS = "FILTERPRESS";
    public static final String MODULE_ALERT = "ALERT";
    public static final String MODULE_HIST_PERSIST = "HIST_PERSIST";
    public static final String MODULE_DENSITY_CONTROL = "DENSITY_CONTROL";

    @Value("${sf.modules.list}")
    private String moduleListStr;
    private Set configedModules = new HashSet();

    @PostConstruct
    void init() {
        if (StringUtils.isBlank(this.moduleListStr)) {
            logger.error("Module list is required. ");
            return;
        }

        String[] arr = this.moduleListStr.split(",");
        for (String str : arr) {
            str = str.trim().toUpperCase();
            configedModules.add(str);
        }
    }

    public boolean containModule(String moduleName) {
        if (configedModules.contains(MODULE_ALL)){
            return true;
        }

        return configedModules.contains(moduleName);
    }

}
