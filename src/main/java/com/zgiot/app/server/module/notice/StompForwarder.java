package com.zgiot.app.server.module.notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class StompForwarder {
    private static final Logger logger = LoggerFactory.getLogger(StompForwarder.class);
    @Autowired
    private StompClient stompClient;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Value("#{'${stomp.client.topics}'.split(',')}")
    private String[] topics;


    public void initForwarder() {
        stompClient.addSessionListener(new StompSessionListener() {
            @Override
            public void onSessionActive() {
                logger.info("STOMP forwarding started");
            }

            @Override
            public void onSessionInactive() {
                logger.warn("STOMP forwarding error because session inactive");
            }

            @Override
            public void onError(Throwable error) {
                logger.error("STOMP forwarding error", error);
            }
        });
        stompClient.connect().thenAccept(client -> {
            for (String topic : topics) {
                client.subscribe(topic, String.class, message -> {
                    logger.trace("received message [{}] from topic: [{}], forward to clients", message, topic);
                    messagingTemplate.convertAndSend(topic, message);
                });
            }
        });
    }
}
