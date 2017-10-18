package com.zgiot.app.server.module.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class StompClient {
    private static final Logger logger = LoggerFactory.getLogger(StompClient.class);

    private final String serverUrl;
    private WebSocketStompClient stompClient;
    private StompSessionManager sessionManager;
    private SubscriptionManager subscriptionManager;

    private boolean autoReconnect = true;
    private Timer connectionGuard;

    public StompClient(String serverUrl) {
        if (serverUrl == null) {
            throw new NullPointerException("server url is null");
        }
        this.sessionManager = StompSessionManager.getInstance();
        this.subscriptionManager = SubscriptionManager.getInstance();
        this.serverUrl = serverUrl;
        StandardWebSocketClient wsClient = new StandardWebSocketClient();
        this.stompClient = new WebSocketStompClient(wsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.stompClient.setTaskScheduler(new DefaultManagedTaskScheduler()); // for heartbeats
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public CompletableFuture<StompClient> connect() {
        CompletableFuture<StompClient> future = new CompletableFuture<>();
        stompClient.connect(serverUrl, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                sessionManager.setSession(session);
                subscriptionManager.resubscribeServer();
                logger.debug("client has connected:{}", session.getSessionId());
                future.complete(StompClient.this);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                        byte[] payload, Throwable exception) {
                sessionManager.onSessionError(exception);
                logger.warn("client has encountered an error", exception);
                future.completeExceptionally(exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                if (!session.isConnected() && exception instanceof ConnectionLostException) {
                    sessionManager.clearSession();
                    sessionManager.onSessionError(exception);
                    if (autoReconnect) {
                        activeGuard();
                    }
                }
                logger.warn("client has encountered an error", exception);
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    public void addSessionListener(StompSessionListener sessionListener) {
        sessionManager.addListener(sessionListener);
    }

    public void removeSessionListener(StompSessionListener sessionListener) {
        sessionManager.removeListener(sessionListener);
    }

    public <T> CompletableFuture<StompSubscription<T>> subscribe(String topic, Class<T> messageType,
            Consumer<T> consumer) {
        return subscriptionManager.newSubscription(topic, messageType, consumer);
    }

    public CompletableFuture<StompSession.Receiptable> send(String destination, Object message) {
        CompletableFuture<StompSession.Receiptable> future = new CompletableFuture<>();
        StompSession.Receiptable send = sessionManager.getSession().send(destination, message);
        future.complete(send);
        return future;
    }

    private Timer resetGuard() {
        return new Timer("stomp-guard");
    }

    private void activeGuard() {
        connectionGuard = resetGuard();
        connectionGuard.schedule(new TimerTask() {
            private AtomicInteger time = new AtomicInteger(1);

            @Override
            public void run() {
                logger.debug("reconnecting to server... {} times attempts", time.getAndIncrement());
                connect().thenAccept(client -> {
                    logger.info("reconnection succeed");
                    connectionGuard.cancel();
                }).exceptionally(throwable -> {
                    logger.warn("reconnection failed", throwable);
                    return null;
                });

            }
        }, 2000L, 5000L);
    }

}
