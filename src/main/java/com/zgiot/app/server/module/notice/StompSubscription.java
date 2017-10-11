package com.zgiot.app.server.module.notice;

import java.util.UUID;
import java.util.function.Consumer;

public class StompSubscription<T> {
    private final String subscriptionId;
    private final String topic;
    private final Class<T> messageType;
    private Consumer<T> consumer;

    public StompSubscription(String topic, Class<T> messageType, Consumer<T> consumer) {
        this.subscriptionId = UUID.randomUUID().toString();
        this.topic = topic;
        this.messageType = messageType;
        this.consumer = consumer;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getTopic() {
        return topic;
    }


    public Class<T> getMessageType() {
        return messageType;
    }


    public Consumer<T> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    void onMessage(T message) {
        consumer.accept(message);
    }

    public void unsubscribe() {
        SubscriptionManager.getInstance().unsubscibe(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StompSubscription<?> that = (StompSubscription<?>) o;

        return subscriptionId.equals(that.subscriptionId);
    }

    @Override
    public int hashCode() {
        return subscriptionId.hashCode();
    }
}
