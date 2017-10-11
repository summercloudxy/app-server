package com.zgiot.app.server.module.notice;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

class StompSessionManager {
    private AtomicReference<StompSession> session;
    private ExecutorService executor = Stomp.EXECUTOR;
    private Set<StompSessionListener> sessionListeners;

    private StompSessionManager() {
        session = new AtomicReference<>(new DisconnectedSession());
        sessionListeners = new CopyOnWriteArraySet<>();
    }

    StompSession getSession() {
        return session.get();
    }

    void setSession(StompSession session) {
        if (!(session instanceof DisconnectedSession)) {
            this.session.set(session);
            for (StompSessionListener listener : sessionListeners) {
                executor.submit(listener::onSessionActive);
            }
        }
    }

    void addListener(StompSessionListener listener) {
        this.sessionListeners.add(listener);
    }

    void removeListener(StompSessionListener listener) {
        this.sessionListeners.remove(listener);
    }

    void clearSession() {
        this.session.set(new DisconnectedSession());
        for (StompSessionListener listener : sessionListeners) {
            executor.submit(listener::onSessionInactive);
        }
    }

    void onSessionError(Throwable error) {
        for (StompSessionListener listener : sessionListeners) {
            executor.submit(() -> listener.onError(error));
        }
    }

    static StompSessionManager getInstance() {
        return ManagerHolder.STOMP_SESSION_MANAGER;
    }

    private static final class ManagerHolder {
        private static final StompSessionManager STOMP_SESSION_MANAGER = new StompSessionManager();
    }

    private static class DisconnectedSession implements StompSession {
        @Override
        public String getSessionId() {
            return "disconnected session";
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public void setAutoReceipt(boolean enabled) {}

        @Override
        public Receiptable send(String destination, Object payload) {
            throwException();
            return null;
        }

        @Override
        public Receiptable send(StompHeaders headers, Object payload) {
            throwException();
            return null;
        }

        @Override
        public Subscription subscribe(String destination, StompFrameHandler handler) {
            throwException();
            return null;
        }

        @Override
        public Subscription subscribe(StompHeaders headers, StompFrameHandler handler) {
            throwException();
            return null;
        }

        @Override
        public Receiptable acknowledge(String messageId, boolean consumed) {
            throwException();
            return null;
        }

        @Override
        public void disconnect() {}

        private void throwException() {
            throw new UnsupportedOperationException("The client has not been connected");
        }
    }

}
