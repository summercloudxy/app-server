package com.zgiot.app.server.config.prop;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dataengine")
public class DataEngineProperties {
    public static final String CONNECTION_MODE_NONE="NONE";
    public static final String CONNECTION_MODE_WEBSOCKET="WSS";
    public static final String CONNECTION_MODE_ROCKETMQ="ROCKETMQ";

    private boolean https = false;
    private String serverName;
    private String websocketEndpoint;
    private String connectionMode;
    private int readTimeout = 5000;
    private int handshakeTimeout = 5000;
    private int connectionTimeout = 10000;

    public String getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(String connectionMode) {
        this.connectionMode = connectionMode;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getWebsocketEndpoint() {
        return websocketEndpoint;
    }

    public void setWebsocketEndpoint(String websocketEndpoint) {
        this.websocketEndpoint = websocketEndpoint;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getHandshakeTimeout() {
        return handshakeTimeout;
    }

    public void setHandshakeTimeout(int handshakeTimeout) {
        this.handshakeTimeout = handshakeTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
