package com.zgiot.app.server.module.notice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Stomp {
    private Stomp() {
    }

    static final ExecutorService EXECUTOR =
            Executors.newCachedThreadPool(new StompThreadFactory());

    private static final class StompThreadFactory implements ThreadFactory {
        private AtomicInteger number = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "stomp-shared-pool(" + number.getAndIncrement() + ")");
        }
    }

}
