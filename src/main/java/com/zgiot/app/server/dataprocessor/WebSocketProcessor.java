package com.zgiot.app.server.dataprocessor;

import com.alibaba.fastjson.JSON;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ThingModel;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketTextListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketProcessor implements DataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketProcessor.class);
    private static final int DEFAULT_HANDSHAKE_TIMEOUT = 3;
    private static final int DEFAULT_CONNECT_TIMEOUT = 10;
    private AsyncHttpClient client;

    private int connectTimeout;

    private int handshakeTimeout;

    private String serverUrl;

    private WebSocket webSocketClient;

    private List<DataListener> listeners = new CopyOnWriteArrayList<>();

    private Timer reconnectTimer;

    private boolean autoReconnect = true;

    private ExecutorService executor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors(), 5000L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new ThreadFactory() {
        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "WebSocket Common Pool-" + counter.getAndIncrement());
        }
    });

    public WebSocketProcessor(String serverUrl) {
        this(serverUrl, DEFAULT_HANDSHAKE_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
    }

    public WebSocketProcessor(String serverUrl, int handshakeTimeout, int connectTimeout) {
        Objects.requireNonNull(serverUrl, "server url cannot be null");
        this.serverUrl = serverUrl;
        this.handshakeTimeout = handshakeTimeout;
        this.connectTimeout = connectTimeout;
        newClient();
    }

    private void newClient() {
        AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder()
                .setConnectTimeout(connectTimeout)
                .setHandshakeTimeout(handshakeTimeout)
                .build();
        client = new DefaultAsyncHttpClient(config);
    }

    @Override
    public CompletableFuture<Void> connect() {
        return client.prepareGet(serverUrl)
                .execute(new WebSocketUpgradeHandler
                        .Builder()
                        .addWebSocketListener(new WebSocketTextListener() {
                            @Override
                            public void onMessage(String message) {
                                handleMessage(message);
                            }

                            @Override
                            public void onOpen(WebSocket websocket) {
                                webSocketClient = websocket;
                                if (reconnectTimer != null) {
                                    reconnectTimer.cancel();
                                    reconnectTimer = null;//help GC
                                    logger.info("reconnected successfully");
                                }
                            }

                            @Override
                            public void onClose(WebSocket websocket) {
                                webSocketClient = websocket;
                                if (autoReconnect && reconnectTimer == null) {
                                    reconnectTimer = new Timer("websocket-guard");
                                    reconnectTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            logger.info("reconnecting...");
                                            connect();
                                        }
                                    }, 1000L, 3000L);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                handleError(t);
                            }
                        }).build())
                .toCompletableFuture()
                .thenApply(webSocket -> null);
    }

    private void handleMessage(String message) {
        try {
            DataModel dataModel = JSON.parseObject(message, DataModel.class);
            logger.trace("received: {}", dataModel);

            // exclude ERR data
            if (ThingModel.CATEGORY_ERROR.equals(dataModel.getThingCategoryCode())){
                logger.warn("Got error data `{}`. ", dataModel.toString());
                return ;
            }

            // each data per thread, but listeners are sync for ensuring logic dependencies
            executor.submit(() -> {
                for (DataListener listener : listeners) {
                    try {
                        listener.onDataChange(dataModel);
                    } catch (Exception e) {
                        handleError(e);
                    }
                }
            });

        } catch (Throwable error) {
            handleError(error);
        }
    }

    private void handleError(Throwable t) {
        for (DataListener listener : listeners) {
            executor.submit(() -> listener.onError(t));
        }
    }

    @Override
    public CompletableFuture<Void> disconnect() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            webSocketClient.close();
            future.complete(null);
        } catch (IOException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public boolean isConnected() {
        return webSocketClient != null && webSocketClient.isOpen();
    }

    @Override
    public void addListener(DataListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(DataListener listener) {
        listeners.remove(listener);
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getHandshakeTimeout() {
        return handshakeTimeout;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }
}
