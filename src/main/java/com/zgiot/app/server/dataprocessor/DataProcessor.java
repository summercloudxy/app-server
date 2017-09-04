package com.zgiot.app.server.dataprocessor;

import java.util.concurrent.CompletableFuture;

public interface DataProcessor {
    CompletableFuture<Void> connect();

    CompletableFuture<Void> disconnect();

    boolean isConnected();

    void addListener(DataListener listener);

    void removeListener(DataListener listener);
}
