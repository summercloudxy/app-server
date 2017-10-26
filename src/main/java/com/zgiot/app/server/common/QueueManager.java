package com.zgiot.app.server.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueManager {
    public static final String HIST_BUFFER = "hist_buf";
    private static final Map<String, Queue> ALL_Q = new HashMap();

    static {
        ALL_Q.put(HIST_BUFFER, new LinkedBlockingQueue());
    }

    public static Queue getQueue(String qName){
        return ALL_Q.get(qName);
    }
}

