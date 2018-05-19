package com.zgiot.app.server.config;

import com.zgiot.app.server.dataprocessor.DataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);
    private static ApplicationContext applicationContext = null;

    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor processor;

    @Autowired
    private ModuleListConfig moduleListConfig;

    private static boolean initFlag = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (applicationContext == null) {
            applicationContext = contextRefreshedEvent.getApplicationContext();
        }

        if (contextRefreshedEvent.getApplicationContext() instanceof GenericWebApplicationContext) {

            if (initFlag) {
                return;
            }
            initFlag = true;

            processor.connect().thenRun(() -> {
                logger.info("Connected DataEngine. ");
                try {
                    this.moduleListConfig.installModules(processor);
                } catch (Exception e) {
                    logger.error("Modules load failed. ", e);
                }
            }).exceptionally(throwable -> {
                logger.error("error", throwable);
                return null;
            });
        }

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
