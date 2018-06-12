package com.zgiot.app.server.config;

import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class SocketSessionRegistry {
    private final ConcurrentMap<String, Set<String>> userSessionIds = new ConcurrentHashMap<>();
    private final Object lock = new Object();


    public SocketSessionRegistry() {
    }

    /**
     * 获取sessionId
     *
     * @param user
     * @return
     */
    public Set<String> getSessionIds(String user) {
        Set set = (Set) this.userSessionIds.get(user);
        return set != null ? set : Collections.emptySet();
    }

    /**
     * 获取所有session
     *
     * @return
     */
    public ConcurrentMap<String, Set<String>> getAllSessionIds() {
        return this.userSessionIds;
    }

    /**
     * register session
     *
     * @param user
     * @param sessionId
     */
    public void registerSessionId(String user, String sessionId) {
        Assert.notNull(user, "User must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        Object var3 = this.lock;
        synchronized (this.lock) {
            Object set = (Set) this.userSessionIds.get(user);
            if (set == null) {
                set = new CopyOnWriteArraySet<>();
                this.userSessionIds.put(user, (Set<String>) set);
            }

            ((Set) set).add(sessionId);
        }
    }

    public void unregisterSessionId(String sessionId) {
        Assert.notNull(sessionId, "Session ID must not be null");
        Object var3 = this.lock;
        synchronized (this.lock) {
            for (Map.Entry<String, Set<String>> entry : userSessionIds.entrySet()) {
                Set<String> set = entry.getValue();
                String user = entry.getKey();
                if (set.contains(sessionId) && set.remove(sessionId) && set.isEmpty()) {
                    this.userSessionIds.remove(user);
                }
            }
        }
    }
}
