package com.zgiot.app.server.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolManager {

    public static final String COMMON_POOL = "common-pool";
    private static final Map<String, ExecutorService> ALL_THREAD_POOL = new HashMap();

    static {
        ExecutorService executor = new ThreadPoolExecutor(3, Runtime.getRuntime().availableProcessors() * 3
                , 5000L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Common-Pool-" + counter.getAndIncrement());
            }
        });
        ALL_THREAD_POOL.put(COMMON_POOL, executor);


    }

    public static ExecutorService getThreadPool(String pName) {
        return ALL_THREAD_POOL.get(pName);
    }

}
