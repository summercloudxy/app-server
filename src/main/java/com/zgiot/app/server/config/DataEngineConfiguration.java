package com.zgiot.app.server.config;

import com.zgiot.app.server.config.prop.DataEngineProperties;
import com.zgiot.app.server.dataprocessor.WebSocketProcessor;
import com.zgiot.app.server.service.impl.DataEngineTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;

@Configuration
@EnableConfigurationProperties(DataEngineProperties.class)
@EnableAutoConfiguration
public class DataEngineConfiguration {
    @Autowired
    private DataEngineProperties properties;

    @Bean(name = "wsProcessor")
    public WebSocketProcessor webSocketProcessor() {
        if (DataEngineProperties.CONNECTION_MODE_WEBSOCKET.equals(properties.getConnectionMode())) {
            String schema = properties.isHttps() ? "wss://" : "ws://";
            String serverName = properties.getServerName();
            String websocketEndpoint = properties.getWebsocketEndpoint();
            String url = schema + serverName + websocketEndpoint;
            return new WebSocketProcessor(url, properties.getHandshakeTimeout(), properties.getConnectionTimeout());
        } else {
            return null;
        }
    }

    @Bean
    public DataEngineTemplate restTemplate(ClientHttpRequestFactory factory) {
        String schema = properties.isHttps() ? "https://" : "http://";
        String serverName = properties.getServerName();
        String url = schema + serverName;
        return new DataEngineTemplate(url, factory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        Netty4ClientHttpRequestFactory factory = new Netty4ClientHttpRequestFactory();
        factory.setReadTimeout(properties.getReadTimeout());
        factory.setConnectTimeout(properties.getConnectionTimeout());
        return factory;
    }
}