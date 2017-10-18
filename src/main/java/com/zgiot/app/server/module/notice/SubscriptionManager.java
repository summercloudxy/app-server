package com.zgiot.app.server.module.notice;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

class SubscriptionManager {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionManager.class);
    private StompSessionManager sessionManager;
    private Map<String, Set<StompSubscription>> subscriptions;
    private Map<String, StompSession.Subscription> serverSubscriptions;

    private ExecutorService executor;

    private ReentrantReadWriteLock lock;

    private SubscriptionManager() {
        this.subscriptions = new ConcurrentHashMap<>();
        this.serverSubscriptions = new ConcurrentHashMap<>();
        this.sessionManager = StompSessionManager.getInstance();
        this.lock = new ReentrantReadWriteLock();
        this.executor = Stomp.EXECUTOR;
    }

    <T> CompletableFuture<StompSubscription<T>> newSubscription(String topic, Class<T> messageType,
                                                                Consumer<T> consumer) {
        CompletableFuture<StompSubscription<T>> future = new CompletableFuture<>();
        // subscribe on server when this topic has never been subscribed
        lock.writeLock().lock();
        try {
            if (!serverSubscriptions.containsKey(topic)) {
                doSubscribe(topic);
            }
        } catch (Throwable error) {
            future.completeExceptionally(error);
            return future;
        } finally {
            lock.writeLock().unlock();
        }

        // register self managed subscription
        StompSubscription<T> subscription = new StompSubscription<>(topic, messageType, consumer);
        if (!subscriptions.containsKey(topic)) {
            subscriptions.put(topic, new CopyOnWriteArraySet<>());
        }
        subscriptions.get(topic).add(subscription);
        future.complete(subscription);

        return future;
    }

    void resubscribeServer() {
        lock.writeLock().lock();
        try {
            serverSubscriptions.forEach((topic,serverSubscription)->{
                doSubscribe(topic);
            });
        }finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    private void doSubscribe(String topic) {
        StompSession.Subscription serverSubscription =
                sessionManager.getSession().subscribe(topic, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Object.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        JSONObject jsonObject = new JSONObject((LinkedHashMap) payload);
                        for (StompSubscription subscription : subscriptions.get(topic)) {
                            Object message = jsonObject.toJavaObject(subscription.getMessageType());
                            executor.submit(() -> subscription.onMessage(message));
                        }
                    }
                });
        serverSubscriptions.put(topic, serverSubscription);
        logger.debug("subscribed topic: [{}] from server, subscription id = {}", topic, serverSubscription.getSubscriptionId());
    }

    void unsubscibe(StompSubscription stompSubscription) {
        String topic = stompSubscription.getTopic();
        Set<StompSubscription> stompSubscriptions = subscriptions.get(topic);
        if (stompSubscriptions != null) {
            stompSubscriptions.remove(stompSubscription);

        }
        if (stompSubscriptions == null || stompSubscriptions.size() == 0) {
            lock.writeLock().lock();
            try {
                subscriptions.remove(topic);
                StompSession.Subscription serverSubscription = serverSubscriptions.remove(topic);
                serverSubscription.unsubscribe();
                logger.debug("unsubscribed topic: [{}] from server, subscription id = {}", topic, serverSubscription.getSubscriptionId());
            } finally {
                lock.writeLock().unlock();
            }
        }

    }

    static SubscriptionManager getInstance() {
        return ManagerHolder.SUBSCRIPTION_MANAGER;
    }

    private static class ManagerHolder {
        private static final SubscriptionManager SUBSCRIPTION_MANAGER = new SubscriptionManager();
    }

}
