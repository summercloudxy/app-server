package com.zgiot.app.server.config;

import com.zgiot.app.server.config.prop.DataEngineProperties;
import com.zgiot.app.server.dataprocessor.DataProcessor;
import com.zgiot.app.server.dataprocessor.RocketMqDataProcessor;
import com.zgiot.app.server.dataprocessor.WebSocketProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.concurrent.CompletableFuture;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);
    private static ApplicationContext applicationContext = null;

    @Autowired
    @Qualifier("wsProcessor")
    private DataProcessor wssProcessor;
    @Autowired
    @Qualifier("rocketMqProcessor")
    private DataProcessor rocketMqDataProcessor;
    @Value("${dataengine.connection-mode}")
    private String dataengineConnMode;

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

            DataProcessor processor = buildDataProcessor();

            if (processor == null) {
                logger.error("No DataProcessor Configured. ");
            } else {
                CompletableFuture<Void> cfu = processor.connect();
                if (cfu != null) {
                    cfu.thenRun(() -> {
                        logger.info("Data Engine WSS Connected. ");
                        this.moduleListConfig.installModules(processor);
                    }).exceptionally(throwable -> {
                        logger.error("error", throwable);
                        return null;
                    });
                } else {
                    logger.info("Data Bus Connected. ");
                    this.moduleListConfig.installModules(processor);
                }
            }
        }

    }

    private DataProcessor buildDataProcessor() {

        if (DataEngineProperties.CONNECTION_MODE_WEBSOCKET.equals(this.dataengineConnMode)) {
            logger.info("DataProcessor `{}` found. ", WebSocketProcessor.class.getName());
            return this.wssProcessor;
        }

        if (DataEngineProperties.CONNECTION_MODE_ROCKETMQ.equals(this.dataengineConnMode)) {
            logger.info("DataProcessor `{}` found. ", RocketMqDataProcessor.class.getName());
            return this.rocketMqDataProcessor;
        }

        logger.error("No dataprocessor found, configured connection mode is `{}`. ", this.dataengineConnMode);

        return null;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
