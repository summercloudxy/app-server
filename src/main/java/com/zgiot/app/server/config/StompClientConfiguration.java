package com.zgiot.app.server.config;

import com.zgiot.app.server.config.prop.StompClientProperties;
import com.zgiot.app.server.module.notice.StompClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StompClientProperties.class)
public class StompClientConfiguration {
    private final StompClientProperties clientProperties;

    @Autowired
    public StompClientConfiguration(StompClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Bean
    public StompClient client() {
        StompClient client = new StompClient(clientProperties.getServerUrl());
        client.setAutoReconnect(clientProperties.isAutoReconnect());
        return client;
    }
}
