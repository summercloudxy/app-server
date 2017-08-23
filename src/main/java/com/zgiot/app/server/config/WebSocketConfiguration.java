package com.zgiot.app.server.config;

import com.zgiot.app.server.config.prop.WebSocketProperties;
import com.zgiot.app.server.dataprocessor.WebSocketProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(WebSocketProperties.class)
@Configuration
public class WebSocketConfiguration {
    @Autowired
    private WebSocketProperties properties;

    @Bean(name = "wsProcessor")
    public WebSocketProcessor webSocketProcessor() {
        return new WebSocketProcessor(properties.getServerUrl(), properties.getHandshakeTimeout(), properties.getConnectionTimeout());
    }
}
