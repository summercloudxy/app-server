package com.zgiot.app.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class STOMPDisConnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    private  SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //login get from browser
        String sessionId = sha.getSessionId();
        webAgentSessionRegistry.unregisterSessionId(sessionId);
    }
}
